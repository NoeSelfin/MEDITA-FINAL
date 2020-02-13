package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.simo.medita.extras.Basics;

public class AdapterFavoritos extends BaseAdapter{	
	private Activity activity;
	private JSONArray data;
	private LayoutInflater inflater=null;	    
	protected Typeface font;
	AssetManager assetManager;

	public AdapterFavoritos(Activity a, JSONArray d) {
		 activity = a;
	     data = d;
	     inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
	}

	public View getView(final int position, View convertView, ViewGroup parent) { 
		View listView = convertView; 
		
		listView = new View(activity); 	
		
		String tipo = data.optJSONObject(position).optString("tipo");
		
		if (tipo.compareTo("pack") == 0){
			listView = inflater.inflate(R.layout.row_favoritos_packs, null);	
			RelativeLayout bg =(RelativeLayout)listView.findViewById(R.id.id_fav_pack_bg);
			ImageView img =(ImageView)listView.findViewById(R.id.id_fav_pack_ico);
			TextView nombre =(TextView)listView.findViewById(R.id.id_fav_pack_titulo);	
			
			try {
	        	nombre.setText("· "+data.getJSONObject(position).getString("pack_titulo")+" ·");
	        	nombre.setTypeface(font);        
	        	
	        	 Bitmap bitmap = Basics.readFileInternal(activity,"iconos", data.optJSONObject(position).getString("pack_icono"));

		  			
		  	       if (bitmap!=null){ 
		  	    	 img.setImageBitmap(bitmap);
		  	       }
		  	       //bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Medita/"+pack.getString("pack_fondo_med"), options);
		  	       bitmap = Basics.readFileInternal(activity,"fondos",data.optJSONObject(position).getString("pack_fondo_med"));
		  	       
		  	       if (bitmap!=null){ 
		  	    	 bg.setBackground(new BitmapDrawable(activity.getResources(), bitmap));
		  	       }
		  	       else{
		  	    	 bg.setBackgroundColor(Color.parseColor(data.optJSONObject(position).getString("pack_color").trim()));
		  	       }
	        		        	
			} catch (JSONException e) {
			}
					
		}
		else{
			listView = inflater.inflate(R.layout.row_meditaciones, null);	
			TextView nombre =(TextView)listView.findViewById(R.id.id_row_med_nombre);
						
			try {
				int d = Integer.valueOf(data.getJSONObject(position).getString("duracion").trim());
				String dur = "";
				if (d < 5)
					dur = "DURACIÓN CORTA";

				if (d == 5)
					dur = "DURACIÓN MEDIA";

				if (d > 9)
					dur = "DURACIÓN LARGA";
				
	        	nombre.setText("DÍA " + data.getJSONObject(position).getString("med_dia")+ " - " + data.getJSONObject(position).getString("med_titulo").trim() +" - "+dur);
	        	nombre.setTypeface(font);        
	        		        	
			} catch (JSONException e) {
			}
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

