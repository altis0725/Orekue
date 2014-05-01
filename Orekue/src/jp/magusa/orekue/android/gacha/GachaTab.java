package jp.magusa.orekue.android.gacha;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.OTabAbstract;
import jp.magusa.orekue.android.home.HomeTab.OnClickHomeBtnListener;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.User;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

public class GachaTab extends OTabAbstract implements OnClickListener
		{

	private final int SUMCARD = 12;
	private final int PRICE = 5;
	
	private final int SAEKI = 0;
	private final int TANAKA = 1;
	private final int MORIMOTO = 2;
	private final int GONDOW = 3;
	private final int KOBAYASHI = 4;
	private final int SHUDO = 5;
	private final int SATO = 6;
	private final int TAKUO = 7;
	private final int NISIZAKI = 8;
	private final int HAYASHI = 9;
	private final int HAGIHARA = 10;
	private final int YONEZAKI = 11;

	int cardnum[] = new int[SUMCARD];
	private Boolean connect = false;
	ViewGroup mContainerView, mTopView;
	Button mBtnSend;
	Button mBtnItem;
	Button mBtnReturn;
	Button mBtnMain;
	ImageView mGacha;
	ImageView mCard;
	Animation mAni;
	Stack<View> mStack = new Stack<View>();
	Context context;
	GridView gridView;
	TextView mCoin;
	TextView mText;
	private ViewFlipper viewFlipper;
	private ViewSwitcher viewSwitcher;
	private MediaPlayer se;
	private MediaPlayer se_end;
	private MediaPlayer bgm;
	

	public GachaTab(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView() {
		if (mContainerView != null) {
			return mContainerView;
		}
		mContainerView = new FrameLayout(mActMain);
		mContainerView.addView(getActivityView());
		 bgm = MediaPlayer.create(context, R.raw.main_bgm3);
		SharedPreferences pref = context.getSharedPreferences("CardPrefs", 0);
		cardnum[SAEKI] = pref.getInt("SAEKI", 0);
		cardnum[TANAKA] = pref.getInt("TANAKA", 0);
		cardnum[MORIMOTO] = pref.getInt("MORIMOTO", 0);
		cardnum[GONDOW] = pref.getInt("GONDOW", 0);
		cardnum[KOBAYASHI] = pref.getInt("KOBAYASHI", 0);
		cardnum[SHUDO] = pref.getInt("SHUDO", 0);
		cardnum[SATO] = pref.getInt("SATO", 0);
		cardnum[TAKUO] = pref.getInt("TAKUO", 0);
		cardnum[NISIZAKI] = pref.getInt("NISIZAKI", 0);
		cardnum[HAYASHI] = pref.getInt("HAYASHI", 0);
		cardnum[HAGIHARA] = pref.getInt("HAGIHARA", 0);
		cardnum[YONEZAKI] = pref.getInt("YONEZAKI", 0);
		return mContainerView;
	}

	private View getActivityView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.gacha_tab, null);
		mTopView = (ViewGroup) v.findViewById(R.id.layout_top);
		mBtnSend = (Button) v.findViewById(R.id.button_send);
		mBtnSend.setOnClickListener(this);
		mBtnItem = (Button) v.findViewById(R.id.button_gacha_items);
		mBtnItem.setOnClickListener(this);
		mCoin = (TextView) v.findViewById(R.id.gacha_mymoney);

		GetUserTask guTask = new GetUserTask();
		guTask.execute();
		// mGacha = (ImageView) v.findViewById(R.id.imageView_gacha);
		mAni = AnimationUtils.loadAnimation(mActMain, R.anim.gacha);

		viewSwitcher = (ViewSwitcher) v.findViewById(R.id.view_switcher);

		viewFlipper = (ViewFlipper) v.findViewById(R.id.flipper);
		ImageView imageView = new ImageView(context);
		imageView.setImageDrawable(context.getResources().getDrawable(
				R.drawable.sora_2));

		viewFlipper.addView(imageView);

		ImageView imageView2 = new ImageView(context);
		imageView2.setImageDrawable(context.getResources().getDrawable(
				R.drawable.sora_3));

		viewFlipper.addView(imageView2);

		viewFlipper.setFlipInterval(150);

		// bring to front
		// for hiding card
		// Never do: honkanBottom bring to front
		// Z axis: honkan_top > card > honkan_bottom
		View honkanTop = v.findViewById(R.id.honkan_top);
		honkanTop.bringToFront();
		applyFont(v);
		return v;
	}

	private View getGachainfoView(GachaGridData data) {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.gacha_info, null);
		mBtnReturn = (Button) v.findViewById(R.id.button_gachainfo_return);
		mBtnReturn.setOnClickListener(this);

		TextView num = (TextView) v.findViewById(R.id.gc_info_num);
		num.setText(context.getResources().getString(R.string.gacha_no)
				+ String.format("%1$02d", data.getId() + 1) + "     "
				+ Integer.toString(data.getNumData())
				+ context.getResources().getString(R.string.gacha_shoji));
		ImageView image = (ImageView) v.findViewById(R.id.gc_info_image);
		image.setImageBitmap(data.getImageData());
		applyFont(v);
		return v;
	}

	private View getGachagetView(int rand) {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.gacha_getcard,
				null);
		mBtnMain = (Button) v.findViewById(R.id.button_gacha_main);
		mBtnMain.setOnClickListener(this);

		mCard = (ImageView) v.findViewById(R.id.gacha_card);
		mText = (TextView) v.findViewById(R.id.gacha_getcom);

		String kyoju = "";

		Bitmap image = null;
		switch (rand) {
		case SAEKI:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.saeki);
			cardnum[SAEKI]++;
			kyoju = context.getResources().getString(R.string.gacha_saeki);
			break;
		case TANAKA:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.tanaka);
			cardnum[TANAKA]++;
			kyoju = context.getResources().getString(R.string.gacha_tanaka);
			break;
		case MORIMOTO:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.morimoto);
			cardnum[MORIMOTO]++;
			kyoju = context.getResources().getString(R.string.gacha_morimoto);
			break;
		case GONDOW:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gondow);
			cardnum[GONDOW]++;
			kyoju = context.getResources().getString(R.string.gacha_gondow);
			break;
		case KOBAYASHI:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.kobayashi);
			cardnum[KOBAYASHI]++;
			kyoju = context.getResources().getString(R.string.gacha_kobayashi);
			break;
		case SHUDO:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.shudo);
			cardnum[SHUDO]++;
			kyoju = context.getResources().getString(R.string.gacha_shudo);
			break;
		case SATO:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.sato);
			cardnum[SATO]++;
			kyoju = context.getResources().getString(R.string.gacha_sato);
			break;
		case TAKUO:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.takuo);
			cardnum[TAKUO]++;
			kyoju = context.getResources().getString(R.string.gacha_takuo);
			break;
		case NISIZAKI:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.nisizaki);
			cardnum[NISIZAKI]++;
			kyoju = context.getResources().getString(R.string.gacha_nisizaki);
			break;
		case HAYASHI:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hayashi);
			cardnum[HAYASHI]++;
			kyoju = context.getResources().getString(R.string.gacha_hayashi);
			break;
		case HAGIHARA:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hagihara);
			cardnum[HAGIHARA]++;
			kyoju = context.getResources().getString(R.string.gacha_hagihara);
			break;
		case YONEZAKI:
			image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.yonezaki);
			cardnum[YONEZAKI]++;
			kyoju = context.getResources().getString(R.string.gacha_yonezaki);
			break;
		default:
			image = null;
		}

		mText.setText(context.getResources().getString(R.string.gacha_no)
				+ String.format("%1$02d", rand + 1) + kyoju
				+ context.getResources().getString(R.string.gacha_getcomment));

		mCard.setImageBitmap(image);
		SharedPreferences pref = context.getSharedPreferences("CardPrefs", 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("SAEKI", cardnum[SAEKI]);
		editor.putInt("TANAKA", cardnum[TANAKA]);
		editor.putInt("MORIMOTO", cardnum[MORIMOTO]);
		editor.putInt("GONDOW", cardnum[GONDOW]);
		editor.putInt("KOBAYASHI", cardnum[KOBAYASHI]);
		editor.putInt("SHUDO", cardnum[SHUDO]);
		editor.putInt("SATO", cardnum[SATO]);
		editor.putInt("TAKUO", cardnum[TAKUO]);
		editor.putInt("NISIZAKI", cardnum[NISIZAKI]);
		editor.putInt("HAYASHI", cardnum[HAYASHI]);
		editor.putInt("HAGIHARA", cardnum[HAGIHARA]);
		editor.putInt("YONEZAKI", cardnum[YONEZAKI]);
		editor.commit();
		applyFont(v);
		return v;
	}

	private View getGachagridView() {
		ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.gacha_grid, null);

		Bitmap def_image = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.card_default);
		Bitmap image[] = new Bitmap[SUMCARD];
		image[SAEKI] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.saeki);

		image[TANAKA] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tanaka);

		image[MORIMOTO] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.morimoto);

		image[GONDOW] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.gondow);

		image[KOBAYASHI] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.kobayashi);

		image[SHUDO] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.shudo);
		
		image[SATO] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.sato);
		
		image[TAKUO] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.takuo);
		
		image[NISIZAKI] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.nisizaki);
		
		image[HAYASHI] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.hayashi);
		
		image[HAGIHARA] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.hagihara);
		
		image[YONEZAKI] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.yonezaki);

		// データの作成
		List<GachaGridData> objects = new ArrayList<GachaGridData>();

		GachaGridData item[] = new GachaGridData[SUMCARD];
		for (int i = SAEKI; i < SUMCARD; i++) {
			item[i] = new GachaGridData();
			if (cardnum[i] > 0) {
				item[i].setImageData(image[i]);
				item[i].setNumData(cardnum[i]);
			} else {
				item[i].setImageData(def_image);
				item[i].setNumData(0);
			}
			item[i].setId(i);
			objects.add(item[i]);
		}

		GachaGridAdapter customAdapater = new GachaGridAdapter(context, 0,
				objects);

		gridView = (GridView) v.findViewById(R.id.gacha_gridview);
		gridView.setAdapter(customAdapater);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GachaGridData data = (GachaGridData) gridView
						.getItemAtPosition(position);
				mStack.add(mContainerView.getChildAt(0));
				mContainerView.removeAllViews();
				mContainerView.addView(getGachainfoView(data));
			}
		});

		mBtnReturn = (Button) v.findViewById(R.id.button_gacha_return);
		mBtnReturn.setOnClickListener(this);
		applyFont(v);
		return v;
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

	private void startScrollAnimation(View targetView, float beginY,
			int duration) {

		// multi-animation
		AnimationSet animSet = new AnimationSet(false);

		// translation
		TranslateAnimation transAnim = new TranslateAnimation(0, 0, beginY,
				beginY + 50
						* context.getResources().getDisplayMetrics().density);
		transAnim.setDuration(duration);
		animSet.addAnimation(transAnim);
		Log.d("STARTANIM", targetView + ";");
		// scale
		ScaleAnimation scaleAnim = new ScaleAnimation(0.8f, 1f, 1f, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(duration);
		animSet.addAnimation(scaleAnim);
		// make view to be state of animation-end.
		animSet.setFillAfter(true);
		animSet.setFillEnabled(true);
		targetView.startAnimation(animSet);
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_send:
			DrawCard drawcard = new DrawCard();
			drawcard.execute();
			break;
		case R.id.button_gacha_items:
			mStack.add(mContainerView.getChildAt(0));
			mContainerView.removeAllViews();
			mContainerView.addView(getGachagridView());
			break;
		case R.id.button_gacha_return:
			mContainerView.removeAllViews();
			mStack.pop();
			mContainerView.addView(getActivityView());
			break;
		case R.id.button_gachainfo_return:
			mContainerView.removeAllViews();
			mStack.pop();
			mContainerView.addView(getGachagridView());
			break;
		case R.id.button_gacha_main:
			mContainerView.removeAllViews();
			mContainerView.addView(getActivityView());
		default:
			break;
		}
	}

	private class GetUserTask extends OTask<Void, Void, User> {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<User> do_in_background(Void... params) {
			try {
				OResponse<User> user = DataStore.getUser(DataStore
						.getMyUserId());
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
			if (errorCode == 0) {
				String precoin;
				String coin;
				String gold = Long.toString(result.getCoin());
				precoin = context.getResources()
						.getString(R.string.gacha_money);
				coin = context.getResources().getString(R.string.gacha_coin);
				mCoin.setText(precoin + " " + halfWidthNumberToFullWidthNumber(gold) + " " + 'Ｇ');
				if(result.getCoin() < PRICE){
					mBtnSend.setEnabled(false);
				}
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class DrawCard extends OTask<Void, Void, Boolean> implements AnimationListener {
		Dialog pr;

		protected void onPreExecute() {
			pr = OProgressDialog.show(mActMain, "Loading", "Please wait...");
			pr.setCanceledOnTouchOutside(false);
		};

		@Override
		protected OResponse<Boolean> do_in_background(Void... params) {
			try {
				OResponse<Boolean> connection = DataStore.drawGacha();
				return connection;
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
			// TODO Auto-generated method stub
			if (pr != null && pr.isShowing())
				pr.dismiss();
			if (errorCode == 0) {
				mBtnSend.setEnabled(false);
				mBtnItem.setEnabled(false);
			
				se = MediaPlayer.create(context, R.raw.gacha_draw2);
				se.start();
				viewSwitcher.showNext();
				viewFlipper.startFlipping();

				View cardView = ((Activity) context).findViewById(R.id.card);
				View shadowView = ((Activity) context)
						.findViewById(R.id.shadow_card);
				startScrollAnimation(cardView, 0, 1500);
				startScrollAnimation(shadowView, -20
						* context.getResources().getDisplayMetrics().density,
						1500);
				// mGacha.startAnimation(mAni);
				AlphaAnimation aanim = new AlphaAnimation(1, 0);
				aanim.setAnimationListener(this);
				aanim.setDuration(3400);
				((Activity) context).findViewById(R.id.gacha).startAnimation(
						aanim);
				
			} else {
				Toast.makeText(mActMain, "ERROR:" + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			Random rand = new Random();
			int ran = rand.nextInt(SUMCARD);
			se_end = MediaPlayer.create(context, R.raw.gacha_get);
			se_end.start();
	
			mContainerView.removeAllViews();
			mContainerView.addView(getGachagetView(ran));
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static String halfWidthNumberToFullWidthNumber(String str) {
        if (str == null){
            throw new IllegalArgumentException();
        }
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ('0' <= c && c <= '9') {
                sb.setCharAt(i, (char) (c - '0' + '０'));
            }
        }
        return sb.toString();
    }

	@Override
	public void reloadTab() {
		new GetUserTask().execute();
	}
}