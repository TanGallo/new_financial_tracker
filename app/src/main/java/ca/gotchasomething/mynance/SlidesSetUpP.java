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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

public class SlidesSetUpP extends AppCompatActivity implements View.OnClickListener {

    private AdapterSlides adapter;
    private Button previousBtn, nextBtn;
    DbManager dbManager;
    ImageView slideImage;
    private ImageView[] dots;
    public int[] slides,
            incomeSlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background},
            billsSlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background,
                    R.layout.b1_slides_set_up_background},
            debtsSlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background},
            savingsSlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background},
            analysisSlides = {R.layout.b1_slides_set_up_background},
            weeklySlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background},
            finalSlides = {R.layout.b1_slides_set_up_background, R.layout.b1_slides_set_up_background};
    private LinearLayout dotsLayout;
    TextView slideTitle, slideTV1, slideTV2, slideTV3, slideTV4, slideTV5, slideTV6, slideTV7, slideTV8;
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

        setContentView(R.layout.b1_slides_set_up_background);

        viewPager = findViewById(R.id.viewPager);
        switch (dbManager.retrieveLatestDone()) {
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

        slideImage = findViewById(R.id.slideImage);
        slideTitle = findViewById(R.id.slideTitle);
        slideTV1 = findViewById(R.id.slideTV1);
        slideTV2 = findViewById(R.id.slideTV2);
        slideTV3 = findViewById(R.id.slideTV3);
        slideTV4 = findViewById(R.id.slideTV4);
        slideTV5 = findViewById(R.id.slideTV5);
        slideTV6 = findViewById(R.id.slideTV6);
        slideTV7 = findViewById(R.id.slideTV7);
        slideTV8 = findViewById(R.id.slideTV8);
        dotsLayout = findViewById(R.id.slideDotsLayout);
        previousBtn = findViewById(R.id.slidePreviousBtn);
        nextBtn = findViewById(R.id.slideNextBtn);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        createDots(0);
        buildFirstSlide();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);

                switch (dbManager.retrieveLatestDone()) {
                    case "start":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_attach_money_white_24dp));
                        slideTV2.setVisibility(View.GONE);
                        slideTV3.setVisibility(View.GONE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        if (position == 0) {
                            slideTitle.setVisibility(View.VISIBLE);
                            slideTitle.setText(R.string.lets_get_real);
                            slideTV1.setText(R.string.b2_text1);
                        } else {
                            slideTitle.setVisibility(View.GONE);
                            if (position == 1) {
                                slideTV1.setText(R.string.b2_text2);
                            } else if (position == 2) {
                                slideTV1.setText(R.string.b2_text3);
                            } else if (position == 3) {
                                slideTV1.setText(R.string.b2_text4);
                            }
                        }
                        break;
                    case "income":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_receipt_white_24dp));
                        slideTitle.setVisibility(View.GONE);
                        if (position == 5) {
                            slideTV2.setVisibility(View.VISIBLE);
                            slideTV3.setVisibility(View.VISIBLE);
                            slideTV4.setVisibility(View.VISIBLE);
                            slideTV5.setVisibility(View.VISIBLE);
                            slideTV6.setVisibility(View.VISIBLE);
                            slideTV7.setVisibility(View.VISIBLE);
                            slideTV8.setVisibility(View.VISIBLE);
                            slideTV1.setText(R.string.do_include);
                            slideTV2.setText(R.string.necessary_expenses);
                            slideTV3.setText(R.string.regular_payments);
                            slideTV4.setText(R.string.memberships);
                            slideTV5.setText(R.string.do_not_include);
                            slideTV6.setText(R.string.credit_vehicles);
                            slideTV7.setText(R.string.savings_investments);
                            slideTV8.setText(R.string.unnecessary_spending);
                        } else {
                            slideTV2.setVisibility(View.GONE);
                            slideTV3.setVisibility(View.GONE);
                            slideTV4.setVisibility(View.GONE);
                            slideTV5.setVisibility(View.GONE);
                            slideTV6.setVisibility(View.GONE);
                            slideTV7.setVisibility(View.GONE);
                            slideTV8.setVisibility(View.GONE);

                            if (position == 0) {
                                slideTV1.setText(R.string.b3_text1);
                            } else if (position == 1) {
                                slideTV1.setText(R.string.b3_text3);
                            } else if (position == 2) {
                                slideTV1.setText(R.string.b3_text4);
                            } else if (position == 3) {
                                slideTV1.setText(R.string.b3_text5);
                            } else if (position == 4) {
                                slideTV1.setText(R.string.b3_text6);
                            }
                        }
                        break;
                    case "bills":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_credit_card_white_24dp));
                        slideTitle.setVisibility(View.GONE);
                        slideTV2.setVisibility(View.GONE);
                        slideTV3.setVisibility(View.GONE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        if (position == 0) {
                            slideTV1.setText(R.string.b4_text1);
                        } else if (position == 1) {
                            slideTV1.setText(R.string.b4_text2);
                        }
                        break;
                    case "debts":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_trending_up_white_24dp));
                        slideTitle.setVisibility(View.GONE);
                        slideTV2.setVisibility(View.GONE);
                        slideTV3.setVisibility(View.GONE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        if (position == 0) {
                            slideTV1.setText(R.string.b5_text1);
                        } else if (position == 1) {
                            slideTV1.setText(R.string.b5_text2);
                        }
                        break;
                    case "savings":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_check_white_24dp));
                        slideTitle.setVisibility(View.GONE);
                        slideTV2.setVisibility(View.VISIBLE);
                        slideTV3.setVisibility(View.VISIBLE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        slideTV1.setText(R.string.thanks);
                        slideTV2.setText(R.string.b6_text1);
                        slideTV3.setText(R.string.b6_text2);
                        break;
                    case "analysis":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_network_check_white_24dp));
                        slideTV3.setVisibility(View.GONE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        if (position == 0) {
                            slideTitle.setVisibility(View.VISIBLE);
                            slideTV2.setVisibility(View.VISIBLE);
                            slideTitle.setText(R.string.almost_done);
                            slideTV1.setText(R.string.what_mynance_does);
                            slideTV2.setText(R.string.b7_text1);
                        } else if (position == 1) {
                            slideTitle.setVisibility(View.GONE);
                            slideTV2.setVisibility(View.GONE);
                            slideTV1.setText(R.string.b7_text2);
                        } else if (position == 2) {
                            slideTitle.setVisibility(View.GONE);
                            slideTV2.setVisibility(View.VISIBLE);
                            slideTV1.setText(R.string.b7_text3);
                            slideTV2.setText(R.string.b7_text4);
                        }
                        break;
                    case "weekly":
                        slideImage.setImageDrawable(getDrawable(R.drawable.ic_done_all_white_24dp));
                        slideTV2.setVisibility(View.VISIBLE);
                        slideTV3.setVisibility(View.GONE);
                        slideTV4.setVisibility(View.GONE);
                        slideTV5.setVisibility(View.GONE);
                        slideTV6.setVisibility(View.GONE);
                        slideTV7.setVisibility(View.GONE);
                        slideTV8.setVisibility(View.GONE);
                        if (position == 0) {
                            slideTitle.setVisibility(View.VISIBLE);
                            slideTitle.setText(R.string.one_more_thing);
                            slideTV1.setText(R.string.one_more_thing_2);
                            slideTV2.setText(R.string.b8_text1);
                        } else {
                            slideTitle.setVisibility(View.GONE);
                            slideTV1.setText(R.string.b8_text2);
                            slideTV2.setText(R.string.b8_text3);
                        }
                        break;

                }

                if (position == 0) {
                    previousBtn.setVisibility(View.INVISIBLE);
                } else {
                    previousBtn.setVisibility(View.VISIBLE);
                }

                if (position == slides.length - 1) {
                    nextBtn.setText(getResources().getString(R.string.start_button));
                } else {
                    nextBtn.setText(getResources().getString(R.string.next_button));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void buildFirstSlide() {
        previousBtn.setVisibility(View.INVISIBLE);
        switch (dbManager.retrieveLatestDone()) {
            case "start":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_attach_money_white_24dp));
                slideTitle.setVisibility(View.VISIBLE);
                slideTitle.setText(R.string.lets_get_real);
                slideTV1.setText(R.string.b2_text1);
                slideTV2.setVisibility(View.GONE);
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                break;
            case "income":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_receipt_white_24dp));
                slideTitle.setVisibility(View.GONE);
                slideTV2.setVisibility(View.GONE);
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTV1.setText(R.string.b3_text1);
                break;
            case "bills":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_credit_card_white_24dp));
                slideTitle.setVisibility(View.GONE);
                slideTV2.setVisibility(View.GONE);
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTV1.setText(R.string.b4_text1);
                break;
            case "debts":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_trending_up_white_24dp));
                slideTitle.setVisibility(View.GONE);
                slideTV2.setVisibility(View.GONE);
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTV1.setText(R.string.b5_text1);
                break;
            case "savings":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_check_white_24dp));
                slideTitle.setVisibility(View.GONE);
                slideTV2.setVisibility(View.VISIBLE);
                slideTV3.setVisibility(View.VISIBLE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTV1.setText(R.string.thanks);
                slideTV2.setText(R.string.b6_text1);
                slideTV3.setText(R.string.b6_text2);
                break;
            case "analysis":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_network_check_white_24dp));
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTitle.setVisibility(View.VISIBLE);
                slideTV2.setVisibility(View.VISIBLE);
                slideTitle.setText(R.string.almost_done);
                slideTV1.setText(R.string.what_mynance_does);
                slideTV2.setText(R.string.b7_text1);
                break;
            case "weekly":
                slideImage.setImageDrawable(getDrawable(R.drawable.ic_done_all_white_24dp));
                slideTV2.setVisibility(View.VISIBLE);
                slideTV3.setVisibility(View.GONE);
                slideTV4.setVisibility(View.GONE);
                slideTV5.setVisibility(View.GONE);
                slideTV6.setVisibility(View.GONE);
                slideTV7.setVisibility(View.GONE);
                slideTV8.setVisibility(View.GONE);
                slideTitle.setVisibility(View.VISIBLE);
                slideTitle.setText(R.string.one_more_thing);
                slideTV1.setText(R.string.one_more_thing_2);
                slideTV2.setText(R.string.b8_text1);
                break;
        }
    }

    private void createDots(int current_position) {
        if (dotsLayout != null)
            dotsLayout.removeAllViews();

        dots = new ImageView[slides.length];

        for (int i = 0; i < slides.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.default_dots));
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
            case R.id.slideNextBtn:
                loadNextSlide();
                break;

            case R.id.slidePreviousBtn:
                loadPreviousSlide();
                break;
        }
    }

    private void loadHome() {
        switch (dbManager.retrieveLatestDone()) {
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
