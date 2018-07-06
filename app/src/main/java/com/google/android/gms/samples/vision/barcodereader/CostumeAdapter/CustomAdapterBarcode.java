package com.google.android.gms.samples.vision.barcodereader.CostumeAdapter;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.MainActivity;
import com.google.android.gms.samples.vision.barcodereader.R;

import java.util.ArrayList;

public class CustomAdapterBarcode extends ArrayAdapter<DataModelBarcode> implements View.OnClickListener{

    private ArrayList<DataModelBarcode> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCode;
        TextView txtFormat;
        ImageView info;
    }



    public CustomAdapterBarcode(ArrayList<DataModelBarcode> data, Context context) {
        super(context, R.layout.row_item_barcode, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelBarcode dataModelBarcode =(DataModelBarcode)object;




        switch (v.getId())
        {

            case R.id.item_info:
    /*               MainActivity.barcodeDisplay.remove(MainActivity.barcodeDisplay.indexOf(dataModelBarcode.code));*/
            /*   Snackbar.make(v, " " + dataModelBarcode.code+ " deleted ", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelBarcode dataModelBarcode = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_barcode, parent, false);
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.code);
            viewHolder.txtFormat = (TextView) convertView.findViewById(R.id.format);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtCode.setText(dataModelBarcode.getCode());
        viewHolder.txtFormat.setText(dataModelBarcode.getFormat());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
