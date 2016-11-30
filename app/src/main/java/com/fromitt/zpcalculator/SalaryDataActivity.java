package com.fromitt.zpcalculator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.fromitt.zpcalculator.storage.SalaryDataItem;
import com.fromitt.zpcalculator.storage.SalaryDataStorage;
import com.fromitt.zpcalculator.ui.DividerItemDecoration;
import com.fromitt.zpcalculator.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Tkachov Vasyl on 09.11.2016.
 */

public class SalaryDataActivity extends AppCompatActivity {

    private static final String TAG = "SalaryDataActivity";
    private List<SalaryDataItem> mSalaryList;
    private RecyclerView mSalaryRecyclerView;
    private SalaryDataAdapter mSalaryAdapter;
    private SalaryDataStorage mSalaryStorage;
    private int REQUEST_CODE = 1408;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSalaryStorage = SalaryDataStorage.getInstance(this);

        mSalaryList = new ArrayList<>();
        mSalaryAdapter = new SalaryDataAdapter(mSalaryList, onClickListener, this);
        mSalaryAdapter.addItems(mSalaryStorage.getItems());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mSalaryRecyclerView = (RecyclerView) findViewById(R.id.salary_recycler_view);
        mSalaryRecyclerView.setLayoutManager(llm);
        mSalaryRecyclerView.setAdapter(mSalaryAdapter);
        mSalaryRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                showDeleteItemDialog(viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mSalaryRecyclerView);

