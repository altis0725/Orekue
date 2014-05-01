package jp.magusa.orekue.android.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.maguro.vs.samon.orekue.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageLoader {

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler = new Handler();// handler to display images in UI thread
    final int noImageResId, notAvailableImageResId;
    final int REQUIRED_SIZE = 100;

    public ImageLoader(Context context, boolean sdcard, String folderName, int noImageResId, int notAvailableImageResId) {
        fileCache = new FileCache(context, sdcard, folderName);
        executorService = Executors.newFixedThreadPool(4);
        this.noImageResId = noImageResId;
        this.notAvailableImageResId = notAvailableImageResId;
    }
    public ImageLoader(Context context, boolean sdcard, String folderName) {
        this(context, sdcard, folderName, R.drawable.o_no_image, R.drawable.o_not_available);
    }

    public void displayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setImageResource(noImageResId);
            queuePhoto(url, imageView);
        }
    }
    
    /**if imageView never used after that*/
    public void displayImage1time(String url, ImageView imageView) {
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setImageResource(noImageResId);
            queuePhoto(url, imageView);
        }
    }
    
    public void removeImageView(ImageView im){
        imageViews.remove(im);
    }
    
    public void removeCacheAvatar(String url){
        Bitmap b = memoryCache.remove(url);
        Log.e("bitmap", "b="+b);
        File f = fileCache.getFile(url);
        if (f != null) f.delete();
    }

    private void queuePhoto(String username, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(username, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    public File getFile(String url){
        return fileCache.getFile(url);
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);
        //if (storeInSdcard && System.currentTimeMillis() - f.lastModified() > 24*3600*1000){//1 day
        //    f.delete();
        //} else {
            // from SD cache
            Bitmap b = decodeFile(f);
            if (b != null)
                return b;
        //}

        // from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        /*try {
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }//*/
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if ( 
                   (width_tmp / 2 < REQUIRED_SIZE 
                           || height_tmp / 2 < REQUIRED_SIZE))
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            /*if (width_tmp > 2048 || height_tmp > 2048){
                int w=2048, h=2048;
                if (width_tmp > 2048) h = height_tmp * 2048 / width_tmp;
                else w = width_tmp * 2048 / height_tmp; 
                Bitmap b = Bitmap.createScaledBitmap(bitmap, w, h, true);
                if (b != bitmap) bitmap.recycle();
                return b;
            }//*/
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;//*/
    }

    // Task for the queue
    private class PhotoToLoad {
        public String username;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            username = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.username);
                memoryCache.put(photoToLoad.username, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
    
    public void resetListView(){
        imageViews.clear();
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.username))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            } else {
                photoToLoad.imageView.setImageResource(notAvailableImageResId);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear(0);
    }
    
    public void clearCache(long lastModifiedTimeToNowMillis){
        memoryCache.clear();
        fileCache.clear(lastModifiedTimeToNowMillis);
    }
    
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
