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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
//import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class SlidesOnboardingP extends AppCompatActivity implements View.OnClickListener {

    AccountsDb acctDb;
    private AdapterSlides adapter;
    BudgetDb expDb;
    private Button skipButton, nextButton;
    Cursor setUpCursor;
    DbHelper setUpHelper;
    DbManager dbManager;
    Double balanceAmount;
    private ImageView[] dots;
    int position, tourDoneYes;
    private int[] slides = {
            R.layout.a_slides_onboarding_1,
            R.layout.a_slides_onboarding_2,
            R.layout.a_slides_onboarding_3,
            R.layout.a_slides_onboarding_4,
            R.layout.a_slides_onboarding_5,
            R.layout.a_slides_onboarding_6
    };
    private LinearLayout dotsLayout;
    SetUpDb setUpDb;
    SQLiteDatabase setUpDbDb;
    String latestDone = null;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dbManager = new DbManager(this);

        if (new PreferenceManager(this).checkPreferences()) {
            loadHome();
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.a1_slides_onboarding_background);

        viewPager = findViewById(R.id.viewPager);
        adapter = new AdapterSlides(slides, this);
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

                if (position == slides.length - 1) {
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

            case R.id.skipButton:
                loadHome();
                new PreferenceManager(this).writePreferences();
                break;
        }
    }

    private void loadHome() {
        dbManager = new DbManager(this);
        if(dbManager.getSetUp().size() == 0) {
            setUpDb = new SetUpDb(
                    "start",
                    balanceAmount,
                    0);
            dbManager.addSetUp(setUpDb);
        }
        if(dbManager.getAccounts().size() == 0) {
            acctDb = new AccountsDb(
                    getString(R.string.main_account),
                    0.0,
                    "N",
                    "N",
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    "N/A",
                    0.0,
                    0);
            dbManager.addAccounts(acctDb);
        }
        if(dbManager.getExpenses().size() == 0) {
            expDb = new BudgetDb(
                    getString(R.string.other),
                    0.0,
                    "Y",
                    "N",
                    12.0,
                    0.0,
                    "B",
                    "N",
                    0);
            dbManager.addBudget(expDb);
        }
        startActivity(new Intent(this, LayoutSetUp.class));
        finish();

    }

    private void loadNextSlide() {
        int next = viewPager.getCurrentItem() + 1;

        if (next < slides.length) {
            viewPager.setCurrentItem(next);
        } else {
            loadHome();
            new PreferenceManager(this).writePreferences();
        }
    }
}
