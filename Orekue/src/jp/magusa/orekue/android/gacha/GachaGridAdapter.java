package jp.magusa.orekue.android.gacha;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GachaGridAdapter extends ArrayAdapter<GachaGridData> {
	private LayoutInflater layoutInflater_;

	Context context;
	
	public GachaGridAdapter(Context context, int textViewResourceId,
			List<GachaGridData> objects) {
		super(context, textViewResourceId, objects);
		layoutInflater_ = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GachaGridData item = (GachaGridData) getItem(position);

		
		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.gacha_grid_item,
					null);
		}

	
		ImageView imageView;
		imageView = (ImageView) convertView.findViewById(R.id.gc_grid_image);
		imageView.setImageBitmap(item.getImageData());

		TextView textView;
		textView = (TextView) convertView.findViewById(R.id.gc_grid_id);
		textView.setText(context.getResources().getString(R.string.gacha_no) + String.format("%1$02d",item.getId() + 1));

		TextView textView3;
		textView3 = (TextView) convertView.findViewById(R.id.gc_grid_num);
		String num = Integer.toString(item.getNumData());
		textView3.setText(num + context.getResources().getString(R.string.gacha_shoji));
		Orekue.applyFont(convertView);
		return convertView;
	}

}
