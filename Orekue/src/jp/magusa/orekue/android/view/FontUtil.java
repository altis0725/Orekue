package jp.magusa.orekue.android.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class FontUtil {

	static private Typeface font = null;

	public static void loadFont(String fontFileName, Context context) {
		font = getFontFromZip(fontFileName, context);
	}

	public static void setFont2TextView(Context context, View root) {
		setFont2TextView(context, root, font);
	}

	/**
	 * View のフォントを指定する。　ViewGroupの子要素のフォントをすべて一致させる。
	 * 
	 * @param context
	 * @param root
	 * @param font
	 */
	private static void setFont2TextView(Context context, View root,
			Typeface fontName) {
		try {
			LinkedList<View> views = new LinkedList<View>();
			views.offer(root);
			while (views.peek() != null) {
				View child = views.poll();
//				Log.i("font", "" + child);
				if (child instanceof ListView) {
				} else if (child instanceof Spinner) {
				} else if (child instanceof TimePicker) {
				} else if (child instanceof DatePicker) {
				} else if (child instanceof ViewGroup) {
					ViewGroup viewGroup = (ViewGroup) child;
					for (int i = 0; i < viewGroup.getChildCount(); i++) {
						views.offer(viewGroup.getChildAt(i));
					}
				} else if (child instanceof TextView) {
					TextView textView = (TextView) child;
					textView.setTypeface(fontName);
					String newText = HalfToFill.HalfToFill(textView.getText().toString());
					textView.setText(newText);

				}
			}
		} catch (Exception e) {
			Log.e("FontUtil", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * zip圧縮されているフォントファイルをassetから解凍し、dataフォルダに格納する
	 * assetのファイルはある程度大きいとzip圧縮されていないとAndroidで認識してくれない
	 * 
	 * @param fontFileName
	 * @param context
	 * @return
	 */
	private static Typeface getFontFromZip(String fontFileName, Context context) {
		Typeface ret = null;
		File zipFile = null;
		try {
			AssetManager am = context.getAssets();
			InputStream is = am.open(fontFileName,
					AssetManager.ACCESS_STREAMING);

			ZipInputStream zis = new ZipInputStream(is);
			ZipEntry ze = zis.getNextEntry();
			if (ze != null) {
				zipFile = new File(context.getFilesDir(), ze.getName());

				// フォントがすでに解凍されていればなにもしない
				if (zipFile.exists()) {
					return Typeface.createFromFile(zipFile.getPath());
				}
				FileOutputStream fos = new FileOutputStream(zipFile, false);
				byte[] buf = new byte[1024];
				int size = 0;
				while ((size = zis.read(buf, 0, buf.length)) > -1) {
					fos.write(buf, 0, size);
				}
				fos.close();
				zis.closeEntry();
				ret = Typeface.createFromFile(zipFile.getPath());
			}
			zis.close();
		} catch (Exception e) {
			Log.e("FontUtil", "font extract fail", e);
			if (zipFile != null && zipFile.exists())
				zipFile.delete();
		}
		return ret;
	}

}