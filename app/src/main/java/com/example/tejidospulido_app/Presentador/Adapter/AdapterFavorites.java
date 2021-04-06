package com.example.tejidospulido_app.Presentador.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Vista.AddCartPopUp;
import com.example.tejidospulido_app.Vista.ProductActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterFavorites extends RecyclerView.Adapter<AdapterFavorites.ViewHolder> {

    ArrayList<Product> productList;
    Context context;


    public AdapterFavorites (Context context, ArrayList<Product> productList){
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public AdapterFavorites.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prod, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorites.ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.productName.setText(p.getNombre());
        String precio = p.getPrecio() + " €";
        holder.productPrice.setText(precio);
        Glide.with(holder.itemView).load(Uri.parse(p.getImagen())).into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProduct(p);
            }
        });

        holder.nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProduct(p);
            }
        });

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.options_favorites);
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                Button similarArticles = dialog.findViewById(R.id.similarArticles);
                Button deleteCart = dialog.findViewById(R.id.deleteCart);

                similarArticles.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                deleteCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(p.getNumero()).removeValue();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(p);
            }
        });
    }

    private void addCart(Product p) {
        AddCartPopUp addCartPopUp = new AddCartPopUp();
        FragmentTransaction transaction = ((FragmentActivity) context)
                .getSupportFragmentManager()
                .beginTransaction();
        AddCartPopUp.newInstance(p, "0.3").show(transaction, "add_cart");
    }

    private void goToProduct(Product p) {
        Intent intent_up  = new Intent(context, ProductActivity.class);
        intent_up.putExtra("product", p);
        context.startActivity(intent_up);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        ImageButton options, add_cart, add_favorite;
        LinearLayout nameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            options = itemView.findViewById(R.id.options);
            add_cart = itemView.findViewById(R.id.add_cart);
            add_favorite = itemView.findViewById(R.id.add_favorite);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            nameLayout = itemView.findViewById(R.id.nameLayout);
        }
    }
}
