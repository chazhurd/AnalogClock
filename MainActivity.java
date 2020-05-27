package com.example.churdlab5;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.os.Bundle;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements Observer {
static TextView mTv;
private Observable myObservable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.digitalTextView);
        myObservable = Analog.AnalogObserver.getInstance();
        myObservable.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onAbout(MenuItem item) {
        Toast.makeText(this, "Lab 5, Spring 2020, Chaz C Hurd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable o, Object arg) {
    Analog.AnalogObserver myObserver = (Analog.AnalogObserver) o;
    mTv.setText(myObserver.getTime());
    }
    // end template

}
