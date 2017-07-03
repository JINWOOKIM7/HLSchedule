package jin.hlschedule.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import jin.hlschedule.Adapter.CustomAdapter_list;
import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.Adapter.HeaderAdapter;
import jin.hlschedule.R;
import jin.hlschedule.Class.SubjectClass;


public class ListActivity extends AppCompatActivity {
    public ListView mAddListView,mListViewHeader;
    public CustomAdapter_list mAddAdapter;
    ArrayList<SubjectClass> mAddArray;
    public Cursor mCursor;
    public DBHelper mDBHelper;
    public Button addBtn, improtBtn, createBtn;
    public SubjectClass mSubjectClass;
    public HeaderAdapter mAdapterHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        Intent intent = getIntent();
        //mAddArray = getIntent().getParcelableArrayListExtra("addlist");
        mAddArray = new ArrayList<SubjectClass>();


        mDBHelper = new DBHelper(this);
        mDBHelper.open();

        mAddListView = (ListView) findViewById(R.id.addlistView);
        mAddAdapter = new CustomAdapter_list(this, mAddArray);
        mAddListView.setAdapter(mAddAdapter);
        mAddListView.setOnItemClickListener(listener);
        mAddListView.setFocusable(false);

        createBtn = (Button) findViewById(R.id.create_button);
        createBtn.setOnClickListener(mClickListener);
        CursorToArray();

    }

    protected void onResume() {
        super.onResume();
        mDBHelper = new DBHelper(this);
        mDBHelper.open();
        mAddAdapter = new CustomAdapter_list(this, mAddArray);
        CursorToArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.mDB.execSQL("delete from addlist;");
        for (int i = 0; i < mAddArray.size(); i++) {
            mDBHelper.mDB.execSQL("INSERT INTO addlist VALUES ('" +
                    mAddArray.get(i).num + "','" +
                    mAddArray.get(i).major + "','" +
                    mAddArray.get(i).name + "','" +
                    mAddArray.get(i).ban + "'," +
                    mAddArray.get(i).hak + ",'" +
                    mAddArray.get(i).isu + "','" +
                    mAddArray.get(i).professor + "','" +
                    mAddArray.get(i).time + "','" +
                    mAddArray.get(i).classroom + "','" +
                    mAddArray.get(i).onoff + "'" + ");");
        }

    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {


                case R.id.create_button:

                    Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                    intent.putParcelableArrayListExtra("addlist",mAddArray);

                    startActivity(intent);
                    break;

            }
        }
    };
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(ListActivity.this, mAddArray.get(position).name, Toast.LENGTH_LONG).show();

        }
    };

    public void CursorToArray() {
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
                    mCursor.getString(mCursor.getColumnIndex("강의실")),mCursor.getString(mCursor.getColumnIndex("온오프"))
            );
            mAddArray.add(mSubjectClass);
        }
        //((AddSearchActivity)AddSearchActivity.mContext).mAddArray = mAddArray;
        mAddAdapter = new CustomAdapter_list(this, mAddArray);
        mAddAdapter.notifyDataSetChanged();
        mAddListView.setAdapter(mAddAdapter);

    }


}
