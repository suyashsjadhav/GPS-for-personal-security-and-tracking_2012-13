package com.project.pushnotifications;

import static com.project.pushnotifications.CommonUtilities.SENDER_ID;
import static com.project.pushnotifications.CommonUtilities.SERVER_URL;

import com.project.pushnotifications.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import static com.project.pushnotifications.MainActivity.name;
import static com.project.pushnotifications.MainActivity.email;
import static com.project.pushnotifications.MainActivity.child_id;
import static com.project.pushnotifications.MainActivity.child_name;

public class RegisterActivity extends Activity {
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Internet detector
	ConnectionDetector cd;
	
	// UI elements
	EditText txtName;
	EditText txtEmail;
	EditText txtChild_id;
	EditText txtChild_name;
	
	// Register button
	Button btnRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}
		
		txtName = (EditText) findViewById(R.id.txtName);
		txtName.setText(name);
		
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtEmail.setText(email);
		
		txtChild_name = (EditText) findViewById(R.id.txtChild_name);
		txtChild_name.setText(child_name);

		txtChild_id = (EditText) findViewById(R.id.txtChild_id);
		txtChild_id.setText(child_id);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		
		/*
		 * Click event on Register button
		 * */
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Read EditText dat
				String name = txtName.getText().toString();
				String email = txtEmail.getText().toString();
				String child_name = txtChild_name.getText().toString();
				String child_id = txtChild_id.getText().toString();
				
				
				// Check if user filled the form
				if(name.trim().length() > 0 && email.trim().length() > 0 && child_id.trim().length() > 0 && child_name.trim().length() > 0){
					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					
					// Registering user on our server					
					// Sending registraiton details to MainActivity
					i.putExtra("name", name);
					i.putExtra("email", email);
					i.putExtra("child_name",child_name);
					i.putExtra("child_id",child_id);
					startActivity(i);
					finish();
				}else{
					// user doen't filled that data
					// ask him to fill the form
					alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
				}
			}
		});
	}

}
