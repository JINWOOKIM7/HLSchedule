package jin.hlschedule.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jin.hlschedule.Network.CookieManager;
import jin.hlschedule.R;
import jin.hlschedule.Network.URLManager;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch, btnBasket, btnPlan, btnTime;
    private EditText textViewHak, textViewPass;
    private CheckBox checkBox_login;
    private LayoutInflater inflater;
    private View dialogView;
    private String strHak, strPass;
    final static String PACKAGE_NAME = "jin.hlschedule";
    final static String DB_NAME = "list.sqlite";
    public static Context mContext;
    AdView mAdView;
    boolean result = false;

    protected void onCreate(Bundle savedInstanceState) {

        CreateStart();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mContext = this;
        if (!isCheckDB(mContext)) {
            copyDB(mContext);

        }

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnSearch = (Button) findViewById(R.id.button_search);
        btnBasket = (Button) findViewById(R.id.button_basket);
        btnPlan = (Button) findViewById(R.id.button_plan);
        btnTime = (Button) findViewById(R.id.button_timetable);

        if (btnSearch != null)
            btnSearch.setOnClickListener(mClickListener);
        if (btnBasket != null)
            btnBasket.setOnClickListener(mClickListener);
        if (btnPlan != null)
            btnPlan.setOnClickListener(mClickListener);
        if (btnTime != null)
            btnTime.setOnClickListener(mClickListener);


    }

    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_search:
                    Intent intentSearch = new Intent(getApplicationContext(), AddSearchActivity.class);
                    startActivity(intentSearch);
                    break;

                case R.id.button_basket:
                    Intent intentBasket = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intentBasket);
                    break;

                case R.id.button_plan:
                    Intent intentPlan = new Intent(getApplicationContext(), PlanActivity.class);
                    startActivity(intentPlan);
                    break;

                case R.id.button_timetable:
                    final SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = prefs.edit();
                    mContext = getApplicationContext();
                    inflater = getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.dialog_login, null);
                    textViewHak = (EditText) dialogView.findViewById(R.id.login_name);
                    textViewPass = (EditText) dialogView.findViewById(R.id.login_password);
                    checkBox_login = (CheckBox) dialogView.findViewById(R.id.checkBox_login);
                    if(prefs.getString("check", "").isEmpty()) {
                        AlertDialog.Builder aDialog = new AlertDialog.Builder(MainActivity.this);
                        aDialog.setTitle("로그인하시겠습니까?");
                        aDialog.setView(dialogView);
                        aDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                strHak = textViewHak.getText().toString();
                                strPass = textViewPass.getText().toString();
                                BackgroundTask task = new BackgroundTask(strHak, strPass);
                                task.execute();
                                try {
                                    if (result) {
                                        Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
                                        if (checkBox_login.isChecked()) {
                                            editor.putString("check", "exist");
                                            editor.putString("hak", strHak);
                                            editor.putString("pass", strPass);
                                            editor.commit();
                                        }
                                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intentLogin);
                                    } else
                                        Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }

                        });
                        aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog ad = aDialog.create();
                        ad.show();
                    }
                    else {
                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentLogin);
                    }
                    break;
            }
        }
    };

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

            return null;
        }

        protected void onPostExecute(Integer a) {
            result = login(hak, pass);
        }

    }

    public boolean login(String id, String pwd) {
        boolean lg = false;
        try {
            String loginurl = "http://was1.hallym.ac.kr:8084/Haksa_u/menu/main.jsp";
            String targeturl = "http://was1.hallym.ac.kr:8084/Haksa_u/menu/sukang_siganpyo.jsp?hakbun=" + id;
            String session = CookieManager.getInstance().doPost(loginurl, "UserID=" + id + "&Password=" + pwd, null);
            URLConnection conn = CookieManager.getInstance().setCookie(targeturl, session);
            InputStream in = conn.getInputStream();
            Document doc = Jsoup.parse(in, URLManager.ENCODING_EUCKR, "");
            if (doc.html().length() < 500) {
                lg = false;
            } else {
                lg = true;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return lg;
    }

    public boolean isCheckDB(Context mContext) {
        String filePath = "/data/data/" + PACKAGE_NAME + "/databases/" + DB_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }

        return false;

    }

    public void copyDB(Context mContext) {

        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + PACKAGE_NAME + "/databases";
        String filePath = "/data/data/" + PACKAGE_NAME + "/databases/" + DB_NAME;
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("db/" + DB_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            } else {
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }

    protected void CreateStart() {

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("check", "");
        if (pref.getString("check", "").isEmpty()) {
            startActivity(new Intent(this, StartActivity.class));
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("check", "exist");
        editor.commit();

    }
}
