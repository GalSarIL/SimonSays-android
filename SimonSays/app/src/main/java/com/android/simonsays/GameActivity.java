package com.android.simonsays;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends AppCompatActivity implements OnClickListener {
    final private Handler mhandler = new Handler();


    MaterialButton[] colours;
    final int[] count = {0};
    TextView finalScore;
    public int score;
    final ArrayList<Integer> pattern = new ArrayList<>();
    final ArrayList<Integer> guess = new ArrayList<>();
    private double id = 0;
    public int currClick;

    private SoundPool sp;
    SharedPreferences savename;

    /**
     * On create function for activity, initialize and receive color buttons (MaterialButton) at runtime
     * Declare of MaterialButton array for showPattern function
     * Declare and initialize game variables
     */
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.game);
        initialize(_savedInstanceState);
        MaterialButton green = findViewById(R.id.Green);
        MaterialButton blue = findViewById(R.id.Blue);
        MaterialButton red = findViewById(R.id.Red);
        MaterialButton purple = findViewById(R.id.Purple);
        finalScore = findViewById(R.id.Score);
        colours = (MaterialButton[]) new MaterialButton[]{green, blue, red, purple};

        green.setOnClickListener(this);
        blue.setOnClickListener(this);
        red.setOnClickListener(this);
        purple.setOnClickListener(this);
        score = 0;
        currClick = 0;
        initializeLogic();
        showPattern(colours, score);
    }

    /**
     * Initialize the activity, loading the saved instance state + shared preferences.
     */
    private void initialize(Bundle _savedInstanceState) {
        savename = getSharedPreferences("savename", Activity.MODE_PRIVATE);

    }

    /**
     * Declare the sounds for buttons
     */
    private void initializeLogic() {
        sp = new SoundPool((int) (4), AudioManager.STREAM_MUSIC, 0);
        id = sp.load(getApplicationContext(), R.raw.bassclarinetc405, 1);
        id = sp.load(getApplicationContext(), R.raw.bassclarinetd405, 1);
        id = sp.load(getApplicationContext(), R.raw.bassclarinete405, 1);
        id = sp.load(getApplicationContext(), R.raw.bassclarinetf405, 1);
    }

    /**
     * This function shows the pattern for the user.
     * The function iterates through the number of current score the user is at (currScore + new color + 1)
     * Each iteration, we have switch case for current color to be shown
     * the function blinks the color using handler with background color and interval of 500ms
     */
    public void showPattern(final MaterialButton[] colours, int score) {
        String result = Integer.toString(score);
        finalScore.setText(getString(R.string.currScoreTxt) + result);
        Random rand = new Random();
        int i = 0;
        pattern.add(rand.nextInt(4));
        for (int j = 1; j < score + 2; j++) {
            int finalI = i;
            mhandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    int x = pattern.get(finalI);
                    final int[] b = {x};
                    switch (x) {
                        case 0:
                            colours[x].setBackgroundColor(getColor(R.color.lightGreen));
                            id = sp.play((int) (1), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                            break;
                        case 1:
                            colours[x].setBackgroundColor(getColor(R.color.lightBlue));
                            id = sp.play((int) (2), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                            break;
                        case 2:
                            colours[x].setBackgroundColor(getColor(R.color.lightRed));
                            id = sp.play((int) (3), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                            break;
                        case 3:
                            colours[x].setBackgroundColor(getColor(R.color.lightPurple));
                            id = sp.play((int) (4), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                            break;
                        default:
                            break;
                    }
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch (b[0]) {
                                case 0:
                                    colours[b[0]].setBackgroundColor(getColor(R.color.green));
                                    break;
                                case 1:
                                    colours[b[0]].setBackgroundColor(getColor(R.color.blue));
                                    break;
                                case 2:
                                    colours[b[0]].setBackgroundColor(getColor(R.color.red));
                                    break;
                                case 3:
                                    colours[b[0]].setBackgroundColor(getColor(R.color.purple));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, 500);
                }
            }, 1000 * j);
            i++;
        }
    }

    /**
     * This function occurs when the user press a button
     * we use switch case for each button
     * Then we check if the pattern equals to the guess the user made, if it does, we move to new set of colors
     * If not, we check if user havent finished clicking on all the buttons, if yes, we continue
     * if not, we pop a fail msg, and save the user current score.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Green:
                guess.add(0);
                id = sp.play((int) (1), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                if (pattern.equals(guess)) {
                    count[0] = 0;
                    guess.clear();
                    score++;
                    showPattern(colours, score);
                } else if (pattern.get(count[0]) != 0) {
                    finalScore.setText(getString(R.string.gameOverTxt) + score);
                    savename.edit().putString("score", String.valueOf(score)).commit();
                    Toast.makeText(this, getString(R.string.closeGameTxt), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() ->{
                        finish();
                    }, 2000);

                } else {
                    count[0]++;
                }
                break;
            case R.id.Blue:
                guess.add(1);
                id = sp.play((int) (2), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                if (pattern.equals(guess)) {
                    count[0] = 0;
                    guess.clear();
                    score++;
                    showPattern(colours, score);
                } else if (pattern.get(count[0]) != 1) {
                    finalScore.setText(getString(R.string.gameOverTxt) + score);
                    savename.edit().putString("score", String.valueOf(score)).commit();
                    Toast.makeText(this, getString(R.string.closeGameTxt), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() ->{
                        finish();
                    }, 2000);

                } else {
                    count[0]++;
                }
                break;
            case R.id.Red:
                guess.add(2);
                id = sp.play((int) (3), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                if (pattern.equals(guess)) {
                    count[0] = 0;
                    guess.clear();
                    score++;
                    showPattern(colours, score);
                } else if (pattern.get(count[0]) != 2) {
                    finalScore.setText(getString(R.string.gameOverTxt) + score);
                    savename.edit().putString("score", String.valueOf(score)).commit();
                    Toast.makeText(this, getString(R.string.closeGameTxt), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() ->{
                        finish();
                    }, 2000);

                } else {
                    count[0]++;
                }
                break;
            case R.id.Purple:
                guess.add(3);
                id = sp.play((int) (4), 1.0f, 1.0f, 1, (int) (0), 1.0f);

                if (pattern.equals(guess)) {
                    count[0] = 0;
                    guess.clear();
                    score++;
                    showPattern(colours, score);
                } else if (pattern.get(count[0]) != 3) {
                    finalScore.setText(getString(R.string.gameOverTxt) + score);
                    savename.edit().putString("score", String.valueOf(score)).commit();
                    Toast.makeText(this, getString(R.string.closeGameTxt), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() ->{
                        finish();
                    }, 2000);

                } else {
                    count[0]++;
                }
                break;
        }

    }
}
