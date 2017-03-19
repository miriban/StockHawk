package com.udacity.stockhawk.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wello on 3/9/17.
 */

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.StockHistoryViewHolder>
{
    class StockHistoryViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_value)
        TextView textView_history_value;
        @BindView(R.id.tv_date)
        TextView textView_history_date;

        public StockHistoryViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private float values[];
    private String dates[];

    public StockHistoryAdapter(String historyData)
    {

    }

    @Override
    public StockHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.stock_history_item,parent,false);

        return new StockHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockHistoryViewHolder holder, int position)
    {
        String value = "$" + Float.toString(values[position]);
        holder.textView_history_value.setText(value);

        holder.textView_history_date.setText(dates[position]);
    }

    @Override
    public int getItemCount()
    {
        return values.length;
    }
}
