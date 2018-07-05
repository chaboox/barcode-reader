
package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.Firebase.LivePreviewActivity;
import com.google.android.gms.vision.barcode.Barcode;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    public static Boolean flash = false;
    public  static List<Barcode> barcode2 = new ArrayList<>();
    public static List<String> barcodeDisplay = new ArrayList<>();
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.readbarcodexz).setOnClickListener(this);
        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.firebase).setOnClickListener(this);
        String test =" ";
        if(barcodeDisplay.size() != 0) {
            for (String s : barcodeDisplay) {
                test = test + "  " + s;

            }
            barcodeValue.setText("" + test + "  " );
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.

           Intent intent = new Intent(this, BarcodeCaptureActivity.class);
           //auto focus always on
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
       else if (v.getId() == R.id.readbarcodexz) {
            // launch barcode activity.

           Intent intent = new Intent(this, XZingActivity.class);

            startActivity(intent);
        } else if (v.getId() == R.id.firebase) {
            // launch barcode activity.
            flash = useFlash.isChecked();
            Intent intent = new Intent(this, LivePreviewActivity.class);

            startActivity(intent);
        }
        else if (v.getId() == R.id.clear) {


           barcode2.clear();
           barcodeDisplay.clear();
            barcodeValue.setText("");

        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (barcodeDisplay.size() != 0) {
                  //  Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    String test = "  ";
                    String test2 = "  ";
                   // barcode2.add(barcode);
                  /*  for(Barcode br :barcode2){
                        test2 = test2 + br.displayValue;
                        if(!barcodeDisplay.contains(br.displayValue))
                            barcodeDisplay.add(br.displayValue);
                    }*/
                    //barcode2.
                    for(String s :barcodeDisplay){
                        test = test +"  "+ s;

                    }
                    barcodeValue.setText(""+test+"  ");
                   // Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
