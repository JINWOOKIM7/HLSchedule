package jin.hlschedule.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import jin.hlschedule.R;

public class SplashActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 1500);

    }


}
