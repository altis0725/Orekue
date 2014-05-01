package jp.magusa.orekue.android.view;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Checkable;

public class TabController{
	private int mCurTabId = -1;
	private List<View> mTabs = new ArrayList<View>();
	private OnTabSelectedListener mLst;
	
	public TabController(ViewGroup container){
		for (int i = 0; i < container.getChildCount(); i++){
			View v = container.getChildAt(i);
			if (v instanceof Checkable) addTab(v);
		}
	}
	
	public TabController(View...views){
		for (View v : views){
			if (v instanceof Checkable){
				addTab(v);
			}
		}
	}
	
	/**return -1 if no tab is selected*/
	public int getCurrentTabId(){
		return mCurTabId;
	}
	
	public int getCount(){
		return mTabs.size();
	}
	
	private boolean addTab(View tab){
		if (!(tab instanceof Checkable)) return false;
		((Checkable)tab).setChecked(false);
		mTabs.add(tab);
		((View)tab).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTab(v, true);
			}
		});
		return true;
	}
	
	public void setOnTabSelectedListener(OnTabSelectedListener lst){
		mLst = lst;
	}
	
	public void setTab(View tab, boolean activeListener){
		int i = mTabs.indexOf(tab);
		if (i<0) return;
		setTab(i, activeListener);
	}
	
	public void setTab(int id, boolean activeListener){
		if (id<0 || id >= mTabs.size()) return;
		
		View tab = mTabs.get(id);
		if (mCurTabId >=0) ((Checkable)mTabs.get(mCurTabId)).setChecked(false);
		((Checkable)tab).setChecked(true);
		int from = mCurTabId;
		mCurTabId = id;
		if (activeListener && mLst != null) mLst.onTabSelected(this, from, id);
		
	}
	
	public void setTab(int id){
		setTab(id, true);
	}
	
	public void clear(){
		if (mCurTabId >=0) ((Checkable)mTabs.get(mCurTabId)).setChecked(false);
		mCurTabId = -1;
	}
	
	public static interface OnTabSelectedListener{
		/**fromTabId = -1 if no selected tab*/
		public void onTabSelected(TabController tabControler, int fromTabId, int toTabId);
	}
}