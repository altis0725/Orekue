package jp.magusa.orekue.android.status;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityView extends WebView{
    
    public ActivityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public ActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ActivityView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void init(){
        WebSettings webSettings = getSettings();
        if (webSettings == null) return;
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        //webSettings.setDefaultTextEncodingName("utf-8");
        
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//return super.onTouchEvent(event);
    	return true;
    }
    
    public void showGraph(ArrayList<List<Long>> params, ArrayList<String> dates){
    	String[] c = {"st","ex","co","fa","so","ar"};
    	String url = "file:///android_asset/lineChart.html?";
    	for(int i = 0; i < dates.size(); i++){
    		if(i == 0){
    			url += "tu0=" + dates.get(i) + "";
    		}else{
    			url += "&tu" + i + "=" + dates.get(i);
    		}
    	}
    	Log.e("size", ""+ params.size());
    	for(int i = 0; i < c.length; i++){
    		for(int j = 0; j < params.get(i).size(); j++){
    			url += "&" + c[i] + j + "=" + params.get(i).get(j);
    		}
    	}
    	Log.e("orekue",url);
        loadUrl(url);
    }
    
    public void showGraph(){
        //int size = 6;
        //int[] params = new int[size];
        int[] params = {15,40,15,20,25,30};
        String url = "file:///android_asset/lineChart.html?p1=" + params[0] + 
        		"&p2=" + params[1] + 
        		"&p3=" + params[2] + 
        		"&p4=" + params[3] +
        		"&p5=" + params[4] +
        		"&p6=" + params[5];
        Log.d("orekue",url);
        loadUrl(url);
    }
    
 
}
