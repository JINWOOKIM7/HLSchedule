package jin.hlschedule.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;

import jin.hlschedule.Network.CookieManager;
import jin.hlschedule.R;
import jin.hlschedule.Network.URLManager;

/**
 * Created by JIN on 2016-07-26.
 */
public class LoginActivity extends AppCompatActivity {
    private String strHak, strPass;
    ArrayList<String> arrlist,mon,tue,wed,thu,fri,timeArray,timeArray2;
    private TextView caseTextview;
    String year;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout3);
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        strHak = prefs.getString("hak", "");
        strPass = prefs.getString("pass", "");

        caseTextview = (TextView)findViewById(R.id.caseTextview3);
        arrlist = new ArrayList<String>();
        mon = new ArrayList<String>();
        tue = new ArrayList<String>();
        wed = new ArrayList<String>();
        thu = new ArrayList<String>();
        fri = new ArrayList<String>();
        timeArray = new ArrayList<String>();
        timeArray2 = new ArrayList<String>();
        BackgroundTask task = new BackgroundTask(strHak, strPass);
        task.execute();



    }

    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        String hak, pass;

        protected void onPreExecute() {
        }

        public BackgroundTask(String hak, String pass) {
            this.hak = hak;
            this.pass = pass;
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            try {
                String loginurl = "http://was1.hallym.ac.kr:8084/Haksa_u/menu/main.jsp";
                String targeturl = "http://was1.hallym.ac.kr:8084/Haksa_u/menu/sukang_siganpyo.jsp?hakbun=" + strHak;
                String session = CookieManager.getInstance().doPost(loginurl, "UserID=" + strHak + "&Password=" + strPass, null);
                URLConnection conn = CookieManager.getInstance().setCookie(targeturl, session);
                InputStream in = conn.getInputStream();
                Document doc = Jsoup.parse(in, URLManager.ENCODING_EUCKR, "");
                Elements td = doc.select("td[class=small]");
                Elements al = td.select("[align=center]");

                Elements td2 = doc.select("span[class=ltd12]");

                year = td2.get(0).text();


                for (int i = 0; i < al.size(); i++) {
                    String strtmp = al.get(i).toString();
                    if (strtmp.contains("bgcolor")) {
                        al.remove(i);
                        i -= 1;
                    }
                }

                for (int j = 0; j < al.size(); j++) {
                    String str = al.get(j).text();
                    str = str.replaceAll("/", "\n");
                    String str1;
                    String str2;
                    int index = str.indexOf(" (");
                    int index2 = str.indexOf(")");
                    if(index != -1 && index2 != -1){
                        str1 = str.substring(0,index);
                        str2 = str.substring(index2+1,str.length());
                        str = str1 + str2;
                    }
                    arrlist.add(str);


                }
                for (int k = 0; k < arrlist.size(); k++) {
                    switch (k % 5) {
                        case 0:
                            mon.add(arrlist.get(k));
                            break;
                        case 1:
                            tue.add(arrlist.get(k));
                            break;
                        case 2:
                            wed.add(arrlist.get(k));
                            break;
                        case 3:
                            thu.add(arrlist.get(k));
                            break;
                        case 4:
                            fri.add(arrlist.get(k));
                            break;
                    }
                }
                timeArray.addAll(mon);
                timeArray.addAll(tue);
                timeArray.addAll(wed);
                timeArray.addAll(thu);
                timeArray.addAll(fri);


            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Integer a) {
            caseTextview.setText(year);
            for (int i = 0; i < timeArray.size(); i++) {
                if(timeArray.get(i).length() > 1){
                    if(!timeArray2.contains(timeArray.get(i)))
                        timeArray2.add(timeArray.get(i));
                }
            }
            int timeSize = timeArray2.size();
            String[] timeCnt = new String[timeSize];
            for (int i = 0; i < timeArray2.size(); i++) {
                timeCnt[i] = timeArray2.get(i);
            }


            TextView[] textViewMon = { (TextView) findViewById(R.id.mon1),(TextView) findViewById(R.id.mon2),(TextView) findViewById(R.id.mon3),
                    (TextView) findViewById(R.id.mon4),(TextView) findViewById(R.id.mon5),(TextView) findViewById(R.id.mon6),
                    (TextView) findViewById(R.id.mon7),(TextView) findViewById(R.id.mon8),(TextView) findViewById(R.id.mon9),
                    (TextView) findViewById(R.id.mon10),(TextView) findViewById(R.id.mon11),(TextView) findViewById(R.id.mon12)
            };

            TextView[] textViewTue = { (TextView) findViewById(R.id.tue1),(TextView) findViewById(R.id.tue2),(TextView) findViewById(R.id.tue3),
                    (TextView) findViewById(R.id.tue4),(TextView) findViewById(R.id.tue5),(TextView) findViewById(R.id.tue6),
                    (TextView) findViewById(R.id.tue7),(TextView) findViewById(R.id.tue8),(TextView) findViewById(R.id.tue9),
                    (TextView) findViewById(R.id.tue10),(TextView) findViewById(R.id.tue11),(TextView) findViewById(R.id.tue12)};

            TextView[] textViewWed = { (TextView) findViewById(R.id.wed1),(TextView) findViewById(R.id.wed2),(TextView) findViewById(R.id.wed3),
                    (TextView) findViewById(R.id.wed4),(TextView) findViewById(R.id.wed5),(TextView) findViewById(R.id.wed6),
                    (TextView) findViewById(R.id.wed7),(TextView) findViewById(R.id.wed8),(TextView) findViewById(R.id.wed9),
                    (TextView) findViewById(R.id.wed10),(TextView) findViewById(R.id.wed11),(TextView) findViewById(R.id.wed12)};

            TextView[] textViewThu = { (TextView) findViewById(R.id.thu1),(TextView) findViewById(R.id.thu2),(TextView) findViewById(R.id.thu3),
                    (TextView) findViewById(R.id.thu4),(TextView) findViewById(R.id.thu5),(TextView) findViewById(R.id.thu6),
                    (TextView) findViewById(R.id.thu7),(TextView) findViewById(R.id.thu8),(TextView) findViewById(R.id.thu9),
                    (TextView) findViewById(R.id.thu10),(TextView) findViewById(R.id.thu11),(TextView) findViewById(R.id.thu12),
            };

            TextView[] textViewFri = {(TextView) findViewById(R.id.fri1),(TextView) findViewById(R.id.fri2),(TextView) findViewById(R.id.fri3),
                    (TextView) findViewById(R.id.fri4),(TextView) findViewById(R.id.fri5),(TextView) findViewById(R.id.fri6),
                    (TextView) findViewById(R.id.fri7),(TextView) findViewById(R.id.fri8),(TextView) findViewById(R.id.fri9),
                    (TextView) findViewById(R.id.fri10),(TextView) findViewById(R.id.fri11),(TextView) findViewById(R.id.fri12)};

            for(int i=0;i<12;i++){
                for(int j=0;j<timeCnt.length;j++) {
                    if(mon.get(i).equals(timeCnt[j])){
                        textViewMon[i].setText(mon.get(i));
                        textViewMon[i].setBackground(getResources().getDrawable(R.drawable.timeview_xml + (j + 1)));
                    }
                    if(tue.get(i).equals(timeCnt[j])){
                        textViewTue[i].setText(tue.get(i));
                        textViewTue[i].setBackground(getResources().getDrawable(R.drawable.timeview_xml + (j + 1)));
                    }
                    if(wed.get(i).equals(timeCnt[j])){
                        textViewWed[i].setText(wed.get(i));
                        textViewWed[i].setBackground(getResources().getDrawable(R.drawable.timeview_xml + (j + 1)));
                    }
                    if(thu.get(i).equals(timeCnt[j])){
                        textViewThu[i].setText(thu.get(i));
                        textViewThu[i].setBackground(getResources().getDrawable(R.drawable.timeview_xml + (j + 1)));
                    }
                    if(fri.get(i).equals(timeCnt[j])){
                        textViewFri[i].setText(fri.get(i));
                        textViewFri[i].setBackground(getResources().getDrawable(R.drawable.timeview_xml + (j + 1)));
                    }
                }
            }
        }

    }


}


