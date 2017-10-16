package com.apps.philipps.opengltest.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.opengltest.Backend;
import com.apps.philipps.opengltest.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper._3D.Activity3D;
import com.apps.philipps.source.helper._3D.SurfaceView3D;
import com.apps.philipps.source.helper.chartdisplay.ChartUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class TGame extends Activity3D {
    private ProgressDialog pd = null;
    private MyGLRenderer renderer3D = null;
    private TextView how_good;
    private TextView highscore;
    private TextView theend;
    private TextView score;
    private double breathdata;
    private double testdata;
    private LineChart myChart;
    private LineData chartData;
    private LineDataSet breathChartData;
    private LineDataSet breathPlaneChartData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tgame);
        init();
        new BackGroundTask().execute();
    }

    private void init() {
        renderer3D = new MyGLRenderer(this);
        openGL = (SurfaceView3D) findViewById(R.id.gl_surface_view);
        openGL.setRenderer(renderer3D);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/slackeyregular.ttf");
        how_good = (TextView) findViewById(R.id.how_good);
        how_good.setTextColor(Color.WHITE);
        how_good.setTypeface(myCustomFont);
        how_good.setTextSize(10f);
        highscore = (TextView) findViewById(R.id.highscore);
        highscore.setTextColor(Color.WHITE);
        highscore.setTypeface(myCustomFont);
        highscore.setTextSize(10f);
        theend = (TextView) findViewById(R.id.theend);
        theend.setTextColor(Color.WHITE);
        theend.setTextSize(20f);
        theend.setTypeface(myCustomFont);
        score = (TextView) findViewById(R.id.score);
        score.setTextColor(Color.YELLOW);
        score.setTypeface(myCustomFont);
        score.setTextSize(20f);
        myChart = ChartUtil.createLineChart(this);
        addContentView(myChart, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 400));
        chartData = ChartUtil.createData();
        myChart.setData(chartData);
        myChart.bringToFront();
        breathChartData = ChartUtil.createDataSet("BreathData", Color.RED);
        chartData.addDataSet(breathChartData);
        breathPlaneChartData = ChartUtil.createDataSet("PlanData", Color.GREEN);
        chartData.addDataSet(breathPlaneChartData);
        chartData.notifyDataChanged();
        AppState.recordData = AppState.inGame = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        renderer3D.gameEngine.pause(renderer3D.gameEngine.isRunning());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Backend.score = 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    class BackGroundTask extends
            AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(TGame.this, "Loading...",
                    "Loading. Please wait...", true, false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TGame.this.pd.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            while (TGame.this.renderer3D.gameEngine == null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            TGame.this.pd.dismiss();
            initPlan();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }


        private void initPlan() {
            setTextViewHowGood();
            PlanManager.start();
        }

    }

    private void refreshChart() {
        testdata = BreathData.get(0).data * getRandomNumber(0, 1);
        breathdata = BreathData.get(0).data;

        breathChartData.addEntry(new Entry(breathChartData.getEntryCount(), (float) breathdata));
        breathPlaneChartData.addEntry(new Entry(breathPlaneChartData.getEntryCount(), (float) testdata));
        breathChartData.notifyDataSetChanged();
        chartData.notifyDataChanged();
        myChart.notifyDataSetChanged();
        myChart.refreshDrawableState();
        myChart.invalidate();
        myChart.setVisibleXRange(6, 60);
        myChart.moveViewToX(breathPlaneChartData.getEntryCount() - 60);
    }

    private int getRandomNumber(int Min, int Max) {

        return Min + (int) (Math.random() * ((Max - Min) + 1));
    }

    private void setTextViewHowGood() {
        new Thread() {
            public void run() {
                while (renderer3D.gameEngine.isRunning()) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                AppState.recordData = true;
                                long seconds = PlanManager.getDuration() / 1000;
                                String left = (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                                        + (seconds != 0 ? seconds % 60 + ":" : "")
                                        + PlanManager.getDuration() % 1000;
                                how_good.setText(BreathInterpreter.getStatus().getError().toString());
                                highscore.setText("Time left: " + left + " Best score: " + Backend.highscore);
                                score.setText("Score: " + Backend.score);
                                refreshChart();
                            }
                        });
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String text = "You get " + Backend.score + " Coins";
                        text = text + "\n" + "High scores:";
                        for (Object o : Backend.cacheManager.loadHighScore(Backend.gName))
                            text = text + "\n" + (int) o;
                        theend.setText(text);

                    }
                });

            }
        }.start();
    }
}
