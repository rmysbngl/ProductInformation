package com.example.rumeysal.productinformation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BTConnect extends AppCompatActivity {
    static BluetoothSPP bt;
    TextView textStatus;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);
        textStatus=(TextView) findViewById(R.id.BTStatus) ;
        bt=new BluetoothSPP(this);
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                textStatus.setText("Status : Not connect");

                //bt.connect(MAC_ADDRESS);
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_urunyerlestir, menu);
            }

            public void onDeviceConnectionFailed() {
                textStatus.setText("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                textStatus.setText("Status : Connected to " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_urunyerlestir, menu);
            }
        });
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(true)
                .setMessage("Cihaz Ekle")
                .setPositiveButton("Cihaz Ekle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
                        Intent intent2 = new Intent(getApplicationContext(), DeviceList.class);
                        startActivityForResult(intent2, BluetoothState.REQUEST_CONNECT_DEVICE);

                    }
                })
                .setNegativeButton("Geri Dön", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(BTConnect.this,UrunYerlestir.class);
                        startActivity(intent);
                    }
                });
        dialog.show();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_product) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_urunyerlestir, menu);
        return true;
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);

            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);

            //ss.setSenData(sendData);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                //  setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public static BluetoothSPP getBt() {
        return bt;
    }

    public void NextPage(View view) {
        switch(view.getId()){
            case(R.id.NextPage):
                Intent intent=new Intent(BTConnect.this,ArduinoGiris.class);
                startActivity(intent);
                break;


        }
    }
}
