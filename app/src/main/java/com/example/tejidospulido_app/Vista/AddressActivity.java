package com.example.tejidospulido_app.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.Model.Classes.User;
import com.example.tejidospulido_app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText name, surname, address, cp, city, country;
    ImageButton go_back;
    Button guardar_button, submit;
    Address a;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Intent i = getIntent();
        type = i.getStringExtra("type");
        a = (Address) i.getSerializableExtra("address");
        setViews();
    }

    private void setViews() {
        name = (TextInputEditText) findViewById(R.id.name);
        name.setText(a.getNombre());
        surname = (TextInputEditText) findViewById(R.id.surname);
        surname.setText(a.getApellidos());
        address = (TextInputEditText) findViewById(R.id.address);
        address.setText(a.getDireccion());
        cp = (TextInputEditText) findViewById(R.id.cp);
        cp.setText(a.getCodigoPostal());
        city = (TextInputEditText) findViewById(R.id.city);
        city.setText(a.getPoblacion());
        country = (TextInputEditText) findViewById(R.id.country);
        country.setText(a.getPais());

        go_back = (ImageButton) findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
        guardar_button = (Button) findViewById(R.id.guardar_button);
        guardar_button.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Address new_address;
        switch (v.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.guardar_button:
                new_address = new Address(name.getText().toString(), surname.getText().toString(), address.getText().toString(), cp.getText().toString(), city.getText().toString(), country.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue(new_address);
                break;
            case R.id.submit:
                new_address = new Address(name.getText().toString(), surname.getText().toString(), address.getText().toString(), cp.getText().toString(), city.getText().toString(), country.getText().toString());
                if (type.equals("envio")) FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").setValue(new_address);
                else FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("billingAddress").setValue(new_address);
                finish();
                break;
        }
    }
}
