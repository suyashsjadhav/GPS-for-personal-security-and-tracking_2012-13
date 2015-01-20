package com.project.pushnotifications;

import static com.project.pushnotifications.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.project.pushnotifications.CommonUtilities.EXTRA_MESSAGE;
import static com.project.pushnotifications.CommonUtilities.SENDER_ID;
import static com.project.pushnotifications.MainActivity.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.project.pushnotifications.R;
import com.google.android.gcm.GCMRegistrar;
import static com.project.pushnotifications.GCMIntentService.lat;
import static com.project.pushnotifications.GCMIntentService.lng;
public class MainActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Connection detector
	ConnectionDetector cd;
	
	public static String name;
	public static String email;
	public static String child_id;
	public static String child_name;
	
	public static String home_lat="18.5087";
	public static String home_lng="73.8265";
	
	public static EditText latEdit;
	public static EditText lngEdit;
	
	public static EditText name_edittext;
	public static EditText email_edittext;
	public static EditText child_id_edittext;
	public static EditText child_name_edittext;
	
	public static SharedPreferences pref_data ;
	public  static Editor editor ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	////////////////////////////////////////////////////////////////////////////////////////////
		pref_data= getApplicationContext().getSharedPreferences("GPS_parent_data", 0); // 0 - for private mode
		editor= pref_data.edit();
	////////////////////////////////////////////////////////////////////////////////////////
		/*
		editor.putInt(key, value)
		editor.putBoolean("key_name", true); // Storing boolean - true/false
		editor.putString("key_name", "string value"); // Storing string
		editor.putInt("key_name", "int value"); // Storing integer
		editor.putFloat("key_name", "float value"); // Storing float
		editor.putLong("key_name", "long value"); // Storing long
		 
		editor.commit(); // commit changes
		*/
		//////////////////////////////////////////////////////////////////////////////////
		
		// returns stored preference value
		// If value is not present return second param value - In this case null
		
		name=pref_data.getString("name", name);
		email=pref_data.getString("email", email);
		child_id=pref_data.getString("child_id", child_id);
		child_name=pref_data.getString("child_name", child_name);
		home_lat=pref_data.getString("home_lat", "18.5087");
		home_lng=pref_data.getString("home_lng", "73.8265");
		lat=Double.valueOf(pref_data.getString("lat_str", "18.4972"));
		lng=Double.valueOf(pref_data.getString("lng_str", "73.8026"));
      
		
		////////////////////////////////////////////////////////////////////////////////////
		latEdit=(EditText)findViewById(R.id.editText2);
		lngEdit=(EditText)findViewById(R.id.editText1);
		name_edittext=(EditText)findViewById(R.id.txtName);
		email_edittext=(EditText)findViewById(R.id.txtEmail);
		child_id_edittext=(EditText)findViewById(R.id.txtChild_id);
		child_name_edittext=(EditText)findViewById(R.id.txtChild_name);
		
		latEdit.setText(home_lat);
		lngEdit.setText(home_lng);
		/*
		name_edittext.setText(name);
		email_edittext.setText(email);
		child_id_edittext.setText(child_id);
		child_name_edittext.setText(child_name);
		*/
		cd = new ConnectionDetector(getApplicationContext());
		
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		
		///////////////////
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {

		        try {
		        	Intent i = getIntent();
					
					name = i.getStringExtra("name");
					email = i.getStringExtra("email");	
					child_id = i.getStringExtra("child_id");
					child_name= i.getStringExtra("child_name");
					
		        }  catch (Exception e) {
		        
		        //    Toast.makeText(getApplicationContext(), "ChildACt yzzzzzzzz", Toast.LENGTH_SHORT).show();
		         }
	    }
		};
		thread.start();
	
		// Getting name, email from intent

		editor.putString("name", name);
		editor.putString("email", email);
		editor.putString("child_id", child_id);
		editor.putString("child_name", child_name);		 
		editor.commit(); // commit changes
		
		
		///////////////////
		// Making sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Making sure the manifest was properly set
		
		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
				
		
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context,name,email,child_name, child_id, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}		

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			lblMessage.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	public void go_to_map(View view){
		Intent go_to_map= new Intent(this,Show_on_map.class);
		startActivity(go_to_map);
	}
	public void save_home_coordinate(View view){
		
		//latEdit.setText(home_lat);
		//lngEdit.setText(home_lng);
		
		home_lat=latEdit.getText().toString();
		home_lng=lngEdit.getText().toString();
		editor.putString("home_lat", home_lat);
		editor.putString("home_lng", home_lng);
	}
	
}
