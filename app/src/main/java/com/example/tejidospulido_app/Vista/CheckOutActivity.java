package com.example.tejidospulido_app.Vista;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tejidospulido_app.Model.Config.FirebaseEphemeralKeyProvider;
import com.example.tejidospulido_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.PaymentSessionData;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CheckOutActivity extends AppCompatActivity {
    private final int RC_SIGN_IN = 1;
    private FirebaseUser currentUser;
    private PaymentSession paymentSession;
    private PaymentMethod selectedPaymentMethod;
    private Stripe stripe;
    PaymentMethod paymentMethod;

    TextView checkoutSummary, paymentMethodTv, greeting;
    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_payment_layout);
        PaymentConfiguration.init(getApplicationContext(), "pk_test_51Ico74J7WLvztKNjqXqv5vb9dM3lBrhhttHzT54AQywvUfcMlwOILWF3N0EBs2O6JsFV2yuixVKGt4yL1XOGInGG006MF3LLeB");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        stripe = new Stripe(getApplicationContext(),
                PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());

        payButton = findViewById(R.id.payButton);
        checkoutSummary = findViewById(R.id.checkoutSummary);
        greeting = findViewById(R.id.greeting);
        paymentMethodTv = findViewById(R.id.paymentmethod);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPayment(selectedPaymentMethod.id);
            }
        });

        paymentMethodTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentSession.presentPaymentMethodSelection(selectedPaymentMethod.id);
            }
        });
        showUI();
    }

    private void showUI() {
        greeting.setVisibility(View.VISIBLE);
        checkoutSummary.setVisibility(View.VISIBLE);
        payButton.setVisibility(View.VISIBLE);
        paymentMethodTv.setVisibility(View.VISIBLE);

        greeting.setText("Hello" + currentUser.getDisplayName());

        setupPaymentSession();
    }

    private void confirmPayment(final String paymentMethodId) {
        CollectionReference paymentCollection = FirebaseFirestore.getInstance().collection("stripe_customers")
                .document(currentUser.getUid()).collection("payments");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("amount", 8800);
        hashMap.put("currency", "cad");

        paymentCollection.add(hashMap)
                .addOnSuccessListener(new OnSuccessListener() {

                    public void onSuccess(Object listener) {
                        this.onSuccesss((DocumentReference)listener);
                    }

                    public void onSuccesss(DocumentReference documentReference) {
                        StringBuilder thisUser = (new StringBuilder()).append("DocumentSnapshot added with ID: ");

                        Log.d("paymentsss", thisUser.append(documentReference.getId()).toString());

                        documentReference.addSnapshotListener(new EventListener() {
                            public void onEvent(Object listener, FirebaseFirestoreException e) {
                                this.onEvent((DocumentSnapshot)listener, e);
                            }

                            public final void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("paymentsss", "Listen failed.", (Throwable)e);
                                } else {
                                    if (snapshot != null && snapshot.exists()) {
                                        Log.d("paymentsss", "Current data: " + snapshot.getData());

                                        Object clientSecret = snapshot.getData().get("client_secret");
                                        Log.d("paymentsss", "Create paymentIntent returns " + clientSecret);
                                        if (clientSecret != null) {

                                            stripe.confirmPayment(CheckOutActivity.this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                                    paymentMethodId, clientSecret.toString()));

                                            checkoutSummary.setText((CharSequence)"Thank you for your payment");
                                            Toast.makeText(getApplicationContext(), (CharSequence)"Payment Done!!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Log.e("paymentsss", "Current payment intent : null");
                                        payButton.setEnabled(true);
                                    }

                                }
                            }
                        });
                    }
                }).addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            public final void onFailure(@NotNull Exception e) {
                Log.w("paymentsss", "Error adding document", (Throwable)e);
                payButton.setEnabled(true);
            }
        }));
    }

    private void setupPaymentSession() {
        CustomerSession.initCustomerSession(this, new FirebaseEphemeralKeyProvider());

        PaymentSessionConfig config = new PaymentSessionConfig.Builder()
                .setShippingMethodsRequired(false)
                .setShippingInfoRequired(false)
                .build();

        paymentSession = new PaymentSession(this, config);

        paymentSession.init(new PaymentSession.PaymentSessionListener() {
            public void onPaymentSessionDataChanged(@NotNull PaymentSessionData data) {

                Log.d("PaymentSession1", "11PaymentSession has changed: " + data);
                Log.d("PaymentSession11", "1111 " + data.isPaymentReadyToCharge() + " <> " + data.getPaymentMethod());
                if (data.isPaymentReadyToCharge()) {
                    Log.d("PaymentSession2", "222Ready to charge");

                    payButton.setEnabled(true);
                    PaymentMethod paymentMethod = data.getPaymentMethod();
                    if (paymentMethod != null) {
                        Log.d("PaymentSession3", "333PaymentMethod " + paymentMethod + " selected");

                        StringBuilder thisUser = new StringBuilder();
                        PaymentMethod.Card card = paymentMethod.card;

                        thisUser = thisUser.append(card != null ? card.brand : null).append(" card ends with ");
                        paymentMethodTv.setText((CharSequence)thisUser.append(card != null ? card.last4 : null).toString());

                        selectedPaymentMethod = paymentMethod;
                    }
                }
            }

            public void onCommunicatingStateChanged(boolean isCommunicating) {
                Log.d("PaymentSession4", "444isCommunicating " + isCommunicating);
            }

            public void onError(int errorCode, @NotNull String errorMessage) {
                Log.e("PaymentSession5", "555onError: " + errorCode + ", " + errorMessage);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            paymentSession.handlePaymentData(requestCode, resultCode, data);

            stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
            Log.d("Login", "User ${currentUser?.displayName} has signed in.");
            showUI();
        }
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckOutActivity> activityRef;

        PaymentResultCallback(@NonNull CheckOutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckOutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //activity.displayAlert("Payment completed", gson.toJson(paymentIntent), true);
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed
                //activity.displayAlert("Payment failed", Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(), false);
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckOutActivity activity = activityRef.get();
        }
    }
}
