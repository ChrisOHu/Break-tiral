package com.break_demo.trial;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends NavigationActivity {

    private MyGalleryList myGalleryList;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        ((FrameLayout) findViewById(R.id.container)).addView(v);

        myGalleryList = MyGalleryList.getInstance(getApplicationContext());
        myGalleryList.loadGalleryFromAss("gallery");

        findViewById(R.id.preButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.left2);
                    return true;
                }
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    myGalleryList.loadPre();
                    setCurrentPicture();
                    v.setBackgroundResource(R.drawable.left1);
                    return true;
                }

                return false;
            }
        });

        findViewById(R.id.nextButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.right2);
                    return true;
                }
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    myGalleryList.loadNext();
                    setCurrentPicture();
                    v.setBackgroundResource(R.drawable.right1);
                    return true;
                }

                return false;
            }
        });

        setCurrentPicture();

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFullScreen();
            }
        });

        LinearLayout l = (LinearLayout) findViewById(R.id.viewLayout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) l.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findViewById(R.id.textLayout).setVisibility(View.GONE);
                params.setMargins(0, 0, 0, 0);
                l.setLayoutParams(params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCurrentPicture();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        setCurrentPicture();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goFullScreen() {
        Intent i = new Intent(this, FullViewActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        LinearLayout l = (LinearLayout) findViewById(R.id.viewLayout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) l.getLayoutParams();
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                findViewById(R.id.textLayout).setVisibility(View.GONE);
                params.setMargins(0, 0, 0, 0);
                l.setLayoutParams(params);
                return;
            default:
                findViewById(R.id.textLayout).setVisibility(View.VISIBLE);
                params.setMargins(0, 70, 0, 20);
                l.setLayoutParams(params);
                return;
        }
    }

    protected void setCurrentPicture() {
        String title;
        String desc;
        Drawable d;

        title = myGalleryList.getCurrentTitle();
        desc  = myGalleryList.getCurrentDesc();
        d     = myGalleryList.getCurrentDrawable();

        ((TextView) findViewById(R.id.imageTitle)).setText(title);
        ((TextView) findViewById(R.id.imageDescs)).setText(desc);

        //Bitmap bmp_frame = BitmapFactory.decodeResource(getResources(), R.drawable.image_frame);
        //Bitmap bmp_image = ((BitmapDrawable) d).getBitmap();
        //Bitmap bmp_final = overlay(bmp_image, bmp_frame);
        //bmp_final = roundCornerBitmap(bmp_final, 160);
        Bitmap bmp_image = ((BitmapDrawable) d).getBitmap();
        //bmp_image = roundCornerBitmap(bmp_image, 100);
        bmp_image = roundCornerBitmapWithBorder(bmp_image, 0xffff0000, 50, 10, getApplicationContext());

        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bmp_image);

        //((ImageView) findViewById(R.id.imageView)).setImageDrawable(d);
        //((LayerDrawable)((ImageView) findViewById(R.id.imageView)).getDrawable())
        //        .setDrawableByLayerId(0, d);

        if (mShareActionProvider != null) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("content://" + MyContentProvider.CONTENT_URI + "/" + myGalleryList.getCurrentPath()));
            mShareActionProvider.setShareIntent(i);
        }
    }

    //TODO: these might go to bitmap processing factory
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        bmp1 = Bitmap.createScaledBitmap(bmp1, bmp2.getWidth(), bmp2.getHeight(), true);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static Bitmap roundCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //final int color = 0xff424242;
        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap roundCornerBitmapWithBorder(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        return output;
    }
}
