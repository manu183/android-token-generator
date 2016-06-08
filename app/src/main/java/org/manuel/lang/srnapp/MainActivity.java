package org.manuel.lang.srnapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.manuel.lang.srnapp.tokenGenerator.TokenGeneration;


@SuppressWarnings("ALL")
public class MainActivity extends Activity {

    /**
     * MUST be private!!
     */
    private static final String sharedSecret = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam";
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get TextViews
        final TextView tokenText =(TextView)findViewById(R.id.token);
        final TextView progressText = (TextView) findViewById(R.id.progresstext);

        ////get the progress bar
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);

        //set token to current one before the progress bar starts
        tokenText.setText(TokenGeneration.getToken(sharedSecret) + "");

        //initial progess for the progress bar
        mProgressBar.setProgress((((int)System.currentTimeMillis()/1000)%60 + 30)%60);

        //change progress text before progress bar starts
        progressText.setText("New token will be generated in " + ((60 - ((int) System.currentTimeMillis() / 1000) % 60 + 30) % 60) + " seconds");

        //define the progress bar's countdown timer
        mCountDownTimer=new CountDownTimer(60000,1000) {
            //for each second change the progress bar and its text
            @Override
            public void onTick(long millisUntilFinished) {
                mProgressBar.setProgress((((int)System.currentTimeMillis()/1000)%60 + 30)%60);
                progressText.setText("New token will be generated in " + ((60 - ((int) System.currentTimeMillis() / 1000) % 60 + 30) % 60) + " seconds");
                if((((int)System.currentTimeMillis()/1000)%60 + 30)%60 == 0){
                    //not called initially
                    onFinish();
                }
            }

            //when the progress bar is finished
            @Override
            public void onFinish() {
                mProgressBar.setProgress((((int)System.currentTimeMillis()/1000)%60 + 30)%60);
                progressText.setText("New token will be generated in " + ((((int) System.currentTimeMillis() / 1000) % 60 + 30) % 60) + " seconds");
                mCountDownTimer.start();
                tokenText.setText(TokenGeneration.getToken(sharedSecret) + "");
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}