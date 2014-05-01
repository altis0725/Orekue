package jp.magusa.orekue.android.home;

import jp.maguro.vs.samon.orekue.R;
import jp.maguro.vs.samon.orekue.R.color;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.status.AvatarListAdapter;
import jp.magusa.orekue.android.status.ReleasedPrefixListAdapter;
import jp.magusa.orekue.android.status.ReleasedTitleListAdapter;
import jp.magusa.orekue.android.status.UserDetailView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendDetailDialog {
	Dialog mDialog;
	Activity act;

	UserDetailView mGraph;
	protected TextView mTvLevel, mTvUserName, mTvPrefix, mTvTitle, mTvMoney;
	protected TextView mTvStudy, mTvExercise, mTvCommunication, mTvFashion,
			mTvSociety, mTvArt;
	protected TextView mTvRTitle, mTvRPrefix;
	protected ListView mLvActivity, mLvRTitle;
	protected ImageView mIvTitle, mIvAvatar, mIvSAvatar, mIvRTitle;
	protected User user;
	protected AlertDialog alertDialog;
	protected ReleasedPrefixListAdapter mRPLAdapter;
	protected ReleasedTitleListAdapter mRTAdapter;
	protected AvatarListAdapter mALAdapter;

	public FriendDetailDialog(final Activity act, final User user) {
		this.act = act;
		this.user = user;
		Dialog dialog;
		dialog = new Dialog(act);
		mDialog = dialog;
		// mDialog.setContentView(R.layout.home_friend_detail);
		dialog.setTitle(null);
		// dialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(R.color.post_dialog_window_color));
		// dialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setTitleColor(color.post_dialog_window_color);
		dialog.setCanceledOnTouchOutside(false);
		// */
	}

	public void show() {
		View v = act.getLayoutInflater().inflate(R.layout.home_friend_detail,
				null);
		((TextView) v.findViewById(R.id.textView_username)).setText(user
				.getName());
		((TextView) v.findViewById(R.id.textView_level)).setText("Level: "
				+ user.getParamLevel());
		((TextView) v.findViewById(R.id.textView_prefix)).setText("Prefix: "
				+ user.getPrefixId());

		UserDetailView mGraph = (UserDetailView) v.findViewById(R.id.graphview);
		mGraph.showRaderChart(user);
		mGraph.setInitialScale(1);
		mGraph.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);

		v = getUserDetailView(user);
		mDialog.setContentView(v);
		Dialog dialog = mDialog;
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setTitleColor(color.post_dialog_window_color);
		dialog.show();
	}

	private View getUserDetailView(User result) {
		
    	ViewGroup v = (ViewGroup) act.getLayoutInflater().inflate(R.layout.status_tab, null);
        mGraph = (UserDetailView) v.findViewById(R.id.graphview);
        mTvMoney = (TextView) v.findViewById(R.id.textView_money);
        mTvLevel = (TextView) v.findViewById(R.id.textView_level);
        mTvUserName = (TextView) v.findViewById(R.id.textView_userName);
        mTvPrefix = (TextView) v.findViewById(R.id.textView_prefix);
        mTvTitle = (TextView) v.findViewById(R.id.textView_title);
        mTvStudy = (TextView) v.findViewById(R.id.textView_study);
        mTvExercise = (TextView) v.findViewById(R.id.textView_exercise);
        mTvCommunication = (TextView) v.findViewById(R.id.textView_communication);
        mTvFashion = (TextView) v.findViewById(R.id.textView_fashion);
        mTvSociety = (TextView) v.findViewById(R.id.textView_society);
        mTvArt = (TextView) v.findViewById(R.id.textView_art);
        mIvTitle = (ImageView) v.findViewById(R.id.imageView_title);
        mIvAvatar = (ImageView) v.findViewById(R.id.imageView_avatar);
        v.findViewById(R.id.btn_change_history).setVisibility(View.GONE);
        
        TextView btnSetting = (TextView) v.findViewById(R.id.btn_change_settings);
        btnSetting.setText(" OK ");
        btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
        
        mGraph.showRaderChart(result.getParamStudy(), result.getParamExercise(), 
        		result.getParamCommunication(), result.getParamFashion(), result.getParamSociety(),
        		result.getParamArt());
        mGraph.setInitialScale(1);
        mTvUserName.setText(result.getName());
        mTvLevel.setText("Ｌｖ." + result.getParamLevel());
        mTvStudy.setText("勉強 : " + result.getParamStudy());
        mTvExercise.setText("運動 : " + result.getParamExercise());
        mTvCommunication.setText("コミュ : " + result.getParamCommunication());
        mTvFashion.setText("ファッション :" + result.getParamFashion());
        mTvSociety.setText("社会勉強 :" + result.getParamSociety());
        mTvArt.setText("芸術 : " + result.getParamArt());
        mTvMoney.setText("おかね :" + result.getCoin() + "Ｇ");
        mTvPrefix.setText(result.getPrefix().getName());
        mTvTitle.setText(result.getTitle().getName());
        int title_resId = act.getResources().getIdentifier(
        		result.getTitle().getIconUrl(), "drawable", act.getPackageName());
        mIvTitle.setImageResource(title_resId);
        int avatar_resId = 0;
        if (result.getIcon() != null){
        	avatar_resId = act.getResources().getIdentifier(
        			result.getIcon(), "drawable", act.getPackageName());
        }else{
        	avatar_resId = act.getResources().getIdentifier(
        			"ic_launcher", "drawable", act.getPackageName());
        }
        mIvAvatar.setImageResource(avatar_resId);
        Orekue.applyFont(v);
        return v;
	}
}
