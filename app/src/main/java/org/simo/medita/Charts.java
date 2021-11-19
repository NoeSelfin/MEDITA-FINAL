package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class Charts  extends Activity {
    protected SharedPreferences prefs;
    protected Typeface font;
    protected SlidingMenu menu_lateral;
    protected ImageView menu;
    protected BarChart barchart1;
    protected BarChart barchart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_charts);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        prefs = getSharedPreferences(getString(R.string.sharedpref_name), Context.MODE_PRIVATE);

        barchart1 = findViewById(R.id.barchart1);
        barchart2 = findViewById(R.id.barchart2);

        showBarChart1();
        showBarChart2();
    }
    private void showBarChart1(){
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Title";

        //input data
        for(int i = 0; i < 6; i++){
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barchart1.setData(data);
        barchart1.invalidate();
    }
    private void showBarChart2(){
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Title";

        //input data
        for(int i = 0; i < 6; i++){
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barchart2.setData(data);
        barchart2.invalidate();
    }
}
