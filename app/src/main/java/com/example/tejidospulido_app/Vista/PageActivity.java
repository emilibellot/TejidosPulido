package com.example.tejidospulido_app.Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Page;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Model.Classes.ProductReference;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterPages;
import com.example.tejidospulido_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Page";

    Page pagina;
    String category;
    ArrayList<Product> productList;
    ArrayList<String> favoriteList;
    RecyclerView pageProducts;
    AdapterPages adapter;
    ImageButton favorites, cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        Intent i = getIntent();
        pagina = (Page)i.getSerializableExtra("page");
        category = i.getStringExtra("category");
        setViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleChargeFavoriteList();
        handleChargeProducts(this);
    }

    private void setViews() {
        //Imatge de l'avatar de l'usuari:
        productList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        TextView pag = (TextView) findViewById(R.id.page);
        pag.setText(pagina.getNombre());
        favorites = (ImageButton) findViewById(R.id.favorites);
        cart = (ImageButton) findViewById(R.id.cart);

        pageProducts = (RecyclerView) findViewById(R.id.rv_page);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getParent(), 2, GridLayoutManager.VERTICAL, false);
        pageProducts.setLayoutManager(gridLayoutManager);

        handleChargeCarrito();
    }

    private void handleChargeCarrito() {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("carrito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handleButton("cart", (int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleChargeFavoriteList() {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    favoriteList.add(data.getValue().toString());
                }
                handleButton("favorites", (int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleButton(String button, int fav) {
        if (fav == 0){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart);
            else this.favorites.setImageResource(R.drawable.ic_favorite);
        }
        else if (fav == 1){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_one);
            else this.favorites.setImageResource(R.drawable.ic_fav_one);
        }
        else if (fav == 2){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_two);
            else this.favorites.setImageResource(R.drawable.ic_fav_two);
        }
        else if (fav == 3){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_three);
            else this.favorites.setImageResource(R.drawable.ic_fav_three);
        }
        else if (fav == 4){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_four);
            else this.favorites.setImageResource(R.drawable.ic_fav_four);
        }
        else if (fav == 5){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_five);
            else this.favorites.setImageResource(R.drawable.ic_fav_five);
        }
        else if (fav == 6){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_six);
            else this.favorites.setImageResource(R.drawable.ic_fav_six);
        }
        else if (fav == 7){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_seven);
            else this.favorites.setImageResource(R.drawable.ic_fav_seven);
        }
        else if (fav == 8){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_eight);
            else this.favorites.setImageResource(R.drawable.ic_fav_eight);
        }
        else if (fav == 9){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_nine);
            else this.favorites.setImageResource(R.drawable.ic_fav_nine);
        }
        else if (fav > 9){
            if(button.equals("cart"))this.cart.setImageResource(R.drawable.ic_cart_more_than_nine);
            else this.favorites.setImageResource(R.drawable.ic_fav_more_than_nine);
        }
    }

    private void handleChargeProducts(Context context) {
        String prod, var;
        productList.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("productos");
        ArrayList<ProductReference> productReferences = pagina.getListOfProducts();
        for (ProductReference pR : productReferences){
            prod = pR.getNumero().substring(0, 4);
            var = pR.getNumero().substring(5, 9);
            ref.child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Product p = dataSnapshot.getValue(Product.class);
                    productList.add(p);
                    adapter = new AdapterPages(context, productList, favoriteList);
                    pageProducts.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(getParent(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        switch (v.getId()) {
            case R.id.back_to_menu_button:
                finish();
                break;
            case R.id.favorites:
                editor = preferences.edit();
                editor.putString("viewPage", "fav");
                editor.apply();
                finish();
                break;
            case R.id.cart:
                editor = preferences.edit();
                editor.putString("viewPage", "car");
                editor.apply();
                finish();
                break;
        }
    }
}
