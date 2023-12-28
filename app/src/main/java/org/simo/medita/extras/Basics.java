
package org.simo.medita.extras;

/**
 * author Ignacio Paso 
 * 
 **/

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.Config;
import org.simo.medita.R;
import org.simo.medita.R.drawable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Basics {
	
	//informaci?n del dispositivo
	
	public static String getLanguage(){	
		return Locale.getDefault().getDisplayLanguage();
	}
	public static String getCodeLanguage(){	
		return Locale.getDefault().getISO3Language();
	}	
	public static String getCodeLanguage(Context ctx){	
		return 	 ctx.getResources().getConfiguration().locale.getISO3Language();
	}
	public static String getPackName(Context ctx){	
		return 	 ctx.getPackageName();
	}
	public static String getClassName(Context ctx){	
		return 	 ctx.getClass().getSimpleName();
	}
	public static String getAppName(Context ctx){	
		PackageManager packageManager = ctx.getPackageManager();
		ApplicationInfo applicationInfo;
		try {
		    applicationInfo = packageManager.getApplicationInfo(getPackName(ctx), 0);
		} catch (final NameNotFoundException e) {
			applicationInfo=null;
		}
		if(applicationInfo != null) 
			return packageManager.getApplicationLabel(applicationInfo).toString();
		else
			return null;
	}
	public static String getSpaceInternalMemory(Context ctx){
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(ctx, availableBlocks * blockSize);
		
	}
	
	public static String getWifiMac(Context ctx){
    	WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
    	String address;	    	
	    WifiInfo info = wifiManager.getConnectionInfo();
	    address = info.getMacAddress();   	    
	    	
		return address;
    }	
	
	/*public static String getImei(Context ctx){
		String imei=null;
		TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId().toString();		
		return imei;
	}*/
	
	 protected static int getSreenWidht(Activity act){
    	 Display display = act.getWindowManager().getDefaultDisplay();
 		Point size = new Point();
 		display.getSize(size);
 		int width = size.x;
 		int height = size.y;
 		return width;
    }
    protected static int getSreenHeight(Activity act){
   	 Display display = act.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		return height;
   }	
    
	protected static Drawable getIconApp(String pkg, Activity act){
		Drawable icon=null;
		try{
			
			if (isPackageExisted(pkg,act))
				icon = act.getPackageManager().getApplicationIcon(pkg);
			else
				icon = act.getResources().getDrawable(drawable.ic_launcher);
			
		}catch (NameNotFoundException ne)
			 {
				icon = act.getResources().getDrawable(drawable.ic_launcher);
			 }	
		return icon;
		
	}	
	
    
    public static boolean isPackageExisted(String targetPackage, Activity act){
        List<ApplicationInfo> packages;
        PackageManager pm;
            pm = act.getPackageManager();        
            packages = pm.getInstalledApplications(0);
            for (ApplicationInfo packageInfo : packages) {
        if(packageInfo.packageName.equals(targetPackage)) return true;
        }        
        return false;
    }
	
	public static boolean checkConn(Context ctx){    	
	      ConnectivityManager conMgr =  (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	  	  NetworkInfo i = conMgr.getActiveNetworkInfo();
	  	  if (i == null)
	  	    return false;
	  	  if (!i.isConnected())
	  	    return false;
	  	  if (!i.isAvailable())
	  	    return false;
	  	  return true;
	  }
	public static boolean isWifiActivated(Context ctx){	
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		    if(wifiManager.isWifiEnabled()){
		         return true;
		    }else{
		       return false;
		    }
	}
	 public static boolean SD_disponible(){
			boolean sdDisponible = false;
			boolean sdAccesoEscritura = false;
			 
			//Comprobamos el estado de la memoria externa (tarjeta SD)
			String estado = Environment.getExternalStorageState();
			 
			if (estado.equals(Environment.MEDIA_MOUNTED))
			{
			    sdDisponible = true;
			    sdAccesoEscritura = true;
			}
			else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
			{
			    sdDisponible = true;
			    sdAccesoEscritura = false;
			}
			else
			{
			    sdDisponible = false;
			    sdAccesoEscritura = false;
			}
			if (sdDisponible && sdAccesoEscritura)
				return true;
			else
				return false;
		}
	
	//Formataci?n de datos
	 public static String getFecha(){

		 String fecha = "";    	
		 Calendar calendar = Calendar.getInstance();

		 String year= Integer.toString(calendar.get(Calendar.YEAR));
		 String dia = Integer.toString(calendar.get(Calendar.DATE));
		 String mes = Integer.toString(calendar.get(Calendar.MONTH)+1);
		 fecha=dia+"/"+mes+"/"+year;
		 return fecha;
	    }
	 public static String getFechaCont(){

		 String fecha = "";    	
		 Calendar calendar = Calendar.getInstance();

		 String year= Integer.toString(calendar.get(Calendar.YEAR));
		 String dia = Integer.toString(calendar.get(Calendar.DATE));
		 String mes = Integer.toString(calendar.get(Calendar.MONTH)+1);
		 fecha=dia+mes+year;
		 return fecha;
	    }
	 public static String getHora(){

		 String hora = "";    	
		 Calendar calendar = Calendar.getInstance();

		 String h= Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		 String m = Integer.toString(calendar.get(Calendar.MINUTE));
		 String s = Integer.toString(calendar.get(Calendar.SECOND));
		 hora=h+m+s;
		 return hora;
	    }
	 public static String getFechaSQL(){

		 String fecha = "";    	
		 Calendar calendar = Calendar.getInstance();
		 String year= Integer.toString(calendar.get(Calendar.YEAR));
		 String dia = Integer.toString(calendar.get(Calendar.DATE));
		 String mes = Integer.toString(calendar.get(Calendar.MONTH)+1);
		 fecha=year+"-"+mes+"-"+dia+" 00:00:00.000";
		 
		 return fecha;
	    }
	 public static String getFechaHoraSQL(){

		 String fecha = "";    	
		 Calendar calendar = Calendar.getInstance();
		 String year= Integer.toString(calendar.get(Calendar.YEAR));
		 String dia = Integer.toString(calendar.get(Calendar.DATE));
		 String mes = Integer.toString(calendar.get(Calendar.MONTH)+1);
		 String h= Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		 String m = Integer.toString(calendar.get(Calendar.MINUTE));
		 String s = Integer.toString(calendar.get(Calendar.SECOND));
		 fecha=year+"-"+mes+"-"+dia+" "+h+":"+m+":"+s;
		 
		 return fecha;
	    }
	public static String formatDirGps(String direccion){
    	
    	if ((direccion.contains("PARES"))||(direccion.contains("IMPARES"))){
    		int inicio = direccion.indexOf("(");
    		int fin = direccion.indexOf(")", inicio+1);    		 
    		direccion=direccion.replace(direccion.substring(inicio, fin+1),"");
    	}
		if  ((direccion.contains("PARES"))||(direccion.contains("IMPARES"))){
			int inicio = direccion.indexOf("(");
    		int fin = direccion.indexOf(")", inicio+1);    		 
    		direccion=direccion.replace(direccion.substring(inicio, fin+1),"");		
		}	
		
		direccion=direccion.replaceAll("n.d", "");
		return direccion;
	}	
	//Gesti?n de errores
	/*public void error(Context ctx, Activity act){
    	Vibrator vibrator =(Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    act.finish();
    } 	*/
	
	 //Imagenes
	
	

	public static Bitmap loadImageFromUrl(final String url) {
    	final int BUFFER_IO_SIZE = 8000;
       try {
           BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream(), BUFFER_IO_SIZE);
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           BufferedOutputStream bos = new BufferedOutputStream(baos, BUFFER_IO_SIZE);
           copy(bis, bos);
           bos.flush();
           return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
       } catch (IOException e) {
    	   return null;
       }
       
   }

   private static void copy(final InputStream bis, final OutputStream baos) throws IOException {
       byte[] buf = new byte[256];
       int l;
       while ((l = bis.read(buf)) >= 0) baos.write(buf, 0, l);
   }
	
	 public static String imageToString(Bitmap bmp){
			
			int bytes = bmp.getByteCount();
			ByteBuffer buffer = ByteBuffer.allocate(bytes);
			bmp.copyPixelsToBuffer(buffer);
			byte[] array = buffer.array();
			
			String base64 = Base64.encodeToString(array, Base64.DEFAULT);
		
			return base64;	 			 
		}
	 public static Bitmap StringToimage(String bmp){
			
			byte[] array = Base64.decode(bmp, Base64.DEFAULT);		
			Bitmap bitmap = BitmapFactory.decodeByteArray(array , 0, array.length);
			
			return bitmap;	 			 
	} 
	 public static Bitmap compressBitmap(Bitmap bmIn, int quality){		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 bmIn.compress(CompressFormat.JPEG, quality, out);
		 Bitmap bmOut=BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
		 return bmOut;
	 }
	 public static byte[] compressBitmapByte(Bitmap bmIn, int quality){		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 bmIn.compress(CompressFormat.JPEG, quality, out);
		 byte[] byteArray = out.toByteArray();
		 return byteArray;
	 }
	
	 //Json format. Procesamiento de Objetos
	 public static JSONArray concatArray(JSONArray arr1, JSONArray arr2)throws JSONException {
	        JSONArray result = new JSONArray();
	        for (int i = 0; i < arr1.length(); i++) {
	            result.put(arr1.get(i));
	        }
	        for (int i = 0; i < arr2.length(); i++) {
	            result.put(arr2.get(i));
	        }
	        return result;
	    }
	 
	 //Codificaci?n
	 public static String toBase64(String s){
		 byte[] data = null;
		 try {
				data = s.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				System.out.println("Error pasando el String a Bytes");
			}
		 s=Base64.encodeToString(data,Base64.DEFAULT);
		 return s;		 
	 }
	 public static String toBase64(byte[] data){
		 String s;
		 s=Base64.encodeToString(data,Base64.DEFAULT);
		 return s;		 
	 }
	 public static String fromBase64(String s){
		 byte[] data = null;
		 try {
			 data = Base64.decode(s.toString(), Base64.DEFAULT);
	     	s = new String(data, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				System.out.println("Error pasando el String a Bytes");
			}   	
         
		 return s;
	 }	 	 
	 
		//Ficheros
		 public static boolean createDirIfNotExists(String path) {
			    boolean ret = true;

			    File file = new File(Environment.getExternalStorageDirectory(), path);
			    if (!file.exists()) {
			        if (!file.mkdirs()) {
			            Log.i("Files","Problem creating Image folder");
			            ret = false;
			        }
			    }
			    return ret;
			}
		 public static boolean logging(String output, String folder){
				if (SD_disponible()){				
				try
				{
					createDirIfNotExists(folder);
				    File ruta_sd = Environment.getExternalStorageDirectory();		 
				    File f = new File(ruta_sd.getAbsolutePath()+"/"+folder, "log_"+getFecha()+".txt");		    
				    FileWriter fout= new FileWriter(f, true);
				    fout.append(System.getProperty("line.separator"));
				    fout.write(output);
				    fout.close();		 
				}
				catch (Exception ex)
				{
					Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
					return false;		    
				}
				return true;
				}
				else
					return false;
			}	 
		 
		public static boolean writeFileSD(String folder,String fichero, String contenido){
			if (SD_disponible()){		
				try
				{
				    File ruta_sd = Environment.getExternalStorageDirectory();			 
				    File f = new File(ruta_sd.getAbsolutePath()+"/"+folder, fichero);
				 
				    OutputStreamWriter fout =new OutputStreamWriter(new FileOutputStream(f));			 
				    fout.write(contenido);
				    fout.append(System.getProperty("line.separator"));
				    fout.close();
				}
				catch (Exception ex)
				{
				    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
				    return false;
				}	
				return true;
			}			
			return false;
		}
		public static String readFileSD(String folder, String file){			
			String texto=null;
			if (SD_disponible()){		
				try
				{
				    File ruta_sd = Environment.getExternalStorageDirectory();			 
				    File f = new File(ruta_sd.getAbsolutePath()+"/"+folder, file);			 
				    BufferedReader fin =new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				    texto = fin.readLine();
				    fin.close();
				}
				catch (Exception ex)
				{
				    Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
				    texto=null;
				}		
			}
			return texto;
		}		
		
		public static String saveBitmapToInternalStorage(Context ctx,Bitmap bitmapImage, String folder,String name){
	        ContextWrapper cw = new ContextWrapper(ctx);
	         // path to /data/data/yourapp/app_data/imageDir
	        File directory = cw.getDir(folder, Context.MODE_PRIVATE);
	        // Create imageDir
	        File mypath=new File(directory,name);

	        FileOutputStream fos = null;
	        try {           

	            fos = new FileOutputStream(mypath);

	       // Use the compress method on the BitMap object to write image to the OutputStream
	            bitmapImage.compress(CompressFormat.PNG, 100, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return directory.getAbsolutePath();
	    }
		public static String saveMp3ToInternalStorage(Context ctx,Bitmap bitmapImage, String folder,String name){
	        ContextWrapper cw = new ContextWrapper(ctx);
	         // path to /data/data/yourapp/app_data/imageDir
	        File directory = cw.getDir(folder, Context.MODE_PRIVATE);
	        // Create imageDir
	        File mypath=new File(directory,name);

	        FileOutputStream fos = null;
	        try {           

	            fos = new FileOutputStream(mypath);

	       // Use the compress method on the BitMap object to write image to the OutputStream
	            bitmapImage.compress(CompressFormat.PNG, 100, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return directory.getAbsolutePath();
	    }
		
		
		public static boolean writeFileInternal(Context ctx, String fichero, String contenido){
			
			try
			{
			    OutputStreamWriter fout=new OutputStreamWriter(ctx.openFileOutput(fichero, Context.MODE_PRIVATE));			 
			    fout.write(contenido);
			    fout.close();
			}
			catch (Exception ex)
			{				
			    Log.e("Ficheros", "Error al escribir fichero a memoria interna");
			    return false;
			}			
			return true;
		}		
		public static Bitmap readFileInternal(Context ctx, String folder,String fichero){
			 Bitmap b = null;
			 ContextWrapper cw = new ContextWrapper(ctx);
	         // path to /data/data/yourapp/app_data/imageDir
	         File directory = cw.getDir(folder, Context.MODE_PRIVATE);

			 try {
			        File f=new File(directory,fichero);
			        b = BitmapFactory.decodeStream(new FileInputStream(f));
			    } 
			    catch (FileNotFoundException e) 
			    {
			        e.printStackTrace();
			    }
			
			return b;
		}		
		public static boolean saveBitmapSD(String file, String folder, Bitmap bitmap){
			if (SD_disponible()){ 
				 createDirIfNotExists(folder);
		         String filepath = Environment.getExternalStorageDirectory() + "/"+folder+"/"+file;	          
	             try {
	                     FileOutputStream fos = new FileOutputStream(filepath);	                          
	                     bitmap.compress(CompressFormat.PNG, 90, fos);	                          
	                     fos.flush();
	                     fos.close();
	             } catch (FileNotFoundException e) {
	             	return false;
	             } catch (IOException e) {
	           	  	return false;
	             } 
	             return true;
			}			
			 return false;
		 }
		public static boolean saveBitmapInternal(Context ctx, String fichero, String folder, Bitmap bitmap){			
			ContextWrapper cw = new ContextWrapper(ctx);			
			File dirImages = cw.getDir(folder, Context.MODE_PRIVATE);
			File myPath = new File(dirImages, fichero+".jpg");
			FileOutputStream fos = null;
			try{
				fos = new FileOutputStream(myPath);
			    bitmap.compress(CompressFormat.JPEG, 10, fos);
			    fos.flush();
			    fos.close();
			    }catch (FileNotFoundException ex){
			        ex.printStackTrace();
			        return false;
			    }catch (IOException ex){
			        ex.printStackTrace();
			        return false;
			    }		
			return true;
		 }	
		
		public static File[] getFilesFromDir(Context ctx, String folder){
			String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		    String targetPath = ExternalStorageDirectoryPath + "/"+folder+"/";
		    Toast.makeText(ctx, targetPath, Toast.LENGTH_LONG).show();
		    File targetDirector = new File(targetPath);
		    File[] files = targetDirector.listFiles();
		    
		    return files;		       
		}		
		
		
		
		//Gesti?n de errores
		public static String ErrorMng(int codigo, String folder, String text, String clase, String function){			
			
			String Output=Basics.getHora()+codigo+text+clase+function;		
			writeFileSD(folder,"error_"+getFechaCont(),Output);	
			
			if (codigo==0){
				return ("No hay conexi?n a Internet. Con?ctese a la red");
			}
			else if (codigo==1){
				return ("El servidor no responde. Vuelva a probar en unos minutos");
			}
			else if (codigo==2){
				return ("Respuesta err?nea del servidor.");
			}
			else if (codigo==3){
				return ("Error de datos.");
			}
			else if (codigo==4){
				return ("Tiempo de espera agotado.");
			}
			else if (codigo==5){
				return ("Error tomando la fotograf?a");	
			}
			else if (codigo==6){
				return ("Error escribiendio datos en la SD");	
			}
			else{
				return ("Error inesperado");
			}
		}
		
		//Persistencia. Implementaci?n del servicio.
		
		//La idea es que se intente reconectar varias veces con el servidor y que sino se guarde en una base de datos
		//para intentar enviarla mas tarde.
		
		public static boolean entregaPersistente(Context ctx, int reintentos, Object o, String datos){
			if(Basics.checkConn(ctx)){
    			while ((reintentos>0)&&(o==null)){
        			try {
					} catch (Exception e) {
						return false;
					}
        			reintentos--;
    			}
			}	
			return true;
		}	
		
		public static boolean entregaPersistenteSavedData(Context ctx, int reintentos, Object o, String datos){
			if(Basics.checkConn(ctx)){
    			while ((reintentos>0)&&(o==null)){
        			try {
					} catch (Exception e) {
						return false;
					}
        			reintentos--;
    			}
			}	
			
			//Si hay una entrega ok, tenemos que borrarla del saved Prefences "savedDataToSend".
			
			return true;
		}	
		
		public static boolean saveDataToSend(Context ctx, String datos, JSONObject cabeceras){				
			
//	        SharedPreferences prefs = ctx.getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
	        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
	        String savedDataToSend=prefs.getString("savedDataToSend", null);
	        try {
				JSONArray jsonarray=new JSONArray(savedDataToSend);
				JSONArray putJsonarray=new JSONArray();
				//Formamos el objeto
				putJsonarray.put(datos);
				putJsonarray.put(cabeceras);
				//Insertamos el objeto
				jsonarray.put(putJsonarray);				
			} catch (JSONException e) {
				return false;
			} 			
			return true;
		}
		
		//Licenciamiento y seguridad
		
		public static String encriptar(String texto, String clave){
			String resp=null;
			SecretKeySpec key = new SecretKeySpec(clave.getBytes(), "AES");
			 Cipher cipher;
			  try {
			   cipher = Cipher.getInstance("AES");			    
			   //Comienzo a encriptar
			   cipher.init(Cipher.ENCRYPT_MODE, key);
			   byte[] campoCifrado = cipher.doFinal(texto.getBytes());
			   resp=new String(campoCifrado);
			  } catch (Exception e) {
				   e.printStackTrace();
			  }
			return resp;
		}
		
		public static String desencriptar(String texto, String clave){
				String resp=null;
				Cipher cipher = null;
				try {
					cipher = Cipher.getInstance("AES");
				} catch (NoSuchAlgorithmException e1) {
					return null;
				} catch (NoSuchPaddingException e1) {
					return null;
				}
				
				SecretKeySpec key = new SecretKeySpec(clave.getBytes(), "AES");
				try {
					cipher.init(Cipher.DECRYPT_MODE, key);
					byte[] campoCifrado = cipher.doFinal(texto.getBytes());
				 	byte[] datosDecifrados = cipher.doFinal(campoCifrado);
				 	resp = new String(datosDecifrados);
				  } catch (Exception e) {
					  e.printStackTrace();
				  }
				
				return resp;			
		}
		
		
		public static boolean Validator(String id_aplicacion, String clave,Context ctx, String url) {  
			
			boolean validado=false;
			if(Basics.checkConn(ctx)){				
				HttpResponse response = null;
				JSONObject jsonobject = null;
				String result = null;				
				try{
					HttpPost post = new HttpPost(url);  
					 
					List <NameValuePair> parameters = new ArrayList <NameValuePair>();  
					parameters.add(new BasicNameValuePair("id", id_aplicacion));  					
					 
					UrlEncodedFormEntity sendentity =  new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
					post.setEntity(sendentity);   
					
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
					HttpConnectionParams.setSoTimeout(httpParameters, 42000);					
					 
					HttpClient client = new DefaultHttpClient(httpParameters);  
					response = client.execute(post);
					
					result=EntityUtils.toString(response.getEntity());				
					
				}catch(Exception e){
					validado=true;

				}
				try{
					jsonobject= new JSONObject(result);
					jsonobject=jsonobject.getJSONObject("app1");
					if (jsonobject.getString("expiration").compareToIgnoreCase("yes")==0){
												
					    Calendar cal = Calendar.getInstance();
					    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd z yyyy");
					    cal.setTime(sdf.parse("Mon Mar 14 GMT 2015"));					    
					    
					    if (cal.after(Calendar.getInstance())){
					    	validado=false;					   

						    Log.i("print","plac plac plac");
					    }					
					}
				}catch(Exception e){
					validado=true;
				}
		   if (validado)		
			   Log.i("print","Licencia correcta");
		   else
			   Log.i("print","La licencia de su programa ha expirado");
		   return validado;			   
		}		
		else{
			if (validado)		
				   Log.i("print","Licencia correcta");
			   else
				   Log.i("print","La licencia de su programa ha expirado");
			return validado;
		}			
	}
		
		public static int contarDecimales(double numero){
			String num=String.valueOf(numero);			
			String [] num2 = num.split("\\.");				
			
			return num2[1].length();
		}
		
		public static double recortarDecimales(double valor, int numero){
			String num=String.valueOf(valor);			
			String [] num2 = num.split("\\.");		
			
			
			String aux=String.valueOf(num2[1]);			
			String aux2=aux.substring(0, numero);
			
			String res = num2[0].concat(".").concat(aux2);
						
			return Double.valueOf(res);			
		}
		
		public static int pxToDp(Context ctx, int px) {
		    DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
		    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		    return dp;
		}
		public static int dpToPx(Context ctx,int dp)
		{
		    float density = ctx.getResources().getDisplayMetrics().density;
		    return Math.round((float)dp * density);
		}

	public static void toastCentered( Activity activity, String message, int duration){
		if (activity != null){
			Toast toast = Toast.makeText( activity, message, duration);

			TextView tv = toast.getView().findViewById(android.R.id.message);
			if( tv != null) tv.setGravity( Gravity.CENTER );
			toast.show();
		}

	}

	/** Añade los dias marcados a la fecha indicada
	 *
	 * @param fecha fecha a comprobar
	 * @param dias días despues de la fecha a comprobar
	 * @return long con la fecha resultante
	 */
	public static long daysAfterDate(long fecha, int dias){
		Date date = new Date(fecha);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTimeInMillis();
	}

    /** Dada una fecha inicial en millis, añade 1, 6 u 12 meses a la fecha de cancelacion.
     *
     * @param ctx
     * @param sku
     * @param fechaInicialInMillis
     * @return
     */
	public static String getCancelDate(Context ctx, String sku, long fechaInicialInMillis){

        printLog("getCancelDate");
        int suscriptionMonths = 0;
        final String[] skuIds = ctx.getResources().getStringArray(R.array.suscriptions_sku);
        if (sku.compareToIgnoreCase(skuIds[2])==0){
            suscriptionMonths = 12;
        } else if (sku.compareToIgnoreCase(skuIds[1])==0){
            suscriptionMonths = 6;
        } else if (sku.compareToIgnoreCase(skuIds[0]) == 0) {
            suscriptionMonths = 1;
        }
		SimpleDateFormat dateFormat = new SimpleDateFormat(Config.defaultDateFormat, Locale.ENGLISH);
		Date dateInicial = new Date(fechaInicialInMillis);
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTime(dateInicial);

		if (calendarInicial.getTimeInMillis() < System.currentTimeMillis()){
            printLog("fecha cancel anterior a hoy");
            // mientras la fecha de cancelacion sea anterior a la fecha actual añadimos los meses de suscripcion correspondientes
            while (calendarInicial.getTimeInMillis() < System.currentTimeMillis()){
                printLog("fecha cancel anterior a hoy. Añadiendo " + suscriptionMonths + " mes/es a la fecha");
                calendarInicial.add(Calendar.MONTH, suscriptionMonths);
            }
        }

		Date d = calendarInicial.getTime();
		printLog("getCancelDate fecha final: " + dateFormat.format(d));
		return dateFormat.format(d);
	}

	/** Comprobacion de si la suscripcion esta activa o no
	 *
	 * @param fechaFin String con la fecha fin de la ddbb
	 * @return true si la fecha de fin de suscripcion es posterior al dia de hoy
	 * @throws ParseException
	 */
	public static boolean checkFechaFin(String fechaFin) throws ParseException {

		Date fechaFinSubs = new SimpleDateFormat(Config.defaultDateFormat, Locale.ENGLISH).parse(fechaFin);

		if (System.currentTimeMillis() < fechaFinSubs.getTime()){
			return true;
		}
		return false;
	}

    /** Comprara si 2 fechas son iguales en dia, mes y año.
     *
     * @param timeInMillis01
     * @param timeInMillis02
     * @return true si las fechas son iguales. false si son distintas
     */
    public static boolean compareDateDMY(long timeInMillis01, long timeInMillis02){

		Date date1 = new Date(timeInMillis01);
		Calendar calendar01 = Calendar.getInstance();
		calendar01.setTime(date1);

		Date date2 = new Date(timeInMillis02);
		Calendar calendar02 = Calendar.getInstance();
		calendar02.setTime(date2);

		if (calendar01.get(Calendar.YEAR) != calendar02.get(Calendar.YEAR)){
			return false;
		}
		if (calendar01.get(Calendar.MONTH) != calendar02.get(Calendar.MONTH)){
			return false;
		}
		if (calendar01.get(Calendar.DAY_OF_MONTH) != calendar02.get(Calendar.DAY_OF_MONTH)){
			return false;
		}
		return true;
	}

    /** Convierte un String con una fecha en un String con la fecha en milisegundos
     *
     * @param fechaDDBB
     * @return String con la fecha en milisegundos
     * @throws ParseException
     */
    public static String ddbbDateToMillis(String fechaDDBB) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Config.defaultDateFormat, Locale.ENGLISH);
		Date dateLastUpdated = dateFormat.parse(fechaDDBB);
		Calendar calendarLastUpdate = Calendar.getInstance();
        calendarLastUpdate.setTime(dateLastUpdated);

        return String.valueOf(dateLastUpdated.getTime());
    }

    public static void printLog(String text){
        if (Config.log){
            Log.i(Config.tag, text);
        }
    }
}
