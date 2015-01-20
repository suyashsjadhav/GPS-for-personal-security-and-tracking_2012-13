package com.project.gps_trck_child;


import java.io.IOException;
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
import org.apache.http.protocol.HTTP;

import com.project.gps_trck_child.Qr_read_activity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.telephony.TelephonyManager;
import android.util.Log;

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Child_main_screen extends Activity {
	public final static String EXTRA_MESSAGE_QR="Starting QR read";
	public String QR;
	public String latitude;
	public String longitude;
	public double longitude1;
	public double latitude1;
	public String number,id;
	public static String device_id; 
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_main_screen);
        TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tManager.getDeviceId(); 	
        //regular_update();
        
      
        //////////////////////////////////////////////////////////////////////////
        final Intent call_GPS_CoFetch=new Intent(this,GPS_tracking_activity.class);
        /////////////////////////////////////////////////////////////////////////    	
        if(!this.isFinishing()){
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
        
        	public void run() 
        	{
        		// code here what ever is required
        		//	/////////////////////////////////////////////////////////


        		startActivityForResult(call_GPS_CoFetch,1);
        		
        		///////////////////////////////////////////////////////////
        		handler.postDelayed(this, 1000*15);
        		}
        };
        
        handler.postDelayed(r, 1000*15);
        }
        //////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.child_main_screen, menu);
        return true;
    
    }
    
    
    
    public void start_qr_read(View view){
    	Intent call_qr_read=new Intent(this,Qr_read_activity.class);
 		startActivityForResult(call_qr_read,0);
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

 
        switch(requestCode){
        case 0:  {
        	QR=intent.getStringExtra("QR");
        	latitude=intent.getStringExtra("latitude");
        	longitude=intent.getStringExtra("longitude");
         	Toast.makeText(getApplicationContext(), "ChildACt \nYour Location is - \nLat: " + latitude + "\nLong: " + longitude+"\nQR code--"+QR, Toast.LENGTH_SHORT).show();
        	
       	// Create a new HttpClient and Post Header
      
        // Building post parameters
        // key and value pair
        
       do_post();
 
        
       Toast.makeText(getApplicationContext(), "ChildACt main", Toast.LENGTH_SHORT).show();
  //     	Intent GPS_coordinate_send=new Intent(this,GPS_coordinate_send.class);
   //  	startActivity(GPS_coordinate_send);
     break;
        }
        ///////////////////////////////////////////////////////////////////
        case 1:  {
        	
      //  	latitude=intent.getStringExtra("latitude");
        //	longitude=intent.getStringExtra("longitude");
         //	Toast.makeText(getApplicationContext(), "## REPEAT \nYour Location is - \nLat: " + latitude + "\nLong: " + longitude+"\nQR code--"+QR, Toast.LENGTH_SHORT).show();
        	
         	//////////////////////////////////////////////////
        	QR="";
           	latitude1=intent.getDoubleExtra("latitude",latitude1);
          	longitude1=intent.getDoubleExtra("longitude",longitude1);
          	
          	Toast.makeText(getApplicationContext(), "##REPEAT Your Location is - \nLat: " + latitude + "\nLong: " + longitude+"\nQR code--"+QR, Toast.LENGTH_SHORT).show();

           //latitude1=00.00;
           //longitude1=110.00;

          	//////////////////////////////////////////////////////
          	latitude=String.valueOf(latitude1);
          	longitude=String.valueOf(longitude1);

         	//////////////////////////////////////////////////
         	regular_update();  
       
         	
     break;
        }
        
        ///////////////////////////////////////////////////////////////////
        }
        }
	
	public void do_post(){
			
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		    	
		    	// Creating HTTP client
			    HttpClient httpClient = new DefaultHttpClient();
			    // Creating HTTP Post
			    HttpPost httpPost = new HttpPost("http://192.168.1.5/www_soga/GCM_ser/child.php");
				
		    	// Making HTTP Request
		        try {
		        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
		          
		        	nameValuePair.add(new BasicNameValuePair("device_id",device_id));
		            nameValuePair.add(new BasicNameValuePair("latitude",latitude));
		     		nameValuePair.add(new BasicNameValuePair("longitude", longitude));
		     		nameValuePair.add(new BasicNameValuePair("QR", QR));
		     		
		     		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
		            
		     		HttpResponse response = httpClient.execute(httpPost);
		 
		            // writing response to log
		            Log.d("Http Response:", response.toString());
		        } catch (ClientProtocolException e) {
		        
		        // 	Toast.makeText(getApplicationContext(), "ChildACt yzzz", Toast.LENGTH_SHORT).show();
		        } catch (IOException e) {
		        
		        //    Toast.makeText(getApplicationContext(), "ChildACt yzzzzzzzz", Toast.LENGTH_SHORT).show();
		         }
	    }
		};
		thread.start();

	}

	public void regular_update(){

		
	  	/////////////////////////////////////////////////////////////////
		
		Thread thread1 = new Thread()
		{
		    @Override
		    public void run() {
		    
		    	//////////////////////////////////////////////////////////////////
				// Creating HTTP client
			    HttpClient httpClient = new DefaultHttpClient();
			    // Creating HTTP Post
			    HttpPost httpPost = new HttpPost("http://192.168.1.5/www_soga/GCM_ser/child2.php");
				
				
			    
		    	// Making HTTP Request
		        try {
		        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
		          
		        	nameValuePair.add(new BasicNameValuePair("device_id",device_id));
		            nameValuePair.add(new BasicNameValuePair("latitude",latitude));
		     		nameValuePair.add(new BasicNameValuePair("longitude", longitude));
		     		nameValuePair.add(new BasicNameValuePair("QR", QR));
		     		
		     		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
		            
		     		HttpResponse response = httpClient.execute(httpPost);
		 
		            // writing response to log
		            Log.d("Http Response:", response.toString());
		        } catch (ClientProtocolException e) {
		        
		        // 	Toast.makeText(getApplicationContext(), "ChildACt yzzz", Toast.LENGTH_SHORT).show();
		        } catch (IOException e) {
		        
		        //    Toast.makeText(getApplicationContext(), "ChildACt yzzzzzzzz", Toast.LENGTH_SHORT).show();
		         }
	        	//////////////////////////////////////////////////////
		      
	    }
		};

		thread1.start();


	}
/*
	@Override
	protected void onDestroy()
	{
		
		//finish();
	}*/
}
