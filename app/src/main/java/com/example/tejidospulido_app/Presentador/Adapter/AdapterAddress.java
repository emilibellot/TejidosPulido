package com.example.tejidospulido_app.Presentador.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Vista.NewAddressActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.ViewHolder> {

    ArrayList<String> addressesKeyList;
    ArrayList<Address> addresses;
    String shippingAddress, billingAddress;
    Context context;


    public AdapterAddress(Context context, ArrayList<String> addressesKeyList, ArrayList<Address> addresses, String shippingAddress, String billingAddress){
        this.context = context;
        this.addressesKeyList = addressesKeyList;
        this.addresses = addresses;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    @NonNull
    @Override
    public AdapterAddress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address, parent,false);
        return new AdapterAddress.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddress.ViewHolder holder, int position) {
        String key = addressesKeyList.get(position);
        Address a = addresses.get(position);
        String contact = a.getNombre() + " " + a.getApellidos();
        holder.name.setText(contact.toUpperCase());
        holder.address.setText(a.getDireccion());
        holder.CP.setText(a.getCodigoPostal());
        holder.city.setText(a.getPoblacion());

        holder.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_up  = new Intent(context, NewAddressActivity.class);
                intent_up.putExtra("address", a);
                intent_up.putExtra("key", key);
                intent_up.putExtra("type", "edit");
                context.startActivity(intent_up);
            }
        });

        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                ImageView alertIcon = dialog.findViewById(R.id.alertIcon);
                alertIcon.setImageResource(R.drawable.ic_delete);
                TextView title = dialog.findViewById(R.id.titleDialag);
                title.setText("¿Estás seguro?");
                TextView message = dialog.findViewById(R.id.messageDialag);
                message.setText("Se eliminará la dirección seleccionada.");

                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button okey = dialog.findViewById(R.id.okey);
                okey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (key.equals(shippingAddress)){
                            FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue("");
                            shippingAddress = "";
                        }
                        if (key.equals(billingAddress)){
                            FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("billingAddress").setValue("");
                            billingAddress = "";
                        }
                        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").child(key).removeValue();
                        dialog.dismiss();
                        removeAt(position);
                    }
                });
                dialog.show();
            }
        });

        if(shippingAddress.equals(key))holder.shipping_address_checkbox.setChecked(true);
        else holder.shipping_address_checkbox.setChecked(false);

        if(billingAddress.equals(key))holder.billing_address_checkbox.setChecked(true);
        else holder.billing_address_checkbox.setChecked(false);

        holder.shipping_address_checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue(key);
                    shippingAddress = key;
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue("");
                    shippingAddress = "";
                }
            }
        });

        holder.billing_address_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("billingAddress").setValue(key);
                    billingAddress = key;
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("billingAddress").setValue("");
                    billingAddress = "";
                }
            }
        });
    }

    public void removeAt(int position) {
        addresses.remove(position);
        addressesKeyList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addresses.size());
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, CP, city;
        CheckBox billing_address_checkbox, shipping_address_checkbox;
        ImageButton editAddress, deleteAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            CP = itemView.findViewById(R.id.CP);
            city = itemView.findViewById(R.id.city);
            editAddress = itemView.findViewById(R.id.editAddress);
            deleteAddress = itemView.findViewById(R.id.deleteAddress);
            billing_address_checkbox = itemView.findViewById(R.id.billing_address_checkbox);
            shipping_address_checkbox = itemView.findViewById(R.id.shipping_address_checkbox);
        }
    }
}

