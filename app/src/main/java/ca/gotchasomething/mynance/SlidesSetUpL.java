package ca.gotchasomething.mynance;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

public class SlidesSetUpL extends AppCompatActivity implements View.OnClickListener {

    private AdapterSlides adapter;
    private Button previousButton, nextButton;
    DbManager dbManager;
    private ImageView[] dots;
    private int[] slides, incomeSlides = {R.layout.b2_slides_set_up_income_1_land, R.layout.b2_slides_set_up_income_2_land, R.layout.b2_slides_set_up_income_3_land, R.layout.b2_slides_set_up_income_4_land}, billsSlides = {R.layout.b3_slides_set_up_bills_1_land,
            R.layout.b3_slides_set_up_bills_1a_land, R.layout.b3_slides_set_up_bills_2_land, R.layout.b3_slides_set_up_bills_3_land, R.layout.b3_slides_set_up_bills_4_land,
            R.layout.b3_slides_set_up_bills_5_land}, debtsSlides = {R.layout.b4_slides_set_up_debts_1_land, R.layout.b4_slides_set_up_debts_2_land},
            savingsSlides = {R.layout.b5_slides_set_up_savings_1_land, R.layout.b5_slides_set_up_savings_2_land}, analysisSlides = {R.layout.b6_slides_set_up_analysis_1_land},
            weeklySlides = {R.layout.b7_slides_set_up_weekly_1_land, R.layout.b7_slides_set_up_weekly_2_land, R.layout.b7_slides_set_up_weekly_3_land},
            finalSlides = {R.layout.b8_slides_set_up_final_1_land, R.layout.b8_slides_set_up_final_2_land};
    private LinearLayout dotsLayout;
    String latestDone = null;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(this);

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.b1_slides_set_up_background_land);

        viewPager = findViewById(R.id.viewPager);
        latestDone = dbManager.retrieveLatestDone();
        switch (latestDone) {
            case "start":
                slides = incomeSlides;
                break;
            case "income":
                slides = billsSlides;
                break;
            case "bills":
                slides = debtsSlides;
                break;
            case "debts":
                slides = savingsSlides;
                break;
            case "savings":
                slides = analysisSlides;
                break;
            case "analysis":
                slides = weeklySlides;
                break;
            case "weekly":
                slides = finalSlides;
                break;
        }
        adapter = new AdapterSlides(slides, this);
        viewPager.setAdapter(adapter);

        dotsLayout = findViewById(R.id.dotsLayout);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        createDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);

                if (position == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                } else {
                    previousButton.setVisibility(View.VISIBLE);
                }

                if (position == slides.length - 1) {
                    nextButton.setText(getResources().getString(R.string.start_button));
                } else {
                    nextButton.setText(getResources().getString(R.string.next_button));
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

        dots = new ImageView[slides.length];

        for (int i = 0; i < slides.length; i++) {
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

            case R.id.previousButton:
                loadPreviousSlide();
                break;
        }
    }

    private void loadHome() {
        switch (latestDone) {
            case "start":
                startActivity(new Intent(this, AddIncome.class));
                break;
            case "income":
                startActivity(new Intent(this, AddExpense.class));
                break;
            case "bills":
                startActivity(new Intent(this, AddDebts.class));
                break;
            case "debts":
                startActivity(new Intent(this, AddSavings.class));
                break;
            case "savings":
                startActivity(new Intent(this, SetUpAnalysis.class));
                break;
            case "analysis":
                startActivity(new Intent(this, LayoutWeeklyLimitsList.class));
                break;
            case "weekly":
                startActivity(new Intent(this, SetUpFinal.class));
                break;
        }
        finish();
    }

    private void loadNextSlide() {
        int next = viewPager.getCurrentItem() + 1;

        if (next < slides.length) {
            viewPager.setCurrentItem(next);
        } else {
            loadHome();
        }
    }

    private void loadPreviousSlide() {
        int previous = viewPager.getCurrentItem() - 1;

        if (previous < 0) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(previous);
        }
    }
}
