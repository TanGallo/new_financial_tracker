package ca.gotchasomething.mynance.tabFragments;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.CurrentDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyOut extends Fragment {

    boolean possible = true;
    Button moneyOutButton, cancelMoneyOutEntryButton, updateMoneyOutEntryButton;
    ContentValues moneyOutValue, moneyOutValue2;
    CurrentDbManager currentDbManager;
    Cursor moneyOutCursor;
    Date moneyOutDate;
    DbHelper moneyOutDbHelper, currentHelper3, currentHelper5;
    Double moneyOutAmount, currentAccountBalance, newCurrentAccountBalance3, currentAvailableBalance, newCurrentAvailableBalance3, moneyOutD,
            oldMoneyOutAmount, newMoneyOutAmount, moneyOutAmountD;
    EditText moneyOutAmountText, moneyOutAmountEditText;
    General general;
    int moneyOutToPay, moneyOutPaid;
    Intent backToDaily, backToDaily2, backToDaily3;
    ListView moneyOutList;
    MoneyOutAdapter moneyOutAdapter;
    MoneyOutDb moneyOutDb;
    MoneyOutDbManager moneyOutDbManager;
    MoneyOutSpinnerAdapter moneyOutSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyOutSDF;
    Spinner moneyOutCatSpinner;
    SQLiteDatabase moneyOutDbDb, currentDbDb3, currentDbDb5;
    String moneyOutCatS, moneyOutCat, moneyOutPriority, moneyOutPriorityS, moneyOutCreatedOn, moneyOutCC, moneyOutS, moneyOut2, moneyOutAmountS;
    TextView moneyOutCatText;
    Timestamp moneyOutTimestamp;
    View v, moneyOutLine;

    public DailyMoneyOut() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_out, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();
        currentDbManager = new CurrentDbManager(getContext());

        moneyOutAmountText = v.findViewById(R.id.moneyOutAmount);
        moneyOutButton = v.findViewById(R.id.moneyOutButton);
        moneyOutList = v.findViewById(R.id.moneyOutList);
        moneyOutCatText = v.findViewById(R.id.moneyOutCatText);
        moneyOutCatText.setVisibility(View.GONE);
        moneyOutAmountEditText = v.findViewById(R.id.moneyOutAmountEditText);
        moneyOutAmountEditText.setVisibility(View.GONE);
        cancelMoneyOutEntryButton = v.findViewById(R.id.cancelMoneyOutEntryButton);
        cancelMoneyOutEntryButton.setVisibility(View.GONE);
        updateMoneyOutEntryButton = v.findViewById(R.id.updateMoneyOutEntryButton);
        updateMoneyOutEntryButton.setVisibility(View.GONE);
        moneyOutLine = v.findViewById(R.id.moneyOutLine);
        moneyOutLine.setVisibility(View.GONE);

        moneyOutButton.setOnClickListener(onClickMoneyOutButton);

        moneyOutDbManager = new MoneyOutDbManager(getContext());
        moneyOutAdapter = new MoneyOutAdapter(getContext(), moneyOutDbManager.getCashTrans());
        moneyOutList.setAdapter(moneyOutAdapter);

        moneyOutCatSpinner = v.findViewById(R.id.moneyOutCatSpinner);
        moneyOutDbHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutDbHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        moneyOutSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor);
        moneyOutCatSpinner.setAdapter(moneyOutSpinnerAdapter);

        moneyOutCatSpinner.setOnItemSelectedListener(moneyOutSpinnerSelection);

    }

    public void updateCurrentAvailableBalanceMoneyOut() {
        currentAvailableBalance = currentDbManager.retrieveCurrentAvailableBalance();
        newCurrentAvailableBalance3 = currentAvailableBalance - moneyOutAmount;

        if (newCurrentAvailableBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {

            moneyOutValue2 = new ContentValues();
            moneyOutValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newCurrentAvailableBalance3);
            currentHelper5 = new DbHelper(getContext());
            currentDbDb5 = currentHelper5.getWritableDatabase();
            currentDbDb5.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue2, DbHelper.ID + "= '1'", null);
        }
    }

    public void updateCurrentAccountBalanceMoneyOut() {
        currentAccountBalance = currentDbManager.retrieveCurrentAccountBalance();
        newCurrentAccountBalance3 = currentAccountBalance - moneyOutAmount;

        if (newCurrentAccountBalance3 < 0) {
            Toast.makeText(getContext(), "You cannot make this purchase. See the Help section if you'd like suggestions.", Toast.LENGTH_LONG).show();
            possible = false;
        } else {

            moneyOutValue = new ContentValues();
            moneyOutValue.put(DbHelper.CURRENTACCOUNTBALANCE, newCurrentAccountBalance3);
            currentHelper3 = new DbHelper(getContext());
            currentDbDb3 = currentHelper3.getWritableDatabase();
            currentDbDb3.update(DbHelper.CURRENT_TABLE_NAME, moneyOutValue, DbHelper.ID + "= '1'", null);
        }
    }

    AdapterView.OnItemSelectedListener moneyOutSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyOutCatS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            moneyOutPriorityS = moneyOutCursor.getString(moneyOutCursor.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onClickMoneyOutButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = moneyOutCatS;
            moneyOutPriority = moneyOutPriorityS;
            moneyOutAmount = Double.valueOf(moneyOutAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "N";
            moneyOutToPay = 0;
            moneyOutPaid = 0;

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyOutAmountText.setText("");
            moneyOutCatSpinner.setSelection(0, false);

            moneyOutAdapter.updateCashTrans(moneyOutDbManager.getCashTrans());
            moneyOutAdapter.notifyDataSetChanged();

            if (moneyOutPriority.equals("B")) {
                updateCurrentAvailableBalanceMoneyOut();

                if (possible) {
                    updateCurrentAccountBalanceMoneyOut();
                }
            } else if (moneyOutPriority.equals("A")) {
                updateCurrentAccountBalanceMoneyOut();
            }

            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily2);
        }
    };

    public class MoneyOutAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> cashTrans;

        private MoneyOutAdapter(
                Context context,
                List<MoneyOutDb> cashTrans) {

            super(context, -1, cashTrans);

            this.context = context;
            this.cashTrans = cashTrans;
        }

        public void updateCashTrans(List<MoneyOutDb> cashTrans) {
            this.cashTrans = cashTrans;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return cashTrans.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyOutViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new MoneyOutViewHolder();
                holder.moneyOutEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.moneyOutDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.moneyOutAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.moneyOutCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.moneyOutDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (MoneyOutViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.moneyOutDate.setText(cashTrans.get(position).getMoneyOutCreatedOn());

            //retrieve moneyOutCat
            holder.moneyOutCat.setText(cashTrans.get(position).getMoneyOutCat());

            //moneyOutAmount and format as currency
            try {
                moneyOutS = (String.valueOf(cashTrans.get(position).getMoneyOutAmount()));
                if (moneyOutS != null && !moneyOutS.equals("")) {
                    moneyOutD = Double.valueOf(moneyOutS);
                } else {
                    moneyOutD = 0.0;
                }
                moneyOut2 = currencyFormat.format(moneyOutD);
                holder.moneyOutAmount.setText(moneyOut2);
            } catch (NumberFormatException e) {
                holder.moneyOutAmount.setText(moneyOut2);
            }

            holder.moneyOutDelete.setTag(cashTrans.get(position));
            holder.moneyOutEdit.setTag(cashTrans.get(position));

            //click on pencil icon
            holder.moneyOutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.moneyOutEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    moneyOutDbManager = new MoneyOutDbManager(getContext());

                    moneyOutCatText.setVisibility(View.VISIBLE);
                    moneyOutAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyOutEntryButton.setVisibility(View.VISIBLE);
                    moneyOutLine.setVisibility(View.VISIBLE);

                    moneyOutCatText.setText(moneyOutDb.getMoneyOutCat());

                    moneyOutAmountD = moneyOutDb.getMoneyOutAmount();
                    moneyOutAmountS = currencyFormat.format(moneyOutAmountD);
                    moneyOutAmountEditText.setText(moneyOutAmountS);

                    oldMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);

                    updateMoneyOutEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                moneyOutDb.setMoneyOutAmount(Double.valueOf(moneyOutAmountEditText.getText().toString()));
                                newMoneyOutAmount = Double.valueOf(moneyOutAmountEditText.getText().toString());
                            } catch (NumberFormatException e) {
                                moneyOutDb.setMoneyOutAmount(general.extractingDollars(moneyOutAmountEditText));
                                newMoneyOutAmount = general.extractingDollars(moneyOutAmountEditText);
                            }

                            moneyOutAmount = newMoneyOutAmount - oldMoneyOutAmount;

                            moneyOutDbManager.updateMoneyOut(moneyOutDb);
                            moneyOutAdapter.updateCashTrans(moneyOutDbManager.getCashTrans());
                            notifyDataSetChanged();

                            Toast.makeText(getContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            moneyOutCatText.setVisibility(View.GONE);
                            moneyOutAmountEditText.setVisibility(View.GONE);
                            cancelMoneyOutEntryButton.setVisibility(View.GONE);
                            updateMoneyOutEntryButton.setVisibility(View.GONE);
                            moneyOutLine.setVisibility(View.GONE);

                            updateCurrentAccountBalanceMoneyOut();
                            updateCurrentAvailableBalanceMoneyOut();

                            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily2);
                        }
                    });

                    cancelMoneyOutEntryButton.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {
                            moneyOutCatText.setVisibility(View.GONE);
                            moneyOutAmountEditText.setVisibility(View.GONE);
                            cancelMoneyOutEntryButton.setVisibility(View.GONE);
                            updateMoneyOutEntryButton.setVisibility(View.GONE);
                            moneyOutLine.setVisibility(View.GONE);

                            backToDaily3 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily3);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyOutDelete.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    moneyOutAmount = -(Double.valueOf(cashTrans.get(position).getMoneyOutAmount()));

                    moneyOutDb = (MoneyOutDb) holder.moneyOutDelete.getTag();
                    moneyOutDbManager.deleteMoneyOut(moneyOutDb);
                    moneyOutAdapter.updateCashTrans(moneyOutDbManager.getCashTrans());
                    notifyDataSetChanged();

                    updateCurrentAvailableBalanceMoneyOut();
                    updateCurrentAccountBalanceMoneyOut();

                    backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
                    backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(backToDaily);
                }
            });

            return convertView;
        }
    }

    private static class MoneyOutViewHolder {
        public TextView moneyOutDate;
        public TextView moneyOutCat;
        public TextView moneyOutAmount;
        ImageButton moneyOutEdit;
        ImageButton moneyOutDelete;
    }
}
