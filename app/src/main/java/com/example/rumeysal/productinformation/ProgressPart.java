package com.example.rumeysal.productinformation;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


public class ProgressPart extends AppCompatActivity  {
    static int counter=0;
    BluetoothSPP bt;
    static float value=0;
    int checkValue=1;
    ProgressBar timer;
    MyCountDownTimer CDT;
    static int minuteProgress;
    int minute;
    TextView değer;
    public static  ArrayList<Float> vac=new ArrayList<>();
    public static void setVacuumNew(int vacuumNew) {
        VacuumNew = vacuumNew;
    }

    static int VacuumNew;



    private LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_part);
        timer=(ProgressBar) findViewById(R.id.progressBar) ;
        minute=minuteProgress;
        bt=(BTConnect.getBt());
        değer=(TextView) findViewById(R.id.Values) ;
        mChart=(LineChart) findViewById(R.id.graph);
        mChart.setDescription("");
        mChart.setNoDataText("No data");

        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.BLACK);

        LineData data=new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);


        Legend box=mChart.getLegend();
        box.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);
        box.setXEntrySpace(5);
        box.setYEntrySpace(5);
        box.setTextColor(Color.BLUE);


        Legend l=mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis Xl=mChart.getXAxis();
        Xl.setTextColor(Color.WHITE);
        Xl.setDrawGridLines(false);
        Xl.setAvoidFirstLastClipping(false);



        YAxis Yl=mChart.getAxisLeft();
        Yl.setDrawGridLines(false);
        Yl.setTextColor(Color.WHITE);
        Yl.setStartAtZero(false);
        Yl.setAxisMaxValue(0f);
        Yl.setAxisMinValue(-100f);

        YAxis Yaxis=mChart.getAxisRight();
        Yaxis.setEnabled(false);

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.d("Message",message);
               String[]veri= message.split(":");
                if(veri[0].equals("BT")){

                        value=Float.valueOf(veri[1].trim());

                   }
                if((message.trim()).equals("finish")){
                    bt.stopService();
                    timer.setProgress(100);
                    CDT=new MyCountDownTimer(minuteProgress,1000);
                    CDT.start();


                }




        }});




    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(value!=checkValue){

                                createSet().setValueTextSize(10f);
                                 addEntry();
                                    değer.setText(String.valueOf(value));
                                vac.add(value);
                                value=checkValue;


                            }

                        }
                    });
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (value!=(VacuumNew+1));

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }




    public void addEntry(){
        LineData data=mChart.getLineData();

        if(data!=null){
            LineDataSet set =data.getDataSetByIndex(0);

            if(set==null){
                set=createSet();
                data.addDataSet(set);

            }
            data.addXValue("");
            data.addEntry(new Entry(value,set.getEntryCount()),0);
            data.setValueTextSize(10f);

            //createSet().setValueTextSize(10f);

            mChart.notifyDataSetChanged();
           mChart.setVisibleXRange(200);

            mChart.moveViewToX(data.getXValCount());
        }
    }
    private LineDataSet createSet(){
        LineDataSet set=new LineDataSet(null,"Vacuum");





        set.setColor(Color.BLUE);
        set.setLineWidth(1f);
        set.setCircleSize(1f);
        set.setCircleColor(Color.BLUE);
        set.setCircleColorHole(Color.BLUE);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.BLUE);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(0f);




        return set;
    }

    public void Click(View view) {
     switch (view.getId()){
            case(R.id.Anasayfa):
                Intent intent=new Intent(ProgressPart.this,GirisEkrani.class);
                startActivity(intent);
                break;
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/100);
            timer.setProgress(progress);
        }

        @Override
        public void onFinish() {
            Toast.makeText(ProgressPart.this, "Task completed", Toast.LENGTH_SHORT).show();
            timer.setProgress(100);
            AlertDialog.Builder dialog=new AlertDialog.Builder(ProgressPart.this);
            dialog.setMessage("Task Completed")
                    .setCancelable(true)
                    .setPositiveButton("Go Back Main Page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(ProgressPart.this,GirisEkrani.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("View Screen",null);
            dialog.show();

        }

    }

    public static void setMinuteProgress(int minuteProgress) {
        ProgressPart.minuteProgress = minuteProgress;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ProgressPart.this,GirisEkrani.class);
        startActivity(intent);
    }
}


