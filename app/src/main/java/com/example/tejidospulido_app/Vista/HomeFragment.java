package com.example.tejidospulido_app.Vista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Model.SaveSharedPreference;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterCombinations;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HomeFragment extends Fragment {

    //Variables
    User user;

    TextView wellcome;
    RecyclerView rv_recientes;
    ArrayList<Product> productList;
    ArrayList<String> favoriteList;
    AdapterCombinations adapter;

    public HomeFragment(User user){
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleChargeRecentProducts(getContext());
    }

    private void setViews(View view) {
        productList = new ArrayList<>();
        if (user.getFavoritos() != null){
            favoriteList = new ArrayList<>(user.getFavoritos().values());
        }else{
            favoriteList = new ArrayList<>();
        }

        wellcome = view.findViewById(R.id.wellcome);
        String wellc = "Hola, "+user.getUsername();
        wellcome.setText(wellc);

        rv_recientes = view.findViewById(R.id.rv_recientes);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_recientes.setLayoutManager(lm);
    }

    private void handleChargeRecentProducts(Context context) {
        productList.clear();
        String prod, var;
        Set<String> s = new HashSet<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("recientes", new HashSet<String>()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("productos");
        for (String pr : s){
            prod = pr.substring(0, 4);
            var = pr.substring(5, 9);
            ref.child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Product p = dataSnapshot.getValue(Product.class);
                    productList.add(p);
                    adapter = new AdapterCombinations(context, productList, favoriteList);
                    rv_recientes.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }
}