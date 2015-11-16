package com.google.engedu.ghost;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String COMPUTER_WINS = "You formed a Word!! Computer Wins";
    private static final String USER_WINS = "Computer formed a Word!! YOU Win";
    private static final String COMPUTER_CHALLENGE = "Computer Challenged the Word and Wins!!";
    private static final String USER_CHALLENGE = "You Challenged the Word and Won!!";
    private static final String USER_CHALLENGE_WRONG = "You Challenged the Word and Lost!!";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        InputStream myStream = null;
        try {
            myStream = getAssets().open("words.txt");
            dictionary = new SimpleDictionary(myStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);


        Button challenge = (Button) findViewById(R.id.challenge);
        Button restart = (Button) findViewById(R.id.restart);
        final TextView text = (TextView) findViewById(R.id.ghostText);
        final TextView label = (TextView) findViewById(R.id.gameStatus);

        challenge.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String fragment = text.getText().toString();
                if(fragment.length()>=4 && dictionary.isWord(fragment)){
                    label.setText(USER_WINS);
                }
                else{
                    String word = dictionary.getAnyWordStartingWith(fragment);
                    if(word == null){
                        label.setText(USER_CHALLENGE);
                    }
                    else{
                        text.setText(word);
                        label.setText(USER_CHALLENGE_WRONG);
                    }
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStart(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        TextView text = (TextView) findViewById(R.id.ghostText);
        String fragment = text.getText().toString();

        if(fragment.length()>=4 && dictionary.isWord(fragment)){
            label.setText(COMPUTER_WINS);
        }
        else{
            String word = dictionary.getAnyWordStartingWith(fragment);
            if(word == null){
                label.setText(COMPUTER_CHALLENGE);
            }
            else{
                fragment = word.substring(0,fragment.length()+1);
                text.setText(fragment);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },2000);
        }
        return true;
    }

    public boolean onKeyUp (int keyCode, KeyEvent event){

     //   Toast.makeText(GhostActivity.this,"Hello--"+(int)'a'+"--"+keyCode,Toast.LENGTH_SHORT).show();
        if(keyCode >= 29 && keyCode <= 54){
            TextView label = (TextView) findViewById(R.id.gameStatus);
            TextView text = (TextView) findViewById(R.id.ghostText);

            String fragment = text.getText().toString();
            fragment = fragment + (char)event.getUnicodeChar();
            text.setText(fragment);

            userTurn = false;
            label.setText(COMPUTER_TURN);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 2000);
            return true;
        }
        else {
            return super.onKeyUp(keyCode, event);
        }
    }

}
