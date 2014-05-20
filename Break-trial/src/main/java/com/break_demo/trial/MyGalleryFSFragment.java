package com.break_demo.trial;



import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.break_demo.trial.util.SystemUiHider;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MyGalleryFSFragment extends Fragment {

    private Drawable mDrawable;
    private static SystemUiHider mSystemUiHider;
    public MyGalleryFSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rV = inflater.inflate(R.layout.fragment_my_gallery_f, container, false);

        ((ImageView) rV.findViewById(R.id.imageViewFS)).setImageDrawable(mDrawable);
        rV.findViewById(R.id.imageViewFS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemUiHider != null) {
                    mSystemUiHider.toggle();
                }
            }
        });
        return rV;
    }

    public void setDrawable(Drawable d) {
        mDrawable = d;
    }
    public static void setSystemUiHider(SystemUiHider s) {
        mSystemUiHider = s;
    }
}
