package ca.gotchasomething.mynance;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class LayoutHelp extends MainNavigation {

    ImageButton downBudgetButton, downContactUsButton, downDailyMoneyButton, downDebtsButton, downSavingsButton, downSetUpButton, upBudgetButton,
            upContactUsButton, upDailyMoneyButton, upDebtsButton, upSavingsButton, upSetUpButton;
    Intent email, goToRatings, web;
    LinearLayout contactUsText, ratingsLayout;
    TextView budgetQ1, budgetA1, budgetQ2, budgetA2, budgetA2A, budgetA2B, budgetA2C, budgetA2D, budgetA2E, budgetA2F, budgetA2G, budgetQ3, budgetA3,
            budgetA3A, budgetA3B, budgetA3C, dailyMoneyQ1, dailyMoneyA1, dailyMoneyQ2, dailyMoneyA2, dailyMoneyQ3, dailyMoneyA3, dailyMoneyQ4, dailyMoneyA4,
            dailyMoneyQ5, dailyMoneyA5, dailyMoneyA5A, dailyMoneyA5B, dailyMoneyA5C, dailyMoneyQ6, dailyMoneyA6, dailyMoneyA6A, dailyMoneyA6B, dailyMoneyQ7,
            dailyMoneyA7, dailyMoneyA7A, dailyMoneyA7B, dailyMoneyQ8, dailyMoneyA8, dailyMoneyQ9, dailyMoneyA9, dailyMoneyQ10, dailyMoneyA10,
            debtsQ1, debtsA1, debtsQ2, debtsA2, debtsQ3, debtsA3, debtsQ4, debtsA4, debtsQ5, debtsA5, helpInstructions, savingsQ1, savingsA1, savingsQ2,
            savingsA2, savingsQ3, savingsA3, savingsQ4, savingsA4, savingsQ5, savingsA5, setUpQ1, setUpA1, emailText, urlText;

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

        helpInstructions = findViewById(R.id.helpInstructions);

        upSetUpButton = findViewById(R.id.upSetUpButton);
        upSetUpButton.setVisibility(View.GONE);
        downSetUpButton = findViewById(R.id.downSetUpButton);
        setUpQ1 = findViewById(R.id.setUpQ1);
        setUpQ1.setVisibility(View.GONE);
        setUpA1 = findViewById(R.id.setUpA1);
        setUpA1.setVisibility(View.GONE);

        upDailyMoneyButton = findViewById(R.id.upDailyMoneyButton);
        upDailyMoneyButton.setVisibility(View.GONE);
        downDailyMoneyButton = findViewById(R.id.downDailyMoneyButton);
        dailyMoneyQ1 = findViewById(R.id.dailyMoneyQ1);
        dailyMoneyQ1.setVisibility(View.GONE);
        dailyMoneyA1 = findViewById(R.id.dailyMoneyA1);
        dailyMoneyA1.setVisibility(View.GONE);
        dailyMoneyQ2 = findViewById(R.id.dailyMoneyQ2);
        dailyMoneyQ2.setVisibility(View.GONE);
        dailyMoneyA2 = findViewById(R.id.dailyMoneyA2);
        dailyMoneyA2.setVisibility(View.GONE);
        dailyMoneyQ3 = findViewById(R.id.dailyMoneyQ3);
        dailyMoneyQ3.setVisibility(View.GONE);
        dailyMoneyA3 = findViewById(R.id.dailyMoneyA3);
        dailyMoneyA3.setVisibility(View.GONE);
        dailyMoneyQ4 = findViewById(R.id.dailyMoneyQ4);
        dailyMoneyQ4.setVisibility(View.GONE);
        dailyMoneyA4 = findViewById(R.id.dailyMoneyA4);
        dailyMoneyA4.setVisibility(View.GONE);
        dailyMoneyQ5 = findViewById(R.id.dailyMoneyQ5);
        dailyMoneyQ5.setVisibility(View.GONE);
        dailyMoneyA5 = findViewById(R.id.dailyMoneyA5);
        dailyMoneyA5.setVisibility(View.GONE);
        dailyMoneyA5A = findViewById(R.id.dailyMoneyA5A);
        dailyMoneyA5A.setVisibility(View.GONE);
        dailyMoneyA5B = findViewById(R.id.dailyMoneyA5B);
        dailyMoneyA5B.setVisibility(View.GONE);
        dailyMoneyA5C = findViewById(R.id.dailyMoneyA5C);
        dailyMoneyA5C.setVisibility(View.GONE);
        dailyMoneyQ6 = findViewById(R.id.dailyMoneyQ6);
        dailyMoneyQ6.setVisibility(View.GONE);
        dailyMoneyA6 = findViewById(R.id.dailyMoneyA6);
        dailyMoneyA6.setVisibility(View.GONE);
        dailyMoneyA6A = findViewById(R.id.dailyMoneyA6A);
        dailyMoneyA6A.setVisibility(View.GONE);
        dailyMoneyA6B = findViewById(R.id.dailyMoneyA6B);
        dailyMoneyA6B.setVisibility(View.GONE);
        dailyMoneyQ7 = findViewById(R.id.dailyMoneyQ7);
        dailyMoneyQ7.setVisibility(View.GONE);
        dailyMoneyA7 = findViewById(R.id.dailyMoneyA7);
        dailyMoneyA7.setVisibility(View.GONE);
        dailyMoneyA7A = findViewById(R.id.dailyMoneyA7A);
        dailyMoneyA7A.setVisibility(View.GONE);
        dailyMoneyA7B = findViewById(R.id.dailyMoneyA7B);
        dailyMoneyA7B.setVisibility(View.GONE);
        dailyMoneyQ8 = findViewById(R.id.dailyMoneyQ8);
        dailyMoneyQ8.setVisibility(View.GONE);
        dailyMoneyA8 = findViewById(R.id.dailyMoneyA8);
        dailyMoneyA8.setVisibility(View.GONE);
        dailyMoneyQ9 = findViewById(R.id.dailyMoneyQ9);
        dailyMoneyQ9.setVisibility(View.GONE);
        dailyMoneyA9 = findViewById(R.id.dailyMoneyA9);
        dailyMoneyA9.setVisibility(View.GONE);
        dailyMoneyQ10 = findViewById(R.id.dailyMoneyQ10);
        dailyMoneyQ10.setVisibility(View.GONE);
        dailyMoneyA10 = findViewById(R.id.dailyMoneyA10);
        dailyMoneyA10.setVisibility(View.GONE);

        upBudgetButton = findViewById(R.id.upBudgetButton);
        upBudgetButton.setVisibility(View.GONE);
        downBudgetButton = findViewById(R.id.downBudgetButton);
        budgetQ1 = findViewById(R.id.budgetQ1);
        budgetQ1.setVisibility(View.GONE);
        budgetA1 = findViewById(R.id.budgetA1);
        budgetA1.setVisibility(View.GONE);
        budgetQ2 = findViewById(R.id.budgetQ2);
        budgetQ2.setVisibility(View.GONE);
        budgetA2 = findViewById(R.id.budgetA2);
        budgetA2.setVisibility(View.GONE);
        budgetA2A = findViewById(R.id.budgetA2A);
        budgetA2A.setVisibility(View.GONE);
        budgetA2B = findViewById(R.id.budgetA2B);
        budgetA2B.setVisibility(View.GONE);
        budgetA2C = findViewById(R.id.budgetA2C);
        budgetA2C.setVisibility(View.GONE);
        budgetA2D = findViewById(R.id.budgetA2D);
        budgetA2D.setVisibility(View.GONE);
        budgetA2E = findViewById(R.id.budgetA2E);
        budgetA2E.setVisibility(View.GONE);
        budgetA2F = findViewById(R.id.budgetA2F);
        budgetA2F.setVisibility(View.GONE);
        budgetA2G = findViewById(R.id.budgetA2G);
        budgetA2G.setVisibility(View.GONE);
        budgetQ3 = findViewById(R.id.budgetQ3);
        budgetQ3.setVisibility(View.GONE);
        budgetA3 = findViewById(R.id.budgetA3);
        budgetA3.setVisibility(View.GONE);
        budgetA3A = findViewById(R.id.budgetA3A);
        budgetA3A.setVisibility(View.GONE);
        budgetA3B = findViewById(R.id.budgetA3B);
        budgetA3B.setVisibility(View.GONE);
        budgetA3C = findViewById(R.id.budgetA3C);
        budgetA3C.setVisibility(View.GONE);

        upDebtsButton = findViewById(R.id.upDebtsButton);
        upDebtsButton.setVisibility(View.GONE);
        downDebtsButton = findViewById(R.id.downDebtsButton);
        debtsQ1 = findViewById(R.id.debtsQ1);
        debtsQ1.setVisibility(View.GONE);
        debtsA1 = findViewById(R.id.debtsA1);
        debtsA1.setVisibility(View.GONE);
        debtsQ2 = findViewById(R.id.debtsQ2);
        debtsQ2.setVisibility(View.GONE);
        debtsA2 = findViewById(R.id.debtsA2);
        debtsA2.setVisibility(View.GONE);
        debtsQ3 = findViewById(R.id.debtsQ3);
        debtsQ3.setVisibility(View.GONE);
        debtsA3 = findViewById(R.id.debtsA3);
        debtsA3.setVisibility(View.GONE);
        debtsQ4 = findViewById(R.id.debtsQ4);
        debtsQ4.setVisibility(View.GONE);
        debtsA4 = findViewById(R.id.debtsA4);
        debtsA4.setVisibility(View.GONE);
        debtsQ5 = findViewById(R.id.debtsQ5);
        debtsQ5.setVisibility(View.GONE);
        debtsA5 = findViewById(R.id.debtsA5);
        debtsA5.setVisibility(View.GONE);

        upSavingsButton = findViewById(R.id.upSavingsButton);
        upSavingsButton.setVisibility(View.GONE);
        downSavingsButton = findViewById(R.id.downSavingsButton);
        savingsQ1 = findViewById(R.id.savingsQ1);
        savingsQ1.setVisibility(View.GONE);
        savingsA1 = findViewById(R.id.savingsA1);
        savingsA1.setVisibility(View.GONE);
        savingsQ2 = findViewById(R.id.savingsQ2);
        savingsQ2.setVisibility(View.GONE);
        savingsA2 = findViewById(R.id.savingsA2);
        savingsA2.setVisibility(View.GONE);
        savingsQ3 = findViewById(R.id.savingsQ3);
        savingsQ3.setVisibility(View.GONE);
        savingsA3 = findViewById(R.id.savingsA3);
        savingsA3.setVisibility(View.GONE);
        savingsQ4 = findViewById(R.id.savingsQ4);
        savingsQ4.setVisibility(View.GONE);
        savingsA4 = findViewById(R.id.savingsA4);
        savingsA4.setVisibility(View.GONE);
        savingsQ5 = findViewById(R.id.savingsQ5);
        savingsQ5.setVisibility(View.GONE);
        savingsA5 = findViewById(R.id.savingsA5);
        savingsA5.setVisibility(View.GONE);

        upContactUsButton = findViewById(R.id.upContactUsButton);
        upContactUsButton.setVisibility(View.GONE);
        downContactUsButton = findViewById(R.id.downContactUsButton);
        contactUsText = findViewById(R.id.contactUsText);
        contactUsText.setVisibility(View.GONE);
        emailText = findViewById(R.id.emailText);
        emailText.setVisibility(View.GONE);
        urlText = findViewById(R.id.urlText);
        urlText.setVisibility(View.GONE);

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
        upContactUsButton.setOnClickListener(onClickUpContactUs);
        downContactUsButton.setOnClickListener(onClickDownContactUs);
        ratingsLayout.setOnClickListener(onClickRatings);
    }

    View.OnClickListener onClickDownSetUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpInstructions.setVisibility(View.GONE);
            downSetUpButton.setVisibility(View.GONE);
            upSetUpButton.setVisibility(View.VISIBLE);

            setUpQ1.setVisibility(View.VISIBLE);
            setUpQ1.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUpA1.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    View.OnClickListener onClickUpSetUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upSetUpButton.setVisibility(View.GONE);
            downSetUpButton.setVisibility(View.VISIBLE);
            setUpQ1.setVisibility(View.GONE);
            setUpA1.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownDailyMoney = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpInstructions.setVisibility(View.GONE);
            downDailyMoneyButton.setVisibility(View.GONE);
            upDailyMoneyButton.setVisibility(View.VISIBLE);

            dailyMoneyQ1.setVisibility(View.VISIBLE);
            dailyMoneyQ1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA1.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ2.setVisibility(View.VISIBLE);
            dailyMoneyQ2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA2.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ3.setVisibility(View.VISIBLE);
            dailyMoneyQ3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA3.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ4.setVisibility(View.VISIBLE);
            dailyMoneyQ4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA4.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ5.setVisibility(View.VISIBLE);
            dailyMoneyQ5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA5.setVisibility(View.VISIBLE);
                    dailyMoneyA5A.setVisibility(View.VISIBLE);
                    dailyMoneyA5B.setVisibility(View.VISIBLE);
                    dailyMoneyA5C.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ6.setVisibility(View.VISIBLE);
            dailyMoneyQ6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA6.setVisibility(View.VISIBLE);
                    dailyMoneyA6A.setVisibility(View.VISIBLE);
                    dailyMoneyA6B.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ7.setVisibility(View.VISIBLE);
            dailyMoneyQ7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA7.setVisibility(View.VISIBLE);
                    dailyMoneyA7A.setVisibility(View.VISIBLE);
                    dailyMoneyA7B.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ8.setVisibility(View.VISIBLE);
            dailyMoneyQ8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA8.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ9.setVisibility(View.VISIBLE);
            dailyMoneyQ9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA9.setVisibility(View.VISIBLE);
                }
            });

            dailyMoneyQ10.setVisibility(View.VISIBLE);
            dailyMoneyQ10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyMoneyA10.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    View.OnClickListener onClickUpDailyMoney = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upDailyMoneyButton.setVisibility(View.GONE);
            downDailyMoneyButton.setVisibility(View.VISIBLE);

            dailyMoneyQ1.setVisibility(View.GONE);
            dailyMoneyA1.setVisibility(View.GONE);
            dailyMoneyQ2.setVisibility(View.GONE);
            dailyMoneyA2.setVisibility(View.GONE);
            dailyMoneyQ3.setVisibility(View.GONE);
            dailyMoneyA3.setVisibility(View.GONE);
            dailyMoneyQ4.setVisibility(View.GONE);
            dailyMoneyA4.setVisibility(View.GONE);
            dailyMoneyQ5.setVisibility(View.GONE);
            dailyMoneyA5.setVisibility(View.GONE);
            dailyMoneyA5A.setVisibility(View.GONE);
            dailyMoneyA5B.setVisibility(View.GONE);
            dailyMoneyA5C.setVisibility(View.GONE);
            dailyMoneyQ6.setVisibility(View.GONE);
            dailyMoneyA6.setVisibility(View.GONE);
            dailyMoneyA6A.setVisibility(View.GONE);
            dailyMoneyA6B.setVisibility(View.GONE);
            dailyMoneyQ7.setVisibility(View.GONE);
            dailyMoneyA7.setVisibility(View.GONE);
            dailyMoneyA7A.setVisibility(View.GONE);
            dailyMoneyA7B.setVisibility(View.GONE);
            dailyMoneyQ8.setVisibility(View.GONE);
            dailyMoneyA8.setVisibility(View.GONE);
            dailyMoneyQ9.setVisibility(View.GONE);
            dailyMoneyA9.setVisibility(View.GONE);
            dailyMoneyQ10.setVisibility(View.GONE);
            dailyMoneyA10.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpInstructions.setVisibility(View.GONE);
            downBudgetButton.setVisibility(View.GONE);
            upBudgetButton.setVisibility(View.VISIBLE);

            budgetQ1.setVisibility(View.VISIBLE);
            budgetQ1.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budgetA1.setVisibility(View.VISIBLE);
                }
            });

            budgetQ2.setVisibility(View.VISIBLE);
            budgetQ2.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    budgetA2.setVisibility(View.VISIBLE);
                    budgetA2A.setVisibility(View.VISIBLE);
                    budgetA2B.setVisibility(View.VISIBLE);
                    budgetA2C.setVisibility(View.VISIBLE);
                    budgetA2D.setVisibility(View.VISIBLE);
                    budgetA2E.setVisibility(View.VISIBLE);
                    budgetA2F.setVisibility(View.VISIBLE);
                    budgetA2G.setVisibility(View.VISIBLE);
                }
            });

            budgetQ3.setVisibility(View.VISIBLE);
            budgetQ3.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    budgetA3.setVisibility(View.VISIBLE);
                    budgetA3A.setVisibility(View.VISIBLE);
                    budgetA3B.setVisibility(View.VISIBLE);
                    budgetA3C.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    View.OnClickListener onClickUpBudget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upBudgetButton.setVisibility(View.GONE);
            downBudgetButton.setVisibility(View.VISIBLE);

            budgetQ1.setVisibility(View.GONE);
            budgetA1.setVisibility(View.GONE);
            budgetQ2.setVisibility(View.GONE);
            budgetA2.setVisibility(View.GONE);
            budgetA2A.setVisibility(View.GONE);
            budgetA2B.setVisibility(View.GONE);
            budgetA2C.setVisibility(View.GONE);
            budgetA2D.setVisibility(View.GONE);
            budgetA2E.setVisibility(View.GONE);
            budgetA2F.setVisibility(View.GONE);
            budgetA2G.setVisibility(View.GONE);
            budgetQ3.setVisibility(View.GONE);
            budgetA3.setVisibility(View.GONE);
            budgetA3A.setVisibility(View.GONE);
            budgetA3B.setVisibility(View.GONE);
            budgetA3C.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownDebts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpInstructions.setVisibility(View.GONE);
            downDebtsButton.setVisibility(View.GONE);
            upDebtsButton.setVisibility(View.VISIBLE);

            debtsQ1.setVisibility(View.VISIBLE);
            debtsQ1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debtsA1.setVisibility(View.VISIBLE);
                }
            });

            debtsQ2.setVisibility(View.VISIBLE);
            debtsQ2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debtsA2.setVisibility(View.VISIBLE);
                }
            });

            debtsQ3.setVisibility(View.VISIBLE);
            debtsQ3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debtsA3.setVisibility(View.VISIBLE);
                }
            });

            debtsQ4.setVisibility(View.VISIBLE);
            debtsQ4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debtsA4.setVisibility(View.VISIBLE);
                }
            });

            debtsQ5.setVisibility(View.VISIBLE);
            debtsQ5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    debtsA5.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    View.OnClickListener onClickUpDebts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upDebtsButton.setVisibility(View.GONE);
            downDebtsButton.setVisibility(View.VISIBLE);

            debtsQ1.setVisibility(View.GONE);
            debtsA1.setVisibility(View.GONE);
            debtsQ2.setVisibility(View.GONE);
            debtsA2.setVisibility(View.GONE);
            debtsQ3.setVisibility(View.GONE);
            debtsA3.setVisibility(View.GONE);
            debtsQ4.setVisibility(View.GONE);
            debtsA4.setVisibility(View.GONE);
            debtsQ5.setVisibility(View.GONE);
            debtsA5.setVisibility(View.GONE);
        }
    };

    View.OnClickListener onClickDownSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            helpInstructions.setVisibility(View.GONE);
            downSavingsButton.setVisibility(View.GONE);
            upSavingsButton.setVisibility(View.VISIBLE);

            savingsQ1.setVisibility(View.VISIBLE);
            savingsQ1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsA1.setVisibility(View.VISIBLE);
                }
            });

            savingsQ2.setVisibility(View.VISIBLE);
            savingsQ2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsA2.setVisibility(View.VISIBLE);
                }
            });

            savingsQ3.setVisibility(View.VISIBLE);
            savingsQ3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsA3.setVisibility(View.VISIBLE);
                }
            });

            savingsQ4.setVisibility(View.VISIBLE);
            savingsQ4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsA4.setVisibility(View.VISIBLE);
                }
            });

            savingsQ5.setVisibility(View.VISIBLE);
            savingsQ5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savingsA5.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    View.OnClickListener onClickUpSavings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upSavingsButton.setVisibility(View.GONE);
            downSavingsButton.setVisibility(View.VISIBLE);

            savingsQ1.setVisibility(View.GONE);
            savingsA1.setVisibility(View.GONE);
            savingsQ2.setVisibility(View.GONE);
            savingsA2.setVisibility(View.GONE);
            savingsQ3.setVisibility(View.GONE);
            savingsA3.setVisibility(View.GONE);
            savingsQ4.setVisibility(View.GONE);
            savingsA4.setVisibility(View.GONE);
            savingsQ5.setVisibility(View.GONE);
            savingsA5.setVisibility(View.GONE);
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
                    String url = getString(R.string.website);

                    web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(url));
                    startActivity(web);
                }
            });

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
