package ca.gotchasomething.mynance;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class LayoutHelp extends MainNavigation {

    Intent email,
            goToRatings,
            web;
    LinearLayout contactUsText,
            ratingsLayout;
    TextView budgetQ1,
            budgetQ2,
            budgetA1,
            budgetA2,
            dailyMoneyQ3,
            dailyMoneyA3,
            dailyMoneyQ4,
            dailyMoneyA4,
            dailyMoneyQ5,
            dailyMoneyA5,
            dailyMoneyA6A,
            dailyMoneyA6B,
            dailyMoneyQ8,
            dailyMoneyA8,
            debtsQ2,
            debtsA2,
            helpInstructions,
            emailText,
            urlText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuConfig();

        helpInstructions = findViewById(R.id.helpInstructions);

        dailyMoneyQ3 = findViewById(R.id.dailyMoneyQ3);
        dailyMoneyA3 = findViewById(R.id.dailyMoneyA3);
        dailyMoneyA3.setVisibility(View.GONE);
        dailyMoneyQ4 = findViewById(R.id.dailyMoneyQ4);
        dailyMoneyA4 = findViewById(R.id.dailyMoneyA4);
        dailyMoneyA4.setVisibility(View.GONE);
        dailyMoneyQ5 = findViewById(R.id.dailyMoneyQ5);
        dailyMoneyA5 = findViewById(R.id.dailyMoneyA5);
        dailyMoneyA5.setVisibility(View.GONE);
        dailyMoneyA6A = findViewById(R.id.dailyMoneyA6A);
        dailyMoneyA6A.setVisibility(View.GONE);
        dailyMoneyA6B = findViewById(R.id.dailyMoneyA6B);
        dailyMoneyA6B.setVisibility(View.GONE);
        dailyMoneyQ8 = findViewById(R.id.dailyMoneyQ8);
        dailyMoneyA8 = findViewById(R.id.dailyMoneyA8);
        dailyMoneyA8.setVisibility(View.GONE);

        budgetQ1 = findViewById(R.id.budgetQ1);
        budgetA1 = findViewById(R.id.budgetA1);
        budgetA1.setVisibility(View.GONE);
        budgetQ2 = findViewById(R.id.budgetQ2);
        budgetA2 = findViewById(R.id.budgetA2);
        budgetA2.setVisibility(View.GONE);

        debtsQ2 = findViewById(R.id.debtsQ2);
        debtsA2 = findViewById(R.id.debtsA2);
        debtsA2.setVisibility(View.GONE);

        contactUsText = findViewById(R.id.contactUsText);
        emailText = findViewById(R.id.emailText);
        emailText.setOnClickListener(onClickEmail);
        urlText = findViewById(R.id.urlText);
        urlText.setOnClickListener(onClickWebsite);

        ratingsLayout = findViewById(R.id.ratingsLayout);
        ratingsLayout.setOnClickListener(onClickRatings);

        dailyMoneyQ3.setOnClickListener(onClickQ3);
        dailyMoneyQ4.setOnClickListener(onClickQ4);
        dailyMoneyQ5.setOnClickListener(onClickQ5);
        dailyMoneyQ8.setOnClickListener(onClickQ8);
        budgetQ2.setOnClickListener(onClickBdgtQ2);
        budgetQ1.setOnClickListener(onClickBdgtQ1);
        debtsQ2.setOnClickListener(onClickDebtQ2);
    }

    View.OnClickListener onClickQ3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dailyMoneyA3.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickQ4 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dailyMoneyA4.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickQ5 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dailyMoneyA5.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickQ8 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dailyMoneyA8.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickBdgtQ2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            budgetA2.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickBdgtQ1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            budgetA1.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickDebtQ2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            debtsA2.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickRatings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToRatings = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_url)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToRatings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            } else {
                goToRatings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(goToRatings);
        }
    };

    View.OnClickListener onClickEmail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = new Intent(Intent.ACTION_SEND);
                    email.setType("message/rfc822");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});

                    try {
                        startActivity(Intent.createChooser(email, getString(R.string.email_address)));
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.email_warning), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    View.OnClickListener onClickWebsite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            urlText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getString(R.string.website);

                    web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(url));
                    startActivity(web);
                }
            });
        }
    };
}

