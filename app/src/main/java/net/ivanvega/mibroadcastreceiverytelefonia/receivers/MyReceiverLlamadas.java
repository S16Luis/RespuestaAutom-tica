package net.ivanvega.mibroadcastreceiverytelefonia.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyReceiverLlamadas extends BroadcastReceiver {
    private String num;
    private String msg;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences p = context.getSharedPreferences("datos",Context.MODE_PRIVATE);
            num=p.getString("telefono","");
            msg=p.getString("mensaje","");
            Toast.makeText(context, num, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Toast.makeText(context, "Llamada Activa", Toast.LENGTH_LONG).show();
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                Toast.makeText(context, "Llamada Cancelada", Toast.LENGTH_LONG).show();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(num, null, msg,
                        null, null);
                Toast.makeText(context, "Mensaje enviado al numero: "+num, Toast.LENGTH_LONG).show();
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, "Estan Llamando!!!", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

