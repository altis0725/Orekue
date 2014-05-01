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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankingAdapter extends ArrayAdapter<User> {
	private Activity context;
	private List<User> items;
	private LayoutInflater inflater;
	private Resources resources;
	private long categoryId;
	
	public RankingAdapter(Activity context, int textViewResourceId,  
			List<User> items, long categoryId) {  
			super(context, textViewResourceId, items);  
			this.context = context;
			this.items = items;  
			this.inflater = (LayoutInflater) context  
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			resources = context.getResources();
			this.categoryId = categoryId;
			}  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;  
		if (view == null) {  
			view = inflater.inflate(R.layout.friends_ranking_item, null);
			Orekue.applyFont(view);
		}
		
		User user = items.get(position);
		Title title = user.getTitle();
		Prefix prefix = user.getPrefix();
		long point = getParam(user,categoryId);
		
		//順位
		TextView rankView = (TextView) view.findViewById(R.id.friends_ranking_item_rank);
		/*
		if(position == 0){
			rankView.setText("1位");
		}
		else{
			long point2 = items.get(position - 1).getParamArt();
			if(point2 == point){
				rank++;
			}
		}
		*/
		rankView.setText(position + 1 + "位");
		
		//得点
		TextView pointView = (TextView) view.findViewById(R.id.friends_ranking_item_point);
		pointView.setText(point + "点");
		
		// 名前
		TextView nameView = (TextView) view.findViewById(R.id.friends_ranking_item_name);
		nameView.setText(user.getName());
		
		// 称号
		TextView titleView = (TextView) view.findViewById(R.id.friends_ranking_item_title_name);
		titleView.setText(prefix.getName() + title.getName());
		
		// アバター画像
		ImageView iconView = (ImageView) view.findViewById(R.id.friends_ranking_item_icon);
		int resId;
		if(user.getIcon() == null){
			resId = resources.getIdentifier( "ic_launcher", "drawable", context.getPackageName());
		}else{
			resId = resources.getIdentifier( user.getIcon(), "drawable", context.getPackageName());
		}		iconView.setImageResource(resId);
		
		//Orekue.applyFont(view);
		return view;  
	}
	
	private long getParam(User user, long categoryId){
		switch((int)categoryId){
		case 1: return user.getParamStudy();
		case 2: return user.getParamExercise();
		case 3: return user.getParamCommunication();
		case 4: return user.getParamFashion();
		case 5: return user.getParamSociety();
		case 6: return user.getParamArt();
		default : return -1;
		}
	}
	
	public User getUserAt(int position){
		return items.get(position);
	}
}
