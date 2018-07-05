package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.zxing.Result;

public class XZingActivity extends AppCompatActivity implements ZXingScannerViewEdit.ResultHandler{
    private ZXingScannerViewEdit zXingScannerView;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zXingScannerView =new ZXingScannerViewEdit(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    public void scan(View view){
        zXingScannerView =new ZXingScannerViewEdit(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
       if(!MainActivity.barcodeDisplay.contains(result.getText())) {
            MainActivity.barcodeDisplay.add(result.getText());
        }
        if(toast != null)
         toast.cancel();
         toast = Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT);
         toast.show();
        zXingScannerView.resumeCameraPreview(this);

    }
    @Override
    public void onBackPressed() {
        Intent data = new Intent(this, MainActivity.class);
        startActivity(data);
        finish();
    }
}
