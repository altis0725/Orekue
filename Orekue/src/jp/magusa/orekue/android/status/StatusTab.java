package jp.magusa.orekue.android.status;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.OTabAbstract;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OActivity;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.Prefix;
import jp.magusa.orekue.android.model.Title;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusTab extends OTabAbstract implements OnClickListener {
	ViewGroup mContainerView;
	UserDetailView mGraph;
	ActivityView oGraph;
	Stack<View> mStack = new Stack<View>();
	final private String[] avatarList = { "ic_launcher", "avatar_1",
			"avatar_2", "avatar_3", "avatar_4" };
	String chooseAvatarName = null;

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

	private final int USER_DETAIL_VIEW_ID = -10;
	private final int ACTIVITY_VIEW_ID = -11;
	private final int ACCOUNT_SETTING_VIEW_ID = -13;

	public StatusTab(Activity context) {
		super(context);
	}

	@Override
	public View getView() {
		if (mContainerView != null) {
			//mContainerView.removeAllViews();
			//mStack.clear();
			//mContainerView.addView(getUserDetailView());
			return mContainerView;
		}
		mContainerView = new FrameLayout(mActMain);
		mStack.add(getUserDetailView());
		mContainerView.addView(getUserDetailView());
		return mContainerView;
	}

	private View getUserDetailView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.status_tab, null);
		mGraph = (UserDetailView) v.findViewById(R.id.graphview);
		mTvMoney = (TextView) v.findViewById(R.id.textView_money);
		mTvLevel = (TextView) v.findViewById(R.id.textView_level);
		mTvUserName = (TextView) v.findViewById(R.id.textView_userName);
		mTvPrefix = (TextView) v.findViewById(R.id.textView_prefix);
		mTvTitle = (TextView) v.findViewById(R.id.textView_title);
		mTvStudy = (TextView) v.findViewById(R.id.textView_study);
		mTvExercise = (TextView) v.findViewById(R.id.textView_exercise);
		mTvCommunication = (TextView) v
				.findViewById(R.id.textView_communication);
		mTvFashion = (TextView) v.findViewById(R.id.textView_fashion);
		mTvSociety = (TextView) v.findViewById(R.id.textView_society);
		mTvArt = (TextView) v.findViewById(R.id.textView_art);
		mIvTitle = (ImageView) v.findViewById(R.id.imageView_title);
		mIvAvatar = (ImageView) v.findViewById(R.id.imageView_avatar);
		v.findViewById(R.id.btn_change_settings).setOnClickListener(this);
		v.findViewById(R.id.btn_change_history).setOnClickListener(this);
		GetUserTask guTask = new GetUserTask(v);
		guTask.execute();
		v.setId(USER_DETAIL_VIEW_ID);
		return v;
	}

	private View getActivityView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.history_tab, null);
		applyFont(v);
		oGraph = (ActivityView) v.findViewById(R.id.graphview);
		v.findViewById(R.id.btn_return).setOnClickListener(this);
		oGraph.setInitialScale(1);
		// oGraph.showGraph();
		mLvActivity = (ListView) v.findViewById(R.id.listView_Activity);
		GetActivityTask gaTask = new GetActivityTask();
		gaTask.execute();
		v.setId(ACTIVITY_VIEW_ID);
		return v;
	}

	private View accountSettingView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(
				R.layout.account_setting_tab, null);
		mTvRTitle = (TextView) v.findViewById(R.id.textView_released_title);
		mTvRPrefix = (TextView) v.findViewById(R.id.textView_released_prefix);
		mIvSAvatar = (ImageView) v.findViewById(R.id.imageView_setting_avatar);
		mIvRTitle = (ImageView) v.findViewById(R.id.imageView_released_title);
		mTvRPrefix.setText(user.getPrefix().getName());
		mTvRTitle.setText(user.getTitle().getName());
		int title_resId = mActMain.getResources().getIdentifier(
				user.getTitle().getIconUrl(), "drawable",
				mActMain.getPackageName());
		mIvRTitle.setImageResource(title_resId);
		int avatar_resId = 0;
		if (user.getIcon() != null) {
			avatar_resId = mActMain.getResources().getIdentifier(
					user.getIcon(), "drawable", mActMain.getPackageName());
		} else {
			avatar_resId = mActMain.getResources().getIdentifier("ic_launcher",
					"drawable", mActMain.getPackageName());
		}
		mIvSAvatar.setImageResource(avatar_resId);
		applyFont(v);
		v.findViewById(R.id.btn_change_avatar).setOnClickListener(this);
		v.findViewById(R.id.btn_change_prefix).setOnClickListener(this);
		v.findViewById(R.id.btn_change_title).setOnClickListener(this);
		v.findViewById(R.id.btn_return).setOnClickListener(this);
		v.findViewById(R.id.btn_enter).setOnClickListener(this);

		v.setId(ACCOUNT_SETTING_VIEW_ID);
		return v;
	}

	private class GetUserTask extends OTask<Void, Void, User> {
		Dialog pr;
		ViewGroup viewGroup;

		private GetUserTask(ViewGroup v) {
			viewGroup = v;
		}

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<User> do_in_background(Void... params) {
			try {
				OResponse<User> user = DataStore.getUser(DataStore
						.getMyUserId());
				// Log.e("user", "user"+user.data.getParamArt());
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
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				// mGraph.setPadding(0, 0, 0, 0);
				user = result;
				mGraph.showRaderChart(result);
				mGraph.setInitialScale(1);
				mTvUserName.setText(result.getName());
				mTvLevel.setText("Ｌｖ." + result.getParamLevel());
				mTvStudy.setText("勉強 : " + result.getParamStudy());
				mTvExercise.setText("運動 : " + result.getParamExercise());
				mTvCommunication.setText("コミュ : "
						+ result.getParamCommunication());
				mTvFashion.setText("ファッション :" + result.getParamFashion());
				mTvSociety.setText("社会勉強 :" + result.getParamSociety());
				mTvArt.setText("芸術 : " + result.getParamArt());
				mTvMoney.setText("おかね :" + result.getCoin() + "Ｇ");
				mTvPrefix.setText(result.getPrefix().getName());
				mTvTitle.setText(result.getTitle().getName());
				int title_resId = mActMain.getResources().getIdentifier(
						result.getTitle().getIconUrl(), "drawable",
						mActMain.getPackageName());
				mIvTitle.setImageResource(title_resId);
				int avatar_resId = 0;
				if (result.getIcon() != null) {
					avatar_resId = mActMain.getResources().getIdentifier(
							result.getIcon(), "drawable",
							mActMain.getPackageName());
				} else {
					avatar_resId = mActMain.getResources().getIdentifier(
							"ic_launcher", "drawable",
							mActMain.getPackageName());
				}
				mIvAvatar.setImageResource(avatar_resId);
				applyFont(viewGroup);
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class GetActivityTask extends OTask<Void, Void, List<OActivity>> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<List<OActivity>> do_in_background(Void... params) {
			try {
				OResponse<List<OActivity>> activities = DataStore
						.getActivities(10, 0);
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
				List<OActivity> result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				//Collections.reverse(result);
				Log.e("activiteis", "" + result);
				mLvActivity
						.setAdapter(new HistoryListAdapter(mActMain, result));
				ArrayList<List<Long>> h_params = getParamHistory(result);
				ArrayList<String> dates = getDates(result);
				oGraph.showGraph(h_params, dates);
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class GetReleasedPrefixTask extends OTask<Void, Void, List<Prefix>> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<List<Prefix>> do_in_background(Void... params) {
			try {
				OResponse<List<Prefix>> prefixes = DataStore
						.getReleasedPrefix();
				return prefixes;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onFinish(int errorCode, String errorMessage,
				List<Prefix> result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				View v = mInflater.inflate(R.layout.account_dialog_title, null);
				applyFont(v);

				// アダプターに入れるListデータ
				List<Prefix> data = new ArrayList<Prefix>();
				int selected = 0;
				for (int i = 0; i < result.size(); i++) {
					data.add(result.get(i));
					if (result.get(i).getName().equals(mTvRPrefix.getText())) {
						// mSpPrefix.setSelection(i);
						// Log.e("status", result.get(i).getName() +
						// mTvRPrefix.getText());
						selected = i;
					}
				}

				// ArrayAdapterの初期化
				mRPLAdapter = new ReleasedPrefixListAdapter(mActMain,
						R.layout.account_released_prefix_item, data);

				// AlertDialogで選択肢を表示
				AlertDialog.Builder builder = new AlertDialog.Builder(mActMain);
				builder.setCustomTitle(v);
				builder.setSingleChoiceItems(mRPLAdapter, selected,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								user.setPrefixId(mRPLAdapter.getItem(which)
										.getId());
								mTvRPrefix.setText(mRPLAdapter.getItem(which)
										.getName());
								alertDialog.dismiss();
							}
						});
				alertDialog = builder.create();
				alertDialog.show();
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void openAvatarListDialog() {
		View v = mInflater.inflate(R.layout.account_dialog_title, null);
		applyFont(v);

		// アダプターに入れるListデータ
		List<String> data = new ArrayList<String>();
		int selected = 0;
		for (int i = 0; i < avatarList.length; i++) {
			data.add(avatarList[i]);
			if (avatarList[i].equals(user.getIcon())) {
				selected = i;
			}
		}
		// ArrayAdapterの初期化
		mALAdapter = new AvatarListAdapter(mActMain,
				R.layout.account_avatar_item, data);

		// AlertDialogで選択肢を表示
		AlertDialog.Builder builder = new AlertDialog.Builder(mActMain);
		builder.setCustomTitle(v);
		builder.setSingleChoiceItems(mALAdapter, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						chooseAvatarName = mALAdapter.getItem(which);
						int avatar_resId = mActMain.getResources()
								.getIdentifier(mALAdapter.getItem(which),
										"drawable", mActMain.getPackageName());
						mIvSAvatar.setImageResource(avatar_resId);
						alertDialog.dismiss();
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}

	private ArrayList<String> getDates(List<OActivity> result) {
		ArrayList<String> dates = new ArrayList<String>();
		//Collections.reverse(result);
		if (result.size() == 0) {
			List<Integer> createTime = getCalendar(user.getTimeStamp());
			List<Integer> now = getCalendar(System.currentTimeMillis());
			dates = dateList2String(createTime, now);
		} else if (result.size() == 1) {
			List<Integer> createTime = getCalendar(user.getTimeStamp());
			List<Integer> actTime = getCalendar(result.get(0).getDate());
			dates = dateList2String(createTime, actTime);
		} else {
			List<Integer> afterTime = getCalendar(result.get(0).getDate());
			List<Integer> beforeTime = getCalendar(result.get(result.size() - 1)
					.getDate());
			dates = dateList2String(beforeTime, afterTime);
		}
		return dates;
	}

	private ArrayList<String> dateList2String(List<Integer> before,
			List<Integer> after) {
		ArrayList<String> dates = new ArrayList<String>();
		if (before.get(0) != after.get(0) || before.get(1) != after.get(1)) {
			dates.add(before.get(4) + "year" + changeDateFormat(before.get(0)) + "month" 
					+ changeDateFormat(before.get(1)) + "day");
			dates.add(after.get(4) + "year" + changeDateFormat(after.get(0)) + "month" 
					+ changeDateFormat(after.get(1)) + "day");
		} else if (before.get(2) != after.get(2)) {
			dates.add(before.get(4) + "year" + changeDateFormat(before.get(0)) + "month"
					+ changeDateFormat(before.get(1)) + "day"
					+ changeDateFormat(before.get(2)) + "hour");
			dates.add(after.get(4) + "year" + changeDateFormat(after.get(0)) + "month"
					+ changeDateFormat(after.get(1)) + "day" 
					+ changeDateFormat(after.get(2)) + "hour");
		} else {
			dates.add(before.get(4) + "year" + changeDateFormat(before.get(0)) + "month"
					+ changeDateFormat(before.get(1)) + "day" 
					+ changeDateFormat(before.get(2)) + "hour"
					+ changeDateFormat(before.get(3)) + "min");
			dates.add(after.get(4) + "year" + changeDateFormat(after.get(0)) + "month"
					+ changeDateFormat(after.get(1)) + "day" 
					+ changeDateFormat(after.get(2)) + "hour"
					+ changeDateFormat(after.get(3)) + "min");
		}
		return dates;
	}

	private List<Integer> getCalendar(long time) {
		List<Integer> dlist = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		dlist.add(cal.get(Calendar.MONTH) + 1);
		dlist.add(cal.get(Calendar.DATE));
		dlist.add(cal.get(Calendar.HOUR_OF_DAY));
		dlist.add(cal.get(Calendar.MINUTE));
		dlist.add(cal.get(Calendar.YEAR));
		return dlist;
	}
	
	private String changeDateFormat(int date){
		String d = String.format("%02d", date);
		return d;
	}

	private ArrayList<List<Long>> getParamHistory(List<OActivity> activities) {
		ArrayList<List<Long>> histories = new ArrayList<List<Long>>(6);
		ArrayList<Long> h_study = new ArrayList<Long>(), h_exe = new ArrayList<Long>(), h_commu = new ArrayList<Long>(), h_fashion = new ArrayList<Long>(), h_society = new ArrayList<Long>(), h_art = new ArrayList<Long>();

		long pre_study = 0L, pre_exe = 0L, pre_commu = 0L, pre_fashion = 0L, pre_society = 0L, pre_art = 0L;

		if (activities.size() == 0) {
			h_study.add(0L);
			h_exe.add(0L);
			h_commu.add(0L);
			h_fashion.add(0L);
			h_society.add(0L);
			h_art.add(0L);
		}
		h_study.add(0L);
		h_exe.add(0L);
		h_commu.add(0L);
		h_fashion.add(0L);
		h_society.add(0L);
		h_art.add(0L);
		//Collections.reverse(activities);
		//for (int i = 0; i < activities.size(); i++) {
		for (int i = activities.size()-1; i >= 0; i--) {
			OActivity oa = activities.get(i);
			h_study.add(oa.getStudyIncrement() + pre_study);
			h_exe.add(oa.getExerciseIncrement() + pre_exe);
			h_commu.add(oa.getCommunicationIncrement() + pre_commu);
			h_fashion.add(oa.getFashionIncrement() + pre_fashion);
			h_society.add(oa.getSocietyIncrement() + pre_society);
			h_art.add(oa.getArtIncrement() + pre_art);

			pre_study = oa.getStudyIncrement() + pre_study;
			pre_exe = oa.getExerciseIncrement() + pre_exe;
			pre_commu = oa.getCommunicationIncrement() + pre_commu;
			pre_fashion = oa.getFashionIncrement() + pre_fashion;
			pre_society = oa.getSocietyIncrement() + pre_society;
			pre_art = oa.getArtIncrement() + pre_art;
		}

		histories.add(h_study);
		histories.add(h_exe);
		histories.add(h_commu);
		histories.add(h_fashion);
		histories.add(h_society);
		histories.add(h_art);

		return histories;
	}

	private class GetReleasedTitleTask extends OTask<Void, Void, List<Title>> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<List<Title>> do_in_background(Void... params) {
			try {
				OResponse<List<Title>> titles = DataStore.getReleasedTitle();
				return titles;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onFinish(int errorCode, String errorMessage,
				List<Title> result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				View v = mInflater.inflate(R.layout.account_dialog_title, null);
				applyFont(v);

				// アダプターに入れるListデータ
				List<Title> data = new ArrayList<Title>();
				int selected = 0;
				for (int i = 0; i < result.size(); i++) {
					data.add(result.get(i));
					if (result.get(i).getName().equals(mTvRTitle.getText())) {
						selected = i;
					}
				}

				// ArrayAdapterの初期化
				mRTAdapter = new ReleasedTitleListAdapter(mActMain,
						R.layout.account_released_title_item, data);

				// AlertDialogで選択肢を表示
				AlertDialog.Builder builder = new AlertDialog.Builder(mActMain);
				builder.setCustomTitle(v);
				builder.setSingleChoiceItems(mRTAdapter, selected,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								user.setTitleId(mRTAdapter.getItem(which)
										.getId());
								mTvRTitle.setText(mRTAdapter.getItem(which)
										.getName());
								int title_resId = mActMain.getResources()
										.getIdentifier(
												mRTAdapter.getItem(which)
														.getIconUrl(),
												"drawable",
												mActMain.getPackageName());
								mIvRTitle.setImageResource(title_resId);
								alertDialog.dismiss();
							}
						});
				alertDialog = builder.create();
				alertDialog.show();
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class updateUserStatusTask extends OTask<Void, Void, User> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<User> do_in_background(Void... params) {
			try {
				OResponse<User> u = DataStore.sendUserStatus(user);
				return u;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onFinish(int errorCode, String errorMessage, User result) {
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {// success
				Toast.makeText(mActMain, "更新しました", Toast.LENGTH_SHORT).show();
				reloadTab();
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
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
		case R.id.btn_change_history:
			mStack.add(mContainerView.getChildAt(0));
			mContainerView.removeAllViews();
			mContainerView.addView(getActivityView());
			break;
		case R.id.btn_return:
			mContainerView.removeAllViews();
			mStack.pop();
			mContainerView.addView(getUserDetailView());
			break;
		case R.id.btn_change_settings:
			mStack.add(mContainerView.getChildAt(0));
			mContainerView.removeAllViews();
			mContainerView.addView(accountSettingView());
			break;
		case R.id.btn_change_avatar:
			openAvatarListDialog();
			break;
		case R.id.btn_change_prefix:
			GetReleasedPrefixTask grpTask = new GetReleasedPrefixTask();
			grpTask.execute();
			break;
		case R.id.btn_change_title:
			GetReleasedTitleTask grtTask = new GetReleasedTitleTask();
			grtTask.execute();
			break;
		case R.id.btn_enter:
			Log.e("enter", "enter");
			user.setIcon(chooseAvatarName);
			updateUserStatusTask usTask = new updateUserStatusTask();
			usTask.execute();
		default:
			break;
		}
	}

	@Override
	public void reloadTab() {

		mContainerView.removeAllViews();
		mStack.clear();
		mContainerView.addView(getUserDetailView());
		
//		View current = mStack.pop();
//		View reloadingView = null;
//		switch (current.getId()) {
//		case USER_DETAIL_VIEW_ID:
//			reloadingView = getUserDetailView();
//			break;
//		case ACTIVITY_VIEW_ID:
//			reloadingView = getActivityView();
//			break;
//		case ACCOUNT_SETTING_VIEW_ID:
//			reloadingView = accountSettingView();
//			break;
//		default:
//			break;
//		}
//		mContainerView.addView(reloadingView);
//		mStack.push(reloadingView);
		

	}

}
