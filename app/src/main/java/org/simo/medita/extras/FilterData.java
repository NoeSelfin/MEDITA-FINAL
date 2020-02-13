package org.simo.medita.extras;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class FilterData {
	
	
	public FilterData(){	
	}
			
	public JSONArray filterMeditaciones(JSONArray input, String id_pack){
		 JSONArray output = new JSONArray();
		 
		 for (int i=0; i<input.length();i++){
			 if (input.optJSONObject(i).optString("id_pack").compareToIgnoreCase(id_pack) == 0)
				 output.put(input.optJSONObject(i));
		 }
		 
		 return output;
	 }
	
	public JSONObject getPack(JSONArray input, String id_meditacion){
		 JSONObject output = new JSONObject();
		 
		 for (int i=0; i<input.length();i++){
			 for (int j=0; j<input.optJSONObject(i).optJSONArray("meditaciones").length();j++){
				 if (input.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("id_meditacion").compareTo(id_meditacion) == 0)
					 output = input.optJSONObject(i).optJSONObject("pack");
			 }			
		 }
		 
		 return output;
	 }
	

	public JSONObject getPackSimple(JSONArray packs, String id_pack){
		 JSONObject output = new JSONObject();
		 
		 for (int i=0; i<packs.length();i++){
			 if (packs.optJSONObject(i).optString("id_pack").compareTo(id_pack) == 0)
				 output = packs.optJSONObject(i);
			 			
		 }
		 
		 return output;
	 }
	
	public boolean isLast(JSONArray meditaciones, String id_meditacion){
		boolean res = false;
		int pos = 0;
		for(int i=0;i<meditaciones.length();i++){
			if (meditaciones.optJSONObject(i).optString("id_meditacion").compareTo(id_meditacion) == 0)
				pos = i;			
		}
		
		Log.i("medita_isLast", String.valueOf(meditaciones.length()));
		Log.i("medita_isLast", String.valueOf(pos));
		
		if (pos == meditaciones.length()-1)
			return true;
		else
			return false;
		
	}
	public boolean isFirst(JSONArray meditaciones, String id_meditacion){
		boolean res = false;
		int pos = 0;
		for(int i=0;i<meditaciones.length();i++){
			if (meditaciones.optJSONObject(i).optString("id_meditacion").compareTo(id_meditacion) == 0)
				pos = i;			
		}
		
		if (pos == 0)
			return true;
		else
			return false;
		
	}
	
	public JSONObject nextMed(JSONArray input, String id_meditacion){
		 JSONObject output = new JSONObject();
		 int pos = 0;
		 
		 for (int i=0; i<input.length();i++){
			 if (input.optJSONObject(i).optString("id_meditacion").compareTo(id_meditacion) == 0)
				 pos = i;
		 }
		 
		 
		 if (pos == (input.length() - 1)){
			 return null;
		 }
		 else{
			 pos = pos + 1;
			 if (pos <= input.length()){
				 output =  input.optJSONObject(pos);
				 return output;
			 }
			 else
				 return null;
		 }		 
		
	 }
	public JSONObject prevMed(JSONArray input, String id_meditacion){
		JSONObject output = new JSONObject();
		 int pos = 0;
		 
		 for (int i=0; i<input.length();i++){
			 if (input.optJSONObject(i).optString("id_meditacion").compareTo(id_meditacion) == 0)
				 pos = i;
		 }
		 
		 pos = pos - 1;
		 if (pos >= 0)
			 output =  input.optJSONObject(pos);			 
		 
		 return output;
	 }
	public JSONArray prepareFavoritos(JSONArray input){
		 JSONArray output = new JSONArray();
		 JSONArray meditaciones = new JSONArray();
		 JSONObject pack = new JSONObject();
		 
		 for (int i=0; i<input.length();i++){
			 meditaciones = input.optJSONObject(i).optJSONArray("meditaciones");
			 pack = input.optJSONObject(i).optJSONObject("pack");
			 try {
				pack.put("tipo", "pack");
				output.put(pack);
				for (int j=0; j<meditaciones.length();j++){
					 output.put(meditaciones.optJSONObject(j));
				 }
			} catch (JSONException e) {
			}
			 
			 			
		 }		 
		 return output;
	 }
	public boolean isFavorito(JSONArray favoritos, JSONObject meditacion){
		boolean response = false;
		 JSONArray meditaciones = new JSONArray();
		 
		 for (int i=0; i<favoritos.length();i++){
			 meditaciones = favoritos.optJSONObject(i).optJSONArray("meditaciones");
			 for (int j=0; j<meditaciones.length();j++){
				 if (meditaciones.optJSONObject(j).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) == 0)
					 response = true;
			 }			
		 }		 
		 return response;
	 }
	public boolean isFavoritoDur(JSONArray favoritos, JSONObject meditacion, String duracion){
		boolean response = false;
		 JSONArray meditaciones = new JSONArray();
		 Log.i("medita_isFav",favoritos.toString());
		 Log.i("medita_isFav",duracion);
		 
		 for (int i=0; i<favoritos.length();i++){
			 meditaciones = favoritos.optJSONObject(i).optJSONArray("meditaciones");
			 for (int j=0; j<meditaciones.length();j++){
				 if ((meditaciones.optJSONObject(j).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) == 0) &&
				 (duracion.compareTo(meditaciones.optJSONObject(j).optString("duracion")) == 0)){
					 response = true;
				 }
			 }			
		 }		 
		 return response;
	 }
	public JSONArray setFavorito(JSONArray favoritos, JSONObject meditacion, JSONObject pack, String dur){
		String id_pack = pack.optString("id_pack");
		boolean pack_finded = false;
		 		
		for (int i=0; i<favoritos.length();i++){
			 if (favoritos.optJSONObject(i).optJSONObject("pack").optString("id_pack").compareTo(id_pack) == 0 ){
				 pack_finded = true;
				 try {
					 meditacion.put("duracion", dur);
					 favoritos.optJSONObject(i).optJSONArray("meditaciones").put(meditacion);

				} catch (JSONException e) {
				}				 
				 
			 }
		
		 }	
		
		if (!pack_finded){
			//nuevo pack y meditacion a insertar			
			
			JSONArray meditaciones = new JSONArray();
			meditaciones.put(meditacion);
			JSONObject packs_fav = new JSONObject();
			try {
				packs_fav.put("pack", pack);
				packs_fav.put("meditaciones", meditaciones);				
				favoritos.put(packs_fav); 
			} catch (JSONException e) {
			}
			
		}
		
		 return favoritos;
	 }
	public JSONArray setFavoritoDur(JSONArray favoritos, JSONObject meditacion, JSONObject pack, String dur){
		String id_pack = pack.optString("id_pack");
		boolean pack_finded = false;
		 		
		for (int i=0; i<favoritos.length();i++){
			 if (favoritos.optJSONObject(i).optJSONObject("pack").optString("id_pack").compareTo(id_pack) == 0 ){
				 pack_finded = true;
				 try {
					 meditacion.put("duracion", dur);
					 favoritos.optJSONObject(i).optJSONArray("meditaciones").put(meditacion);

				} catch (JSONException e) {
				}				 
				 
			 }
		
		 }	
		
		if (!pack_finded){
			//nuevo pack y meditacion a insertar		
			try {				
				JSONArray meditaciones = new JSONArray();
				meditacion.put("duracion", dur);
				meditaciones.put(meditacion);
				JSONObject packs_fav = new JSONObject();
				packs_fav.put("pack", pack);
				packs_fav.put("meditaciones", meditaciones);				
				favoritos.put(packs_fav); 
			} catch (JSONException e) {
			}
			
		}
		
		 return favoritos;
	 }
	
	public JSONArray unSetFavoritoDur(JSONArray favoritos, JSONObject meditacion, String dur){
		JSONArray favoritos_aux = new JSONArray();
		JSONObject aux = new JSONObject();
		JSONArray meditaciones = new JSONArray();
		int contador = 0;
				 		
		for (int i=0; i<favoritos.length();i++){
			 aux = new JSONObject();
			 meditaciones = new JSONArray();
			 contador = 0;
			 try {
				// Log.i("medita_filter",  favoritos.optJSONObject(i).optJSONObject("pack").toString());
				 for (int j=0; j<favoritos.optJSONObject(i).optJSONArray("meditaciones").length();j++){
					 
					// Log.i("medita_filter",  favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("id_meditacion"));
					 //Log.i("medita_filter",  meditacion.optString("id_meditacion"));
					 if (favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) != 0){
						 meditaciones.put(favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j));
						 contador ++;
					 }
					 else{
						 if (dur.compareTo(favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("duracion")) != 0){
							 meditaciones.put(favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j));
							 contador ++;
							 
						 }					 
					 }
				 }
				 
				 if (contador > 0){
					 aux.put("pack", favoritos.optJSONObject(i).optJSONObject("pack"));
					 aux.put("meditaciones", meditaciones);
					 favoritos_aux.put(aux);
				 }
				 
				 
			} catch (JSONException e1) {
				Log.i("medita_filter", "Error unSetFavoritos");
			}
		
			 
		 }	
		
		 return favoritos_aux;
	 }
	public JSONArray unSetFavorito(JSONArray favoritos, JSONObject meditacion){
		JSONArray favoritos_aux = new JSONArray();
		JSONObject aux = new JSONObject();
		JSONArray meditaciones = new JSONArray();
		int contador = 0;
		 		
		for (int i=0; i<favoritos.length();i++){
			 aux = new JSONObject();
			 meditaciones = new JSONArray();
			 contador = 0;
			 try {
				// Log.i("medita_filter",  favoritos.optJSONObject(i).optJSONObject("pack").toString());
				 for (int j=0; j<favoritos.optJSONObject(i).optJSONArray("meditaciones").length();j++){
					 
					// Log.i("medita_filter",  favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("id_meditacion"));
					 //Log.i("medita_filter",  meditacion.optString("id_meditacion"));
					 if (favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) != 0){
						 meditaciones.put(favoritos.optJSONObject(i).optJSONArray("meditaciones").optJSONObject(j));
						 contador ++;
					 }
				 }
				 
				 if (contador > 0){
					 aux.put("pack", favoritos.optJSONObject(i).optJSONObject("pack"));
					 aux.put("meditaciones", meditaciones);
					 favoritos_aux.put(aux);
				 }
				 
				 
			} catch (JSONException e1) {
				Log.i("medita_filter", "Error unSetFavoritos");
			}
		
			 
		 }	
		
		 return favoritos_aux;
	 }
	
	public JSONArray setCompleted(JSONArray meditaciones, JSONObject meditacion){
		 JSONArray ja = meditaciones;
		 for (int i=0; i<ja.length();i++){
			 if (ja.optJSONObject(i).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) == 0){
				 try {
					ja.optJSONObject(i).put("isCompleted", true);
				} catch (JSONException e) {
				}				 
			 }
		 }	
		 
		 
		 return ja;
	 }
	
	public boolean isPrevCompleted(JSONArray meditaciones, JSONObject meditacion){
		int marca = 0;
		 for (int i=0; i<meditaciones.length();i++){
			 if (meditaciones.optJSONObject(i).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) == 0){
				 marca = i;			 
			 }
		 }	
		 
		 if (marca == 0)
			 return true;
		 else{
			 if (meditaciones.optJSONObject(marca - 1).optBoolean("isCompleted",false))
				 return true;
			 else
				 return false;
		 }
		 
		 
	 }
	
	public boolean isCompleted(JSONArray meditaciones, JSONObject meditacion){
		 for (int i=0; i<meditaciones.length();i++){
			 if (meditaciones.optJSONObject(i).optString("id_meditacion").compareTo(meditacion.optString("id_meditacion")) == 0){
				if (meditaciones.optJSONObject(i).optBoolean("isCompleted",false))	 
					return true;
			 }
		 }	
		 
		return false;
		 
		 
	 }


}
