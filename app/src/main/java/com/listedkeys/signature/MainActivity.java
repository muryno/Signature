package com.listedkeys.signature;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn_get_sign, mClear, mGetSign, mCancel;

    File file;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap bitmap;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to open signature panel
        btn_get_sign = findViewById(R.id.signature);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        // Dialog Function
        dialog = new Dialog(MainActivity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        btn_get_sign.setOnClickListener(v -> {
            // Function call for Digital Signature
            dialog_action();

        });
    }


    // Function for Digital Signature
    public void dialog_action() {

        mContent = dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = dialog.findViewById(R.id.clear);
        mGetSign = dialog.findViewById(R.id.getsign);
        mCancel = dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(v -> {
            Log.v("log_tag", "Panel Cleared");
            mSignature.clear();

        });

        mGetSign.setOnClickListener(v -> {

            Log.v("log_tag", "Panel Saved");
            view.setDrawingCacheEnabled(true);
           save(view, StoredPath);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
            // Calling the same class
            recreate();

        });

        mCancel.setOnClickListener(v -> {
            Log.v("log_tag", "Panel Canceled");
            dialog.dismiss();
            // Calling the same class
            recreate();
        });
        dialog.show();
    }

    public void save(View v, String StoredPath) {
        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        try {
            // Output the file
            FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
            v.draw(canvas);

            // Convert the output file to Image such as .png
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }

    }

}
