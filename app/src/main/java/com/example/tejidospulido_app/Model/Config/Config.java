package com.example.tejidospulido_app.Model.Config;

import android.app.Application;

import com.stripe.android.BuildConfig;
import com.stripe.android.PaymentConfiguration;

public class Config extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(getApplicationContext(), "pk_test_51Ico74J7WLvztKNjqXqv5vb9dM3lBrhhttHzT54AQywvUfcMlwOILWF3N0EBs2O6JsFV2yuixVKGt4yL1XOGInGG006MF3LLeB");
    }
}
