package com.carlosmartel.project5;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.imankur.analogclockview.AnalogClock;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private MyAnalogClock clock;

    private float translationalViscosity = 0.25f;
    private float rotationalViscosity = 0.5f;

    private float[] translationClamp = {0, 0.75f};
    private float[] rotationalClamp = {0, 0.5f};
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
                SensorManager.SENSOR_DELAY_GAME); //para que se obtengan las coordenadas de manera exhaustiva
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

        //ratio of movement (X,Y)

        //ojo con la aceleración, tiene que ir a valores pequeños
        translationalViscosity += 0.002;
        rotationalViscosity += 0.1;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            updatePosition(x, y, translationalViscosity);
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

        translationalViscosity = MathUtils.clamp(translationalViscosity, translationClamp[0], translationClamp[1]);
        rotationalViscosity = MathUtils.clamp(rotationalViscosity, rotationalClamp[0], rotationalClamp[1]);

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
            translationalViscosity = 0;
        }
        // Bottom //
        if (clock.getY() > bottomBorder) {
            clock.setY(bottomBorder);
            translationalViscosity = 0;
        }

        clock.setTranslationX(clock.getTranslationX() - x * viscosity);
        clock.setTranslationY(clock.getTranslationY() + y * viscosity);
    }

    private void updateRotation(float horizontalCoordinate, float verticalCoordinate, float viscosity) {

        DecimalFormat dm = new DecimalFormat("#");
        double deg = Math.toDegrees(Math.atan2(horizontalCoordinate, verticalCoordinate));
        System.out.println("Rotation: " + dm.format(deg) + "; clock angle: " + clock.getRotation());

        float formattedDeg = Float.parseFloat(dm.format(deg));
        if (formattedDeg < clock.getRotation()) {
            clock.setRotation(clock.getRotation() - viscosity);
        } else if (formattedDeg > clock.getRotation()) {
            clock.setRotation(clock.getRotation() + viscosity);
        }
        /*
            double angle = Math.atan2(horizontalCoordinate, verticalCoordinate) / (Math.PI / 180);
            int rotation = (int) Math.round(angle);
            if(rotation != clock.getRotation())
                clock.setRotation(clock.getRotation() + viscosity);
            System.out.println("Rotation: " + rotation + "; angle: " + rotation + "; clock angle: " + clock.getRotation());
            clock.setRotation(rotation);
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}