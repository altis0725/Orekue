package jp.magusa.orekue.android.status;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

public class AvatarListAdapter extends ArrayAdapter<String>{
	private List<String> items;
	private LayoutInflater inflater;
	private Activity context;
	
	public AvatarListAdapter(Activity context, int resourceId,  
			List<String> items) {  
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
			view = inflater.inflate(R.layout.account_avatar_item, null);
		}
		String avatar = items.get(position);
		ImageView iv = (ImageView) view.findViewById(R.id.avatar_image);
		int resId = context.getResources().getIdentifier(
    			avatar, "drawable", context.getPackageName());
		iv.setImageResource(resId);
		AccountCustomLayout acl = (AccountCustomLayout) view.findViewById(R.id.AACL);
		acl.setButton((RadioButton) view.findViewById(R.id.rbtn_avatar));
		Orekue.applyFont(view);
		return view;  
	}
}
