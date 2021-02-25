package net.ivanvega.mibroadcastreceiverytelefonia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MiReceiverTelefonia;
import net.ivanvega.mibroadcastreceiverytelefonia.receivers.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    MyBroadcastReceiver myBroadcastReceiver= new MyBroadcastReceiver();
    MiReceiverTelefonia miReceiverTelefonia = new MiReceiverTelefonia();
    Button btnS;
    EditText txtTel, txtMessage;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnS = findViewById(R.id.btnSend);
        txtTel = findViewById(R.id.txtPhone);
        txtMessage = findViewById(R.id.txtTexto);
        preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        txtTel.setText(preferences.getString("telefono",""));
        txtMessage.setText(preferences.getString("mensaje",""));
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcast = new Intent();
                broadcast.setAction(getString(R.string.action_broadcast));
                broadcast.putExtra("key1", "parametro de la difusion");
                sendBroadcast(broadcast);
            }
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(getString(R.string.action_broadcast));
        this.registerReceiver(myBroadcastReceiver, filter);
        IntentFilter intentFilterTel = new IntentFilter(Telephony.Sms .Intents.SMS_RECEIVED_ACTION);
        getApplicationContext().registerReceiver(miReceiverTelefonia,
                intentFilterTel
        );
    }

    private void enviarSMS(String tel, String msj) {
        SmsManager smsManager =  SmsManager.getDefault();

        smsManager.sendTextMessage(tel,null, msj,
                null, null);



        Toast.makeText(
                this, "Mensaje enviado",
                Toast.LENGTH_LONG
        ).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcastReceiver);
    }

    public void btnSMS_onclick(View v){
        enviarSMS(txtTel.getText().toString(), txtMessage.getText().toString());
    }

    public void GuardarDatos(View v){
        Intent broadcastL = new Intent();
        broadcastL.setAction(getString(R.string.action_broadcast_llamadas));
        sendBroadcast(broadcastL);
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("telefono",txtTel.getText().toString());
        editor.putString("mensaje",txtMessage.getText().toString());
        editor.commit();
    }

}