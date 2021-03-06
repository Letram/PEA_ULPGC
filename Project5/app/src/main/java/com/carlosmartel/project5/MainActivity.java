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
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private CustomAnalogClock clock;
    private TextView rotationDisabledText;

    private final float firstTranslationalViscosity = 0.25f;
    private float translationalViscosity = firstTranslationalViscosity;
    private final float firstRotationalViscosity = 0.5f;
    private float rotationalViscosity = firstRotationalViscosity;

    private final float[] translationClamp = {0, 0.75f};
    private final float[] rotationalClamp = {0, 1};

    private final float[] inputTolerance = {-0.25f, 0.25f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        clock = findViewById(R.id.analog_clock);
        rotationDisabledText = findViewById(R.id.rotationDisabled);
        rotationDisabledText.setVisibility(View.GONE);

        int flag;

        flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;

        getWindow().getDecorView().setSystemUiVisibility(flag);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = 0, y = 0;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            synchronized (this) {
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];

                updatePosition(x, y, translationalViscosity);

                //ojo con la aceleración, tiene que ir a valores pequeños
                translationalViscosity += 0.002;
                rotationalViscosity += 0.08;

                translationalViscosity = MathUtils.clamp(translationalViscosity, translationClamp[0], translationClamp[1]);
                rotationalViscosity = MathUtils.clamp(rotationalViscosity, rotationalClamp[0], rotationalClamp[1]);
            }
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            synchronized (this) {
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
            }
        }
        //System.out.println(x + " - " + y);
        if ((inputTolerance[0] < x && x < inputTolerance[1]) && (inputTolerance[0] < y && y < inputTolerance[1]))
            updateRotation(x, y, rotationalViscosity, true);
        else
            updateRotation(x, y, rotationalViscosity, false);
    }

    /**
     * Method in charge of moving the clock through the screen using the X and Y accelerometer values
     *
     * @param x horizontal movement of the clock as an increment.
     * @param y vertical movement of the clock. Just the increase value.
     * @param viscosity factor of sinkness of the clock while rotating. Works like a resistance.
     */
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


    /**
     * Method in charge of rotating the clock using its X and Y rotation values and inclination.
     *
     * @param horizontalCoordinate rotation of the X axis
     * @param verticalCoordinate rotation of the Y axis
     * @param viscosity factor of sinkness of the clock while rotating. Works like a resistance.
     * @param isNotStanding if the phone is in horizontal position or vertical (standing) position
     */
    private void updateRotation(float horizontalCoordinate, float verticalCoordinate, float viscosity, boolean isNotStanding) {
        DecimalFormat dm = new DecimalFormat("#");
        float clockRotation = clock.getRotation();
        double deg = Math.toDegrees(Math.atan2(horizontalCoordinate, verticalCoordinate));
        float formattedDeg = Float.parseFloat(dm.format(deg));

        if (isNotStanding) {
            rotationDisabledText.setVisibility(View.VISIBLE);
            formattedDeg = 0;
        }else rotationDisabledText.setVisibility(View.GONE);

        while (clockRotation < 0 || formattedDeg < 0) {
            clockRotation += 360f;
            formattedDeg += 360f;
        }

        clockRotation = clockRotation % 360f;
        formattedDeg = formattedDeg % 360f;

        float distance = (formattedDeg - clockRotation);

        //System.out.println("Rotation: " + formattedDeg + "; clock angle: " + clockRotation + "; distance: " + distance);

        int degreeTolerance = 5;
        if (Math.abs(distance) > degreeTolerance) {
            if (distance < 0) {
                if (distance > -180)
                    clock.setRotation(clock.getRotation() * 0.8f + (clock.getRotation() - viscosity) * 0.2f);
                    //clock.setRotation(clock.getRotation() - viscosity);
                    //else clock.setRotation(clock.getRotation() + viscosity);
                else
                    clock.setRotation(clock.getRotation() * 0.8f + (clock.getRotation() + viscosity) * 0.2f);
            } else {
                if (distance > 180)
                    clock.setRotation(clock.getRotation() * 0.8f + (clock.getRotation() - viscosity) * 0.2f);
                    //clock.setRotation(clock.getRotation() - viscosity);
                else
                    clock.setRotation(clock.getRotation() * 0.8f + (clock.getRotation() + viscosity) * 0.2f);
                //else clock.setRotation(clock.getRotation() + viscosity);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}