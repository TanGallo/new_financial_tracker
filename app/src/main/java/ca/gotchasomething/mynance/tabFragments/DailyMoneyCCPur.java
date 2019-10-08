/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ca.gotchasomething.mynance.AddDebts;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutCCPurList;
import ca.gotchasomething.mynance.LayoutCreditCardTransactions;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.ExpenseBudgetDb;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.spinners.MoneyOutCCSpinnerAdapter;

public class DailyMoneyCCPur extends Fragment {

    boolean aPoss = true, bPoss = true;
    Button monCC1CreateBtn;
    ContentValues monCC1CV;
    Cursor monCC1Cur;
    DbHelper monCC1Helper, monCC1Helper2;
    DbManager monCC1DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, monCC1MonOutAmt = 0.0, monCC1MonOutNewAmt = 0.0,
            monCC1MonOutOldAmt = 0.0;
    ExpenseBudgetDb monCC1ExpDb;
    General monCC1Gen;
    Intent monCC1Refresh, monCC1ToAddDebts, monCC1ToList;
    ListView monCC1ListView;
    long debtIdExp, monCC1ChargingDebtIdFromSpin, monCC1ExpRefKeyMO;
    MonCC1LstAdapter monCC1LstAdapter;
    MoneyOutDb monCC1MonOutDb;
    MoneyOutCCSpinnerAdapter monCC1CCSpinAdapter;
    Spinner monCC1DebtSpin;
    SQLiteDatabase monCC1Db, monCC1Db2;
    String expCatFromTag = null, expPriorityFromTag = null, expWeeklyFromTag = null, monCC1ChargingDebtNameFromSpin = null, monCC1ExpAmt = null;
    View v;

    public DailyMoneyCCPur() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_3_choose_create_spinner, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monCC1DbMgr = new DbManager(getContext());
        monCC1Gen = new General();

        monCC1DebtSpin = v.findViewById(R.id.frag3ChooseSpin);
        monCC1Helper = new DbHelper(getContext());
        monCC1Db = monCC1Helper.getReadableDatabase();
        monCC1Cur = monCC1Db.rawQuery("SELECT * FROM " + DbHelper.DEBTS_TABLE_NAME + " ORDER BY " + DbHelper.DEBTNAME + " ASC", null);
        monCC1CCSpinAdapter = new MoneyOutCCSpinnerAdapter(getContext(), monCC1Cur);
        monCC1DebtSpin.setAdapter(monCC1CCSpinAdapter);

        monCC1DebtSpin.setOnItemSelectedListener(monCC1DebtSpinSelection);

        monCC1CreateBtn = v.findViewById(R.id.frag3CreateBtn);
        monCC1ListView = v.findViewById(R.id.frag3ListView);

        monCC1CreateBtn.setOnClickListener(onClickMonCC1CreateBtn);

        monCC1LstAdapter = new MonCC1LstAdapter(getContext(), monCC1DbMgr.getExpense());
        monCC1ListView.setAdapter(monCC1LstAdapter);

