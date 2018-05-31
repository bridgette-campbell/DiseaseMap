package edu.uw.tacoma.css.diseasemap;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorsActivity extends AppCompatActivity {
    private static final String TAG = "ColorsActivity";

    /**
     * SharedPreferences keys for the user-selected colors
     */
    public static final String SELECTED_COOL_COLOR = "selected_cool_color";
    public static final String SELECTED_WARM_COLOR = "selected_warm_color";

    /*
     * User-selected colors
     */
    private String mCoolColor;
    private String mWarmColor;

    private Map<String, Integer> mCoolMap = new HashMap<>();
    private Map<String, Integer> mWarmMap = new HashMap<>();
    {
        mCoolMap.put("Green", 0x00cd00);
        mCoolMap.put("Blue-Green", 0x00868b);
        mCoolMap.put("Blue", 0x0000ff);
        mCoolMap.put("Blue-Violet", 0x483d8b);
        mCoolMap.put("Violet", 0x7a378b);
        mCoolMap.put("Violet-Red", 0xc71585);

        mWarmMap.put("Yellow-Green", 0xadff2f);
        mWarmMap.put("Yellow", 0xffff00);
        mWarmMap.put("Orange-Yellow", 0xffa500);
        mWarmMap.put("Orange", 0xee7600);
        mWarmMap.put("Red-Orange", 0xee4000);
        mWarmMap.put("Red", 0xff0000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        final List<String> coolList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cool_colors_array)));

        // Cool Spinner
        final Spinner coolSpinner = findViewById(R.id.cool_spinner);
        ArrayAdapter<CharSequence> coolAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, coolList) {
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                int color = Color.WHITE;
                switch (position) {
                    case 0:
                        color = getResources().getColor(R.color.green);
                        break;
                    case 1:
                        color = getResources().getColor(R.color.blue_green);
                        break;
                    case 2:
                        color = getResources().getColor(R.color.blue);
                        break;
                    case 3:
                        color = getResources().getColor(R.color.blue_violet);
                        break;
                    case 4:
                        color = getResources().getColor(R.color.violet);
                        break;
                    case 5:
                        color = getResources().getColor(R.color.violet_red);
                        break;
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
        int savedColor = getSharedPreferences("com.uw.diseasemaps", Context.MODE_PRIVATE)
                .getInt(SELECTED_COOL_COLOR, 0x00cd00);
        String selectedName = coolList.get(0);
        for (String name : mCoolMap.keySet()) {
            if (mCoolMap.get(name) == (savedColor)){
                selectedName = name;
            }
        }
        coolSpinner.setSelection(coolAdapter.getPosition(selectedName));


        // Warm Spinner
        final Spinner warmSpinner = findViewById(R.id.warm_spinner);
        final List<String> warmList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.warm_colors_array)));
        ArrayAdapter<CharSequence> warmAdapter = new ArrayAdapter(this.getBaseContext(), android.R.layout.simple_dropdown_item_1line, warmList) {
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                int color = Color.WHITE;
                for (String name : warmList) {
                    switch (position) {
                        case 5:
                            color = getResources().getColor(R.color.yellow_green);
                            break;
                        case 4:
                            color = getResources().getColor(R.color.yellow);
                            break;
                        case 3:
                            color = getResources().getColor(R.color.orange_yellow);
                            break;
                        case 2:
                            color = getResources().getColor(R.color.orange);
                            break;
                        case 1:
                            color = getResources().getColor(R.color.red_orange);
                            break;
                        case 0:
                            color = getResources().getColor(R.color.red);
                            break;
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
        savedColor = getSharedPreferences("com.uw.diseasemaps", Context.MODE_PRIVATE)
                .getInt(SELECTED_WARM_COLOR, 0xff0000);
        selectedName = warmList.get(0);
        for (String name : mWarmMap.keySet()) {
            if (mWarmMap.get(name) == (savedColor)){
                selectedName = name;
            }
        }
        warmSpinner.setSelection(warmAdapter.getPosition(selectedName));

        Button confirm = findViewById(R.id.btn_colors_selected);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSharedPreferences("com.uw.diseasemaps", Context.MODE_PRIVATE)
                        .edit()
                        .putInt(ColorsActivity.SELECTED_COOL_COLOR, mCoolMap.get(mCoolColor))
                        .apply();

                Log.i(TAG, "Cool color (" + mCoolColor + ") selection saved");

                getSharedPreferences("com.uw.diseasemaps", Context.MODE_PRIVATE)
                        .edit()
                        .putInt(ColorsActivity.SELECTED_WARM_COLOR, mWarmMap.get(mWarmColor))
                        .apply();

                Log.i(TAG, "Warm color (" + mWarmColor + ") selection saved");

                finish();
            }
        });
    }
}
