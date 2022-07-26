package com.android.simonsays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class LeaderboardActivity extends AppCompatActivity {

    private String fontName = "";

    private ArrayList<HashMap<String, Object>> users = new ArrayList<>();

    private ListView listview1;
    private TextView textview1;

    private SharedPreferences savename;

    /**
     * On create function, calls initialize functions
     */
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.leaderboard);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    /**
     * Initialize leaderboard's ListView by receiving the object at runtime
     * Initialize the user shared preferences
     */
    private void initialize(Bundle _savedInstanceState) {
        listview1 = (ListView) findViewById(R.id.listview1);

        savename = getSharedPreferences("savename", Activity.MODE_PRIVATE);
    }

    /**
     * In this function we initialize the leaderboard logic, we get all the names from user shared preferences
     */
    private void initializeLogic() {
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("name", savename.getString("name", ""));
            users.add(_item);

        }

        listview1.setAdapter(new Listview1Adapter(users));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
        _changeActivityFont("indie_flower");
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
    public void _changeActivityFont(final String _fontname) {
        fontName = "fonts/".concat(_fontname.concat(".ttf"));
        overrideFonts(this, getWindow().getDecorView());
    }
    /**
     * Implements the fonts
     */
    private void overrideFonts(final Context context, final View v) {

        try {
            Typeface
                    typeFace = Typeface.createFromAsset(getAssets(), fontName);
            if ((v instanceof ViewGroup)) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0;
                     i < vg.getChildCount();
                     i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else {
                if ((v instanceof TextView)) {
                    ((TextView) v).setTypeface(typeFace);
                } else {
                    if ((v instanceof EditText)) {
                        ((EditText) v).setTypeface(typeFace);
                    } else {
                        if ((v instanceof Button)) {
                            ((Button) v).setTypeface(typeFace);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Util.showMessage(getApplicationContext(), "Error Loading Font");
        }
        ;
    }

    /**
     * This function builds the leaderboard views
     */
    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.learderboardlist, null);
            }

            final TextView txtName = (TextView) _view.findViewById(R.id.txtName);
            final TextView textview3 = (TextView) _view.findViewById(R.id.textview3);
            txtName.setText(users.get((int) _position).get("name").toString());
            Integer currScore = Integer.parseInt(textview3.getText().toString());
            Integer newScore = Integer.parseInt(savename.getString("score", ""));
            if(currScore < newScore) {
                textview3.setText(savename.getString("score", ""));
            }
            txtName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/indie_flower.ttf"), Typeface.NORMAL);
            return _view;
        }
    }

}
