package edu.virginia.ece.inertia.besic.mementoopt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;


public class WaitingRoomActivity3 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memento_waiting_room_3);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Intent EMA_intent = new Intent(this, WaitingRoomActivity2.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, 30, EMA_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //AlarmManager.cancel(pIntent);

        ((AlarmManager) getSystemService(ALARM_SERVICE)).cancel(pIntent);


    }


    public void dismissClick(View v) {
        startActivity(new Intent(this, ClockfaceActivity.class));

    }

    public void proceed2EMAClick(View v) {
        startActivity(new Intent(this, dEMAActivity.class));

    }



}