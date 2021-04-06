package com.example.tejidospulido_app.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Valoration;
import com.example.tejidospulido_app.Model.Classes.Valorations;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterValorations;
import com.example.tejidospulido_app.R;

import java.util.ArrayList;

public class ValorationsActivity extends AppCompatActivity {

    Valorations valorations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorations);
        Intent i = getIntent();
        valorations = (Valorations) i.getSerializableExtra("valoration");
        setViews();
    }

    private void setViews() {
        ImageButton gobackButton = findViewById(R.id.gobackButton);
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<Valoration> val = valorations.getValoraciones();
        val.remove(0);
        RecyclerView rv_valorations = findViewById(R.id.rv_valorations);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_valorations.setLayoutManager(lm);

        AdapterValorations adapter = new AdapterValorations(val);
        rv_valorations.setAdapter(adapter);

    }
}
