package jp.magusa.orekue.android.status;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.Title;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ReleasedTitleListAdapter extends ArrayAdapter<Title> {
	private Activity context;
	private List<Title> items;
	private LayoutInflater inflater;
	
	public ReleasedTitleListAdapter(Activity context, int resourceId,  
			List<Title> items) {  
			super(context, resourceId, items);  
			this.context = context;
			this.items = items;  
			this.inflater = (LayoutInflater) context  
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;  
		if (view == null) {  
			view = inflater.inflate(R.layout.account_released_title_item, null);  
		}
		Title title = items.get(position);
		ImageView iv = (ImageView) view.findViewById(R.id.released_title_image);
		int resId = context.getResources().getIdentifier(
    			title.getIconUrl(), "drawable", context.getPackageName());
		iv.setImageResource(resId);
		TextView tv = (TextView) view.findViewById(R.id.released_title_name);
		tv.setText(title.getName());
		RadioButton rb = (RadioButton) view.findViewById(R.id.rbtn_released_title);
		AccountCustomLayout acl = (AccountCustomLayout) view.findViewById(R.id.TACL);
		acl.setButton(rb);
		Orekue.applyFont(view);
		return view;  
	}
}

