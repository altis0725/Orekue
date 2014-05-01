package jp.magusa.orekue.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public abstract class OTabAbstract {
    protected LayoutInflater mInflater;
    protected Activity mActMain;

    public OTabAbstract(Activity context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActMain = context;
    };

    public abstract View getView();

    public abstract boolean canGoBack();

    public abstract void goBack();
    
    public void onResume(){
        
    }
    
    public void onPause(){
        
    }
    
    public void onNewIntent(Intent intent) {
    }
    
    public void applyFont(View v){
    	Orekue.applyFont(v);
    }
    
    public void refresh(){
    	
    }
    
    
    
    public abstract void reloadTab();
    
}
