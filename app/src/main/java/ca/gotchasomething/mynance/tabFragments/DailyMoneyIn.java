/*package ca.gotchasomething.mynance.tabFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ca.gotchasomething.mynance.AddIncome;
import ca.gotchasomething.mynance.DailyMoneyInListB;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.IncomeBudgetDb;
import ca.gotchasomething.mynance.data.MoneyInDb;

public class DailyMoneyIn extends Fragment {

    Button monIn1CreateBtn;
    ContentValues monInCV;
    DbHelper monInHelper;
    DbManager monInDbMgr;
    Double moneyInA = 0.0, moneyInOwing = 0.0, moneyInB = 0.0, monInMonInAmt = 0.0, monInMonInOldAmt = 0.0, monInMonInNewAmt = 0.0, newMoneyA = 0.0,
            newMoneyOwing = 0.0, newMoneyB = 0.0;
    General monInGen;
    IncomeBudgetDb monInIncDb;
    Intent monInToAddInc, monInToList;
    LayoutDailyMoney monInLayDaiMon;
    LinearLayout frag1HeaderLayout;
    ListView monInList;
    long monInIncRefKeyMI;
    MoneyInDb monInMoneyInDb;
    MonInAdapter monInAdapter;
    SQLiteDatabase monInDb;
    TextView temp, tempE, monInAvailAcctTV, monInBudgWarnTV, monInTotAcctTV;
    View v;

    public DailyMoneyIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_1_choose_or_create, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monInDbMgr = new DbManager(getContext());
        monInGen = new General();

        temp = v.findViewById(R.id.temp);
        temp.setVisibility(View.GONE);
        tempE = v.findViewById(R.id.tempE);
        tempE.setVisibility(View.GONE);
        frag1HeaderLayout = v.findViewById(R.id.frag1HeaderLayout);
        frag1HeaderLayout.setVisibility(View.GONE);
        monIn1CreateBtn = v.findViewById(R.id.frag1CreateBtn);
        monIn1CreateBtn.setOnClickListener(onClickMonIn1CreateBtn);
        monInAvailAcctTV = v.findViewById(R.id.dailyMoneyAvailAmtTV);
        monInBudgWarnTV = v.findViewById(R.id.dailyMoneyBudgetWarnTV);
        monInTotAcctTV = v.findViewById(R.id.dailyMoneyTotalAmtTV);

        monInList = v.findViewById(R.id.frag1ListView);
        monInAdapter = new MonInAdapter(getContext(), monInDbMgr.getIncomes());
        monInList.setAdapter(monInAdapter);

        monInCV = new ContentValues();
        monInCV.put(DbHelper.LASTPAGEID, 1);
        monInHelper = new DbHelper(getContext());
        monInDb = monInHelper.getWritableDatabase();
        monInDb.update(DbHelper.CURRENT_TABLE_NAME, monInCV, DbHelper.ID + "= '1'", null);
        monInDb.close();

    }

    View.OnClickListener onClickMonIn1CreateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monInToAddInc = new Intent(getContext(), AddIncome.class);
            monInToAddInc.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(monInToAddInc);
        }
    };

    public class MonInAdapter extends ArrayAdapter<IncomeBudgetDb> {

        private Context context;
        private List<IncomeBudgetDb> incomes;

        private MonInAdapter(
                Context context,
                List<IncomeBudgetDb> incomes) {

            super(context, -1, incomes);

            this.context = context;
            this.incomes = incomes;
        }

        public void updateIncomes(List<IncomeBudgetDb> incomes) {
            this.incomes = incomes;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incomes.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder monInHldr;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.frag_list_3_payments,
                        parent, false);

                monInHldr = new MoneyInViewHolder();
                monInHldr.monInDepLabel = convertView.findViewById(R.id.paytPayLabel);
                monInHldr.monInDepLabel.setText(getString(R.string.deposit));
                monInHldr.monInFromLabel = convertView.findViewById(R.id.paytToLabel);
                monInHldr.monInFromLabel.setText(getString(R.string.from));
                monInHldr.monInCatTV = convertView.findViewById(R.id.paytCatTV);
                monInHldr.monInAmtTV = convertView.findViewById(R.id.paytAmtTV);
                monInHldr.monInNewAmtET = convertView.findViewById(R.id.paytAmtET);
                monInHldr.monInSaveButton = convertView.findViewById(R.id.paytSaveBtn);
                monInHldr.monInWarnLayout = convertView.findViewById(R.id.paytWarnLayout);
                monInHldr.monInWarnLayout.setVisibility(View.GONE);
                monInHldr.monInWarnTV = convertView.findViewById(R.id.paytWarnTV);
                monInHldr.monInYesContButton = convertView.findViewById(R.id.paytYesContBtn);
                monInHldr.monInNoContButton = convertView.findViewById(R.id.paytNoContBtn);
                monInHldr.monInDefLayout = convertView.findViewById(R.id.paytDefLayout);
                monInHldr.monInDefLayout.setVisibility(View.GONE);
                monInHldr.monInYesDefButton = convertView.findViewById(R.id.paytYesDefBtn);
                monInHldr.monInNoDefButton = convertView.findViewById(R.id.paytNoDefBtn);
                convertView.setTag(monInHldr);

            } else {
                monInHldr = (MoneyInViewHolder) convertView.getTag();
            }

            monInHldr.monInCatTV.setText(incomes.get(position).getIncomeName());
            monInGen.dblASCurrency(String.valueOf(incomes.get(position).getIncomeAmount()), monInHldr.monInAmtTV);
            //monInHldr.monInAmtTV.setText(String.valueOf(incomes.get(position).getIncomeAmount()));

            monInIncRefKeyMI = incomes.get(position).getId();

            monInHldr.monInSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monInIncDb = (IncomeBudgetDb) monInHldr.monInSaveButton.getTag();

                    monInMonInOldAmt = incomes.get(position).getIncomeAmount();
                    monInMonInNewAmt = monInGen.dblFromET(monInHldr.monInNewAmtET);

                    if (monInMonInNewAmt == 0) {
                        monInMonInAmt = monInMonInOldAmt;
                    } else {
                        monInMonInAmt = monInMonInNewAmt;
                    }

                    if (monInMonInAmt == monInMonInNewAmt) {
                        monInHldr.monInDefLayout.setVisibility(View.VISIBLE);

                        monInHldr.monInNoDefButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monInHldr.monInDefLayout.setVisibility(View.GONE);
                            }
                        });

                        monInHldr.monInYesDefButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monInHldr.monInDefLayout.setVisibility(View.GONE);
                                monInIncDb.setIncomeAmount(monInMonInAmt);
                                monInDbMgr.updateIncome(monInIncDb);
                            }
                        });
                    }

                    monInDbMgr.detAandBPortionsInc(monInMonInAmt);

                    monInDbMgr.updateTotAcctBalPlus(monInMonInAmt);

                    monInDbMgr.updateAvailBalPlus(moneyInA, moneyInOwing, moneyInB);
                    if (monInDbMgr.retrieveCurrentOwingA() < 0) {
                        monInDbMgr.adjustAandBPortions(moneyInA, moneyInOwing, moneyInB);
                        moneyInA = newMoneyA;
                        moneyInOwing = newMoneyOwing;
                        moneyInB = newMoneyB;
                    }

                    monInMoneyInDb = new MoneyInDb(
                            incomes.get(position).getIncomeName(),
                            monInMonInAmt,
                            moneyInA,
                            moneyInOwing,
                            moneyInB,
                            monInGen.createTimestamp(),
                            incomes.get(position).getId(),
                            0);
                    monInDbMgr.addMoneyIn(monInMoneyInDb);
                    monInAdapter.updateIncomes(monInDbMgr.getIncomes());
                    notifyDataSetChanged();

                    monInDbMgr.updateIncAnnAmt(incomes.get(position).getId());

                    //monInLayDaiMon = new LayoutDailyMoney();
                    //monInLayDaiMon.layDaiMonHeaderText();

                    //monInDbMgr.dailyMoneyHeaderText(monInBudgWarnTV, monInTotAcctTV, monInAvailAcctTV);
                    monInToList = new Intent(getContext(), DailyMoneyInListB.class);
                    monInToList.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(monInToList);
                }
            });
            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView monInDepLabel;
        public TextView monInFromLabel;
        public TextView monInCatTV;
        public TextView monInAmtTV;
        public EditText monInNewAmtET;
        public ImageButton monInSaveButton;
        public LinearLayout monInWarnLayout;
        public TextView monInWarnTV;
        public Button monInYesContButton;
        public Button monInNoContButton;
        public LinearLayout monInDefLayout;
        public Button monInYesDefButton;
        public Button monInNoDefButton;
    }
}*/
