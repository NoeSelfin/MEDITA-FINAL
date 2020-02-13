package org.simo.medita;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igna on 05/03/2019.
 */

class AdapterSuscripciones extends BaseAdapter {

    Context context;
    JSONArray data;
    private static LayoutInflater inflater = null;
    Typeface font;

    public AdapterSuscripciones(Context context, JSONArray data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        font = Typeface.createFromAsset(context.getAssets(), "tipo/Dosis-Regular.otf");
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length();
    }

    @Override
    public JSONObject getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return data.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_suscripciones, null);
        TextView product = (TextView) vi.findViewById(R.id.sku_product_sus);
        try {
//            Log.i("test_medita", "AdapterSuscripciones item position " + position + " -> " + data.getJSONObject(position));
            product.setText("SUSCRIPCIÓN " + data.getJSONObject(position).getString("product").toUpperCase() +" "+data.getJSONObject(position).getString("price"));
            product.setTypeface(font);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vi;
    }
}
