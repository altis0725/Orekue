package jp.magusa.orekue.android.status;

import jp.magusa.orekue.android.model.User;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UserDetailView extends WebView{
    
    public UserDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public UserDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public UserDetailView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

      int width = MeasureSpec.getSize(widthMeasureSpec);
      setMeasuredDimension(width, width*95/100);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//return super.onTouchEvent(event);
    	return true;
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
    
    public void showRaderChart(long study, long exercise, long communication,
    		long fashion, long society, long art){
        
        String url = "file:///android_asset/raderChart.html" +
        		"?p1=" + study + 
        		"&p2=" + exercise + 
        		"&p3=" + communication + 
        		"&p4=" + fashion +
        		"&p5=" + society +
        		"&p6=" + art;
        Log.d("orekue",url);
        loadUrl(url);
    }
    
    public void showRaderChart(User user){
    	showRaderChart(user.getParamStudy(),
				user.getParamExercise(),
				user.getParamCommunication(),
				user.getParamFashion(), user.getParamSociety(),
				user.getParamArt());
    }
 
}
