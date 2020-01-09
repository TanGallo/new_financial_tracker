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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

import ca.gotchasomething.mynance.data.AccountsDb;
import ca.gotchasomething.mynance.data.BudgetDb;
import ca.gotchasomething.mynance.data.SetUpDb;

public class SlidesOnboardingP extends AppCompatActivity implements View.OnClickListener {

    AccountsDb acctDb;
    private AdapterSlides adapter;
    BudgetDb expDb;
    private Button previousButton, skipButton, nextButton;
    DbManager dbManager;
    Double balanceAmount;
    ImageView slide1Image;
    private ImageView[] dots;
    //private int[] slides = new int[6];
    private int[] slides = {
            R.layout.a1_slides_onboarding_background,
            R.layout.a1_slides_onboarding_background,
            R.layout.a1_slides_onboarding_background,
            R.layout.a1_slides_onboarding_background,
            R.layout.a1_slides_onboarding_background,
            R.layout.a1_slides_onboarding_background
    };
    private int slideImage[] = {
            R.mipmap.ic_launcher,
            R.drawable.ic_create_new_folder_white_24dp,
            R.drawable.ic_account_balance_white_24dp,
            R.drawable.ic_equalizer_white_24dp,
            R.drawable.ic_credit_card_white_24dp,
            R.drawable.ic_help_outline_white_24dp};
    /*private String[] slideTitle = new String[] {
            getResources().getString(R.string.welcome_to_mynance),
            getResources().getString(R.string.step_1),
            getResources().getString(R.string.step_2),
            null,
            null,
            null};
    private String[] slideDescription = new String[] {
            getResources().getString(R.string.a_text1),
            getResources().getString(R.string.a_text2),
            getResources().getString(R.string.a_text3),
            getResources().getString(R.string.a_text4),
            getResources().getString(R.string.a_text5),
            getResources().getString(R.string.a_text6)};*/
    private LinearLayout dotsLayout;
    RelativeLayout slide1Layout;
    SetUpDb setUpDb;
    String[] slideTitle, slideDescription;
    TextView slide1Title, slide1Description;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreferences()) { loadHome();
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.a1_slides_onboarding_background);

        slideTitle = new String[] {
                getResources().getString(R.string.welcome_to_mynance),
                getResources().getString(R.string.step_1),
                getResources().getString(R.string.step_2),
                null,
                null,
                null};
        slideDescription = new String[] {
                getResources().getString(R.string.a_text1),
                getResources().getString(R.string.a_text2),
                getResources().getString(R.string.a_text3),
                getResources().getString(R.string.a_text4),
                getResources().getString(R.string.a_text5),
                getResources().getString(R.string.a_text6)};

        viewPager = findViewById(R.id.viewPager);
        adapter = new AdapterSlides(slides, this);
        //adapter = new AdapterSlides(slides, slideImage, slideTitle, slideDescription, this);
        viewPager.setAdapter(adapter);

        slide1Layout = findViewById(R.id.slide1Layout);
        slide1Image = findViewById(R.id.slide1Image);
        slide1Title = findViewById(R.id.slide1Title);
        slide1Description = findViewById(R.id.slide1Description);
        dotsLayout = findViewById(R.id.dotsLayout);
        previousButton = findViewById(R.id.previousButton);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton.setOnClickListener(this);
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
                slide1Image.setImageDrawable(getDrawable(slideImage[position]));
                //String slideImage1 = slideTitle[position];
                //slide1Title.setText(slideImage1);
                slide1Title.setText(slideTitle[position]);
                slide1Description.setText(slideDescription[position]);
                //createSlideImages(position);
                //createSlideText(position);

                /*switch(position) {
                    case 0:
                        slide1Image.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                        slide1Title.setVisibility(View.VISIBLE);
                        slide1Title.setText(R.string.welcome_to_mynance);
                        slide1Description.setText(R.string.a_text1);
                        break;
                    case 1:
                        slide1Image.setImageDrawable(getDrawable(R.drawable.ic_create_new_folder_white_24dp));
                        slide1Title.setVisibility(View.VISIBLE);
                        slide1Title.setText(R.string.step_1);
                        slide1Description.setText(R.string.a_text2);
                        break;
                    case 2:
                        slide1Image.setImageDrawable(getDrawable(R.drawable.ic_account_balance_white_24dp));
                        slide1Title.setVisibility(View.VISIBLE);
                        slide1Title.setText(R.string.step_2);
                        slide1Description.setText(R.string.a_text3);
                        break;
                    case 3:
                        slide1Image.setImageDrawable(getDrawable(R.drawable.ic_equalizer_white_24dp));
                        slide1Title.setVisibility(View.INVISIBLE);
                        slide1Description.setText(R.string.a_text4);
                        break;
                    case 4:
                        slide1Image.setImageDrawable(getDrawable(R.drawable.ic_credit_card_white_24dp));
                        slide1Title.setVisibility(View.INVISIBLE);
                        slide1Description.setText(R.string.a_text5);
                        break;
                    case 5:
                        slide1Image.setImageDrawable(getDrawable(R.drawable.ic_help_outline_white_24dp));
                        slide1Title.setVisibility(View.INVISIBLE);
                        slide1Description.setText(R.string.a_text6);
                        break;
                }*/

                if (position == slides.length - 1) {
                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setText(getResources().getString(R.string.start_button));
                    skipButton.setVisibility(View.GONE);
                } else if(position == 0) {
                    previousButton.setVisibility(View.GONE);
                    nextButton.setText(getResources().getString(R.string.next_button));
                    skipButton.setVisibility(View.VISIBLE);
                } else {
                    previousButton.setVisibility(View.VISIBLE);
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
                        AppCompatResources.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(
                        AppCompatResources.getDrawable(this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);

            dotsLayout.addView(dots[i], params);
        }
    }

    /*private void createSlideImages(int current_position) {
        if (slide1Layout != null)
            slide1Layout.removeAllViews();

        slideImage = new ImageView[slides.length];
        for (int i = 0; i < slides.length; i++) {
            slideImage[i] = new ImageView(this);

            switch(current_position) {
                case 0:
                    slideImage[0].setImageDrawable(AppCompatResources.getDrawable(this, R.mipmap.ic_launcher));
                    break;
                case 1:
                    slideImage[1].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_create_new_folder_white_24dp));
                    break;
                case 2:
                    slideImage[2].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_account_balance_white_24dp));
                    break;
                case 3:
                    slideImage[3].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_equalizer_white_24dp));
                    break;
                case 4:
                    slideImage[4].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_credit_card_white_24dp));
                    break;
                case 5:
                    slideImage[5].setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_help_outline_white_24dp));
                    break;
            }
            slide1Layout.addView(slideImage[i]);
        }
    }*/

    /*private void createSlideText(int current_position) {
        slideTitle = new String[] {
                getString(R.string.welcome_to_mynance),
                getString(R.string.step_1),
                getString(R.string.step_2),
                null,
                null,
                null};
        slideDescription = new String[] {
                getString(R.string.a_text1),
                getString(R.string.a_text2),
                getString(R.string.a_text3),
                getString(R.string.a_text4),
                getString(R.string.a_text5),
                getString(R.string.a_text6)};

            slide1Title.setText(slideTitle[current_position]);
            slide1Description.setText(slideDescription[current_position]);
    }*/

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

            case R.id.previousButton:
                loadPreviousSlide();
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
                    "N/A",
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
                    "E",
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

    private void loadPreviousSlide() {
        int previous = viewPager.getCurrentItem() - 1;

        if (previous > -1) {
            viewPager.setCurrentItem(previous);
        }
    }
}
