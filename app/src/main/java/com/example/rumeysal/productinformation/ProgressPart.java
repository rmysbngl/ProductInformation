package com.example.rumeysal.productinformation;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
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
    BluetoothSPP bt;
    static float value;
    int checkValue=1;
    ProgressBar timer;
    MyCountDownTimer CDT;
    static int minuteProgress;          //Arduino Giriş sayfasından gelen süre değeri
    int minute;
    TextView değer;
    private LineChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_part);
        timer=(ProgressBar) findViewById(R.id.progressBar) ;            //Progress bar ın kurulumu
        timer.setProgress(100);
        minute=minuteProgress;
        bt=(BTConnect.getBt());
        değer=(TextView) findViewById(R.id.Values) ;
        value=0;           //TODO: Tüpün içindeki değer her zaman sabitse bu değeri ona göre kur

       //Grafiğin kurulması
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


        //Bluetooth üzerinden veri almak için
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.d("Message",message);
               String[]veri= message.split(":");  //TODO: Eğer vacuum değeri alınıyorsa, alınan verinin formu  (BT: "vacuum değeri" ) şeklinde olmalıdır. iki nokta ve BT kısmı önemli
                if(veri[0].equals("BT")){
                        value=Float.valueOf(veri[1].trim());        //alınan veri 'value' ya kurulup sonra 'value' plot ediliyor

                   }
                if((message.trim()).equals("finish")){     //TODO: Bittiğini anlamak için 'finish' komutu gönderilmeli
                    bt.stopService();                      //veri alımı bittiği için bluetooth servisi duruyor
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
                            if(value!=checkValue){          //Grafiğe yeni bir değerin girdiğini farketmesi için yapıldı. Diğer türlü aynı veriden 3 kere giriliyordu
                                 addEntry();            //Grafiğe veriyi yazdırmak için
                                 değer.setText(String.valueOf(value));           //ekranda yazılan veriyi göstermek için
                                 value=checkValue;


                            }

                        }
                    });
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (value!=(VacuumNew+1));          //Ayarlanan vakum değerinde grafiğin yazımının bitmesi için //TODO: eğer orda yazandan daha farklı bir değerde çalışma ihtimali varsa cihazdan alınan veriye göre düzenlenmesi lazım

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();       //uygulama kapandığında bluetooth u kapatmak için
    }




    public void addEntry(){
        LineData data=mChart.getLineData();

        if(data!=null){
            LineDataSet set =data.getDataSetByIndex(0);

            if(set==null){          //veri yokkende grafiği kurmak için
                set=createSet();
                data.addDataSet(set);

            }
            data.addXValue("");
            data.addEntry(new Entry(value,set.getEntryCount()),0);          //yeni eklenen verinin grafiğe eklenmesi için
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(200);           //Bütün verilerin aynı grafikte görünmesi için. Eğer veri alındıkça kayması isteniyorsa burdaki sayının  küçüklmesi gerek. Mesela 10'a filan kurulablir.
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
            case(R.id.Anasayfa):                    //Tekrar anaekrana dönmek için
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
            timer.setProgress(0);
            AlertDialog.Builder dialog=new AlertDialog.Builder(ProgressPart.this);
            dialog.setMessage("Task Completed")
                    .setCancelable(true)
                    .setPositiveButton("Go Back Main Page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(ProgressPart.this,GirisEkrani.class);          //Anasayfaya dönmek içn
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("View Screen",null);             //Grafiği görmek için
            dialog.show();

        }

    }

    public static void setMinuteProgress(int minuteProgress) {
        ProgressPart.minuteProgress = minuteProgress;
    }

    //Geri tuşuna basınca bozulmaları önlemek için geri tuşu direkt giriş sayfasına yönlendiriyor
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ProgressPart.this,GirisEkrani.class);
        startActivity(intent);
    }
    public static void setVacuumNew(int vacuumNew) {
        VacuumNew = vacuumNew;
    }

    static int VacuumNew;



}