        toggleNoDataPlaceholder();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SalaryDataItem item = mSalaryList.get(mSalaryRecyclerView
                    .getChildViewHolder(view).getAdapterPosition());
            updateItem(item);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE || data != null) {
            SalaryDataItem itemSalary = new SalaryDataItem(
                    new Date(data.getLongExtra(MessageUtils.DATE, 0)),
                    data.getFloatExtra(MessageUtils.SALARY, 0),
                    data.getFloatExtra(MessageUtils.EXCHANGE_BUY1, 1),
                    data.getFloatExtra(MessageUtils.EXCHANGE_SALE1, 1),
                    data.getFloatExtra(MessageUtils.MONEY_ON_CARD1, 0),
                    data.getFloatExtra(MessageUtils.EXCHANGE_BUY2, 1),
                    data.getFloatExtra(MessageUtils.EXCHANGE_SALE2, 1),
                    data.getFloatExtra(MessageUtils.MONEY_ON_CARD2, 0));
            if (data.getBooleanExtra(MessageUtils.IS_NEW, true)) {
                mSalaryStorage.addItem(itemSalary);
                mSalaryAdapter.addItem(itemSalary);
                mSalaryRecyclerView.smoothScrollToPosition(0);
                Snackbar.make(mSalaryRecyclerView, R.string.item_saved, Snackbar.LENGTH_SHORT).show();
            } else {
                mSalaryStorage.updateItem(itemSalary);
                mSalaryList.clear();
                mSalaryList.addAll(mSalaryStorage.getItems());
                mSalaryAdapter.notifyDataSetChanged();
                Snackbar.make(mSalaryRecyclerView, R.string.item_modified, Snackbar.LENGTH_SHORT).show();
            }
            toggleNoDataPlaceholder();
        }
    }

    private void addNewItem(long dateMillis) {
        // Set up intent for launching salary form.
        Intent intent = new Intent(SalaryDataActivity.this, ItemFormActivity.class);
        // Put picked date to extras.
        intent.putExtra(MessageUtils.DATE, dateMillis);
        // Notify, that we will making a new entry.
        intent.putExtra(MessageUtils.IS_NEW, true);
        // Start activity.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    SalaryDataActivity.this,
                    findViewById(R.id.toolbar),
                    findViewById(R.id.toolbar).getTransitionName());
            startActivityForResult(intent, REQUEST_CODE, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void updateItem(SalaryDataItem item) {
        // Set up intent for launching salary form.
        Intent intent = new Intent(SalaryDataActivity.this, ItemFormActivity.class);
        intent.putExtra(MessageUtils.IS_NEW, false);
        intent.putExtra(MessageUtils.DATE, item.getDate().getTime());
        intent.putExtra(MessageUtils.SALARY, item.getSalary());
        intent.putExtra(MessageUtils.EXCHANGE_BUY1, item.getExchangeBuy1());
        intent.putExtra(MessageUtils.EXCHANGE_SALE1, item.getExchangeSale1());
        intent.putExtra(MessageUtils.MONEY_ON_CARD1, item.getMoneyOnCard1());
        intent.putExtra(MessageUtils.EXCHANGE_BUY2, item.getExchangeBuy2());
        intent.putExtra(MessageUtils.EXCHANGE_SALE2, item.getExchangeSale2());
        intent.putExtra(MessageUtils.MONEY_ON_CARD2, item.getMoneyOnCard2());
        // Start activity.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    SalaryDataActivity.this,
                    findViewById(R.id.toolbar),
                    findViewById(R.id.toolbar).getTransitionName());
            startActivityForResult(intent, REQUEST_CODE, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @SuppressLint("InflateParams")
    private void showDatePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_date_picker, null, false);

        final NumberPicker monthPicker = (NumberPicker) view.findViewById(R.id.month_picker);
        final NumberPicker yearPicker = (NumberPicker) view.findViewById(R.id.year_picker);
        final ImageView entryExistsMarker = (ImageView) view.findViewById(R.id.entry_exists_marker);

        setDividerColor(monthPicker, ContextCompat.getColor(this, R.color.colorPrimary));
        setDividerColor(yearPicker, ContextCompat.getColor(this, R.color.colorPrimary));

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(getResources().getStringArray(R.array.months));
        yearPicker.setMinValue(getResources().getInteger(R.integer.year_start));
        yearPicker.setMaxValue(getResources().getInteger(R.integer.year_end));

        final Calendar calendar = Calendar.getInstance();
        monthPicker.setValue(calendar.get(Calendar.MONTH));
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);

        final ArrayList<Long> itemDatesList = new ArrayList<>();
        for (SalaryDataItem item : mSalaryStorage.getItems()) {
            itemDatesList.add(item.getDate().getTime());
        }

        toggleEntryExistsMarker(entryExistsMarker, itemDatesList, calendar);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                // Set picked date to calendar. Day is set to the first day of the month.
                calendar.set(yearPicker.getValue(), newValue, 1, 0, 0, 0);
                toggleEntryExistsMarker(entryExistsMarker, itemDatesList, calendar);
            }
        });

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                // Set picked date to calendar. Day is set to the first day of the month.
                calendar.set(newValue, monthPicker.getValue(), 1, 0, 0, 0);
                toggleEntryExistsMarker(entryExistsMarker, itemDatesList, calendar);
            }
        });

        entryExistsMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.item_exists, Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setView(view)
                .setTitle(R.string.date_picker_title)
                .setNegativeButton(R.string.date_picker_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.date_picker_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get milliseconds from date picked.
                        long dateMillis = calendar.getTimeInMillis();

                        // Check if entry for this date already exists.
                        SalaryDataItem item = mSalaryStorage.getSalaryDataEntry(dateMillis);
                        if (item == null) {
                            addNewItem(calendar.getTimeInMillis());
                        } else {
                            updateItem(item);
                        }

                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void toggleEntryExistsMarker(ImageView existenceMarker, ArrayList<Long> itemDatesList, Calendar calendar) {
        long cleanMillis = (calendar.getTimeInMillis() / 1000) * 1000;
        if (itemDatesList.contains(cleanMillis)) {
            existenceMarker.setVisibility(View.VISIBLE);
        } else {
            existenceMarker.setVisibility(View.GONE);
        }
    }

    /**
     * Hacky method to change {@link NumberPicker} divider color.
     *
     * @param picker NumberPicker to be modified.
     * @param color  new divider color.
     */
    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException
                        | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void showDeleteItemDialog(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Date itemDate = mSalaryAdapter.getItem(viewHolder.getAdapterPosition()).getDate();
        builder.setMessage(getString(R.string.delete_item_title, MessageUtils.DATE_FORMAT_TOOLBAR.format(itemDate)))
                .setCancelable(false)
                .setNegativeButton(R.string.delete_item_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mSalaryAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                })
                .setPositiveButton(R.string.delete_item_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mSalaryStorage.deleteItem(itemDate);
                        mSalaryAdapter.removeItem(viewHolder.getAdapterPosition());
                        toggleNoDataPlaceholder();
                        Snackbar.make(mSalaryRecyclerView, R.string.item_deleted, Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void toggleNoDataPlaceholder() {
        findViewById(R.id.placeholder_no_data).setVisibility(mSalaryAdapter.getItemCount() == 0 ?
                View.VISIBLE : View.GONE);
    }
}
