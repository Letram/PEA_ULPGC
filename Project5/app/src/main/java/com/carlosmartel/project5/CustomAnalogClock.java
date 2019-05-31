package com.carlosmartel.project5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

public class CustomAnalogClock extends View {

    /** height, width of the clock's view */
    private int mHeight, mWidth = 0;

    /** numeric numbers to denote the hours */
    private int[] mClockHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    /** spacing and padding of the clock-hands around the clock round */
    private int mPadding = 0;

    /** truncation of the heights of the clock-hands,
     hour clock-hand will be smaller comparetively to others */
    private int mHandTruncation, mHourHandTruncation = 0;

    /** others attributes to calculate the locations of hour-points */
    private int mRadius = 0;
    private Paint mPaint;
    private Rect mRect = new Rect();
    private boolean isInit;  // it will be true once the clock will be initialized.

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* initialize necessary values */
        if (!isInit) {
            mPaint = new Paint();
            mHeight = getHeight();
            mWidth = getWidth();
            int mNumeralSpacing = 0;
            mPadding = mNumeralSpacing + 50;  // spacing from the circle border
            int minAttr = Math.min(mHeight, mWidth);
            mRadius = minAttr / 2 - mPadding;

            // for maintaining different heights among the clock-hands
            mHandTruncation = minAttr / 20;
            mHourHandTruncation = minAttr / 17;

            isInit = true;  // set true once initialized
        }

        /* draw the canvas-color */
        canvas.drawColor(Color.TRANSPARENT);
        /* circle border */
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle((float) (mWidth / 2.0), (float) (mHeight / 2.0), mRadius + mPadding - 10, mPaint);

        /* clock-center */
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float) (mWidth / 2.0), (float) (mHeight / 2.0), 12, mPaint);  // the 03 clock hands will be rotated from this center point.

        /* border of hours */

        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);  // set font size (optional)

        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect);  // for circle-wise bounding

            // find the circle-wise (x, y) position as mathematical rule
            double angle = Math.PI / 6 * (hour - 3);
            int x = (int) (mWidth / 2 + Math.cos(angle) * mRadius - mRect.width() / 2);
            int y = (int) (mHeight / 2.0 + Math.sin(angle) * mRadius + mRect.height() / 2.0);

            canvas.drawText(String.valueOf(hour), x, y, mPaint);  // you can draw dots to denote hours as alternative
        }

        /* draw clock hands to represent the every single time */

        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;

        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60.0) * 5f, true, false); // draw hours
        drawHandLine(canvas, calendar.get(Calendar.MINUTE), false, false); // draw minutes
        drawHandLine(canvas, calendar.get(Calendar.SECOND), false, true); // draw seconds

        /* invalidate the appearance for next representation of time  */
        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius = isHour ? mRadius - mHandTruncation - mHourHandTruncation : mRadius - mHandTruncation;
        if (isSecond) mPaint.setColor(Color.RED);
        canvas.drawLine((float) (mWidth / 2.0), (float) (mHeight / 2.0), (float) (mWidth / 2 + Math.cos(angle) * handRadius), (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }
}