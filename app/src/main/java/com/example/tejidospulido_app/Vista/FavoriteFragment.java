package com.example.tejidospulido_app.Vista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterFavorites;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterPages;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private User user;
    private ArrayList<Product> productList;
    private RecyclerView favoriteProducts;
    private LinearLayoutManager lm;
    private AdapterFavorites adapter;

    public FavoriteFragment(User user){
        this.user = user;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
    }

    private void setViews(View view) {
        productList = new ArrayList<>();

        favoriteProducts = (RecyclerView) view.findViewById(R.id.rv_favorites);
        favoriteProducts.addItemDecoration(new DividerItemDecoration(favoriteProducts.getContext(), DividerItemDecoration.VERTICAL));
        lm = new LinearLayoutManager(getContext());
        favoriteProducts.setLayoutManager(lm);

        handleChargeFavoriteList(getContext());
    }

    private void handleChargeFavoriteList(Context context) {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String prod = data.getValue().toString().substring(0, 4);
                    String var = data.getValue().toString().substring(5, 9);
                    FirebaseDatabase.getInstance().getReference().child("productos").child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Product p = snapshot.getValue(Product.class);
                            productList.add(p);
                            adapter = new AdapterFavorites(context, productList);
                            favoriteProducts.setAdapter(adapter);
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
}
