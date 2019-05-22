package com.carlosmartel.project5;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import java.text.DecimalFormat;

//todo quitar los temblores
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private MyAnalogClock clock;

    private float firstTranslationalViscosity = 0.25f;
    private float translationalViscosity = firstTranslationalViscosity;
    private float firstRotationalViscosity = 0.5f;
    private float rotationalViscosity = firstRotationalViscosity;

    private float[] translationClamp = {0, 0.75f};
    private float[] rotationalClamp = {0, 0.5f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        clock = findViewById(R.id.analog_clock);

        int flag;

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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            synchronized (this) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                System.out.println("x: " + x + ", y: " + y + ", z: " + z);
                updatePosition(x, y, translationalViscosity);

                //ojo con la aceleración, tiene que ir a valores pequeños
                translationalViscosity += 0.002;
                rotationalViscosity += 0.8;

                translationalViscosity = MathUtils.clamp(translationalViscosity, translationClamp[0], translationClamp[1]);
                rotationalViscosity = MathUtils.clamp(rotationalViscosity, rotationalClamp[0], rotationalClamp[1]);
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            updateRotation(x, y, rotationalViscosity);

        }
    }

    private void updatePosition(float x, float y, float viscosity) {
        //get the display's size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //and bounds of the screen apart from (0,0), the other diagonal
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
            translationalViscosity = firstTranslationalViscosity;
        }
        // Bottom //
        if (clock.getY() > bottomBorder) {
            clock.setY(bottomBorder);
            translationalViscosity = firstTranslationalViscosity;
        }

        clock.setTranslationX(clock.getTranslationX() - x * viscosity);
        clock.setTranslationY(clock.getTranslationY() + y * viscosity);
    }


        private void updateRotation(float horizontalCoordinate, float verticalCoordinate, float viscosity) {

            DecimalFormat dm = new DecimalFormat("#");
            float clockRotation = clock.getRotation();
            double deg = Math.toDegrees(Math.atan2(horizontalCoordinate, verticalCoordinate));

            float formattedDeg = Float.parseFloat(dm.format(deg));

            System.out.println("Rotation: " + formattedDeg + "; clock angle: " + clockRotation);

            while(clockRotation < 0 || formattedDeg < 0){
                clockRotation += 360f;
                formattedDeg += 360f;
            }

            clockRotation = clockRotation % 360f;
            formattedDeg = formattedDeg % 360f;

            float distance = (formattedDeg - clockRotation);

            System.out.println("Rotation: " + formattedDeg + "; clock angle: " + clockRotation + "; distance: " + distance);

            if (distance < 0) {
                if (distance > -180) clock.setRotation(clock.getRotation() - viscosity);
                else clock.setRotation(clock.getRotation() + viscosity);
            } else {
                if (distance > 180) clock.setRotation(clock.getRotation() - viscosity);
                else clock.setRotation(clock.getRotation() + viscosity);
            }

        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}