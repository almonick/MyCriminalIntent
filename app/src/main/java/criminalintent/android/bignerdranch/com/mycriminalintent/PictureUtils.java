package criminalintent.android.bignerdranch.com.mycriminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.util.StringTokenizer;

/**
 * Created by Bender on 15/09/2015.
 */
public class PictureUtils {
	public  static Bitmap scaleBitmap(String path, int destWidth, int destHeight){
		Log.d("","### scaleBitmap " + path + " " + destWidth + " X " + destHeight);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		Log.d("","### scaleBitmap srcWidth" + srcWidth + "  XX " + srcHeight );
		int sampleSize = 1;
		if(srcWidth > destWidth || srcHeight > destHeight){
			if(srcWidth < srcHeight){
				sampleSize = Math.round(srcWidth / destWidth);
			}else{
				sampleSize = Math.round(srcHeight / destHeight);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		Log.d("","### scaleBitmap sampleSize" + sampleSize );
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getScaleBitmap(String path, Activity activity){
		Point point = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(point);
		return scaleBitmap(path, point.x, point.y);
	}
}
