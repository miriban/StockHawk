package com.udacity.stockhawk.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
}
