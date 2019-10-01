package com.example.study_1001_2nd_explicitintent;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> mThumbIds = new ArrayList<>();
    ArrayList<Integer> mTouch = new ArrayList<>();

    DisplayMetrics mMetrics;
    Button btnFinish;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mThumbIds.add(R.drawable.pic1);
        mThumbIds.add(R.drawable.pic2);
        mThumbIds.add(R.drawable.pic3);
        mThumbIds.add(R.drawable.pic4);
        mThumbIds.add(R.drawable.pic5);
        mThumbIds.add(R.drawable.pic6);
        mThumbIds.add(R.drawable.pic7);
        mThumbIds.add(R.drawable.pic8);
        mThumbIds.add(R.drawable.pic9);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);
        mTouch.add(0);


        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), RatingBarActivity.class);
                intent.putIntegerArrayListExtra("imageArr", mThumbIds);
                intent.putIntegerArrayListExtra("ratingArr", mTouch);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener
            = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int locate = (int)id;
            int touch = mTouch.get(locate);
            if(touch == 5) touch = 0;
            else touch++;
            mTouch.set(locate, touch);

            Toast.makeText(MainActivity.this,
                    "id : "+id+", touch : "+touch,
                    Toast.LENGTH_LONG).show();

        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.size();
        }

        public Object getItem(int position) {
            return mThumbIds.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            int rowWidth = (mMetrics.widthPixels) / 3;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth, (int) (rowWidth*1.6)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds.get(position));
            return imageView;
        }
    }

}
