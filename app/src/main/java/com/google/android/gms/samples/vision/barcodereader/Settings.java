package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final CompoundButton switchFlash = (CompoundButton) findViewById(R.id.switch_flash);
        final CompoundButton switchVibrator = (CompoundButton) findViewById(R.id.vibrator);
        final CompoundButton switchSound = (CompoundButton) findViewById(R.id.switch_sound);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.groupe);
        RadioButton radioButtonFirebase = (RadioButton)findViewById(R.id.radioButton);
        RadioButton radioButtonGoogle = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton radioButtonXZing = (RadioButton)findViewById(R.id.radioButton3);


        if(MainActivity.activeScan == 2131296395){
           radioButtonFirebase.setChecked(true);
        }
        else if (MainActivity.activeScan == 2131296397){
           radioButtonXZing.setChecked(true);

        }
        else {
            Log.d("ID", "onCreate: "+MainActivity.activeScan);
            radioButtonGoogle.setChecked(true);

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("id", "onCheckedChanged: "+i);
                MainActivity.activeScan = i;
            }
        });
        if(MainActivity.flash) switchFlash.setChecked(true);
        if(MainActivity.sound) switchSound.setChecked(true);
        if(MainActivity.vibratorSwitch) switchVibrator.setChecked(true);
        switchFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.flash = switchFlash.isChecked();
            }
        });
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.sound = switchSound.isChecked();
            }
        });
        switchVibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.vibratorSwitch = switchVibrator.isChecked();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
