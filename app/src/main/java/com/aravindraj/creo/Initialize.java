package com.aravindraj.creo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by AravindRaj on 24-04-2016.
 */
public class Initialize extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "rZo5cqSAIDSY4vdPxca2FKLa8pusrT3HyKeHlQbY", "mQ9k1viat8EMAqSI1tusgrqb6JiMaBZKU24Kusz8");
    }
}
