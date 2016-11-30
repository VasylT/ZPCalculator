package com.fromitt.zpcalculator;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.ChangeImageTransform;
import android.transition.CircularPropagation;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionPropagation;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fromitt.zpcalculator.utils.MessageUtils;

import java.util.Date;

/**
 *
 * Created by Tkachov Vasyl on 04.11.2016.
 */

public class ItemFormActivity extends AppCompatActivity {

    // UI references.
    private TextInputEditText mExchangeBuyMonthStart;
    private TextInputEditText mExchangeSaleMonthStart;
    private TextInputEditText mCardMonthStart;
    private TextInputEditText mExchangeBuyMonthEnd;
    private TextInputEditText mExchangeSaleMonthEnd;
    private TextInputEditText mCardMonthEnd;
    private TextInputEditText mSalaryView;
    private FrameLayout       mDoneButton;
    private long mSalaryItemDate;
    private boolean mIsNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        mSalaryView = (TextInputEditText) findViewById(R.id.salary);
        mExchangeBuyMonthStart = (TextInputEditText) findViewById(R.id.exchange_buy_month_start);
        mExchangeSaleMonthStart = (TextInputEditText) findViewById(R.id.exchange_sale_month_start);
        mCardMonthStart = (TextInputEditText) findViewById(R.id.money_on_card_month_start);
        mExchangeBuyMonthEnd = (TextInputEditText) findViewById(R.id.exchange_buy_month_end);
        mExchangeSaleMonthEnd = (TextInputEditText) findViewById(R.id.exchange_sale_month_end);
        mCardMonthEnd = (TextInputEditText) findViewById(R.id.money_on_card_month_end);
        mDoneButton = (FrameLayout) findViewById(R.id.btn_done);

