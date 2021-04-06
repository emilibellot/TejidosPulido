package com.example.tejidospulido_app.Vista;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PersonalDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_INTENT = 1;

    User user;

    TextInputEditText full_name, email, phoneNumber;

    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");
        setViews();
    }

    private void setViews() {
        profileImage = (ImageView) findViewById(R.id.profileImage);
        if (user.getImage().equals("")) Glide.with(this).load(R.drawable.ic_profile).into(profileImage);
        else Glide.with(this).load(Uri.parse(user.getImage())).into(profileImage);
        full_name = (TextInputEditText) findViewById(R.id.full_name);
        full_name.setText(user.getUsername());
        email = (TextInputEditText) findViewById(R.id.email);
        email.setText(user.getEmail());
        phoneNumber = (TextInputEditText) findViewById(R.id.phoneNumber);
        phoneNumber.setText(user.getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_to_menu_button:
                finish();
                break;
            case R.id.guardar_button:
                saveData();
                break;
            case R.id.profileImage:
                if (checkPermissionREAD_EXTERNAL_STORAGE(PersonalDataActivity.this)) openGallery();
                break;
        }
    }

    private void saveData() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (!user.getImage().equals("")) ref.child("image").setValue(user.getImage());
        ref.child("username").setValue(full_name.getText().toString());
        ref.child("email").setValue(email.getText().toString());
        ref.child("phone").setValue(phoneNumber.getText().toString());
        finish();
    }

    //Funcio per obrir la galeria i seleccionar la nova imatge d'acatar de l'usuari:
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(gallery, GALLERY_INTENT);
    }

    @Override
    //Funcio que assigna la imatge seleccionada com a nova imatge d'acatar de l'usuari:
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            handleImage(uri);
        }
    }

    //Codi per demanar permis a l'usuari
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Permiso necesario", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(msg);
        alertBuilder.setMessage("Se requiere su permiso para acceder a la galería de imágenes.");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void handleImage(Uri uri) {
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(uri.getLastPathSegment());
        UploadTask uploadTask = filePath.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    onSuccesImageChange(downloadUri);
                }
            }
        });
    }

    private void onSuccesImageChange(Uri downloadUri) {
        Glide.with(this).load(downloadUri.toString()).into(profileImage);
        profileImage.setImageURI(downloadUri);
        user.setImage(downloadUri.toString());
    }
}
