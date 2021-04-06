package com.example.tejidospulido_app.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tejidospulido_app.Model.Classes.User;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.SaveSharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private final int DURATION_SPLASH = 8000;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView slogan_1, slogan_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image = (ImageView) findViewById(R.id.logo);
        slogan_1 = (TextView) findViewById(R.id.slogan_1);
        slogan_2 = (TextView) findViewById(R.id.slogan_2);

        image.setAnimation(topAnim);
        slogan_1.setAnimation(bottomAnim);
        slogan_2.setAnimation(bottomAnim);

        if(SaveSharedPreference.getEmail(this).length() != 0) {
            DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User user = dataSnapshot.getValue(User.class);

                    CountDownTimer mCountDownTimer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {}

                        @Override
                        public void onFinish() {
                            Intent sig = new Intent(SplashActivity.this, MainActivity.class);
                            sig.putExtra("user", user);
                            startActivity(sig);
                            finish();
                        }
                    }.start();
                }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });
        }
        else {
            new Handler(Looper.myLooper()).postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, DURATION_SPLASH);
        }
    }
}