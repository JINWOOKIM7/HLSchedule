package jin.hlschedule.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jin.hlschedule.R;


public class StartAdapter extends PagerAdapter {

    LayoutInflater inflater;

    public StartAdapter(LayoutInflater inflater) {
        // TODO Auto-generated constructor stub
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 7; //이미지 개수 리턴(그림이 10개라서 10을 리턴)
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view = null;

        view = inflater.inflate(R.layout.start_image, null);

        ImageView img = (ImageView) view.findViewById(R.id.img_viewpager_childimage);

        img.setImageResource(R.drawable.start01+position);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        container.removeView((View) object);

    }


    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v == obj;
    }

}
