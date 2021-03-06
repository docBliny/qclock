package com.blinnikka.android.qclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Starts the main service when the device is booted.
 */
public class BootReceiver extends BroadcastReceiver {

	// **************************************** //
	// Constants
	// **************************************** //
	private static final String TAG = Shared.APP_NAME + ".BootReceiver";

	// **************************************** //
	// Public Methods
	// **************************************** //
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(TAG + ", " + "onReceive, Intent=" + intent.getAction());
    	Log.d(TAG, "onReceive, Intent=" + intent.getAction());

    	// Start the receiver service
    	Intent serviceIntent = new Intent(context, ClockService.class);
    	context.startService(serviceIntent);
	}

}
