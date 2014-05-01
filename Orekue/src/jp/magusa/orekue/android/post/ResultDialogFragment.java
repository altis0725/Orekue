package jp.magusa.orekue.android.post;

import java.util.ArrayList;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultDialogFragment extends DialogFragment {

	public static final String POST_RESULTS_STRINGS_TAG = "result_strings";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Log.i("result_dialog1", getActivity().toString());
		// Resources res = getResources();
		View titleView = getActivity().getLayoutInflater().inflate(
				R.layout.account_dialog_title, null);
		Orekue.applyFont(titleView);
		((TextView) titleView.findViewById(R.id.dialog_title))
				.setText(R.string.post_result_title);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCustomTitle(titleView);

		View contentsView = getActivity().getLayoutInflater().inflate(
				R.layout.post_result, null);
		builder.setView(contentsView);

		final Dialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(
				R.color.post_dialog_window_color);
		dialog.getWindow().setFormat(STYLE_NO_TITLE);

		// dialog.setContentView(R.layout.post_result);
		// dialog.setTitle(R.string.post_result_title);
		// dialog.getWindow().setTitleColor(res.getColor(R.color.post_dialog_title_color));
		dialog.setCanceledOnTouchOutside(false);
		// Set Listener
		contentsView.findViewById(R.id.post_result_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getDialog().dismiss();
					}
				});

		// Set Results TextViews
		ArrayList<String> resultStrings = getArguments().getStringArrayList(
				POST_RESULTS_STRINGS_TAG);
		if (resultStrings != null) {
			LinearLayout resultArea = ((LinearLayout) contentsView
					.findViewById(R.id.post_result_views));
			TextView result;
			for (String i : resultStrings) {
				result = new TextView(getActivity());
				result.setText(i);
				result.setBackgroundResource(R.drawable.post_dialog_result_block);
				result.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				result.setPadding(10, 10, 10, 10);
				resultArea.addView(result);
			}
			result = new TextView(getActivity());
			result.setText(R.string.triangle);
			result.setBackgroundResource(R.drawable.post_dialog_result_block);
			result.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			result.setGravity(Gravity.CENTER);
			result.setPadding(5, 5, 5, 5);
			resultArea.addView(result);
		}
		Orekue.applyFont(contentsView.findViewById(R.id.post_result_root));
		return dialog;
	}

	public void addResult(View view) {
		((LinearLayout) getDialog().findViewById(R.id.post_result_views))
				.addView(view);
	}

	public void addResult(String text) {
		Log.i("result_dialog2", "" + getActivity());
		TextView result = new TextView(getActivity());
		result.setText(text);

		((LinearLayout) getDialog().findViewById(R.id.post_result_views))
				.addView(result);

	}
}
