package main.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initQRCodeScanner();
    }

    private void initQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Intent resultIntent = new Intent();
            if (result.getContents() != null) {
                String contenu = result.getContents();
                resultIntent.putExtra("QR_CODE_CONTENT", contenu);

                setResult(RESULT_OK, resultIntent); // Set the result to OK with the QR code content
                finish(); // Close the scanner activity
            } else {
                setResult(RESULT_CANCELED); // Set the result to CANCELED
                Toast.makeText(this, "Scan annulé", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
