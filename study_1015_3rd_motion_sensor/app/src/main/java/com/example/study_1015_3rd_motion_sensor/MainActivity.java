package com.example.study_1015_3rd_motion_sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView x_gravity;
    TextView y_gravity;
    TextView z_gravity;
    TextView x_accelerometer;
    TextView y_accelerometer;
    TextView z_accelerometer;
    TextView x_linear_accelerometer;
    TextView y_linear_accelerometer;
    TextView z_linear_accelerometer;
    TextView x_gyroscope;
    TextView y_gyroscope;
    TextView z_gyroscope;

    SensorManager sensorManager;
    Sensor sensor_gravity;
    Sensor sensor_accelerometer;
    Sensor sensor_linear_accelerometer;
    Sensor sensor_gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x_gravity = findViewById(R.id.x_gravity);
        y_gravity = findViewById(R.id.y_gravity);
        z_gravity = findViewById(R.id.z_gravity);
        x_accelerometer = findViewById(R.id.x_accelerometer);
        y_accelerometer = findViewById(R.id.y_accelerometer);
        z_accelerometer = findViewById(R.id.z_accelerometer);
        x_linear_accelerometer = findViewById(R.id.x_linear_accelerometer);
        y_linear_accelerometer = findViewById(R.id.y_linear_accelerometer);
        z_linear_accelerometer = findViewById(R.id.z_linear_accelerometer);
        x_gyroscope = findViewById(R.id.x_gyroscope);
        y_gyroscope = findViewById(R.id.y_gyroscope);
        z_gyroscope = findViewById(R.id.z_gyroscope);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_linear_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensor_gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensor_gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor_linear_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor_gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        switch (event.sensor.getType()){
            case Sensor.TYPE_GRAVITY:
                x_gravity.setText("X: " + String.format("%.2f", event.values[0]));
                y_gravity.setText("Y: " + String.format("%.2f", event.values[1]));
                z_gravity.setText("Z: " + String.format("%.2f", event.values[2]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                x_accelerometer.setText("X: " + String.format("%.2f", event.values[0]));
                y_accelerometer.setText("Y: " + String.format("%.2f", event.values[1]));
                z_accelerometer.setText("Z: " + String.format("%.2f", event.values[2]));
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                x_linear_accelerometer.setText("X: " + String.format("%.2f", event.values[0]));
                y_linear_accelerometer.setText("Y: " + String.format("%.2f", event.values[1]));
                z_linear_accelerometer.setText("Z: " + String.format("%.2f", event.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                x_gyroscope.setText("X: " + String.format("%.2f", event.values[0]));
                y_gyroscope.setText("Y: " + String.format("%.2f", event.values[1]));
                z_gyroscope.setText("Z: " + String.format("%.2f", event.values[2]));
                break;
        }
    }
}
