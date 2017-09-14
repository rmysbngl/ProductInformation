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
    ProgressBar timer;
    MyCountDownTimer CDT;

    static int minuteProgress;
    int minute;     //Arduino Giriş sayfasından gelen süre değeri
    TextView değerV;
    TextView değerP;
    TextView değerG;

    static float vacuumValue;
    static float plasmaValue;
    static float gasValue;

    int checkValue=1;

    private LineChart mChart;
    private LineChart chartPlasma;
    private LineChart chartGas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_part);

        timer=(ProgressBar) findViewById(R.id.progressBar) ;            //Progress bar ın kurulumu
        timer.setProgress(100);
        minute=minuteProgress;
        bt=(BTConnect.getBt());

        değerV = (TextView) findViewById(R.id.Values);
/*      değerP = (TextView) findViewById(R.id.txtPlasmaValue);
        değerG = (TextView) findViewById(R.id.txtGasValue);*/

        vacuumValue = 0;
/*        plasmaValue = 0;
        gasValue = 0;*/

        //TODO: Tüpün içindeki değer her zaman sabitse bu değeri ona göre kur

        drawVacuumGraph();
/*        drawPlasmaGraph();
        drawGasGraph();*/

        //Bluetooth üzerinden veri almak için

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.d("Message",message);
               String[]veri= message.split(":");
                //TODO: Eğer vacuum değeri alınıyorsa, alınan verinin formu  (BT: "vacuum değeri" ) şeklinde olmalıdır. iki nokta ve BT kısmı önemli
                if(veri[0].equals("BT")){
                        vacuumValue=Float.valueOf(veri[1].trim());        //alınan veri 'value' ya kurulup sonra 'value' plot ediliyor
                        /*plasmaValue=Float.valueOf(veri[1].trim());*/
/*                        gasValue=Float.valueOf(veri[1].trim());*/
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
                            if(vacuumValue!=checkValue){  //Grafiğe yeni bir değerin girdiğini farketmesi için yapıldı. Diğer türlü aynı veriden 3 kere giriliyordu
                                 addEntry();        //Grafiğe veriyi yazdırmak için
                                 değerV.setText(String.valueOf(vacuumValue));           //ekranda yazılan veriyi göstermek için
                                vacuumValue=checkValue;
                            }

                        }
                    });
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (vacuumValue!=(VacuumNew+1)); //Ayarlanan vakum değerinde grafiğin yazımının bitmesi için //TODO: eğer orda yazandan daha farklı bir değerde çalışma ihtimali varsa cihazdan alınan veriye göre düzenlenmesi lazım

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();       //uygulama kapandığında bluetooth u kapatmak için
    }

    public void addEntry(){
        LineData vData = mChart.getLineData();
       /* LineData pData = chartPlasma.getLineData();
        LineData gData = chartGas.getLineData();*/

        if(vData!=null){
            LineDataSet set = vData.getDataSetByIndex(0);

            if(set==null){          //veri yokkende grafiği kurmak için
                set=createSet();
                vData.addDataSet(set);

            }
            vData.addXValue("");
            vData.addEntry(new Entry(vacuumValue,set.getEntryCount()),0); //yeni eklenen verinin grafiğe eklenmesi için
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(200);           //Bütün verilerin aynı grafikte görünmesi için. Eğer veri alındıkça kayması isteniyorsa burdaki sayının  küçüklmesi gerek. Mesela 10'a filan kurulablir.
            mChart.moveViewToX(vData.getXValCount());
        }

        /*if(pData!=null){
            LineDataSet set = pData.getDataSetByIndex(0);

            if(set==null){          //veri yokkende grafiği kurmak için
                set=plasmaDataSet();
                pData.addDataSet(set);

            }
            pData.addXValue("");
            pData.addEntry(new Entry(value,set.getEntryCount()),0); //yeni eklenen verinin grafiğe eklenmesi için
            chartPlasma.notifyDataSetChanged();
            chartPlasma.setVisibleXRange(200);           //Bütün verilerin aynı grafikte görünmesi için. Eğer veri alındıkça kayması isteniyorsa burdaki sayının  küçüklmesi gerek. Mesela 10'a filan kurulablir.
            chartPlasma.moveViewToX(pData.getXValCount());
        }

        if(gData!=null){
            LineDataSet set = gData.getDataSetByIndex(0);

            if(set==null){          //veri yokkende grafiği kurmak için
                set=gasDataSet();
                gData.addDataSet(set);

            }
            gData.addXValue("");
            gData.addEntry(new Entry(value,set.getEntryCount()),0); //yeni eklenen verinin grafiğe eklenmesi için
            chartGas.notifyDataSetChanged();
            chartGas.setVisibleXRange(200);           //Bütün verilerin aynı grafikte görünmesi için. Eğer veri alındıkça kayması isteniyorsa burdaki sayının  küçüklmesi gerek. Mesela 10'a filan kurulablir.
            chartGas.moveViewToX(gData.getXValCount());
        }
*/
    }

    private LineDataSet createSet(){
        LineDataSet set=new LineDataSet(null,"Vacuum");
        set.setColor(Color.parseColor("#2196F3"));
        set.setLineWidth(1f);
        set.setCircleSize(1f);
        set.setCircleColor(Color.parseColor("#2196F3"));
        set.setCircleColorHole(Color.parseColor("#2196F3"));
        set.setFillColor(Color.parseColor("#2196F3"));
        set.setHighLightColor(Color.parseColor("#2196F3"));
        set.setValueTextColor(Color.parseColor("#ECEFF1"));
        set.setValueTextSize(0f);

        return set;
    }

  /*  private LineDataSet plasmaDataSet() {

        LineDataSet set = new LineDataSet(null, "Plasma");
        set.setColor(Color.parseColor("#D50000"));
        set.setLineWidth(1f);
        set.setCircleSize(1f);
        set.setCircleColor(Color.parseColor("#D50000"));
        set.setCircleColorHole(Color.parseColor("#D50000"));
        set.setFillColor(Color.parseColor("#D50000"));
        set.setHighLightColor(Color.parseColor("#D50000"));
        set.setValueTextColor(Color.parseColor("#212121"));
        set.setValueTextSize(0f);

        return set;
    }

    private LineDataSet gasDataSet() {

        LineDataSet set = new LineDataSet(null, "Gas");
        set.setColor(Color.parseColor("#1B5E20"));
        set.setLineWidth(1f);
        set.setCircleSize(1f);
        set.setCircleColor(Color.parseColor("#1B5E20"));
        set.setCircleColorHole(Color.parseColor("#1B5E20"));
        set.setFillColor(Color.parseColor("#1B5E20"));
        set.setHighLightColor(Color.parseColor("#1B5E20"));
        set.setValueTextColor(Color.parseColor("#212121"));
        set.setValueTextSize(0f);

        return set;
    }*/

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

    public void drawVacuumGraph (){

        //Grafiğin kurulması
        mChart=(LineChart) findViewById(R.id.graphVacuum);
        mChart.setDescription("");
        mChart.setNoDataText("No data");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.parseColor("#E3F2FD"));

        LineData data=new LineData();
        data.setValueTextColor(Color.parseColor("#0D47A1"));
        mChart.setData(data);

        Legend l =mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.parseColor("#0D47A1"));

        XAxis Xl=mChart.getXAxis();
        Xl.setTextColor(Color.parseColor("#0D47A1"));
        Xl.setDrawGridLines(false);
        Xl.setAvoidFirstLastClipping(false);

        YAxis Yl=mChart.getAxisLeft();
        Yl.setDrawGridLines(false);
        Yl.setTextColor(Color.parseColor("#0D47A1"));
        Yl.setStartAtZero(false);
        Yl.setAxisMaxValue(0f);
        Yl.setAxisMinValue(-100f);
        YAxis Yaxis=mChart.getAxisRight();
        Yaxis.setEnabled(false);


    }
