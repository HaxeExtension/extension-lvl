package org.haxe.extension;

import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;


/* 
	Google Play License Verification extension
	@author Pozirk Games (https://www.pozirk.com)
*/
public class ExtensionLVL extends Extension
{	
	private static final String BASE64_PUBLIC_KEY = "::SET_LVL_PUBLIC_KEY::";
	private static final byte[] PEPPER = new byte[20]; //why does everyone call it SALT? let's call it SUGAR!
	
	private static int _res = -1;
	
	private static LicenseChecker _checker; 
	private static LicenseCheckerCallback _checkerCallback;
	
	private Handler _handler;
		
	public static int isLicensed()
	{
		return _res;
	}
	
	/**
	 * Called when an activity you launched exits, giving you the requestCode 
	 * you started it with, the resultCode it returned, and any additional data 
	 * from it.
	 */
	public boolean onActivityResult (int requestCode, int resultCode, Intent data)
	{
		return true;
	}

	/**
	 * Called when the activity receives th results for permission requests.
	 */
	public boolean onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		return true;
	}
	
	
	/**
	 * Called when the activity is starting.
	 */
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		
		_handler = new Handler();
		
		Random rnd = new Random();
		rnd.nextBytes(PEPPER);
		
		//Extension.mainActivity.setContentView(R.layout.main);
		
		String deviceId = Secure.getString(Extension.mainActivity.getContentResolver(), Secure.ANDROID_ID); 
		
		_checkerCallback = new MyLicenseCheckerCallback(); 
		_checker = new LicenseChecker(Extension.mainActivity, new ServerManagedPolicy(Extension.mainActivity, new AESObfuscator(PEPPER, Extension.mainActivity.getPackageName(), deviceId)), BASE64_PUBLIC_KEY); 
		_checker.checkAccess(_checkerCallback);
	}
	
	private void displayResult(final String result)
	{
		_handler.post(new Runnable()
		{
			public void run() { Log.i("MainActivityLicense", "License check result: " + result); }
		});
	}
	
	
	public class MyLicenseCheckerCallback implements LicenseCheckerCallback
	{	
		public void allow(int policyReason)
		{
			_res = policyReason;
			String result = String.format("Access allowed. " + policyReason);
			displayResult(result);
		}

		public void dontAllow(int policyReason)
		{
			_res = policyReason;
			String result = String.format("Access disallowed. " + policyReason);
			displayResult(result);
			
			// Should not allow access. In most cases, the app should assume
			// the user has access unless it encounters this. If it does,
			// the app should inform the user of their unlicensed ways
			// and then either shut down the app or limit the user to a
			// restricted set of features.
			// In this example, we show a dialog that takes the user to Market.
			// If the reason for the lack of license is that the service is
			// unavailable or there is another problem, we display a
			// retry button on the dialog and a different message.
			/*if(policyReason != Policy.RETRY)
			{
				_checker.followLastLicensingUrl(Extension.mainActivity);
				Extension.mainActivity.finish();
			}*/
		}
		
		 public void applicationError(int errorCode)
		 {
				_res = errorCode;
				String result = String.format("Application error: " + errorCode);
				displayResult(result);
		 }
	}
	
	/**
	 * Perform any final cleanup before an activity is destroyed.
	 */
	public void onDestroy()
	{
		super.onDestroy();
		_checker.onDestroy();	
	}
	
	
	/**
	 * Called as part of the activity lifecycle when an activity is going into
	 * the background, but has not (yet) been killed.
	 */
	public void onPause() {}
	
	
	/**
	 * Called after {@link #onStop} when the current activity is being 
	 * re-displayed to the user (the user has navigated back to it).
	 */
	public void onRestart() {}
	
	
	/**
	 * Called after {@link #onRestart}, or {@link #onPause}, for your activity 
	 * to start interacting with the user.
	 */
	public void onResume() {}
	
	
	/**
	 * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when  
	 * the activity had been stopped, but is now again being displayed to the 
	 * user.
	 */
	public void onStart() {}
	
	
	/**
	 * Called when the activity is no longer visible to the user, because 
	 * another activity has been resumed and is covering this one. 
	 */
	public void onStop() {}
}