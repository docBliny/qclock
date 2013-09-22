package com.blinnikka.android.qclock;

import java.util.Timer;

import com.blinnikka.android.qclock.Shared;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Manages the timer that triggers the time rendering.
 */
public class ClockService extends Service {

	// **************************************** //
	// Constants
	// **************************************** //
	/** Class name for debugging purposes. */
	private static final String TAG = Shared.APP_NAME + ".ClockService";

	// **************************************** //
	// Private Fields
	// **************************************** //
	/** The clock timer. */
	private Timer timer;
	
	/** A value indicating whether the timer is started. */
	private Boolean isActive = false;
	
	// **************************************** //
	// Public Methods
	// **************************************** //
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();

		// Create the timer
		this.timer = new Timer();
		
		Toast.makeText(this, R.string.msg_started, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		super.onStartCommand(intent, flags, startId);
		
		if(!this.isActive) {
			Log.d(TAG, "starting timer");
			this.isActive = true;
			
			// Start the timer
			this.timer.schedule(new ClockTask(this), 0, 1000);
		}
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");

		// Clean up the timer
		this.timer.cancel();
		this.timer = null;
		this.isActive = false;
		
		super.onDestroy();
	}
}
