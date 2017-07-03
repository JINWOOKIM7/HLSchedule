package jin.hlschedule.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import jin.hlschedule.Class.SubjectClass;
import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.R;

public class CustomAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater inflater;
    static ArrayList<SubjectClass> subjectList;
    private ViewHolder viewHolder;
    private SubjectClass mSubjectClass;
    boolean bool;
    private View.OnClickListener mOnClickListener = null;
    private DBHelper mDBHelper;
    ArrayList<SubjectClass> mAddArray;
    private Cursor mCursor;
    public CustomAdapter(Context c, ArrayList<SubjectClass> array) {
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
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                    String name = subjectList.get(position).name;

                    alert_confirm.setMessage(name + " 과목을 추가 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        mSubjectClass = new SubjectClass(
                                                subjectList.get(position).num,
                                                subjectList.get(position).major,
                                                subjectList.get(position).name,
                                                subjectList.get(position).ban,
                                                subjectList.get(position).hak,
                                                subjectList.get(position).isu,
                                                subjectList.get(position).professor,
                                                subjectList.get(position).time,
                                                subjectList.get(position).classroom,
                                                subjectList.get(position).onoff
                                        );
                                    Boolean contains = false;

                                    mDBHelper = new DBHelper(mContext);
                                    mDBHelper.open();
                                    mAddArray = new ArrayList<SubjectClass>();
                                    mCursor = null;
                                    mCursor = mDBHelper.getAllColumns();
                                    while (mCursor.moveToNext()) {
                                        mSubjectClass = new SubjectClass(
                                                mCursor.getString(mCursor.getColumnIndex("교과번호")),
                                                mCursor.getString(mCursor.getColumnIndex("대상학과")),
                                                mCursor.getString(mCursor.getColumnIndex("교과목명")),
                                                mCursor.getString(mCursor.getColumnIndex("분반")),
                                                mCursor.getInt(mCursor.getColumnIndex("학점")),
                                                mCursor.getString(mCursor.getColumnIndex("이수구분")),
                                                mCursor.getString(mCursor.getColumnIndex("담당교수")),
                                                mCursor.getString(mCursor.getColumnIndex("수업시간")),
                                                mCursor.getString(mCursor.getColumnIndex("강의실")),
                                                mCursor.getString(mCursor.getColumnIndex("온오프"))
                                        );
                                        mAddArray.add(mSubjectClass);
                                    }
                                    Log.v("database",mAddArray.size()+"");
                                    for(int i=0;i<mAddArray.size();i++){
                                        if(mAddArray.get(i).num.equals(subjectList.get(position).num) &&
                                                mAddArray.get(i).major.equals(subjectList.get(position).major) &&
                                                mAddArray.get(i).name.equals(subjectList.get(position).name) &&
                                                mAddArray.get(i).ban.equals(subjectList.get(position).ban) &&
                                                mAddArray.get(i).hak == subjectList.get(position).hak &&
                                                mAddArray.get(i).isu.equals(subjectList.get(position).isu) &&
                                                mAddArray.get(i).professor.equals(subjectList.get(position).professor) &&
                                                mAddArray.get(i).time.equals(subjectList.get(position).time))
                                            contains = true;
                                    }


                                    if(contains) {
                                        Toast toast = Toast.makeText(mContext, "동일한 수업이 있습니다", Toast.LENGTH_LONG );
                                        toast.show();
                                    }

                                    else {
                                        mDBHelper.mDB.execSQL("INSERT INTO addlist VALUES ('" +
                                                subjectList.get(position).num + "','" +
                                                subjectList.get(position).major + "','" +
                                                subjectList.get(position).name + "','" +
                                                subjectList.get(position).ban + "'," +
                                                subjectList.get(position).hak + ",'" +
                                                subjectList.get(position).isu + "','" +
                                                subjectList.get(position).professor + "','" +
                                                subjectList.get(position).time + "','" +
                                                subjectList.get(position).classroom + "','" +
                                                subjectList.get(position).onoff + "'" + ");");
                                        //((AddSearchActivity) mContext).mAddArray.add(mSubjectClass);

                                        Toast toast = Toast.makeText(mContext,  subjectList.get(position).name + " 추가 되었습니다", Toast.LENGTH_LONG );
                                        toast.show();

                                    }

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




