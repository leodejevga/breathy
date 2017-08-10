package com.apps.philipps.source.helper.chartdisplay;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class ChartUtil {
    public static LineChart createLineChart(Context context){
        LineChart result;
        result = new LineChart( context );
        result.setNoDataText( "Please check the connection, no Data available" );
        result.setTouchEnabled( false );
        result.setDragEnabled( false );
        result.setClickable( false );

        result.setDrawGridBackground( false );
        XAxis x = result.getXAxis();
        x.setAvoidFirstLastClipping( true );
        x.setDrawGridLines( false );
        x.setEnabled( false );

        YAxis y = result.getAxisLeft();
        y.setAxisMaximum( 1000 );
        y.setAxisMinimum( 0 );
        y.setDrawGridLines( false );
        y.setEnabled( false );
        result.getAxisRight().setEnabled( false );
        result.getLegend().setEnabled( false );
        return result;
    }

    public static LineData createData(){
        LineData result;
        result = new LineData();

        return result;
    }

    public static LineDataSet createDataSet(String name, int color){
        LineDataSet result;

        result = new LineDataSet( null, name );
        result.setCubicIntensity( 0.2f );
        result.setAxisDependency( YAxis.AxisDependency.LEFT );
        result.setColor( color );
        result.setFillAlpha( 65 );
        result.addEntry( new Entry( 0,0 ) );

        Log.i("A","Erstellung LineDataset");

        return result;
    }
}
