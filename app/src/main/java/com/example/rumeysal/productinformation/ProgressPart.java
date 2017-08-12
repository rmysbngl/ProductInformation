package com.example.rumeysal.productinformation;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


public class ProgressPart extends AppCompatActivity {

    BluetoothSPP bt;



    ArrayList<Integer> vac=new ArrayList<>();
    LineChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_part);

        bt=(BTConnect.getBt());
        chart=(LineChart) findViewById(R.id.DataChart) ;



        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
              //  textRead.append(message + "\n");
                Log.d("Message",message);
                //Toast.makeText(ProgressPart.this, ""+message, Toast.LENGTH_SHORT).show();
               String[]veri= message.split(":");
                if(veri[0].equals("BT")){
                vac.add(Integer.valueOf(veri[1].trim()));}
                //    Toast.makeText(ProgressPart.this, ""+Integer.valueOf(veri[1].trim()), Toast.LENGTH_SHORT).show();}
                if((message.trim()).equals("finish")){
                    bt.stopService();
                   // Toast.makeText(ProgressPart.this, ""+vacuum.size(), Toast.LENGTH_SHORT).show();
                }



        }});





    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void Send(View view) {
        switch(view.getId()){
            case R.id.Send:
                bt.send("say hi",true);
                break;


        }
    }
}
