package com.example.study_1001_2nd_explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class RatingBarActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;
    Intent intent;

    List<String> listContent = Arrays.asList(
            "그림 1",
            "그림 2",
            "그림 3",
            "그림 4",
            "그림 5",
            "그림 6",
            "그림 7",
            "그림 8",
            "그림 9"
    );

    List<Integer> listResId;
    List<Integer> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        intent = getIntent();
        listResId = intent.getIntegerArrayListExtra("imageArr");
        ratings = intent.getIntegerArrayListExtra("ratingArr");

        init();
        getData();
    }

    void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    void getData() {
        // 임의의 데이터입니다.




        for (int i = 0; i < 9; i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            RecyclerData data = new RecyclerData();
            data.setImage(listResId.get(i));
            data.setText(listContent.get(i));
            data.setRate(ratings.get(i));


            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
