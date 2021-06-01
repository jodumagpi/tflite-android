package com.example.housetflite;

import org.tensorflow.lite.Interpreter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText crim, zone, indus, chas, nox, room, age, dis, rad, tax, pratio, black, stat;
    float[][] input = new float[1][13];
    Button predict;
    float[][] result = new float[1][1];
    Interpreter model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of your trained model
        try {
            model = new Interpreter(loadModelFile());
        } catch (Exception e){
            e.printStackTrace();
        }

        // Declare button
        predict = (Button) findViewById(R.id.predict);

        // Declare fields
        crim = (EditText) findViewById(R.id.crim);
        zone = (EditText) findViewById(R.id.zn);
        indus = (EditText) findViewById(R.id.indus);
        chas = (EditText) findViewById(R.id.chas);
        nox = (EditText) findViewById(R.id.nox);
        room = (EditText) findViewById(R.id.rm);
        age = (EditText) findViewById(R.id.age);
        dis = (EditText) findViewById(R.id.dis);
        rad = (EditText) findViewById(R.id.rad);
        tax = (EditText) findViewById(R.id.tax);
        pratio = (EditText) findViewById(R.id.ptratio);
        black = (EditText) findViewById(R.id.black);
        stat = (EditText) findViewById(R.id.lstat);

        // Set the button action
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure there are no empty fields
                if (crim.getText().toString().equals("") ||
                        crim.getText().toString().equals("") ||
                        zone.getText().toString().equals("") ||
                        indus.getText().toString().equals("") ||
                        chas.getText().toString().equals("") ||
                        nox.getText().toString().equals("") ||
                        room.getText().toString().equals("") ||
                        age.getText().toString().equals("") ||
                        dis.getText().toString().equals("") ||
                        rad.getText().toString().equals("") ||
                        tax.getText().toString().equals("") ||
                        pratio.getText().toString().equals("") ||
                        black.getText().toString().equals("") ||
                        stat.getText().toString().equals("")) {
                    // We do not accept empty fields
                    Toast.makeText(MainActivity.this, "Please fill in all input fields.",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Get input values
                    input[0][0] = Float.parseFloat(crim.getText().toString());
                    input[0][1] = Float.parseFloat(zone.getText().toString());
                    input[0][2] = Float.parseFloat(indus.getText().toString());
                    input[0][3] = Float.parseFloat(chas.getText().toString());
                    input[0][4] = Float.parseFloat(nox.getText().toString());
                    input[0][5] = Float.parseFloat(room.getText().toString());
                    input[0][6] = Float.parseFloat(age.getText().toString());
                    input[0][7] = Float.parseFloat(dis.getText().toString());
                    input[0][8] = Float.parseFloat(rad.getText().toString());
                    input[0][9] = Float.parseFloat(tax.getText().toString());
                    input[0][10] = Float.parseFloat(pratio.getText().toString());
                    input[0][11] = Float.parseFloat(black.getText().toString());
                    input[0][12] = Float.parseFloat(stat.getText().toString());

                    // run the inference
                    model.run(input, result);
                    // show result in a dialog box
                    showResult(String.valueOf(result[0][0]));
                }
            }
        });

    }

    // method to show the dialog box
    private void showResult(String result) {
        AlertDialog.Builder pred = new AlertDialog.Builder(this);
        pred.setTitle("Inference Result");
        pred.setMessage("Median Value : " + result);
        pred.create().show();
        // clear fields
        crim.setText("");
        zone.setText("");
        indus.setText("");
        chas.setText("");
        nox.setText("");
        room.setText("");
        age.setText("");
        dis.setText("");
        rad.setText("");
        tax.setText("");
        pratio.setText("");
        black.setText("");
        stat.setText("");
    }

    // method to load model
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("house_reg_model.tflite");
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffSets = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSets, declaredLength);
    }
}