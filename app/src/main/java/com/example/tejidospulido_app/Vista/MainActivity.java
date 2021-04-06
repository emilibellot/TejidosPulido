package com.example.tejidospulido_app.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.example.tejidospulido_app.Presentador.Adapter.ViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "Main";

    //Variables
    User user;

    ViewPagerAdapter mAdapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView profile_button, avatar;
    TextView full_name;
    View headerLayout;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");

        //Set Widgets
        viewPager2 = findViewById(R.id.pager);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        profile_button = findViewById(R.id.profile_button);

        headerLayout = navigationView.getHeaderView(0); // 0-index header

        avatar = headerLayout.findViewById(R.id.avatar);
        full_name = headerLayout.findViewById(R.id.full_name);

        if (user.getImage() == null || user.getImage().equals("")) Glide.with(MainActivity.this).load(R.drawable.ic_profile).into(avatar);
        else Glide.with(MainActivity.this).load(Uri.parse(user.getImage())).into(avatar);
        full_name.setText(user.getUsername());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        //AddFragment
        mAdapter.addFragment(new HomeFragment(user));     //0
        mAdapter.addFragment(new MenuFragment(user));     //1
        mAdapter.addFragment(new FavoriteFragment(user)); //2
        mAdapter.addFragment(new CartFragment(user));     //3

        viewPager2.setAdapter(mAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        configurePage();
        navigationDrawer();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.putString("viewPage", "home");
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = new User();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDbRef = mDatabase.getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.getValue(User.class);
                if (user.getImage().equals("")) Glide.with(MainActivity.this).load(R.drawable.ic_profile).into(avatar);
                else Glide.with(MainActivity.this).load(Uri.parse(user.getImage())).into(avatar);
                full_name.setText(user.getUsername());
                configurePage();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        String current_view = this.preferences.getString("viewPage", "home");
        if(current_view.equals("fav")){
            viewPager2.setCurrentItem(2, true);
        }
        else if (current_view.equals("car")){
            viewPager2.setCurrentItem(3, true);
        }
    }

    private void configurePage(){
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_home);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_menu);
                        break;
                    case 2:
                        if (user.getFavoritos() == null){
                            tab.setIcon(R.drawable.ic_favorite);
                        }
                        else if (user.getFavoritos().size() == 1){
                            tab.setIcon(R.drawable.ic_fav_one);
                        }
                        else if (user.getFavoritos().size() == 2){
                            tab.setIcon(R.drawable.ic_fav_two);
                        }
                        else if (user.getFavoritos().size() == 3){
                            tab.setIcon(R.drawable.ic_fav_three);
                        }
                        else if (user.getFavoritos().size() == 4){
                            tab.setIcon(R.drawable.ic_fav_four);
                        }
                        else if (user.getFavoritos().size() == 5){
                            tab.setIcon(R.drawable.ic_fav_five);
                        }
                        else if (user.getFavoritos().size() == 6){
                            tab.setIcon(R.drawable.ic_fav_six);
                        }
                        else if (user.getFavoritos().size() == 7){
                            tab.setIcon(R.drawable.ic_fav_seven);
                        }
                        else if (user.getFavoritos().size() == 8){
                            tab.setIcon(R.drawable.ic_fav_eight);
                        }
                        else if (user.getFavoritos().size() == 9){
                            tab.setIcon(R.drawable.ic_fav_nine);
                        }
                        else if (user.getFavoritos().size() > 9){
                            tab.setIcon(R.drawable.ic_fav_more_than_nine);
                        }
                        break;
                    case 3:
                        if (user.getCarrito() == null){
                            tab.setIcon(R.drawable.ic_cart);
                        }
                        else if (user.getCarrito().size() == 1){
                            tab.setIcon(R.drawable.ic_cart_one);
                        }
                        else if (user.getCarrito().size() == 2){
                            tab.setIcon(R.drawable.ic_cart_two);
                        }
                        else if (user.getCarrito().size() == 3){
                            tab.setIcon(R.drawable.ic_cart_three);
                        }
                        else if (user.getCarrito().size() == 4){
                            tab.setIcon(R.drawable.ic_cart_four);
                        }
                        else if (user.getCarrito().size() == 5){
                            tab.setIcon(R.drawable.ic_cart_five);
                        }
                        else if (user.getCarrito().size() == 6){
                            tab.setIcon(R.drawable.ic_cart_six);
                        }
                        else if (user.getCarrito().size() == 7){
                            tab.setIcon(R.drawable.ic_cart_seven);
                        }
                        else if (user.getCarrito().size() == 8){
                            tab.setIcon(R.drawable.ic_cart_eight);
                        }
                        else if (user.getCarrito().size() == 9){
                            tab.setIcon(R.drawable.ic_cart_nine);
                        }
                        else if (user.getCarrito().size() > 9){
                            tab.setIcon(R.drawable.ic_cart_more_than_nine);
                        }
                        break;
                }
            }
        }).attach();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_orders:
                intent = new Intent(this, OrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case R.id.nav_options:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}