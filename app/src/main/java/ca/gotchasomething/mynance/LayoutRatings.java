package ca.gotchasomething.mynance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class LayoutRatings extends LayoutMoneyOut {

    Button emailNoButton,
            emailYesButton,
            enjoyNoButton,
            enjoyNotSureButton,
            enjoyYesButton,
            rateNoButton,
            rateYesButton;
    General rateGen;
    Intent email,
            goToRatings,
            rateToMonOut;
    LinearLayout ratEnjoyLayout,
            ratEmailLayout,
            ratRateLayout;
    String clicksS = null,
            clicksES = null;
    TextView ratingsLabel,
            ratingsYesLabel,
            ratingsNoLabel,
            ratTemp,
            ratTempE;
    public static int clicked = 0;
    public static int clickedE = 0;
    public static final String SRPE = "shared ratings pref";
    public static final String CTE = String.valueOf(clickedE);
    public static final String SRP = "shared ratings pref";
    public static final String CT = String.valueOf(clicked);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c6_layout_ratings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        rateGen = new General();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ratEnjoyLayout = findViewById(R.id.ratEnjoyLayout);
        ratingsLabel = findViewById(R.id.ratLabel);
        enjoyNoButton = findViewById(R.id.ratNoJoyBtn);
        enjoyNotSureButton = findViewById(R.id.ratNotSureJoyBtn);
        enjoyYesButton = findViewById(R.id.ratYesJoyBtn);

        ratEmailLayout = findViewById(R.id.ratEmailLayout);
        ratEmailLayout.setVisibility(View.GONE);
        ratingsNoLabel = findViewById(R.id.ratNoLabel);
        emailNoButton = findViewById(R.id.ratNoEmailBtn);
        emailYesButton = findViewById(R.id.ratYesEmailBtn);

        ratRateLayout = findViewById(R.id.ratRateLayout);
        ratRateLayout.setVisibility(View.GONE);
        ratingsYesLabel = findViewById(R.id.ratYesLabel);
        rateNoButton = findViewById(R.id.ratNoRateBtn);
        rateYesButton = findViewById(R.id.ratYesRateBtn);

        enjoyNoButton.setOnClickListener(onClickNoJoyBtn);
        enjoyNotSureButton.setOnClickListener(onClickNotSureJoyBtn);
        enjoyYesButton.setOnClickListener(onClickYesJoyBtn);

        ratTemp = findViewById(R.id.ratTemp);
        ratTemp.setVisibility(View.GONE);
        ratTempE = findViewById(R.id.ratTempE);
        ratTempE.setVisibility(View.GONE);

        loadClicks();
        updateClicks();
        loadClicksE();
        updateClicksE();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(String.valueOf(clicked), ratTemp.getText().toString());
        super.onSaveInstanceState(savedInstanceState);

    }

    View.OnClickListener onClickNoJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ratEnjoyLayout.setVisibility(View.GONE);
            ratEmailLayout.setVisibility(View.VISIBLE);

            emailNoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.ask_later, Toast.LENGTH_LONG).show();

                    rateToMonOut = new Intent(LayoutRatings.this, LayoutMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);
                }
            });
            emailYesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedE++;
                    ratTempE.setText(String.valueOf(clickedE));

                    saveClicksE();

                    rateToMonOut = new Intent(LayoutRatings.this, LayoutMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);

                    email = new Intent(Intent.ACTION_SEND);
                    email.setType("message/rfc822");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});

                    try {
                        startActivity(Intent.createChooser(email, getString(R.string.choose_email)));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.email_warning), Toast.LENGTH_LONG).show();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    } else {
                        email.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    }
                }
            });
        }
    };

    View.OnClickListener onClickNotSureJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getApplicationContext(), R.string.ask_later, Toast.LENGTH_LONG).show();

            rateToMonOut = new Intent(LayoutRatings.this, LayoutMoneyOut.class);
            rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(rateToMonOut);
        }
    };

    View.OnClickListener onClickYesJoyBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ratEnjoyLayout.setVisibility(View.GONE);
            ratRateLayout.setVisibility(View.VISIBLE);

            rateNoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.ask_later, Toast.LENGTH_LONG).show();

                    rateToMonOut = new Intent(LayoutRatings.this, LayoutMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);
                }
            });
            rateYesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked++;
                    ratTemp.setText(String.valueOf(clicked));

                    saveClicks();

                    rateToMonOut = new Intent(LayoutRatings.this, LayoutMoneyOut.class);
                    rateToMonOut.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(rateToMonOut);

                    goToRatings = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_url)));
                    goToRatings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToRatings);
                }
            });
        }
    };

    public void saveClicks() {
        sp = getApplicationContext().getSharedPreferences(SRP, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CT, ratTemp.getText().toString());
        clicked = Integer.valueOf(ratTemp.getText().toString());
        editor.apply();
    }

    public void loadClicks() {
        sp = getApplicationContext().getSharedPreferences(SRP, getApplicationContext().MODE_PRIVATE);
        clicksS = sp.getString(CT, "");
    }

    public void updateClicks() {
        ratTemp.setText(clicksS);

        if (!clicksS.equals("")) {
            clicked = Integer.valueOf(clicksS);
        } else {
            clicked = 0;
        }
    }

    public void saveClicksE() {
        spE = getApplicationContext().getSharedPreferences(SRPE, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editorE = spE.edit();
        editorE.putString(CTE, ratTempE.getText().toString());
        clickedE = Integer.valueOf(ratTempE.getText().toString());
        editorE.apply();
    }

    public void loadClicksE() {
        spE = getApplicationContext().getSharedPreferences(SRPE, getApplicationContext().MODE_PRIVATE);
        clicksES = spE.getString(CTE, "");
    }

    public void updateClicksE() {
        ratTempE.setText(clicksES);

        if (!clicksES.equals("")) {
            clickedE = Integer.valueOf(clicksES);
        } else {
            clickedE = 0;
        }
    }
}