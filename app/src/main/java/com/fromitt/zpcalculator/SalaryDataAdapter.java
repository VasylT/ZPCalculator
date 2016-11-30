package com.fromitt.zpcalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fromitt.zpcalculator.storage.Calculator;
import com.fromitt.zpcalculator.storage.SalaryDataItem;
import com.fromitt.zpcalculator.utils.MessageUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Tkachov Vasyl on 09.11.2016.
 */

public class SalaryDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SalaryDataItem> mObjects;
    private View.OnClickListener onClickListener;
    private Context context;

    public SalaryDataAdapter(List<SalaryDataItem> objects, View.OnClickListener onClickListener, Context context) {
        mObjects = objects;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_salary, parent, false);
        view.setOnClickListener(onClickListener);

        return new SalaryViewHolder(view);
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SalaryViewHolder salaryHolder = (SalaryViewHolder) holder;
        SalaryDataItem salaryDataItem = mObjects.get(position);
        SalaryViewItem salaryViewItem = Calculator.calculateSalaryValues(salaryDataItem);

        Date date = salaryDataItem.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int imageResource;
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                imageResource = R.drawable.ic_january;
                break;
            case Calendar.FEBRUARY:
                imageResource = R.drawable.ic_february;
                break;
            case Calendar.MARCH:
                imageResource = R.drawable.ic_march;
                break;
            case Calendar.APRIL:
                imageResource = R.drawable.ic_april;
                break;
            case Calendar.MAY:
                imageResource = R.drawable.ic_may;
                break;
            case Calendar.JUNE:
                imageResource = R.drawable.ic_june;
                break;
            case Calendar.JULY:
                imageResource = R.drawable.ic_july;
                break;
            case Calendar.AUGUST:
                imageResource = R.drawable.ic_august;
                break;
            case Calendar.SEPTEMBER:
                imageResource = R.drawable.ic_september;
                break;
            case Calendar.OCTOBER:
                imageResource = R.drawable.ic_october;
                break;
            case Calendar.NOVEMBER:
                imageResource = R.drawable.ic_november;
                break;
            case Calendar.DECEMBER:
                imageResource = R.drawable.ic_december;
                break;
            default:
                imageResource = 0;
        }
        salaryHolder.monthImageView.setImageResource(imageResource);

        salaryHolder.dateView.setText(MessageUtils.DATE_FORMAT.format(date));
        salaryHolder.exchangeRateView.setText(context.getResources().getString(
                R.string.exchange_rate_range,
                salaryViewItem.getExchangeRateStart(), salaryViewItem.getExchangeRateEnd()));
        salaryHolder.moneyOnCardView.setText(String.valueOf(salaryViewItem.getMoneyOnCard()));
        salaryHolder.cashView.setText(String.valueOf(salaryViewItem.getCash()));
        salaryHolder.totalView.setText(String.valueOf(salaryViewItem.getTotal()));
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public void addItem(int position, SalaryDataItem item) {
        mObjects.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(SalaryDataItem item) {
        mObjects.add(0, item);
        notifyItemInserted(0);
    }

    public void addItems(List<SalaryDataItem> items) {
        mObjects.addAll(items);
    }

    public SalaryDataItem getItem(int position) {
        return mObjects.get(position);
    }

    public void removeItem(int position) {
        mObjects.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItems() {
        int size = mObjects.size();
        mObjects.clear();
        notifyItemRangeRemoved(0, size);
    }

    private class SalaryViewHolder extends RecyclerView.ViewHolder {

        final TextView dateView;
        final TextView exchangeRateView;
        final TextView moneyOnCardView;
        final TextView cashView;
        final TextView totalView;
        final ImageView monthImageView;

        public SalaryViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.date);
            exchangeRateView = (TextView) itemView.findViewById(R.id.exchange_rate);
            moneyOnCardView = (TextView) itemView.findViewById(R.id.money_on_card);
            cashView = (TextView) itemView.findViewById(R.id.cash);
            totalView = (TextView) itemView.findViewById(R.id.salary_total);
            monthImageView = (ImageView) itemView.findViewById(R.id.month_image_view);
        }
    }
}
