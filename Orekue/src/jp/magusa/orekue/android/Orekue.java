package jp.magusa.orekue.android;

import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.view.FontUtil;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import android.view.View;

public class Orekue extends Application{
	public static Orekue o;
	public static SharedPreferences sPref;
    public static Editor sEditor;
	public Orekue(){
		o = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		sPref = getSharedPreferences("orekue", MODE_PRIVATE);
		
        sEditor = sPref.edit();
        DataStore.setMyUserId(sPref.getLong("my_user_id", -1));
        FontUtil.loadFont("fonts/misaki_gothic.zip", o);
        
	}
	
	public static String getDeviceId(){
		return Secure.getString(o.getContentResolver(),
                Secure.ANDROID_ID);
	}
	
	public static void applyFont(View v){
		FontUtil.setFont2TextView(o, v);
	}
}
