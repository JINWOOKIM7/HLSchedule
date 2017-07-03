package jin.hlschedule.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;

import jin.hlschedule.Activity.TimeTableActivity;
import jin.hlschedule.R;

public class CustomAdapter_essen extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    ArrayList<String> essenList;
    boolean[] chkArray;
    public CustomAdapter_essen(Context c, ArrayList<String> array) {
        inflater = LayoutInflater.from(c);
        essenList = array;
        mContext = c;
        chkArray = new boolean[array.size()];

    }

    @Override
    public int getCount() {
        return essenList.size();
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
            v = inflater.inflate(R.layout.essen_row, null);

            viewHolder.name = (TextView) v.findViewById(R.id.essen_name);
            viewHolder.checkBox = (CheckBox) v.findViewById(R.id.essen_check);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.name.setText(essenList.get(position));
        viewHolder.checkBox.setChecked(chkArray[position]);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    chkArray[position]=true;
                    if( !((TimeTableActivity) mContext).name_list_checked.contains( essenList.get(position)) )
                        ((TimeTableActivity) mContext).name_list_checked.add(essenList.get(position));
                }
                else{
                    chkArray[position]=false;
                    for(int i=0; i<((TimeTableActivity) mContext).name_list_checked.size(); i++){
                        if(((TimeTableActivity) mContext).name_list_checked.get(i).equals( essenList.get(position)+"")){
                            ((TimeTableActivity) mContext).name_list_checked.remove(i);
                        }
                    }
                }
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        return v;
    }


    public void setArrayList(ArrayList<String> arrays) {
        this.essenList = arrays;
    }

    public ArrayList<String> getArrayList() {
        return essenList;
    }

    class ViewHolder {
        TextView name;
        CheckBox checkBox;

    }
}




