package com.example.tejidospulido_app.Vista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterAddress;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.SaveSharedPreference;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Profile";
    final Context context = this;

    private RecyclerView rv_addresses;
    private LinearLayoutManager lm;
    private AdapterAddress adapter;
    ArrayList<String> addressesKeyList;
    ArrayList<Address> addressesList;

    User user;
    ImageView profileImage;
    TextView full_name, email;

    ValueEventListener valueEventListener;
    DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");
        findViews();

        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                setViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressesKeyList = new ArrayList<>();
                addressesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Address a = dataSnapshot.getValue(Address.class);
                    addressesKeyList.add(dataSnapshot.getKey());
                    addressesList.add(a);
                    setAddressess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void findViews() {
        profileImage = (ImageView) findViewById(R.id.profileImage);
        full_name = (TextView) findViewById(R.id.full_name);
        email = (TextView) findViewById(R.id.email);
        rv_addresses = findViewById(R.id.rv_addresses);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_addresses.setLayoutManager(lm);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setAddressess() {
        adapter = new AdapterAddress(this, addressesKeyList, addressesList, user.getAddress(), user.getBillingAddress());
        rv_addresses.setAdapter(adapter);
    }

    private void setViews() {
        if (isValidContextForGlide(context)){
            if (user.getImage().equals("")) Glide.with(context).load(R.drawable.ic_profile).into(profileImage);
            else Glide.with(context).load(Uri.parse(user.getImage())).into(profileImage);
        }
        full_name.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent_up;
        switch (v.getId())
        {
            case R.id.back_to_menu_button:
                finish();
                break;
            case R.id.edit:
                intent_up = new Intent(ProfileActivity.this, PersonalDataActivity.class);
                intent_up.putExtra("user", user);
                startActivity(intent_up);
                break;
            case R.id.addAddress:
                intent_up = new Intent(ProfileActivity.this, NewAddressActivity.class);
                intent_up.putExtra("type", "new");
                startActivity(intent_up);
                break;
            case R.id.logout:
                SaveSharedPreference.clearEmail(this);
                FirebaseAuth.getInstance().signOut();
                intent_up = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent_up);
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDbRef != null && valueEventListener != null)
            mDbRef.removeEventListener(valueEventListener);
    }
}

