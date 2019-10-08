/*package ca.gotchasomething.mynance.tabFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.MainActivity;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.TransfersDb;
import ca.gotchasomething.mynance.spinners.TransferSpinnerAdapter;

public class DailyTransfers extends Fragment {

    Button trn1Btn, trn1doneBtn, trn1NoBtn, trn1YesBtn;
    Cursor trn1Cur, trn1Cur2;
    DbHelper trn1Helper, trn1Helper2;
    DbManager trn1DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0,
            moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, newMoneyA = 0.0, newMoneyOwing = 0.0, newMoneyB = 0.0, savAmtFromDb = 0.0,
            savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0, trn1TrnAmt = 0.0;
    EditText trn1trnAmtET;
    General trn1Gen;
    Intent trn1Refresh, trn1ToMain;
    LinearLayout trn1WarnLayout;
    long trn1FromSpinDebtId, trn1FromSpinSavId, trn1ToSpinDebtId, trn1ToSpinSavId;
    Spinner trn1FromSpin, trn1ToSpin;
    SQLiteDatabase trn1Db, trn1Db2;
    String trn1FromSpinName = null, trn1ToSpinName = null;
    TextView trn1WarnTV;
    TransfersDb trn1TransDb;
    TransferSpinnerAdapter trn1FromSpinAdapter, trn1ToSpinAdapter;
    View v;

    public DailyTransfers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.d3_frag_transfers, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trn1Gen = new General();
        trn1DbMgr = new DbManager(getContext());

        trn1FromSpin = v.findViewById(R.id.transferFromSpin);
        trn1ToSpin = v.findViewById(R.id.transferToSpin);
        trn1trnAmtET = v.findViewById(R.id.transferAmtET);
        trn1Btn = v.findViewById(R.id.transferBtn);
        trn1doneBtn = v.findViewById(R.id.transfersDoneBtn);
        trn1WarnLayout = v.findViewById(R.id.transferWarnLayout);
        trn1WarnLayout.setVisibility(View.GONE);
        trn1WarnTV = v.findViewById(R.id.transferWarnTV);
        trn1NoBtn = v.findViewById(R.id.transferNoBtn);
        trn1YesBtn = v.findViewById(R.id.transferYesBtn);

        trn1Btn.setOnClickListener(onClickTransferBtn);
        trn1doneBtn.setOnClickListener(onClickTrn1DoneBtn);

        trn1Helper = new DbHelper(getContext());
        trn1Db = trn1Helper.getReadableDatabase();
        trn1Cur = trn1Db.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ACCTNAME + " ASC", null);
        trn1FromSpinAdapter = new TransferSpinnerAdapter(getContext(), trn1Cur);
        trn1FromSpin.setAdapter(trn1FromSpinAdapter);

        trn1FromSpin.setOnItemSelectedListener(trn1FromSpinSel);

        trn1Helper2 = new DbHelper(getContext());
        trn1Db2 = trn1Helper2.getReadableDatabase();
        trn1Cur2 = trn1Db2.rawQuery("SELECT * FROM " + DbHelper.ACCOUNTS_TABLE_NAME + " ORDER BY " + DbHelper.ACCTNAME + " ASC", null);
        trn1ToSpinAdapter = new TransferSpinnerAdapter(getContext(), trn1Cur2);
        trn1ToSpin.setAdapter(trn1ToSpinAdapter);

        trn1ToSpin.setOnItemSelectedListener(trn1ToSpinSel);

    }

    AdapterView.OnItemSelectedListener trn1FromSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn1FromSpinName = trn1Cur.getString(trn1Cur.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            trn1FromSpinDebtId = trn1Cur.getLong(trn1Cur.getColumnIndexOrThrow(DbHelper.DEBTID));
            trn1FromSpinSavId = trn1Cur.getLong(trn1Cur.getColumnIndexOrThrow(DbHelper.SAVID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener trn1ToSpinSel = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            trn1ToSpinName = trn1Cur2.getString(trn1Cur2.getColumnIndexOrThrow(DbHelper.ACCTNAME));
            trn1ToSpinDebtId = trn1Cur2.getLong(trn1Cur2.getColumnIndexOrThrow(DbHelper.DEBTID));
            trn1ToSpinSavId = trn1Cur2.getLong(trn1Cur2.getColumnIndexOrThrow(DbHelper.SAVID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickTrn1DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn1ToMain = new Intent(getContext(), MainActivity.class);
            trn1ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn1ToMain);
        }
    };

    View.OnClickListener onClickTransferBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            trn1TrnAmt = trn1Gen.dblFromET(trn1trnAmtET);

            if (trn1FromSpinDebtId != 0) {
                for (DebtDb d : trn1DbMgr.getDebts()) {
                    if (String.valueOf(d.getId()).equals(trn1FromSpinDebtId)) {
                        debtAmtFromDb = d.getDebtOwing();
                        debtLimitFromDb = d.getDebtLimit();
                        debtRateFromDb = d.getDebtRate();
                        debtPaytFromDb = d.getDebtPayments();
                    }
                }
                if (debtLimitFromDb > 0) {
                    if (debtAmtFromDb + trn1TrnAmt > debtLimitFromDb) {
                        trn1WarnLayout.setVisibility(View.VISIBLE);
                        trn1WarnTV.setText(getString(R.string.not_enough_credit_warning));
                        trn1NoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                trn1WarnLayout.setVisibility(View.GONE);
                                trn1Refresh = new Intent(getContext(), DailyTransfers.class);
                                trn1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                startActivity(trn1Refresh);
                            }
                        });

                        trn1YesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                trn1WarnLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                trn1DbMgr.updateDebtRecPlusPt1(trn1TrnAmt, debtAmtFromDb, trn1FromSpinDebtId);
                for (DebtDb d : trn1DbMgr.getDebts()) {
                    if (String.valueOf(d.getId()).equals(trn1FromSpinDebtId)) {
                        debtAmtFromDb = d.getDebtOwing();
                        debtLimitFromDb = d.getDebtLimit();
                        debtRateFromDb = d.getDebtRate();
                        debtPaytFromDb = d.getDebtPayments();
                    }
                }
                trn1DbMgr.updateDebtRecPt2(trn1Gen.calcDebtDate(
                        debtAmtFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn1FromSpinDebtId);
            } else if (trn1FromSpinSavId != 0) {
                for (SavingsDb s : trn1DbMgr.getSavings()) {
                    if (String.valueOf(s.getId()).equals(trn1FromSpinSavId)) {
                        savAmtFromDb = s.getSavingsAmount();
                        savGoalFromDb = s.getSavingsGoal();
                        savRateFromDb = s.getSavingsRate();
                        savPaytFromDb = s.getSavingsPayments();
                    }
                }
                if (savAmtFromDb - trn1TrnAmt < 0) {
                    trn1WarnLayout.setVisibility(View.VISIBLE);
                    trn1WarnTV.setText(getString(R.string.not_enough_savings_warning));
                    trn1NoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1WarnLayout.setVisibility(View.GONE);
                            trn1Refresh = new Intent(getContext(), DailyTransfers.class);
                            trn1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(trn1Refresh);
                        }
                    });
                    trn1YesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1WarnLayout.setVisibility(View.GONE);
                        }
                    });
                }
                trn1DbMgr.updateSavRecMinusPt1(trn1TrnAmt, savAmtFromDb, trn1FromSpinSavId);
                for (SavingsDb s : trn1DbMgr.getSavings()) {
                    if (String.valueOf(s.getId()).equals(trn1FromSpinSavId)) {
                        savAmtFromDb = s.getSavingsAmount();
                        savGoalFromDb = s.getSavingsGoal();
                        savRateFromDb = s.getSavingsRate();
                        savPaytFromDb = s.getSavingsPayments();
                    }
                }
                trn1DbMgr.updateSavRecPt2(trn1Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn1FromSpinSavId);
            } else {
                if(trn1DbMgr.retrieveCurrentAccountBalance() - trn1TrnAmt < 0) {
                    trn1WarnLayout.setVisibility(View.VISIBLE);
                    trn1WarnTV.setText(getString(R.string.payment_not_possible_A));
                    trn1NoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1WarnLayout.setVisibility(View.GONE);
                            trn1Refresh = new Intent(getContext(), DailyTransfers.class);
                            trn1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(trn1Refresh);
                        }
                    });
                    trn1YesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn1WarnLayout.setVisibility(View.GONE);
                        }
                    });
                }
                trn1DbMgr.updateTotAcctBalMinus(trn1TrnAmt);
                trn1DbMgr.detAandBPortionsExp(trn1TrnAmt, "A");
                trn1DbMgr.updateAvailBalMinus(moneyOutA, moneyOutOwing, moneyOutB);
                if (trn1DbMgr.retrieveCurrentOwingA() < 0) {
                    trn1DbMgr.adjustAandBPortions(moneyOutA, moneyOutOwing, moneyOutB);
                    moneyOutA = newMoneyA;
                    moneyOutOwing = newMoneyOwing;
                    moneyOutB = newMoneyB;
                }
            }

            if (trn1ToSpinDebtId != 0) {
                for (DebtDb d : trn1DbMgr.getDebts()) {
                    if (String.valueOf(d.getId()).equals(trn1ToSpinDebtId)) {
                        debtAmtFromDb = d.getDebtOwing();
                        debtLimitFromDb = d.getDebtLimit();
                        debtRateFromDb = d.getDebtRate();
                        debtPaytFromDb = d.getDebtPayments();
                    }
                }
                trn1DbMgr.updateDebtRecMinusPt1(trn1TrnAmt, debtAmtFromDb, trn1ToSpinDebtId);
                for (DebtDb d : trn1DbMgr.getDebts()) {
                    if (String.valueOf(d.getId()).equals(trn1ToSpinDebtId)) {
                        debtAmtFromDb = d.getDebtOwing();
                        debtLimitFromDb = d.getDebtLimit();
                        debtRateFromDb = d.getDebtRate();
                        debtPaytFromDb = d.getDebtPayments();
                    }
                }
                trn1DbMgr.updateDebtRecPt2(trn1Gen.calcDebtDate(
                        debtAmtFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn1ToSpinDebtId);
            } else if (trn1ToSpinSavId != 0) {
                for (SavingsDb s : trn1DbMgr.getSavings()) {
                    if (String.valueOf(s.getId()).equals(trn1ToSpinSavId)) {
                        savAmtFromDb = s.getSavingsAmount();
                        savGoalFromDb = s.getSavingsGoal();
                        savRateFromDb = s.getSavingsRate();
                        savPaytFromDb = s.getSavingsPayments();
                    }
                }
                trn1DbMgr.updateSavRecPlusPt1(trn1TrnAmt, savAmtFromDb, trn1ToSpinSavId);
                for (SavingsDb s : trn1DbMgr.getSavings()) {
                    if (String.valueOf(s.getId()).equals(trn1ToSpinSavId)) {
                        savAmtFromDb = s.getSavingsAmount();
                        savGoalFromDb = s.getSavingsGoal();
                        savRateFromDb = s.getSavingsRate();
                        savPaytFromDb = s.getSavingsPayments();
                    }
                }
                trn1DbMgr.updateSavRecPt2(trn1Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn1ToSpinSavId);
            } else {
                trn1DbMgr.updateTotAcctBalPlus(trn1TrnAmt);
                trn1DbMgr.detAandBPortionsInc(trn1TrnAmt);
                trn1DbMgr.updateAvailBalPlus(moneyInA, moneyInOwing, moneyInB);
                if (trn1DbMgr.retrieveCurrentOwingA() < 0) {
                    trn1DbMgr.adjustAandBPortions(moneyInA, moneyInOwing, moneyInB);
                    moneyInA = newMoneyA;
                    moneyInOwing = newMoneyOwing;
                    moneyInB = newMoneyB;
                }
            }

            trn1TransDb = new TransfersDb(
                    trn1FromSpinName,
                    trn1ToSpinName,
                    trn1FromSpinDebtId,
                    trn1FromSpinSavId,
                    trn1ToSpinDebtId,
                    trn1ToSpinSavId,
                    trn1TrnAmt,
                    trn1Gen.createTimestamp(),
                    0);
            trn1DbMgr.addTransfers(trn1TransDb);

            trn1DbMgr.updateAllSavBudget();
            trn1DbMgr.updateAllDebtBudget();

            Toast.makeText(getContext(), R.string.transfer_complete, Toast.LENGTH_LONG).show();

            trn1Refresh = new Intent(getContext(), DailyTransfers.class);
            trn1Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn1Refresh);
        }
    };
}*/
