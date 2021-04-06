package com.example.tejidospulido_app.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Register";

    private EditText email, password;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button register_button = (Button) findViewById(R.id.register);
        TextView back_to_login = (TextView) findViewById(R.id.back_to_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        register_button.setOnClickListener(this);
        back_to_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:
                Signup(email.getText().toString().trim(), password.getText().toString().trim());
                break;
            case R.id.back_to_login:
                Intent intent_up = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent_up);
                finish();
                break;
        }
    }

    private void Signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            User user = new User(email, password);
                            mDatabase.child("usuarios").child(mAuth.getCurrentUser().getUid()).setValue(user);
                            Intent intent_in = new Intent(SignupActivity.this, MainActivity.class);
                            intent_in.putExtra("user", user);
                            startActivity(intent_in);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                onError("Este correo ya tiene una cuenta asociada, porfavor introduce otro correo electrónico o acceda a su cuenta");
                            } catch (FirebaseAuthWeakPasswordException e) {
                                onError("Contraseña incorrecta. Por favor, introduce una contraseña válida. Debe contener más de 6 carácteres alfanuméricos");
                            } catch (Exception e) {
                                onError(task.getException().getMessage());
                            }
                        }

                        // ...
                    }
                });
    }

    public void onError(String error) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(error).setTitle("Error").setCancelable(true);

        AlertDialog alert = builder.create();

        alert.setTitle("Error");
        alert.show();
    }
}