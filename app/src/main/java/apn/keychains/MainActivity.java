package apn.keychains;
import android.os.Environment;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;// this is the number message

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button button;// Private instance variable
    IntentResult result;
    EditText label;
    String stringLabel;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_opening_screen);

        button = (Button) this.findViewById(R.id.scanningbutton);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        label = (EditText) findViewById (R.id.imagelabel);
        stringLabel = label.getText().toString();
        Log.e("Working", stringLabel);
        if(result != null) {
            if(result.getContents() == null && data!=null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent barcodes = new Intent (MainActivity.this, ResultPage.class);
                barcodes.putExtra("num",result.getContents());
                barcodes.putExtra("format", result.getFormatName());
                Log.e("formatname", result.getFormatName());
                barcodes.putExtra("Label",stringLabel);
                Log.e("Working2", stringLabel);
                startActivity(barcodes);


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}