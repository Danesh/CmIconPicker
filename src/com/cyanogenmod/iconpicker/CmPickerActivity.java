package com.cyanogenmod.iconpicker;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class CmPickerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Intent in = new Intent();
                DrawableInfo d = (DrawableInfo) adapterView.getAdapter().getItem(position);
                in.putExtra("image_normal", ((BitmapDrawable)d.draw_normal).getBitmap());
                in.putExtra("image_activated", ((BitmapDrawable)d.draw_activated).getBitmap());
                setResult(Activity.RESULT_OK, in);
                finish();
            }
        });
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<DrawableInfo> mDrawables = new ArrayList<DrawableInfo>();

        public ImageAdapter(Context c) {
            mContext = c;
            Field[] drawables = R.drawable.class.getFields();
            for (Field f : drawables) {
                if (f.getName().startsWith("ic_") && f.getName().endsWith("_normal")) {
                    int idNorm = getResources().getIdentifier(f.getName(), "drawable", getPackageName());
                    int idAct = getResources().getIdentifier(f.getName().replaceAll("_normal", "_activated"), "drawable", getPackageName());
                    if (idNorm != 0 && idAct != 0) {
                        Drawable drawNorm = getResources().getDrawable(idNorm);
                        Drawable drawAct = getResources().getDrawable(idAct);
                        if (drawNorm != null && drawAct != null) {
                            mDrawables.add(new DrawableInfo(drawNorm, drawAct));
                        }
                    }
                }
            }
        }

        public int getCount() {
            return mDrawables.size();
        }

        public Object getItem(int position) {
            return mDrawables.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageDrawable(((DrawableInfo) getItem(position)).draw_normal);
            return imageView;
        }

    }
    
    private class DrawableInfo {
        Drawable draw_normal;
        Drawable draw_activated;
        DrawableInfo(Drawable norm, Drawable act) {
            draw_normal = norm;
            draw_activated = act;
        }
    }
}