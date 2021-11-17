package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class AdapterMeditacionesHome extends BaseAdapter{
    private Activity activity;
    private JSONArray data;
    private LayoutInflater inflater=null;
    protected Typeface font;
    AssetManager assetManager;

    public AdapterMeditacionesHome(Activity a, JSONArray d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
    }

    public class Holder
    {
        ImageView img;
        TextView nombre;
        TextView desc;
        LinearLayout corta;
        LinearLayout media;
        LinearLayout larga;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View listView = convertView;
        if (convertView == null) {
            listView = new View(activity);
            listView = inflater.inflate(R.layout.row_meditaciones, null);
            holder.img =(ImageView)listView.findViewById(R.id.id_row_med_img);
            holder.nombre =(TextView)listView.findViewById(R.id.id_row_med_nombre);
            holder.desc =(TextView)listView.findViewById(R.id.id_row_med_description);
            listView.setTag(holder);
        }
        else {
            holder = (Holder) listView.getTag();
        }

        try {

            int aux = Integer.valueOf(data.getJSONObject(position).getString("med_dia"));

            if (aux == 0){
                holder.nombre.setText(data.getJSONObject(position).getString("med_titulo").trim());
                holder.desc.setText("");
            }
            else{
                holder.nombre.setText(data.getJSONObject(position).getString("med_titulo").trim());
                holder.desc.setText(data.getJSONObject(position).optString("med_desc").trim());
            }

            holder.nombre.setTextColor(Color.parseColor("#0c465e"));
            holder.desc.setTextColor(Color.parseColor("#0c465e"));


            holder.nombre.setTypeface(font);
            holder.desc.setTypeface(font);

        } catch (JSONException e) {
        }


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
