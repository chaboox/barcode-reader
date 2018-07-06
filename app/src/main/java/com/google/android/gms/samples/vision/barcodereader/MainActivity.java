
package com.google.android.gms.samples.vision.barcodereader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.CostumeAdapter.CustomAdapterBarcode;
import com.google.android.gms.samples.vision.barcodereader.CostumeAdapter.DataModelBarcode;
import com.google.android.gms.samples.vision.barcodereader.Firebase.LivePreviewActivity;
import com.google.android.gms.vision.barcode.Barcode;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;


public class MainActivity  extends AppCompatActivity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    public static Boolean flash = false;
    public  static List<Barcode> barcode2 = new ArrayList<>();
    public static List<String> barcodeDisplay = new ArrayList<>();
    public static List<BarcodeData> barcodeDisplayData = new ArrayList<>();
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private ListView listView;
    private ArrayList<DataModelBarcode> dataModelBarcodes;
    private static CustomAdapterBarcode adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d(TAG, "onMenuItemClick:YOO ");
                return true;
            }
        });
        listView=(ListView)findViewById(R.id.list_view);
        statusMessage = (TextView)findViewById(R.id.status_message);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        dataModelBarcodes = new ArrayList<>();
        findViewById(R.id.readbarcodexz).setOnClickListener(this);
        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.firebase).setOnClickListener(this);
        String test =" ";
        if(barcodeDisplay.size() != 0) {
            if(barcodeDisplayData.size() != 0)
                for (BarcodeData bd  :barcodeDisplayData)
                {
                    // test = test + "  " + s;
                    dataModelBarcodes.add(new DataModelBarcode(bd.getCode(), "Code 39", bd.getHour(), bd.getDate()));


                }
                else
            for (String s : barcodeDisplay) {
               // test = test + "  " + s;
                dataModelBarcodes.add(new DataModelBarcode(s, "Code 39", "00:00","01/01/2018"));


            }
          //  barcodeValue.setText("" + test + "  " );
            adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DataModelBarcode dataModelClient = dataModelBarcodes.get(position);

                   /* Snackbar.make(view, dataModelClient.getCode()+"\n"+ dataModelClient.getFormat(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    */
                   /* Intent i = new Intent(ModifierClient.this,ModifierClientSelect.class);
                    i.putExtra("idUser",idU);
                    i.putExtra("idClient", dataModelClient.getId());

                    startActivity(i);*/

                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "DKHALE: ");
        switch (item.getItemId()) {
            case R.id.setting_button:
              Intent intent = new Intent(this, SettingsActivity.class);
              startActivity(intent);
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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

           Intent intent = new Intent(this, LivePreviewActivity.class);

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
           barcodeDisplayData.clear();
           listView.setAdapter(adapter);
           dataModelBarcodes.clear();
            adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());

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
                  //  statusMessage.setText(R.string.barcode_success);
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
                       // test = test +"  "+ s;
                        dataModelBarcodes.add(new DataModelBarcode(s, "Code 39","00:00","01/01/2018"));

                    }
                   // barcodeValue.setText(""+test+"  ");
                   // Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                  //  statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
                adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModelBarcode dataModelClient = dataModelBarcodes.get(position);

                        Snackbar.make(view, dataModelClient.getCode()+"\n"+ dataModelClient.getFormat(), Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();
                   /* Intent i = new Intent(ModifierClient.this,ModifierClientSelect.class);
                    // Log.d("yoo", "  "+id);
                    i.putExtra("idUser",idU);
                    i.putExtra("idClient", dataModelClient.getId());

                    startActivity(i);*/

                    }
                });
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setMessage("Are you sure you want to quit ");
        adb.setNegativeButton("Yes", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }});
        adb.setPositiveButton("No", null);
        adb.show();
    }

}
