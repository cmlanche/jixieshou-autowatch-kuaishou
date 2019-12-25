package com.cmlanche.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import com.cmlanche.core.utils.Logger;
import com.cmlanche.jixieshou.R;

public class PointView extends View {
    private Paint paint;
    private Path linePath;

    private Point start = new Point(200, 200);
    private Point end = new Point(400, 400);
    private int startPointRadius = 100;
    private int endPointRadius = 50;

    private boolean isInStartPoint = false;
    private boolean isInEndPoint = false;

    public PointView(Context context) {
        super(context);
        paint = new Paint();
        linePath = new Path();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(isDownInPoint(event.getRawX(), event.getY(), start, startPointRadius)) {
                            isInStartPoint = true;
                            isInEndPoint = false;
                        } else if(isDownInPoint(event.getRawX(), event.getRawY(), end, endPointRadius)) {
                            isInEndPoint = true;
                            isInStartPoint = false;
                        } else {
                            isInStartPoint = false;
                            isInEndPoint = false;
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(isInStartPoint) {
                            start.x = (int) event.getRawX();
                            start.y = (int) event.getRawY();
                            invalidate();
                        } else if(isInEndPoint) {
                            end.x = (int) event.getRawX();
                            end.y = (int) event.getRawY();
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        linePath.reset();

        canvas.drawLine(0, 0, start.x, start.y, paint);
        canvas.drawLine(0, 0, end.x, end.y, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        if(isInStartPoint) {
            paint.setColor(Color.parseColor("#ff0000"));
        }
        canvas.drawCircle(start.x, start.y, startPointRadius, paint);

        paint.setColor(Color.parseColor("#ffffff"));
        linePath.moveTo(start.x, start.y);
        linePath.lineTo(end.x, end.y);
        canvas.drawPath(linePath, paint);

        if(isInEndPoint) {
            paint.setColor(Color.parseColor("#ff0000"));
        }
        canvas.drawCircle(end.x, end.y, endPointRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private boolean isDownInPoint(float downX, float downY, Point point, int radius) {
        double distance = Math.sqrt(Math.pow(point.x - downX, 2) + Math.pow(point.y - downY, 2));
        Logger.i("distance: " + distance);
        return distance <= radius;
    }
}
