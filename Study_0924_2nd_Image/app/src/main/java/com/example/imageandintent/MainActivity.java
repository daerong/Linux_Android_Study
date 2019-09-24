package com.example.imageandintent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation())
        );
        recyclerView.setLayoutManager(linearLayoutManager);

        List<MainItem> items = new ArrayList<>();
        items.add(new MainItem(R.drawable.pic1, "Title 1", "Man 1"));
        items.add(new MainItem(R.drawable.pic2, "Title 2", "Man 2"));
        items.add(new MainItem(R.drawable.pic3, "Title 3", "Man 3"));
        items.add(new MainItem(R.drawable.pic4, "Title 4", "Man 4"));
        items.add(new MainItem(R.drawable.pic5, "Title 5", "Man 5"));
        items.add(new MainItem(R.drawable.pic6, "Title 6", "Man 6"));
        items.add(new MainItem(R.drawable.pic7, "Title 7", "Man 7"));
        items.add(new MainItem(R.drawable.pic8, "Title 8", "Man 8"));
        items.add(new MainItem(R.drawable.pic9, "Title 9", "Man 9"));

        mainAdapter = new MainAdapter(this, items);
        recyclerView.setAdapter(mainAdapter);

    }
}
