package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
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
import org.simo.medita.extras.Basics;


public class AdapterPacks extends BaseAdapter {
	private SharedPreferences prefs;
	private Activity activity;
    private JSONArray data;
    private LayoutInflater inflater=null;	    
    protected Typeface font;
    AssetManager assetManager;
 
	public AdapterPacks(Activity a, JSONArray d) {
		 activity = a;
         data = d;
         inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 font = Typeface.createFromAsset(activity.getAssets(), "tipo/Dosis-Regular.otf");
//		 prefs = a.getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = a.getSharedPreferences(a.getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
	}
	
	 public class Holder
	    {
		 	ImageView img;
	        TextView nombre;
	        TextView nuevo;
	        TextView precio;
	        TextView prox;
	        LinearLayout contenedor;
	        
	    }
 
	public View getView(int position, View convertView, ViewGroup parent) { 
		Holder holder=new Holder();
		View listView = convertView; 
		
		if (convertView == null) {
			listView = new View(activity); 
			listView = inflater.inflate(R.layout.row_mainmenu, null);		
			
			holder.img =(ImageView)listView.findViewById(R.id.id_row_main_img);
			holder.nombre =(TextView)listView.findViewById(R.id.id_row_main_nombre);
			holder.nuevo =(TextView)listView.findViewById(R.id.id_row_main_nuevo);
			holder.precio =(TextView)listView.findViewById(R.id.id_row_main_precio);
			holder.prox =(TextView)listView.findViewById(R.id.id_row_main_proximamente);
			holder.contenedor = (LinearLayout)listView.findViewById(R.id.id_row_main_bg);
			    
			listView.setTag(holder);
 
		} else {
			holder = (Holder) listView.getTag();
		}
		
		
		  try {
	        	holder.nombre.setText("· "+data.getJSONObject(position).getString("pack_titulo")+" ·");
	        	holder.nombre.setTypeface(font);    	
	        	holder.prox.setTypeface(font); 
	        	holder.contenedor.setBackgroundColor(Color.parseColor(data.getJSONObject(position).getString("pack_color").trim()));
	        	
	        	if (data.getJSONObject(position).getString("proximamente").compareToIgnoreCase("1") == 0){
	        		holder.prox.setVisibility(View.VISIBLE);
	        		holder.precio.setVisibility(View.GONE);
	        		holder.nuevo.setVisibility(View.GONE);
	        	}
	        	
	        	else{
	        		holder.prox.setVisibility(View.GONE);

		        	if (data.getJSONObject(position).getString("pack_precio").compareToIgnoreCase("0") != 0){	   
		        		
		        		if (!checkSuscrito()){
//		        		if (checkSuscrito()){
		        			holder.precio.setText(data.getJSONObject(position).getString("pack_precio")+ Html.fromHtml("&#8364") );
		        			holder.precio.setText(activity.getString(R.string.suscribirse));
			        		holder.precio.setTypeface(font);
			        		// si no esta suscrito comprobamos las compras realizadas antes de añadir suscripciones
			        		 if(checkComprado(String.valueOf(data.getJSONObject(position).getInt("id_pack")))){
			        		 	Log.i("medita_compras", "tiene comprado el pack -> " + String.valueOf(data.getJSONObject(position).getInt("id_pack")));
								 holder.precio.setVisibility(View.GONE);
							}else{
								 holder.precio.setVisibility(View.VISIBLE);
							 }
		        		} else {
		        			holder.precio.setVisibility(View.GONE);
		        		}
		        		
		        	}
		        	else{
		        		holder.precio.setVisibility(View.GONE);
		        	}
		         
		        	
		        	if (data.getJSONObject(position).getString("pack_nuevo").compareToIgnoreCase("1") == 0){
						//		        		holder.nuevo.setVisibility(View.VISIBLE);
					}else{
						holder.nuevo.setVisibility(View.GONE);
					}

		        	holder.nuevo.setTypeface(font);   
	        	}
	        	
	        	
	        	        	
	  		    //BitmapFactory.Options options = new BitmapFactory.Options();
	  	        // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	  	        //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Medita/"+data.getJSONObject(position).getString("pack_icono"), options);
	  	        Bitmap bitmap = Basics.readFileInternal(activity,"iconos",data.getJSONObject(position).getString("pack_icono"));
	  			
	  	       if (bitmap!=null){ 
	  	    	 holder.img.setImageBitmap(bitmap);
	  	       }
	  	      
	  	    
	        	
	       
	        		        	
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
	
//  protected boolean checkComprado(String id_pack) throws JSONException {
    protected boolean checkSuscrito() throws JSONException {
		// prefs.edit().putString(getString(R.string.skus_activos), jsonArray.toString()).commit();
		if (prefs.getBoolean(activity.getResources().getString(R.string.suscrito), false)){
            return true;
		}
		return false;
    }

    protected boolean checkComprado(String id_pack){
        if (prefs.contains("comprado_"+id_pack)){
            if (prefs.getBoolean("comprado_"+id_pack, false)){
                return true;
            }
            return false;
        }
        return false;
    }

}