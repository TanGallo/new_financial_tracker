package ca.gotchasomething.mynance;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LayoutHelp extends MainNavigation {

    ImageButton downBudgetButton, downContactUsButton, downCreditCardButton, downDailyMoneyButton, downDebtsButton, downMoneyInButton, downMoneyOutButton,
            downPayCCButton, downSavingsButton, downSetUpButton, downSpendingReportButton, downWeeklyLimitsButton, upBudgetButton, upContactUsButton,
            upCreditCardButton, upDailyMoneyButton, upDebtsButton, upMoneyInButton, upMoneyOutButton, upPayCCButton, upSavingsButton, upSetUpButton,
            upSpendingReportButton, upWeeklyLimitsButton;
    Intent email, goToRatings, web;
    LinearLayout budgetText, contactUsText, creditCardText, dailyMoneyText, debtsText, moneyInText, moneyOutText, payCCText, savingsText, setUpText,
            spendingReportText, ratingsLayout, weeklyLimitsText;
    TextView emailText, urlText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        upSetUpButton = findViewById(R.id.upSetUpButton);
        downSetUpButton = findViewById(R.id.downSetUpButton);
        setUpText = findViewById(R.id.setUpText);
        setUpText.setVisibility(View.GONE);
        upDailyMoneyButton = findViewById(R.id.upDailyMoneyButton);
        downDailyMoneyButton = findViewById(R.id.downDailyMoneyButton);
        dailyMoneyText = findViewById(R.id.dailyMoneyText);
        dailyMoneyText.setVisibility(View.GONE);
        downMoneyInButton = findViewById(R.id.downMoneyInButton);
        upMoneyInButton = findViewById(R.id.upMoneyInButton);
        moneyInText = findViewById(R.id.moneyInText);
        moneyInText.setVisibility(View.GONE);
        downMoneyOutButton = findViewById(R.id.downMoneyOutButton);
        upMoneyOutButton = findViewById(R.id.upMoneyOutButton);
        moneyOutText = findViewById(R.id.moneyOutText);
        moneyOutText.setVisibility(View.GONE);
        downCreditCardButton = findViewById(R.id.downCreditCardButton);
        upCreditCardButton = findViewById(R.id.upCreditCardButton);
        creditCardText = findViewById(R.id.creditCardText);
        creditCardText.setVisibility(View.GONE);
        downPayCCButton = findViewById(R.id.downPayCCButton);
        upPayCCButton = findViewById(R.id.upPayCCButton);
        payCCText = findViewById(R.id.payCCText);
        payCCText.setVisibility(View.GONE);
        downWeeklyLimitsButton = findViewById(R.id.downWeeklyLimitsButton);
        upWeeklyLimitsButton = findViewById(R.id.upWeeklyLimitsButton);
        weeklyLimitsText = findViewById(R.id.weeklyLimitsText);
        weeklyLimitsText.setVisibility(View.GONE);
        downMoneyInButton.setVisibility(View.GONE);
        upMoneyInButton.setVisibility(View.GONE);
        downMoneyOutButton.setVisibility(View.GONE);
        upMoneyOutButton.setVisibility(View.GONE);
        downCreditCardButton.setVisibility(View.GONE);
        upCreditCardButton.setVisibility(View.GONE);
        downPayCCButton.setVisibility(View.GONE);
        upPayCCButton.setVisibility(View.GONE);
        downWeeklyLimitsButton.setVisibility(View.GONE);
        upWeeklyLimitsButton.setVisibility(View.GONE);
        upBudgetButton = findViewById(R.id.upBudgetButton);
        downBudgetButton = findViewById(R.id.downBudgetButton);
        budgetText = findViewById(R.id.budgetText);
        budgetText.setVisibility(View.GONE);
        upDebtsButton = findViewById(R.id.upDebtsButton);
        downDebtsButton = findViewById(R.id.downDebtsButton);
        debtsText = findViewById(R.id.debtsText);
        debtsText.setVisibility(View.GONE);
        upSavingsButton = findViewById(R.id.upSavingsButton);
        downSavingsButton = findViewById(R.id.downSavingsButton);
        savingsText = findViewById(R.id.savingsText);
        savingsText.setVisibility(View.GONE);
        upSpendingReportButton = findViewById(R.id.upSpendingReportButton);
        downSpendingReportButton = findViewById(R.id.downSpendingReportButton);
        spendingReportText = findViewById(R.id.spendingReportText);
        spendingReportText.setVisibility(View.GONE);
        upContactUsButton = findViewById(R.id.upContactUsButton);
        downContactUsButton = findViewById(R.id.downContactUsButton);
        contactUsText = findViewById(R.id.contactUsText);
        contactUsText.setVisibility(View.GONE);
        emailText = findViewById(R.id.emailText);
        emailText.setVisibility(View.GONE);
        urlText = findViewById(R.id.urlText);
        urlText.setVisibility(View.GONE);
        upSetUpButton.setVisibility(View.GONE);
        upDailyMoneyButton.setVisibility(View.GONE);
        upBudgetButton.setVisibility(View.GONE);
        upDebtsButton.setVisibility(View.GONE);
        upSavingsButton.setVisibility(View.GONE);
        upSpendingReportButton.setVisibility(View.GONE);
        upContactUsButton.setVisibility(View.GONE);
        ratingsLayout = findViewById(R.id.ratingsLayout);

        upSetUpButton.setOnClickListener(onClickUpSetUp);
        downSetUpButton.setOnClickListener(onClickDownSetUp);
        upDailyMoneyButton.setOnClickListener(onClickUpDailyMoney);
        downDailyMoneyButton.setOnClickListener(onClickDownDailyMoney);
        upBudgetButton.setOnClickListener(onClickUpBudget);
        downBudgetButton.setOnClickListener(onClickDownBudget);
        upDebtsButton.setOnClickListener(onClickUpDebts);
        downDebtsButton.setOnClickListener(onClickDownDebts);
        upSavingsButton.setOnClickListener(onClickUpSavings);
        downSavingsButton.setOnClickListener(onClickDownSavings);
        upSpendingReportButton.setOnClickListener(onClickUpSpendingReport);
        downSpendingReportButton.setOnClickListener(onClickDownSpendingReport);
        upContactUsButton.setOnClickListener(onClickUpContactUs);
        downContactUsButton.setOnClickListener(onClickDownContactUs);
        ratingsLayout.setOnClickListener(onClickRatings);
    }

    View.OnClickListener onClickUpSetUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upSetUpButton.setVisibility(View.GONE);
            downSetUpButton.setVisibility(View.VISIBLE);
            setUpText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownSetUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downSetUpButton.setVisibility(View.GONE);
            upSetUpButton.setVisibility(View.VISIBLE);
            setUpText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickUpDailyMoney = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upDailyMoneyButton.setVisibility(View.GONE);
            downDailyMoneyButton.setVisibility(View.VISIBLE);
            dailyMoneyText.setVisibility(View.GONE);
            downMoneyInButton.setVisibility(View.GONE);
            upMoneyInButton.setVisibility(View.GONE);
            downMoneyOutButton.setVisibility(View.GONE);
            upMoneyOutButton.setVisibility(View.GONE);
            downCreditCardButton.setVisibility(View.GONE);
            upCreditCardButton.setVisibility(View.GONE);
            downPayCCButton.setVisibility(View.GONE);
            upPayCCButton.setVisibility(View.GONE);
            downWeeklyLimitsButton.setVisibility(View.GONE);
            upWeeklyLimitsButton.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownDailyMoney = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downDailyMoneyButton.setVisibility(View.GONE);
            upDailyMoneyButton.setVisibility(View.VISIBLE);
            dailyMoneyText.setVisibility(View.VISIBLE);
            downMoneyInButton.setVisibility(View.VISIBLE);
            upMoneyInButton.setVisibility(View.GONE);
            downMoneyOutButton.setVisibility(View.VISIBLE);
            upMoneyOutButton.setVisibility(View.GONE);
            downCreditCardButton.setVisibility(View.VISIBLE);
            upCreditCardButton.setVisibility(View.GONE);
            downPayCCButton.setVisibility(View.VISIBLE);
            upPayCCButton.setVisibility(View.GONE);
            downWeeklyLimitsButton.setVisibility(View.VISIBLE);
            upWeeklyLimitsButton.setVisibility(View.GONE);

            downMoneyInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downMoneyInButton.setVisibility(View.GONE);
                    upMoneyInButton.setVisibility(View.VISIBLE);
                    moneyInText.setVisibility(View.VISIBLE);
                }
            });

            upMoneyInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upMoneyInButton.setVisibility(View.GONE);
                    downMoneyInButton.setVisibility(View.VISIBLE);
                    moneyInText.setVisibility(View.GONE);
                }
            });

            downMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downMoneyOutButton.setVisibility(View.GONE);
                    upMoneyOutButton.setVisibility(View.VISIBLE);
                    moneyOutText.setVisibility(View.VISIBLE);
                }
            });

            upMoneyOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upMoneyOutButton.setVisibility(View.GONE);
                    downMoneyOutButton.setVisibility(View.VISIBLE);
                    moneyOutText.setVisibility(View.GONE);
                }
            });

            downCreditCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downCreditCardButton.setVisibility(View.GONE);
                    upCreditCardButton.setVisibility(View.VISIBLE);
                    creditCardText.setVisibility(View.VISIBLE);
                }
            });

            upCreditCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upCreditCardButton.setVisibility(View.GONE);
                    downCreditCardButton.setVisibility(View.VISIBLE);
                    creditCardText.setVisibility(View.GONE);
                }
            });

            downPayCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downPayCCButton.setVisibility(View.GONE);
                    upPayCCButton.setVisibility(View.VISIBLE);
                    payCCText.setVisibility(View.VISIBLE);
                }
            });

            upPayCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upPayCCButton.setVisibility(View.GONE);
                    downPayCCButton.setVisibility(View.VISIBLE);
                    payCCText.setVisibility(View.GONE);
                }
            });

            downWeeklyLimitsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downWeeklyLimitsButton.setVisibility(View.GONE);
                    upWeeklyLimitsButton.setVisibility(View.VISIBLE);
                    weeklyLimitsText.setVisibility(View.VISIBLE);
                }
            });

            upWeeklyLimitsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upWeeklyLimitsButton.setVisibility(View.GONE);
                    downWeeklyLimitsButton.setVisibility(View.VISIBLE);
                    weeklyLimitsText.setVisibility(View.GONE);
                }
            });
        }
    };

    View.OnClickListener onClickUpBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upBudgetButton.setVisibility(View.GONE);
            downBudgetButton.setVisibility(View.VISIBLE);
            budgetText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downBudgetButton.setVisibility(View.GONE);
            upBudgetButton.setVisibility(View.VISIBLE);
            budgetText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickUpDebts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upDebtsButton.setVisibility(View.GONE);
            downDebtsButton.setVisibility(View.VISIBLE);
            debtsText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownDebts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downDebtsButton.setVisibility(View.GONE);
            upDebtsButton.setVisibility(View.VISIBLE);
            debtsText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickUpSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upSavingsButton.setVisibility(View.GONE);
            downSavingsButton.setVisibility(View.VISIBLE);
            savingsText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downSavingsButton.setVisibility(View.GONE);
            upSavingsButton.setVisibility(View.VISIBLE);
            savingsText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickUpSpendingReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upSpendingReportButton.setVisibility(View.GONE);
            downSpendingReportButton.setVisibility(View.VISIBLE);
            spendingReportText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownSpendingReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downSpendingReportButton.setVisibility(View.GONE);
            upSpendingReportButton.setVisibility(View.VISIBLE);
            spendingReportText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener onClickUpContactUs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upContactUsButton.setVisibility(View.GONE);
            downContactUsButton.setVisibility(View.VISIBLE);
            contactUsText.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownContactUs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downContactUsButton.setVisibility(View.GONE);
            upContactUsButton.setVisibility(View.VISIBLE);
            contactUsText.setVisibility(View.VISIBLE);
            emailText.setVisibility(View.VISIBLE);
            urlText.setVisibility(View.VISIBLE);

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

            urlText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getString(R.string.google_play_url);

                    web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(url));
                    startActivity(web);
                }
            });

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
}
