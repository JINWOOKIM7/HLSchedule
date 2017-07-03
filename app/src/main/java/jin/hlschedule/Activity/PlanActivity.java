package jin.hlschedule.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.R;
import jin.hlschedule.Class.SubjectClass;

/**
 * Created by JIN on 2016-07-26.
 */
public class PlanActivity  extends AppCompatActivity {
    ActionBarDrawerToggle drawerToggle;
    String[] drawer_str;
    TextView mon0, mon1, mon2, mon3, mon4, mon5, mon6, mon7, mon8, mon9, mon10, mon11, mon12,
            tue0, tue1, tue2, tue3, tue4, tue5, tue6, tue7, tue8, tue9, tue10, tue11, tue12,
            wed0, wed1, wed2, wed3, wed4, wed5, wed6, wed7, wed8, wed9, wed10, wed11, wed12,
            thu0, thu1, thu2, thu3, thu4, thu5, thu6, thu7, thu8, thu9, thu10, thu11, thu12,
            fri0, fri1, fri2, fri3, fri4, fri5, fri6, fri7, fri8, fri9, fri10, fri11, fri12;

    LayoutInflater inflater;
    ListView listView;
    DrawerLayout drawerLayout;
    Context mContext;
    DBHelper mDBHelper;
    TextView caseTextview;
    private Cursor mCursor;
    private ArrayList<SubjectClass> planList;
    private SubjectClass mSubjectClass;
    int casese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout2);
        listView = (ListView) findViewById(R.id.drawer2);
        caseTextview = (TextView) findViewById(R.id.caseTextview2);
        planList = new ArrayList<SubjectClass>() ;
        mContext = this;
        mDBHelper = new DBHelper(mContext);
        mDBHelper.open();

        mCursor = null;
        mCursor = mDBHelper.getMaxCase();
        int max=0;
        while (mCursor.moveToNext()) {
            if(max < mCursor.getInt(mCursor.getColumnIndex("수")))
                max = mCursor.getInt(mCursor.getColumnIndex("수"));
        }

        createTextview();

        drawer_str = new String[max];
        for (int i = 0; i < max; i++) {
            drawer_str[i] = "시간표 " + (i + 1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawer_str);
        listView.setAdapter(adapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                casese = position+1;
                initialTextView();
                planList.clear();
                mCursor = mDBHelper.getCase(position+1);
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
                    planList.add(mSubjectClass);
                }
                int sum=0;
                for(int i=0;i<planList.size();i++){
                    sum += planList.get(i).hak;
                }
                caseTextview.setText(sum + "학점");
                setTextview(planList);
                drawerLayout.closeDrawer(listView);

            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);




    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_xml2, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            mCursor = mDBHelper.getMaxCase();

            mDBHelper.mDB.execSQL("delete from savelist where 수 = " + casese);
            int index=0;
            mCursor = mDBHelper.getMaxCase();
            while (mCursor.moveToNext()) {
                index = mCursor.getInt(mCursor.getColumnIndex("수"));

                if(index > casese) {
                    mDBHelper.mDB.execSQL("update savelist set 수 = " + (index - 1) + " where 수 = " + (index));
                }
            }

            Toast toast = Toast.makeText(mContext, "삭제 되었습니다", Toast.LENGTH_LONG);
            toast.show();
            caseTextview.setText("");
            initialTextView();
            mCursor = null;
            mCursor = mDBHelper.getMaxCase();
            int max=0;
            while (mCursor.moveToNext()) {
                if(max < mCursor.getInt(mCursor.getColumnIndex("수")))
                    max = mCursor.getInt(mCursor.getColumnIndex("수"));
            }

            drawer_str = new String[max];
            for (int i = 0; i < max; i++) {
                drawer_str[i] = "시간표 " + (i + 1);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawer_str);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };

            drawerLayout.setDrawerListener(drawerToggle);

        }
        return super.onOptionsItemSelected(item);
    }

    public void createTextview() {
        mon0 = (TextView) findViewById(R.id.mon0);
        mon1 = (TextView) findViewById(R.id.mon1);
        mon2 = (TextView) findViewById(R.id.mon2);
        mon3 = (TextView) findViewById(R.id.mon3);
        mon4 = (TextView) findViewById(R.id.mon4);
        mon5 = (TextView) findViewById(R.id.mon5);
        mon6 = (TextView) findViewById(R.id.mon6);
        mon7 = (TextView) findViewById(R.id.mon7);
        mon8 = (TextView) findViewById(R.id.mon8);
        mon9 = (TextView) findViewById(R.id.mon9);
        mon10 = (TextView) findViewById(R.id.mon10);
        mon11 = (TextView) findViewById(R.id.mon11);
        mon12 = (TextView) findViewById(R.id.mon12);

        tue0 = (TextView) findViewById(R.id.tue0);
        tue1 = (TextView) findViewById(R.id.tue1);
        tue2 = (TextView) findViewById(R.id.tue2);
        tue3 = (TextView) findViewById(R.id.tue3);
        tue4 = (TextView) findViewById(R.id.tue4);
        tue5 = (TextView) findViewById(R.id.tue5);
        tue6 = (TextView) findViewById(R.id.tue6);
        tue7 = (TextView) findViewById(R.id.tue7);
        tue8 = (TextView) findViewById(R.id.tue8);
        tue9 = (TextView) findViewById(R.id.tue9);
        tue10 = (TextView) findViewById(R.id.tue10);
        tue11 = (TextView) findViewById(R.id.tue11);
        tue12 = (TextView) findViewById(R.id.tue12);

        wed0 = (TextView) findViewById(R.id.wed0);
        wed1 = (TextView) findViewById(R.id.wed1);
        wed2 = (TextView) findViewById(R.id.wed2);
        wed3 = (TextView) findViewById(R.id.wed3);
        wed4 = (TextView) findViewById(R.id.wed4);
        wed5 = (TextView) findViewById(R.id.wed5);
        wed6 = (TextView) findViewById(R.id.wed6);
        wed7 = (TextView) findViewById(R.id.wed7);
        wed8 = (TextView) findViewById(R.id.wed8);
        wed9 = (TextView) findViewById(R.id.wed9);
        wed10 = (TextView) findViewById(R.id.wed10);
        wed11 = (TextView) findViewById(R.id.wed11);
        wed12 = (TextView) findViewById(R.id.wed12);

        thu0 = (TextView) findViewById(R.id.thu0);
        thu1 = (TextView) findViewById(R.id.thu1);
        thu2 = (TextView) findViewById(R.id.thu2);
        thu3 = (TextView) findViewById(R.id.thu3);
        thu4 = (TextView) findViewById(R.id.thu4);
        thu5 = (TextView) findViewById(R.id.thu5);
        thu6 = (TextView) findViewById(R.id.thu6);
        thu7 = (TextView) findViewById(R.id.thu7);
        thu8 = (TextView) findViewById(R.id.thu8);
        thu9 = (TextView) findViewById(R.id.thu9);
        thu10 = (TextView) findViewById(R.id.thu10);
        thu11 = (TextView) findViewById(R.id.thu11);
        thu12 = (TextView) findViewById(R.id.thu12);


        fri0 = (TextView) findViewById(R.id.fri0);
        fri1 = (TextView) findViewById(R.id.fri1);
        fri2 = (TextView) findViewById(R.id.fri2);
        fri3 = (TextView) findViewById(R.id.fri3);
        fri4 = (TextView) findViewById(R.id.fri4);
        fri5 = (TextView) findViewById(R.id.fri5);
        fri6 = (TextView) findViewById(R.id.fri6);
        fri7 = (TextView) findViewById(R.id.fri7);
        fri8 = (TextView) findViewById(R.id.fri8);
        fri9 = (TextView) findViewById(R.id.fri9);
        fri10 = (TextView) findViewById(R.id.fri10);
        fri11 = (TextView) findViewById(R.id.fri11);
        fri12 = (TextView) findViewById(R.id.fri12);

    }

    public void initialTextView() {

        mon1.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon2.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon3.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon4.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon5.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon6.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon7.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon8.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon9.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon10.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon11.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        mon12.setBackground(getResources().getDrawable(R.drawable.textview_xml));

        mon1.setText("");
        mon2.setText("");
        mon3.setText("");
        mon4.setText("");
        mon5.setText("");
        mon6.setText("");
        mon7.setText("");
        mon8.setText("");
        mon9.setText("");
        mon10.setText("");
        mon11.setText("");
        mon12.setText("");

        tue1.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue2.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue3.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue4.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue5.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue6.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue7.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue8.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue9.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue10.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue11.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        tue12.setBackground(getResources().getDrawable(R.drawable.textview_xml));

        tue1.setText("");
        tue2.setText("");
        tue3.setText("");
        tue4.setText("");
        tue5.setText("");
        tue6.setText("");
        tue7.setText("");
        tue8.setText("");
        tue9.setText("");
        tue10.setText("");
        tue11.setText("");
        tue12.setText("");


        wed1.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed2.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed3.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed4.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed5.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed6.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed7.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed8.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed9.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed10.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed11.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        wed12.setBackground(getResources().getDrawable(R.drawable.textview_xml));

        wed1.setText("");
        wed2.setText("");
        wed3.setText("");
        wed4.setText("");
        wed5.setText("");
        wed6.setText("");
        wed7.setText("");
        wed8.setText("");
        wed9.setText("");
        wed10.setText("");
        wed11.setText("");
        wed12.setText("");


        thu1.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu2.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu3.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu4.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu5.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu6.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu7.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu8.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu9.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu10.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu11.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        thu12.setBackground(getResources().getDrawable(R.drawable.textview_xml));


        thu1.setText("");
        thu2.setText("");
        thu3.setText("");
        thu4.setText("");
        thu5.setText("");
        thu6.setText("");
        thu7.setText("");
        thu8.setText("");
        thu9.setText("");
        thu10.setText("");
        thu11.setText("");
        thu12.setText("");

        fri1.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri2.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri3.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri4.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri5.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri6.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri7.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri8.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri9.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri10.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri11.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri12.setBackground(getResources().getDrawable(R.drawable.textview_xml));
        fri1.setText("");
        fri2.setText("");
        fri3.setText("");
        fri4.setText("");
        fri5.setText("");
        fri6.setText("");
        fri7.setText("");
        fri8.setText("");
        fri9.setText("");
        fri10.setText("");
        fri11.setText("");
        fri12.setText("");
    }

    public void setTextview(ArrayList<SubjectClass> array) {
        int timeSize = array.size();
        String[] timeCnt = new String[timeSize];
        for (int i = 0; i < array.size(); i++) {

            timeCnt[i] = array.get(i).name;
        }

        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.get(i).timetable.length; j++) {
                for (int k = 0; k < timeCnt.length; k++) {
                    if (k > 10) k -= 9;
                    if (array.get(i).name.equals(timeCnt[k])) {
                        switch (array.get(i).timetable[j]) {
                            ////////////
                            case "월1":
                                mon1.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon1.setText(array.get(i).name);
                                break;
                            case "월2":
                                mon2.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon2.setText(array.get(i).name);
                                break;
                            case "월3":
                                mon3.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon3.setText(array.get(i).name);
                                break;
                            case "월4":
                                mon4.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon4.setText(array.get(i).name);
                                break;
                            case "월5":
                                mon5.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon5.setText(array.get(i).name);
                                break;
                            case "월6":
                                mon6.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon6.setText(array.get(i).name);
                                break;
                            case "월7":
                                mon7.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon7.setText(array.get(i).name);
                                break;
                            case "월8":
                                mon8.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon8.setText(array.get(i).name);
                                break;
                            case "월9":
                                mon9.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon9.setText(array.get(i).name);
                                break;
                            case "월10":
                                mon10.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon10.setText(array.get(i).name);
                                break;
                            case "월11":
                                mon11.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon11.setText(array.get(i).name);
                                break;
                            case "월12":
                                mon12.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                mon12.setText(array.get(i).name);
                                break;
                            ////////////
                            case "화1":
                                tue1.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue1.setText(array.get(i).name);
                                break;
                            case "화2":
                                tue2.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue2.setText(array.get(i).name);
                                break;
                            case "화3":
                                tue3.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue3.setText(array.get(i).name);
                                break;
                            case "화4":
                                tue4.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue4.setText(array.get(i).name);
                                break;
                            case "화5":
                                tue5.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue5.setText(array.get(i).name);
                                break;
                            case "화6":
                                tue6.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue6.setText(array.get(i).name);
                                break;
                            case "화7":
                                tue7.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue7.setText(array.get(i).name);
                                break;
                            case "화8":
                                tue8.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue8.setText(array.get(i).name);
                                break;
                            case "화9":
                                tue9.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue9.setText(array.get(i).name);
                                break;
                            case "화10":
                                tue10.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue10.setText(array.get(i).name);
                                break;
                            case "화11":
                                tue11.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue11.setText(array.get(i).name);
                                break;
                            case "화12":
                                tue12.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                tue12.setText(array.get(i).name);
                                break;
                            ////////////
                            case "수1":
                                wed1.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed1.setText(array.get(i).name);
                                break;
                            case "수2":
                                wed2.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed2.setText(array.get(i).name);
                                break;
                            case "수3":
                                wed3.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed3.setText(array.get(i).name);
                                break;
                            case "수4":
                                wed4.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed4.setText(array.get(i).name);
                                break;
                            case "수5":
                                wed5.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed5.setText(array.get(i).name);
                                break;
                            case "수6":
                                wed6.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed6.setText(array.get(i).name);
                                break;
                            case "수7":
                                wed7.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed7.setText(array.get(i).name);
                                break;
                            case "수8":
                                wed8.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed8.setText(array.get(i).name);
                                break;
                            case "수9":
                                wed9.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed9.setText(array.get(i).name);
                                break;
                            case "수10":
                                wed10.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed10.setText(array.get(i).name);
                                break;
                            case "수11":
                                wed11.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed11.setText(array.get(i).name);
                                break;
                            case "수12":
                                wed12.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                wed12.setText(array.get(i).name);
                                break;
                            ////////////
                            case "목1":
                                thu1.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu1.setText(array.get(i).name);
                                break;
                            case "목2":
                                thu2.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu2.setText(array.get(i).name);
                                break;
                            case "목3":
                                thu3.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu3.setText(array.get(i).name);
                                break;
                            case "목4":
                                thu4.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu4.setText(array.get(i).name);
                                break;
                            case "목5":
                                thu5.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu5.setText(array.get(i).name);
                                break;
                            case "목6":
                                thu6.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu6.setText(array.get(i).name);
                                break;
                            case "목7":
                                thu7.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu7.setText(array.get(i).name);
                                break;
                            case "목8":
                                thu8.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu8.setText(array.get(i).name);
                                break;
                            case "목9":
                                thu9.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu9.setText(array.get(i).name);
                                break;
                            case "목10":
                                thu10.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu10.setText(array.get(i).name);
                                break;
                            case "목11":
                                thu11.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu11.setText(array.get(i).name);
                                break;
                            case "목12":
                                thu12.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                thu12.setText(array.get(i).name);
                                break;
                            ////////////
                            case "금1":
                                fri1.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri1.setText(array.get(i).name);
                                break;
                            case "금2":
                                fri2.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri2.setText(array.get(i).name);
                                break;
                            case "금3":
                                fri3.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri3.setText(array.get(i).name);
                                break;
                            case "금4":
                                fri4.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri4.setText(array.get(i).name);
                                break;
                            case "금5":
                                fri5.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri5.setText(array.get(i).name);
                                break;
                            case "금6":
                                fri6.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri6.setText(array.get(i).name);
                                break;
                            case "금7":
                                fri7.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri7.setText(array.get(i).name);
                                break;
                            case "금8":
                                fri8.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri8.setText(array.get(i).name);
                                break;
                            case "금9":
                                fri9.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri9.setText(array.get(i).name);
                                break;
                            case "금10":
                                fri10.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri10.setText(array.get(i).name);
                                break;
                            case "금11":
                                fri11.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri11.setText(array.get(i).name);
                                break;
                            case "금12":
                                fri12.setBackground(getResources().getDrawable(R.drawable.timeview_xml + (k + 1)));
                                fri12.setText(array.get(i).name);
                                break;
                        }
                    }
                }
            }
        }

    }
    public static ArrayList<ArrayList<SubjectClass>> createSubject(ArrayList<SubjectClass> create_arrayList, int index) {
        ArrayList<ArrayList<SubjectClass>> allSubsets;
        if (index == -1) {
            allSubsets = new ArrayList<ArrayList<SubjectClass>>();
            allSubsets.add(new ArrayList<SubjectClass>());
            return allSubsets;
        }
        allSubsets = createSubject(create_arrayList, index - 1);
        ArrayList<ArrayList<SubjectClass>> moreSets = new ArrayList<ArrayList<SubjectClass>>();
        SubjectClass item = create_arrayList.get(index);
        for (ArrayList<SubjectClass> set : allSubsets) {
            ArrayList<SubjectClass> addSet = new ArrayList<SubjectClass>(set);
            addSet.add(item);
            moreSets.add(addSet);
        }
        allSubsets.addAll(moreSets);
        return allSubsets;
    }

    public static ArrayList<ArrayList<SubjectClass>> removeSubject(ArrayList<ArrayList<SubjectClass>> remove_arrayList) {
        ArrayList<Integer> remove_index = new ArrayList<Integer>();
        for (int i = 0; i < remove_arrayList.size(); i++) {
            if (remove_arrayList.get(i).size() == 0) {
                remove_arrayList.remove(i);
            }

        }

        for (int i = 0; i < remove_arrayList.size(); i++) {
            for (int j = 0; j < remove_arrayList.get(i).size(); j++) { // 각 경우의 수
                for (int k = 0; k < remove_arrayList.get(i).size(); k++) {
                    if (!remove_arrayList.get(i).get(j).ban.equals(remove_arrayList.get(i).get(k).ban) &&
                            remove_arrayList.get(i).get(j).num.equals(remove_arrayList.get(i).get(k).num)) {
                        if (!remove_index.contains(i)) {
                            remove_index.add(i);
                        }
                    }
                    if (!remove_arrayList.get(i).get(j).name.equals(remove_arrayList.get(i).get(k).name)) {
                        if (remove_arrayList.get(i).get(j).num.equals(remove_arrayList.get(i).get(k).num)) {
                            if (!remove_index.contains(i)) {
                                remove_index.add(i);
                            }
                        }
                        for (int x = 0; x < remove_arrayList.get(i).get(j).timetable.length; x++) {
                            for (int y = 0; y < remove_arrayList.get(i).get(k).timetable.length; y++) {
                                if (remove_arrayList.get(i).get(j).timetable[x].equals(remove_arrayList.get(i).get(k).timetable[y])) {
                                    if (!remove_index.contains(i)) {
                                        remove_index.add(i);
                                    }
                                }

                            }
                        }

                    }
                }
            }

        }

        for (int i = remove_index.size() - 1; i >= 0; i--) {
            int removeIndex = remove_index.get(i);
            remove_arrayList.remove(removeIndex);
        }

        return remove_arrayList;
    }

}
