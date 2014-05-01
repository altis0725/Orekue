package jp.magusa.orekue.android.status;

import java.util.Calendar;
import java.util.List;

import jp.maguro.vs.samon.orekue.R;
import jp.magusa.orekue.android.Orekue;
import jp.magusa.orekue.android.model.DataStore;
import jp.magusa.orekue.android.model.OActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class HistoryListAdapter extends BaseAdapter {
    private Context context;
    private List<OActivity> actList;

    public HistoryListAdapter(Context context, List<OActivity> activities) {
        super();
        this.context = context;
        actList = activities;
    }

    public int getCount() {
        return actList.size();
    }

    public Object getItem(int position) {
        return actList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        OActivity a = (OActivity) getItem(position);
        TextView date,duration,tag,content;
        View v = convertView;

        if(v==null){
          LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          v = inflater.inflate(R.layout.history_list, null);
        }

        if(a != null){
          date = (TextView) v.findViewById(R.id.list_date);
          duration = (TextView) v.findViewById(R.id.list_duration);
          tag = (TextView) v.findViewById(R.id.list_tag);
          content = (TextView) v.findViewById(R.id.list_content);
          Calendar cal = Calendar.getInstance();
          cal.setTimeInMillis(a.getDate());
          int year = cal.get(Calendar.YEAR);
          int month = cal.get(Calendar.MONTH) + 1;
          int day = cal.get(Calendar.DATE);
          int hour = cal.get(Calendar.HOUR_OF_DAY);
          int minute = cal.get(Calendar.MINUTE);
          date.setText(String.format("%04d年%02d月%02d日 %02d:%02d", year, month, day, hour, minute));
          if(a.getDuration()/60 < 1){
              duration.setText(Long.toString(a.getDuration() % 60)
    				+ context.getResources().getText(R.string.home_duration));
          }else{
        	  duration.setText(Long.toString(a.getDuration() / 60)
	      				+ context.getResources().getText(R.string.home_hour)
	    				+ Long.toString(a.getDuration() % 60)
	    				+ context.getResources().getText(R.string.home_duration));
          }
          //List<Category> cats = DataStore.getCategories();
          //int choose = (int)a.getCategoryId();
          int choose = (int)a.getTagId();
          if(choose != 0){
        	  tag.setText("[" + DataStore.getTagName(choose) + "]");
          }else{
        	  tag.setText("");
          }
          content.setText(a.getContent());
        }
        Orekue.applyFont(v);
        return v;
    }
}
