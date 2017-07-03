package jin.hlschedule.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import jin.hlschedule.Activity.ListActivity;
import jin.hlschedule.Class.SubjectClass;
import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.R;

public class CustomAdapter_list extends BaseAdapter {


    private Context mContext = null;
    private LayoutInflater inflater;
    static ArrayList<SubjectClass> subjectList;
    private ViewHolder viewHolder;
    private SubjectClass mSubjectClass;
    private DBHelper mDBHelper;

    public CustomAdapter_list(Context c, ArrayList<SubjectClass> array) {
        inflater = LayoutInflater.from(c);
        subjectList = array;
        mContext = c;

    }

    @Override
    public int getCount() {
        return subjectList.size();
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
            v = inflater.inflate(R.layout.list_row, null);

            viewHolder.no = (TextView) v.findViewById(R.id.noTextview);
            viewHolder.major = (TextView) v.findViewById(R.id.majorTextview);
            viewHolder.name = (TextView) v.findViewById(R.id.nameTextview);
            viewHolder.ban = (TextView) v.findViewById(R.id.banTextview);
            viewHolder.hak = (TextView) v.findViewById(R.id.hakTextview);
            viewHolder.isu = (TextView) v.findViewById(R.id.isuTextview);
            viewHolder.prof = (TextView) v.findViewById(R.id.profTextview);
            viewHolder.time = (TextView) v.findViewById(R.id.timeTextview);
            viewHolder.btn = (Button) v.findViewById(R.id.add_button);
            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.no.setText(position + 1 + "");
        viewHolder.major.setText(subjectList.get(position).major);
        viewHolder.name.setText(subjectList.get(position).name);
        viewHolder.ban.setText(subjectList.get(position).ban);
        viewHolder.hak.setText(subjectList.get(position).hak + "");
        viewHolder.isu.setText(subjectList.get(position).isu);
        viewHolder.prof.setText(subjectList.get(position).professor);
        viewHolder.time.setText(subjectList.get(position).time);


        if (viewHolder.btn != null)
            viewHolder.btn.setText("-");
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                final String name = subjectList.get(position).name;
                final String ban = subjectList.get(position).ban;
                final String major = subjectList.get(position).major;
                alert_confirm.setMessage(name + " 과목을 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDBHelper = new DBHelper(mContext);
                                mDBHelper.open();
                                mDBHelper.mDB.execSQL("delete from addlist where 대상학과 = '" + major + "' AND 교과목명 = '" + name + "'  AND 분반 = '" + ban + "';");
                                ((ListActivity) mContext).CursorToArray();
                                ((ListActivity) mContext).mAddAdapter.notifyDataSetChanged();
                                //mDBHelper.deleteColumns(major,name,ban);
                                /*
                                ((AddSearchActivity) AddSearchActivity.mContext).mAddArray = ((ListActivity) mContext).mAddArray;
                                if (((AddSearchActivity) AddSearchActivity.mContext).mAddArray.size() != 0)
                                    ((AddSearchActivity) AddSearchActivity.mContext).mAddArray.remove(position);
                                 */
                                ((ListActivity) mContext).mAddAdapter.notifyDataSetChanged();
                                Toast toast = Toast.makeText(mContext, "삭제 되었습니다", Toast.LENGTH_LONG);
                                toast.show();

                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }

        });

        return v;
    }



    public void setArrayList(ArrayList<SubjectClass> arrays) {
        this.subjectList = arrays;
    }

    public ArrayList<SubjectClass> getArrayList() {
        return subjectList;
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
        Button btn;

    }


}





