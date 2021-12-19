package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.HttpConnection;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Downloader {
	protected Context ctx;
	protected SharedPreferences prefs;
	protected AnimationDrawable loadingAnimation;	
	protected ImageView loading;
	protected RelativeLayout reproducir;
	protected AdapterPacks adapterpacks;
	protected ListView listview;
	protected JSONArray packs;
	protected TextView time_left;
	protected  MediaPlayer mp;
	protected boolean error = false;
	protected ImageView play;

	public Downloader(Context ctx, SharedPreferences prefs,ImageView loading, int type){
		this.ctx = ctx;
		this.prefs = prefs;
		this.loading = loading;
		
		if (type==1){
			loadingAnimation = new AnimationDrawable();
			loadingAnimation.setOneShot(false);
			loading.setBackground( new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00000.png")));	        
	        AddFrames af =  new AddFrames(ctx, loadingAnimation);
	        af.addLoadingFrames();
	        loadingAnimation = af.getAnimation();
	        loading.setBackground(loadingAnimation);
	        
		}
		else if (type == 0){
	        loadingAnimation = ((AnimationDrawable) loading.getBackground());
		}        
	}	

	
	public Downloader(Context ctx, SharedPreferences prefs){
		this.ctx = ctx;
		this.prefs = prefs;
		
	}

	public void downloadData(RelativeLayout reproducir,AdapterPacks adapterpacks,ListView listview){
		 this.reproducir = reproducir;
		 this.adapterpacks = adapterpacks;
		 this.listview = listview;
		 loadingAnimation.start();
		 
		 //prefs.edit().clear().commit();
		 rebootData();
		
		 new downloadPacks().execute();
		 
	}
	public void downloadData(RelativeLayout reproducir,AdapterPacks adapterpacks,ListView listview, int version){
		 this.reproducir = reproducir;
		 this.adapterpacks = adapterpacks;
		 this.listview = listview;
		 loadingAnimation.start();
		 //prefs.edit().clear().commit();
		 rebootData();
		 prefs.edit().putInt("version",version).commit();

		 new downloadPacks().execute();
		 
	}
	public void downloadMp3(String mp3, TextView time, MediaPlayer mp, final String pack, final ImageView play, boolean streaming, String id_meditation){
		
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		String free = Formatter.formatFileSize(ctx, availableBlocks * blockSize);
		long total =  availableBlocks * blockSize;
		Log.i("medita_memory", free);
		Log.i("medita_memory", String.valueOf(total));
		Log.i("medita_memory", Formatter.formatFileSize(ctx, 361078784));

		if (streaming){
			//if (total > 361078784){
			if (total < 0){
				Log.i("medita_intros", "1");
				time_left = time;
				this.mp = mp;
				this.play = play;
				new downloadSaveMp3().execute(mp3,id_meditation);
			}
			else {
			/*final Dialog dialog = new Dialog(ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_generico);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.setCancelable(false);
			TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
			text.setText("No hay espacio en la memória interna de la APP.");

			TextView dialogButton = (TextView) dialog.findViewById(R.id.id_alert_btn);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();


					Intent i = new Intent(ctx, Meditaciones.class);
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					i.putExtra("pack", pack);
					i.setAction(Config.from_Reproductor);
		    		ctx.startActivity(i);
		    		((Activity)ctx).finish();

				}
			});

			dialog.show();		*/


				try {
					Log.i("medita_streaming", "Reproduciendo srtreaming!!");
					Log.i("medita_streaming", "1");
					mp.reset();
					mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mp.setDataSource(Config.url_meditaciones + mp3);
					mp.prepare();
					Reproductor.play_block = false;
					play.performClick();
				} catch (Exception e) {
					Toast.makeText(ctx, "Ha habido un error de conexión, intente conectarse más tarde.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(ctx, MainActivity.class);
					//i.setAction(Config.from_Meditaciones);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ctx.startActivity(i);
					((Activity) ctx).finish();
				}
			}
		}else{
			if (total > 361078784){
				new downloadSaveMp3().execute(mp3,id_meditation);
			}else{
				Toast.makeText(ctx, "No queda espacio en el dispositivo para guardar la meditación.", Toast.LENGTH_LONG).show();
			}
		}
		
		 
	}
	public void searchUpdates(){
		new isUpdated().execute();		 
	}
	
	private class downloadPacks extends AsyncTask<String, Void, String> {			 
		 @Override
	        protected void onPreExecute() {
			 Log.i("medita_downloader","Empezando a descargar packs.");
			// loadingAnimation.start();
		 	}
	        @Override
	        protected String doInBackground(String... params) {	  
	           String mac = Basics.getWifiMac(ctx);
	           String result = null;
	           HttpConnection http = null;	        
	           
	           if (Basics.checkConn(ctx)){
	        	   try {
					   http = new HttpConnection();
					   
					   JSONObject jo =  new JSONObject();
					   jo.put("token",Config.token);
					   jo.put("mac",mac);
					   
					   http = new HttpConnection();
			           result = http.postData(Config.url_get_packs, jo.toString());
			           if (Config.log)
			        	   Log.i("medita",result);			           
			         			           
					} catch (JSONException e) {
						Log.i("medita","Error descargando datos de los packs.");
						error = false;
					}
	           }			   
 
	           return result;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	
	        	if ((result != null) && (result.compareTo("") != 0)){	
	        		try {
						packs = new JSONArray(result);
						MainActivity.packs = packs;
						prefs.edit().putString("packs", result).commit();	        			
						new downloadMeditaciones().execute();
					} catch (JSONException e) {
						//prefs.edit().clear().commit();
						error = false;
					}	        		        		
	        		
	        	}	        	
	        	else {
	        		//prefs.edit().clear().commit();
	        		
	        		//Dialogo error datos.
	        		loadingAnimation.stop();
		           	loading.setVisibility(View.INVISIBLE);
		           	reproducir.setVisibility(View.VISIBLE);
	        	}

				Log.i("medita_downloader","Terminando a descargar packs.");
	        		        	
	        }     	        
	  }    
	
	private class downloadMeditaciones extends AsyncTask<String, Void, String> {			 
		 @Override
	        protected void onPreExecute() {
			// loadingAnimation.start();
			 Log.i("medita_downloader","Empezando a descargar meditaciones.");
		 	}
	        @Override
	        protected String doInBackground(String... params) {	  
	           String mac = Basics.getWifiMac(ctx);
	           String result = null;
	           HttpConnection http = null;	        
	           
	           if (Basics.checkConn(ctx)){
	        	   try {
					   http = new HttpConnection();
					   
					   JSONObject jo =  new JSONObject();
					   jo.put("token",Config.token);
					   jo.put("mac",mac);
					   
					   http = new HttpConnection();
			           result = http.postData(Config.url_get_meditaciones, jo.toString());
			           if (Config.log)
			        	   Log.i("medita",result);			           
			         			           
					} catch (JSONException e) {
						Log.i("medita","Error descargando datos de los packs.");
						error = false;
					}
	           }			   

	           return result;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	
	        	if ((result != null) && (result.compareTo("") != 0)){	     
	        		
	        		JSONArray completadas = new JSONArray();
	        		JSONArray meditaciones_aux = new JSONArray();
	        		if (prefs.contains("meditaciones_completadas")){
	        			try {
	        				meditaciones_aux = new JSONArray (result);
	        				completadas = new JSONArray (prefs.getString("meditaciones_completadas",""));
	        				
	        				for (int i=0; i<meditaciones_aux.length();i++){
	        					for (int j=0; j< completadas.length() ; j++ ){
	        						if (meditaciones_aux.optJSONObject(i).optString("id_meditacion").compareTo(completadas.getString(j)) == 0){
		        						 try {
		        							 meditaciones_aux.optJSONObject(i).put("isCompleted", true);
		        						} catch (JSONException e) {
		        						}				 
		        					 }
	        					}
	        					 
	        				 }	
	        			} catch (JSONException e) {
	        			}
		        		prefs.edit().putString("meditaciones", meditaciones_aux.toString()).commit();	 

	               }
	        		else{
	        			prefs.edit().putString("meditaciones",result).commit();	
	        		}
	        		
					new downloadOptions().execute();
	        	}	        	
	        	else {
	        		//prefs.edit().clear().commit();
	        		//rebootData();
	        		//Dialogo error datos.
	        		loadingAnimation.stop();
		           	loading.setVisibility(View.INVISIBLE);
		           	reproducir.setVisibility(View.VISIBLE);
	        	}
	        	
				prefs.edit().putInt("version", prefs.getInt("server_version",0)).commit();

				Log.i("medita_downloader","Terminando de descargar meditaciones.");
	        }     	        
	  }
	private class downloadOptions extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// loadingAnimation.start();
			Log.i("medita_downloader","Empezando a descargar opciones de la Home.");
		}
		@Override
		protected String doInBackground(String... params) {
			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;

			if (Basics.checkConn(ctx)){
				try {
					http = new HttpConnection();

					JSONObject jo =  new JSONObject();
					jo.put("token",Config.token);
					jo.put("mac",mac);

					http = new HttpConnection();
					result = http.postData(Config.url_get_config, jo.toString());
					if (Config.log)
						Log.i("medita",result);

				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de configuración de la Home.");
					error = false;
				}
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if ((result != null) && (result.compareTo("") != 0)){
				prefs.edit().putString("home_options",result).commit();

				new downloadSavePacksIcons().execute();
				new downloadSavePacksBg().execute();

			}
			else {
				//prefs.edit().clear().commit();
				//rebootData();
				//Dialogo error datos.
				loadingAnimation.stop();
				loading.setVisibility(View.INVISIBLE);
				reproducir.setVisibility(View.VISIBLE);
			}

			Log.i("medita_downloader","Terminando de descargar opciones de la Home.");
		}
	}
	private	class downloadSavePacksIcons extends AsyncTask<String, Void, String> {			 
			 @Override
		        protected void onPreExecute() {
				 Log.i("medita_downloader","Empezando de descargar pack icons.");
			 	}
		        @Override
		        protected String doInBackground(String... params) {	  
		           String mac = Basics.getWifiMac(ctx);
		           String result = null;
		           HttpConnection http = null;	        
		           
		           if (Basics.checkConn(ctx)){
		        	   JSONArray ja = packs;
					   Bitmap ico= null;
					   for(int i=0; i<ja.length();i++){
						   http = new HttpConnection();
						   ico = http.getBitmap(Config.url_iconos + ja.optJSONObject(i).optString("pack_icono"));
						   //Basics.saveBitmapSD(ja.optJSONObject(i).optString("pack_icono"), "Medita", ico);
						   Basics.saveBitmapToInternalStorage(ctx,ico,"iconos",ja.optJSONObject(i).optString("pack_icono"));
					   }
		           }			   
	  
		           return result;
		        }

		        @Override
		        protected void onPostExecute(String result) {
		        	
		        	//new downloadSavePacksBg().execute();

					Log.i("medita_downloader","Terminando de descargar pack_icons.");
		        	
		        	
		        }     	        
		   
	}
	private	class downloadSavePacksBg extends AsyncTask<String, Void, String> {			 
		 @Override
	        protected void onPreExecute() {
			 Log.i("medita_downloader","Empezando a  descargar Backgrounds.");
		 	}
	        @Override
	        protected String doInBackground(String... params) {	  
	           String mac = Basics.getWifiMac(ctx);
	           String result = null;
	           HttpConnection http = null;	        
	           
	           if (Basics.checkConn(ctx)){
	        	   JSONArray ja = packs;
				   Bitmap fondo= null;
				   for(int i=0; i<ja.length();i++){
					   http = new HttpConnection();
					   fondo = http.getBitmap(Config.url_fondos + ja.optJSONObject(i).optString("pack_fondo_med"));
					   //Basics.saveBitmapSD(ja.optJSONObject(i).optString("pack_fondo_med"), "Medita", fondo);
					   Basics.saveBitmapToInternalStorage(ctx,fondo,"fondos",ja.optJSONObject(i).optString("pack_fondo_med"));
					   http = new HttpConnection();
					   fondo = http.getBitmap(Config.url_fondos + ja.optJSONObject(i).optString("pack_fondo_rep"));
					   //Basics.saveBitmapSD(ja.optJSONObject(i).optString("pack_fondo_rep"), "Medita", fondo);
					   Basics.saveBitmapToInternalStorage(ctx,fondo,"fondos",ja.optJSONObject(i).optString("pack_fondo_rep"));
				   }
	           }			   
 
	           return result;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	
	        	if ((result == null) || (result.compareTo("") == 0)){
	        		adapterpacks = new AdapterPacks((Activity)ctx, packs);
					listview.setAdapter(adapterpacks);
					adapterpacks.notifyDataSetChanged();	 

	        		
	        	}
	        	
	        	if ((result == null) || (result.compareTo("") == 0)){
	        		//Dialogo error datos.
	        	}
	        	
	        	loadingAnimation.stop();
	           	loading.setVisibility(View.INVISIBLE);
	           	reproducir.setVisibility(View.VISIBLE);
	           	
	           	if (!prefs.contains("firstTime")){
	           		Intent i = new Intent ((Activity)ctx, Tutorial.class);
	           		ctx.startActivity(i);
	           		
	           		prefs.edit().putBoolean("firstTime", true).commit();
	           		// marcamos que se ha mostrado el tutorial acutalizado (solo pasara en nuevas instalaciones)
					prefs.edit().putBoolean("updated_tutorial", true).commit();
	           	}else{
					((MainActivity)ctx).alert_meditaciones_nuevas();
				}
	           	
	           	if (!prefs.contains("disclaimer")){
					((MainActivity)ctx).disclaimer();
				}

				Log.i("medita_downloader","Terminando de  descargar Backgrounds.");

	        }     	        
	}
	private class downloadSaveMp3 extends AsyncTask<String, Void, String> {
		protected String song;
		protected String id_meditation;
		 @Override
	        protected void onPreExecute() {		
			 loadingAnimation.start();
			 	loading.setVisibility(View.VISIBLE);
			 	
		 	}
	        @Override
	        protected String doInBackground(String... params) {	  
	           String mac = Basics.getWifiMac(ctx);
	           InputStream result = null;
	           HttpConnection http = null;	
	           String url = params[0];
	           song = params[0];
	           id_meditation = params[1];
	           File mypath = null;
	           
	           if (Basics.checkConn(ctx)){
	        	   try {
		        	   URL Url = new URL(Config.url_meditaciones + url);				   
					   InputStream input = new BufferedInputStream(Url.openStream());				   
					   OutputStream output;
					   ContextWrapper cw = new ContextWrapper(ctx);
				         // path to /data/data/yourapp/app_data/imageDir
				        File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
				        // Create imageDir
				        mypath=new File(directory,params[0]);
					
						output = new FileOutputStream(mypath);

			            byte data[] = new byte[1024];
			            long total = 0;
			            int count=0;
			            while ((count = input.read(data)) != -1) {
			                total++;
			                Log.i("Medita "+"while","A"+total);
			                output.write(data, 0, count);
			            }
	
			            output.flush();
			            output.close();
			            input.close();
			            			            
	        	   } catch (FileNotFoundException e) {
	        		   Log.i("medita", "Error FileNotFoundException");
					} catch (IOException e) {
						 Log.i("medita", "Error IOException");
						 if (mypath != null){
							 mypath.delete();
						 }
					}
				   
	           }			   

	           return null;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	             /*Utilities utils = new Utilities();
	        	 ContextWrapper cw = new ContextWrapper(ctx);
	             File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
	             File file=new File(directory,song);
	     		 if(file.exists())   {
	     			try {
	     				Log.i("medita_intro","3");
	     				FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
	     				mp.reset();
	     		        mp.setDataSource(fileInputStream.getFD());
	     		        mp.prepare();
	     		        time_left.setText(""+utils.milliSecondsToTimer(mp.getDuration()));
	     		        Reproductor.play_block = false;
	     		        play.performClick();

	     		        
	     			} catch (IOException e) {	
	     				Log.i("medita","Error playing audio");
	     				file.delete();
	     				Toast.makeText(ctx, "Ha habido un error de conexión, intente conectarse más tarde 1.",Toast.LENGTH_LONG).show();
	     				 Intent i = new Intent(ctx, MainActivity.class);   
						 //i.setAction(Config.from_Meditaciones);
						 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    		 ctx.startActivity(i);
			    		 ((Activity)ctx).finish();
	     			}  
	     		 }
	     		 else{
	     			 if (Basics.checkConn(ctx)){
	     				try {

							Log.i("medita_intro","4");
		            		mp.reset();
		                  	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    				mp.setDataSource(Config.url_meditaciones + song);
		    				mp.prepare();
		    		        Reproductor.play_block = false;
		    			    play.performClick();  
		    			} catch (Exception e) {
		    				Toast.makeText(ctx, "Ha habido un error de conexión, intente conectarse más tarde 2.",Toast.LENGTH_LONG).show();
		     				 Intent i = new Intent(ctx, MainActivity.class);   
							 //i.setAction(Config.from_Meditaciones);
							 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    		 ctx.startActivity(i);
				    		 ((Activity)ctx).finish();
		    			} 
	     			 }
	     			 else{
	     				 Toast.makeText(ctx, "Ha habido un error de conexión, intente conectarse más tarde 3.",Toast.LENGTH_LONG).show();
	     				 Intent i = new Intent(ctx, MainActivity.class);   
						 //i.setAction(Config.from_Meditaciones);
						 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    		 ctx.startActivity(i);
			    		 ((Activity)ctx).finish();
	     			 }
	     			
	              
	     		 }*/

				new MeditationFunctions(ctx).setMeditationDownload(id_meditation);
	        	
	     		loadingAnimation.stop();
	           	loading.setVisibility(View.INVISIBLE);
	        }
	}
	private class isUpdated extends AsyncTask<String, Void, String> {
		 @Override
	        protected void onPreExecute() {
		 	}
	        @Override
	        protected String doInBackground(String... params) {	  
	           String mac = Basics.getWifiMac(ctx);
	           String result = null;
	           HttpConnection http = null;	        
	           
	           if (Basics.checkConn(ctx)){
	        	   try {
					   http = new HttpConnection();
					   
					   JSONObject jo =  new JSONObject();
					   jo.put("token",Config.token);
					   jo.put("mac",mac);
					   
					   http = new HttpConnection();
			           result = http.postData(Config.url_get_updates, jo.toString());

			           if (Config.log){
			        	   if (result != null)
			        		   Log.i("medita",result);		        	   
			           }
			        	   
			         			           
					} catch (JSONException e) {
						Log.i("medita","Error descargando datos de actualizaciones.");
						error = false;
					}
	           }			   

	           return result;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	
	        	if ((result != null) && (result.compareTo("") != 0)){	
	        		int version;
					String version_desc ;
					try {
						version = new JSONArray(result).optJSONObject(0).optInt("version");
						version_desc = new JSONArray(result).optJSONObject(0).optString("notas");
						Log.i("medita_result",String.valueOf(version));
						Log.i("medita_result",version_desc);
		        		prefs.edit().putInt("server_version", version).commit();
						prefs.edit().putString("version_desc", version_desc).commit();

					} catch (JSONException e) {
					}
	        		
	        	}	
    	
	        	
	        }     	        
	  } 
	
	protected void rebootData(){
		JSONArray completadas = new JSONArray();
		JSONArray meditaciones_aux = new JSONArray();
		if (prefs.contains("meditaciones")){
			try {
				meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
				 for (int i=0; i<meditaciones_aux.length();i++){
					 if (meditaciones_aux.optJSONObject(i).optBoolean("isCompleted")){
						 completadas.put(meditaciones_aux.optJSONObject(i).optString("id_meditacion"));				 
					 }
				 }	
		    	prefs.edit().putString("meditaciones_completadas", completadas.toString()).commit();
			} catch (JSONException e) {
			}
	  	
       }
		 prefs.edit().remove("isNoFirstTime").apply();
		 prefs.edit().remove("packs").apply();
		 prefs.edit().remove("meditaciones").apply();
		 
		 ContextWrapper cw = new ContextWrapper(ctx);
         // path to /data/data/yourapp/app_data/imageDir
         File dir = cw.getDir("meditaciones", Context.MODE_PRIVATE);
         if (dir.isDirectory()) 
         {
             String[] children = dir.list();
             for (int i = 0; i < children.length; i++)
             {
                new File(dir, children[i]).delete();
             }
         }
         dir = cw.getDir("iconos", Context.MODE_PRIVATE);
         if (dir.isDirectory()) 
         {
             String[] children = dir.list();
             for (int i = 0; i < children.length; i++)
             {
                new File(dir, children[i]).delete();
             }
         }
         dir = cw.getDir("fondos", Context.MODE_PRIVATE);
         if (dir.isDirectory()) 
         {
             String[] children = dir.list();
             for (int i = 0; i < children.length; i++)
             {
                new File(dir, children[i]).delete();
             }
         }
		 
	}
	
	public Bitmap loadBitmapFromAsset(String file) {
		   Bitmap bm = null;
	        // load image
	        try {
	            // get input stream
	            InputStream ims = ctx.getAssets().open(file);
	            // load image as Drawable
	             bm =  BitmapFactory.decodeStream(ims);
	        }
	        catch(IOException ex) {
	            return null;
	        }
	        return bm;
	 }

	public void login(boolean conection, String email, String password, Login.AsyncResponse response){

		new Login(conection, email, password, response).execute();
	}

	public static class Login extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String email;
		private String password;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public Login(boolean conection, String email, String password, AsyncResponse response) {
			this.conection = conection;
			this.email = email;
			this.password = password;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;

			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("usuario", email);
					jsonObject.put("password", password);
					jsonObject.put("token", Config.token);

                    http = new HttpConnection();
					result = http.postData(Config.url_login, jsonObject.toString());
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public void registrar(boolean conection, String email, String password, String nombre,int newsletter, Registrar.AsyncResponse response){

		new Registrar(conection, email, password, nombre,newsletter,response).execute();
	}

	public static class Registrar extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String email;
		private String password;
		private String nombre;
		private int newsletter;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public Registrar(boolean conection, String email, String password, String nombre, int newsletter, AsyncResponse response) {
			this.conection = conection;
			this.email = email;
			this.password = password;
			this.nombre = nombre;
			this.newsletter = newsletter;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
			//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;

			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("usuario", email);
					jsonObject.put("password", password);
					jsonObject.put("plataforma", Config.plataforma);
					jsonObject.put("nombre", nombre);
					jsonObject.put("newsletter", newsletter);
					jsonObject.put("token", Config.token);

					http = new HttpConnection();
					result = http.postData(Config.url_registrar, jsonObject.toString());
					if (Config.log){
						if (result != null)
							Log.i("medita",result);
					}
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public void recuperarPass(boolean conection, String email, RecuperarPass.AsyncResponse response){

		new RecuperarPass(conection, email, response).execute();
	}

	public static class RecuperarPass extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String email;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public RecuperarPass(boolean conection, String email, AsyncResponse response) {
			this.conection = conection;
			this.email = email;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
			//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;

			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("usuario", email);
					jsonObject.put("token", Config.token);
					http = new HttpConnection();
					result = http.postData(Config.url_recuperar_pass, jsonObject.toString());
					if (Config.log){
						if (result != null)
							Log.i("medita",result);
					}
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public void saveSuscription(boolean conection, String id_usuario, String sku, String purchaseToken,
								String orderId, String fecha_inicio, boolean autoRenewing, String fecha_fin_suscripcion, SaveSuscription.AsyncResponse response){
		new SaveSuscription(conection, id_usuario, sku, purchaseToken, orderId, fecha_inicio, autoRenewing, fecha_fin_suscripcion, response).execute();
	}

	public static class SaveSuscription extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String id_usuario;
		private String sku;
		private String purchaseToken;
		private String orderId;
		private String fecha_inicio_millis;
		private boolean autoRenewing;
		private String fecha_fin_suscripcion;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public SaveSuscription(boolean conection, String id_usuario, String sku, String purchaseToken,
							   String orderId, String fecha_inicio_millis, boolean autoRenewing,
							   String fecha_fin_suscripcion, AsyncResponse response) {
			this.conection = conection;
			this.id_usuario = id_usuario;
			this.sku = sku;
			this.purchaseToken = purchaseToken;
			this.orderId = orderId;
			this.fecha_inicio_millis = fecha_inicio_millis;
			this.autoRenewing = autoRenewing;
			this.fecha_fin_suscripcion = fecha_fin_suscripcion;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
			String result = null;
			HttpConnection http = null;
			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id_usuario", id_usuario);
					jsonObject.put("purchaseToken", purchaseToken);
					jsonObject.put("orderId", orderId);

					long fecha = Long.parseLong(fecha_inicio_millis);
					Date date = new Date(fecha);
					SimpleDateFormat dateFormat = new SimpleDateFormat(Config.defaultDateFormat, Locale.ENGLISH);
					jsonObject.put("fecha_inicio", dateFormat.format(date));

					jsonObject.put("sku", sku);

					jsonObject.put("autoRenewing", autoRenewing);
					jsonObject.put("fecha_fin_suscripcion", fecha_fin_suscripcion);
					jsonObject.put("token", Config.token);

					http = new HttpConnection();
					result = http.postData(Config.url_guardar_suscripcion, jsonObject.toString());

				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public static class GetSuscripciones extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String id_usuario;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public GetSuscripciones(boolean conection, String id_usuario, AsyncResponse response) {
			this.conection = conection;
			this.id_usuario = id_usuario;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
			//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;
			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id_usuario", id_usuario);
					jsonObject.put("token", Config.token);

					http = new HttpConnection();
					result = http.postData(Config.url_get_suscripcion, jsonObject.toString());
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public static class UpdateSuscripciones extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String id_usuario;
		private String purchaseToken;
		private boolean autoRenewing;
		private String orderId;
		private String fecha_inicio_millis;
		private String fecha_fin_suscripcion;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public UpdateSuscripciones(boolean conection, String id_usuario, String purchaseToken,
								   String orderId, String fecha_inicio_millis, boolean autoRenewing,
								   String fecha_fin_suscripcion, AsyncResponse response) {

			this.conection = conection;
			this.id_usuario = id_usuario;
			this.purchaseToken = purchaseToken;
			this.orderId = orderId;
			this.fecha_inicio_millis = fecha_inicio_millis;
			this.autoRenewing = autoRenewing;
			this.fecha_fin_suscripcion = fecha_fin_suscripcion;
			this.response = response;

		}

		@Override
		protected String doInBackground(Void... voids) {
			//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;
			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id_usuario", id_usuario);
					jsonObject.put("purchaseToken", purchaseToken);
					jsonObject.put("autoRenewing", autoRenewing);
					jsonObject.put("orderId", orderId);

					long fecha = Long.parseLong(fecha_inicio_millis);
					Date date = new Date(fecha);
					SimpleDateFormat dateFormat = new SimpleDateFormat(Config.defaultDateFormat, Locale.ENGLISH);
					jsonObject.put("fecha_inicio", dateFormat.format(date));

					//todo comprobar estas lineas en la ddbb
//					date = new Date(System.currentTimeMillis());
//					jsonObject.put("last_update", dateFormat.format(date));

					jsonObject.put("fecha_fin_suscripcion", fecha_fin_suscripcion);
					jsonObject.put("token", Config.token);


					http = new HttpConnection();
					result = http.postData(Config.url_update_suscripcion, jsonObject.toString());
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

	public static class setNewsletter extends AsyncTask<Void, Void, String> {

		private boolean conection;
		private String email;
		private String name;
		private String mac;
		private AsyncResponse response;

		public interface AsyncResponse {
			void processFinish(String respuesta);
		}

		public setNewsletter(boolean conection, String email, String name, String mac, AsyncResponse response) {
			this.conection = conection;
			this.email = email;
			this.name = name;
			this.mac = mac;
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... voids) {
//			String mac = Basics.getWifiMac(ctx);
			String result = null;
			HttpConnection http = null;

			if (conection){
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("token",Config.token);
					jsonObject.put("mac",mac);
					jsonObject.put("plataforma","Android");
					jsonObject.put("email",email);
					jsonObject.put("name",name);

					http = new HttpConnection();

					result = http.postData(Config.url_set_newsletter, jsonObject.toString());
					if (Config.log){
						if (result != null)
							Log.i("medita",result);
					}
				} catch (JSONException e) {
					Log.i("medita","Error descargando datos de Login.");
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			response.processFinish(result);
		}
	}

}
