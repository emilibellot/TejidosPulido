package com.example.tejidospulido_app.Vista;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Address;
import com.example.tejidospulido_app.Model.Classes.Order;
import com.example.tejidospulido_app.Model.Classes.OrderLine;
import com.example.tejidospulido_app.Model.Classes.Product;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterOrderAddress;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterPayProduct;
import com.example.tejidospulido_app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout direcctionLayout, paymentLayout, confirmationLayout, direccionDataLayout, direccionLayout, direccionesLayout, newAddress, direccionFacturacionLayout, datosContactoLayout, direccionFacturacionDataLayout, direccionesFacturacionLayout, newAddressFacturacion;
    ImageButton directionButton, payButton, confirmationButton, hechoButton, editDireccionEnvio, editDireccionFacturacion;
    Button goToPayment, saveAddress, submitAddress, saveAddressFacturacion, submitAddressFacturacion;
    ImageView fase1, fase2, fase3, formaPagoConfirmationImage;
    RadioGroup formaPagoRdioGroup;
    CheckBox add_address, setDefault, add_addressFacturacion, setDefaultFacturacion;
    Order order;
    String formaPago;
    BigDecimal precio_final;

    TextInputLayout nameLayout, surnameLayout, addressLayout, cpLayout, cityLayout, countryLayout;
    TextInputLayout nameFacturacionLayout, surnameFacturacionLayout, addressFacturacionLayout, cpFacturacionLayout, cityFacturacionLayout, countryFacturacionLayout;

    TextInputEditText name, surname, address, cp, city, country;
    TextInputEditText nameFacturacion, surnameFacturacion, addressFacturacion, cpFacturacion, cityFacturacion, countryFacturacion;

    private RecyclerView rv_addresses, rv_addressesFacturacion, rv_pedido;

    EditText phoneContact;
    TextView faseTitle, nombreContacto, nombreCalle, codigoPostal, pais, nombreContactoFacturacion, nombreCalleFacturacion, codigoPostalFacturacion, paisFacturacion, introduceDireccion, introduceDireccionFacturacion;
    TextView precioEnvio, precioTotal;
    TextView nombreContactoEnvioConfirmation, nombreCalleEnvioConfirmation, codigoPostalEnvioConfirmation, paisEnvioConfirmation, nombreContactoFacturacionConfirmation,
            nombreCalleFacturacionConfirmation, codigoPostalFacturacionConfirmation, paisFacturacionConfirmation, formaPagoConfirmationText, precioEnvioConfirmation, precioTotalConfirmation, fechaEntrega;

    ArrayList<String> addressesKeyList;
    ArrayList<Address> addressesList;
    ArrayList<String> quantityList;
    ArrayList<Product> productList;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        formaPago = "";
        activity = this;
        setViews();
    }

    private void setViews() {
        direcctionLayout = findViewById(R.id.displayDirection);
        paymentLayout = findViewById(R.id.displayPayment);
        confirmationLayout = findViewById(R.id.displayConfirmation);

        faseTitle = findViewById(R.id.faseTitle);

        fase1 = findViewById(R.id.fase1);
        fase2 = findViewById(R.id.fase2);
        fase3 = findViewById(R.id.fase3);

        directionButton = findViewById(R.id.directionButton);
        directionButton.setOnClickListener(this);

        payButton = findViewById(R.id.payButton);
        payButton.setClickable(false);
        payButton.setEnabled(false);
        payButton.setOnClickListener(this);

        confirmationButton = findViewById(R.id.confirmationButton);
        confirmationButton.setClickable(false);
        confirmationButton.setEnabled(false);
        confirmationButton.setOnClickListener(this);

        hechoButton = findViewById(R.id.hechoButton);
        hechoButton.setClickable(false);
        hechoButton.setEnabled(false);
        hechoButton.setOnClickListener(this);

        goToPayment = findViewById(R.id.goToPayment);
        goToPayment.setOnClickListener(this);

        direccionLayout = findViewById(R.id.direccionLayout);
        direccionDataLayout = findViewById(R.id.direccionDataLayout);
        introduceDireccion = findViewById(R.id.introduceDireccion);
        direccionFacturacionLayout = findViewById(R.id.direccionFacturacionLayout);
        datosContactoLayout = findViewById(R.id.datosContactoLayout);
        direccionesLayout = findViewById(R.id.direccionesLayout);
        newAddress = findViewById(R.id.newAddress);

        direccionFacturacionDataLayout = findViewById(R.id.direccionFacturacionDataLayout);
        introduceDireccionFacturacion = findViewById(R.id.introduceDireccionFacturacion);
        direccionesFacturacionLayout = findViewById(R.id.direccionesFacturacionLayout);
        newAddressFacturacion = findViewById(R.id.newAddressFacturacion);

        editDireccionEnvio = findViewById(R.id.editDireccionEnvio);
        editDireccionEnvio.setOnClickListener(this);

        submitAddress = findViewById(R.id.submitAddress);
        submitAddress.setOnClickListener(this);

        saveAddress = findViewById(R.id.saveAddress);
        saveAddress.setOnClickListener(this);

        submitAddressFacturacion = findViewById(R.id.submitAddressFacturacion);
        submitAddressFacturacion.setOnClickListener(this);

        saveAddressFacturacion = findViewById(R.id.saveAddressFacturacion);
        saveAddressFacturacion.setOnClickListener(this);

        phoneContact = findViewById(R.id.phoneContact);

        nameLayout = findViewById(R.id.nameLayout);
        surnameLayout = findViewById(R.id.surnameLayout);
        addressLayout = findViewById(R.id.addressLayout);
        cpLayout = findViewById(R.id.cpLayout);
        cityLayout = findViewById(R.id.cityLayout);
        countryLayout = findViewById(R.id.countryLayout);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        address = findViewById(R.id.address);
        cp = findViewById(R.id.cp);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);

        nameFacturacionLayout = findViewById(R.id.nameFacturacionLayout);
        surnameFacturacionLayout = findViewById(R.id.surnameFacturacionLayout);
        addressFacturacionLayout = findViewById(R.id.addressFacturacionLayout);
        cpFacturacionLayout = findViewById(R.id.cpFacturacionLayout);
        cityFacturacionLayout = findViewById(R.id.cityFacturacionLayout);
        countryFacturacionLayout = findViewById(R.id.countryFacturacionLayout);

        nameFacturacion = findViewById(R.id.nameFacturacion);
        surnameFacturacion = findViewById(R.id.surnameFacturacion);
        addressFacturacion = findViewById(R.id.addressFacturacion);
        cpFacturacion = findViewById(R.id.cpFacturacion);
        cityFacturacion = findViewById(R.id.cityFacturacion);
        countryFacturacion = findViewById(R.id.countryFacturacion);

        rv_addresses = findViewById(R.id.rv_addresses);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_addresses.setLayoutManager(lm);

        rv_addressesFacturacion = findViewById(R.id.rv_addressesFacturacion);
        LinearLayoutManager lmFacturacion = new LinearLayoutManager(this);
        rv_addressesFacturacion.setLayoutManager(lmFacturacion);

        rv_pedido = findViewById(R.id.rv_pedido);
        LinearLayoutManager lmPago = new LinearLayoutManager(this);
        rv_pedido.setLayoutManager(lmPago);

        setDefault = findViewById(R.id.setDefault);
        add_address = findViewById(R.id.add_address);
        add_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    newAddress.setVisibility(View.VISIBLE);
                    newAddress.requestFocus();
                    setAddressess("newAddress", "add");
                }
                else {
                    newAddress.setVisibility(View.GONE);
                }
            }
        });

        setDefaultFacturacion = findViewById(R.id.setDefaultFacturacion);
        add_addressFacturacion = findViewById(R.id.add_addressFacturacion);
        add_addressFacturacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    newAddressFacturacion.setVisibility(View.VISIBLE);
                    newAddressFacturacion.requestFocus();
                    setAddressess("newAddress", "fac");
                }
                else {
                    newAddressFacturacion.setVisibility(View.GONE);
                }
            }
        });

        editDireccionFacturacion = findViewById(R.id.editDireccionFacturacion);
        editDireccionFacturacion.setOnClickListener(this);

        nombreContacto = findViewById(R.id.nombreContacto);
        nombreCalle = findViewById(R.id.nombreCalle);
        codigoPostal = findViewById(R.id.codigoPostal);
        pais = findViewById(R.id.pais);

        nombreContactoEnvioConfirmation = findViewById(R.id.nombreContactoEnvioConfirmation);
        nombreCalleEnvioConfirmation = findViewById(R.id.nombreCalleEnvioConfirmation);
        codigoPostalEnvioConfirmation = findViewById(R.id.codigoPostalEnvioConfirmation);
        paisEnvioConfirmation = findViewById(R.id.paisEnvioConfirmation);

        nombreContactoFacturacion = findViewById(R.id.nombreContactoFacturacion);
        nombreCalleFacturacion = findViewById(R.id.nombreCalleFacturacion);
        codigoPostalFacturacion = findViewById(R.id.codigoPostalFacturacion);
        paisFacturacion = findViewById(R.id.paisFacturacion);

        nombreContactoFacturacionConfirmation = findViewById(R.id.nombreContactoFacturacionConfirmation);
        nombreCalleFacturacionConfirmation = findViewById(R.id.nombreCalleFacturacionConfirmation);
        codigoPostalFacturacionConfirmation = findViewById(R.id.codigoPostalFacturacionConfirmation);
        paisFacturacionConfirmation = findViewById(R.id.paisFacturacionConfirmation);

        precioEnvio = findViewById(R.id.precioEnvio);
        precioTotal = findViewById(R.id.precioTotal);

        precioEnvioConfirmation = findViewById(R.id.precioEnvioConfirmation);
        precioTotalConfirmation = findViewById(R.id.precioTotalConfirmation);

        formaPagoRdioGroup = findViewById(R.id.formaPagoRdioGroup);
        formaPagoRdioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Paypal){
                    formaPago = "Paypal";
                }
                else if (checkedId == R.id.Tarjeta){
                    formaPago = "Tarjeta";
                }
                else if (checkedId == R.id.Reembolso){
                    formaPago = "Reembolso";
                }
            }
        });

        formaPagoConfirmationImage = findViewById(R.id.formaPagoConfirmationImage);
        formaPagoConfirmationText = findViewById(R.id.formaPagoConfirmationText);

        fechaEntrega = findViewById(R.id.fechaEntrega);

        handleOrder();

        handleChargeAddressesOrder();
    }

    private void handleOrder() {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order = snapshot.getValue(Order.class);
                handleViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleViews() {
        if (order.getAddress() == null){
            direccionDataLayout.setVisibility(View.GONE);
            introduceDireccion.setVisibility(View.VISIBLE);
        }
        else{
            introduceDireccion.setVisibility(View.GONE);
            String contacto = order.getAddress().getNombre() + " " + order.getAddress().getApellidos();
            nombreContacto.setText(contacto.toUpperCase());
            nombreCalle.setText(order.getAddress().getDireccion());
            String cp_ciudad = order.getAddress().getCodigoPostal() + ", " + order.getAddress().getPoblacion();
            codigoPostal.setText(cp_ciudad);
            pais.setText(order.getAddress().getPais());

            nombreContactoEnvioConfirmation.setText(contacto.toUpperCase());
            nombreCalleEnvioConfirmation.setText(order.getAddress().getDireccion());
            codigoPostalEnvioConfirmation.setText(cp_ciudad);
            paisEnvioConfirmation.setText(order.getAddress().getPais());
        }

        if (order.getBillingAddress() == null){
            direccionFacturacionDataLayout.setVisibility(View.GONE);
            introduceDireccionFacturacion.setVisibility(View.VISIBLE);
        }
        else{
            introduceDireccionFacturacion.setVisibility(View.GONE);
            String contactoFact = order.getBillingAddress().getNombre() + " " + order.getBillingAddress().getApellidos();
            nombreContactoFacturacion.setText(contactoFact.toUpperCase());
            nombreCalleFacturacion.setText(order.getBillingAddress().getDireccion());
            String cp_ciudadFact = order.getBillingAddress().getCodigoPostal() + ", " + order.getBillingAddress().getPoblacion();
            codigoPostalFacturacion.setText(cp_ciudadFact);
            paisFacturacion.setText(order.getBillingAddress().getPais());

            nombreContactoFacturacionConfirmation.setText(contactoFact.toUpperCase());
            nombreCalleFacturacionConfirmation.setText(order.getBillingAddress().getDireccion());
            codigoPostalFacturacionConfirmation.setText(cp_ciudadFact);
            paisFacturacionConfirmation.setText(order.getBillingAddress().getPais());
        }

        phoneContact.setText(order.getPhone());

        String precio_total_s;
        BigDecimal precio_total_d = new BigDecimal(order.getPrice());
        Log.d("holaaa", String.valueOf(precio_total_d));
        Log.d("holaaa213", String.valueOf(new BigDecimal("60.0")));
        if(precio_total_d.compareTo(new BigDecimal("60.0")) == 0 || precio_total_d.compareTo(new BigDecimal("60.0")) == 1){
            precioEnvio.setText(R.string.Gratis);
            precio_final = precio_total_d;
            precioEnvioConfirmation.setText(R.string.Gratis);
            precio_total_s = order.getPrice() + " €";
            precioTotal.setText(order.getPrice());
            precioTotalConfirmation.setText(order.getPrice());
        }
        else{
            precioEnvio.setText("5.40 €");
            precioEnvioConfirmation.setText("5.40 €");
            precio_final = precio_total_d.add(new BigDecimal("5.40")).setScale(2, BigDecimal.ROUND_UP);
            precio_total_s = precio_final + " €";
            precioTotal.setText(precio_total_s);
            precioTotalConfirmation.setText(precio_total_s);
        }

        if (order.getPayForm().equals("Paypal")) formaPagoConfirmationImage.setImageResource(R.drawable.ic_paypal);
        else if (order.getPayForm().equals("Tarjeta")) formaPagoConfirmationImage.setImageResource(R.drawable.ic_visa);
        else formaPagoConfirmationImage.setImageResource(R.drawable.ic_cash_on_delivery);
        formaPagoConfirmationText.setText(order.getPayForm());

        handleDate();

        handleChargeProductsOrder();
    }

    private void handleDate() {
        String date = order.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 3);
        int month1      = c.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth1 = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek1  = c.get(Calendar.DAY_OF_WEEK);

        c.add(Calendar.DAY_OF_MONTH, 2);
        int month2      = c.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth2 = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek2  = c.get(Calendar.DAY_OF_WEEK);

        String dayWeek1 = getDayOfWeek(dayOfWeek1);
        String dayWeek2 = getDayOfWeek(dayOfWeek2);
        //Date after adding the days to the given date
        String newDate = dayWeek1 + ", " + dayOfMonth1 + "." + String.valueOf(month1+1) + ". - " + dayWeek2 + ", " + dayOfMonth2 + "." + String.valueOf(month2+1) + ".";
        fechaEntrega.setText(newDate);
    }

    private String getDayOfWeek(int dayOfWeek) {
        if(dayOfWeek == 0) return "Lun";
        else if(dayOfWeek == 1) return "Mar";
        else if(dayOfWeek == 2) return "Mie";
        else if(dayOfWeek == 3) return "Jue";
        else if(dayOfWeek == 4) return "Vie";
        else if(dayOfWeek == 5) return "Sab";
        else return "Dom";
    }

    private void handleChargeAddressesOrder() {
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressesKeyList = new ArrayList<>();
                addressesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Address a = dataSnapshot.getValue(Address.class);
                    addressesKeyList.add(dataSnapshot.getKey());
                    addressesList.add(a);
                    setAddressess(null, "all");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAddressess(String address, String type) {
        AdapterOrderAddress adapter;
        if(type.equals("all")){
            adapter = new AdapterOrderAddress(this, addressesKeyList, addressesList, address, add_address, "add");
            AdapterOrderAddress adapterFacturacion = new AdapterOrderAddress(this, addressesKeyList, addressesList, address, add_addressFacturacion, "fac");
            rv_addresses.setAdapter(adapter);
            rv_addressesFacturacion.setAdapter(adapterFacturacion);
        }
        else if (type.equals("add")){
            adapter = new AdapterOrderAddress(this, addressesKeyList, addressesList, address, add_address, "add");
            rv_addresses.setAdapter(adapter);
        }
        else if (type.equals("fac")){
            adapter = new AdapterOrderAddress(this, addressesKeyList, addressesList, address, add_addressFacturacion, "fac");
            rv_addressesFacturacion.setAdapter(adapter);
        }
    }

    private void handleChargeProductsOrder() {
        productList = new ArrayList<>();
        quantityList = new ArrayList<>();
        for (OrderLine orderLine : order.getOrder()) {
            String prod = orderLine.getProduct().substring(0, 4);
            String var = orderLine.getProduct().substring(5, 9);
            Log.d("YEPA", prod);
            Log.d("YEPA", var);
            FirebaseDatabase.getInstance().getReference().child("productos").child(prod).child("variantes").child(var).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product p = snapshot.getValue(Product.class);
                    productList.add(p);
                    quantityList.add(orderLine.getQuantity());
                    setProducts();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setProducts() {
        AdapterPayProduct adapter = new AdapterPayProduct(this, productList, quantityList);
        rv_pedido.setAdapter(adapter);
    }

    public boolean isWrong(Address a){
        return a.getNombre().isEmpty() || a.getApellidos().isEmpty() || a.getDireccion().isEmpty() || a.getCodigoPostal().isEmpty() || a.getPoblacion().isEmpty() || a.getPais().isEmpty();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        Address newAddress;
        DatabaseReference ref;
        String key;

        switch (v.getId()) {
            case R.id.back_to_menu_carrito:
                finish();
                break;

            case R.id.goToPayment:
                /*if (isWrong(order.getAddress()) || isWrong(order.getBillingAddress())){
                    Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.alert_dialog_accept);
                    dialog.getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.background));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                    ImageView alertIcon = dialog.findViewById(R.id.alertIcon);
                    alertIcon.setImageResource(R.drawable.ic_alert);
                    TextView title = dialog.findViewById(R.id.titleDialag);
                    title.setText(R.string.ErrorAddressesTitle);
                    TextView message = dialog.findViewById(R.id.messageDialag);
                    message.setText(R.string.ErrorAddressesMessage);

                    Button okey = dialog.findViewById(R.id.okey);
                    okey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{*/
                    hideKeyboard(activity);
                    order.setTotalPrice(String.valueOf(precio_final));
                    order.setPhone(phoneContact.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").setValue(order);
                    faseTitle.setText(R.string.Pago);
                    directionButton.setImageResource(R.drawable.ic_checked);
                    payButton.setImageResource(R.drawable.ic_fase_two_green);
                    payButton.setClickable(true);
                    payButton.setEnabled(true);
                    direcctionLayout.setVisibility(View.GONE);
                    paymentLayout.setVisibility(View.VISIBLE);
                    fase1.setBackgroundResource(R.drawable.fase_background);
                //}
                break;

            case R.id.goToCofirmation:
                if (formaPago.isEmpty()){
                    Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.alert_dialog_accept);
                    dialog.getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.background));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                    ImageView alertIcon = dialog.findViewById(R.id.alertIcon);
                    alertIcon.setImageResource(R.drawable.ic_alert);
                    TextView title = dialog.findViewById(R.id.titleDialag);
                    title.setText(R.string.ErrorPayFormTitle);
                    TextView message = dialog.findViewById(R.id.messageDialag);
                    message.setText(R.string.ErrorPayFormMessage);

                    Button okey = dialog.findViewById(R.id.okey);
                    okey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    order.setPayForm(formaPago);
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").setValue(order);
                    faseTitle.setText(R.string.Confirmacion);
                    payButton.setImageResource(R.drawable.ic_checked);
                    confirmationButton.setImageResource(R.drawable.ic_fase_three_green);
                    confirmationButton.setClickable(true);
                    confirmationButton.setEnabled(true);
                    paymentLayout.setVisibility(View.GONE);
                    confirmationLayout.setVisibility(View.VISIBLE);
                    fase2.setBackgroundResource(R.drawable.fase_background);
                }
                break;

            case R.id.goToHecho:
                faseTitle.setText(R.string.Hecho);
                payButton.setImageResource(R.drawable.ic_checked);
                confirmationButton.setImageResource(R.drawable.ic_checked);
                hechoButton.setImageResource(R.drawable.ic_fase_four_green);
                hechoButton.setClickable(true);
                hechoButton.setEnabled(true);
                paymentLayout.setVisibility(View.GONE);
                confirmationLayout.setVisibility(View.GONE);
                fase3.setBackgroundResource(R.drawable.fase_background);
                Intent intent_up  = new Intent(activity, CheckOutActivity.class);
                activity.startActivity(intent_up);
                break;

            case R.id.directionButton:
                faseTitle.setText(R.string.Direccion);
                directionButton.setImageResource(R.drawable.ic_fase_one_green);
                payButton.setImageResource(R.drawable.ic_fase_two_default);
                payButton.setClickable(false);
                payButton.setEnabled(false);
                confirmationButton.setImageResource(R.drawable.ic_fase_three_default);
                confirmationButton.setClickable(false);
                confirmationButton.setEnabled(false);
                hechoButton.setImageResource(R.drawable.ic_fase_four_default);
                hechoButton.setClickable(false);
                hechoButton.setEnabled(false);
                direcctionLayout.setVisibility(View.VISIBLE);
                paymentLayout.setVisibility(View.GONE);
                confirmationLayout.setVisibility(View.GONE);
                fase1.setBackgroundResource(R.drawable.vertical_bordered_layout);
                fase2.setBackgroundResource(R.drawable.vertical_bordered_layout);
                fase3.setBackgroundResource(R.drawable.vertical_bordered_layout);
                break;

            case R.id.payButton:
                faseTitle.setText(R.string.Pago);
                directionButton.setImageResource(R.drawable.ic_checked);
                payButton.setImageResource(R.drawable.ic_fase_two_green);
                confirmationButton.setImageResource(R.drawable.ic_fase_three_default);
                confirmationButton.setClickable(false);
                confirmationButton.setEnabled(false);
                hechoButton.setImageResource(R.drawable.ic_fase_four_default);
                hechoButton.setClickable(false);
                hechoButton.setEnabled(false);
                direcctionLayout.setVisibility(View.GONE);
                paymentLayout.setVisibility(View.VISIBLE);
                confirmationLayout.setVisibility(View.GONE);
                fase1.setBackgroundResource(R.drawable.fase_background);
                fase2.setBackgroundResource(R.drawable.vertical_bordered_layout);
                fase3.setBackgroundResource(R.drawable.vertical_bordered_layout);
                break;

            case R.id.confirmationButton:
                faseTitle.setText(R.string.Confirmacion);
                directionButton.setImageResource(R.drawable.ic_checked);
                payButton.setImageResource(R.drawable.ic_checked);
                confirmationButton.setImageResource(R.drawable.ic_fase_three_green);
                hechoButton.setImageResource(R.drawable.ic_fase_four_default);
                hechoButton.setClickable(false);
                hechoButton.setEnabled(false);
                direcctionLayout.setVisibility(View.GONE);
                paymentLayout.setVisibility(View.GONE);
                confirmationLayout.setVisibility(View.VISIBLE);
                fase1.setBackgroundResource(R.drawable.fase_background);
                fase2.setBackgroundResource(R.drawable.fase_background);
                fase3.setBackgroundResource(R.drawable.vertical_bordered_layout);
                break;

            case R.id.editDireccionEnvio:
                /*intent = new Intent(this, AddressActivity.class);
                intent.putExtra("type", "envio");
                intent.putExtra("address", order.getAddress());
                startActivity(intent);*/
                direccionDataLayout.setVisibility(View.GONE);
                introduceDireccion.setVisibility(View.GONE);
                direccionFacturacionLayout.setVisibility(View.GONE);
                datosContactoLayout.setVisibility(View.GONE);
                goToPayment.setVisibility(View.GONE);
                editDireccionEnvio.setVisibility(View.GONE);
                direccionesLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.editDireccionFacturacion:
                /*intent = new Intent(this, AddressActivity.class);
                intent.putExtra("type", "facturacion");
                intent.putExtra("address", order.getBillingAddress());
                startActivity(intent);*/
                direccionFacturacionDataLayout.setVisibility(View.GONE);
                introduceDireccionFacturacion.setVisibility(View.GONE);
                direccionLayout.setVisibility(View.GONE);
                datosContactoLayout.setVisibility(View.GONE);
                goToPayment.setVisibility(View.GONE);
                editDireccionFacturacion.setVisibility(View.GONE);
                direccionesFacturacionLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.submitAddress:
                introduceDireccion.setVisibility(View.GONE);
                direccionesLayout.setVisibility(View.GONE);
                editDireccionEnvio.setVisibility(View.VISIBLE);
                direccionDataLayout.setVisibility(View.VISIBLE);
                direccionFacturacionLayout.setVisibility(View.VISIBLE);
                datosContactoLayout.setVisibility(View.VISIBLE);
                goToPayment.setVisibility(View.VISIBLE);
                break;

            case R.id.submitAddressFacturacion:
                introduceDireccionFacturacion.setVisibility(View.GONE);
                direccionesFacturacionLayout.setVisibility(View.GONE);
                editDireccionFacturacion.setVisibility(View.VISIBLE);
                direccionFacturacionDataLayout.setVisibility(View.VISIBLE);
                direccionLayout.setVisibility(View.VISIBLE);
                datosContactoLayout.setVisibility(View.VISIBLE);
                goToPayment.setVisibility(View.VISIBLE);
                break;

            case R.id.saveAddress:
                newAddress = new Address(name.getText().toString(), surname.getText().toString(), address.getText().toString(), cp.getText().toString(), city.getText().toString(), country.getText().toString());
                hideErrors("add");
                if (checkAddress("add", newAddress)) {
                    ref = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").push();
                    key = ref.getKey();
                    ref.setValue(newAddress);
                    if (setDefault.isChecked()) {
                        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue(key);
                    }
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").setValue(newAddress);
                    addressesKeyList.add(key);
                    addressesList.add(newAddress);
                    setAddressess(key, "add");
                    add_address.setChecked(false);
                }
                break;

            case R.id.saveAddressFacturacion:
                newAddress = new Address(name.getText().toString(), surname.getText().toString(), address.getText().toString(), cp.getText().toString(), city.getText().toString(), country.getText().toString());
                hideErrors("fac");
                if (checkAddress("fac", newAddress)) {
                    ref = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("addresses").push();
                    key = ref.getKey();
                    ref.setValue(newAddress);
                    if (setDefaultFacturacion.isChecked()){
                        FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").setValue(key);
                    }
                    FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pedido").child("address").setValue(newAddress);
                    addressesKeyList.add(key);
                    addressesList.add(newAddress);
                    setAddressess(key, "fac");
                    add_addressFacturacion.setChecked(false);
                }
                break;
        }
    }

    private void hideErrors(String type) {
        if (type.equals("add")) {
            nameLayout.setError(null);
            surnameLayout.setError(null);
            addressLayout.setError(null);
            cpLayout.setError(null);
            cityLayout.setError(null);
            countryLayout.setError(null);
        }
        else {
            nameFacturacionLayout.setError(null);
            surnameFacturacionLayout.setError(null);
            addressFacturacionLayout.setError(null);
            cpFacturacionLayout.setError(null);
            cityFacturacionLayout.setError(null);
            countryFacturacionLayout.setError(null);
        }
    }

    private boolean checkAddress(String type, Address newAddress) {
        boolean b = true;
        if (newAddress.getNombre().isEmpty()) {
            if (type.equals("add")) nameLayout.setError("Introduce un nombre de contacto.");
            else nameFacturacionLayout.setError("Introduce un nombre de contacto.");
            b = false;
        }
        if (newAddress.getApellidos().isEmpty()) {
            if (type.equals("add")) surnameLayout.setError("Introduce los apellidos de contacto.");
            else surnameFacturacionLayout.setError("Introduce los apellidos de contacto.");
            b = false;
        }
        if (newAddress.getDireccion().isEmpty()) {
            if (type.equals("add")) addressLayout.setError("Introduce una dirección correcta.");
            else addressFacturacionLayout.setError("Introduce una dirección correcta.");
            b = false;
        }
        if (newAddress.getCodigoPostal().isEmpty()) {
            if (type.equals("add")) cpLayout.setError("Introduce un código postal correcto.");
            else cpFacturacionLayout.setError("Introduce un código postal correcto.");
            b = false;
        }
        if (newAddress.getPoblacion().isEmpty()) {
            if (type.equals("add")) cityLayout.setError("Introduce una población correcta.");
            else cityFacturacionLayout.setError("Introduce una población correcta.");
            b = false;
        }
        if (newAddress.getPais().isEmpty()) {
            if (type.equals("add")) countryLayout.setError("Selecciona un país.");
            else countryFacturacionLayout.setError("Selecciona un país.");
            b = false;
        }
        return b;
    }
}
