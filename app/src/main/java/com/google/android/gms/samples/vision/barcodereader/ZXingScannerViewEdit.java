package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//import me.dm7.barcodescanner.core.BarcodeScannerView;
//import me.dm7.barcodescanner.core.DisplayUtils;

public class ZXingScannerViewEdit extends BarcodeScannerView {
    private static final String TAG = "ZXingScannerView";
    private MultiFormatReader mMultiFormatReader;
    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList();
    private List<BarcodeFormat> mFormats;
    private com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.ResultHandler mResultHandler;
    private Result rawResult;
    public ZXingScannerViewEdit(Context context) {
        super(context);
        this.initMultiFormatReader();
    }

    public ZXingScannerViewEdit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.initMultiFormatReader();
    }

    public void setFormats(List<BarcodeFormat> formats) {
        this.mFormats = formats;
        this.initMultiFormatReader();
    }

    public void setResultHandler(com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.ResultHandler resultHandler) {
        this.mResultHandler = resultHandler;
    }

    public Collection<BarcodeFormat> getFormats() {
        return this.mFormats == null ? ALL_FORMATS : this.mFormats;
    }

    private void initMultiFormatReader() {
        Map<DecodeHintType, Object> hints = new EnumMap(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, this.getFormats());
        this.mMultiFormatReader = new MultiFormatReader();
        this.mMultiFormatReader.setHints(hints);
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (this.mResultHandler != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = parameters.getPreviewSize();
                int width = size.width;
                int height = size.height;
                if (DisplayUtils.getScreenOrientation(this.getContext()) == 1) {
                    byte[] rotatedData = new byte[data.length];
                    int y = 0;

                    while(true) {
                        if (y >= height) {
                            y = width;
                            width = height;
                            height = y;
                            data = rotatedData;
                            break;
                        }

                        for(int x = 0; x < width; ++x) {
                            rotatedData[x * height + height - y - 1] = data[x + y * width];
                        }

                        ++y;
                    }
                }

                rawResult = null;
                PlanarYUVLuminanceSource source = this.buildLuminanceSource(data, width, height);
                if (source != null) {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        rawResult = this.mMultiFormatReader.decodeWithState(bitmap);
                    } catch (ReaderException var17) {
                        ;
                    } catch (NullPointerException var18) {
                        ;
                    } catch (ArrayIndexOutOfBoundsException var19) {
                        ;
                    } finally {
                        this.mMultiFormatReader.reset();
                    }
                }

                if (rawResult != null) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.ResultHandler tmpResultHandler = com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.this.mResultHandler;
                            com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.this.mResultHandler = null;
                            com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.this.stopCameraPreview();
                            if (tmpResultHandler != null) {
                                tmpResultHandler.handleResult(rawResult);
                            }

                        }
                    });
                } else {
                    camera.setOneShotPreviewCallback(this);
                }
            } catch (RuntimeException var21) {
                Log.e("ZXingScannerView", var21.toString(), var21);
            }

        }
    }

    public void resumeCameraPreview(com.google.android.gms.samples.vision.barcodereader.ZXingScannerViewEdit.ResultHandler resultHandler) {
        this.mResultHandler = resultHandler;
        super.resumeCameraPreview();
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = this.getFramingRectInPreview(width, height);
        if (rect == null) {
            return null;
        } else {
            PlanarYUVLuminanceSource source = null;

            try {
                source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
            } catch (Exception var7) {
                ;
            }

            return source;
        }
    }

    static {
     //  ALL_FORMATS.add(BarcodeFormat.UPC_A);
      // ALL_FORMATS.add(BarcodeFormat.UPC_E);
        ALL_FORMATS.add(BarcodeFormat.EAN_13);
        //ALL_FORMATS.add(BarcodeFormat.EAN_8);
      //  ALL_FORMATS.add(BarcodeFormat.RSS_14);
        ALL_FORMATS.add(BarcodeFormat.CODE_39);
        /*ALL_FORMATS.add(BarcodeFormat.CODE_93);
        ALL_FORMATS.add(BarcodeFormat.CODE_128);
        ALL_FORMATS.add(BarcodeFormat.ITF);
        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.QR_CODE);
        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX);
        ALL_FORMATS.add(BarcodeFormat.PDF_417);*/
    }

    public interface ResultHandler {
        void handleResult(Result var1);
    }
}
