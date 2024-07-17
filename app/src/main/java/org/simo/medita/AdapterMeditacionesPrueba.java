package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.FilterData;
import android.graphics.BlurMaskFilter;

public class AdapterMeditacionesPrueba extends BaseAdapter {
    private Activity activity;
    private JSONArray data;
    private LayoutInflater inflater = null;
    protected Typeface font;
    AssetManager assetManager;
    JSONObject pack;

    public AdapterMeditacionesPrueba(Activity a, JSONArray d, JSONObject pack) {
        activity = a;
        data = d;
        this.pack = pack;

        Log.d("pruebaLog", " " + this.pack);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
    }

    public class Holder {
        ImageView img;
        ImageView download_img;
        TextView nombre;
        TextView desc;
        LinearLayout corta;
        LinearLayout media;
        LinearLayout larga;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View listView = convertView;
        if (convertView == null) {
            listView = inflater.inflate(R.layout.row_meditaciones, null);
            holder.img = listView.findViewById(R.id.id_row_med_img);
            holder.download_img = listView.findViewById(R.id.id_row_med_img_downloaded);
            holder.nombre = listView.findViewById(R.id.id_row_med_nombre);
            holder.desc = listView.findViewById(R.id.id_row_med_description);
            listView.setTag(holder);
        } else {
            holder = (Holder) listView.getTag();
        }

        try {
            if (pack.optInt("continuo") == 1) {
                holder.desc.setText("");
                int aux = Integer.parseInt(data.getJSONObject(position).getString("med_dia"));
                if (aux == 0) {
                    holder.nombre.setText(data.getJSONObject(position).getString("med_titulo").trim());
                } else {
                    holder.nombre.setText("DÍA " + data.getJSONObject(position).getString("med_dia") + " - " + data.getJSONObject(position).getString("med_titulo").trim());
                }

                if (new FilterData().isPrevCompleted(data, data.getJSONObject(position))) {
                    holder.nombre.setTextColor(Color.parseColor("#0c465e"));
                } else {
                    holder.nombre.setTextColor(Color.parseColor("#bbbaba"));
                }
            } else {
                holder.nombre.setText(data.getJSONObject(position).getString("med_titulo").trim() + " - Prueba");
                holder.nombre.setTextColor(Color.parseColor("#0c465e"));
            }
            holder.nombre.setTypeface(font);

            if (position >= 2) {
                float radius = 15f;
                BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                holder.nombre.getPaint().setMaskFilter(filter);
                holder.img.setVisibility(View.GONE);
                holder.download_img.setVisibility(View.VISIBLE);

                // Ajustar el tamaño del icono de candado
                ViewGroup.LayoutParams params = holder.download_img.getLayoutParams();
                params.width = (int) (40 * activity.getResources().getDisplayMetrics().density);
                params.height = (int) (40 * activity.getResources().getDisplayMetrics().density);
                holder.download_img.setLayoutParams(params);
                holder.download_img.setColorFilter(Color.parseColor("#FFC107")); // Color amarillo

                listView.setClickable(true);
                listView.setFocusable(true);
            } else {
                holder.nombre.getPaint().setMaskFilter(null);
                holder.img.setVisibility(View.VISIBLE);
                holder.download_img.setVisibility(View.GONE);

                // Ajustar el tamaño del icono de reproducción
                ViewGroup.LayoutParams params = holder.img.getLayoutParams();
                params.width = (int) (20 * activity.getResources().getDisplayMetrics().density);
                params.height = (int) (20 * activity.getResources().getDisplayMetrics().density);
                holder.img.setLayoutParams(params);
                holder.img.setColorFilter(Color.parseColor("#FFC107")); // Color amarillo

                listView.setClickable(false);
                listView.setFocusable(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
