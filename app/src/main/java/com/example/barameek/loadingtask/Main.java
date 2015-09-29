package com.example.barameek.loadingtask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Main extends Activity {
    ArrayList<Bitmap> arr_bm = new ArrayList<Bitmap>();

    int[] resId = { R.drawable.image01, R.drawable.image02, R.drawable.image03,
            R.drawable.image04, R.drawable.image05, R.drawable.image06,
            R.drawable.image07, R.drawable.image08, R.drawable.image09,
            R.drawable.image10, R.drawable.image11, R.drawable.image12,
            R.drawable.image13, R.drawable.image14, R.drawable.image15,
            R.drawable.image16, R.drawable.image17, R.drawable.image18,
            R.drawable.image19, R.drawable.image20, R.drawable.image21,
            R.drawable.image22, R.drawable.image23, R.drawable.image24,
            R.drawable.image25, R.drawable.image26, R.drawable.image27 };

    int width;
    /*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        for(int i = 0 ; i < resId.length ; i++) {
            Bitmap bm = decodeSampledBitmapFromResource(getResources()
                    , resId[i], 500, 500);
            if(bm.getWidth() > bm.getHeight()) {
                bm = Bitmap.createScaledBitmap(bm
                        , 500 * bm.getWidth() / bm.getHeight(), 500, false);
            } else if(bm.getWidth() < bm.getHeight()) {
                bm = Bitmap.createScaledBitmap(bm
                        , 500, 500 * bm.getHeight() / bm.getWidth(), false);
            } else {
                bm = Bitmap.createScaledBitmap(bm, 500, 500, false);
            }

            arr_bm.add(bm);
            System.gc();
        }

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(Main.this));
    }
    */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        new LoadViewTask().execute();
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        ProgressDialog pd;

        protected void onPreExecute()
        {
            pd = new ProgressDialog(Main.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("Loading...");
            pd.setMessage("Loading images...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();
        }

        protected Void doInBackground(Void... params)
        {
            try
            {
                synchronized (this)
                {
                    for(int i = 0 ; i < resId.length ; i++) {
                        Bitmap bm = decodeSampledBitmapFromResource(getResources()
                                , resId[i], 500, 500);
                        if(bm.getWidth() > bm.getHeight()) {
                            bm = Bitmap.createScaledBitmap(bm
                                    , 500 * bm.getWidth() / bm.getHeight()
                                    , 500, false);
                        } else if(bm.getWidth() < bm.getHeight()) {
                            bm = Bitmap.createScaledBitmap(bm, 500
                                    , 500 * bm.getHeight() / bm.getWidth(), false);
                        } else {
                            bm = Bitmap.createScaledBitmap(bm, 500, 500, false);
                        }

                        arr_bm.add(bm);
                        int c = (int)((100f / resId.length) * (i + 1));
                        publishProgress(c);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            pd.setProgress(values[0]);
        }

        protected void onPostExecute(Void result)
        {
            pd.dismiss();
            setContentView(R.layout.main);
            GridView gridview = (GridView) findViewById(R.id.gridView);
            gridview.setAdapter(new ImageAdapter(Main.this));
        }
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize =
                calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options
            , int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float)height / (float)reqHeight);
            final int widthRatio = Math.round((float)width / (float)reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return resId.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(width / 3
                        , width / 3));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(arr_bm.get(position));
            return imageView;
        }
    }
}
