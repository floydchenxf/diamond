package com.floyd.diamond.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.floyd.diamond.R;

public class IndexFragmentPager extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.indexpager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_welcome);
        //根据id填充广告条的每一项
        int index = getArguments().getInt("imageid");
        switch (index) {
            case 1:
                imageView.setBackgroundResource(R.drawable.m1);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.m2);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.m3);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.m4);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.m5);
                break;

            default:
                break;
        }
        return view;
    }
}
