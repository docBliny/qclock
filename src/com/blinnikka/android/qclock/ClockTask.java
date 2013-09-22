package com.blinnikka.android.qclock;

import java.util.Calendar;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.blinnikka.android.qclock.LedClient;
import com.blinnikka.android.qclock.LedClient.Callbacks;

/**
 * Renders the time using the device LEDs.
 */
public class ClockTask extends TimerTask {

	// **************************************** //
	// Constants
	// **************************************** //
	/** Class name for debugging purposes. */
	private static final String TAG = Shared.APP_NAME + ".ClockTask";

	/** The default priority for our application for LED access. */
	private static final int DEFAULT_PRIORITY = 8; 

	private static final int HOUR_RED = 255;
	private static final int HOUR_GREEN = 255;
	private static final int HOUR_BLUE = 255;
	
	private static final int MINUTE_RED = 0;
	private static final int MINUTE_GREEN = 0;
	private static final int MINUTE_BLUE = 255;
	
	private static final int SECOND_RED = 255;
	private static final int SECOND_GREEN = 0;
	private static final int SECOND_BLUE = 0;
	
	private static final int AM_RED = 00;
	private static final int AM_GREEN = 0;
	private static final int AM_BLUE = 0;
	
	private static final int PM_RED = 255;
	private static final int PM_GREEN = 0;
	private static final int PM_BLUE = 0;
	
	// **************************************** //
	// Private Fields
	// **************************************** //
	//private Context context
	/** The LED client class to communicate with the device LEDs. */
	private final LedClient ledClient;
	
	/** A value indicating whether the LED service is connected. */
	Boolean isConnected = false;
	
	private int ledCount;
	
	final Handler rangeHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			// Get the range values and set
			if(ClockTask.this.isConnected) {
				// Set clock "hands"
				ClockTask.this.ledClient.setLedRange(0, 
						ClockTask.this.ledClient.getLedCount(), (int[]) msg.obj);
				
				// Set AM/PM indicator
				if(msg.what == 0) {
					ClockTask.this.ledClient.setLed(1000, AM_RED, AM_GREEN, AM_BLUE);
				} else {
					ClockTask.this.ledClient.setLed(1000, PM_RED, PM_GREEN, PM_BLUE);
				}
			}
			
			return false;
		}});
	
	// **************************************** //
	// Constructors
	// **************************************** //
	/**
	 * Instantiates a new instance of the ClockTask object.
	 * 
	 * @param context The application context.
	 */
	public ClockTask(Context context) {
		Log.d(TAG, ".ctor");
		
		// Create the LED client
		this.ledClient = new LedClient(context, "ClockTask", new Callbacks() {

			public void ledClientConnected(LedClient ledClient) {
				Log.d(TAG, "ledClientConnected");
				
				ClockTask.this.ledCount = ledClient.getLedCount();
				
				ledClient.enable(DEFAULT_PRIORITY);
				isConnected = true;
			}

			public void ledClientDisconnected(LedClient ledClient) {
				Log.d(TAG, "ledClientDisconnected");
				isConnected = false;
			}
		});
		
		// Connect to the LED service
		ledClient.connect();
	}

	// **************************************** //
	// Public Methods
	// **************************************** //
	@Override
	public void run() {
		//Log.d(TAG, "run()");

		if(this.isConnected) {
			Calendar now = Calendar.getInstance();
			this.rangeHandler.sendMessage(Message.obtain(this.rangeHandler, now.get(Calendar.AM_PM), 
					getDisplayRange(now)));
		}
	}

	// **************************************** //
	// Private Methods
	// **************************************** //
	/**
	 * Returns an array suitable to be passed to the LED client that displays
	 * the current time.
	 * 
	 * @return Array of LED values to display.
	 */
	private int[] getDisplayRange(Calendar time) {
		
		int position;
		int[] result = new int[this.ledCount * 3];
		
		// Fill the array with empty values
		for(int index = 0; index < result.length; index +=1) {
			result[index] = 0;
		}
		
		// Calculate hour hand position
		int hour = time.get(Calendar.HOUR);
		position = getPosition(hour, 12);
		result[position * 3] = HOUR_RED;
		result[(position * 3) + 1] = HOUR_GREEN;
		result[(position * 3) + 2] = HOUR_BLUE;
		
		// Calculate minute hand position
		position = getPosition(time.get(Calendar.MINUTE), 60);
		result[position * 3] = MINUTE_RED;
		result[(position * 3) + 1] = MINUTE_GREEN;
		result[(position * 3) + 2] = MINUTE_BLUE;
		
		// Calculate second hand position
		position = getPosition(time.get(Calendar.SECOND), 60);
		result[position * 3] = SECOND_RED;
		result[(position * 3) + 1] = SECOND_GREEN;
		result[(position * 3) + 2] = SECOND_BLUE;
		
		return result;
	}
	
	/**
	 * Gets the LED position for the given clock hand value.
	 * 
	 * @param value A value representing a clock hand position.
	 * @param max A value indicating what maximum value for the unit.
	 * 
	 * @return The LED corresponding to the clock hand position.
	 */
	private int getPosition(int value, double max) {
		
		double offset = (ledCount / max);
		int result;

		result = Math.min((int)(value * offset), this.ledCount);
		
		return result;
	}
}
