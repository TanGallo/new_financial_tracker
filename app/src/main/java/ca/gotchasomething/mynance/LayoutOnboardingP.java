package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

public class LayoutOnboardingP extends AppCompatActivity implements View.OnClickListener {

    private AdapterOnboarding adapter;
    private Button skipButton, nextButton;
    Cursor setUpCursor, setUpCursor2, expenseCursor, incomeCursor;
    DbHelper setUpHelper, setUpHelper2, expenseHelper, incomeHelper;
    private ImageView[] dots;
    int tourDoneYes;
    private int[] onboardingSlides = {
            R.layout.layout_onboarding_1,
            R.layout.layout_onboarding_2,
            R.layout.layout_onboarding_3,
            R.layout.layout_onboarding_4,
            R.layout.layout_onboarding_5,
            R.layout.layout_onboarding_6
    };
    private LinearLayout dotsLayout;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase setUpDbDb, setUpDbDb2, expenseDbDb, incomeDbDb;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreferences()) {
            loadHome();
        }

        /*if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        setContentView(R.layout.layout_onboarding);

        viewPager = findViewById(R.id.viewPager);
        adapter = new AdapterOnboarding(onboardingSlides, this);
        viewPager.setAdapter(adapter);

        dotsLayout = findViewById(R.id.dotsLayout);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        createDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);

                if (position == onboardingSlides.length - 1) {
                    nextButton.setText(getResources().getString(R.string.start_button));
                    skipButton.setVisibility(View.INVISIBLE);
                } else {
                    nextButton.setText(getResources().getString(R.string.next_button));
                    skipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int current_position) {
        if (dotsLayout != null)
            dotsLayout.removeAllViews();

        dots = new ImageView[onboardingSlides.length];

        for (int i = 0; i < onboardingSlides.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(
                        AppCompatResources.getDrawable(
                                this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(
                        AppCompatResources.getDrawable(
                                this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);

            dotsLayout.addView(dots[i], params);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nextButton:
                loadNextSlide();
                break;

            case R.id.skipButton:
                loadHome();
                new PreferenceManager(this).writePreferences();
                break;
        }
    }

    private void loadHome() {
        setUpHelper = new DbHelper(getApplicationContext());
        setUpDbDb = setUpHelper.getReadableDatabase();
        setUpCursor = setUpDbDb.rawQuery(" SELECT max(tourDone) FROM " + DbHelper.SET_UP_TABLE_NAME + "", null);
        setUpCursor.moveToFirst();
        tourDoneYes = setUpCursor.getInt(0);
        setUpCursor.close();

        if (tourDoneYes <= 0) {
            startActivity(new Intent(this, LayoutSetUp.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));

            /*Double startingBalance = retrieveStartingBalance();
            if(startingBalance.isNaN() || startingBalance < 0 || startingBalance == 0) {
                startingBalance = 0.0;
            }
            String startingBalance2 = currencyFormat.format(startingBalance);
            TextView totalAccountText = findViewById(R.id.totalAccountText);
            totalAccountText.setText(startingBalance2);

            Double startingBalanceAvailable = startingBalance * retrieveBPercentage();
            if(startingBalance == 0) {
                startingBalanceAvailable = 0.0;
            }
            String startingBalanceAvailable2 = currencyFormat.format(startingBalanceAvailable);
            TextView availableAccountText = findViewById(R.id.availableAccountText);
            availableAccountText.setText(startingBalanceAvailable2);*/

            finish();
        }
    }

    private void loadNextSlide() {
        int next = viewPager.getCurrentItem() + 1;

        if (next < onboardingSlides.length) {
            viewPager.setCurrentItem(next);
        } else {
            loadHome();
            new PreferenceManager(this).writePreferences();
        }
    }

    /*public Double retrieveStartingBalance() {
        setUpHelper2 = new DbHelper(this);
        setUpDbDb2 = setUpHelper2.getReadableDatabase();
        setUpCursor2 = setUpDbDb2.rawQuery("SELECT max(balanceAmount)" + " FROM " + DbHelper.SET_UP_TABLE_NAME, null);
        setUpCursor2.moveToFirst();
        Double startingBalanceResult = setUpCursor2.getDouble(0);
        setUpCursor2.close();

        return startingBalanceResult;
    }

    public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(this);
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        expenseCursor.moveToFirst();
        Double totalAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(this);
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        Double totalIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        Double percentB = 1 - (totalAExpenses / totalIncome);

        return percentB;

    }*/
}
