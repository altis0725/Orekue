package jp.magusa.orekue.android.status;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;

@SuppressLint("NewApi")
public class AccountCustomLayout extends LinearLayout implements Checkable {
	 
    private RadioButton mRadioButton;
    Context context;

    public AccountCustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initialize();
    }
 
    public AccountCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }
 
    public AccountCustomLayout(Context context) {
        super(context);
        this.context = context;
        initialize();
    }
    
    public void setButton(RadioButton rb){
    	mRadioButton = rb;
    }
    private void initialize() {
        // レイアウトを追加する
        //addView(inflate(context, R.layout.account_released_title_item, null));
    	
        //mRadioButton = (RadioButton) findViewById(R.id.rbtn_account_setting);
        //Log.e("layout", "" + this.getChildCount());
    }
 
    @Override
    public boolean isChecked() {
        return mRadioButton.isChecked();
    }
 
    @Override
    public void setChecked(boolean checked) {
        // RadioButton の表示を切り替える
        mRadioButton.setChecked(checked);
    }
 
    @Override
    public void toggle() {
    }
 
}
