//    Rage Comic Maker for Android (c) Tamas Marki 2011-2013
//	  This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
package com.tmarki.comicmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PackHandler {
	public static final String ALL_THE_FACES =  "--ALL THE FACES--";
	private final String DEFAULT_COMIC_PACK = "Default pack";
	private Map<CharSequence, Vector<String>> imgs;
	
	public Map<CharSequence, Vector<String>> getBundles (AssetManager am) {
		imgs = new HashMap<CharSequence, Vector<String>>();
		imgs.put(ALL_THE_FACES, new Vector<String>());
		try {
			String[] nameList = am.list(DEFAULT_COMIC_PACK);
			for (String folder : nameList) {
				String[] subNameList = am.list(DEFAULT_COMIC_PACK + "/" + folder);
				Vector<String> fns = new Vector<String>();
				for (String fn : subNameList) {
					fns.add(fn);
				}
				imgs.put(folder, fns);
			}
		} catch (IOException e) {
			Log.d ("RAGE", e.getMessage());
			e.printStackTrace();
		}
		return imgs;
	}
	public Bitmap getDefaultPackDrawable (String folder, String file, int fixedHeight, AssetManager am) {
		if (am == null || imgs == null)
			return null;
		try {
			if (folder.equals(ALL_THE_FACES)) {
//				String[] nameList = am.list(DEFAULT_COMIC_PACK);
				folderloop:
				for (CharSequence ff : imgs.keySet()) {
//					String[] subNameList = am.list(DEFAULT_COMIC_PACK + "/" + ff);
					for (String fn : imgs.get(ff)) {
						if (fn.equals(file)) {
							folder = ff.toString();
							break folderloop;
						}
					}
				}
			}
	    	InputStream is;
			is = am.open(DEFAULT_COMIC_PACK + "/" + folder + "/" + file);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			if (bmp != null) {
				if (fixedHeight > 0) {
					Bitmap tmp = bmp;
					bmp = Bitmap.createScaledBitmap(bmp, fixedHeight, bmp.getWidth() * fixedHeight / tmp.getHeight(), true);
					tmp.recycle();
				}
				return bmp;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 public Bitmap decodeFile(File f){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        int scale = 1;
	        if (o.outHeight > ImageObject.maxImageHeight || o.outWidth > ImageObject.maxImageWidth) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(ImageObject.maxImageHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (Exception e) {
	    }
	    return b;
	}

	static public Bitmap decodeStream(InputStream f, int size){
	    Bitmap b = null;
	    try {
	        b = BitmapFactory.decodeStream(f);
	    } catch (Exception e) {
	    	Log.d ("RAGE", e.toString ());
    	} catch (OutOfMemoryError e) {
    		Log.d ("RAGE", e.toString ());
    	}
	    return b;
	}
	
}
