package com.example.rumeysal.productinformation;


import android.graphics.Color;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.RelativeLayout;
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
    static int t;
    BluetoothSPP bt;
         static float value;
    int checkValue=1;

   public static  ArrayList<Integer> vac=new ArrayList<>();

private RelativeLayout mainLayout;
    private LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_part);

        bt=(BTConnect.getBt());
    mainLayout=(RelativeLayout)  findViewById(R.id.MainLayout);
        mChart=new LineChart(this);
        mainLayout.addView(mChart);
        mChart.setDescription("");
        mChart.setNoDataText("No data");

        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
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
       // Yl.setAxisMaxValue(120f);
        Yl.setAxisMaxValue(0f);
        Yl.setAxisMinValue(-100f);

        YAxis Yaxis=mChart.getAxisRight();
        Yaxis.setEnabled(false);

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
              //  textRead.append(message + "\n");
                Log.d("Message",message);
                //Toast.makeText(ProgressPart.this, ""+message, Toast.LENGTH_SHORT).show();
               String[]veri= message.split(":");
                if(veri[0].equals("BT")){

                    vac.add(Integer.valueOf(veri[1].trim()));
               //     Toast.makeText(ProgressPart.this, ""+vac.get(0), Toast.LENGTH_SHORT).show();

                   //
                        value=Float.valueOf(veri[1].trim());
                  //  addEntry();

                    //Toast.makeText(ProgressPart.this, ""+Integer.valueOf(veri[1].trim()), Toast.LENGTH_SHORT).show();
              //  new DataPoint(0,Integer.valueOf(veri[1].trim()));}

                   }
                if((message.trim()).equals("finish")){
                    bt.stopService();
                    value=-75;

                   //

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
                            addEntry();
                            value=checkValue;
                            }

                        }
                    });
                    try {
                        Thread.sleep(350);
                      //  Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (value!=75);

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
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(200);
         // mChart.setVisibleXRange(2);
            mChart.moveViewToX(data.getXValCount());
        }
    }
    private LineDataSet createSet(){
        LineDataSet set=new LineDataSet(null,"Vacuum");
        //set.setDrawCubic(true);
      //  set.setCubicIntensity(0.2f);
        //set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.BLUE);
        set.setLineWidth(1f);
        set.setCircleSize(1f);
       // set.setFillAlpha(65);
        set.setCircleColor(Color.BLUE);
        set.setCircleColorHole(Color.BLUE);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.BLUE);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);






        return set;
    }

}
