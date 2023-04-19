package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AdapterMeditacionesDuracionDownloads extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater=null;
    protected Typeface font;
    protected ArrayList<String> durs;
    AssetManager assetManager;
    protected boolean isPresentation = false;
    protected JSONObject meditacion;
    MeditationFunctions med_funcs;

    public AdapterMeditacionesDuracionDownloads(Activity a, ArrayList<String> durs, boolean isPresentation, JSONObject meditacion) {
        activity = a;
        this.durs = durs;
        this.isPresentation = isPresentation;
        this.meditacion = meditacion;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
        med_funcs = new MeditationFunctions(activity.getBaseContext());
    }

    public class Holder
    {
        ImageView img;
        TextView nombre;
        ImageView download_img;
        ImageView delete_img;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View listView = convertView;
        if (convertView == null) {
            listView = new View(activity);
            listView = inflater.inflate(R.layout.row_meditaciones_downloaded, null);
            holder.img =(ImageView)listView.findViewById(R.id.id_row_med_img);
            holder.nombre =(TextView)listView.findViewById(R.id.id_row_med_nombre);
            holder.download_img =(ImageView)listView.findViewById(R.id.id_row_med_img_downloaded);
            holder.delete_img =(ImageView)listView.findViewById(R.id.id_row_med_img_downloaded_deleted);
            listView.setTag(holder);

        }
        else {
            holder = (Holder) listView.getTag();
        }

        int duracion = Integer.valueOf(durs.get(position));
        String dur ="";
        if (duracion < 5)
            dur = "Corta "+ durs.get(position) + " MIN";
        if ((duracion >= 5) && (duracion <= 9))
            dur = "Media "+ durs.get(position) + " MIN";
        if (duracion > 9)
            dur = "Larga "+ durs.get(position) + " MIN";

        if (isPresentation)
            dur = "Presentación";


        holder.nombre.setText(dur);
        holder.nombre.setTypeface(font);

        String song =meditacion.optString("med_fichero") + "M"+durs.get(position)+".mp3";
        ContextWrapper cw = new ContextWrapper(activity.getBaseContext());
        File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
        final File file=new File(directory,song);


        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(file.exists())   {
                    file.delete();
                }

                med_funcs.deleteMeditationDownload(meditacion.optString("id_meditacion"));

                Toast.makeText(activity.getBaseContext(),"Meditación borrada de descargas.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(activity.getBaseContext(), Home.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getBaseContext().startActivity(i);
                activity.finish();

            }
        });

        return listView;
    }

    @Override
    public int getCount() {
        return durs.size();
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

