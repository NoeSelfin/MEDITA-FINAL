package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AdapterMeditacionesDuracion extends BaseAdapter{	
	private Activity activity;
	private LayoutInflater inflater=null;	    
	protected Typeface font;
	protected ArrayList<String> durs;
	AssetManager assetManager;
	protected boolean isPresentation = false;
	protected JSONObject meditacion;

	public AdapterMeditacionesDuracion(Activity a, ArrayList<String> durs, boolean isPresentation, JSONObject meditacion) {
		 activity = a;
		 this.durs = durs;
		 this.isPresentation = isPresentation;
		 this.meditacion = meditacion;
	     inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
	}

	 public class Holder
	    {
		 	ImageView img;
	        TextView nombre;
			ImageView download_img;
	    }

	public View getView(int position, View convertView, ViewGroup parent) { 
		Holder holder=new Holder();
		View listView = convertView; 
		if (convertView == null) {
			listView = new View(activity); 		
			listView = inflater.inflate(R.layout.row_meditaciones, null);	
			holder.img =(ImageView)listView.findViewById(R.id.id_row_med_img);
			holder.nombre =(TextView)listView.findViewById(R.id.id_row_med_nombre);
			holder.download_img =(ImageView)listView.findViewById(R.id.id_row_med_img_downloaded);
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
		File file=new File(directory,song);
		if(file.exists())   {
			holder.download_img.setVisibility(View.VISIBLE);
		}else{
			holder.download_img.setVisibility(View.GONE);
		}
				
	
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

