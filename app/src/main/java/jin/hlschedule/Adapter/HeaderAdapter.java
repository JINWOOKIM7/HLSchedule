package jin.hlschedule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jin.hlschedule.R;


public class HeaderAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private ArrayList<String> list;

    public HeaderAdapter(Context c, ArrayList<String> list) {
        inflater = LayoutInflater.from(c);
        this.list = list;
        mContext = c;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {
        View v = convertview;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_header, null);
            viewHolder.no = (TextView) v.findViewById(R.id.textView1);
            viewHolder.major = (TextView) v.findViewById(R.id.textView2);
            viewHolder.name = (TextView) v.findViewById(R.id.textView3);
            viewHolder.ban = (TextView) v.findViewById(R.id.textView4);
            viewHolder.hak = (TextView) v.findViewById(R.id.textView5);
            viewHolder.isu = (TextView) v.findViewById(R.id.textView6);
            viewHolder.prof = (TextView) v.findViewById(R.id.textView7);
            viewHolder.time = (TextView) v.findViewById(R.id.textView8);
            viewHolder.add = (TextView) v.findViewById(R.id.textView9);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        if(list.get(0).equals("remove")) {
            viewHolder.no.setText("No");
            viewHolder.major.setText("대상학과");
            viewHolder.name.setText("교과목명");
            viewHolder.ban.setText("분반");
            viewHolder.hak.setText("학점");
            viewHolder.isu.setText("이수구분");
            viewHolder.prof.setText("담당교수");
            viewHolder.time.setText("시간표");
            viewHolder.add.setText("삭제");
        }
        else {
            viewHolder.no.setText("No");
            viewHolder.major.setText("대상학과");
            viewHolder.name.setText("교과목명");
            viewHolder.ban.setText("분반");
            viewHolder.hak.setText("학점");
            viewHolder.isu.setText("이수구분");
            viewHolder.prof.setText("담당교수");
            viewHolder.time.setText("시간표");
            viewHolder.add.setText("추가");
        }

        return v;
    }




    class ViewHolder {
        TextView no;
        TextView major;
        TextView name;
        TextView ban;
        TextView hak;
        TextView isu;
        TextView prof;
        TextView time;
        TextView add;
    }
}






