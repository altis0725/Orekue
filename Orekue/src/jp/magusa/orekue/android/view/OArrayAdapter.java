package jp.magusa.orekue.android.view;

import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class OArrayAdapter<T> extends ArrayAdapter<T> {

	public OArrayAdapter(Context context, int resource, int textViewResourceId,
			List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public OArrayAdapter(Context context, int resource, int textViewResourceId,
			T[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public OArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public OArrayAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	public OArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
	}

	public OArrayAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		Orekue.applyFont(v);
		v.setBackgroundResource(R.drawable.dialog_frame);
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = super.getDropDownView(position, convertView, parent);
		Orekue.applyFont(v);
		v.setBackgroundResource(R.drawable.dialog_frame);
		return v;
	}
}
