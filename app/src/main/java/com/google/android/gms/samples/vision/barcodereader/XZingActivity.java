package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.Firebase.LivePreviewActivity;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
         if(result == null) Log.d("NU", "handleResult: ");
        else{
       if(!MainActivity.barcodeDisplay.contains(result.getText())) {
            MainActivity.barcodeDisplay.add(result.getText());
           int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
           int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           Calendar c = Calendar.getInstance();
           String date = sdf.format(c.getTime());
           MainActivity.barcodeDisplayData.add(new BarcodeData(result.getText(), "Code 39",currentHour+":"+currentMinute,date  ));
           if(toast != null)
               toast.cancel();
           toast = Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT);
           toast.show();
           if(MainActivity.vibratorSwitch)
               ((Vibrator) LivePreviewActivity.c.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200); // for 200 ms
           if(MainActivity.sound)
               (new ToneGenerator(AudioManager.STREAM_MUSIC, 100)).startTone(ToneGenerator.TONE_CDMA_PIP,150);
        }
        if(toast != null)
         toast.cancel();
         toast = Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT);
         toast.show();
        zXingScannerView.resumeCameraPreview(this);}

    }
    @Override
    public void onBackPressed() {
        Intent data = new Intent(this, MainActivity.class);
        startActivity(data);
        finish();
    }
}
