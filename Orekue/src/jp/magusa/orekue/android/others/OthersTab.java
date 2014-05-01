package jp.magusa.orekue.android.others;

import java.io.IOException;
import java.net.MalformedURLException;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.OTabAbstract;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OthersTab extends OTabAbstract implements OnClickListener {
    ViewGroup mContainerView, mTopView;
    TextView mTvUserInfo;
    EditText mEditUserId;

    public OthersTab(Activity context) {
        super(context);
    }

    @Override
    public View getView() {
        if (mContainerView != null)
            return mContainerView;
        mContainerView = (ViewGroup) mInflater.inflate(R.layout.others_tab, null);
        mTopView = (ViewGroup) mContainerView.findViewById(R.id.layout_top);
        mContainerView.findViewById(R.id.button_getuser).setOnClickListener(this);
        mTvUserInfo = (TextView) mContainerView.findViewById(R.id.textView_userInfo);
        mEditUserId = (EditText) mContainerView.findViewById(R.id.editText_userId);
        return mContainerView;
    }


    @Override
    public boolean canGoBack() {
        return false;
    }

    @Override
    public void goBack() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_getuser:
            long id = 0;
            try {
                id = Long.parseLong(mEditUserId.getText().toString());
            } catch (Exception e) {
                Toast.makeText(mActMain, "Enter a number", Toast.LENGTH_SHORT).show();
                return;
            }
            final long userId = id;
            new OTask<Void, Void, User>() {
                Dialog pr;
                protected void onPreExecute() {
                    pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
                    pr.setCanceledOnTouchOutside(false);
                };

                @Override
                protected OResponse<User>  do_in_background(Void... params) {
                    try {
                        OResponse<User> user = DataStore.getUser(userId);
                        Log.e("user", "user"+user);
                        return user;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onFinish(int errorCode, String errorMessage, User result) {
                    if (pr != null && pr.isShowing()) pr.dismiss();
                    if (errorCode == 0){//success
                        mTvUserInfo.setText("username = "+result.getName());
                    } else {
                        mTvUserInfo.setText("ERROR:"+errorMessage);
                    }
                }
                
            }.execute();
            break;

        default:
            break;
        }
    }

	@Override
	public void reloadTab() {
		// TODO Auto-generated method stub
		
	}

    
}
