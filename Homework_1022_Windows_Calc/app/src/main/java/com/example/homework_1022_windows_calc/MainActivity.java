package com.example.homework_1022_windows_calc;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    CalcFragment calcFragment;
    HistoryFragment historyFragment;
    PageAdapter adapter = new PageAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
        setupViewPager(mViewPager);
    }

    public void setupViewPager(ViewPager viewPager) {
        calcFragment = new CalcFragment();
        adapter.addFragment(calcFragment, "계산기");
        historyFragment = new HistoryFragment();
        adapter.addFragment(historyFragment, "기록");

        viewPager.setAdapter(adapter);
    }

    public void callHistoryAdd(String innerFormula, String innerResult){
        historyFragment.addHistory(innerFormula, innerResult);
    }
}