        mCardMonthEnd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptComplete();
                    return true;
                }
                return false;
            }
        });

        Intent startIntent = getIntent();
        mSalaryItemDate = startIntent.getLongExtra(MessageUtils.DATE, 0);
        if (mSalaryItemDate == 0) {
            // Bad date. Close activity.
            setResult(RESULT_CANCELED);
            finish();
            return;
        } else {
            // Display date value.
            String date = MessageUtils.DATE_FORMAT_TOOLBAR.format(new Date(mSalaryItemDate));
            ((TextView) findViewById(R.id.date_view)).append(date);
        }

        mIsNew = startIntent.getBooleanExtra(MessageUtils.IS_NEW, true);

        if (!mIsNew) {
            // Set stored values to edit fields.
            float salary = startIntent.getFloatExtra(MessageUtils.SALARY, 0);
            float exchangeBuyStart = startIntent.getFloatExtra(MessageUtils.EXCHANGE_BUY1, 0);
            float exchangeSaleStart = startIntent.getFloatExtra(MessageUtils.EXCHANGE_SALE1, 0);
            float cardStart = startIntent.getFloatExtra(MessageUtils.MONEY_ON_CARD1, 0);
            float exchangeBuyEnd = startIntent.getFloatExtra(MessageUtils.EXCHANGE_BUY2, 0);
            float exchangeSaleEnd = startIntent.getFloatExtra(MessageUtils.EXCHANGE_SALE2, 0);
            float cardEnd = startIntent.getFloatExtra(MessageUtils.MONEY_ON_CARD2, 0);

            if (salary != 0)
                mSalaryView.append(String.valueOf(salary));
            if (exchangeBuyStart != 0)
                mExchangeBuyMonthStart.append(String.valueOf(exchangeBuyStart));
            if (exchangeSaleStart != 0)
                mExchangeSaleMonthStart.append(String.valueOf(exchangeSaleStart));
            if (cardStart != 0)
                mCardMonthStart.append(String.valueOf(cardStart));
            if (exchangeBuyEnd != 0)
                mExchangeBuyMonthEnd.append(String.valueOf(exchangeBuyEnd));
            if (exchangeSaleEnd != 0)
                mExchangeSaleMonthEnd.append(String.valueOf(exchangeSaleEnd));
            if (cardEnd != 0)
                mCardMonthEnd.append(String.valueOf(cardEnd));
        }

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptComplete();
            }
        });

        setAnimations();
    }

    @SuppressLint("RtlHardcoded")
    private void setAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.TOP);
            slide.setInterpolator(new DecelerateInterpolator());
            slide.setDuration(getResources().getInteger(R.integer.animation_duration_short));
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(slide);
//            // Previously invisible form view.
//            final View salaryFormView = findViewById(R.id.salary_form);
//
//            // Start animation after view is attached to the activity.
//            salaryFormView.post(new Runnable()
//            {
//                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void run(){
//                    // Get the center for the clipping circle.
//                    int cx = (salaryFormView.getLeft() + salaryFormView.getRight()) / 2;
//                    int cy = 0;
//
//                    // Get the final radius for the clipping circle.
//                    int finalRadius = Math.max(salaryFormView.getWidth(), salaryFormView.getHeight());
//
//                    // Create the animator for view (the start radius is zero).
//                    Animator anim = ViewAnimationUtils.createCircularReveal(salaryFormView, cx, cy, 0, finalRadius);
//                    anim.setInterpolator(new DecelerateInterpolator());
//                    anim.setDuration(getResources().getInteger(R.integer.animation_duration_long));
//
//                    // Make the view visible and start the animation.
//                    salaryFormView.setVisibility(View.VISIBLE);
//                    anim.start();
//                }
//            });
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptComplete() {
        // Reset errors.
        mSalaryView.setError(null);
        mExchangeBuyMonthStart.setError(null);
        mExchangeSaleMonthStart.setError(null);
        mCardMonthStart.setError(null);
        mExchangeBuyMonthEnd.setError(null);
        mExchangeSaleMonthEnd.setError(null);
        mCardMonthEnd.setError(null);

        // Store values at the time of the calculate attempt.
        String salary = mSalaryView.getText().toString();
        String exchangeBuyStart = mExchangeBuyMonthStart.getText().toString();
        String exchangeSaleStart = mExchangeSaleMonthStart.getText().toString();
        String cardStart = mCardMonthStart.getText().toString();
        String exchangeBuyEnd = mExchangeBuyMonthEnd.getText().toString();
        String exchangeSaleEnd = mExchangeSaleMonthEnd.getText().toString();
        String cardEnd = mCardMonthEnd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isRequiredFieldValid(salary, mSalaryView)) {
            focusView = mSalaryView;
            cancel = true;
        }
        if (TextUtils.isEmpty(exchangeBuyStart)) {
            exchangeBuyStart = "0";
        }
        if (TextUtils.isEmpty(exchangeSaleStart)) {
            exchangeSaleStart = "0";
        }
        if (TextUtils.isEmpty(cardStart)) {
            cardStart = "0";
        }
        if (TextUtils.isEmpty(exchangeBuyEnd)) {
            exchangeBuyEnd = "0";
        }
        if (TextUtils.isEmpty(exchangeSaleEnd)) {
            exchangeSaleEnd = "0";
        }
        if (TextUtils.isEmpty(cardEnd)) {
            cardEnd = "0";
        }

        if (cancel) {
            // An error occurred. Focusing view that caused a problem.
            focusView.requestFocus();
        } else {
            // Complete form, setting prompted values for activity result.
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MessageUtils.IS_NEW, mIsNew);
            resultIntent.putExtra(MessageUtils.DATE, mSalaryItemDate);
            resultIntent.putExtra(MessageUtils.SALARY, Float.parseFloat(salary));
            resultIntent.putExtra(MessageUtils.EXCHANGE_BUY1, Float.parseFloat(exchangeBuyStart));
            resultIntent.putExtra(MessageUtils.EXCHANGE_SALE1, Float.parseFloat(exchangeSaleStart));
            resultIntent.putExtra(MessageUtils.MONEY_ON_CARD1, Float.parseFloat(cardStart));
            resultIntent.putExtra(MessageUtils.EXCHANGE_BUY2, Float.parseFloat(exchangeBuyEnd));
            resultIntent.putExtra(MessageUtils.EXCHANGE_SALE2, Float.parseFloat(exchangeSaleEnd));
            resultIntent.putExtra(MessageUtils.MONEY_ON_CARD2, Float.parseFloat(cardEnd));
            setResult(Activity.RESULT_OK, resultIntent);
            // Finish activity.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
    }

    private boolean isRequiredFieldValid(String text, TextInputEditText input) {
        if (TextUtils.isEmpty(text)) {
            input.setError(getString(R.string.error_field_required));
        } else {
            return true;
        }
        return false;
    }
}

