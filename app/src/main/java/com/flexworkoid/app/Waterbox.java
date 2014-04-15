package com.flexworkoid.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Waterbox extends View {

    int value;
    int planedValue;
    String valueString;
    float fillLevel = 0;

    float startwidth = 0;
    float startheight = 0;
    float maxwidth = 0;
    float maxheight = 0;
    float offset = 5;

    Paint paint;
    Paint paint2;
    Paint textPaint = new Paint(){
        {
        setColor(Color.WHITE);
        setStyle(Style.FILL_AND_STROKE);
        setTextSize(30f);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
    };

    public Waterbox(Context context, int value, int planedValue) {
        super(context);
        setValues(value, planedValue);
        startAnimation();
    }

    public Waterbox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        startAnimation();
    }

    float o = 1f;
    float o2 = 1f;
    float o3 = 1f;

    float x = startwidth+5;
    float y = 0;
    float increment = 1f;
    float increment2 = 1f;
    float increment3 = 1f;
    float waveSize = 10f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        y = maxheight - fillLevel;
//        final Path path = new Path();
//        path.moveTo(x, y);
//        path.cubicTo(100, y + o,
//                maxwidth - 100,
//                y - o,
//                maxwidth, y);
//        path.lineTo(maxwidth, maxheight);
//        path.lineTo(x, maxheight);
//        path.lineTo(x, y);
//        path.close();

        float offset = -10;
        final Path path2 = new Path();
        path2.moveTo(x, y);
        path2.cubicTo
                (75, y - o2, //Control 1
                maxwidth - 75, y+offset + o3, //Control 2
                maxwidth, y); //End Point
        path2.lineTo(maxwidth, maxheight);
        path2.lineTo(x, maxheight);
        path2.lineTo(x, y);
        path2.close();

        canvas.drawPath(path2, paint2);
//        canvas.drawPath(path, paint);
        canvas.drawText(valueString, maxwidth/2, maxheight/2, textPaint);
    }

    long mAnimStartTime;
    Handler mHandler = new Handler();
    Runnable mTick = new Runnable() {
        public void run() {
//            if(o <= waveSize){
//                increment = -1f;
//            }
//            if(o <= -waveSize){
//                increment = 1f;
//            }
//            float rand = (float) Math.random() * 1;
//            o += rand*increment;


            if(o2 >= waveSize){
                increment2 = -1f;
            }
            if(o2 <= -waveSize){
                increment2 = 1f;
            }
            o2 += (Math.random() * 1)*increment2;
            if(o3 >= waveSize){
                increment3 = -1f;
            }
            if(o3 <= -waveSize){
                increment3 = 1f;
            }
            o3 += (Math.random() * 1)*increment3;

            invalidate();
            mHandler.postDelayed(this, 20); // 20ms == 60fps
        }
    };

    void startAnimation() {
        mAnimStartTime = SystemClock.uptimeMillis();
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        Log.e("/////////// visibility " + visibility, "/////////// visibility " + visibility);
        super.dispatchWindowVisibilityChanged(visibility);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        Log.e("/////////// hasFocus " + hasFocus, "/////////// hasFocus " + hasFocus);
        super.dispatchWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        w = (int) (w-offset);
        h = (int) (h-offset);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        startwidth = xpad;
        startheight = ypad;
        maxwidth = (float) w - xpad;
        maxheight = (float) h - ypad;

        paint = new Paint() {
            {
//            setColor(Color.argb(100, 74, 32, 150));
//            setColor(Color.WHITE);
                setColor(Color.argb(50, 74, 32, 150));
                setStyle(Style.FILL_AND_STROKE);
//            setStrokeCap(Paint.Cap.ROUND);
//            setStrokeWidth(3.0f);
                setAntiAlias(true);
//              setShader(new LinearGradient(0, 0, 0, maxheight, Color.argb(75, 74, 32, 150), Color.argb(75, 42, 18, 85), Shader.TileMode.CLAMP));
            }
        };

        paint2 = new Paint() {
            {
            setColor(Color.argb(50, 0, 255, 255));
            setStyle(Style.FILL_AND_STROKE);
            setAntiAlias(true);
//              setShader(new LinearGradient(0, 0, 0, maxheight, Color.argb(100, 74, 32, 150), Color.argb(100, 42, 18, 85), Shader.TileMode.CLAMP));
            }
        };

//        float toCenterOffset = Math.abs(width - height)/2;
//        if(width > height){
//            width = height;
//            rectf = new RectF(offset+toCenterOffset, offset, width+toCenterOffset, height);
//        } else {
//            height = width;
//            rectf = new RectF(offset, offset+toCenterOffset, width, height+toCenterOffset);
//        }
        updateText();
    }

    public void setValues(int value, int planedValue) {
        this.value = value;
        this.planedValue = planedValue;
        updateText();
    }

    public void updateText(){
        int hours = value / 60;
        int minutes = value % 60;
        valueString = hours+":"+minutes;
        fillLevel = (maxheight / planedValue) * value;
    }
}
