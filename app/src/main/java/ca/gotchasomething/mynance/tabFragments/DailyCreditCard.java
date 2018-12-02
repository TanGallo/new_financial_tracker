package ca.gotchasomething.mynance.tabFragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import ca.gotchasomething.mynance.DbHelper;
//import ca.gotchasomething.mynance.HeaderDailyMoney;
import ca.gotchasomething.mynance.LayoutDebt;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.DebtDb;
import ca.gotchasomething.mynance.data.DebtDbManager;
import ca.gotchasomething.mynance.data.ExpenseBudgetDbManager;
import ca.gotchasomething.mynance.data.IncomeBudgetDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyCreditCard extends Fragment {

    boolean isSpinnerTouched;
    Button cancelCCButton, updateCCButton;
    CCAdapter ccAdapter, ccAdapter2;
    CheckBox ccPaidCheckbox;
    Cursor moneyOutCursor, moneyOutCursor2, moneyOutCursor4, moneyOutCursor5, moneyOutCursor6, incomeCursor, expenseCursor, moneyInCursor;
    DbHelper moneyOutHelper, moneyOutHelper2, moneyOutHelper3, moneyOutHelper4, moneyOutHelper5, moneyOutHelper6, incomeHelper, expenseHelper, moneyInHelper;
    Double ccAmountD, thisAmount, moneyOutAmountS, totalCCPaymentDue, ccPaymentAmount, newCCPaymentAmount, accountBalance, totalBudgetAExpenses,
            totalBudgetIncome, percentB, moneyInTotal, moneyOutTotal, toPayToB, totalSpentOnB;
    EditText ccCatEntry, ccAmountEntry;
    ExpenseBudgetDbManager expenseBudgetDbManager;
    ImageButton thisCatButton;
    IncomeBudgetDbManager incomeBudgetDbManager;
    int thisIdL, toPayToday, maxMoneyOutToPay;
    Intent refreshHeader, editCC, backToDailyCreditCard, backToDailyCreditCard2;
    LinearLayout editCCLayout;
    ListView ccListView;
    long moneyOutId, thisId, thisId2;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutSpinnerAdapter moneyOutAdapter, moneyOutAdapter2;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    Spinner ccCatSpinner, ccCatSpinner2;
    SQLiteDatabase moneyOutDbDb, moneyOutDbDb2, moneyOutDbDb3, moneyOutDbDb4, moneyOutDbDb5, moneyOutDbDb6, incomeDbDb, expenseDbDb, moneyInDbDb;
    String ccAmountS, ccAmount2, thisCat, moneyOutCatS, moneyOutPriorityS, selectedItemCategory, totalCCPaymentDueS, exec;
    TextView checkBelowLabel, totalCCPaymentDueLabel, totalCCPaymentDueAmount, ccPaidLabel, thisCatText, ccOopsText;
    View v, editCCLine;

    public DailyCreditCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_credit_card, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editCCLayout = v.findViewById(R.id.editCCLayout);
        editCCLayout.setVisibility(View.GONE);
        editCCLine = v.findViewById(R.id.editCCLine);
        editCCLine.setVisibility(View.GONE);
        thisCatText = v.findViewById(R.id.thisCatText);
        thisCatText.setVisibility(View.GONE);
        ccAmountEntry = v.findViewById(R.id.ccAmountEntry);
        ccAmountEntry.setVisibility(View.GONE);
        cancelCCButton = v.findViewById(R.id.cancelCCButton);
        cancelCCButton.setVisibility(View.GONE);
        updateCCButton = v.findViewById(R.id.updateCCButton);
        updateCCButton.setVisibility(View.GONE);
        checkBelowLabel = v.findViewById(R.id.checkBelowLabel);
        totalCCPaymentDueLabel = v.findViewById(R.id.totalCCPaymentDueLabel);
        totalCCPaymentDueLabel.setVisibility(View.GONE);
        totalCCPaymentDueAmount = v.findViewById(R.id.totalCCPaymentDueAmount);
        totalCCPaymentDueAmount.setVisibility(View.GONE);
        ccPaidLabel = v.findViewById(R.id.ccPaidLabel);
        ccPaidLabel.setVisibility(View.GONE);
        ccPaidCheckbox = v.findViewById(R.id.ccPaidCheckbox);
        ccPaidCheckbox.setVisibility(View.GONE);
        ccOopsText = v.findViewById(R.id.ccOopsText);
        ccOopsText.setVisibility(View.GONE);

        ccListView = v.findViewById(R.id.ccListView);

        moneyOutDbManager = new MoneyOutDbManager(getContext());
        ccAdapter = new CCAdapter(getContext(), moneyOutDbManager.getCCTrans());
        ccListView.setAdapter(ccAdapter);

        //incomeBudgetDbManager = new IncomeBudgetDbManager(getContext());
        //expenseBudgetDbManager = new ExpenseBudgetDbManager(getContext());

        resetToPay();
        updateCCPaymentDue();
    }

    public void checkIfPaymentPossible() {

        if (retrieveToPayTotal() > (retrieveMoneyInTotal() - retrieveMoneyOutTotal()) ||
                retrieveToPayBTotal() > ((retrieveMoneyInTotal() * retrieveBPercentage()) - retrieveMoneyOutBTotal())) {
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
            ccOopsText.setVisibility(View.VISIBLE);
        } else {
            ccPaidLabel.setVisibility(View.VISIBLE);
            ccPaidCheckbox.setVisibility(View.VISIBLE);
            ccOopsText.setVisibility(View.GONE);
        }
    }

    public Double retrieveToPayBTotal() {
        moneyOutHelper5 = new DbHelper(getContext());
        moneyOutDbDb5 = moneyOutHelper5.getReadableDatabase();
        moneyOutCursor5 = moneyOutDbDb5.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        try {
            moneyOutCursor5.moveToFirst();
        } catch (Exception e) {
            toPayToB = 0.0;
        }
        toPayToB = moneyOutCursor5.getDouble(0);
        moneyOutCursor5.close();
        return toPayToB;
    }

    public Double retrieveBPercentage() {
        expenseHelper = new DbHelper(getContext());
        expenseDbDb = expenseHelper.getReadableDatabase();
        expenseCursor = expenseDbDb.rawQuery("SELECT sum(expenseAAnnualAmount)" + " FROM " + DbHelper.EXPENSES_TABLE_NAME, null);
        try {
            expenseCursor.moveToFirst();
        } catch (Exception e) {
            totalBudgetAExpenses = 0.0;
        }
        totalBudgetAExpenses = expenseCursor.getDouble(0);
        expenseCursor.close();

        incomeHelper = new DbHelper(getContext());
        incomeDbDb = incomeHelper.getReadableDatabase();
        incomeCursor = incomeDbDb.rawQuery("SELECT sum(incomeAnnualAmount)" + " FROM " + DbHelper.INCOME_TABLE_NAME, null);
        incomeCursor.moveToFirst();
        totalBudgetIncome = incomeCursor.getDouble(0);
        incomeCursor.close();

        percentB = 1 - (totalBudgetAExpenses / totalBudgetIncome);

        return percentB;

    }

    public Double retrieveMoneyInTotal() {
        moneyInHelper = new DbHelper(getContext());
        moneyInDbDb = moneyInHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT sum(moneyInAmount)" + " FROM " + DbHelper.MONEY_IN_TABLE_NAME, null);
        moneyInCursor.moveToFirst();
        moneyInTotal = moneyInCursor.getDouble(0);
        moneyInCursor.close();

        return moneyInTotal;
    }

    public Double retrieveMoneyOutTotal() {
        moneyOutHelper4 = new DbHelper(getContext());
        moneyOutDbDb4 = moneyOutHelper4.getReadableDatabase();
        moneyOutCursor4 = moneyOutDbDb4.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME +
                " WHERE " + DbHelper.MONEYOUTCC + " = 'N'", null);
        try {
            moneyOutCursor4.moveToFirst();
        } catch (Exception e2) {
            moneyOutTotal = 0.0;
        }
        moneyOutTotal = moneyOutCursor4.getDouble(0);
        moneyOutCursor4.close();

        return moneyOutTotal;
    }

    public Double retrieveMoneyOutBTotal() {
        moneyOutHelper6 = new DbHelper(getContext());
        moneyOutDbDb6 = moneyOutHelper6.getReadableDatabase();
        moneyOutCursor6 = moneyOutDbDb6.rawQuery("SELECT sum(moneyOutAmount)" + " FROM " + DbHelper.MONEY_OUT_TABLE_NAME + " WHERE " +
                DbHelper.MONEYOUTCC + " = 'N' AND " + DbHelper.MONEYOUTPRIORITY + " = 'B'", null);
        try {
            moneyOutCursor6.moveToFirst();
        } catch (Exception e3) {
            totalSpentOnB = 0.0;
        }
        totalSpentOnB = moneyOutCursor6.getDouble(0);
        moneyOutCursor6.close();

        return totalSpentOnB;
    }

    public void resetToPay() {
        moneyOutHelper3 = new DbHelper(getContext());
        moneyOutDbDb3 = moneyOutHelper3.getWritableDatabase();
        ContentValues updateMoneyOutToPay = new ContentValues();
        updateMoneyOutToPay.put(DbHelper.MONEYOUTTOPAY, 0);
        moneyOutDbDb3.update(DbHelper.MONEY_OUT_TABLE_NAME, updateMoneyOutToPay, DbHelper.MONEYOUTTOPAY + "='1'", null);
    }

    public Double retrieveToPayTotal() {
        moneyOutHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT sum(moneyOutAmount) FROM " + DbHelper.MONEY_OUT_TABLE_NAME
                + " WHERE " + DbHelper.MONEYOUTTOPAY + " = '1'", null);
        try {
            moneyOutCursor.moveToFirst();
        } catch (Exception e) {
            totalCCPaymentDue = 0.0;
        }
        totalCCPaymentDue = moneyOutCursor.getDouble(0);
        moneyOutCursor.close();

        return totalCCPaymentDue;
    }

    public void updateCCPaymentDue() {

        retrieveToPayTotal();

        totalCCPaymentDueS = currencyFormat.format(totalCCPaymentDue);
        totalCCPaymentDueAmount.setText(totalCCPaymentDueS);

        moneyOutHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT max(moneyOutToPay) FROM " + DbHelper.MONEY_OUT_TABLE_NAME, null);
        moneyOutCursor2.moveToFirst();
        maxMoneyOutToPay = moneyOutCursor2.getInt(0);
        moneyOutCursor2.close();

        if (totalCCPaymentDue == 0.0 && maxMoneyOutToPay == 0) {
            checkBelowLabel.setVisibility(View.VISIBLE);
            totalCCPaymentDueLabel.setVisibility(View.GONE);
            totalCCPaymentDueAmount.setVisibility(View.GONE);
            ccPaidLabel.setVisibility(View.GONE);
            ccPaidCheckbox.setVisibility(View.GONE);
        } else {
            checkBelowLabel.setVisibility(View.GONE);
            totalCCPaymentDueLabel.setVisibility(View.VISIBLE);
            totalCCPaymentDueAmount.setVisibility(View.VISIBLE);
            ccPaidLabel.setVisibility(View.VISIBLE);
            ccPaidCheckbox.setVisibility(View.VISIBLE);
        }
    }

    public class CCAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private CCAdapter(
                Context context,
                List<MoneyOutDb> ccTrans) {

            super(context, -1, ccTrans);

            this.context = context;
            this.ccTrans = ccTrans;
        }

        public void updateCCTrans(List<MoneyOutDb> ccTrans) {
            this.ccTrans = ccTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ccTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final CCViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_credit_card,
                        parent, false);

                holder = new CCViewHolder();
                holder.ccCat = convertView.findViewById(R.id.ccCat);
                holder.ccAmount = convertView.findViewById(R.id.ccAmount);
                holder.ccCheck = convertView.findViewById(R.id.ccCheck);
                holder.ccDeleted = convertView.findViewById(R.id.deleteCCButton);
                holder.ccEdit = convertView.findViewById(R.id.editCCButton);
                convertView.setTag(holder);

            } else {
                holder = (CCViewHolder) convertView.getTag();
            }

            holder.ccDeleted.setTag(ccTrans.get(position));
            holder.ccEdit.setTag(ccTrans.get(position));
            holder.ccCheck.setTag(ccTrans.get(position));

            //retrieve ccCat
            holder.ccCat.setText(ccTrans.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccAmountS = (String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                if (ccAmountS != null && !ccAmountS.equals("")) {
                    ccAmountD = Double.valueOf(ccAmountS);
                } else {
                    ccAmountD = 0.0;
                }
                ccAmount2 = currencyFormat.format(ccAmountD);
                holder.ccAmount.setText(ccAmount2);
            } catch (NumberFormatException e) {
                holder.ccAmount.setText(ccAmount2);
            }

            holder.ccCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    moneyOutDb = (MoneyOutDb) holder.ccCheck.getTag();

                    if (isChecked) {
                        moneyOutDb.setMoneyOutToPay(1);
                    } else {
                        moneyOutDb.setMoneyOutToPay(0);
                    }

                    moneyOutDbManager.updateMoneyOut(moneyOutDb);

                    updateCCPaymentDue();
                    checkIfPaymentPossible();

                }
            });

            //click on pencil icon
            holder.ccEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccEdit.getTag();

                    checkBelowLabel.setVisibility(View.GONE);
                    totalCCPaymentDueAmount.setVisibility(View.GONE);
                    ccPaidLabel.setVisibility(View.GONE);
                    ccPaidCheckbox.setVisibility(View.GONE);
                    editCCLayout.setVisibility(View.VISIBLE);
                    editCCLine.setVisibility(View.VISIBLE);
                    thisCatText.setVisibility(View.VISIBLE);
                    ccAmountEntry.setVisibility(View.VISIBLE);
                    cancelCCButton.setVisibility(View.VISIBLE);
                    updateCCButton.setVisibility(View.VISIBLE);

                    ccAmountEntry.setText(String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                    thisCatText.setText(String.valueOf(ccTrans.get(position).getMoneyOutCat()));

                }
            });

            cancelCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkBelowLabel.setVisibility(View.VISIBLE);
                    editCCLayout.setVisibility(View.GONE);
                    editCCLine.setVisibility(View.GONE);
                }
            });

            updateCCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb.setMoneyOutAmount(Double.valueOf(ccAmountEntry.getText().toString()));

                    moneyOutDbManager.updateMoneyOut(moneyOutDb);
                    ccAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();

                    checkBelowLabel.setVisibility(View.VISIBLE);
                    editCCLayout.setVisibility(View.GONE);
                    editCCLine.setVisibility(View.GONE);
                }
            });

            //click on trash can icon
            holder.ccDeleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccDeleted.getTag();
                    moneyOutDbManager.deleteMoneyOut(moneyOutDb);

                    ccAdapter.updateCCTrans(moneyOutDbManager.getCCTrans());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class CCViewHolder {
        public TextView ccCat;
        public TextView ccAmount;
        public CheckBox ccCheck;
        ImageButton ccDeleted;
        ImageButton ccEdit;
    }
}
