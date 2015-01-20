package com.project.pushnotifications;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
 

import static com.project.pushnotifications.GCMIntentService.lat;
import static com.project.pushnotifications.GCMIntentService.lng;
import static com.project.pushnotifications.MainActivity.home_lat;
import static com.project.pushnotifications.MainActivity.home_lng;
import static com.project.pushnotifications.MainActivity.latEdit;
import static com.project.pushnotifications.MainActivity.lngEdit;
import static com.project.pushnotifications.MainActivity.editor;
public class Show_on_map extends FragmentActivity implements LocationListener {
	
	

	GoogleMap googleMap;
	public LatLng home_loc = new LatLng(Double.valueOf(home_lat),Double.valueOf(home_lng));
	public MarkerOptions marker_home_loc=new MarkerOptions();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_on_map);
	
		// Getting reference to the SupportMapFragment of activity_main.xml
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		LatLng child_loc = new LatLng(lat,lng);
	

		// Getting GoogleMap object from the fragment
		googleMap = fm.getMap();
		
	    //*googleMap.setOnMarkerDragListener(googleMap.OnMarkerDragListener listener);

		// Enabling MyLocation Layer of Google Map
	//	googleMap.setMyLocationEnabled(true);				
	//*	MarkerOptions marker_home_loc = new MarkerOptions().draggable(true);
		

        MarkerOptions marker_child_loc = new MarkerOptions();
        
        
        marker_child_loc.position(child_loc);
        marker_child_loc.title("Child Location");
        
        marker_home_loc.position(home_loc);
        marker_home_loc.title("Home");
        marker_home_loc.icon(BitmapDescriptorFactory.fromResource(R.drawable.home));
        marker_home_loc.draggable(true);
       
        
       googleMap.moveCamera(CameraUpdateFactory.newLatLng(child_loc));
       googleMap.animateCamera(CameraUpdateFactory.newLatLng(child_loc));
       googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
   	   googleMap.addMarker(marker_child_loc);
   	   googleMap.addMarker(marker_home_loc);


		 Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
		List<Address> addresses = null;
		  // Call the synchronous getFromLocation() method by passing in the lat/long values.
       
		try {
			addresses = geocoder.getFromLocation(child_loc.latitude, child_loc.longitude, 1);
		} 
       catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
		if (addresses != null && addresses.size() > 0) {
           Address address = addresses.get(0);
           // Format the first line of address (if available), city, and country name.

           String addressText = String.format("%s, %s, %s",
                   address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                   address.getLocality(),
                   address.getCountryName());
    		  // Setting the title for the marker.
           // This will be displayed on taping the marker
           marker_child_loc.title("Child Location-"+addressText);
         
       	googleMap.addMarker(marker_child_loc);
        
        // Clears the previously touched position
        //googleMap.clear();

        // Animating to the touched position
      

        // Placing a marker on the touched position
      
		}
		
		
      
     // Zoom in the Google Map
     	
     		  
     	
     	
        
        
        
        
     // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new OnMapClickListener() {
 
            @Override
            public void onMapClick(LatLng latLng) {
            }
    ///*        @Override
  ///*          public void onMarkerDragEnd(marker_home_loc){}
         });
        
        googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
               
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            	 
            	   home_loc=marker.getPosition();
                   home_lat=String.valueOf(home_loc.latitude);
                   home_lng=String.valueOf(home_loc.longitude);
                   latEdit.setText(home_lat);
                   lngEdit.setText(home_lng) ;    
            /////////////////////////////////////////////////////////
                   editor.putString("home_lat", home_lat);
                   editor.putString("home_lng", home_lng);
                   editor.commit();
                   ////////////////////////////////////////////
                   double diff_lat,diff_lng;
                   diff_lat=Math.abs(Double.valueOf(home_lat)-lat);
                   diff_lng=Math.abs(Double.valueOf(home_lng)-lng);
             //  	   Toast.makeText(getApplicationContext(), "Diff: " + String.valueOf(diff_lat)+String.valueOf(diff_lng), Toast.LENGTH_SHORT).show();
                   if((diff_lat+diff_lng)<0.01)
                   {
                	   Toast.makeText(getApplicationContext(), "Chid near home " , Toast.LENGTH_SHORT).show();
                	   
                   }
            	
                   ////////////////////////////////////////////
            
            }
            	
            	

            @Override
            public void onMarkerDrag(Marker marker) {
             
            }
        });

	}
	

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}
	

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.show_on_map, menu);
	    return true;
	  }


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	  
	
	  
	}