package jp.magusa.orekue.android.friends;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.OTabAbstract;
import jp.magusa.orekue.android.friends.FriendListAdapter.RemoveFriendListener;
import jp.magusa.orekue.android.home.FriendDetailDialog;
import jp.magusa.orekue.android.model.Category;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.view.OArrayAdapter;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FriendsTab extends OTabAbstract implements OnClickListener,
		OnNdefPushCompleteCallback, CreateNdefMessageCallback {
	ViewGroup mContainerView;
	private static final String TAG = "Beam";
	private static final int MESSAGE_SENT = 1;
	NfcAdapter mNfcAdapter;
	FrameLayout mFrame;

	MakeFriendsView makeFriendsView;
	PendingIntent pendingIntent;
	IntentFilter ndef;
	String[][] techListsArray = new String[][] { new String[] { NfcF.class
			.getName() } };

	public FriendsTab(Activity context) {
		super(context);
	}

	@Override
	public View getView() {
		if (mContainerView != null)
			return mContainerView;
		mContainerView = new FrameLayout(mActMain);
		mContainerView.addView(getFriendsTopView());
		// mContainerView.addView(getExchangeView());
		// mContainerView.addView(new FriendsExchangeView(mActMain));
		return mContainerView;
	}

	public View getFriendsTopView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.friends_top, null);
		v.findViewById(R.id.friends_button_ranking).setOnClickListener(this);
		v.findViewById(R.id.friends_button_friendlist).setOnClickListener(this);
		v.findViewById(R.id.friends_button_exchange).setOnClickListener(this);
		mFrame = (FrameLayout) v.findViewById(R.id.friends_frame);
		applyFont(v);
		return v;
	}

	public View getRankingView() {
		return new RankingView(mActMain, null);
	}

	public View getFriendlistView() {
		return new FriendsListView(mActMain, null);
	}

	public View getMakeFriendView() {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(mActMain);
		if (mNfcAdapter != null) {
			mNfcAdapter.setOnNdefPushCompleteCallback(this, mActMain);
		}
		makeFriendsView = new MakeFriendsView(mActMain, null);
		return makeFriendsView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friends_button_ranking:
			mFrame.removeAllViews();
			mFrame.addView(getRankingView());
			break;
		case R.id.friends_button_friendlist:
			mFrame.removeAllViews();
			mFrame.addView(getFriendlistView());
			break;
		case R.id.friends_button_exchange:
			mFrame.removeAllViews();
			mFrame.addView(getMakeFriendView());
			break;
		default:
			break;
		}
	}

	// /////////////////////////////
	// inner view class
	// /////////////////////////////
	private class FriendsListView extends LinearLayout {
		ViewGroup layout;
		ListView listView;
		FriendListAdapter flAdapter;

		public FriendsListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			layout = (ViewGroup) mInflater.inflate(R.layout.friends_friendlist,
					this);
			listView = (ListView) layout
					.findViewById(R.id.friends_friendlist_listview);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					User user = flAdapter.getUserAt(position);
					ShowFrinedDetail(user);
				}
			});

			new GetFriendsListTask().execute();
		}

		protected class GetFriendsListTask extends
				OTask<Void, Void, List<User>> implements RemoveFriendListener {
			Dialog pr;

			protected void onPreExecute() {
				pr = OProgressDialog
						.show(mActMain, "Loading", "Please wait...");
				pr.setCanceledOnTouchOutside(false);
			};

			@Override
			protected OResponse<List<User>> do_in_background(Void... params) {
				try {
					OResponse<List<User>> friendsList = DataStore.getFriends();
					Log.e("friendsList", friendsList.data.size() + "人");
					return friendsList;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(int errorCode, String errorMessage,
					List<User> result) {
				if (pr != null && pr.isShowing())
					pr.dismiss();
				if (errorCode == 0) {// success
					// for(int i = 0; i < 5; i++){
					// User user = new User();
					// user.setName("name");
					// result.add(user);
					// }

					flAdapter = new FriendListAdapter(mActMain,
							R.layout.friends_friendlist_item, result);
					flAdapter.setOnRemoveFriendListener(this);

					listView.setAdapter(flAdapter);
				} else {
					// mTvUserInfo.setText("ERROR:"+errorMessage);
					Toast.makeText(mActMain, "ERROR:" + errorMessage,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onRemoveFriend(final User friendInfo) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActMain);
				builder.setMessage(friendInfo.getName()+" を友達リストから削除しますか？");
				builder.setTitle(null);
				builder.setNegativeButton("キャンセル", null);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new RemoveFriendTask().execute(friendInfo);
					}
				});
				builder.show();
			}
		}

		private class RemoveFriendTask extends OTask<User, Void, Boolean> {
			Dialog pr;

			protected void onPreExecute() {
				pr = OProgressDialog
						.show(mActMain, "Loading", "Please wait...");
				pr.setCanceledOnTouchOutside(false);
			};

			@Override
			protected OResponse<Boolean> do_in_background(User... params) {
				try {
					return DataStore.removeFriend(params[0].getId());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(int errorCode, String errorMessage,
					Boolean result) {
				if (pr != null && pr.isShowing())
					pr.dismiss();
				if (errorCode == 0) {// success
					Toast.makeText(mActMain, "削除に成功しました", Toast.LENGTH_LONG).show();
					new GetFriendsListTask().execute();
				} else {
					Toast.makeText(mActMain, "ERROR:" + errorMessage,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private class RankingView extends LinearLayout {
		ViewGroup layout;
		ListView listView;
		Spinner spinner;
		RankingAdapter rAdapter;

		public RankingView(Context context, AttributeSet attrs) {
			super(context, attrs);
			layout = (ViewGroup) mInflater.inflate(R.layout.friends_ranking,
					this);
			listView = (ListView) layout
					.findViewById(R.id.friends_ranking_listview);
			spinner = (Spinner) layout
					.findViewById(R.id.friends_ranking_spinner);
			listView.setAdapter(rAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					User user = rAdapter.getUserAt(position);
					ShowFrinedDetail(user);
				}
			});

			List<Category> clist = DataStore.getCategories();
			ArrayAdapter<Category> categolyAdapter = new OArrayAdapter<Category>(
					mActMain, android.R.layout.simple_list_item_1, clist);
			spinner.setAdapter(categolyAdapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					new GetRankingTask(position + 1).execute();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			// spinner.setFocusableInTouchMode(true);
			// spinner.setFocusable(true);
		}

		private class GetRankingTask extends OTask<Void, Void, List<User>> {
			long categoryId;
			Dialog pr;

			protected void onPreExecute() {
				pr = OProgressDialog
						.show(mActMain, "Loading", "Please wait...");
				pr.setCanceledOnTouchOutside(false);
			};

			public GetRankingTask(long categoryId) {
				this.categoryId = categoryId;
			}

			@Override
			protected OResponse<List<User>> do_in_background(Void... params) {
				try {
					OResponse<List<User>> rankingList = DataStore
							.getRanking(categoryId);
					return rankingList;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(int errorCode, String errorMessage,
					List<User> result) {
				if (pr != null && pr.isShowing())
					pr.dismiss();
				if (errorCode == 0) {// success
					rAdapter = new RankingAdapter(mActMain,
							android.R.layout.simple_list_item_1, result,
							categoryId);
					listView.setAdapter(rAdapter);
				} else {
					// mTvUserInfo.setText("ERROR:"+errorMessage);
					Toast.makeText(mActMain, "ERROR:" + errorMessage,
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}

	private class MakeFriendsView extends LinearLayout {
		ViewGroup layout;
		TextView mInfoText;
		Button mBtnBeam;
		Button mBtnBeamCancel;

		public MakeFriendsView(Context context, AttributeSet attrs) {
			super(context, attrs);
			layout = (ViewGroup) mInflater.inflate(R.layout.friends_exchange,
					this);
			mInfoText = (TextView) layout
					.findViewById(R.id.friends_makeFriendInfoText);
			mBtnBeam = (Button) layout.findViewById(R.id.btn_beam);
			mBtnBeamCancel = (Button) layout.findViewById(R.id.btn_beamCancel);
			mBtnBeamCancel.setVisibility(View.GONE);
			if (mNfcAdapter == null) {
				mInfoText.setText("友達通信はこの端末では利用できません。");
				mBtnBeam.setVisibility(View.GONE);
			} else if (!mNfcAdapter.isEnabled()) {
				mInfoText.setText("NFCを有効化してください。");
				mBtnBeam.setVisibility(View.GONE);
			} else {
				mBtnBeam.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mInfoText.setText("他の端末を近づけてください");
						findFriend();
						mBtnBeam.setVisibility(View.GONE);
						mBtnBeamCancel.setVisibility(View.VISIBLE);
					}
				});
				mBtnBeamCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						init();
					}
				});

				pendingIntent = PendingIntent.getActivity(mActMain, 0,
						new Intent(mActMain, mActMain.getClass())
								.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
				ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
				try {
					ndef.addDataType("application/titech.orekue");
				} catch (MalformedMimeTypeException e) {
					throw new RuntimeException("fail", e);
				}
				mNfcAdapter.enableForegroundDispatch(mActMain, pendingIntent,
						new IntentFilter[] { ndef, }, techListsArray);
			}
			applyFont(this);
		}

		public void makeFriends(long friendUserId) {
			// 　ここでasyncTaskでDataStore.makeFirendsする
			new MakeFriendsTask().execute(friendUserId);
		}

		private void init() {
			cancelFindFriend();
			mInfoText.setText("友達登録するには送信ボタンを押してください。この状態で受信も出来ます。");
			mBtnBeam.setVisibility(View.VISIBLE);
			mBtnBeamCancel.setVisibility(View.GONE);
		}

		private class MakeFriendsTask extends OTask<Long, Void, Void> {
			Dialog pr;
			int taskResultCode;
			final int SUCCESS_MAKE_FRIEND = 0;
			final int ALLREADY_FRIEND = 1;
			final int UNKNOWN_ERROR = 2;
			OResponse<User> getUserRes;

			protected void onPreExecute() {
				pr = OProgressDialog
						.show(mActMain, "Loading", "Please wait...");
				pr.setCanceledOnTouchOutside(false);
			};

			@Override
			protected OResponse<Void> do_in_background(Long... params) {
				try {
					OResponse<Long> friendIdRes = DataStore
							.makeFriends(params[0]);
					if (friendIdRes.getErrorCode() == 0) { // success
						taskResultCode = SUCCESS_MAKE_FRIEND;
						getUserRes = DataStore.getUser(friendIdRes.data);
						return null;
					} else if (friendIdRes.getErrorCode() == 1) {
						taskResultCode = ALLREADY_FRIEND;
						return null;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onFinish(int errorCode, String errorMessage,
					Void result) {
				if (pr != null && pr.isShowing())
					pr.dismiss();
				if (taskResultCode == SUCCESS_MAKE_FRIEND) {// success
					if (getUserRes.isSuccess())
						ShowFrinedDetail(getUserRes.data);
					Toast.makeText(mActMain, "友達登録が完了しました", Toast.LENGTH_LONG)
							.show();
				} else if (taskResultCode == ALLREADY_FRIEND) { // 既に友達登録済み
					Toast.makeText(mActMain, "既に登録済みです", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(mActMain, "ERROR:" + errorMessage,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void ShowFrinedDetail(User user) {
		FriendDetailDialog dialog = new FriendDetailDialog(mActMain, user);
		dialog.show();
	}

	// /////////////////////////////
	// functions for android beam
	// /////////////////////////////
	@Override
	public void onNdefPushComplete(NfcEvent event) {
		Log.d(TAG, "onNdefPushComplete");
		// A handler is needed to send messages to the activity when this
		// callback occurs, because it happens from a binder thread
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}

	/** This handler receives a message from onNdefPushComplete */
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				makeFriendsView.init();
				Toast.makeText(mActMain, "通信が成功しました", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		Log.d(TAG, "createNdefMessage");
		NdefMessage msg = null;
		String text = Long.toString(DataStore.getMyUserId());
		msg = new NdefMessage(NdefRecord.createMime(
				"application/titech.orekue", text.getBytes()));
		return msg;
	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		Long friendUserId = Long.valueOf(new String(msg.getRecords()[0]
				.getPayload()));
		// makeFriendsView.mInfoText.setText("you were sent your friend's userId : "
		// + new String(msg.getRecords()[0].getPayload()));
		makeFriendsView.makeFriends(friendUserId);
		makeFriendsView.init();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume!");
		if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
			mNfcAdapter.enableForegroundDispatch(mActMain, pendingIntent,
					new IntentFilter[] { ndef, }, techListsArray);
		}
	}

	@Override
	public void onPause() {
		if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
			mNfcAdapter.disableForegroundDispatch(mActMain);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			if (makeFriendsView != null
					&& (View) mFrame.getChildAt(0) == makeFriendsView) {
				Log.d(TAG, "友達づくりボタンが選択中");
				processIntent(intent);
			} else {
				Log.d(TAG, "友達づくりボタンが選択されていない");
				Toast.makeText(mActMain, "「友達づくり」を選択してください。", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	private Boolean findFriend() {
		if (mNfcAdapter.isEnabled()) {
			mNfcAdapter.setNdefPushMessageCallback(this, mActMain);
			return true;
		} else {
			return false;
		}
	}

	private void cancelFindFriend() {
		mNfcAdapter.setNdefPushMessageCallback(null, mActMain);
	}

	// /////////////////////////////
	// functions for OTabAbstract
	// /////////////////////////////
	@Override
	public boolean canGoBack() {
		return false;
	}

	@Override
	public void goBack() {
	}

	@Override
	public void reloadTab() {
		// TODO Auto-generated method stub

	}
}
