package com.udacity.stockhawk.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wello on 3/9/17.
 */

public class StockHistoryUtils
{
    public static String getReadableDate(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);

        return dateFormat.format(calendar.getTime());
    }

    public static float[] getStockValues(String historyData)
    {
        String lines[] = historyData.split("\n");
        float[] values = new float[lines.length];
        for( int i =0;i<lines.length;i++)
        {
            String lineData[] = lines[i].split(",");
            values[i] = Float.parseFloat(lineData[1]);
        }
        return values;
    }

    public static String[] getStockDates(String historyData)
    {
        String lines[] = historyData.split("\n");
        String[] dates = new String[lines.length];
        for( int i =0;i<lines.length;i++)
        {
            String lineData[] = lines[i].split(",");
            dates[i] = getReadableDate(Long.parseLong(lineData[0]));
        }
        return dates;
    }

    public static void createChart(Context context,
                                   String symbol,
                                   LineChart chart, final float values[], final String dates[]
                                   )
    {
        int chartColor = Color.rgb(0xFF, 0x91, 0x11);
        List<Entry> entryList = new ArrayList<>();
        for(int i=0; i<values.length; i++)
        {
            entryList.add(new Entry(i, values[values.length-1-i]));
        }

        LineDataSet dataSet = new LineDataSet(entryList, symbol);
        dataSet.setColor(chartColor);
        dataSet.setCircleColor(chartColor);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return dates[dates.length - (int)value - 1];
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(R.color.colorAccent);
        chart.getLegend().setWordWrapEnabled(true);
        Description description = new Description();
        description.setText(symbol+" "+context.getString(R.string.stock_history));
        chart.setDescription(description);
        YAxis yAxiz = chart.getAxis(YAxis.AxisDependency.LEFT);
        yAxiz.setDrawGridLines(false);
        yAxiz.setAxisMinimum(0);
        yAxiz.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return value + "$";
            }
        });
        YAxis rightAxis = chart.getAxis(YAxis.AxisDependency.RIGHT);
        rightAxis.setEnabled(false);
        chart.setBorderColor(R.color.colorAccent);
        chart.invalidate();
    }
}
