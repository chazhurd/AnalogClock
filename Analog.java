package com.example.churdlab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;


public class Analog extends View {
    static int mHour = 0;
    static int mMinute = 0;
    static int mSecond = 0;

    final static float[] mHourHand = {-10,0,0,-60,10,0};//BUILD HOUR HAND 4 Vertices MIN
    final static float[] mHourTail = {-10,0,0,10, 10,0};//BUILD A TAIL FOR BOTH HOUR HAND
    final static float[] mMinuteHand = {-3,0,0,-70, 3,0};//BUILD MINUTE HAND 4 vertices MIN
    final static float[] mMinuteTail = {-3,0,0,15, 3, 0};//BUILD A TAIL FOR MINUTE HAND
    final static float[] mSecondHand = {-1,0,0,-80, 1,0};
    final static float[] mSecondTail = {-1,0,0,5,1,0};

    private int mWidth;
    private int mHeight;
    private static final float LOG_WIDTH = 200;
    private static final float LOG_HEIGHT = 200;

    private static final float mCircleRadius = 90;
    private static final float lengthOfHour = 60;
    private static final float lengthOfMinute = 75;
    private static final float lengthOfSecond = 85;
    private static final float largeNotch = 10;
    private static final float smallNotch = 5;

    private Paint mPolyPaint = new Paint();
    private static long TIMER_MSEC = 1000;
    AnalogObserver myObservable = Analog.AnalogObserver.getInstance();


    public static class AnalogObserver extends Observable {
        private static AnalogObserver INSTANCE = null;
        public String getTime()
        {
            String time = "";
            if(mHour < 9 && mMinute <9 && mSecond < 9){
             time = "0" + mHour+":0" +mMinute+":0"+ mSecond;
            }
            else if(mHour < 9 && mMinute <9 && mSecond > 9){
            time = "0" + mHour+":0" +mMinute+":"+ mSecond;
            }
            else if(mHour < 9 && mMinute >9 && mSecond < 9){
                time = "0" + mHour+":" +mMinute+":0"+ mSecond;
            }
            else if(mHour < 9 && mMinute >9 && mSecond > 9){
                time = "0" + mHour+":" +mMinute+":"+ mSecond;
            }
            else{
                time = "" + mHour + ":" + mMinute + ":" +mSecond;
            }
            return time;
        }
        public void setTime(int hour,int min, int sec)
        {
            mHour=hour;
            mMinute = min;
            mSecond = sec;
            setChanged();
            notifyObservers();
        }
        public static AnalogObserver getInstance() {
            if(INSTANCE == null) {
                INSTANCE = new AnalogObserver();
            }
            return INSTANCE;
        }
    }

    public Analog(Context context) {
        super(context);
        AnalogInit();
    }

    public void AnalogInit() {
        final Handler mClockHandler = new Handler() ;
        Runnable clockTimer = new Runnable() {
            @Override
            public void run() {
                invalidate();
                mClockHandler.postDelayed(this, TIMER_MSEC) ;
            }
        } ;
        mClockHandler.postDelayed(clockTimer,TIMER_MSEC);
    }

