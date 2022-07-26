package com.android.simonsays;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivity extends AppCompatActivity {
	private final Timer _timer = new Timer();
	private String fontName = "";

	private ImageView imgIcon;
	private TextView btnStart;
	private TextView txtLeaderBoard;
	private TextView txtSettings;
	private TextView txtName;
	
	private final ObjectAnimator anime = new ObjectAnimator();
	private SharedPreferences savename;

	/**
	 * On create function, calling initialize functions,
	 * also hides action bar on home screen
	 */
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		initializeLogic();
		getSupportActionBar().hide();
	}

	/**
	 * Initialize function, we get Views at runtime, and the shared preferences
	 */
	private void initialize(Bundle _savedInstanceState) {
		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		ImageView imgName = (ImageView) findViewById(R.id.imgName);
		btnStart = (TextView) findViewById(R.id.btnStart);
		txtLeaderBoard = (TextView) findViewById(R.id.txtLeaderBoard);
		txtSettings = (TextView) findViewById(R.id.txtSettings);
		txtName = (TextView) findViewById(R.id.txtName);
		savename = getSharedPreferences("savename", Activity.MODE_PRIVATE);

		/**
		 * Here we create the user and saving it to shared preferences
		 */
		imgName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {

				final com.google.android.material.bottomsheet.BottomSheetDialog btmD = new com.google.android.material.bottomsheet.BottomSheetDialog(HomeActivity.this);
				View btmV;
				btmV = getLayoutInflater().inflate(R.layout.entername,null );
				btmD.setContentView(btmV);

				btmD.getWindow().findViewById(R.id.bg).setBackgroundResource(android.R.color.transparent);

				final LinearLayout back = (LinearLayout) btmV.findViewById(R.id.bg);
				final TextView title = (TextView) btmV.findViewById(R.id.txtTitle);
				final EditText name = (EditText) btmV.findViewById(R.id.edtName);
				final TextView save = (TextView) btmV.findViewById(R.id.btnSave);
				//final TextInputLayout layout = (TextInputLayout) btmV.findViewById(R.id.input);
				save.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/indie_flower.ttf"), Typeface.BOLD);
				title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/indie_flower.ttf"), Typeface.BOLD);
				back.setBackground(getDrawable(R.drawable.entername_back));
				save.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						savename.edit().putString("name", name.getText().toString()).commit();
						btmD.dismiss();
						_clickAnimation(save);
						Util.showMessage(getApplicationContext(), getString(R.string.savedSuccTxt));
					}
				});
				
				btmD.setCancelable(true);
				btmD.show();
			}
		});

		/**
		 * Start button, here we start the game
		 */
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent start = new Intent(HomeActivity.this, GameActivity.class);
				startActivity(start);
				_clickAnimation(btnStart);
			}
		});
		/**
		 * In case user entered Leaderboard without his name, we ask him for name
		 */
		txtLeaderBoard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_clickAnimation(txtLeaderBoard);
				if (txtName.getText().toString().equals("Hi there")) {
					Util.showMessage(getApplicationContext(), "Please enter your name ");
				}
				else {
					Intent leader = new Intent(HomeActivity.this, LeaderboardActivity.class);
					startActivity(leader);
				}
			}
		});
		/**
		 * Settings button
		 */
		txtSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
				startActivity(settings);
				_clickAnimation(txtSettings);
			}
		});
	}

	/**
	 * Initialize Simon Says logo + Getting the last known user name
	 */
	private void initializeLogic() {
		anime.setTarget(imgIcon);
		anime.setPropertyName("rotation");
		anime.setFloatValues((float)(2480), (float)(0));
		anime.setDuration((int)(2000));
		anime.setInterpolator(new DecelerateInterpolator());
		anime.start();
		_changeActivityFont("indie_flower");
		TimerTask t = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (savename.getString("name", "").equals("")) {

						} else {
							txtName.setText(getString(R.string.hi_txt) + " ".concat(savename.getString("name", "")));
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(t, (int)(10), (int)(10));
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
					typeface = Typeface.createFromAsset(getAssets(), fontName);
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
		}
	}

	/**
	 * Fade animations for Home Activity buttons
	 */
	public void _clickAnimation (final View _view) {
		ScaleAnimation fade_in = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.7f);
		fade_in.setDuration(300);
		fade_in.setFillAfter(true);
		_view.startAnimation(fade_in);
	}
	

}
