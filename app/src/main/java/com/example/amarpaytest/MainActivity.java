package com.example.amarpaytest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aamarpay.library.AamarPay;
import com.aamarpay.library.DialogBuilder;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    android.app.AlertDialog alertDialog;
    AlertDialog.Builder builder;
    AamarPay aamarPay;

    DialogBuilder dialogBuilder;

    private String trxID, trxAmount , trxCurrency , customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogBuilder = new DialogBuilder(MainActivity.this, alertDialog);
        builder = new android.app.AlertDialog.Builder(this);

        trxAmount = "500";
        trxCurrency = "BDT";
        customerName = "Forhadul Islam";
        customerEmail = "fitareq@gmail.com";
        customerPhone = "+8801647161559";
        customerAddress = "Mirpur-2";
        customerCity = "Dhaka";
        customerCountry = "Bangladesh";
        paymentDescription = "Description isn't available right now";


        textView = findViewById(R.id.text_view);
        button = findViewById(R.id.button);


        aamarPay = new AamarPay(MainActivity.this, "aamarpay", "28c78bb1f45112f5d40b956fe104645a");
        aamarPay.testMode(true);
        aamarPay.autoGenerateTransactionID(true);
        trxID = aamarPay.generate_trx_id();
        aamarPay.setTransactionID(trxID);


        String s = "Name: "+customerName+"\n"
                +"Phone: "+customerPhone+"\n"
                +"Email: "+customerEmail+"\n"
                +"Address: "+customerAddress+"\n"
                +"City: "+customerCity+"\n"
                +"Country: "+customerCountry+"\n"
                +"Amount: "+trxAmount+" BDT\n"
                +"Transaction ID: "+trxID;

        textView.setText(s);


        button.setOnClickListener(v -> {
            dialogBuilder.showLoading();
            aamarPay.setTransactionParameter(trxAmount,trxCurrency,paymentDescription);
            aamarPay.setCustomerDetails(customerName,customerEmail,customerPhone,customerAddress,customerCity,customerCountry);

            aamarPay.initPGW(new AamarPay.onInitListener() {
                @Override
                public void onInitFailure(Boolean error, String message) {
                    dialogBuilder.dismissDialog();
                    dialogBuilder.errorPopUp(message);
                }

                @Override
                public void onPaymentSuccess(JSONObject jsonObject) {
                    dialogBuilder.dismissDialog();
                    builder.setTitle("Payment successfull");
                    builder.setMessage(jsonObject.toString());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

                @Override
                public void onPaymentFailure(JSONObject jsonObject) {
                    dialogBuilder.dismissDialog();
                    builder.setTitle("Payment Failure");
                    builder.setMessage(jsonObject.toString());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                @Override
                public void onPaymentProcessingFailed(JSONObject jsonObject) {
                    dialogBuilder.dismissDialog();
                    builder.setTitle("Payment Processing Failed");
                    builder.setMessage(jsonObject.toString());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                @Override
                public void onPaymentCancel(JSONObject jsonObject) {
                    dialogBuilder.dismissDialog();
                    builder.setTitle("Payment Canceled by User");
                    builder.setMessage(jsonObject.toString());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        });
    }
}