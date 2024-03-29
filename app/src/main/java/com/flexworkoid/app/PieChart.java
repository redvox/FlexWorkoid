package com.flexworkoid.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBlackStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] value_degree = {20,20,20};
    private int[] colors = { Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN,
            Color.RED, Color.YELLOW };

    float width = 0;
    float height = 0;
    float offset = 5;
    RectF rectf = new RectF(offset, offset, 200, 200);

    public PieChart(Context context, float[] values) {
        super(context);
        paintBlackStroke.setStyle(Style.STROKE);
        paintBlackStroke.setColor(Color.BLACK);
        paintBlackStroke.setStrokeWidth(2);

        setValues(values);
    }

    // inflater constructor
    public PieChart(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paintBlackStroke.setStyle(Style.STROKE);
        paintBlackStroke.setColor(Color.BLACK);
        paintBlackStroke.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int temp = 0;
        for (int i = 0; i < value_degree.length; i++) {// values2.length; i++) {
            if (i != 0)
                temp += (int) value_degree[i - 1];
            paint.setColor(colors[i]);
            canvas.drawArc(rectf, temp, value_degree[i], true, paint);
            canvas.drawArc(rectf, temp, value_degree[i], true, paintBlackStroke);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        w = (int) (w-offset);
        h = (int) (h-offset);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        width = (float) w - xpad;
        height = (float) h - ypad;

        float toCenterOffset = Math.abs(width - height)/2;
        if(width > height){
            width = height;
            rectf = new RectF(offset+toCenterOffset, offset, width+toCenterOffset, height);
        } else {
            height = width;
            rectf = new RectF(offset, offset+toCenterOffset, width, height+toCenterOffset);
        }
    }

    public void setValues(float[] values) {
        float total = 0;
        value_degree = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }

        for (int i = 0; i < values.length; i++) {
            value_degree[i] = 360 * (values[i] / total);
        }
    }

    public void setValuesWithColor(float[] values, int[] colors) {
        setValues(values);
        this.colors = colors;
    }
}
