package edu.virginia.ece.inertia.besic.mementoopt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.virginia.ece.inertia.besic.mementoopt.utils.FileUtil;


public class WatchSensorService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyroscope, mRotationVector, mMagnetometer, mGravity, mHeartRate, mStepCount;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private float mStepOffset;
    Context context;
    int count, maxCount, rate;
    StringBuilder strBuilder;
    long startTime;
    String file_tag;
    int sensorType;

    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakeLockTag");
        wakeLock.acquire();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mGravity = mSensorManager
        //        .getDefaultSensor(Sensor.TYPE_GRAVITY);
        //mGyroscope = mSensorManager
        //       .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //mRotationVector = mSensorManager
       //         .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
       //mMagnetometer = mSensorManager
        //        .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //mHeartRate = mSensorManager
        //        .getDefaultSensor(Sensor.TYPE_HEART_RATE);
        //mStepCount = mSensorManager
        //        .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        rate = SensorManager.SENSOR_DELAY_NORMAL;
        //rate = SensorManager.SENSOR_DELAY_FASTEST;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, mGravity, rate);
        //mSensorManager.registerListener(this, mGyroscope, rate);
        //mSensorManager.registerListener(this, mRotationVector, rate);
        //mSensorManager.registerListener(this, mMagnetometer, rate);
        //mSensorManager.registerListener(this, mHeartRate, rate);
        //mSensorManager.registerListener(this, mHeartRate,SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, mHeartRate,30000000,30000000);
        //mSensorManager.registerListener(this, mHeartRate,30000000000,30000000000);
        //mSensorManager.registerListener(this, mStepCount,SensorManager.SENSOR_DELAY_NORMAL);





        startTime = System.currentTimeMillis();
        strBuilder = new StringBuilder();
        startTime = System.currentTimeMillis();
        count = 0;
        maxCount = 60 * 300;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("stop")) {
            stopSelf(startId);
            //Estimote Stop Code


        } else if (intent != null && intent.hasExtra("start")) {
            file_tag = intent.getStringExtra("start");
            //file_tag = file_tag + ".wada"; CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            file_tag = "accelerometer";

            //Estimote Start Code







        }

        context = this.getApplicationContext();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        wakeLock.release();
        sendData();
    }

    public void sendData() {
        if (strBuilder != null) {
            Log.i("MyTAG", "Sending File. Duration: " + (System.currentTimeMillis() - startTime));
            new fileSaveThread(strBuilder).start();
            strBuilder = new StringBuilder();
            count = 0;
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_HEART_RATE){
            //strBuilder.append("HR TEST");
            strBuilder.append("HEART");
            strBuilder.append(",");


            //System.currentTimeMillis();

            //long timeInMillis = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
            //long timeInMillis = (event.timestamp - System.nanoTime()) / 1000000;
            //long timeInMillis = event.timestamp / 1000000L;
            //long timeInMillis = ((new Date()).getTime() + event.timestamp - System.nanoTime()) / 1000000L;
            long timeInMillis = System.currentTimeMillis();

            //long yourmilliseconds = timeInMillis - 259754000L;
            long yourmilliseconds = timeInMillis;
            //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss.SSS");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS a");
            Date resultdate = new Date(yourmilliseconds);

            //String event_timestamp = sdf.format(resultdate);

            strBuilder.append(sdf.format(resultdate));

            //strBuilder.append(event.timestamp);
            strBuilder.append(",");
            strBuilder.append(sensorType);
            strBuilder.append(",");
            strBuilder.append(event.accuracy);
            strBuilder.append(",");
            strBuilder.append(event.values[0]);

        }
        else if (sensorType == Sensor.TYPE_STEP_COUNTER){

            if (mStepOffset == 0) {
                mStepOffset = event.values[0];
            }
            //mTextView.setText(Float.toString(event.values[0] - mStepOffset));

            strBuilder.append("STEP");
            strBuilder.append(",");


            //long timeInMillis = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L; //sort of works
            //long timeInMillis = (event.timestamp - System.nanoTime()) / 1000000;
            //long timeInMillis = event.timestamp / 1000000L;
            //long timeInMillis = event.timestamp / 1000000;
            //long timeInMillis = ((new Date()).getTime() + event.timestamp - System.nanoTime()) / 1000000L;
            //long timeInMillis = event.timestamp;
            long timeInMillis = System.currentTimeMillis();

            //long yourmilliseconds = timeInMillis - 259754000L;
            long yourmilliseconds = timeInMillis;
            //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss.SSS");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS a");
            Date resultdate = new Date(yourmilliseconds);

            strBuilder.append(sdf.format(resultdate));
            //strBuilder.append(event.timestamp);
            strBuilder.append(",");
            strBuilder.append(sensorType);
            strBuilder.append(",");
            strBuilder.append(Float.toString(event.values[0] - mStepOffset));


            /*
            strBuilder.append(",");
            strBuilder.append(event.accuracy);
            strBuilder.append(",");
            strBuilder.append(event.values[0]);
            strBuilder.append(",");
            strBuilder.append(event.values[1]);
            strBuilder.append(",");
            strBuilder.append(event.values[2]);
*/
        }

        else if (sensorType == Sensor.TYPE_ACCELEROMETER){

            int count;






            strBuilder.append("ACCEL");
            strBuilder.append(",");
            strBuilder.append(event.timestamp);
            strBuilder.append(",");
            strBuilder.append(sensorType);
            strBuilder.append(",");
            strBuilder.append(event.accuracy);
            strBuilder.append(",");
            strBuilder.append(event.values[0]);
            strBuilder.append(",");
            strBuilder.append(event.values[1]);
            strBuilder.append(",");
            strBuilder.append(event.values[2]);
        }
        else if (sensorType == Sensor.TYPE_GYROSCOPE) {

            strBuilder.append("GYRO");
            strBuilder.append(",");
            strBuilder.append(event.timestamp);
            strBuilder.append(",");
            strBuilder.append(sensorType);
            strBuilder.append(",");
            strBuilder.append(event.accuracy);
            strBuilder.append(",");
            strBuilder.append(event.values[0]);
            strBuilder.append(",");
            strBuilder.append(event.values[1]);
            strBuilder.append(",");
            strBuilder.append(event.values[2]);

        }
        else {

            strBuilder.append("OTHER");
            strBuilder.append(",");
            strBuilder.append(event.timestamp);
            strBuilder.append(",");
            strBuilder.append(sensorType);
            strBuilder.append(",");
            strBuilder.append(event.accuracy);
            strBuilder.append(",");
            strBuilder.append(event.values[0]);
            strBuilder.append(",");
            strBuilder.append(event.values[1]);
            strBuilder.append(",");
            strBuilder.append(event.values[2]);

        }
        strBuilder.append("\n");

        count++;
        sendData();
        //Log.i("MyTAG", "Count: " + count);
        if (count >= maxCount)
            sendData();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class fileSaveThread extends Thread {
        long st;
        StringBuilder sb;

        public fileSaveThread(StringBuilder sb) {
            this.st = st;
            this.sb = sb;
        }

        @Override
        public void run() {
            try {
                Log.i("Thread Called", "for saving sensor samples");
                String str = sb.toString();
                if (str.length() == 0)
                    return;
                FileUtil.saveStringToFile(file_tag, str);

            } catch (Exception ex) {
                Log.i("Sensor File Save", ex.toString());
            }
        }

    }



    /**
     * *****************************************
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
