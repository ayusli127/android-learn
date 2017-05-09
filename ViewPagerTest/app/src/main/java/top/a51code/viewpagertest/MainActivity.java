package top.a51code.viewpagertest;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private class Sample{
        private CharSequence title;
        private Class<? extends Activity> activityClass;
        public Sample(int titleResId ,Class<? extends Activity> activityClass){
            this.title = getResources().getString(titleResId);
            this.activityClass = activityClass;
        }
        public String toString(){
            return  title.toString();
        }
    }
    private static Sample[] mSamples;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSamples = new Sample[]{
                new Sample(R.string.title_screen_slide,ScreenSlidePagerActivity.class),
                new Sample(R.string.title_activity_zoom,ZoomActivity.class)
        };
        setListAdapter(new ArrayAdapter<Sample>(this,android.R.layout.simple_list_item_1,android.R.id.text1,mSamples));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(MainActivity.this,mSamples[position].activityClass));
    }
}
