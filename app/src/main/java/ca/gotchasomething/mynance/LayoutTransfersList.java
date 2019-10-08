package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.SavingsDb;
import ca.gotchasomething.mynance.data.TransfersDb;

public class LayoutTransfersList extends AppCompatActivity {

    Button trn2AddMoreBtn, trn2DoneBtn;
    DbManager trn2DbMgr;
    Double debtAmtFromDb = 0.0, debtLimitFromDb = 0.0, debtOwingFromDb = 0.0, debtPaytFromDb = 0.0, debtRateFromDb = 0.0, moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0,
            moneyOutA = 0.0, moneyOutOwing = 0.0, moneyOutB = 0.0, savAmtFromDb = 0.0, savGoalFromDb = 0.0, savPaytFromDb = 0.0, savRateFromDb = 0.0, trn2Amt = 0.0,
            trn2TrnAmtDiff = 0.0, trn2AmtEntry = 0.0, trn2TrnAmtInA = 0.0, trn2TrnAmtInOwing = 0.0, trn2TrnAmtInB = 0.0, trn2TrnAmtOutA = 0.0, trn2TrnAmtOutB = 0.0,
            trn2TrnAmtOutOwing = 0.0, trn2MoneyInA = 0.0, trn2MoneyInAPercent = 0.0, trn2MoneyInB = 0.0, trn2MoneyInOwing = 0.0,
            trn2MoneyOutAPercent = 0.0, trn2NewMoneyInA = 0.0, trn2NewMoneyInB = 0.0, trn2NewMoneyInOwing = 0.0, trn2NewMoneyOutA = 0.0,
            trn2NewMoneyOutOwing = 0.0, trn2NewMoneyOutB = 0.0, trn2MoneyOutA = 0.0, trn2MoneyOutOwing = 0.0, trn2MoneyOutB = 0.0;
    General trn2Gen;
    Intent trn2ToMain, trn2ToAddMonIn, trn2Refresh;
    ListView trn2List;
    long trn2FromDebtId, trn2FromSavId, trn2ToDebtId, trn2ToSavId;
    String trn2MoneyOutPriority = null;
    TransfersDb trn2TransDb;
    Trn2Adapter trn2Adapter;
    TextView trn2Title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2_list_add_done);

        trn2DbMgr = new DbManager(this);
        trn2Gen = new General();

        trn2Title = findViewById(R.id.layout1HeaderLabelTV);
        trn2Title.setText(getString(R.string.transfers));

        trn2AddMoreBtn = findViewById(R.id.layout1AddMoreBtn);
        trn2AddMoreBtn.setText(getString(R.string.record_transfers));
        trn2AddMoreBtn.setOnClickListener(onClickTrn2AddMoreBtn);

        trn2DoneBtn = findViewById(R.id.layout1DoneBtn);
        trn2DoneBtn.setOnClickListener(onClickTrn2DoneBtn);

        trn2List = findViewById(R.id.layout1ListView);
        trn2Adapter = new Trn2Adapter(this, trn2DbMgr.getTransfers());
        trn2List.setAdapter(trn2Adapter);
    }

    public void trn2Refresh() {
        trn2Refresh = new Intent(LayoutTransfersList.this, LayoutTransfersList.class);
        trn2Refresh.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(trn2Refresh);
    }

    View.OnClickListener onClickTrn2DoneBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToMain = new Intent(LayoutTransfersList.this, MainActivity.class);
            trn2ToMain.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToMain);
        }
    };

    View.OnClickListener onClickTrn2AddMoreBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            trn2ToAddMonIn = new Intent(LayoutTransfersList.this, LayoutTransfers.class);
            trn2ToAddMonIn.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(trn2ToAddMonIn);
        }
    };

    public void revAndAdjTransIn() {
        trn2DbMgr.updateTotAcctBalPlus(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentAccountBalance());
        trn2DbMgr.updateAvailBalMinus(trn2TrnAmtInA, trn2TrnAmtInOwing, trn2TrnAmtInB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        trn2MoneyInA = trn2MoneyInAPercent * trn2AmtEntry;
        trn2MoneyInOwing = trn2DbMgr.detOwingPortionInc(trn2AmtEntry, trn2MoneyInA, trn2DbMgr.retrieveCurrentOwingA());
        trn2MoneyInB = trn2AmtEntry - trn2MoneyInA;

        trn2DbMgr.updateAvailBalPlus(trn2MoneyInA, trn2MoneyInOwing, trn2MoneyInB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
            trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());

            trn2NewMoneyInA = trn2DbMgr.detNewAPortion(trn2MoneyInA, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyInOwing = trn2DbMgr.detNewOwingPortion(trn2MoneyInOwing, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyInB = trn2DbMgr.detNewBPortion(trn2MoneyInB, trn2DbMgr.retrieveCurrentOwingA());

            moneyInA = trn2NewMoneyInA;
            moneyInOwing = trn2NewMoneyInOwing;
            moneyInB = trn2NewMoneyInB;
        } else {
            moneyInA = trn2MoneyInA;
            moneyInOwing = trn2MoneyInOwing;
            moneyInB = trn2MoneyInB;
        }

        trn2TransDb.setTransAmtInA(moneyInA);
        trn2TransDb.setTransAmtInOwing(moneyInOwing);
        trn2TransDb.setTransAmtInB(moneyInB);
        trn2DbMgr.updateTransfers(trn2TransDb);
    }

    public void revAndAdjTransOut() {
        trn2DbMgr.updateTotAcctBalMinus(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentAccountBalance());
        trn2DbMgr.updateAvailBalPlus(trn2TrnAmtOutA, trn2TrnAmtOutOwing, trn2TrnAmtOutB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        if (trn2MoneyOutAPercent > 0) {
            trn2MoneyOutPriority = "A";
        } else {
            trn2MoneyOutPriority = "B";
        }

        trn2MoneyOutA = trn2MoneyOutAPercent * trn2AmtEntry;
        trn2MoneyOutOwing = trn2DbMgr.detOwingPortionExp(trn2AmtEntry, trn2MoneyOutPriority, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
        trn2MoneyOutB = trn2AmtEntry - trn2MoneyOutA;

        trn2DbMgr.updateAvailBalMinus(trn2MoneyOutA, trn2MoneyOutOwing, trn2MoneyOutB, trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

        if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
            trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());

            trn2NewMoneyOutA = trn2DbMgr.detNewAPortion(trn2MoneyOutA, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyOutOwing = trn2DbMgr.detNewOwingPortion(trn2MoneyOutOwing, trn2DbMgr.retrieveCurrentOwingA());
            trn2NewMoneyOutB = trn2DbMgr.detNewBPortion(trn2MoneyOutB, trn2DbMgr.retrieveCurrentOwingA());

            moneyOutA = trn2NewMoneyOutA;
            moneyOutOwing = trn2NewMoneyOutOwing;
            moneyOutB = trn2NewMoneyOutB;
        } else {
            moneyOutA = trn2MoneyOutA;
            moneyOutOwing = trn2MoneyOutOwing;
            moneyOutB = trn2MoneyOutB;
        }

        trn2TransDb.setTransAmtOutA(moneyOutA);
        trn2TransDb.setTransAmtOutOwing(moneyOutOwing);
        trn2TransDb.setTransAmtOutB(moneyOutB);
        trn2DbMgr.updateTransfers(trn2TransDb);
    }

    public void trn2DebtPlus() {
        trn2DbMgr.updateDebtRecPlusPt1(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentDebtAmtOwing(trn2FromDebtId), trn2FromDebtId);
        for (DebtDb d : trn2DbMgr.getDebts()) {
            if (d.getId() == trn2FromDebtId) {
                debtOwingFromDb = d.getDebtOwing();
                debtRateFromDb = d.getDebtRate();
                debtPaytFromDb = d.getDebtPayments();
            }
        }
        trn2DbMgr.updateDebtRecPt2(
                trn2Gen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn2FromDebtId);

    }

    public void trn2DebtMinus() {
        trn2DbMgr.updateDebtRecMinusPt1(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentDebtAmtOwing(trn2ToDebtId), trn2ToDebtId);
        for (DebtDb d : trn2DbMgr.getDebts()) {
            if (d.getId() == trn2ToDebtId) {
                debtOwingFromDb = d.getDebtOwing();
                debtRateFromDb = d.getDebtRate();
                debtPaytFromDb = d.getDebtPayments();
            }
        }
        trn2DbMgr.updateDebtRecPt2(
                trn2Gen.calcDebtDate(
                        debtOwingFromDb,
                        debtRateFromDb,
                        debtPaytFromDb,
                        getString(R.string.debt_paid),
                        getString(R.string.too_far)), trn2ToDebtId);
    }

    public void trn2SavMinus() {
        trn2DbMgr.updateSavRecMinusPt1(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentSavAmt(trn2FromSavId), trn2FromSavId);
        for (SavingsDb s : trn2DbMgr.getSavings()) {
            if (s.getId() == trn2FromSavId) {
                savGoalFromDb = s.getSavingsGoal();
                savAmtFromDb = s.getSavingsAmount();
                savRateFromDb = s.getSavingsRate();
                savPaytFromDb = s.getSavingsPayments();
            }
        }
        trn2DbMgr.updateSavRecPt2(
                trn2Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn2FromSavId);
    }

    public void trn2SavPlus() {
        trn2DbMgr.updateSavRecPlusPt1(trn2TrnAmtDiff, trn2DbMgr.retrieveCurrentSavAmt(trn2ToSavId), trn2ToSavId);
        for (SavingsDb s : trn2DbMgr.getSavings()) {
            if (s.getId() == trn2ToSavId) {
                savGoalFromDb = s.getSavingsGoal();
                savAmtFromDb = s.getSavingsAmount();
                savRateFromDb = s.getSavingsRate();
                savPaytFromDb = s.getSavingsPayments();
            }
        }
        trn2DbMgr.updateSavRecPt2(
                trn2Gen.calcSavingsDate(
                        savGoalFromDb,
                        savAmtFromDb,
                        savRateFromDb,
                        savPaytFromDb,
                        getString(R.string.goal_achieved),
                        getString(R.string.too_far)), trn2ToSavId);
    }

    public class Trn2Adapter extends ArrayAdapter<TransfersDb> {

        private Context context;
        private List<TransfersDb> transfers;

        private Trn2Adapter(
                Context context,
                List<TransfersDb> transfers) {

            super(context, -1, transfers);

            this.context = context;
            this.transfers = transfers;
        }

        public void updateTransfers(List<TransfersDb> transfers) {
            this.transfers = transfers;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return transfers.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final Trans2ViewHolder trn2Hldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_8_5tv,
                        parent, false);

                trn2Hldr = new Trans2ViewHolder();
                trn2Hldr.trn2DateTV = convertView.findViewById(R.id.list8TV1);
                trn2Hldr.trn2AmtTV = convertView.findViewById(R.id.list8TV2);
                trn2Hldr.trn2FromNameTV = convertView.findViewById(R.id.list8TV4);
                trn2Hldr.trn2ToNameTV = convertView.findViewById(R.id.list8TV6);
                trn2Hldr.trn2EditBtn = convertView.findViewById(R.id.list8EditBtn);
                trn2Hldr.trn2DelBtn = convertView.findViewById(R.id.list8DelBtn);
                trn2Hldr.trn2UpdateLayout = convertView.findViewById(R.id.list8UpdateLayout);
                trn2Hldr.trn2UpdateLayout.setVisibility(View.GONE);
                trn2Hldr.trn2AmtET = convertView.findViewById(R.id.list8AmtET);
                trn2Hldr.trn2UpdateBtn = convertView.findViewById(R.id.list8UpdateBtn);
                trn2Hldr.trn2CancelBtn = convertView.findViewById(R.id.list8CancelBtn);
                trn2Hldr.trn2WarnLayout = convertView.findViewById(R.id.list8WarnLayout);
                trn2Hldr.trn2WarnLayout.setVisibility(View.GONE);
                trn2Hldr.trn2WarnTV = convertView.findViewById(R.id.list8WarnTV);
                trn2Hldr.trn2YesBtn = convertView.findViewById(R.id.list8YesBtn);
                trn2Hldr.trn2NoBtn = convertView.findViewById(R.id.list8NoBtn);
                convertView.setTag(trn2Hldr);

            } else {
                trn2Hldr = (Trans2ViewHolder) convertView.getTag();
            }

            if (trn2DbMgr.retrieveLastPageId() == 10) {
                trn2Hldr.trn2EditBtn.setVisibility(View.VISIBLE);
                trn2Hldr.trn2DelBtn.setVisibility(View.VISIBLE);
            } else {
                trn2Hldr.trn2EditBtn.setVisibility(View.GONE);
                trn2Hldr.trn2DelBtn.setVisibility(View.GONE);
            }

            trn2Hldr.trn2DateTV.setText(transfers.get(position).getTransCreatedOn());
            trn2Amt = transfers.get(position).getTransAmt();
            trn2Gen.dblASCurrency(String.valueOf(trn2Amt), trn2Hldr.trn2AmtTV);
            trn2Hldr.trn2FromNameTV.setText(transfers.get(position).getTransFromAcct());
            trn2Hldr.trn2ToNameTV.setText(transfers.get(position).getTransToAcct());

            trn2TrnAmtOutA = transfers.get(position).getTransAmtOutA();
            trn2TrnAmtOutOwing = transfers.get(position).getTransAmtOutOwing();
            trn2TrnAmtOutB = transfers.get(position).getTransAmtOutB();
            trn2TrnAmtInA = transfers.get(position).getTransAmtInA();
            trn2TrnAmtInOwing = transfers.get(position).getTransAmtInOwing();
            trn2TrnAmtInB = transfers.get(position).getTransAmtInB();

            trn2FromDebtId = transfers.get(position).getTransFromDebtId();
            trn2FromSavId = transfers.get(position).getTransFromSavid();
            trn2ToDebtId = transfers.get(position).getTransToDebtId();
            trn2ToSavId = transfers.get(position).getTransToSavid();

            trn2MoneyOutAPercent = transfers.get(position).getTransAmtOutA() / transfers.get(position).getTransAmt();
            trn2MoneyInAPercent = transfers.get(position).getTransAmtInA() / transfers.get(position).getTransAmt();

            trn2Hldr.trn2EditBtn.setTag(transfers.get(position));
            trn2Hldr.trn2DelBtn.setTag(transfers.get(position));

            trn2Hldr.trn2EditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    trn2TransDb = (TransfersDb) trn2Hldr.trn2EditBtn.getTag();
                    LayoutTransfersList.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    trn2DbMgr = new DbManager(getContext());

                    trn2Hldr.trn2UpdateLayout.setVisibility(View.VISIBLE);

                    trn2Hldr.trn2CancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn2Refresh();
                        }
                    });

                    trn2Hldr.trn2UpdateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn2AmtEntry = trn2Gen.dblFromET(trn2Hldr.trn2AmtET);
                            trn2TrnAmtDiff = trn2AmtEntry - transfers.get(position).getTransAmt();

                            if (trn2FromDebtId == 0 && trn2FromSavId == 0) { //FROM MAIN ACCT
                                if (trn2DbMgr.retrieveCurrentAccountBalance() - trn2TrnAmtDiff < 0) { //ACCT WILL GO NEGATIVE
                                    trn2Hldr.trn2WarnLayout.setVisibility(View.VISIBLE);
                                    trn2Hldr.trn2WarnTV.setText(getString(R.string.payment_not_possible_A));

                                    trn2Hldr.trn2NoBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Refresh();
                                        }
                                    });

                                    trn2Hldr.trn2YesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Hldr.trn2WarnLayout.setVisibility(View.GONE);
                                            revAndAdjTransOut();
                                            if(trn2ToDebtId != 0) { //TO DEBT ACCT
                                                trn2DebtMinus();
                                            } else if(trn2ToSavId != 0) { //TO SAVINGS ACCT
                                                trn2SavPlus();
                                            }
                                        }
                                    });
                                } else { //ACCT WON'T GO NEGATIVE
                                    revAndAdjTransOut();
                                    if (trn2ToDebtId != 0) { //TO DEBT ACCT
                                        trn2DebtMinus();
                                    } else if (trn2ToSavId != 0) { //TO SAVINGS ACCT
                                        trn2SavPlus();
                                    }
                                }
                            } else if (trn2FromDebtId != 0) { //FROM DEBT ACCT
                                for (DebtDb d : trn2DbMgr.getDebts()) {
                                    if (d.getId() == trn2FromDebtId) {
                                        debtLimitFromDb = d.getDebtLimit();
                                        debtAmtFromDb = d.getDebtOwing();
                                    }
                                }
                                if (debtAmtFromDb + trn2TrnAmtDiff > debtLimitFromDb) { //DEBT WILL GO OVER LIMIT
                                    trn2Hldr.trn2WarnLayout.setVisibility(View.VISIBLE);
                                    trn2Hldr.trn2WarnTV.setText(getString(R.string.not_enough_credit_warning));

                                    trn2Hldr.trn2NoBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Refresh();
                                        }
                                    });

                                    trn2Hldr.trn2YesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Hldr.trn2WarnLayout.setVisibility(View.GONE);
                                            trn2DebtPlus();
                                            if (trn2ToSavId != 0) { //TO SAVINGS ACCT
                                                trn2SavPlus();
                                            } else if (trn2ToDebtId != 0) { //TO DEBT ACCT
                                                trn2DebtMinus();
                                            } else { //TO MAIN ACCT
                                                revAndAdjTransIn();
                                            }
                                        }
                                    });
                                } else { //DEBT WON'T GO OVER LIMIT
                                    trn2DebtPlus();
                                    if (trn2ToSavId != 0) { //TO SAVINGS ACCT
                                        trn2SavPlus();
                                    } else if (trn2ToDebtId != 0) { //TO DEBT ACCT
                                        trn2DebtMinus();
                                    } else { //TO MAIN ACCT
                                        revAndAdjTransIn();
                                    }
                                }
                            } else if (trn2FromSavId != 0) { //FROM SAVINGS ACCT
                                if(trn2DbMgr.retrieveCurrentSavAmt(trn2FromSavId) - trn2TrnAmtDiff < 0) { //ACCT WILL GO NEGATIVE
                                    trn2Hldr.trn2WarnLayout.setVisibility(View.VISIBLE);
                                    trn2Hldr.trn2WarnTV.setText(getString(R.string.not_enough_savings_warning));

                                    trn2Hldr.trn2NoBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Refresh();
                                        }
                                    });

                                    trn2Hldr.trn2YesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            trn2Hldr.trn2WarnLayout.setVisibility(View.GONE);
                                            trn2SavMinus();
                                            if (trn2ToSavId != 0) { //TO SAVINGS ACCT
                                                trn2SavPlus();
                                            } else if (trn2ToDebtId != 0) { //TO DEBT ACCT
                                                trn2DebtMinus();
                                            } else { //TO MAIN ACCT
                                                revAndAdjTransIn();
                                            }
                                        }
                                    });
                                } else { //ACCT WON'T GO OVER NEGATIVE
                                    trn2SavMinus();
                                    if (trn2ToSavId != 0) { //TO SAVINGS ACCT
                                        trn2SavPlus();
                                    } else if (trn2ToDebtId != 0) { //TO DEBT ACCT
                                        trn2DebtMinus();
                                    } else { //TO MAIN ACCT
                                        revAndAdjTransIn();
                                    }
                                }
                            }

                            trn2Adapter.updateTransfers(trn2DbMgr.getTransfers());
                            notifyDataSetChanged();

                            trn2Refresh();
                        }
                    });

                    trn2Hldr.trn2DelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trn2TransDb = (TransfersDb) trn2Hldr.trn2DelBtn.getTag();

                            if (trn2FromDebtId == 0 && trn2FromSavId == 0) { //FROM MAIN ACCT
                                trn2DbMgr.updateTotAcctBalPlus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                                trn2DbMgr.updateAvailBalPlus(transfers.get(position).getTransAmtOutA(), transfers.get(position).getTransAmtOutOwing(), transfers.get(position).getTransAmtOutB(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

                                if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                                    trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                                }

                                if(trn2ToDebtId != 0) { //TO DEBT ACCT
                                    trn2DebtPlus();
                                } else if(trn2ToSavId != 0) { //TO SAVINGS ACCT
                                    trn2SavMinus();
                                }
                            } else if (trn2FromDebtId != 0) { //FROM DEBT ACCT
                                trn2DebtMinus();

                                if(trn2ToDebtId != 0) { //TO DEBT ACCT
                                    trn2DebtPlus();
                                } else if(trn2ToSavId != 0) { //TO SAVINGS ACCT
                                    trn2SavMinus();
                                } else { //TO MAIN ACCT
                                    trn2DbMgr.updateTotAcctBalMinus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                                    trn2DbMgr.updateAvailBalMinus(transfers.get(position).getTransAmtOutA(), transfers.get(position).getTransAmtOutOwing(), transfers.get(position).getTransAmtOutB(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

                                    if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                                        trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                                    }
                                }
                            } else if (trn2FromSavId != 0) { //FROM SAVINGS ACCT
                                trn2SavPlus();

                                if(trn2ToDebtId != 0) { //TO DEBT ACCT
                                    trn2DebtPlus();
                                } else if(trn2ToSavId != 0) { //TO SAVINGS ACCT
                                    trn2SavMinus();
                                } else { //TO MAIN ACCT
                                    trn2DbMgr.updateTotAcctBalMinus(transfers.get(position).getTransAmt(), trn2DbMgr.retrieveCurrentAccountBalance());
                                    trn2DbMgr.updateAvailBalMinus(transfers.get(position).getTransAmtOutA(), transfers.get(position).getTransAmtOutOwing(), transfers.get(position).getTransAmtOutB(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentB());

                                    if (trn2DbMgr.retrieveCurrentOwingA() < 0) {
                                        trn2DbMgr.adjustCurrentAandB(trn2DbMgr.retrieveCurrentOwingA(), trn2DbMgr.retrieveCurrentA(), trn2DbMgr.retrieveCurrentB());
                                    }
                                }
                            }

                            trn2DbMgr.deleteTransfers(trn2TransDb);

                            trn2Adapter.updateTransfers(trn2DbMgr.getTransfers());
                            notifyDataSetChanged();

                            trn2Refresh();
                        }
                    });
                }
            });

            return convertView;
        }
    }

    private static class Trans2ViewHolder {
        public TextView trn2DateTV;
        public TextView trn2AmtTV;
        public TextView trn2FromNameTV;
        public TextView trn2ToNameTV;
        public ImageButton trn2EditBtn;
        public ImageButton trn2DelBtn;
        public RelativeLayout trn2UpdateLayout;
        public EditText trn2AmtET;
        public Button trn2UpdateBtn;
        public Button trn2CancelBtn;
        public LinearLayout trn2WarnLayout;
        public TextView trn2WarnTV;
        public Button trn2YesBtn;
        public Button trn2NoBtn;
    }
}
