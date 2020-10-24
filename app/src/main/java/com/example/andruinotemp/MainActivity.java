package com.example.andruinotemp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andruinotemp.model.TemperatureHumidityMeasure;
import com.example.andruinotemp.tcpip.TcpIpClient;
import com.example.andruinotemp.utils.MeasureThreshold;
import com.example.andruinotemp.utils.Messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTemperatureMeasure;
    private TextView textViewHumidityMeasure;
    private ProgressBar progressBarTemperature;
    private ProgressBar progressBarHumidity;
    private EditText editTextIpAddress;
    private EditText editTextNumberPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textViewTemperatureMeasure = findViewById(R.id.textViewTemperatureMeasure);
        this.textViewHumidityMeasure = findViewById(R.id.textViewHumidityMeasure);
        this.progressBarTemperature = findViewById(R.id.progressBarTemperature);
        this.progressBarHumidity = findViewById(R.id.progressBarHumidity);
        this.editTextIpAddress = findViewById(R.id.editTextIpAddress);
        this.editTextNumberPort = findViewById(R.id.editTextNumberPort);
        this.progressBarTemperature.setMax(50);
        this.progressBarHumidity.setMax(90);
        this.progressBarTemperature.setScaleY(8);
        this.progressBarHumidity.setScaleY(8);
    }
    public void setTextOnTempHumTextViews(TemperatureHumidityMeasure thm){
        final TemperatureHumidityMeasure temperatureHumidityMeasure = thm;
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                textViewTemperatureMeasure.setText(temperatureHumidityMeasure.getTemperature()+"ºC");
                textViewHumidityMeasure.setText(temperatureHumidityMeasure.getHumidity()+"%");
                progressBarTemperature.setProgress((int)Float.parseFloat(temperatureHumidityMeasure.getTemperature()), true);
                progressBarHumidity.setProgress((int)Float.parseFloat(temperatureHumidityMeasure.getHumidity()), true);
            }
        });
    }

    public void setColorOnTempProgressBar(float t){
        final float temperature = t;
        final Context applicationContext = this.getApplicationContext();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int color;
                if(temperature > MeasureThreshold.HOT_TEMPERATURE){
                    color = ContextCompat.getColor(applicationContext, R.color.colorHeatRed);
                }
                else if(temperature <= MeasureThreshold.COLD_TEMPERATURE){
                    color = ContextCompat.getColor(applicationContext, R.color.colorColdBlue);
                }
                else{
                    color = ContextCompat.getColor(applicationContext, R.color.colorMildGreen);
                }
                DrawableCompat.setTint(progressBarTemperature.getProgressDrawable(), color);
            }
        });
    }

    public void setColorOnHumProgressBar(float h){
        final float humidity = h;
        final Context applicationContext = this.getApplicationContext();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int color;
                if(humidity < MeasureThreshold.DRY){
                    color = ContextCompat.getColor(applicationContext, R.color.colorDryOrange);
                }
                else{
                    color = ContextCompat.getColor(applicationContext, R.color.colorWetCyan);
                }
                DrawableCompat.setTint(progressBarHumidity.getProgressDrawable(), color);
            }
        });
    }

    public void onClickButtonConnect(View v){
        final String ipAddress = this.editTextIpAddress.getText().toString();
        final int port = Integer.parseInt(this.editTextNumberPort.getText().toString());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TcpIpClient tcpIpClient = new TcpIpClient();
                try {
                    tcpIpClient.startTcpIpClient(ipAddress, port);
                    DataInputStream in = tcpIpClient.getIn();
                    boolean serverIsAvailable = tcpIpClient.getClientSocket().isConnected();
                    showToast(Messages.SUCCESSFUL_CONNECTION_MSG);
                    while(serverIsAvailable == true){
                        try {
                            String incomingMeasure = in.readUTF();
                            if(incomingMeasure != null){
                                String[] serializedMeasure = incomingMeasure.split("\\|");
                                TemperatureHumidityMeasure temperatureHumidityMeasure = new TemperatureHumidityMeasure(serializedMeasure[0],serializedMeasure[1],serializedMeasure[2]);
                                setTextOnTempHumTextViews(temperatureHumidityMeasure);
                                float t = Float.parseFloat(temperatureHumidityMeasure.getTemperature());
                                float h = Float.parseFloat(temperatureHumidityMeasure.getHumidity());
                                setColorOnTempProgressBar(t);
                                setColorOnHumProgressBar(h);
                            }
                        } catch (IOException ex) {
                            serverIsAvailable = false;
                            showToast(Messages.CONNECTION_ABORTED_MSG);
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (IOException ex) {
                    showToast(ex.getMessage());
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void showToast(String msg){
        final String message = msg;
        final Context applicationContext = this.getApplicationContext();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}