package com.example.tejidospulido_app.Vista;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tejidospulido_app.R;

public class OrdersActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_menu_button:
                finish();
                break;
        }
    }
}
