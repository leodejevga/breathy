package com.apps.philipps.opengltest.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
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
        pd = ProgressDialog.show(TGame.this, "Loading...",
                "Loading. Please wait...", true, false);
        new BackGroundTask().execute();
        init();
    }

    private void init() {
        renderer3D = new MyGLRenderer(this);
        openGL = (SurfaceView3D) findViewById(R.id.gl_surface_view);
        openGL.setRenderer(renderer3D);
        how_good = (TextView) findViewById(R.id.how_good);
        how_good.setTextColor(Color.WHITE);
        highscore = (TextView) findViewById(R.id.highscore);
        highscore.setTextColor(Color.WHITE);
        theend = (TextView) findViewById(R.id.theend);
        theend.setTextColor(Color.WHITE);
        theend.setTextSize(20f);
        score = (TextView) findViewById(R.id.score);
        score.setTextColor(Color.YELLOW);
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
        openGL.onPause();
        renderer3D.gameEngine.pause(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderer3D.gameEngine.pause(false);
        Backend.life = 3;
        Backend.score = 0;
        System.gc();
    }


    @Override
    protected void onResume() {
        super.onResume();
        openGL.onResume();
        System.gc();
    }

    class BackGroundTask extends
            AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            // showDialog(AUTHORIZING_DIALOG);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (TGame.this.pd != null) {
                TGame.this.pd.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            while (TGame.this.renderer3D.gameEngine == null) {
                //wait ultil object is loaded
            }
            TGame.this.pd.dismiss();
            initPlan();
            return true;

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
                                how_good.setText(BreathInterpreter.getStatus().getError().toString());
                                highscore.setText("Life: " + Backend.life + " High score: " + Backend.highscore);
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
                        if (renderer3D.gameEngine.isWin()) {
                            theend.setText("Congratulations !");
                        } else {
                            theend.setText("You need to breath better !");
                        }
                    }
                });

            }
        }.start();
    }
}
