package jp.magusa.orekue.android.home;

import java.util.Calendar;
import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OActivity;
import jp.magusa.orekue.android.model.Pair;
import jp.magusa.orekue.android.model.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimelineAdapter extends ArrayAdapter<Pair<User, OActivity>> {
	private LayoutInflater layoutInflater_;
	private volatile ItemDelegate delegate;

	Context context;

	public TimelineAdapter(Context context, int textViewResourceId,
			List<Pair<User, OActivity>> objects) {
		super(context, textViewResourceId, objects);
		layoutInflater_ = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	
	public void setDelegate(ItemDelegate d){
		delegate = d;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pair<User, OActivity> ob = (Pair<User, OActivity>) getItem(position);
		OActivity item = ob.second;
		User user = ob.first;
		ViewHolder holder;

		if (null == convertView) {
			holder = new ViewHolder();
			convertView = layoutInflater_
					.inflate(R.layout.home_timeline_list_item, null);
			Orekue.applyFont(convertView);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.hl_text);
			holder.levelView = (TextView) convertView
					.findViewById(R.id.hl_level);
			holder.nameView = (TextView) convertView.findViewById(R.id.hl_name);
			holder.prefixView = (TextView) convertView
					.findViewById(R.id.hl_pre);
			holder.tagView = (TextView) convertView.findViewById(R.id.hl_tag);
			holder.dateView = (TextView) convertView.findViewById(R.id.hl_date);
			holder.titleView = (TextView) convertView.findViewById(R.id.hl_job);
			holder.levelView = (TextView) convertView.findViewById(R.id.hl_level);
			holder.prefixView = (TextView) convertView.findViewById(R.id.hl_pre);
			holder.durationView = (TextView) convertView
					.findViewById(R.id.hl_duration);
			holder.pointView = (TextView) convertView
					.findViewById(R.id.hl_point);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.hl_image);
			holder.imageView.setOnClickListener(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(item.getDate());
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		holder.user = user;
		holder.titleView.setText(user.getTitle().getName());
		holder.prefixView.setText(user.getPrefix().getName());
		holder.levelView.setText("Lv."+user.getParamLevel());
		holder.nameView.setText(user.getName());
		holder.contentView.setText(item.getContent());
		holder.tagView.setText("[" + DataStore.getTagName(item.getTagId()) + "]");
		holder.dateView.setText(month + "/" + day + "/" + hour + ":" + minute);
		holder.durationView.setText(Long.toString(item.getDuration() / 60)
				+ context.getResources().getText(R.string.home_hour)
				+ Long.toString(item.getDuration() % 60)
				+ context.getResources().getText(R.string.home_duration));
		holder.pointView.setText(Long.toString(item.getDuration())
				+ context.getResources().getText(R.string.home_point));
		// holder.levelView.setText(item.getContent());
		// holder.nameView.setText(item.getContent());
		// holder.prefixView.setText(item.getContent());
		// holder.imageView.setImageBitmap(item.getImageData());
		
		int avatar_resId = 0;
        if (user.getIcon() != null){
        	avatar_resId = context.getResources().getIdentifier(
        			user.getIcon(), "drawable", context.getPackageName());
        }else{
        	avatar_resId = context.getResources().getIdentifier(
        			"ic_launcher", "drawable", context.getPackageName());
        }
        holder.imageView.setImageResource(avatar_resId);

		return convertView;
	}

	private class ViewHolder implements OnClickListener {
		public TextView nameView;
		public TextView titleView;
		public TextView prefixView;
		public TextView levelView;
		public TextView tagView;
		public TextView dateView;
		public TextView durationView;
		public TextView pointView;
		public TextView contentView;
		public ImageView imageView;
		User user;

		public ViewHolder() {

		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.hl_image:
				if (delegate!= null) {
					delegate.onRenkeiClicked(user);
				}
			}
		}

	}

	public interface ItemDelegate {
		public void onRenkeiClicked(User item);
	}
}
