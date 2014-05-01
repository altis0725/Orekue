package jp.magusa.orekue.android.post;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.maguro.vs.samon.orekue.R.color;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.Category;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OActivity;
import jp.magusa.orekue.android.model.OActivityResponse;
import jp.magusa.orekue.android.model.OResponse;
import jp.magusa.orekue.android.model.OTask;
import jp.magusa.orekue.android.model.Prefix;
import jp.magusa.orekue.android.model.Tag;
import jp.magusa.orekue.android.model.Title;
import jp.magusa.orekue.android.view.OArrayAdapter;
import jp.magusa.orekue.android.view.OProgressDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class PostDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Log.i("post_dialog", "" + getActivity());
		View titleView = getActivity().getLayoutInflater().inflate(R.layout.account_dialog_title, null);
    	Orekue.applyFont(titleView);
    	((TextView)titleView.findViewById(R.id.dialog_title)).setText(R.string.post_title);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(titleView);
        
        View contentsView = getActivity().getLayoutInflater().inflate(R.layout.post, null);
        builder.setView(contentsView);
		final Dialog dialog = builder.create();//new Dialog(getActivity());
		//dialog.setContentView(R.layout.post);
		//dialog.setTitle(R.string.post_title);
		// dialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(R.color.post_dialog_window_color));
		//dialog.getWindow().setBackgroundDrawableResource(
		//		color.post_dialog_window_color);
		//dialog.getWindow().setTitleColor(
		//		getResources().getColor(R.color.post_dialog_title_color));
		//v.setBackgroundResource(R.color.post_dialog_title_color);
		dialog.setCanceledOnTouchOutside(false);
		TimePicker startTP = (TimePicker) contentsView
				.findViewById(R.id.post_activity_start_time);
		startTP.setIs24HourView(true);
//		startTP.setOnTimeChangedListener(new OnTimeChangedListener() {
//			@Override
//			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				// 5分で割り、下一桁が何分か取得
//				final int mod = minute % 10;
//
//				if (mod == 0) {
//					// 下一桁0分は何もしない
//				} else if (mod <= 5) {
//					// 下一桁1,2分は下一桁5分に切り上げ
//					final int value = minute + 10 - mod;
//					view.setCurrentMinute(value);
//					if (value == 60) {
//						// 60分なら1時間加算
//						view.setCurrentHour(hourOfDay + 1);
//					}
//				} else {
//					// 上記以外は下一桁0分に切り下げ
//					view.setCurrentMinute(minute - mod);
//				}
//			}
//		});
//		startTP.setCurrentMinute(0);
		// TimePicker x = ((TimePicker) dialog
		// .findViewById(R.id.post_activity_start_time));
		// x.setEnabled(false);
		
		
		// set category
		List<Category> clist = DataStore.getCategories();
		ArrayAdapter<Category> aac = new OArrayAdapter<Category>(getActivity(),
				android.R.layout.simple_list_item_1, clist);
		// aac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner catsp = ((Spinner) contentsView.findViewById(R.id.post_category));
		catsp.setAdapter(aac);
		catsp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner sp = (Spinner) parent;
