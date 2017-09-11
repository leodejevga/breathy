package com.apps.philipps.opengltest.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private Integer breathdata;
    private Integer testdata;
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
        myChart = ChartUtil.createLineChart(this);
        addContentView( myChart, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,400 ) );
        chartData = ChartUtil.createData();
        myChart.setData( chartData );
        myChart.bringToFront();
        breathChartData = ChartUtil.createDataSet("BreathData", Color.RED );
        chartData.addDataSet( breathChartData );
        breathPlaneChartData = ChartUtil.createDataSet( "PlanData", Color.GREEN );
        chartData.addDataSet( breathPlaneChartData );
        chartData.notifyDataChanged();
        AppState.recordData = AppState.inGame = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        openGL.onPause();
        renderer3D.gameEngine.onPause();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openGL.onPause();
        renderer3D.gameEngine.onPause();
        System.gc();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        openGL.onResume();
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

        private void initPlan(){
            setTextViewHowGood();
            PlanManager.startPlan();
        }

    }

    private void refreshChart(){
        testdata = BreathData.get( 0 ) * getRandomNumber( 0, 1 );
        breathdata = BreathData.get( 0 );

        breathChartData.addEntry(  new Entry( breathChartData.getEntryCount(), breathdata));
        breathPlaneChartData.addEntry( new Entry(breathPlaneChartData.getEntryCount(), testdata));
        breathChartData.notifyDataSetChanged();
        chartData.notifyDataChanged();
        myChart.notifyDataSetChanged();
        myChart.refreshDrawableState();
        myChart.invalidate();
        myChart.setVisibleXRange( 6, 60 );
        myChart.moveViewToX( breathPlaneChartData.getEntryCount() - 60 );
    }

    private int getRandomNumber(int Min, int Max){

        return Min + (int)(Math.random() * ((Max - Min) + 1));
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
                                refreshChart();
                            }
                        });
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
