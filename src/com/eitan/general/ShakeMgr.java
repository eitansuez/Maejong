package com.eitan.general;

import java.util.List;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeMgr implements SensorEventListener
{
   private SensorManager _sensorMgr;
   private Activity _activity;
   private Sensor _accelerometer;
   
   private long _lastUpdate = -1;
   private float _lastX, _lastY, _lastZ;
   private static final int FORCE_THRESHOLD = 1150;

   public ShakeMgr(Activity activity)
   {
      this(activity, false);
   }
   public ShakeMgr(Activity activity, boolean disableForEmulator)
   {
      if (!(activity instanceof ShakeListener))
      {
         throw new RuntimeException("Activity must be declared an instance of a ShakeListener");
      }
      _activity = activity;
      
      if (disableForEmulator) return;
      
      _sensorMgr = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
      List<Sensor> sensors = _sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
      if (sensors.size() > 0)
      {
         _accelerometer = sensors.get(0);
      }
   }
 
   
   public void onAccuracyChanged(Sensor sensor, int accuracy) { }

   public void onSensorChanged(SensorEvent event)
   {
      if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER || event.values.length < 3)
         return;

      long currentTime = System.currentTimeMillis();
      if ((currentTime - _lastUpdate) > 100)
      {
         long diffTime = (currentTime - _lastUpdate);
         _lastUpdate = currentTime;
         float x = event.values[SensorManager.DATA_X];
         float y = event.values[SensorManager.DATA_Y];
         float z = event.values[SensorManager.DATA_Z];

         float currenForce = Math.abs(x + y + z - _lastX - _lastY - _lastZ) / diffTime * 10000;
         if (currenForce > FORCE_THRESHOLD)
         {
            ((ShakeListener) _activity).onShake();
         }
         _lastX = x; _lastY = y; _lastZ = z;
      }
   }

   public boolean register()
   {
      if (_accelerometer == null) return false;
      boolean supported = _sensorMgr.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_GAME);
      if (!supported)
      {
         deregister();
      }
      return supported;
   }

   public void deregister()
   {
      if (_sensorMgr != null)
         _sensorMgr.unregisterListener(this, _accelerometer);
   }
}