//				List<Tag> tlist = DataStore.getTagsByCategoryId(((Category) sp
//						.getSelectedItem()).getId());
				final Long catId = ((Category) sp.getSelectedItem()).getId();
				
				// set tags
				new OTask<Void, Void, List<Tag>>() {
					Dialog pr;

					@Override
					protected void onPreExecute() {
						pr = OProgressDialog.show(getActivity(), "Loading",
								"Please wait...");
						pr.setCanceledOnTouchOutside(false);
					};
					@Override
					protected OResponse<List<Tag>> do_in_background(Void... params) {
						try {
							return DataStore.getTagsByCategoryId(catId);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onFinish(int errorCode, String errorMessage,
							List<Tag> result) {
						if (pr != null && pr.isShowing())
							pr.dismiss();
						if (errorCode == 0) {// success
							ArrayAdapter<Tag> aat = new OArrayAdapter<Tag>(
									getActivity(), android.R.layout.simple_list_item_1,
									result);
							((Spinner) dialog.findViewById(R.id.post_tag))
									.setAdapter(aat);
						} else {
							Toast.makeText(getActivity(), "ERROR:" + errorMessage,
									Toast.LENGTH_SHORT).show();
						}
					}

				}.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		ArrayAdapter<String> aath = new OArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, getActivity().getResources()
				.getStringArray(R.array.time_hours));
		((Spinner) contentsView.findViewById(R.id.post_time_hours)).setAdapter(aath);
		aath = new OArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, getActivity().getResources()
				.getStringArray(R.array.time_minutes));
		((Spinner) contentsView.findViewById(R.id.post_time_minutes)).setAdapter(aath);
		
		// set send button listener
		contentsView.findViewById(R.id.post_button_send).setOnClickListener(
				new OnClickListener() {
					public void onClick(View view) {
						if (view.getId() == R.id.post_button_send) {
							// Log.i("post", "post");

							Category category = (Category) ((Spinner) getDialog()
									.findViewById(R.id.post_category))
									.getSelectedItem();
							Tag tag = (Tag) ((Spinner) getDialog()
									.findViewById(R.id.post_tag))
									.getSelectedItem();
							Long hours = Long.valueOf(((Spinner) getDialog()
									.findViewById(R.id.post_time_hours))
									.getSelectedItem().toString());
							Long minutes = Long.valueOf(((Spinner) getDialog()
									.findViewById(R.id.post_time_minutes))
									.getSelectedItem().toString());
							String message = ((EditText) getDialog()
									.findViewById(R.id.post_edit_message))
									.getText().toString();

							DatePicker dp = ((DatePicker) getDialog()
									.findViewById(R.id.post_activity_date));
							TimePicker tp = ((TimePicker) getDialog()
									.findViewById(R.id.post_activity_start_time));
							Calendar date = Calendar.getInstance();
							date.set(dp.getYear(), dp.getMonth(),
									dp.getDayOfMonth(), tp.getCurrentHour(),
									tp.getCurrentMinute());
							final OActivity oa = new OActivity();
							if (hours == 0 && minutes == 0) {
								Toast.makeText(getActivity(),
										R.string.post_no_time_activity_message,
										Toast.LENGTH_SHORT).show();
								return;
							}
							// set data to oactivity
							oa.setUserId(DataStore.getMyUserId());
							oa.setTagId(tag.getId());
							oa.setCategoryId(category.getId());
							oa.setDuration(Long.valueOf(hours) * 60
									+ Long.valueOf(minutes));
							oa.setDate(date.getTimeInMillis());
							oa.setTimeStamp(System.currentTimeMillis());
							oa.setContent(message);
							new OTask<Void, Void, OActivityResponse>() {
								Dialog pr;

								@Override
								protected void onPreExecute() {
									pr = OProgressDialog.show(getActivity(),
											"Loading", "Please wait...");
									pr.setCanceledOnTouchOutside(false);
								};

								@Override
								protected OResponse<OActivityResponse> do_in_background(
										Void... params) {
									try {
										return DataStore.postActivity(oa);
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return null;
								}

								@Override
								protected void onFinish(int errorCode,
										String errorMessage,
										OActivityResponse result) {
									if (pr != null && pr.isShowing())
										pr.dismiss();
									if (errorCode == 0) {// success
										showResult(result);
										getDialog().dismiss();
									} else {
										Toast.makeText(getActivity(),
												"ERROR:" + errorMessage,
												Toast.LENGTH_SHORT).show();
									}
								}

							}.execute();

							// Log.i("post", "month1:" + dp.getYear());
							// Log.i("post", "month1:" + dp.getMonth());
							// Log.i("post", "month1:" + dp.getDayOfMonth());
							// Log.i("post", "month2:" +
							// date.get(Calendar.YEAR));
							// Log.i("post", "month2:" +
							// date.get(Calendar.MONTH));
							// Log.i("post", "month2:" +
							// date.get(Calendar.DATE));

						}
					}
				});

		contentsView.findViewById(R.id.post_button_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						if (view.getId() == R.id.post_button_cancel) {
							getDialog().cancel();
						}
					}
				});
		Orekue.applyFont(contentsView);
		return dialog;
	}

	// 投稿の連携機能だが実装しないことになったのでとりあえず凍結
	/*
	public void initializeForRelation(OActivity relativeActivity) {
		Spinner category = (Spinner) getDialog().findViewById(
				R.id.post_category);
		Spinner tag = (Spinner) getDialog().findViewById(R.id.post_tag);
		Spinner hours = (Spinner) getDialog()
				.findViewById(R.id.post_time_hours);
		Spinner minutes = (Spinner) getDialog().findViewById(
				R.id.post_time_minutes);

		DatePicker dp = (DatePicker) getDialog().findViewById(
				R.id.post_activity_date);
		TimePicker tp = (TimePicker) getDialog().findViewById(
				R.id.post_activity_start_time);

		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(relativeActivity.getDate());
		dp.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH), null);
		dp.setEnabled(false);
	}
*/
	private void showResult(OActivityResponse oactivity) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		ResultDialogFragment result = new ResultDialogFragment();

		Bundle bundle = new Bundle();
		Resources res = getResources();
		ArrayList<String> resultsStringArray = new ArrayList<String>();
		long study = oactivity.getStudyIncrement();
		if (study > 0) {
			resultsStringArray.add(res.getString(R.string.post_result_study)
					+ oactivity.getStudyIncrement()
					+ res.getString(R.string.post_result_up));
		}
		if (oactivity.getArtIncrement() > 0) {
			resultsStringArray.add(res.getString(R.string.post_result_art)
					+ oactivity.getArtIncrement()
					+ res.getString(R.string.post_result_up));

		}
		if (oactivity.getExerciseIncrement() > 0) {
			resultsStringArray.add(res
					.getString(R.string.post_result_excercise)
					+ oactivity.getExerciseIncrement()
					+ res.getString(R.string.post_result_up));

		}
		if (oactivity.getFashionIncrement() > 0) {
			resultsStringArray.add(res.getString(R.string.post_result_fashion)
					+ oactivity.getFashionIncrement()
					+ res.getString(R.string.post_result_up));

		}
		if (oactivity.getSocietyIncrement() > 0) {
			resultsStringArray.add(res.getString(R.string.post_result_sociaty)
					+ oactivity.getSocietyIncrement()
					+ res.getString(R.string.post_result_up));

		}
		if (oactivity.getCommunicationIncrement() > 0) {
			resultsStringArray.add(res
					.getString(R.string.post_result_communication)
					+ oactivity.getCommunicationIncrement()
					+ res.getString(R.string.post_result_up));
		}
		List<Title> titles = oactivity.getEarnedTitle();
		if (titles != null) {
			for (Title i : titles) {
				resultsStringArray.add(res.getString(R.string.title) + "["
						+ i.getName() + "]"
						+ res.getString(R.string.post_result_get_job_message));
			}
		}
		List<Prefix> prefixes = oactivity.getEarnedPrefix();
		if (prefixes != null) {
			for (Prefix i : prefixes) {
				resultsStringArray.add(res.getString(R.string.prefix) + "["
						+ i.getName() + "]"
						+ res.getString(R.string.post_result_getted_message));
			}
		}
		
		long upedLevel = oactivity.getIncreasedLevel();
		if(upedLevel >0){
			resultsStringArray.add(res.getString(R.string.level) + upedLevel +res.getString(R.string.upped));
		}
		
		
		long coins = oactivity.getEarnedCoin();
		if(coins > 0){
			resultsStringArray.add(res.getString(R.string.coin) + coins+"G" +res.getString(R.string.post_result_getted_message));
		}
		
		bundle.putStringArrayList(
				ResultDialogFragment.POST_RESULTS_STRINGS_TAG,
				resultsStringArray);
		

		result.setArguments(bundle);
		result.show(fm, "post_result_dialog");

		// //back up
		// FragmentManager fm = getActivity().getSupportFragmentManager();
		// ResultDialogFragment result = new ResultDialogFragment();
		//
		// result.show(fm, "post_result_dialog");
		//
		// Resources res = getResources();
		// long study = oactivity.getStudyIncrement();
		// if (study > 0) {
		// result.addResult(res.getString(R.string.post_result_study)
		// + oactivity.getStudyIncrement()
		// + res.getString(R.string.post_result_up));
		// }
		// if (oactivity.getArtIncrement() > 0) {
		// result.addResult(res.getString(R.string.post_result_art)
		// + oactivity.getArtIncrement()
		// + res.getString(R.string.post_result_up));
		//
		// }
		// if (oactivity.getExerciseIncrement() > 0) {
		// result.addResult(res.getString(R.string.post_result_excercise)
		// + oactivity.getExerciseIncrement()
		// + res.getString(R.string.post_result_up));
		//
		// }
		// if (oactivity.getFashionIncrement() > 0) {
		// result.addResult(res.getString(R.string.post_result_fashion)
		// + oactivity.getFashionIncrement()
		// + res.getString(R.string.post_result_up));
		//
		// }
		// if (oactivity.getSocietyIncrement() > 0) {
		// result.addResult(res.getString(R.string.post_result_sociaty)
		// + oactivity.getSocietyIncrement()
		// + res.getString(R.string.post_result_up));
		//
		// }
		// if (oactivity.getCommunicationIncrement() > 0) {
		// result.addResult(res.getString(R.string.post_result_communication)
		// + oactivity.getCommunicationIncrement()
		// + res.getString(R.string.post_result_up));
		// }
	}

}
