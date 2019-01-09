package edu.virginia.ece.inertia.besic.mementoopt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.virginia.ece.inertia.besic.mementoopt.utils.FileUtil.saveStringToFile;


public class PainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memento_pain);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);


    }


    public void backClick(View v) {
        startActivity(new Intent(this, ClockfaceActivity.class));

    }

    public void painClick(View v) {
        //Log.i("EMA_FLAG = ", Integer.toString(EMA_FLAG));
        //EMA_FLAG = 1;

        long yourmilliseconds = System.currentTimeMillis();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date resultdate = new Date(yourmilliseconds);
        //System.out.println(sdf.format(resultdate));

        //String time = System.currentTimeMillis();
        //String painText = "PAIN, " + resultdate + "\n";
        String painText = "PAIN, , " + sdf.format(resultdate) + "\n";
        String fileName = "pain";
        FileOutputStream fos = null;

        saveStringToFile(fileName, painText);
        //Toast.makeText(this, "Pain event marked!", Toast.LENGTH_SHORT).show();


        startActivity(new Intent(this, EMAActivity.class));

    }



}