package com.example.termproject_jni_thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout gameBoard;
    ImageView gamePlayer;

    public static int locateX = 0;
    public static int locateY = 0;
    public static float blockWidth = 0;

    static {
        System.loadLibrary("jni-thread");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.game_board);
        gameBoard.setOnClickListener(clickListener);

        gamePlayer = findViewById(R.id.game_player);



    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.game_board :
//                    locateX++;
//                    gamePlayer.setX(gamePlayer.getWidth()*locateX);

                    locateY++;
                    gamePlayer.setY(gamePlayer.getHeight()*locateY);

                    break;
                default:
            }
        }
    };

}