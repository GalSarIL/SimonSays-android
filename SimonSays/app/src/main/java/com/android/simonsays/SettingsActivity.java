package com.android.simonsays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;


public class SettingsActivity extends AppCompatActivity {
	
	private String fontName = "";
	private MaterialButton btnOn, btnOff, btnHebrew, btnEnglish;
	private SharedPreferences music;
	private MediaPlayer mp;

	/**
	 *
	 * On create function, calls initialize functions
	 */
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		initializeLogic();
	}

	/**
	 * Initialize activity screen, getting the current state of music and is it on/off
	 */
	private void initialize(Bundle _savedInstanceState) {
		btnOn = (MaterialButton) findViewById(R.id.btnOn);
		btnOff = (MaterialButton) findViewById(R.id.btnOff);
		music = getSharedPreferences("music", Activity.MODE_PRIVATE);
		btnEnglish = (MaterialButton) findViewById(R.id.btnEnglish);
		btnHebrew = (MaterialButton) findViewById(R.id.btnHebrew);

		/*
		Enable music button listener
		 */
		btnOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if(mp != null){
				}else{
					mp = MediaPlayer.create(getApplicationContext(), R.raw.order);
					mp.start();
					mp.setLooping(true);
					btnOn.setVisibility(View.GONE);
					btnOff.setVisibility(View.VISIBLE);
					music.edit().putString("music", "true").commit();
				}
			}
		});
		/*
		Disable music button listener
		 */
		btnOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				btnOff.setVisibility(View.GONE);
				btnOn.setVisibility(View.VISIBLE);
				music.edit().putString("music", "false").commit();
				if(mp != null){
					if (mp.isPlaying()) {
						mp.pause();
						mp.reset();
						mp.release();
					}
					else {
						
					}
				}
				finish();
			}
		});
		/*
		Switch language button to English by using setLocale with en parameter
		 */
		btnEnglish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setLocale("en");
			}
		});
		/*
		Switch language button to Hebrew by using setLocale with iw parameter
		 */
		btnHebrew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				setLocale("iw");
			}
		});
	}

	/**
	 * Initialize enable and disable music button by receiving last state from user shared preferences
	 */
	private void initializeLogic() {
		_changeActivityFont("indie_flower");
		btnOn.setVisibility(View.GONE);
		btnOff.setVisibility(View.GONE);
		if (music.getString("music", "").equals("true")) {
			btnOff.setVisibility(View.VISIBLE);
		}
		else {
			btnOn.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	/**
	 * Here we override fonts with out fonts
	 */
	public void _changeActivityFont (final String _fontname) {
		fontName = "fonts/".concat(_fontname.concat(".ttf"));
		overrideFonts(this,getWindow().getDecorView()); 
	}
	/**
	 * Implements the fonts
	 */
	private void overrideFonts(final Context context, final View v) {
		
		try {
			Typeface
					typeface = Typeface.createFromAsset(getAssets(), fontName);;
			if ((v instanceof ViewGroup)) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0;
				i < vg.getChildCount();
				i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			}
			else {
				if ((v instanceof TextView)) {
					((TextView) v).setTypeface(typeface);
				}
				else {
					if ((v instanceof EditText )) {
						((EditText) v).setTypeface(typeface);
					}
					else {
						if ((v instanceof Button)) {
							((Button) v).setTypeface(typeface);
						}
					}
				}
			}
		}
		catch(Exception e)
		
		{
			Util.showMessage(getApplicationContext(), "Error Loading Font");
		};
	}

	/**
	 * Taking all the resources (display and configuration), creating Locale object and implementing the "decided" language string xml
	 * as the current used one
	 * default will be by device language
	 * @param lang - language you wish to change app to
	 */
	public void setLocale(String lang) {
		Locale myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Intent refresh = new Intent(this, HomeActivity.class);
		finish();
		startActivity(refresh);
	}
}
