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

public class AdapterPayProduct extends RecyclerView.Adapter<AdapterPayProduct.ViewHolder> {

    ArrayList<Product> productList;
    ArrayList<String> quantityList;
    Context context;


    public AdapterPayProduct(Context context, ArrayList<Product> productList, ArrayList<String> quantityList){
        this.context = context;
        this.productList = productList;
        this.quantityList = quantityList;
    }

    @NonNull
    @Override
    public AdapterPayProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pay_prod, parent,false);
        return new AdapterPayProduct.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPayProduct.ViewHolder holder, int position) {
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, quantity, totalPrice;
        ImageView productImage;
        ImageButton delete;
        RelativeLayout nameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            delete = itemView.findViewById(R.id.delete);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            nameLayout = itemView.findViewById(R.id.nameLayout);
        }
    }
}

