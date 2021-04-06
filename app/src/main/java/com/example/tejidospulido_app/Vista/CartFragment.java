package com.example.tejidospulido_app.Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.Model.Classes.Order;
import com.example.tejidospulido_app.Model.Classes.OrderLine;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterCart;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterFavorites;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class CartFragment extends Fragment implements View.OnClickListener {

    private User user;
    private ArrayList<Product> productList;
    private ArrayList<OrderLine> orderLineList;
    private ArrayList<String> quantityList;
    private RecyclerView cartProducts;
    private LinearLayoutManager lm;
    private AdapterCart adapter;
    Button startOrder;
    TextView totalPrice;
    BigDecimal total;

    public CartFragment(User user){
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        total = new BigDecimal("0.0");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setViews(View view) {
        productList = new ArrayList<>();
        orderLineList = new ArrayList<>();
        quantityList = new ArrayList<>();
        totalPrice = view.findViewById(R.id.totalPrice);
        startOrder = (Button) view.findViewById(R.id.startOrder);
        startOrder.setOnClickListener(this);

        cartProducts = (RecyclerView) view.findViewById(R.id.rv_carrito);
        cartProducts.addItemDecoration(new DividerItemDecoration(cartProducts.getContext(), DividerItemDecoration.VERTICAL));
        lm = new LinearLayoutManager(getContext());
        cartProducts.setLayoutManager(lm);

        handleChargeCartList(getContext());
    }

    private void handleChargeCartList(Context context) {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("carrito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                quantityList.clear();
                total = new BigDecimal("0.0");
                final int[] count = {1};
                int n = (int) snapshot.getChildrenCount();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String prod = data.getKey().substring(0, 4);
                    String var = data.getKey().substring(5, 9);
                    FirebaseDatabase.getInstance().getReference().child("productos").child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Product p = snapshot.getValue(Product.class);
                            OrderLine o = new OrderLine(p.getNumero(), data.getValue().toString(), p.getPrecio());
                            productList.add(p);
                            orderLineList.add(o);
                            quantityList.add(data.getValue().toString());
                            adapter = new AdapterCart(context, productList, quantityList);
                            cartProducts.setAdapter(adapter);
                            String tP = handleTotalPrice(data.getValue().toString(), p.getPrecio());
                            if(count[0] == n){
                                totalPrice.setText(tP);
                                Log.d("HSAHDSAHSA", String.valueOf(tP));
                            }
                            else ++count[0];
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String handleTotalPrice(String q, String p) {
        BigDecimal aux = (new BigDecimal(q).multiply(new BigDecimal(p))).setScale(2, BigDecimal.ROUND_UP);
        total = total.add(aux);
        return total + " €";
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.startOrder:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("viewPage", "cart");
                editor.apply();
                Map<String, Address> addresses = user.getAddresses();
                Address address = new Address();
                Address billingAddress = new Address();
                if (addresses != null){
                    address = addresses.get(user.getAddress());
                    billingAddress = addresses.get(user.getBillingAddress());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                String date = sdf.format(c.getTime());
                Order o = new Order(user.getUsername(), orderLineList, String.valueOf(total), address, billingAddress, date);
                o.setPhone(user.getPhone());
                FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").setValue(o);
                intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
        }
    }
}
