package jin.hlschedule.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jin.hlschedule.Adapter.CustomAdapter_essen;
import jin.hlschedule.DB.DBHelper;
import jin.hlschedule.R;
import jin.hlschedule.Class.SubjectClass;


public class TimeTableActivity extends AppCompatActivity {
    ActionBarDrawerToggle drawerToggle;
    String[] drawer_str;
    TextView mon0, mon1, mon2, mon3, mon4, mon5, mon6, mon7, mon8, mon9, mon10, mon11, mon12,
            tue0, tue1, tue2, tue3, tue4, tue5, tue6, tue7, tue8, tue9, tue10, tue11, tue12,
            wed0, wed1, wed2, wed3, wed4, wed5, wed6, wed7, wed8, wed9, wed10, wed11, wed12,
            thu0, thu1, thu2, thu3, thu4, thu5, thu6, thu7, thu8, thu9, thu10, thu11, thu12,
            fri0, fri1, fri2, fri3, fri4, fri5, fri6, fri7, fri8, fri9, fri10, fri11, fri12;
    TextView caseText;
    EditText editText_over, editText_under;
    Spinner spinner_week, spinner_sub;
    Button button_include, button_except;
    public ArrayList<ArrayList<SubjectClass>> tmp;
    public ArrayList<ArrayList<SubjectClass>> result;
    public ArrayList<ArrayList<SubjectClass>> setResult;
    public ArrayList<SubjectClass> mAddArray;
    public ArrayList<SubjectClass> mLastArray;
    public ArrayList<String> name_list;
    public ArrayList<String> name_list_checked;
    LayoutInflater inflater;
    ListView listView;
    DrawerLayout drawerLayout;
    Context mContext;
    DBHelper mDBHelper;
    View dialogView;
    CheckBox checkBox_over, checkBox_under, checkBox_week, checkBox_sub;
    int cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog, null);
        cases = -1;
        mContext = this;


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        listView = (ListView) findViewById(R.id.drawer);
        mAddArray = getIntent().getParcelableArrayListExtra("addlist");
        mLastArray = new ArrayList<SubjectClass>();
        for (int i = 0; i < mAddArray.size(); i++) {
            SubjectClass sc = new SubjectClass(mAddArray.get(i).num, mAddArray.get(i).major, mAddArray.get(i).name, mAddArray.get(i).ban,
                    mAddArray.get(i).hak, mAddArray.get(i).isu, mAddArray.get(i).professor, mAddArray.get(i).time, mAddArray.get(i).classroom, mAddArray.get(i).onoff);
            mLastArray.add(sc);
        }
        for (int i = 0; i < mLastArray.size(); i++) {
            if (!mLastArray.get(i).bool)
                createTextview();
            else {

            }
        }

        name_list = new ArrayList<String>();
        name_list_checked = new ArrayList<String>();
        for (int i = 0; i < mLastArray.size(); i++) {
            if (!name_list.contains(mLastArray.get(i).name))
                name_list.add(mLastArray.get(i).name);
        }

        tmp = createSubject(mLastArray, mLastArray.size() - 1);
        result = removeSubject((ArrayList<ArrayList<SubjectClass>>) tmp.clone());

        drawer_str = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
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

        mDBHelper = new DBHelper(mContext);
        mDBHelper.open();
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        caseText = (TextView) findViewById(R.id.caseTextview);
        caseText.setText("");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                initialTextView();
                setTextview(result.get(position));
                int sum = 0;
                for (int i = 0; i < result.get(position).size(); i++) {
                    sum += result.get(position).get(i).hak;
                }
                caseText.setText("희망과목 " + mAddArray.size() + "개 중 " + result.get(position).size() + "개 수강" + "(" + sum + "학점)");
                cases = position;
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
        getMenuInflater().inflate(R.menu.menu_xml, menu);
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

        if (id == R.id.action_set) {
            inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.dialog, null);
            name_list_checked.clear();
            CustomAdapter_essen mCustomAdapter;
            ListView mEssenList = (ListView) dialogView.findViewById(R.id.listView_essential);
            mCustomAdapter = new CustomAdapter_essen(TimeTableActivity.this, name_list);
            mEssenList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mEssenList.setAdapter(mCustomAdapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            builder.setTitle("설 정")
                    .setPositiveButton("완료",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult = new ArrayList<ArrayList<SubjectClass>>();
                                    boolean[] checkBool = new boolean[5];
                                    checkBox_over = (CheckBox) dialogView.findViewById(R.id.checkBox_over);
                                    checkBox_under = (CheckBox) dialogView.findViewById(R.id.checkBox_under);
                                    checkBox_week = (CheckBox) dialogView.findViewById(R.id.checkBox_week);
                                    checkBox_sub = (CheckBox) dialogView.findViewById(R.id.checkBox_sub);

                                    editText_over = (EditText) dialogView.findViewById(R.id.editText_over);
                                    editText_under = (EditText) dialogView.findViewById(R.id.editText_under);

                                    spinner_week = (Spinner) dialogView.findViewById(R.id.spinner_week);
                                    spinner_sub = (Spinner) dialogView.findViewById(R.id.spinner_sub);

                                    if (checkBox_over.isChecked())
                                        checkBool[0] = true;

                                    if (checkBox_under.isChecked())
                                        checkBool[1] = true;

                                    if (checkBox_week.isChecked())
                                        checkBool[2] = true;

                                    if (checkBox_sub.isChecked())
                                        checkBool[3] = true;

                                    if (name_list_checked.size() > 0)
                                        checkBool[4] = true;

                                    if (checkBool[0] == true || checkBool[1] == true || checkBool[2] == true ||
                                            checkBool[3] == true || checkBool[4] == true) {
                                        setResult = setCheck((ArrayList<ArrayList<SubjectClass>>) result.clone(), checkBool);
                                        drawer_str = new String[setResult.size()];
                                        for (int i = 0; i < setResult.size(); i++) {
                                            drawer_str[i] = "시간표 " + (i + 1);
                                        }
                                        initialTextView();
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, drawer_str);
                                        caseText.setText("");
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                int sum = 0;
                                                for (int i = 0; i < setResult.get(position).size(); i++) {
                                                    sum += setResult.get(position).get(i).hak;
                                                }
                                                caseText.setText("희망과목 " + mAddArray.size() + "개 중 " + setResult.get(position).size() + "개 수강" + "(" + sum + "학점)");
                                                initialTextView();
                                                setTextview(setResult.get(position));

                                                drawerLayout.closeDrawer(listView);

                                            }
                                        });

                                    }


                                }
                            })
                    .setNegativeButton("취소", null);

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            return true;
        } else if (id == R.id.action_add) {
            Cursor mCursor = null;
            mCursor = mDBHelper.getMaxCase();
            int max=0;
            while (mCursor.moveToNext()) {
                if(max < mCursor.getInt(mCursor.getColumnIndex("수")))
                    max = mCursor.getInt(mCursor.getColumnIndex("수"));
            }

            if (cases != -1) {
                for (int i = 0; i < result.get(cases).size(); i++) {
                    mDBHelper.mDB.execSQL("INSERT INTO savelist VALUES ('" +
                            result.get(cases).get(i).num + "','" +
                            result.get(cases).get(i).major + "','" +
                            result.get(cases).get(i).name + "','" +
                            result.get(cases).get(i).ban + "'," +
                            result.get(cases).get(i).hak + ",'" +
                            result.get(cases).get(i).isu + "','" +
                            result.get(cases).get(i).professor + "','" +
                            result.get(cases).get(i).time + "','" +
                            result.get(cases).get(i).classroom + "','" +
                            result.get(cases).get(i).onoff + "'," +
                            (max+1)  + ");");

                }
                Toast.makeText(TimeTableActivity.this, "추가되었습니다", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    public ArrayList<ArrayList<SubjectClass>> setCheck(ArrayList<ArrayList<SubjectClass>> arrayList, boolean[] checkbool) {
        if (checkbool[0]) {
            if (!editText_over.getText().toString().equals("")) {
                int over = Integer.parseInt(editText_over.getText().toString());
                int sum = 0;
                ArrayList<Integer> over_index = new ArrayList<Integer>();
                over_index.clear();
                for (int i = 0; i < arrayList.size(); i++) {
                    for (int j = 0; j < arrayList.get(i).size(); j++) {
                        sum += arrayList.get(i).get(j).hak;
                    }
                    if (sum < over) {
                        if (!over_index.contains(i)) {
                            over_index.add(i);
                        }
                    }
                    sum = 0;
                }
                for (int k = over_index.size() - 1; k >= 0; k--) {
                    int tmpIndex = over_index.get(k);
                    if (over_index.size() > 0) {
                        arrayList.remove(tmpIndex);
                    }
                }
            }
        }
        if (checkbool[1]) {
            if (!editText_under.getText().toString().equals("")) {
                int under = Integer.parseInt(editText_under.getText().toString());
                int sum = 0;
                ArrayList<Integer> over_index = new ArrayList<Integer>();
                over_index.clear();
                for (int i = 0; i < arrayList.size(); i++) {
                    for (int j = 0; j < arrayList.get(i).size(); j++) {
                        sum += arrayList.get(i).get(j).hak;
                    }
                    if (sum > under) {
                        if (!over_index.contains(i)) {
                            over_index.add(i);
                        }
                    }
                    sum = 0;
                }
                for (int k = over_index.size() - 1; k >= 0; k--) {
                    int tmpIndex = over_index.get(k);
                    if (over_index.size() > 0) {
                        arrayList.remove(tmpIndex);
                    }
                }
            }
        }
        if (checkbool[2]) {
            int week = Integer.parseInt((String) spinner_week.getSelectedItem());
            ArrayList<Integer> week_index = new ArrayList<Integer>();
            ArrayList<String> dayCnt = new ArrayList<String>();
            week_index.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 0; j < arrayList.get(i).size(); j++) {
                    for (int k = 0; k < arrayList.get(i).get(j).timetable.length; k++) {
                        if (!dayCnt.contains(arrayList.get(i).get(j).timetable[k].charAt(0) + "")) {
                            dayCnt.add(arrayList.get(i).get(j).timetable[k].charAt(0) + "");
                            Log.d("check", "test");
                        }
                    }
                }
                if (dayCnt.size() != week) {
                    if (!week_index.contains(i))
                        week_index.add(i);
                }
                dayCnt.clear();
            }

            for (int k = week_index.size() - 1; k >= 0; k--) {
                int tmpIndex = week_index.get(k);
                if (week_index.size() > 0) {
                    arrayList.remove(tmpIndex);
                }
            }


        }

        if (checkbool[3]) {
            int sub = Integer.parseInt((String) spinner_sub.getSelectedItem());
            ArrayList<Integer> sub_index = new ArrayList<Integer>();
            sub_index.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).size() < sub) {
                    if (!sub_index.contains(i)) {
                        sub_index.add(i);
                    }
                }
            }
            for (int k = sub_index.size() - 1; k >= 0; k--) {
                int tmpIndex = sub_index.get(k);
                if (sub_index.size() > 0) {
                    arrayList.remove(tmpIndex);
                }
            }
        }

        if (checkbool[4]) {
            ArrayList<Integer> essen_index = new ArrayList<Integer>();
            ArrayList<ArrayList<SubjectClass>> tmp = new ArrayList<ArrayList<SubjectClass>>();
            essen_index.clear();
            for(int i=0;i<name_list_checked.size();i++){
                Log.v("checkbool",name_list_checked.get(i));
            }
            for (int i = 0; i < arrayList.size(); i++) {
                boolean[] boolean1 = new boolean[name_list_checked.size()];
                boolean boolean2 = true;
                for (int x = 0; x < boolean1.length; x++)
                    boolean1[x] = true;
                for (int j = 0; j < arrayList.get(i).size(); j++) {
                    for (int k = 0; k < name_list_checked.size(); k++) {
                        if (arrayList.get(i).get(j).name.equals(name_list_checked.get(k))) {
                            boolean1[k] = false; //있을때
                        }
                    }
                }
                for (int y = 0; y < boolean1.length; y++) {
                    if (boolean1[y])
                        boolean2 = false;
                }
                if (boolean2) {
                    if (!essen_index.contains(i)) {
                        essen_index.add(i);
                    }
                }

            }
            for (int k = 0; k < essen_index.size(); k++) {
                int tmpIndex = essen_index.get(k);
                if (essen_index.size() > 0) {
                    tmp.add(arrayList.get(tmpIndex));
                }
            }

            arrayList = (ArrayList<ArrayList<SubjectClass>>) tmp.clone();

        }

        return arrayList;
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