    public Analog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        AnalogInit();
    }
    @Override
    public void onDraw(Canvas canvas) {


        canvas.scale(mWidth / LOG_WIDTH, mHeight / LOG_HEIGHT);
        canvas.drawRGB(0, 0, 0);

        //circle
        mPolyPaint.setColor(Color.argb(255,255,255,255));
        canvas.translate(100,100);
        mPolyPaint.setStrokeWidth(4);
        mPolyPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0,0,mCircleRadius,mPolyPaint);
        //Ticks
        canvas.save();
        mPolyPaint.setColor(Color.WHITE);
        mPolyPaint.setStrokeWidth(1);

        for(int x = 0; x < 60; x++)
        {
            if(x%5==0)
            {
                canvas.drawLine(0,mCircleRadius,0,mCircleRadius -10,mPolyPaint);

            }
            else {
                canvas.drawLine(0, mCircleRadius, 0, mCircleRadius-5, mPolyPaint);
            }

            canvas.rotate(6);

        }
        canvas.restore();

        GregorianCalendar gCalendar = new GregorianCalendar();
        mSecond = gCalendar.get(Calendar.SECOND);
        mMinute = gCalendar.get(Calendar.MINUTE);
        mHour = gCalendar.get(Calendar.HOUR);
        myObservable.setTime(mHour,mMinute,mSecond);
        //2.4
        double secondDegree = (double)gCalendar.get(Calendar.SECOND)*6;
        double minuteDegree = (double)gCalendar.get(Calendar.MINUTE)*6+ secondDegree/60;
        double hourDegree = (double)0.5*((gCalendar.get(Calendar.HOUR_OF_DAY)%12)*60+ ((double)((gCalendar.get(Calendar.MINUTE)*60)/60)));

        mPolyPaint.setStrokeWidth(2);

        canvas.save();
        canvas.rotate((float)secondDegree);
        Path mySecondHandPath = new Path();
        mySecondHandPath.moveTo(mSecondHand[0], mSecondHand[1]);
        mySecondHandPath.lineTo(mSecondHand[2], mSecondHand[3]);
        mySecondHandPath.lineTo(mSecondHand[4], mSecondHand[5]);
        mySecondHandPath.close();
        Path mySecondHandPathTail = new Path();
        mySecondHandPathTail.moveTo(mSecondTail[0], mSecondTail[1]);
        mySecondHandPathTail.lineTo(mSecondTail[2], mSecondTail[3]);
        mySecondHandPathTail.lineTo(mSecondTail[4], mSecondTail[5]);
        mySecondHandPathTail.close();
        mPolyPaint.setColor(Color.argb(255,0,0,255));
        canvas.drawPath(mySecondHandPath, mPolyPaint);
        canvas.drawPath(mySecondHandPathTail, mPolyPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate((float)minuteDegree);
        Path myMinuteHandPath = new Path();
        myMinuteHandPath.moveTo(mMinuteHand[0], mMinuteHand[1]);
        myMinuteHandPath.lineTo(mMinuteHand[2], mMinuteHand[3]);
        myMinuteHandPath.lineTo(mMinuteHand[4], mMinuteHand[5]);
        myMinuteHandPath.close();
        Path myMinuteHandPathTail = new Path();
        myMinuteHandPathTail.moveTo(mMinuteTail[0], mMinuteTail[1]);
        myMinuteHandPathTail.lineTo(mMinuteTail[2], mMinuteTail[3]);
        myMinuteHandPathTail.lineTo(mMinuteTail[4], mMinuteTail[5]);
        myMinuteHandPathTail.close();
        mPolyPaint.setColor(Color.argb(255,0,255,0));
        canvas.drawPath(myMinuteHandPath, mPolyPaint);
        canvas.drawPath(myMinuteHandPathTail, mPolyPaint);
        canvas.restore();



//Hour Hand.
        canvas.save();
        canvas.rotate((float)hourDegree);
        Path myHourHandPath = new Path();
        myHourHandPath.moveTo(mHourHand[0], mHourHand[1]);
        myHourHandPath.lineTo(mHourHand[2], mHourHand[3]);
        myHourHandPath.lineTo(mHourHand[4], mHourHand[5]);
        myHourHandPath.close();
        Path myHourHandPathTail = new Path();
        myHourHandPathTail.moveTo(mHourTail[0], mHourTail[1]);
        myHourHandPathTail.lineTo(mHourTail[2], mHourTail[3]);
        myHourHandPathTail.lineTo(mHourTail[4], mHourTail[5]);
        myHourHandPathTail.close();
        mPolyPaint.setColor(Color.argb(255, 255, 0, 0));
        canvas.drawPath(myHourHandPath,mPolyPaint);
        canvas.drawPath(myHourHandPathTail,mPolyPaint);
        canvas.restore();

    }
    @Override
    public void onSizeChanged(int a, int b, int c, int d)
    {
        super.onSizeChanged(a,b,c,d);
        mWidth = a;
        mHeight = b;
    }
    //on measure definitely wrong
    public void onMeasure(int a, int b) {
        int setWidth;
        int setHeight;
        int actualWidth = MeasureSpec.getSize(a);
        int actualHeight = MeasureSpec.getSize(b);

        //if all width is used  ratio is 3/5 logW/logH
        float desiredWidth = actualHeight*(LOG_WIDTH/LOG_HEIGHT);
        float desiredHeight = desiredWidth / (LOG_WIDTH / LOG_HEIGHT);

        if (desiredHeight > actualHeight) {

            setWidth = (int)desiredWidth;
            setHeight = actualHeight;
        }else{
            setWidth = actualWidth;
            setHeight = (int)desiredHeight;
        }
        setMeasuredDimension(setWidth,setHeight);

    }
    public void setTime(int[] time){
        mHour = time[0];
        mMinute = time[1];
        mSecond = time[2];
    }
}
