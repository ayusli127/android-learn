package top.a51code.viewpagertest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page,container,false);
        ((TextView)rootView.findViewById(android.R.id.text1)).setText(getString(R.string.title_template_step,mPageNumber+1));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
    public int getPageNumber(){
        return mPageNumber;
    }
    public static ScreenSlidePageFragment create(int pageNumber){
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,pageNumber);
        fragment.setArguments(args);
        return  fragment;
    }

    public ScreenSlidePageFragment() {
    }
}