        monCC1CV = new ContentValues();
        monCC1CV.put(DbHelper.LASTPAGEID, 5);
        monCC1Helper2 = new DbHelper(getContext());
        monCC1Db2 = monCC1Helper2.getWritableDatabase();
        monCC1Db2.update(DbHelper.CURRENT_TABLE_NAME, monCC1CV, DbHelper.ID + "= '1'", null);
        monCC1Db2.close();

    }

    public void monCC1Refresh() {
        monCC1Refresh = new Intent(getContext(), LayoutCreditCardTransactions.class);
        monCC1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monCC1Refresh);
    }

    View.OnClickListener onClickMonCC1CreateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monCC1ToAddDebts = new Intent(getContext(), AddDebts.class);
            monCC1ToAddDebts.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monCC1ToAddDebts);
        }
    };

    AdapterView.OnItemSelectedListener monCC1DebtSpinSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monCC1ChargingDebtNameFromSpin = monCC1Cur.getString(monCC1Cur.getColumnIndexOrThrow(DbHelper.DEBTNAME));
            monCC1ChargingDebtIdFromSpin = monCC1Cur.getLong(monCC1Cur.getColumnIndexOrThrow(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void monCC1ChangeDefault() {
        monCC1ExpDb.setExpenseAmount(monCC1MonOutAmt);
        monCC1DbMgr.updateExpense(monCC1ExpDb);
        monCC1ContTransaction();
    }

    public void monCC1ContTransaction() {

        monCC1DbMgr.updateDebtRecPlusPt1(monCC1MonOutAmt, debtAmtFromDb, monCC1ChargingDebtIdFromSpin);
        for (DebtDb d : monCC1DbMgr.getDebts()) {
            if (String.valueOf(d.getId()).equals(String.valueOf(monCC1ChargingDebtIdFromSpin))) {
                debtAmtFromDb = d.getDebtOwing();
                debtLimitFromDb = d.getDebtLimit();
                debtRateFromDb = d.getDebtRate();
                debtPaytFromDb = d.getDebtPayments();
            }
        }
        monCC1DbMgr.updateDebtRecPt2(monCC1Gen.calcDebtDate(
                debtAmtFromDb,
                debtRateFromDb,
                debtPaytFromDb,
                getString(R.string.debt_paid),
                getString(R.string.too_far)), monCC1ChargingDebtIdFromSpin);

        monCC1MonOutDb = new MoneyOutDb(
                expCatFromTag,
                expPriorityFromTag,
                expWeeklyFromTag,
                monCC1MonOutAmt,
                0.0,
                0.0,
                0.0,
                monCC1ChargingDebtIdFromSpin,
                monCC1ChargingDebtNameFromSpin,
                monCC1Gen.createTimestamp(),
                "Y",
                monCC1ChargingDebtNameFromSpin,
                monCC1ChargingDebtIdFromSpin,
                0,
                0,
                monCC1ExpRefKeyMO,
                0);
        monCC1DbMgr.addMoneyOut(monCC1MonOutDb);

        monCC1DbMgr.makeNewExpAnnAmt(monCC1ExpRefKeyMO, monCC1Gen.last365Days());
        monCC1DbMgr.updateExpense(monCC1ExpDb);

        monCC1LstAdapter.updateExpenses(monCC1DbMgr.getExpense());
        monCC1LstAdapter.notifyDataSetChanged();

        monCC1ToList = new Intent(getContext(), LayoutCCPurList.class);
        monCC1ToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(monCC1ToList);
    }

    public class MonCC1LstAdapter extends ArrayAdapter<ExpenseBudgetDb> {

        private Context context;
        private List<ExpenseBudgetDb> expenses;

        private MonCC1LstAdapter(
                Context context,
                List<ExpenseBudgetDb> expenses) {

            super(context, -1, expenses);

            this.context = context;
            this.expenses = expenses;
        }

        public void updateExpenses(List<ExpenseBudgetDb> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return expenses.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MonCC1ViewHolder monCC1Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                monCC1Hldr = new MonCC1ViewHolder();
                monCC1Hldr.monCC1PaytPayLabel = convertView.findViewById(R.id.paytPayLabel);
                monCC1Hldr.monCC1PaytPayLabel.setText(getString(R.string.spent));
                monCC1Hldr.monCC1PaytAmtTV = convertView.findViewById(R.id.paytAmtTV);
                monCC1Hldr.monCC1PaytCatTV = convertView.findViewById(R.id.paytCatTV);
                monCC1Hldr.monCC1PaytAmtET = convertView.findViewById(R.id.paytAmtET);
                monCC1Hldr.monCC1PaytSaveBtn = convertView.findViewById(R.id.paytSaveBtn);
                monCC1Hldr.monCC1PaytWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                monCC1Hldr.monCC1PaytWarnTV = convertView.findViewById(R.id.paytWarnTV);
                monCC1Hldr.monCC1PaytYesContBtn = convertView.findViewById(R.id.paytYesContBtn);
                monCC1Hldr.monCC1PaytNoContBtn = convertView.findViewById(R.id.paytNoContBtn);
                monCC1Hldr.monCC1PaytDefLayout = convertView.findViewById(R.id.paytDefLayout);
                monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                monCC1Hldr.monCC1PaytYesDefBtn = convertView.findViewById(R.id.paytYesDefBtn);
                monCC1Hldr.monCC1PaytNoDefBtn = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(monCC1Hldr);

            } else {
                monCC1Hldr = (MonCC1ViewHolder) convertView.getTag();
            }

            monCC1ExpAmt = String.valueOf(expenses.get(position).getExpenseAmount());
            monCC1Gen.dblASCurrency(monCC1ExpAmt, monCC1Hldr.monCC1PaytAmtTV);
            monCC1Hldr.monCC1PaytCatTV.setText(expenses.get(position).getExpenseName());

            monCC1ExpRefKeyMO = expenses.get(position).getId();

            monCC1Hldr.monCC1PaytSaveBtn.setTag(expenses.get(position));

            monCC1Hldr.monCC1PaytSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monCC1ExpDb = (ExpenseBudgetDb) monCC1Hldr.monCC1PaytSaveBtn.getTag();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    monCC1DbMgr = new DbManager(getContext());

                    monCC1MonOutOldAmt = expenses.get(position).getExpenseAmount();
                    monCC1MonOutNewAmt = monCC1Gen.dblFromET(monCC1Hldr.monCC1PaytAmtET);

                    if (monCC1MonOutNewAmt == 0) {
                        monCC1MonOutAmt = monCC1MonOutOldAmt;
                    } else {
                        monCC1MonOutAmt = monCC1MonOutNewAmt;
                    }

                    expPriorityFromTag = monCC1ExpDb.getExpensePriority();
                    expWeeklyFromTag = monCC1ExpDb.getExpenseWeekly();

                    for (DebtDb d : monCC1DbMgr.getDebts()) {
                        if (String.valueOf(d.getId()).equals(String.valueOf(monCC1ChargingDebtIdFromSpin))) {
                            debtAmtFromDb = d.getDebtOwing();
                            debtLimitFromDb = d.getDebtLimit();
                            debtRateFromDb = d.getDebtRate();
                            debtPaytFromDb = d.getDebtPayments();
                        }
                    }

                    if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) { //CARD IS OVER LIMIT
                        //SHOW WARNING
                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                        monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));
                        //NO CONTINUE
                        monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                monCC1Refresh();
                            }
                        });
                        //YES CONTINUE
                        monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                if (expPriorityFromTag.equals("A")) {  //PRIORITY IS A
                                    if(monCC1DbMgr.retrieveCurrentAccountBalance() - monCC1MonOutAmt < 0) { //A NOT POSSIBLE
                                    //monCC1DbMgr.checkIfAPoss(monCC1MonOutAmt);
                                    //if (!aPoss) { //A NOT POSSIBLE
                                        //SHOW WARNING
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                        monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_A));
                                        //NO CONTINUE
                                        monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                monCC1Refresh();
                                            }
                                        });
                                        //YES CONTINUE
                                        monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    monCC1ContTransaction();
                                                }
                                            }
                                        });
                                    } else { //A POSSIBLE
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            monCC1ContTransaction();
                                        }
                                    }
                                } else if (expPriorityFromTag.equals("B")) { //PRIORITY IS B
                                    if (!bPoss) { //B NOT POSSIBLE
                                        //SHOW WARNING
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                        monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_B));
                                        //NO CONTINUE
                                        monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                monCC1Refresh();
                                            }
                                        });
                                        //YES CONTINUE
                                        monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                                    //SHOW OPTION TO CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                                    //NO CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ContTransaction();
                                                        }
                                                    });
                                                    //YES CHANGE DEFAULT
                                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ChangeDefault();
                                                        }
                                                    });
                                                } else { //MONEY IN = DEFAULT AMT
                                                    monCC1ContTransaction();
                                                }
                                            }
                                        });
                                    } else { //B POSSIBLE
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            monCC1ContTransaction();
                                        }
                                    }
                                }
                            }
                        });
                    } else { //CARD NOT OVER LIMIT
                        if (expPriorityFromTag.equals("A")) { //PRIORITY IS A
                            if(monCC1DbMgr.retrieveCurrentAccountBalance() - monCC1MonOutAmt < 0) { //A NOT POSSIBLE
                            //monCC1DbMgr.checkIfAPoss(monCC1MonOutAmt);
                            //if (!aPoss) { //A NOT POSSIBLE
                                //SHOW WARNING
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_A));
                                //NO CONTINUE
                                monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        monCC1Refresh();
                                    }
                                });
                                //YES CONTINUE
                                monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            monCC1ContTransaction();
                                        }
                                    }
                                });
                            } else { //A POSSIBLE
                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                    //SHOW OPTION TO CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                    //NO CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ContTransaction();
                                        }
                                    });
                                    //YES CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ChangeDefault();
                                        }
                                    });
                                } else { //MONEY IN = DEFAULT AMT
                                    monCC1ContTransaction();
                                }
                            }
                        } else if (expPriorityFromTag.equals("B")) { //PRIORITY IS B
                            if (!bPoss) { //B NOT POSSIBLE
                                //SHOW WARNING
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_B));
                                //NO CONTINUE
                                monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        monCC1Refresh();
                                    }
                                });
                                //YES CONTINUE
                                monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                            //SHOW OPTION TO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                            //NO CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });
                                            //YES CHANGE DEFAULT
                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else { //MONEY IN = DEFAULT AMT
                                            monCC1ContTransaction();
                                        }
                                    }
                                });
                            } else { //B POSSIBLE
                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) { //MONEY IN = NEW AMT
                                    //SHOW OPTION TO CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);
                                    //NO CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ContTransaction();
                                        }
                                    });
                                    //YES CHANGE DEFAULT
                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ChangeDefault();
                                        }
                                    });
                                } else { //MONEY IN = DEFAULT AMT
                                    monCC1ContTransaction();
                                }
                            }
                        }
                    }*/

                    /*if (expPriorityFromTag.equals("A")) {
                        monCC1DbMgr.checkIfAPoss(monCC1MonOutAmt);
                        if (!aPoss) {
                            monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                            monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_A));

                            monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                    monCC1Refresh();
                                }
                            });

                            monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                    if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                        monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                        monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                monCC1Refresh();
                                            }
                                        });
                                        monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ContTransaction();
                                                        }
                                                    });

                                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ChangeDefault();
                                                        }
                                                    });
                                                } else {
                                                    monCC1ContTransaction();
                                                }
                                            }
                                        });
                                    } else {
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });

                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else {
                                            monCC1ContTransaction();
                                        }
                                    }
                                }
                            });
                        } else {
                            if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) {
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        monCC1Refresh();
                                    }
                                });
                                monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });

                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else {
                                            monCC1ContTransaction();
                                        }
                                    }
                                });
                            } else {
                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ContTransaction();
                                        }
                                    });

                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ChangeDefault();
                                        }
                                    });
                                } else {
                                    monCC1ContTransaction();
                                }
                            }
                        }
                    } else if (expPriorityFromTag.equals("B")) {
                        monCC1DbMgr.checkIfBPoss(monCC1MonOutAmt);
                        if (!bPoss) {
                            monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                            monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.payment_not_possible_B));
                            monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                    monCC1Refresh();
                                }
                            });
                            monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                    if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                        monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                        monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                                monCC1Refresh();
                                            }
                                        });
                                        monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ContTransaction();
                                                        }
                                                    });

                                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                            monCC1ChangeDefault();
                                                        }
                                                    });
                                                } else {
                                                    monCC1ContTransaction();
                                                }
                                            }
                                        });
                                    } else {
                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });

                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else {
                                            monCC1ContTransaction();
                                        }
                                    }
                                }
                            });
                        } else {
                            if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) {
                                monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                                monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));

                                monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                        monCC1Refresh();
                                    }
                                });
                                monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);

                                        if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                            monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ContTransaction();
                                                }
                                            });

                                            monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                                    monCC1ChangeDefault();
                                                }
                                            });
                                        } else {
                                            monCC1ContTransaction();
                                        }
                                    }
                                });
                            } else {
                                if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                                    monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                                    monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ContTransaction();
                                        }
                                    });

                                    monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                            monCC1ChangeDefault();
                                        }
                                    });
                                } else {
                                    monCC1ContTransaction();
                                }
                            }
                        }
                    }/*

                    /*for (DebtDb d : monCC1DbMgr.getDebts()) {
                        if (String.valueOf(d.getId()).equals(String.valueOf(monCC1ChargingDebtIdFromSpin))) {
                            debtAmtFromDb = d.getDebtOwing();
                            debtLimitFromDb = d.getDebtLimit();
                            debtRateFromDb = d.getDebtRate();
                            debtPaytFromDb = d.getDebtPayments();
                        }
                    }*/
                    /*if (debtLimitFromDb > 0) {
                        if (debtAmtFromDb + monCC1MonOutAmt > debtLimitFromDb) {
                            monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.VISIBLE);
                            monCC1Hldr.monCC1PaytWarnTV.setText(getString(R.string.not_enough_credit_warning));

                            monCC1Hldr.monCC1PaytNoContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                    monCC1Refresh = new Intent(getContext(), DailyMoneyCCPur.class);
                                    monCC1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    startActivity(monCC1Refresh);
                                }
                            });
                            monCC1Hldr.monCC1PaytYesContBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    monCC1Hldr.monCC1PaytWarnLayout.setVisibility(View.GONE);
                                }
                            });
                        }
                    }*/
                    /*monCC1DbMgr.updateDebtRecPlusPt1(monCC1MonOutAmt, debtAmtFromDb, monCC1ChargingDebtIdFromSpin);
                    for (DebtDb d : monCC1DbMgr.getDebts()) {
                        if (String.valueOf(d.getId()).equals(String.valueOf(monCC1ChargingDebtIdFromSpin))) {
                            debtAmtFromDb = d.getDebtOwing();
                            debtLimitFromDb = d.getDebtLimit();
                            debtRateFromDb = d.getDebtRate();
                            debtPaytFromDb = d.getDebtPayments();
                        }
                    }
                    monCC1DbMgr.updateDebtRecPt2(monCC1Gen.calcDebtDate(
                            debtAmtFromDb,
                            debtRateFromDb,
                            debtPaytFromDb,
                            getString(R.string.debt_paid),
                            getString(R.string.too_far)), monCC1ChargingDebtIdFromSpin);*/

                    /*if (monCC1MonOutAmt == monCC1MonOutNewAmt) {
                        monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.VISIBLE);

                        monCC1Hldr.monCC1PaytNoDefBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                            }
                        });

                        monCC1Hldr.monCC1PaytYesDefBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monCC1Hldr.monCC1PaytDefLayout.setVisibility(View.GONE);
                                monCC1ChangeDefault();
                            }
                        });
                    }*/

                    /*monCC1MonOutDb = new MoneyOutDb(
                            expCatFromTag,
                            expPriorityFromTag,
                            expWeeklyFromTag,
                            monCC1MonOutAmt,
                            0.0,
                            0.0,
                            0.0,
                            monCC1Gen.createTimestamp(),
                            "Y",
                            monCC1ChargingDebtNameFromSpin,
                            monCC1ChargingDebtIdFromSpin,
                            0,
                            0,
                            monCC1ExpRefKeyMO,
                            0);
                    monCC1DbMgr.addMoneyOut(monCC1MonOutDb);

                    monCC1DbMgr.makeNewExpAnnAmt(monCC1ExpRefKeyMO);

                    monCC1ToList = new Intent(getContext(), LayoutCCPurList.class);
                    monCC1ToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(monCC1ToList);*/
                /*}
            });
            return convertView;
        }
    }

    private static class MonCC1ViewHolder {
        public TextView monCC1PaytPayLabel;
        public TextView monCC1PaytAmtTV;
        public TextView monCC1PaytOnLabel;
        public TextView monCC1PaytCatTV;
        public EditText monCC1PaytAmtET;
        public ImageButton monCC1PaytSaveBtn;
        public LinearLayout monCC1PaytWarnLayout;
        public TextView monCC1PaytWarnTV;
        public Button monCC1PaytYesContBtn;
        public Button monCC1PaytNoContBtn;
        public LinearLayout monCC1PaytDefLayout;
        public Button monCC1PaytYesDefBtn;
        public Button monCC1PaytNoDefBtn;
    }
}*/
