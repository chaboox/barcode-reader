
package com.google.android.gms.samples.vision.barcodereader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.StrictMode;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity  extends AppCompatActivity implements View.OnClickListener {


    private TextView statusMessage;
    public static Boolean flash = false;
    public static Boolean vibratorSwitch = true;
    public static Boolean sound = false;
    public static int activeScan = 2131296395;
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

        listView=(ListView)findViewById(R.id.list_view);
        statusMessage = (TextView)findViewById(R.id.status_message);
        dataModelBarcodes = new ArrayList<>();
        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);

        if(barcodeDisplay.size() != 0) {
            if(barcodeDisplayData.size() != 0)
                for (BarcodeData bd  :barcodeDisplayData)
                {
                    dataModelBarcodes.add(new DataModelBarcode(bd.getCode(), "Code 39", bd.getHour(), bd.getDate()));
                }
               /* else
            for (String s : barcodeDisplay) {
               // test = test + "  " + s;
                dataModelBarcodes.add(new DataModelBarcode(s, "Code 39", "00:00","01/01/2018"));


            }*/
            adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DataModelBarcode dataModelBarcode = dataModelBarcodes.get(position);

                   /* Snackbar.make(view, dataModelClient.getCode()+"\n"+ dataModelClient.getFormat(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    */
                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting_button:
              Intent intent = new Intent(this, Settings.class);
              startActivity(intent);
                return true;



            default:

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            if(activeScan == 2131296395){
                Intent intent = new Intent(this, LivePreviewActivity.class);
                startActivity(intent);
            }
            else if (activeScan == 2131296397){
                Intent intent = new Intent(this, XZingActivity.class);

                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);

                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, flash);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }

        }

        else if (v.getId() == R.id.clear) {

           barcode2.clear();
           barcodeDisplay.clear();
           barcodeDisplayData.clear();
           listView.setAdapter(adapter);
           dataModelBarcodes.clear();
            adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());

        }
        else if (v.getId() == R.id.send) {
            if(MainActivity.barcodeDisplayData.size() == 0)
                Toast.makeText(getApplicationContext(),"Please scan first",Toast.LENGTH_SHORT).show();

            else
            mailSender();


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

                    for(String s :barcodeDisplay){
                       // test = test +"  "+ s;
                        dataModelBarcodes.add(new DataModelBarcode(s, "Code 39","00:00","01/01/2018"));

                    }

                }
                adapter= new CustomAdapterBarcode(dataModelBarcodes,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModelBarcode dataModelClient = dataModelBarcodes.get(position);

                        Snackbar.make(view, dataModelClient.getCode()+"\n"+ dataModelClient.getFormat(), Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();

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
        adb.setMessage("Are you sure you want to quit ?");
        adb.setNegativeButton("Yes", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }});
        adb.setPositiveButton("No", null);
        adb.show();

    }


        public void mailSender(){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            String combinedString =   "\"Date\",\"Heure\",\"Scan\"";
            for(BarcodeData barcodeData :barcodeDisplayData){
                combinedString = combinedString +"\n"+"\"" + barcodeData.getDate() +"\",\"" + barcodeData.getHour() + "\",\"" + barcodeData.getCode() +  "\"";
            }

            File file   = null;
            File root   = Environment.getExternalStorageDirectory();
            if (root.canWrite()){
                File dir    =   new File (root.getAbsolutePath() + "/Barcode-reader");
                dir.mkdirs();
                file   =   new File(dir, "Data.csv");
                FileOutputStream out   =   null;
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    out.write(combinedString.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("plain/text");
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "m.amara@mcq-scan.com" });
                i.putExtra(Intent.EXTRA_SUBJECT, "Résultat du scan");
                i.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "E-mail"));


        }


}

