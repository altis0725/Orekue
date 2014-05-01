package jp.magusa.orekue.android;

//dhoshino edit

import java.util.Random;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.friends.FriendsTab;
import jp.magusa.orekue.android.gacha.GachaTab;
import jp.magusa.orekue.android.home.HomeTab;
import jp.magusa.orekue.android.home.HomeTab.OnClickHomeBtnListener;
import jp.magusa.orekue.android.others.OthersTab;
import jp.magusa.orekue.android.post.PostDialogFragment;
import jp.magusa.orekue.android.status.StatusTab;
import jp.magusa.orekue.android.view.DepthPageTransformer;
import jp.magusa.orekue.android.view.TabController;
import jp.magusa.orekue.android.view.TabController.OnTabSelectedListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		OnTabSelectedListener, OnPageChangeListener, OnClickHomeBtnListener{
	TabController mTabController;
	ViewPager mViewPager;
	OTabAbstract mCurTab;
	OTabAbstract[] mTabs;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		this.getActionBar().setDisplayShowCustomEnabled(true);
		this.getActionBar().setDisplayShowTitleEnabled(false);
		View v = getLayoutInflater().inflate(R.layout.main_title, null);
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(getTitle());
		Orekue.applyFont(title);
		this.getActionBar().setCustomView(v);

		// TODO gcm
		// getGcmInfo();
		// ou commit test

		mTabController = new TabController(
				(ViewGroup) findViewById(R.id.layout_tabs));
		Orekue.applyFont(findViewById(R.id.layout_tabs));
		mTabController.setOnTabSelectedListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		// Listener for moving to StatusTab
		HomeTab htab = new HomeTab(this);
		htab.setOnClickHomeBtnListener(this);
		GachaTab gtab = new GachaTab(this);
		mTabs = new OTabAbstract[] { htab, new FriendsTab(this),
				new StatusTab(this), gtab, new OthersTab(this) };

		mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View v = null;
				switch (position) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					v = mTabs[position].getView();
					break;
				default:
					TextView text = new TextView(MainActivity.this);
					text.setGravity(Gravity.CENTER);
					text.setTextSize(30);
					text.setTextColor(Color.WHITE);
					text.setText("Page " + position);
					text.setPadding(30, 30, 30, 30);
					int bg = Color.rgb(
							(int) Math.floor(Math.random() * 128) + 64,
							(int) Math.floor(Math.random() * 128) + 64,
							(int) Math.floor(Math.random() * 128) + 64);
					text.setBackgroundColor(bg);
					container.addView(text, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					v = text;
					break;
				}
				ViewGroup gr = (ViewGroup) v.getParent();
				if (gr != null)
					gr.removeAllViews();
				container.addView(v);
				return v;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}

			@Override
			public int getCount() {
				return mTabController.getCount();
			}
		});
		mViewPager.setOnPageChangeListener(this);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			//mViewPager.setPageTransformer(true, new DepthPageTransformer());
		}
		mTabController.setTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_post:
			// Toast.makeText(this, "Post", Toast.LENGTH_LONG).show();
			showPostDialog();
			break;
		case R.id.item_reload:
			reload();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(TabController tabControler, int fromTabId,
			int toTabId) {
		if (fromTabId != toTabId)
			mViewPager.setCurrentItem(toTabId, true);
		// ->go to onPageSelected to deploy
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		mTabController.setTab(position, false);
		// TODO
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onBackPressed() {
		if (mTabs[mViewPager.getCurrentItem()].canGoBack()) {
			mTabs[mViewPager.getCurrentItem()].goBack();
		} else
			super.onBackPressed();
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			if (mViewPager.getCurrentItem() == 1) { // friendsTabが選択されている
				mTabs[1].onNewIntent(intent);
			} else {
				Toast.makeText(this, "友達通信を受信するには友達タブを選択してください。",
						Toast.LENGTH_LONG).show();
			}
		} else {
			for (OTabAbstract tab : mTabs) {
				tab.onNewIntent(intent);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		for (OTabAbstract tab : mTabs) {
			tab.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		for (OTabAbstract tab : mTabs) {
			tab.onPause();
		}
	}

	private void showPostDialog() {
		FragmentManager fm = getSupportFragmentManager();
		PostDialogFragment post = new PostDialogFragment();
		post.show(fm, "post_dialog");
		// ResultDialogFragment result = new ResultDialogFragment();
		// result.show(fm, "post_dialog");

	}

	@Override
	public void onHomeBtnClick() {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(2, true);
		Log.d("gachagacha", "HelloWorld");
	}


	private void reload(){
		//mCurTab.reloadTab();
		mTabs[mViewPager.getCurrentItem()].reloadTab();
//		int num = mViewPager.getAdapter().getItemPosition(mCurTab);
	}
}
