package jp.magusa.orekue.android.imageloader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context, boolean sdcard, String folderName) {
        if (!sdcard)
            cacheDir = context.getCacheDir();
        else {
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED))
                cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),
                        folderName);
            else
                cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = null;
        try {
            filename = "rr_" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            filename = "" + url.hashCode();
        }
        File f = new File(cacheDir, filename);
        return f;
    }
    
    public String getFileName(String url) {
        String filename = null;
        try {
            filename = "rr_" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            filename = "" + url.hashCode();
        }
        return cacheDir.getAbsolutePath()+File.pathSeparator+filename;
    }

    public void clear(long lastModifiedTimeToNowMillis) {
        File[] files = cacheDir.listFiles();
        long cur = System.currentTimeMillis();
        if (files == null)
            return;
        for (File f : files) {
            if (cur - f.lastModified() > lastModifiedTimeToNowMillis)
                f.delete();
        }
    }

}