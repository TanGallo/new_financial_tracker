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

public class SlidesSetUpP extends AppCompatActivity implements View.OnClickListener {

    private AdapterSlides adapter;
    private Button previousButton, nextButton;
    DbManager dbManager;
    private ImageView[] dots;
    private int[] slides, incomeSlides = {R.layout.slides_set_up_income_1, R.layout.slides_set_up_income_2}, billsSlides = {R.layout.slides_set_up_bills_1,
            R.layout.slides_set_up_bills_1a, R.layout.slides_set_up_bills_2, R.layout.slides_set_up_bills_3, R.layout.slides_set_up_bills_4,
            R.layout.slides_set_up_bills_5}, debtsSlides = {R.layout.slides_set_up_debts_1, R.layout.slides_set_up_debts_2, R.layout.slides_set_up_debts_3},
            savingsSlides = {R.layout.slides_set_up_savings_1, R.layout.slides_set_up_savings_2};
    private LinearLayout dotsLayout;
    String latestDone = null;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(this);

        /*if (new PreferenceManager(this).checkPreferences()) {
            loadHome();
        }*/

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.slides_set_up_background);

        viewPager = findViewById(R.id.viewPager);
        latestDone = dbManager.retrieveLatestDone();
        switch(latestDone) {
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
                /*case "savings":
                slides = ;
                    break;
                case "budget":
                slides = ;
                    break;
                case "balance":
                slides = ;
                    break;*/
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
        //dbManager.retrieveLatestDone();
        switch(latestDone) {
            case "start":
                startActivity(new Intent(this, SetUpAddIncome.class));
                break;
            case "income":
                startActivity(new Intent(this, SetUpAddBills.class));
                break;
            case "bills":
                startActivity(new Intent(this, AddDebt.class));
                break;
            case "debts":
                startActivity(new Intent(this, AddSavings.class));
                break;
                /*startActivity(new Intent(this, className.class));
                    break;
                case "budget":
                startActivity(new Intent(this, className.class));
                    break;
                case "balance":
                startActivity(new Intent(this, className.class));
                    break;*/
        }
            //startActivity(new Intent(this, SetUpAddIncome.class));
            finish();
    }

    private void loadNextSlide() {
        int next = viewPager.getCurrentItem() + 1;

        if (next < slides.length) {
            viewPager.setCurrentItem(next);
        } else {
            loadHome();
            //new PreferenceManager(this).writePreferences();
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
