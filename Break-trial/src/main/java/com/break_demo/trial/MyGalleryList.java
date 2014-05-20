package com.break_demo.trial;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by shawnhu on 5/17/14.
 */
public class MyGalleryList {
    private static MyGalleryList mMyGalleryList;
    private static ArrayList<MyPicture> mList;
    private static int mCurPos;
    private static Context mContext;

    enum PathOrient {
        ASS,
    };

    private MyGalleryList() {
        mCurPos = 0;
    }

    public static MyGalleryList getInstance(Context context) {
        if (mMyGalleryList == null) {
            mMyGalleryList = new MyGalleryList();
        }
        if (mList == null) {
            mList = new ArrayList<MyPicture>();
        }
        if (mContext == null) {
            setContext(context);
        }

        return mMyGalleryList;
    }

    private static MyGalleryList setContext(Context context) {
        mContext = context;
        return mMyGalleryList;
    }

    public void loadGalleryFromAss(String p){
        Map<String, Integer[]> resIdMap = new LinkedHashMap<String, Integer[]>();
        resIdMap.put(p + "/image1.jpg", new Integer[] {R.string.image1_title, R.string.image1_descs});
        resIdMap.put(p + "/image2.jpg", new Integer[] {R.string.image2_title, R.string.image2_descs});
        resIdMap.put(p + "/image3.jpg", new Integer[] {R.string.image3_title, R.string.image3_descs});
        resIdMap.put(p + "/image4.jpg", new Integer[] {R.string.image4_title, R.string.image4_descs});
        resIdMap.put(p + "/image5.jpg", new Integer[] {R.string.image5_title, R.string.image5_descs});
        resIdMap.put(p + "/image6.jpg", new Integer[] {R.string.image6_title, R.string.image6_descs});
        resIdMap.put(p + "/image7.jpg", new Integer[] {R.string.image7_title, R.string.image7_descs});
        resIdMap.put(p + "/image8.jpg", new Integer[] {R.string.image8_title, R.string.image8_descs});
        resIdMap.put(p + "/image9.jpg", new Integer[] {R.string.image9_title, R.string.image9_descs});
        resIdMap.put(p + "/image10.jpg", new Integer[] {R.string.image10_title, R.string.image10_descs});
        resIdMap.put(p + "/image11.jpg", new Integer[] {R.string.image11_title, R.string.image11_descs});
        resIdMap.put(p + "/image12.jpg", new Integer[] {R.string.image12_title, R.string.image12_descs});

        Iterator i = resIdMap.entrySet().iterator();
        int index = 0;
        while (i.hasNext()) {
            Map.Entry pairs = (Map.Entry) i.next();
            String path = (String) pairs.getKey();
            Integer ids[] = (Integer[] )pairs.getValue();
            String t = mContext.getString(ids[0]);
            String d = mContext.getString(ids[1]);
            MyPicture myPicture = new MyPicture(t, d, path, PathOrient.ASS);
            mList.add(index, myPicture);
            index++;
        }
        load(mCurPos);
    }

    private void unloadCurrent() {
        mList.get(mCurPos).drawable = null;
    }

    private void loadFromAss(int p, MyPicture pic) throws IOException {
        InputStream i = mContext.getAssets().open(pic.path);
        pic.drawable = Drawable.createFromStream(i, null);
    }

    public void load(int p) {
        unloadCurrent();
        mCurPos = p;
        MyPicture curPic = mList.get(mCurPos);
        try {
            switch (curPic.pathOrient) {
                case ASS:
                    loadFromAss(p, curPic);
                    break;
                default:
                    throw new IOException("Ops, can't load from there");
            }
        } catch(IOException e) {
            curPic.title = mContext.getResources().getString(R.string.ops_title);
            curPic.descs = mContext.getResources().getString(R.string.ops_desc);
            curPic.drawable = mContext.getResources().getDrawable(R.drawable.notfound);

            e.printStackTrace();
        }
    }

    public void loadPre() {
        int p = mCurPos - 1;
        p = p < 0 ? (mList.size() - 1) : p;
        load(p);
    }

    public void loadNext() {
        int p = mCurPos + 1;
        p = p < mList.size() ? p : 0;
        load(p);
    }

    public int currentIndex() {
        return mCurPos;
    }

    public Drawable synchronizedLoadNGetDrawable(int p) {
        synchronized (this) {
            load(p);
            return getCurrentDrawable();
        }
    }
    public String synchronizedLoadNGetPath(int p) {
        synchronized (this) {
            load(p);
            return getCurrentPath();
        }
    }
    public Drawable getCurrentDrawable() {
        return mList.get(mCurPos).drawable;
    }

    public String getCurrentTitle() {
        return mList.get(mCurPos).title;
    }

    public String getCurrentDesc() {
        return mList.get(mCurPos).descs;
    }

    public String getCurrentPath() {
        return mList.get(mCurPos).path;
    }

    public int getCount() {
        return mList.size();
    }

    protected class MyPicture {
        MyPicture(String t, String de, String p, PathOrient po) {
            title = t;
            descs = de;
            path  = p;
            drawable = null;
            pathOrient = po;
        }
        String title;
        String descs;
        String path;
        PathOrient pathOrient;
        Drawable drawable;
    }
}
