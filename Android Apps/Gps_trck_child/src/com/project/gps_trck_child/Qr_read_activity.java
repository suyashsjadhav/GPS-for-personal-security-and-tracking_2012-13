package com.project.gps_trck_child;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.media.MediaPlayer;
import android.content.Context;
import android.content.Intent;
import android.widget.*;


public class Qr_read_activity extends Activity {
Context context;
int duration= Toast.LENGTH_SHORT;
public String QR;

public double latitude ;
public double longitude ;
public String latitude1;
public String longitude1;


@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_read_activity);

     	//******************************************************************
	

      	Intent QRdroid = new Intent("com.google.zxing.client.android.SCAN");  
  		QRdroid.putExtra("SCAN_MODE", "QR_CODE_MODE");
  	    startActivityForResult(QRdroid, 0);
         
     	//******************************************************************
  /*   	// Creating delay and then starting activity....
     	  new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
              }
          }, 100);*/
  		
	     //*****************************************************************
  	 	  
     	//*******************************************************************
  	    
  	    //sending data to server
  		//******************************************************************  	

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qr_read_activity, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode){
        case 0:  {
   
  
            if (resultCode == RESULT_OK) {
            	QR=intent.getStringExtra("SCAN_RESULT");
            	
            	Intent call_GPS_CoFetch=new Intent(this,GPS_tracking_activity.class);
               	startActivityForResult(call_GPS_CoFetch,1);
          	      
                //   Creating delay and then starting activity....
               /*	 new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
               //          	Toast.makeText(getApplicationContext(), " Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);*/
//            	

            } else if (resultCode == RESULT_CANCELED) {
            }
        }
        break;
  
        case 1:  {
        	   
          if (resultCode == RESULT_OK) {
  
      
           	latitude=intent.getDoubleExtra("latitude",latitude);
          	longitude=intent.getDoubleExtra("longitude",longitude);
          	
          	Toast.makeText(getApplicationContext(), " Your Location is - \nLat: " + latitude + "\nLong: " + longitude+"\nQR code--"+QR, Toast.LENGTH_SHORT).show();

           playAlertTone(getApplicationContext());

          	//////////////////////////////////////////////////////
          	latitude1=String.valueOf(latitude);
          	longitude1=String.valueOf(longitude);
///////////////////////////////////////////////////////////////
          	
        	Intent result =new Intent();
      	        result.putExtra("latitude",latitude1);
      	        result.putExtra("longitude",longitude1);
      	        result.putExtra("QR", QR);
      	        setResult(Activity.RESULT_OK,result);
      	        finish();
        	
           } else if (resultCode == RESULT_CANCELED) {
           	//if gps avtivity fails...........
        
           }
      
       }
        break;
       
    	//Toast.makeText(context, text1, duration).show();
        }
        
     //   Creating delay and then starting activity....
/*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
    
            }
        }, 5000);*/
    	
       	
    }

//////************************************************************************/////////
	

	public  void playAlertTone(final Context context){
		

	    Thread t = new Thread(){
	            public void run(){
	                MediaPlayer player = null;
	                int countBeep = 0;
	                while(countBeep<2){
	      //          player = MediaPlayer.create(context,Settings.System.DEFAULT_NOTIFICATION_URI);
	                	  player = MediaPlayer.create(context,R.raw.beepbeep);
	                player.start();
	                countBeep+=1;
	                try {

	                                // 100 milisecond is duration gap between two beep
	                    Thread.sleep(player.getDuration()+100);
	                                       player.release();
	                } catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }


	                }
	            }
	        };

	        t.start();   

	    }



}















