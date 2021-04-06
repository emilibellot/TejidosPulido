package com.example.tejidospulido_app.Vista;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterCombinations;
import com.example.tejidospulido_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    Product product;

    ImageView productImage, displayDescriptionButton, displayUtilityButton, valoration, combinacionesButton;
    TextView descriptionTitle, description, utilityTitle, utility, textValoration, productValoration, totalValoration;
    EditText quantityInput;
    LinearLayout layoutDescription, layoutUtility;
    RelativeLayout valorationButton, combinacionesLayout;
    ImageButton add_favorite1, add_favorite2;

    RecyclerView rv_combinations;
    AdapterCombinations adapter;
    ArrayList<Product> productList;
    ArrayList<String> favoriteList;

    BigDecimal quantity, aggregation;
    String q;
    boolean displayedDes, displayedUt, favorite;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");
        context = this;
        setViews();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Set<String> oldSet = sharedPrefs.getStringSet("recientes", new HashSet<String>());

        //make a copy, update it and save it
        Set<String> newStrSet = new HashSet<String>();
        newStrSet.add(product.getNumero());
        newStrSet.addAll(oldSet);

        editor.putStringSet("recientes",newStrSet);
        editor.apply();
    }

    private void setViews() {
        productList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        TextView prod = (TextView) findViewById(R.id.product);
        prod.setText(product.getNombre());
        productImage = findViewById(R.id.productImage);
        Glide.with(this).load(Uri.parse(product.getImagen())).into(productImage);
        TextView name = (TextView) findViewById(R.id.productName);
        name.setText(product.getNombre());
        TextView price = (TextView) findViewById(R.id.productPrice);
        String precio = product.getPrecio() + " €";
        price.setText(precio);
        add_favorite1 = (ImageButton) findViewById(R.id.add_favorite1);
        add_favorite2 = (ImageButton) findViewById(R.id.add_favorite2);

        handleFavorite();

        quantityInput = findViewById(R.id.quantityInput);
        quantity = new BigDecimal("0.30");
        aggregation = new BigDecimal("0.05");

        //DETAILS AND DESCRIPTION
        displayedDes = false;
        displayedUt = false;
        displayDescriptionButton = findViewById(R.id.displayDescriptionButton);
        displayDescriptionButton.setOnClickListener(this);

        displayUtilityButton = findViewById(R.id.displayUtilityButton);
        displayUtilityButton.setOnClickListener(this);

        descriptionTitle = findViewById(R.id.descriptionTitle);
        descriptionTitle.setOnClickListener(this);

        description = findViewById(R.id.description);
        description.setText(product.getDescripcion());

        utilityTitle = findViewById(R.id.utilityTitle);
        utilityTitle.setOnClickListener(this);

        utility = findViewById(R.id.utility);
        utility.setText(product.getFunciones());

        layoutDescription = findViewById(R.id.layoutDescription);
        layoutUtility = findViewById(R.id.layoutUtility);

        valorationButton = findViewById(R.id.valorationButton);
        textValoration = findViewById(R.id.textValoration);
        productValoration = findViewById(R.id.productValoration);
        totalValoration = findViewById(R.id.totalValoration);
        valoration = findViewById(R.id.valoration);
        handleValoration();


        handleChargeFavoriteList();

        combinacionesLayout = findViewById(R.id.combinacionesLayout);
        combinacionesButton = findViewById(R.id.combinacionesButton);
        rv_combinations = (RecyclerView) findViewById(R.id.rv_combinations);
        if (product.getCombinaciones() == null){
            combinacionesLayout.setVisibility(View.GONE);
            rv_combinations.setVisibility(View.GONE);
        }
        else{
            combinacionesButton.setOnClickListener(this);
            LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rv_combinations.setLayoutManager(lm);
            handleCombinations();
        }
    }

    private void handleChargeFavoriteList() {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    favoriteList.add(data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleCombinations() {
        String prod, var;
        ArrayList<Product> productList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("productos");
        for (String pr : product.getCombinaciones().values()){
            prod = pr.substring(0, 4);
            var = pr.substring(5, 9);
            ref.child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Product p = dataSnapshot.getValue(Product.class);
                    productList.add(p);
                    adapter = new AdapterCombinations(context, productList, favoriteList);
                    rv_combinations.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(getParent(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void handleValoration() {
        if (product.getValoracion().getValoraciones().size() == 0){
            textValoration.setVisibility(View.VISIBLE);
            productValoration.setVisibility(View.GONE);
            totalValoration.setVisibility(View.GONE);
            valoration.setVisibility(View.GONE);
        }
        else{
            valorationButton.setOnClickListener(this);
            textValoration.setVisibility(View.GONE);
            String val = product.getValoracion().getValoracion();
            BigDecimal valora = new BigDecimal(val).setScale(0, RoundingMode.HALF_UP);
            int v = valora.intValue();
            if (v == 0 || v == 1){
                productValoration.setText("1");
                valoration.setImageResource(R.drawable.ic_rating1);
            }
            else if (v == 2){
                productValoration.setText("2");
                valoration.setImageResource(R.drawable.ic_rating2);
            }
            else if (v == 3){
                productValoration.setText("3");
                valoration.setImageResource(R.drawable.ic_rating3);
            }
            else if (v == 4){
                productValoration.setText("4");
                valoration.setImageResource(R.drawable.ic_rating4);
            }
            else if (v == 5){
                productValoration.setText("5");
                valoration.setImageResource(R.drawable.ic_rating5);
            }
            productValoration.setVisibility(View.VISIBLE);
            totalValoration.setVisibility(View.VISIBLE);
            valoration.setVisibility(View.VISIBLE);
        }
    }

    private void handleFavorite() {
        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> favoritos = (Map<String, String>) snapshot.getValue();
                if (favoritos != null && favoritos.containsKey(product.getNumero())) {
                    add_favorite1.setImageResource(R.drawable.ic_fav);
                    add_favorite2.setImageResource(R.drawable.ic_fav);
                    favorite = true;
                }
                else{
                    add_favorite1.setImageResource(R.drawable.ic_favorite);
                    add_favorite2.setImageResource(R.drawable.ic_favorite);
                    favorite = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent_up;
        switch (v.getId()) {
            case R.id.back_to_page_button:
                finish();
                break;
            case R.id.add_cart1:
            case R.id.add_cart2:
                FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("carrito").child(product.getNumero()).setValue(quantityInput.getText().toString());
                break;
            case R.id.add_favorite1:
            case R.id.add_favorite2:
                if (favorite){
                    FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(product.getNumero()).removeValue();
                    add_favorite1.setImageResource(R.drawable.ic_favorite);
                    add_favorite2.setImageResource(R.drawable.ic_favorite);
                    favorite = false;
                }
                else{
                    FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(product.getNumero()).setValue(product.getNumero());
                    add_favorite1.setImageResource(R.drawable.ic_fav);
                    add_favorite2.setImageResource(R.drawable.ic_fav);
                    favorite = true;
                }
                break;
            case R.id.plusButton:
                q = quantityInput.getText().toString();
                if(!q.isEmpty()) {
                    try {
                        quantity = new BigDecimal(q);
                        // it means it is double
                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }
                }

                quantity = quantity.add(aggregation);
                quantityInput.setText(String.valueOf(quantity));
                break;
            case R.id.minusButton:
                q = quantityInput.getText().toString();
                if(!q.isEmpty()) {
                    try {
                        quantity = new BigDecimal(q);
                        // it means it is double
                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }
                }
                quantity = quantity.subtract(aggregation);
                quantityInput.setText(String.valueOf(quantity));
                break;
            case R.id.valorationButton:
                intent_up  = new Intent(context, ValorationsActivity.class);
                intent_up.putExtra("valoration", product.getValoracion());
                context.startActivity(intent_up);
                break;
            case R.id.displayDescriptionButton:
            case R.id.descriptionTitle:
                if (displayedDes){
                    handleAnimation(displayDescriptionButton, displayedDes);
                    hideDescription();
                    displayedDes = false;
                }else {
                    handleAnimation(displayDescriptionButton, displayedDes);
                    displayDescription();
                    displayedDes = true;
                }
                break;
            case R.id.displayUtilityButton:
            case R.id.utilityTitle:
                if (displayedUt){
                    handleAnimation(displayUtilityButton, displayedUt);
                    hideUtility();
                    displayedUt = false;
                }else {
                    handleAnimation(displayUtilityButton, displayedUt);
                    displayUtility();
                    displayedUt = true;
                }
                break;
            case R.id.combinacionesButton:
                intent_up  = new Intent(context, CombinationActivity.class);
                ArrayList<String> listCombinations = new ArrayList<>(product.getCombinaciones().values());
                intent_up.putStringArrayListExtra("combinaciones", listCombinations);
                intent_up.putExtra("product", product);
                context.startActivity(intent_up);
                break;
        }
    }

    public void handleAnimation(ImageView displayButton, boolean displayedDes){
        if(displayedDes){
            ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(displayButton, "rotation", 180f, 0f);
            rotateAnimation.setDuration(250);
            rotateAnimation.start();
        }
        else{
            ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(displayButton, "rotation", 0f, 180f);
            rotateAnimation.setDuration(250);
            rotateAnimation.start();
        }
    }

    private void displayDescription() {
        layoutDescription.setVisibility(View.VISIBLE);
    }

    private void displayUtility() {
        layoutUtility.setVisibility(View.VISIBLE);
    }

    private void hideDescription() {
        layoutDescription.setVisibility(View.GONE);
    }

    private void hideUtility() {
        layoutUtility.setVisibility(View.GONE);
    }
}