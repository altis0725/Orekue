package jp.magusa.orekue.android.home;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.OTabAbstract;
import jp.magusa.orekue.android.home.TimelineAdapter.ItemDelegate;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OActivity;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.Pair;
import jp.magusa.orekue.android.model.Prefix;
import jp.magusa.orekue.android.model.Tag;
import jp.magusa.orekue.android.model.Title;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.status.UserDetailView;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeTab extends OTabAbstract implements OnClickListener, ItemDelegate {
	ViewGroup mContainerView;
	UserDetailView mGraph;
	Stack<View> mStack = new Stack<View>();
	Context context;
	TimelineAdapter mTimelineAdapter;
	

	public interface OnClickHomeBtnListener {
		void onHomeBtnClick();
	}

	private OnClickHomeBtnListener mOnClickHomeBtnListener;

	public void setOnClickHomeBtnListener(
			OnClickHomeBtnListener mOnClickHomeBtnListener) {
		this.mOnClickHomeBtnListener = mOnClickHomeBtnListener;
	}

	TextView mTvLevel, mTvUserName, mTvPrefix, mTvTitle, mTvlog, mTvGold;
	ListView listView;
	ImageView mIvImage;
	LinearLayout mLlayout;

	public HomeTab(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView() {
		if (mContainerView != null)
			return mContainerView;
		Log.d("HomeTab", "haha");
		mContainerView = new FrameLayout(mActMain);
		mContainerView.addView(getGraphView());
		return mContainerView;
	}

	public View getGraphView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.home_header, null);
		mGraph = (UserDetailView) v.findViewById(R.id.graphview);
		mLlayout = (LinearLayout)v.findViewById(R.id.layout_home_profile);
		mTvUserName = (TextView) v.findViewById(R.id.home_name);
		mTvLevel = (TextView) v.findViewById(R.id.home_level);
		mTvPrefix = (TextView) v.findViewById(R.id.home_pre);
		mTvTitle = (TextView) v.findViewById(R.id.home_job);
		mIvImage = (ImageView) v.findViewById(R.id.imageView1);
		mIvImage.setOnClickListener(this);
		mTvlog = (TextView) v.findViewById(R.id.home_log);
		mTvlog.setOnClickListener(this);
		
		applyFont(v);
		
		View home = mInflater.inflate(R.layout.home_graph, null);
		listView = (ListView) home.findViewById(R.id.list_dummy);
		listView.addHeaderView(v);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(mActMain.getResources()
				.getDimensionPixelSize(R.dimen.timeline_list_divider_height));
		mTimelineAdapter = new TimelineAdapter(context, 0, new ArrayList<Pair<User, OActivity>>());
		mTimelineAdapter.setDelegate(this);
		listView.setAdapter(mTimelineAdapter);
		final TimelineTask guTask = new TimelineTask();
		
		//new Handler().postDelayed(new Runnable() {
			
			//@Override
			//public void run() {
				guTask.execute();
			//}
		//}, 500);
		
		return home;
	}

	@Override
	public boolean canGoBack() {
		return !mStack.isEmpty();
	}

	@Override
	public void goBack() {
		mContainerView.removeAllViews();
		mContainerView.addView(mStack.pop());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_screen:
			mStack.add(mContainerView.getChildAt(0));
			mContainerView.removeAllViews();
			mContainerView.addView(getGraphView());
			break;
		case R.id.imageView1:
			if (mOnClickHomeBtnListener != null) {
				mOnClickHomeBtnListener.onHomeBtnClick();
			}
			break;
		case R.id.home_log:
			GetActivityTask gaTask = new GetActivityTask();
			gaTask.execute();
			break;
		default:
			break;
		}
	}

	private class TimelineTask extends OTask<Void, Void, Boolean> {
		Dialog pr;
		OResponse<User> resUser;
		OResponse<Title> resTitle;
		OResponse<Prefix> resPrefix;
		OResponse<List<Pair<User, OActivity>>> resActs;
		OResponse<List<Tag>> resTags;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<Boolean> do_in_background(Void... params) {
			OResponse<Boolean> res = new OResponse<Boolean>();
			res.setErrorCode(0);
			try {
				resUser = DataStore.getUser(DataStore.getMyUserId());
				if (!resUser.isSuccess()) {
					res.setErrorCode(resUser.getErrorCode());
					return res;
				}
				
				long titleId = resUser.data.getTitleId();
				resTitle = DataStore.getTitle(titleId);
				
				resPrefix = DataStore.getPrefix(resUser.data.getPrefixId());
				
				resActs = DataStore.getHomeTimeLine(10, 0);
				if (!resActs.isSuccess()) {
					res.setErrorCode(resActs.getErrorCode());
					return res;
				}
				
				resTags = DataStore.getAllTags();
				return res;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onFinish(int errorCode, String errorMessage, Boolean result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {
				mGraph.showRaderChart(resUser.data);
				mGraph.setInitialScale(1);
				mGraph.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
				mTvUserName.setText(resUser.data.getName());
				applyFont(mTvUserName);
				mTvLevel.setText(Long.toString(resUser.data.getParamLevel()));
				applyFont(mTvLevel);
				mTvTitle.setText(resTitle.data.getName());
				applyFont(mTvTitle);
				mTvPrefix.setText(resPrefix.data.getName());
				applyFont(mTvPrefix);
				
				User user = resUser.data;
				int avatar_resId = 0;
		        if (user.getIcon() != null){
		        	avatar_resId = context.getResources().getIdentifier(
		        			user.getIcon(), "drawable", context.getPackageName());
		        }else{
		        	avatar_resId = context.getResources().getIdentifier(
		        			"ic_launcher", "drawable", context.getPackageName());
		        }
		        mIvImage.setImageResource(avatar_resId);
		
				//Collections.reverse(resActs.data);
				mTimelineAdapter.clear();
				mTimelineAdapter.addAll(resActs.data);
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class GetActivityTask extends OTask<Void, Void, List<Pair<User, OActivity>>> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<List<Pair<User, OActivity>>> do_in_background(Void... params) {
			try {
				OResponse<List<Pair<User, OActivity>>> activities = DataStore.getHomeTimeLine(
						30, 0);
				Log.e("acvities", "activities" + activities);
				return activities;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onFinish(int errorCode, String errorMessage,
				List<Pair<User, OActivity>> result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				Log.d("HomeTab", "Responce results:" + result.size());
				mTimelineAdapter.clear();
				mTimelineAdapter.addAll(result);
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onRenkeiClicked(User item) {
		//show friend detail
		FriendDetailDialog dialog = new FriendDetailDialog(mActMain, item);
		dialog.show();
	}

	@Override
	public void reloadTab() {
		new TimelineTask().execute();
		
	}

}
