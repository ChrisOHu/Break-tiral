package com.break_demo.trial;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MyContentProvider extends ContentProvider {
    public static final String CONTENT_URI = "com.break_demo.trial";
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        return 0;
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) {
        // The asset file name should be the last path segment
        String assetName = uri.getPath();
        int begin_index = assetName.lastIndexOf(CONTENT_URI) + 2;
        assetName = assetName.substring(begin_index);

        // If the given asset name is empty, throw an exception
        if (TextUtils.isEmpty(assetName)) {
            return null;
        }

        try {
            // Try and return a file descriptor for the given asset name
            AssetManager am = getContext().getAssets();
            return am.openFd(assetName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
