package com.example.tejidospulido_app.Presentador.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterOrderAddress extends RecyclerView.Adapter<AdapterOrderAddress.ViewHolder> {

    ArrayList<String> addressesKeyList;
    ArrayList<Address> addresses;
    Context context;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPosition = 0;
    private final CheckBox newAddress;

    String defaultAddress, type;


    public AdapterOrderAddress(Context context, ArrayList<String> addressesKeyList, ArrayList<Address> addresses, String defaultAddress, CheckBox newAddress, String type){
        this.context = context;
        this.addressesKeyList = addressesKeyList;
        this.addresses = addresses;
        this.defaultAddress = defaultAddress;
        this.newAddress = newAddress;
        this.type = type;
    }

    @NonNull
    @Override
    public AdapterOrderAddress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address_order, parent,false);
        return new AdapterOrderAddress.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderAddress.ViewHolder holder, int position) {
        String key = addressesKeyList.get(position);
        Address a = addresses.get(position);
        if (defaultAddress == null && position == 0){
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
            lastChecked = holder.checkBox;
            Log.d("1.", "lastChecked");
            if(type.equals("add")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").setValue(a);
            if(type.equals("fac")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("billingAddress").setValue(a);
        }
        else if (key.equals(defaultAddress)){
            if (position != 0){
                lastChecked.setChecked(false);
                Log.d("2.", "lastChecked");
            }
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
            lastChecked = holder.checkBox;
            lastCheckedPosition = position;
            Log.d("3.", "lastChecked");
        }
        String contact = a.getNombre() + " " + a.getApellidos();
        holder.name.setText(contact.toUpperCase());
        holder.address.setText(a.getDireccion());
        holder.CP.setText(a.getCodigoPostal());
        holder.city.setText(a.getPoblacion());
        holder.country.setText(a.getPais());

        holder.nameInput.setText(a.getNombre());
        holder.surnameInput.setText(a.getApellidos());
        holder.addressInput.setText(a.getDireccion());
        holder.cpInput.setText(a.getCodigoPostal());
        holder.cityInput.setText(a.getPoblacion());
        holder.countryInput.setText(a.getPais());

        holder.saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address newAddress = new Address(holder.nameInput.getText().toString(), holder.surnameInput.getText().toString(), holder.addressInput.getText().toString(), holder.cpInput.getText().toString(), holder.cityInput.getText().toString(), holder.countryInput.getText().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.child("addresses").child(key).setValue(newAddress);
                if (holder.b){
                    if(type.equals("add")) ref.child("address").setValue(key);
                    if(type.equals("fac")) ref.child("billingAddress").setValue(key);
                    holder.checkBox.setChecked(true);
                    holder.checkBox.setClickable(false);
                    defaultAddress = key;
                }
                if(type.equals("add")) ref.child("pedido").child("address").setValue(newAddress);
                if(type.equals("fac")) ref.child("pedido").child("billingAddress").setValue(newAddress);
                holder.hideModify.setVisibility(View.GONE);
                holder.modifyAddress.setVisibility(View.GONE);
                holder.editAddress.setVisibility(View.VISIBLE);
                holder.addressInfo.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if(lastChecked != null){
                        lastChecked.setChecked(false);
                        Log.d("4.", "lastChecked");
                    }
                    if(lastChecked != null){
                        lastChecked.setClickable(true);
                        Log.d("5.", "lastChecked");
                    }
                    if(type.equals("add")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").setValue(a);
                    if(type.equals("fac")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("billingAddress").setValue(a);
                    holder.checkBox.setClickable(false);
                    lastChecked = holder.checkBox;
                    lastCheckedPosition = position;
                    Log.d("6.", "lastChecked");
                    if(newAddress.isChecked()) newAddress.setChecked(false);
                }
                else {
                    if(type.equals("add")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").removeValue();
                    if(type.equals("fac")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("billingAddress").removeValue();
                    lastChecked.setClickable(true);
                    lastChecked = null;
                    lastCheckedPosition = 0;
                    Log.d("7.", "lastChecked");
                    holder.hideModify.setVisibility(View.GONE);
                    holder.modifyAddress.setVisibility(View.GONE);
                    holder.editAddress.setVisibility(View.VISIBLE);
                    holder.addressInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.setDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.b = isChecked;
            }
        });

        holder.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editAddress.setVisibility(View.GONE);
                holder.addressInfo.setVisibility(View.GONE);
                holder.hideModify.setVisibility(View.VISIBLE);
                holder.modifyAddress.setVisibility(View.VISIBLE);
                if(lastChecked != null && lastCheckedPosition != position) {
                    lastChecked.setChecked(false);
                    Log.d("7.", "lastChecked");
                }
                holder.checkBox.setChecked(true);
                holder.checkBox.setClickable(false);
                Log.d("8.", "lastChecked");
                lastChecked = holder.checkBox;
                lastCheckedPosition = position;
                /*Intent intent_up  = new Intent(context, NewAddressActivity.class);
                intent_up.putExtra("address", a);
                intent_up.putExtra("key", key);
                intent_up.putExtra("type", "edit");
                context.startActivity(intent_up);*/
            }
        });

        holder.hideModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.hideModify.setVisibility(View.GONE);
                holder.modifyAddress.setVisibility(View.GONE);
                holder.editAddress.setVisibility(View.VISIBLE);
                holder.addressInfo.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, CP, city, country;
        TextInputEditText nameInput, surnameInput, addressInput, cpInput, cityInput, countryInput;
        CheckBox checkBox, setDefault;
        Button saveAddress;
        ImageButton editAddress, hideModify;
        RelativeLayout addressInfo;
        LinearLayout modifyAddress;

        boolean b;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressInfo = itemView.findViewById(R.id.addressInfo);
            modifyAddress = itemView.findViewById(R.id.modifyAddress);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            CP = itemView.findViewById(R.id.CP);
            city = itemView.findViewById(R.id.city);
            country = itemView.findViewById(R.id.country);

            nameInput = itemView.findViewById(R.id.nameInput);
            surnameInput = itemView.findViewById(R.id.surnameInput);
            addressInput = itemView.findViewById(R.id.addressInput);
            cpInput = itemView.findViewById(R.id.cpInput);
            cityInput = itemView.findViewById(R.id.cityInput);
            countryInput = itemView.findViewById(R.id.countryInput);
            saveAddress = itemView.findViewById(R.id.saveAddress);

            editAddress = itemView.findViewById(R.id.editAddress);
            hideModify = itemView.findViewById(R.id.hideModify);

            checkBox = itemView.findViewById(R.id.checkbox);
            setDefault = itemView.findViewById(R.id.setDefault);

            b = false;
        }
    }
}

