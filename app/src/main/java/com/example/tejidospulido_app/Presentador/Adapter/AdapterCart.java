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
import android.widget.RelativeLayout;
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

import java.math.BigDecimal;
import java.util.ArrayList;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    ArrayList<Product> productList;
    ArrayList<String> quantityList;
    Context context;


    public AdapterCart(Context context, ArrayList<Product> productList, ArrayList<String> quantityList){
        this.context = context;
        this.productList = productList;
        this.quantityList = quantityList;
    }

    @NonNull
    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shop_cart, parent,false);
        return new AdapterCart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ViewHolder holder, int position) {
        Product p = productList.get(position);
        String q = quantityList.get(position);
        Glide.with(holder.itemView).load(Uri.parse(p.getImagen())).into(holder.productImage);
        holder.productName.setText(p.getNombre());
        String price = p.getPrecio() + " €";
        holder.productPrice.setText(price);
        String quantity = q + " metros";
        holder.quantity.setText(quantity);
        BigDecimal qu = new BigDecimal(q);
        BigDecimal pr = new BigDecimal(p.getPrecio());
        BigDecimal total = qu.multiply(pr).setScale(2, BigDecimal.ROUND_UP);
        String totalPrice = total + " €";
        holder.totalPrice.setText(totalPrice);

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
                dialog.setContentView(R.layout.options_cart);
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                Button setQuantity = dialog.findViewById(R.id.setQuantity);
                Button addFavorites = dialog.findViewById(R.id.addFavorites);
                Button deleteCart = dialog.findViewById(R.id.deleteCart);

                setQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        addCart(p, q);
                    }
                });
                addFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(p.getNumero()).setValue(p.getNumero());
                        dialog.dismiss();
                    }
                });
                deleteCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("carrito").child(p.getNumero()).removeValue();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void addCart(Product p, String quantity) {
        AddCartPopUp addCartPopUp = new AddCartPopUp();
        FragmentTransaction transaction = ((FragmentActivity) context)
                .getSupportFragmentManager()
                .beginTransaction();
        addCartPopUp.newInstance(p, quantity).show(transaction, "add_cart");
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

        TextView productName, productPrice, quantity, totalPrice;
        ImageView productImage;
        ImageButton options;
        RelativeLayout nameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            options = itemView.findViewById(R.id.options);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            nameLayout = itemView.findViewById(R.id.nameLayout);
        }
    }
}

