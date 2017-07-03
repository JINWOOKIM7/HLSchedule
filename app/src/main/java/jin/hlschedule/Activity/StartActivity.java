package jin.hlschedule.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import jin.hlschedule.R;
import jin.hlschedule.Adapter.StartAdapter;


public class StartActivity extends Activity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        StartAdapter adapter = new StartAdapter(getLayoutInflater());
        pager.setAdapter(adapter);

        findViewById(R.id.start_button).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
            startActivity(new Intent(v.getContext(), MainActivity.class));
        }
    };
}

