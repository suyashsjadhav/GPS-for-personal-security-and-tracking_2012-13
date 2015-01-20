package com.project.gps_trck_child;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class GPS_coordinate_send extends Activity {

public double latitude ;
public double longitude ;
public String latitude1;
public String longitude1;

// Creating HTTP client
HttpClient httpClient = new DefaultHttpClient();
// Creating HTTP Post
HttpPost httpPost = new HttpPost("http://192.168.1.5/test2.php");


//private final static int DELAYED_ACTION_TIMEOUT = 10000;

//private Handler mRepetitiveTimeoutHandler;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_coordinate_send);
		Intent call_GPS_CoFetch=new Intent(this,GPS_tracking_activity.class);
		startActivityForResult(call_GPS_CoFetch,0);
    
       //////////////////////////////////////////////
/*
    	        mRepetitiveTimeoutHandler = new Handler();
    	        mRepetitiveTimeoutHandler.postDelayed(processWatchTimer, DELAYED_ACTION_TIMEOUT);                       
    	    }

    	    private Runnable processWatchTimer = new Runnable()
    	    {
    	        public void run() 
    	        {
    	            mRepetitiveTimeoutHandler.removeCallbacks(processWatchTimer);
    	            //Put your action here. 
    	        
    	        }
    	    };
    	    */
/*startActivityForResult(call_GPS_CoFetch,0);
 *  TimerTask tasknew = new TimerScheduleFixedRateDelay();
      Timer timer = new Timer();
      
      // scheduling the task at fixed rate delay
      timer.scheduleAtFixedRate(tasknew,500,1000);      
   }
   // this method performs the task
   public void run() {
      System.out.println("working at fixed rate delay");      
   }    
 * */
    	////////////////////////////////////////////////
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps_coordinate_send, menu);
		return true;
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode){
        case 0:  
        {
      	   
            if (resultCode == RESULT_OK) {
    
        
             	latitude=intent.getDoubleExtra("latitude",latitude);
            	longitude=intent.getDoubleExtra("longitude",longitude);
            	
//            	Toast.makeText(getApplicationContext(), " Your Location is - \nLat: " + latitude + "\nLong: " + longitude+"\nQR code--"+QR, Toast.LENGTH_SHORT).show();

  //           playAlertTone(getApplicationContext());

            	//////////////////////////////////////////////////////
            	latitude1=String.valueOf(latitude);
            	longitude1=String.valueOf(longitude);
  ///////////////////////////////////////////////////////////////
             
            	
             	// Create a new HttpClient and Post Header
          
           
            TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tManager.getDeviceId();
            
            
             // Building post parameters
            // key and value pair
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
         
            nameValuePair.add(new BasicNameValuePair("latitude",latitude1));
     		nameValuePair.add(new BasicNameValuePair("longitude", longitude1));
     		nameValuePair.add(new BasicNameValuePair("device_id",device_id));	
     		nameValuePair.add(new BasicNameValuePair("QR", ""));
            // Url Encoding the POST parameters
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }
     
            // Making HTTP Request
            try {
                HttpResponse response = httpClient.execute(httpPost);
     
                // writing response to log
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                // writing exception to log
              //  e.printStackTrace();

            } catch (IOException e) {
                // writing exception to log
               // e.printStackTrace();

     
            }
            
            }
            	
            Toast.makeText(getApplicationContext(), "ChildACt send", Toast.LENGTH_SHORT).show();
            break;
        	}
        }
        
        }

	}
	
	
