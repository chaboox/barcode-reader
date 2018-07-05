package com.google.android.gms.samples.vision.barcodereader.Firebase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.Firebase.GraphicOverlay.Graphic;
import com.google.android.gms.samples.vision.barcodereader.MainActivity;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

public class BarcodeGraphic extends Graphic {

    private static final int TEXT_COLOR = Color.WHITE;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;

    private final Paint rectPaint;
    private final Paint barcodePaint;
    private final FirebaseVisionBarcode barcode;
    private Toast toast;
    BarcodeGraphic(GraphicOverlay overlay, FirebaseVisionBarcode barcode) {
        super(overlay);

        this.barcode = barcode;

        rectPaint = new Paint();
        rectPaint.setColor(TEXT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        barcodePaint = new Paint();
        barcodePaint.setColor(TEXT_COLOR);
        barcodePaint.setTextSize(TEXT_SIZE);
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    /**
     * Draws the barcode block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (barcode == null) {
            throw new IllegalStateException("Attempting to draw a null barcode.");
        }

        // Draws the bounding box around the BarcodeBlock.
        RectF rect = new RectF(barcode.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        if(rect.right-rect.left > rect.bottom - rect.top)
        canvas.drawLine(rect.left, (rect.top + rect.bottom)/2, rect.right, (rect.top + rect.bottom)/2, rectPaint);
        else
            canvas.drawLine((rect.left +rect.right)/2, rect.top , (rect.left +rect.right)/2,  rect.bottom, rectPaint);
       // canvas.drawRect(rect, rectPaint);

        // Renders the barcode at the bottom of the box.
       // canvas.drawText(barcode.getRawValue(), rect.left, rect.bottom, barcodePaint);
        if(!MainActivity.barcodeDisplay.contains(barcode.getDisplayValue())){
            MainActivity.barcodeDisplay.add(barcode.getDisplayValue());
            if(toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), barcode.getDisplayValue(), Toast.LENGTH_SHORT);
            toast.show();
            Vibrator vibrator = (Vibrator) LivePreviewActivity.c.getSystemService(Context.VIBRATOR_SERVICE);
            // if (vibrator.hasVibrator())
            vibrator.vibrate(200); // for 200 ms
        }
    }
}
