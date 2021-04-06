package com.example.tejidospulido_app.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAddressActivity  extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText name, surname, address, cp, city, country;
    ImageButton go_back;
    Button guardar_button, submit;
    Address a;
    String type, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Intent i = getIntent();
        type = i.getStringExtra("type");
        key = i.getStringExtra("key");
        a = (Address) i.getSerializableExtra("address");
        setViews();
    }

    private void setViews() {
        name = (TextInputEditText) findViewById(R.id.name);
        surname = (TextInputEditText) findViewById(R.id.surname);
        address = (TextInputEditText) findViewById(R.id.address);
        cp = (TextInputEditText) findViewById(R.id.cp);
        city = (TextInputEditText) findViewById(R.id.city);
        country = (TextInputEditText) findViewById(R.id.country);
        if (a != null){
            name.setText(a.getNombre());
            surname.setText(a.getApellidos());
            address.setText(a.getDireccion());
            cp.setText(a.getCodigoPostal());
            city.setText(a.getPoblacion());
            country.setText(a.getPais());
        }
        go_back = (ImageButton) findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
        guardar_button = (Button) findViewById(R.id.guardar_button);
        guardar_button.setVisibility(View.GONE);
        submit = (Button) findViewById(R.id.submit);
        submit.setText(R.string.GuardarDireccion);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Address new_address;
        switch (v.getId()) {
            case R.id.go_back:
                finish();
                break;
            case R.id.submit:
                new_address = new Address(name.getText().toString(), surname.getText().toString(), address.getText().toString(), cp.getText().toString(), city.getText().toString(), country.getText().toString());
                if (type.equals("new")) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").push();
                    ref.setValue(new_address);
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").child(key).setValue(new_address);
                }
                finish();
                break;
        }
    }
}
