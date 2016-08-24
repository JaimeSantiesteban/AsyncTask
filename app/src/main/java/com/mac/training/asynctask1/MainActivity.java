package com.mac.training.asynctask1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView myTxt;
    TextView prog;
    int interval = 4000;
    private ProgressBar mProgress;
    MyTask myT;
    boolean taskIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTxt = ((TextView) this.findViewById(R.id.myTxt));
        prog = ((TextView) this.findViewById(R.id.prog));
        mProgress = (ProgressBar) findViewById(R.id.progressBar1);
        mProgress.setMax(10);
    }

    public void startThread(View view) {
        interval -= 500;
        //new MyTask().execute(interval);
        //new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, interval);
        myT = new MyTask();
        myT.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, interval);
    }

    /*

        @Override
        protected void onPause() {
            super.onPause();
            myT.cancel(true);
        }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (taskIsRunning) {
            myT.cancel(true);
        }
        outState.putBoolean("Key", taskIsRunning);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        taskIsRunning = savedInstanceState.getBoolean("Key", false);
        if (taskIsRunning) {
            myT = new MyTask();
            myT.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, interval);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    // Void void wrapper
    private class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskIsRunning = true;
        }

        // Run on Background
        @Override
        protected String doInBackground(Integer... myInt) {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(myInt[0] / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // publish to a different thread
                publishProgress(i);
            }
            // Cant do this from here
            //myTxt.setText(String.valueOf(myInt[0]));
            return String.valueOf(myInt[0]);
        }

        // Passing the progress typically integer
        // UI thread
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //prog.setText(values[0]);
            prog.setText(" " + (values[0] + 1));
            //

            mProgress.setProgress((values[0] + 1));
        }

        // Run in UI Thread
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            myTxt.setText(String.valueOf(str));
            Log.d("FT- ", "Done " + str);
            taskIsRunning = false;
        }


    }


}