/*

    public void drawPlasmaGraph (){

        //Grafiğin kurulması
        mChart=(LineChart) findViewById(R.id.graphPlasma);
        mChart.setDescription("");
        mChart.setNoDataText("No data");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.parseColor("#F9FBE7"));

        LineData data=new LineData();
        data.setValueTextColor(Color.parseColor("#006064"));
        mChart.setData(data);

        Legend l =mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.parseColor("#006064"));

        XAxis Xl=mChart.getXAxis();
        Xl.setTextColor(Color.parseColor("#006064"));
        Xl.setDrawGridLines(false);
        Xl.setAvoidFirstLastClipping(false);

        YAxis Yl=mChart.getAxisLeft();
        Yl.setDrawGridLines(false);
        Yl.setTextColor(Color.parseColor("#006064"));
        Yl.setStartAtZero(false);
        Yl.setAxisMaxValue(0f);
        Yl.setAxisMinValue(-100f);
        YAxis Yaxis=mChart.getAxisRight();
        Yaxis.setEnabled(false);


    }

    public void drawGasGraph (){

        //Grafiğin kurulması
        mChart=(LineChart) findViewById(R.id.graphGas);
        mChart.setDescription("");
        mChart.setNoDataText("No data");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.parseColor("#F9FBE7"));

        LineData data=new LineData();
        data.setValueTextColor(Color.parseColor("#006064"));
        mChart.setData(data);

        Legend l =mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.parseColor("#006064"));

        XAxis Xl=mChart.getXAxis();
        Xl.setTextColor(Color.parseColor("#006064"));
        Xl.setDrawGridLines(false);
        Xl.setAvoidFirstLastClipping(false);

        YAxis Yl=mChart.getAxisLeft();
        Yl.setDrawGridLines(false);
        Yl.setTextColor(Color.parseColor("#006064"));
        Yl.setStartAtZero(false);
        Yl.setAxisMaxValue(0f);
        Yl.setAxisMinValue(-100f);
        YAxis Yaxis=mChart.getAxisRight();
        Yaxis.setEnabled(false);


    }
*/



}


