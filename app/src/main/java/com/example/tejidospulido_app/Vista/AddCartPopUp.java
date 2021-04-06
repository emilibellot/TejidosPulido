package com.example.tejidospulido_app.Vista;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCartPopUp extends BottomSheetDialogFragment {

    Product product;
    Double quantity;
    String quant;
    Context context;

    EditText quantityInput;

    public static AddCartPopUp newInstance(Product modelToPass, String quantity) {
        AddCartPopUp bottomSheetFragment = new AddCartPopUp();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", modelToPass);
        bundle.putString("quantity", quantity);
        bottomSheetFragment .setArguments(bundle);

        return bottomSheetFragment ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_cart_popup, container, false);
        product = (Product) getArguments().getSerializable("product");
        quant = getArguments().getString("quantity");
        context = getContext();
        setViews(v);
        return v;
    }

    private void setViews(View v) {
        quantityInput = v.findViewById(R.id.quantityInput);
        quantityInput.setText(quant);

        ImageButton plus = v.findViewById(R.id.plusButton);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuantity(true);
            }
        });
        ImageButton minus = v.findViewById(R.id.minusButton);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuantity(false);
            }
        });
        Button add_cart = v.findViewById(R.id.add_cart);
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prod = product.getNumero().substring(0, 4);
                String var = product.getNumero().substring(5, 9);
                FirebaseDatabase.getInstance().getReference("productos").child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product p = snapshot.getValue(Product.class);
                        double q = Double.parseDouble(quantityInput.getText().toString());
                        double s = Double.parseDouble(p.getStock());
                        if (s < q){
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.alert_dialog_accept);
                            dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                            ImageView alertIcon = dialog.findViewById(R.id.alertIcon);
                            alertIcon.setImageResource(R.drawable.ic_alert);
                            TextView title = dialog.findViewById(R.id.titleDialag);
                            title.setText("No hay suficiente estoc.");
                            TextView message = dialog.findViewById(R.id.messageDialag);
                            message.setText("No disponemos de suficientes metros. El estoc actual del producto es: " + p.getStock() + " metros.");

                            Button okey = dialog.findViewById(R.id.okey);
                            okey.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        else{
                            FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("carrito").child(product.getNumero()).setValue(quantityInput.getText().toString());
                            dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void setQuantity(boolean b){
        String q = quantityInput.getText().toString();
        if(!q.isEmpty()) {
            try {
                quantity = Double.parseDouble(q);
                // it means it is double
            } catch (Exception e1) {
                // this means it is not double
                e1.printStackTrace();
            }
        }
        if (b) quantity += 0.05;
        else quantity -= 0.05;
        quantity = (double) Math.round(quantity * 100) / 100;
        quantityInput.setText(String.valueOf(quantity));
    }
}
