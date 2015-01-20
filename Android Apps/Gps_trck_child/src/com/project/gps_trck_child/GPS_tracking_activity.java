package com.project.gps_trck_child;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class GPS_tracking_activity extends Activity {
//@@@Button btnShowLocation;

	double latitude ;
	double longitude ;

	// GPSTracker class
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_tracking_activity);

		gps = new GPSTracker(GPS_tracking_activity.this);

		// Show the Up button in the action bar.
		setupActionBar();

			// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
	        	 latitude = gps.getLatitude();
	        	 longitude = gps.getLongitude();
	        		        	
	        	// \n is for new line
	 //       	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
	       
	        	Intent result =new Intent();
	      	    //    result.putExtra("data",data);
	      	        result.putExtra("latitude",latitude);
	      	        result.putExtra("longitude",longitude);
	      	        setResult(Activity.RESULT_OK,result);
	      	        finish();
	      	        
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }
			
////************************send back result of activity **************************************////
	      
	}

	/**
	 * Set up the {@ 	link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps_tracking_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
