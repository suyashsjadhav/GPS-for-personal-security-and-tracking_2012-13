package com.project.pushnotifications;

import static com.project.pushnotifications.CommonUtilities.SENDER_ID;
import static com.project.pushnotifications.CommonUtilities.displayMessage;
import static com.project.pushnotifications.MainActivity.home_lat;
import static com.project.pushnotifications.MainActivity.home_lng;

import static com.project.pushnotifications.MainActivity.editor;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.project.pushnotifications.R;
import com.google.android.gcm.GCMBaseIntentService;;
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
public String message;
public String addressText;
public static double lat=18.4972;
public static double lng=73.8026;
    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", MainActivity.name);
        ServerUtilities.register(context, MainActivity.name, MainActivity.email, MainActivity.child_name, MainActivity.child_id, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
         message = intent.getExtras().getString("price");
         int flag;
      //   displayMessage(context, message);
   if(message!=null){
       String latlng[]=message.split(" ");
          flag=0;      
  
          
          if(latlng[0].equals("0")){
         message="Child Latitude= "+latlng[1]+"Longitude= "+latlng[2];
         message="\nYour child Departed the bus.."+message;
         flag=1;
         }
         if(latlng[0].equals("1")){
         message="Child Latitude= "+latlng[1]+"Longitude= "+latlng[2];
         message="Your child Entered the bus..\n"+message;
         flag=1;
         }	 
         if(latlng[0].equals("2")){
         message="Child Latitude= "+latlng[1]+" Longitude= "+latlng[2];
         flag=1;
         }
             
        	 
         
      if(flag==1){
         editor.putString("lat_str", latlng[1]);
         editor.putString("lng_str", latlng[2]);
         editor.commit();
         
         lat=Double.parseDouble(latlng[1]);
  		 lng=Double.parseDouble(latlng[2]);
      
  	   double diff_lat,diff_lng;
       diff_lat=Math.abs(Double.valueOf(home_lat)-lat);
       diff_lng=Math.abs(Double.valueOf(home_lng)-lng);
   	 //  Toast.makeText(getApplicationContext(), "Diff: " + String.valueOf(diff_lat)+String.valueOf(diff_lng), Toast.LENGTH_SHORT).show();
       if((diff_lat+diff_lng)<0.01)
       {
    	   generateNotification(context, "Your Child Reached Near Home...\n Please get ready to pick him up.."); 
    	   
       }
		////////////////////////////////////////////
		//Reverse Geo-Coding for fetching address 
		//////////////////////////////////////////
		 Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
			List<Address> addresses = null;
			  // Call the synchronous getFromLocation() method by passing in the lat/long values.
	       
			try {
				addresses = geocoder.getFromLocation(lat, lng, 1);
			} 
	       catch (IOException e) {
				// TODO Auto-generated catch block
	    	   addressText="Error Service not available from ISP ";
				e.printStackTrace();
			}
	       
	       
			if (addresses != null && addresses.size() > 0) {
	           Address address = addresses.get(0);
	           // Format the first line of address (if available), city, and country name.

	           addressText = String.format("%s, %s, %s",
	                   address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                   address.getLocality(),
	                   address.getCountryName());
	           }
	 /////////////////////////////////////////////////////////////////////
	        message=message+"\nChild Address: "+addressText;   
     
      }
   }
	        displayMessage(context, message);
    //   displayMessage(context, message1);

        // notifies user
        generateNotification(context, message);
       // generateNotification(context, message1);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);

        displayMessage(context, message);

        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     * @param message1 
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      

    }



}
