package com.example.user.kidsapplocksystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by user on 1/21/2017.
 */
class HUDView extends ViewGroup {
    private Paint mLoadPaint;

    public HUDView(Context context) {
        super(context);
        Toast.makeText(getContext(),"HUDView", Toast.LENGTH_LONG).show();

        mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setTextSize(10);
        mLoadPaint.setARGB(255, 255, 0, 0);
        //onDraw(canvas);

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.photo0);
        Toast.makeText(getContext(),"Draw", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
        return true;
    }
}