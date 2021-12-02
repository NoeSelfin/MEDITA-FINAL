package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;

public class AdapterTotals extends BaseAdapter{
    private Activity activity;
    private JSONArray data;
    private LayoutInflater inflater=null;
    protected Typeface font;
    AssetManager assetManager;

    public AdapterTotals(Activity a, JSONArray d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
    }

    public class Holder
    {
        TextView desc;
        TextView value;
        TextView units;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View listView = convertView;
        if (convertView == null) {
            listView = new View(activity);
            listView = inflater.inflate(R.layout.row_charts_totals, null);
            holder.desc =(TextView)listView.findViewById(R.id.id_row_charts_totals1);
            holder.value =(TextView)listView.findViewById(R.id.id_row_charts_totals2);
            holder.units =(TextView)listView.findViewById(R.id.id_row_charts_totals3);
            listView.setTag(holder);

        }
        else {
            holder = (Holder) listView.getTag();
        }

        holder.desc.setText(data.optJSONObject(position).optString("title",""));
        holder.value.setText(data.optJSONObject(position).optString("value",""));
        holder.units.setText(data.optJSONObject(position).optString("units",""));


        holder.value.setTypeface(font);
        holder.desc.setTypeface(font);
        holder.units.setTypeface(font);


        return listView;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
