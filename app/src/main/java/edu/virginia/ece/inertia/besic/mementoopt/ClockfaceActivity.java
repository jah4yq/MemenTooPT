package edu.virginia.ece.inertia.besic.mementoopt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import edu.virginia.cs.mooncake.wada.utils.DateTimeUtil;
//import edu.virginia.cs.mooncake.wada.utils.WadaUtils;

import edu.virginia.ece.inertia.besic.mementoopt.utils.DateTimeUtil;
import edu.virginia.ece.inertia.besic.mementoopt.utils.WadaUtils;

public class ClockfaceActivity extends Activity {
    String file_tag;
    String mac;
    StringBuilder strBuilder,strbldr;
    Handler handler;
    public int EMA_FLAG;
    public long HR_time;
    //Handler handler= new Handler();
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private static final String FILE_NAME = "pain.txt";

    private TextView batteryTxt;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText("Battery: " + String.valueOf(level) + "%");
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memento_main_clockface);

        batteryTxt = (TextView) this.findViewById(R.id.batteryTxt);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakeLockTag");
        wakeLock.acquire();

        //Log.i("EMA_FLAG = ", Integer.toString(EMA_FLAG));

        //count = 0;
        //maxCount = 60 * 300;

        strBuilder = new StringBuilder();

        TextView date = (TextView)findViewById(R.id.dateView);
        setDate(date);


//WORKING SENSOR CODE

        /*
        Intent i = new Intent(this, WatchSensorService.class);
        //String tag =   mac + "-" + WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');
        String tag = WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');

        i.putExtra("start", tag);
        startService(i);
*/
/*
        Intent j = new Intent(this, StepSensorService.class);
        //String tag =   mac + "-" + WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');
        String jtag = WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');

        j.putExtra("start", jtag);
        startService(j);
*/

        startHR();
        startStep();
        startEstimote();
        //startAccel();


 ///////////////////////////////////////////////////////////////////////////////////////////////////

// Create the Handler object (on the main thread by default)
        final Handler handler = new Handler();
// Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread


                startHR();
                startEstimote();

                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object

                handler.postDelayed(this, 45000);

            }

        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);


        //Begin nested handler
        final Handler nestedhandler = new Handler();
        Runnable nestedrunnable = new Runnable(){
            @Override
            public void run(){

                stopHR();
                stopEstimote();



                nestedhandler.postDelayed(this, 60000);


            }


        };
        nestedhandler.post(nestedrunnable);
        //End nested handler
         /////////////////////////////////////////////////////////////////////////////////////////

/*
        Intent Intent1 = new Intent(this, proximityService.class); //ENABLE THIS!!!
        Intent1.setAction("ACTION_START_SERVICE"); //ENABLE THIS!!!
        //startForegroundService(Intent1);
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        startService(Intent1); //ENABLE THIS!!!
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
*/


// Schedule Daily EMA below


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,30);


        //Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        Intent intent = new Intent(this, WaitingRoomActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);




    }



    public void stopClick(View v) {
        startActivity(new Intent(this, WatchMainActivity.class));


        //Stop Sensor Service
/*
        Intent i = new Intent(this, WatchSensorService.class);
        i.putExtra("stop", "" + System.currentTimeMillis());
        startService(i);
*/

        /*
        Intent j = new Intent(this, StepSensorService.class);
        j.putExtra("stop", "" + System.currentTimeMillis());
        startService(j);
*/
        /*
        Intent k = new Intent(this, HRSensorService.class);
        k.putExtra("stop", "" + System.currentTimeMillis());
        startService(k);
*/
        stopHR();
        stopStep();
        stopEstimote();
        //stopAccel();
/*
        Intent Intent1 = new Intent(this, proximityService.class);
        Intent1.setAction("ACTION_STOP_SERVICE");
        startService(Intent1);
*/
    }

    public void goClick(View v) {
    //public void painClick(Intent intent, int flags, int startId){
        /*
        Log.i("EMA_FLAG = ", Integer.toString(EMA_FLAG));
        EMA_FLAG = 1;

        long yourmilliseconds = System.currentTimeMillis();
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date resultdate = new Date(yourmilliseconds);
        //System.out.println(sdf.format(resultdate));

        //String time = System.currentTimeMillis();
        //String painText = "PAIN, " + resultdate + "\n";
        String painText = "PAIN, " + sdf.format(resultdate) + "\n";
        String fileName = "pain";
        FileOutputStream fos = null;

        saveStringToFile(fileName, painText);
        //Toast.makeText(this, "Pain event marked!", Toast.LENGTH_SHORT).show();
        */

        startActivity(new Intent(this, PainActivity.class));

        //Log.i("EMA_FLAG = ", Integer.toString(EMA_FLAG));


    }

    public void setDate (TextView view){
/*
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
        */
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(yourmilliseconds);

        TextView textView = (TextView)findViewById(R.id.dateView);
        //SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd,yyyy");
        String ds = date.format(resultdate);

        textView.setText(ds);

        //dailyEMA();

    }

    public void startHR (){
        final Intent k = new Intent(this, HRSensorService.class);
        //String tag =   mac + "-" + WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');
        final String ktag = WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');

        k.putExtra("start", ktag);
        startService(k);
    }

    public void stopHR (){
        Intent k = new Intent(this, HRSensorService.class);
        k.putExtra("stop", "" + System.currentTimeMillis());
        startService(k);
    }

    public void startStep (){
        Intent j = new Intent(this, StepSensorService.class);
        //String tag =   mac + "-" + WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');
        String jtag = WadaUtils.getTag(this.getApplicationContext()) +"-" +DateTimeUtil.getDateTimeString(System.currentTimeMillis(), 3).replace(' ','-');

        j.putExtra("start", jtag);
        startService(j);
    }

    public void stopStep (){
        Intent j = new Intent(this, StepSensorService.class);
        j.putExtra("stop", "" + System.currentTimeMillis());
        startService(j);
    }
    public void startEstimote (){
        Intent Intent1 = new Intent(this, proximityService.class); //ENABLE THIS!!!
        Intent1.setAction("ACTION_START_SERVICE"); //ENABLE THIS!!!
        //startForegroundService(Intent1);
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        startService(Intent1); //ENABLE THIS!!!
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
    }

    public void stopEstimote (){
        Intent Intent1 = new Intent(this, proximityService.class);
        Intent1.setAction("ACTION_STOP_SERVICE");
        startService(Intent1);
    }


    public void startAccel () {
        Intent Intent2 = new Intent(this, WatchSensorService.class); //ENABLE THIS!!!
        Intent2.setAction("ACTION_START_SERVICE"); //ENABLE THIS!!!
        //startForegroundService(Intent1);
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        startService(Intent2); //ENABLE THIS!!!
        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
    }

        public void stopAccel (){
            Intent Intent2 = new Intent(this, proximityService.class);
            Intent2.setAction("ACTION_STOP_SERVICE");
            startService(Intent2);
        }

}
