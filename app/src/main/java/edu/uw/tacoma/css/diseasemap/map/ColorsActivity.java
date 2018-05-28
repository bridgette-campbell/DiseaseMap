package edu.uw.tacoma.css.diseasemap.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.tacoma.css.diseasemap.R;

public class ColorsActivity extends AppCompatActivity {//implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "ColorsActivity";

    /**
     * SharedPreferences keys for the user-selected colors
     */
    public static final String SELECTED_COOL_COLOR = "selected_cool_color";
    public static final String SELECTED_WARM_COLOR = "selected_warm_color";

    /**
     * User-selected colors
     */
    private String mCoolColor;
    private String mWarmColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        final List<String> coolList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cool_colors_array)));

        final Spinner coolSpinner = (Spinner) findViewById(R.id.cool_spinner);
        ArrayAdapter<CharSequence> coolAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, coolList) {
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                int color = Color.WHITE;
                //This is very gross. I am sorry.
                if (position == 0) {
                    color = getResources().getColor(R.color.green);
                } else if (position == 1) {
                    color = getResources().getColor(R.color.blue_green);
                } else if (position == 2) {
                    color = getResources().getColor(R.color.blue);
                } else if (position == 3) {
                    color = getResources().getColor(R.color.blue_violet);
                } else if (position == 4) {
                    color = getResources().getColor(R.color.violet);
                } else if (position == 5) {
                    color = getResources().getColor(R.color.violet_red);
                }
                tv.setBackgroundColor(color);
                return view;
            }
        };
        coolAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        coolSpinner.setAdapter(coolAdapter);
        coolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCoolColor = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "Cool color selected: " + mCoolColor);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        coolSpinner.setSelection(coolAdapter.getPosition(coolList.get(0)));

        final List<String> warmList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.warm_colors_array)));

        final Spinner warmSpinner = (Spinner) findViewById(R.id.warm_spinner);
        ArrayAdapter<CharSequence> warmAdapter = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_dropdown_item_1line, warmList) {
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                int color = Color.WHITE;
                for (String name : warmList) {
                    //This is very gross. I am sorry.
                    if (position == 5) {
                        color = getResources().getColor(R.color.yellow_green);
                    } else if (position == 4) {
                        color = getResources().getColor(R.color.yellow);
                    } else if (position == 3) {
                        color = getResources().getColor(R.color.orange_yellow);
                    } else if (position == 2) {
                        color = getResources().getColor(R.color.orange);
                    } else if (position == 1) {
                        color = getResources().getColor(R.color.red_orange);
                    } else if (position == 0) {
                        color = getResources().getColor(R.color.red);
                    }
                }
                tv.setBackgroundColor(color);
                return view;
            }

        };
        warmAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        warmSpinner.setAdapter(warmAdapter);
        warmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWarmColor = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "Warm color selected: " + mWarmColor);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        warmSpinner.setSelection(warmAdapter.getPosition(warmList.get(0)));

        Button confirm = (Button) findViewById(R.id.btn_colors_selected);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences(ColorsActivity.SELECTED_COOL_COLOR, Context.MODE_PRIVATE)
                        .edit()
                        .putString(ColorsActivity.SELECTED_COOL_COLOR, mCoolColor)
                        .apply();

                Log.i(TAG, "Cool color ("+ mCoolColor +") selection saved");

                getSharedPreferences(ColorsActivity.SELECTED_WARM_COLOR, Context.MODE_PRIVATE)
                        .edit()
                        .putString(ColorsActivity.SELECTED_WARM_COLOR, mWarmColor)
                        .apply();

                Log.i(TAG, "Warm color ("+ mWarmColor +") selection saved");
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos);\
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
