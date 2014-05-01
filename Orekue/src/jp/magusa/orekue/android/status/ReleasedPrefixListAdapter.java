package jp.magusa.orekue.android.status;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.Prefix;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class ReleasedPrefixListAdapter extends ArrayAdapter<Prefix> {
	private List<Prefix> items;
	private LayoutInflater inflater;
	
	public ReleasedPrefixListAdapter(Activity context, int resourceId,  
			List<Prefix> items) {  
			super(context, resourceId, items);  
			this.items = items;  
			this.inflater = (LayoutInflater) context  
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;  
		if (view == null) {  
			view = inflater.inflate(R.layout.account_released_prefix_item, null);
			//view = inflater.inflate(android.R.layout.simple_list_item_single_choice, null);
		}
		Prefix prefix = items.get(position);
		TextView tv = (TextView) view.findViewById(R.id.released_prefix_name);
		tv.setText(prefix.getName());
		AccountCustomLayout acl = (AccountCustomLayout) view.findViewById(R.id.PACL);
		acl.setButton((RadioButton) view.findViewById(R.id.rbtn_released_prefix));
		Orekue.applyFont(view);
		return view;  
	}
}

