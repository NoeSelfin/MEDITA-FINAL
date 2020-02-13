package org.simo.medita;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

public class AddFrames {
	Context ctx;
	AnimationDrawable loadingAnimation;
	public AddFrames(Context ctx,AnimationDrawable loadingAnimation){
		this.loadingAnimation = loadingAnimation;
        this.ctx = ctx;
	}
	
	public void addSplashFrames(){
		
		int duration = 30;	

		
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00001.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00002.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00003.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00004.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00005.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00006.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00007.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00008.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00009.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00010.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00011.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00012.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00013.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00014.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00015.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00016.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00017.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00018.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00019.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00020.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00021.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00022.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00023.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00024.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00025.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00026.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00027.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00028.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00029.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00030.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00031.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00032.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00033.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00034.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00035.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00036.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00037.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00038.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00039.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00040.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00041.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00042.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00043.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00044.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00045.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00046.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00047.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00048.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00049.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00050.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00051.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00052.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00053.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00054.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00055.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00056.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00057.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00058.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00059.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00060.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00061.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00062.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00063.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00064.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00065.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00066.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00067.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00068.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00069.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00070.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00071.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00072.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00073.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00074.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00075.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00076.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00077.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00078.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00079.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00080.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00081.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00082.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00083.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00084.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00085.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00086.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00087.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00088.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00089.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00090.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00091.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00092.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00093.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00094.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00095.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00096.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00097.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00098.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00099.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00100.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00101.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00102.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00103.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00104.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00105.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00106.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00107.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00108.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00109.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00110.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00111.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00112.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00113.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00114.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00115.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00116.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00117.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00118.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00119.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00120.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00121.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00122.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00123.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00124.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00125.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00126.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00127.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00128.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00129.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00130.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00131.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00132.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00133.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00134.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00135.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00136.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00137.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00138.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00139.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00140.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00141.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00142.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00143.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00144.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00145.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00146.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00147.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00148.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("splash/Textos intro_00149.png")), duration);

	}
	
public void addLoadingFrames(){
		
		int duration = 41;	
		
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00001.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00002.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00003.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00004.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00005.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00006.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00007.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00008.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00009.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00010.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00011.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00012.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00013.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00014.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00015.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00016.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00017.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00018.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00019.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00020.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00021.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00022.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00023.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00024.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00025.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00026.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00027.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00028.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00029.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00030.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00031.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00032.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00033.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00034.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00035.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00036.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00037.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00038.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00039.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00040.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00041.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00042.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00043.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00044.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00045.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00046.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00047.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00048.png")), duration);
        loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAsset("loading/loading_00049.png")), duration);
        
       
       
        

	}

public void addPlayerFrames(){
	
	int duration = 35;	
	/*
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00000.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00001.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00002.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00003.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00004.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00005.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00006.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00007.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00008.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00009.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00010.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00011.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00012.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00013.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00014.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00015.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00016.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00017.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00018.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00019.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00020.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00021.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00022.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00023.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00024.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00025.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00026.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00027.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00028.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00029.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00030.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00031.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00032.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00033.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00034.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00035.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00036.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00037.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00038.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00039.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00040.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00041.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00042.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00043.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00044.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00045.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00046.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00047.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00048.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00049.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00050.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00051.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00052.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00053.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00054.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00055.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00056.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00057.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00058.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00059.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00060.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00061.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00062.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00063.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00064.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00065.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00066.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00067.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00068.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00069.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00070.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00071.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00072.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00073.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00074.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00075.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00076.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00077.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00078.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00079.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00080.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00081.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00082.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00083.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00084.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00085.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00086.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00087.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00088.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("player/player_00089.png")), duration);
   */   

}

public void addSwitchOn(){
	
	int duration = 15;	
	
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00000.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00001.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00002.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00003.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00004.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00005.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00006.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00007.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00008.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00009.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00010.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00011.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_on/checkbox_00012.png")), duration);    

}
public void addSwitchOff(){	
	int duration = 15;	
	
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00013.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00014.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00015.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00016.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00017.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00018.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00019.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00020.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00021.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00022.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00023.png")), duration);
    loadingAnimation.addFrame(new BitmapDrawable(ctx.getResources(), loadBitmapFromAssetLoseless("switch_off/checkbox_00024.png")), duration);
    

}



	
	public AnimationDrawable getAnimation(){		
		return loadingAnimation;
	}
	public void startAnimation(){		
		loadingAnimation.start();
	}
	
	public Bitmap loadBitmapFromAssetLoseless(String file) {
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
	
	public Bitmap loadBitmapFromAsset(String file) {
		   Bitmap bm = null;
	        // load image
	        try {
	        	
	        	BitmapFactory.Options opts = new BitmapFactory.Options();
	        	opts.inSampleSize = 2;
	        	
	            // get input stream
	            InputStream ims = ctx.getAssets().open(file);
	            // load image as Drawable
	             bm =  BitmapFactory.decodeStream(ims,null,opts);
	        }
	        catch(IOException ex) {
	            return null;
	        }
	        return bm;
	 }

}
