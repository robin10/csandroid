package com.example.robin.dicegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int player1_totalscore;
    int player2_totalscore;
    int player1_currscore;
    int player2_currscore;
    int player_turn;
    TextView p1_total_text;
    TextView p2_total_text;
    TextView p1_curr_text;
    TextView p2_curr_text;
    TextView winner_text;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1_totalscore = 0;
        player2_totalscore = 0;
        player1_currscore = 0;
        player2_currscore = 0;
        player_turn = 1;

        p1_total_text = (TextView) findViewById(R.id.player1_totalscore);
        p2_total_text = (TextView) findViewById(R.id.player2_totalscore);
        p1_curr_text = (TextView) findViewById(R.id.player1_currscore);
        p2_curr_text = (TextView) findViewById(R.id.player2_currscore);
        winner_text = (TextView) findViewById(R.id.winner_text);

        img = (ImageView) findViewById(R.id.dice_image);


        Button roll_button = (Button)findViewById(R.id.roll_button);
        roll_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(player1_totalscore+player1_currscore >= 100)
                {
                    winner_text.setText("Player1 should hold!!!");
                }
                else if(player2_totalscore+player2_currscore >= 100)
                {
                    winner_text.setText("Player2 should hold!!!");
                }

                else {
                    Random r = new Random();
                    int dice_value = r.nextInt(6) + 1;

                    switch (dice_value) {
                        case 1:
                            img.setImageResource(R.drawable.dice1);
                            break;
                        case 2:
                            img.setImageResource(R.drawable.dice2);
                            break;
                        case 3:
                            img.setImageResource(R.drawable.dice3);
                            break;
                        case 4:
                            img.setImageResource(R.drawable.dice4);
                            break;
                        case 5:
                            img.setImageResource(R.drawable.dice5);
                            break;
                        case 6:
                            img.setImageResource(R.drawable.dice6);
                            break;
                    }

                    if (dice_value == 1) {
                        if (player_turn == 1)
                            player_turn = 2;
                        else
                            player_turn = 1;

                        player1_currscore = 0;
                        player2_currscore = 0;
                        dice_value = 0;
                    } else {
                        if (player_turn == 1) {
                            player1_currscore += dice_value;
                        } else {
                            player2_currscore += dice_value;
                        }
                    }
                }

                p1_curr_text.setText("Current: "+ player1_currscore);
                p2_curr_text.setText("Current: "+ player2_currscore);
                p1_total_text.setText("Player1: "+ player1_totalscore);
                p2_total_text.setText("Player2: "+ player2_totalscore);
            }
        });

        Button hold_button = (Button)findViewById(R.id.hold_button);
        hold_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                img.setImageResource(R.drawable.dice);

                if (player_turn == 1) {
                    player1_totalscore += player1_currscore;
                    player1_currscore = 0;
                    player_turn = 2;
                } else {
                    player2_totalscore += player2_currscore;
                    player2_currscore = 0;
                    player_turn = 1;
                }

                if(player1_totalscore+player1_currscore >= 100)
                {
                    winner_text.setText("Player1 wins!!!");
                }
                else if(player2_totalscore+player2_currscore >= 100)
                {
                    winner_text.setText("Player2 wins!!!");
                }

                p1_curr_text.setText("Current: "+ player1_currscore);
                p2_curr_text.setText("Current: "+ player2_currscore);
                p1_total_text.setText("Player1: "+ player1_totalscore);
                p2_total_text.setText("Player2: " + player2_totalscore);
            }
        });

        Button start_button = (Button)findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                img.setImageResource(R.drawable.dice);
                player1_currscore = 0;
                player2_currscore = 0;
                player1_totalscore = 0;
                player2_totalscore = 0;
                player_turn = 1;

                p1_curr_text.setText("Current: "+ player1_currscore);
                p2_curr_text.setText("Current: "+ player2_currscore);
                p1_total_text.setText("Player1: "+ player1_totalscore);
                p2_total_text.setText("Player2: " + player2_totalscore);
                winner_text.setText("Winner: ");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
