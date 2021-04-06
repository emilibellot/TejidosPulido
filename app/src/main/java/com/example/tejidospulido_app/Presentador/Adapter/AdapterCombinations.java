package com.example.tejidospulido_app.Presentador.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.Vista.ProductActivity;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AdapterCombinations extends RecyclerView.Adapter<AdapterCombinations.ViewHolder> {

    ArrayList<Product> productList;
    ArrayList<String> favoriteList;
    Context context;

    public AdapterCombinations (Context context, ArrayList<Product> productList, ArrayList<String> favoriteList){
        this.context = context;
        this.productList = productList;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public AdapterCombinations.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comb, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCombinations.ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.productName.setText(p.getNombre());
        String precio = p.getPrecio() + " €";
        holder.productPrice.setText(precio);
        Glide.with(holder.itemView).load(Uri.parse(p.getImagen())).into(holder.productImage);
        if (favoriteList.contains(p.getNumero())){
            holder.add_favorite.setImageResource(R.drawable.ic_fav);
            holder.favorite = true;
        }else{
            holder.add_favorite.setImageResource(R.drawable.ic_favorite);
            holder.favorite = false;
        }
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

        holder.add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.favorite){
                    FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(p.getNumero()).removeValue();
                    holder.add_favorite.setImageResource(R.drawable.ic_favorite);
                    holder.favorite = false;
                }else{
                    FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(p.getNumero()).setValue(p.getNumero());
                    holder.add_favorite.setImageResource(R.drawable.ic_fav);
                    holder.favorite = true;
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        RelativeLayout nameLayout;
        ImageButton add_favorite;

        boolean favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            add_favorite = itemView.findViewById(R.id.add_favorite);
            nameLayout = itemView.findViewById(R.id.nameLayout);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
