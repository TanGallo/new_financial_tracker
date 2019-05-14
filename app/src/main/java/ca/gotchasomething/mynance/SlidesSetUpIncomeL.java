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

public class SlidesSetUpIncomeL extends AppCompatActivity implements View.OnClickListener {

    private AdapterSetUpIncome adapter1;
    private Button previousButton, nextButton;
    DbManager dbManager;
    private ImageView[] dots;
    private int[] setUpIncomeSlides = {
            R.layout.slides_set_up_income_1_L,
            R.layout.slides_set_up_income_2_L,
    };
    private LinearLayout dotsLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (new PreferenceManager(this).checkPreferences()) {
            loadHome();
        }*/

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.slides_set_up_background_L);

        viewPager = findViewById(R.id.viewPager);
        adapter1 = new AdapterSetUpIncome(setUpIncomeSlides, this);
        viewPager.setAdapter(adapter1);

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

                if (position == setUpIncomeSlides.length - 1) {
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

        dots = new ImageView[setUpIncomeSlides.length];

        for (int i = 0; i < setUpIncomeSlides.length; i++) {
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
            startActivity(new Intent(this, AddIncomeSetUp.class));
            finish();
    }

    private void loadNextSlide() {
        int next = viewPager.getCurrentItem() + 1;

        if (next < setUpIncomeSlides.length) {
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
