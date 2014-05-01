package jp.magusa.orekue.android.friends;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.Prefix;
import jp.magusa.orekue.android.model.Title;
import jp.magusa.orekue.android.model.User;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<User> implements OnClickListener {
	private Activity context;
	private List<User> items;
	private LayoutInflater inflater;
	private Resources resources;
	RemoveFriendListener lstRemoveFriend;

	public FriendListAdapter(Activity context, int textViewResourceId,
			List<User> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resources = context.getResources();
	}
	
	public void setOnRemoveFriendListener(RemoveFriendListener lst){
		this.lstRemoveFriend = lst;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.friends_friendlist_item, null);
			Orekue.applyFont(view);
		}

		User user = items.get(position);
		Title title = user.getTitle();
		Prefix prefix = user.getPrefix();

		// 名前
		TextView nameView = (TextView) view
				.findViewById(R.id.friends_friendlist_item_name);
		nameView.setText(user.getName());

		// 称号
		TextView titleView = (TextView) view
				.findViewById(R.id.friends_friendlist_item_title_name);
		titleView.setText(prefix.getName() + title.getName());

		// アバター画像
		ImageView iconView = (ImageView) view
				.findViewById(R.id.friends_friendlist_item_icon);
		int resId;
		if (user.getIcon() == null) {
			resId = resources.getIdentifier("ic_launcher", "drawable",
					context.getPackageName());
		} else {
			resId = resources.getIdentifier(user.getIcon(), "drawable",
					context.getPackageName());
		}
		iconView.setImageResource(resId);

		// 友達削除
		View btnRemoveFriend = view.findViewById(R.id.button_remove_friend);
		btnRemoveFriend.setOnClickListener(this);
		btnRemoveFriend.setTag(user);

		return view;
	}

	public User getUserAt(int position) {
		return items.get(position);
	}

	public interface RemoveFriendListener {
		public void onRemoveFriend(User friendInfo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_remove_friend:
			if (lstRemoveFriend != null){
				lstRemoveFriend.onRemoveFriend((User) v.getTag());
			}
			break;
		default:
			break;
		}
	}
}
