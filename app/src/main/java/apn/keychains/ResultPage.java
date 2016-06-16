package apn.keychains;
import java.io.File;
import java.io.FileOutputStream;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ResultPage extends MainActivity {


    private String value;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private String stringLabel;
    private String format;
    private BarcodeFormat barcodeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);

        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        scroll.addView(l);

        setContentView(scroll);
        // barcode data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("num");
            stringLabel = extras.getString("Label");
            format = extras.getString("format");

        }
        String barcode_data = value;
        String barcode_label = stringLabel;
        String barcode_format = format;


        if (format.equals("CODE_39")) {
            barcodeFormat = BarcodeFormat.CODE_39;
        } else if (format.equals("CODE_128")) {
            barcodeFormat = BarcodeFormat.CODE_128;
        } else if (format.equals("CODE_93")) {
            barcodeFormat = BarcodeFormat.CODE_39;
        } else if (format.equals("AZTEC")) {
            barcodeFormat = BarcodeFormat.AZTEC;
        } else if (format.equals("CODEBAR")) {
            barcodeFormat = BarcodeFormat.CODABAR;
        } else if (format.equals("DATA_MATRIX")) {
            barcodeFormat = BarcodeFormat.DATA_MATRIX;
        } else if (format.equals("EAN_8")) {
            barcodeFormat = BarcodeFormat.EAN_8;
        } else if (format.equals("EAN_13")) {
            barcodeFormat = BarcodeFormat.EAN_13;
        } else if (format.equals("ITF")) {
            barcodeFormat = BarcodeFormat.ITF;
        } else if (format.equals("MAXICODE")) {
            barcodeFormat = BarcodeFormat.MAXICODE;
        } else if (format.equals("PDF_417")) {
            barcodeFormat = BarcodeFormat.PDF_417;
        } else if (format.equals("QR_CODE")) {
            barcodeFormat = BarcodeFormat.QR_CODE;
        } else if (format.equals("RSS_14")) {
            barcodeFormat = BarcodeFormat.RSS_14;
        } else if (format.equals("RSS_EXPANDED")) {
            barcodeFormat = BarcodeFormat.RSS_EXPANDED;
        } else if (format.equals("UPC_A")) {
            barcodeFormat = BarcodeFormat.UPC_A;
        } else if (format.equals("UPC_E")) {
            barcodeFormat = BarcodeFormat.UPC_E;
        } else if (format.equals("UPC_EAN_EXTENSION")) {
            barcodeFormat = BarcodeFormat.UPC_EAN_EXTENSION;
        }


        // barcode image
        Bitmap bitmap = null;
        ImageView iv = new ImageView(this);


        try {

            bitmap = encodeAsBitmap(barcode_data, barcodeFormat, 600, 300);
            //     iv.setImageBitmap(bitmap);
            SaveImage(bitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        }

        l.addView(iv);


        //barcode text
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);


        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/saved_images");

        for (File file : myDir.listFiles()) {
            ImageView newImage = new ImageView(this);// displaying all the pictures
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            newImage.setImageBitmap(bm);
            l.addView(newImage);
            //barcode text
            TextView tv2 = new TextView(this);
            tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tv2.setText(file.getName().toString().substring(0,file.getName().toString().indexOf(".")));
            l.addView(tv2);

        }
    }
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap MadeBarcode = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        MadeBarcode.setPixels(pixels, 0, width, 0, 0, width, height);
        return MadeBarcode;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
    public void SaveImage(Bitmap finalBitmap) {
//
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String fname = stringLabel+".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
}


