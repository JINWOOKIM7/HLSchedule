package jin.hlschedule.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;

import jin.hlschedule.Adapter.CustomAdapter;
import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.Adapter.HeaderAdapter;
import jin.hlschedule.R;
import jin.hlschedule.Class.SubjectClass;


public class AddSearchActivity extends AppCompatActivity {
    private Spinner spinner01;        // Spinner
    private Spinner spinner02;        // Spinner
    private ListView mListView,mListViewHeader;
    private CustomAdapter mAdapter;
    private HeaderAdapter mAdapterHeader;
    ArrayList<SubjectClass> mListArray;
    ArrayList<SubjectClass> mAddArray;

    final static String PACKAGE_NAME = "jin.hlschedule";
    final static String DB_NAME = "list.sqlite";
    private Cursor mCursor;
    private DBHelper mDBHelper;
    private EditText search_edittext;
    private SubjectClass mSubjectClass;
    private Button mSearchBtn, mListBtn;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsearch_layout);
        mContext = this;
        search_edittext = (EditText) findViewById(R.id.search_editText);
        spinner01 = (Spinner) findViewById(R.id.isudiv);
        setSpinner();
        spinner02 = (Spinner) findViewById(R.id.div);
        spinner01.setOnItemSelectedListener(spinSelectedlistener1);
        spinner02.setOnItemSelectedListener(spinSelectedlistener2);

        mDBHelper = new DBHelper(this);
        mDBHelper.open();

        mListView = (ListView) findViewById(R.id.listView);
        mListArray = new ArrayList<SubjectClass>();
        mAdapter = new CustomAdapter(this, mListArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(listener);
        mListView.setFocusable(false);
        mSearchBtn = (Button) findViewById(R.id.search_button);
        if (mSearchBtn != null)
            mSearchBtn.setOnClickListener(mClickListener);
        mListBtn = (Button) findViewById(R.id.list_button);
        if (mListBtn != null)
            mListBtn.setOnClickListener(mClickListener);
        mAddArray = new ArrayList<SubjectClass>();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new CustomAdapter(this, mListArray);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.search_button:
                        String search_str = search_edittext.getText().toString();
                        if(!search_str.equals("")) {
                            mListArray = new ArrayList<SubjectClass>();
                            mCursor = null;
                            mCursor = mDBHelper.getSearchColumns(search_str);
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

                                mListArray.add(mSubjectClass);

                            }
                            mAdapter = new CustomAdapter(mContext, mListArray);
                            mListView.setAdapter(mAdapter);
                        }
                        break;

                    case R.id.list_button :
                        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                        intent.putParcelableArrayListExtra("addlist",mAddArray);
                        startActivity(intent);

                }
            }
    };

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(AddSearchActivity.this, mListArray.get(position).name, Toast.LENGTH_SHORT).show();

        }
    };
    private OnItemSelectedListener spinSelectedlistener1 = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            switch (position) {
                case (0):
                    setSpinner(R.array.major);
                    break;
                case (1):
                    setSpinner(R.array.essential);
                    break;
                case (2):
                    setSpinner(R.array.choice);
                    break;
                case (3):
                    setSpinner(R.array.core);
                    break;
                case (4):
                    setSpinner(R.array.general);
                    break;
                case (5):
                    setSpinner(R.array.special);
                    break;
                case (6):
                    setSpinner(R.array.hallym);
                    break;
                case (7):
                    setSpinner(R.array.etc);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private OnItemSelectedListener spinSelectedlistener2 = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            String isuStr = (String) spinner01.getSelectedItem();
            String divStr = (String) spinner02.getSelectedItem();
            if(!divStr.equals("구분")) {
                CursorToArray(isuStr, divStr);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private void CursorToArray(String s1, String s2) {
        mListArray = new ArrayList<SubjectClass>();
        mCursor = null;
        if (s1.equals("전공")) {
            mCursor = mDBHelper.getMajorColumns(s2);
        }
        else if (s1.equals("필수기초")) {
            mCursor = mDBHelper.getEssenColumns(s2);
        }
        else if (s1.equals("선택기초")) {
            mCursor = mDBHelper.getSelectColumns(s2);
        }
        else if (s1.equals("핵심교양")) {
            mCursor = mDBHelper.getCoreColumns(s2);
        }
        else if (s1.equals("일반교양")) {
            mCursor = mDBHelper.getGeneralColumns(s2);
        }
        else if (s1.equals("특별교과과정")) {
            mCursor = mDBHelper.getSpecialColumns(s2);
        }
        else if (s1.equals("한림소양")) {
            mCursor = mDBHelper.getHallymColumns(s2);
        }
        else if (s1.equals("기타")) {
            mCursor = mDBHelper.getEtcColumns(s2);
        }
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

            mListArray.add(mSubjectClass);

        }
        mAdapter = new CustomAdapter(this, mListArray);
        mListView.setAdapter(mAdapter);

    }



    public void setSpinner() {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this, R.array.isu_div, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner01.setAdapter(fAdapter);
    }

    public void setSpinner(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(this, itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner02.setAdapter(fAdapter);
    }


}

