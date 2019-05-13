package com.carlosmartel.project5;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.imankur.analogclockview.AnalogClock;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private AnalogClock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        clock = findViewById(R.id.analog_clock);

        int flag = 0;

        flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(flag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL); // Cada 0.2 segundos se obtendrán las coordenadas
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            updatePosition(x, y);
            updateRotation(x, y);
        }
    }

    private void updatePosition(float x, float y) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthScreen = size.x;
        int heightScreen = size.y;
        int rightBorder = widthScreen - clock.getWidth();
        int bottomBorder = heightScreen - clock.getHeight();

        // Left //
        if (clock.getX() <= 0) {
            clock.setX(0);
        }
        // Right //
        if (clock.getX() >= rightBorder) {
            clock.setX(rightBorder);
        }
        // Top //
        if (clock.getY() <= 0) {
            clock.setY(0);
        }
        // Bottom //
        if (clock.getY() > bottomBorder) {
            clock.setY(bottomBorder);
        }

        clock.setTranslationX(clock.getTranslationX() - x);
        clock.setTranslationY(clock.getTranslationY() + y);
    }

    private void updateRotation(float horizontalCoordinate, float verticalCoordinate) {
        double angle = Math.atan2(horizontalCoordinate, verticalCoordinate) / (Math.PI / 180);
        int rotation = (int) Math.round(angle);
        System.out.println("Rotation: " + rotation);
        float tol = 5;
        if(Math.abs(clock.getRotation() - rotation) < tol)
            clock.setRotation(rotation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


}
