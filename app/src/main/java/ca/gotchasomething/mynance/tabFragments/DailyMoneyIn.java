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
import ca.gotchasomething.mynance.DbManager;
import ca.gotchasomething.mynance.General;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;

public class DailyMoneyIn extends Fragment {

    Button moneyInButton, cancelMoneyInEntryButton, updateMoneyInEntryButton;
    ContentValues moneyInValue, moneyInValue2;
    Cursor cursor2;
    Date moneyInDate;
    DbHelper dbHelper2, dbHelper3, dbHelper4;
    DbManager dbManager;
    Double moneyInAmount, moneyInD, newAccountBalance, oldAccountBalance, percentB, oldAvailableBalance, newAvailableBalance,
            newMoneyInAmount, oldMoneyInAmount, moneyInAmountD;
    EditText moneyInAmountText, moneyInAmountEditText;
    General general;
    Intent backToDaily, backToDaily2, backToDaily3, backToDaily4;
    ListView moneyInList;
    MoneyInAdapter moneyInAdapter;
    MoneyInDb moneyInDb;
    MoneyInSpinnerAdapter moneyInSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyInSDF;
    Spinner moneyInCatSpinner;
    SQLiteDatabase db2, db3, db4;
    String moneyInCatS, moneyInCat, moneyInCreatedOn, moneyInS, moneyIn2, moneyInAmountS;
    TextView moneyInCatText;
    Timestamp moneyInTimestamp;
    View v, moneyInLine;

    public DailyMoneyIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_in, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();
        dbManager = new DbManager(getContext());

        moneyInAmountText = v.findViewById(R.id.moneyInAmount);
        moneyInButton = v.findViewById(R.id.moneyInButton);
        moneyInList = v.findViewById(R.id.moneyInList);
        moneyInCatText = v.findViewById(R.id.moneyInCatText);
        moneyInCatText.setVisibility(View.GONE);
        moneyInAmountEditText = v.findViewById(R.id.moneyInAmountEditText);
        moneyInAmountEditText.setVisibility(View.GONE);
        cancelMoneyInEntryButton = v.findViewById(R.id.cancelMoneyInEntryButton);
        cancelMoneyInEntryButton.setVisibility(View.GONE);
        updateMoneyInEntryButton = v.findViewById(R.id.updateMoneyInEntryButton);
        updateMoneyInEntryButton.setVisibility(View.GONE);
        moneyInLine = v.findViewById(R.id.moneyInLine);
        moneyInLine.setVisibility(View.GONE);

        moneyInButton.setOnClickListener(onClickMoneyInButton);

        moneyInAdapter = new MoneyInAdapter(getContext(), dbManager.getMoneyIns());
        moneyInList.setAdapter(moneyInAdapter);

        moneyInCatSpinner = v.findViewById(R.id.moneyInCatSpinner);
        dbHelper2 = new DbHelper(getContext());
        db2 = dbHelper2.getReadableDatabase();
        cursor2 = db2.rawQuery("SELECT * FROM " + DbHelper.INCOME_TABLE_NAME + " ORDER BY " + DbHelper.INCOMENAME + " ASC", null);
        moneyInSpinnerAdapter = new MoneyInSpinnerAdapter(getContext(), cursor2);
        moneyInCatSpinner.setAdapter(moneyInSpinnerAdapter);

        moneyInCatSpinner.setOnItemSelectedListener(moneyInSpinnerSelection);

    }

    public void updateCurrentAvailableBalanceMoneyIn() {
        percentB = dbManager.retrieveBPercentage();
        oldAvailableBalance = dbManager.retrieveCurrentAvailableBalance();
        newAvailableBalance = oldAvailableBalance + (moneyInAmount * percentB);
        moneyInValue2 = new ContentValues();
        moneyInValue2.put(DbHelper.CURRENTAVAILABLEBALANCE, newAvailableBalance);
        dbHelper3 = new DbHelper(getContext());
        db3 = dbHelper3.getWritableDatabase();
        db3.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue2, DbHelper.ID + "= '1'", null);
        db3.close();

    }

    public void updateCurrentAccountBalanceMoneyIn() {
        oldAccountBalance = dbManager.retrieveCurrentAccountBalance();
        newAccountBalance = oldAccountBalance + moneyInAmount;
        moneyInValue = new ContentValues();
        moneyInValue.put(DbHelper.CURRENTACCOUNTBALANCE, newAccountBalance);
        dbHelper4 = new DbHelper(getContext());
        db4 = dbHelper4.getWritableDatabase();
        db4.update(DbHelper.CURRENT_TABLE_NAME, moneyInValue, DbHelper.ID + "= '1'", null);
        db4.close();

    }

    AdapterView.OnItemSelectedListener moneyInSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyInCatS = cursor2.getString(cursor2.getColumnIndexOrThrow(DbHelper.INCOMENAME));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onClickMoneyInButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyInCat = moneyInCatS;
            moneyInAmount = Double.valueOf(moneyInAmountText.getText().toString());
            moneyInDate = new Date();
            moneyInTimestamp = new Timestamp(moneyInDate.getTime());
            moneyInSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyInCreatedOn = moneyInSDF.format(moneyInTimestamp);

            moneyInDb = new MoneyInDb(moneyInCat, moneyInAmount, moneyInCreatedOn, 0);

            dbManager.addMoneyIn(moneyInDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyInAmountText.setText("");
            moneyInCatSpinner.setSelection(0, false);

            moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
            moneyInAdapter.notifyDataSetChanged();

            updateCurrentAccountBalanceMoneyIn();
            updateCurrentAvailableBalanceMoneyIn();

            backToDaily = new Intent(getContext(), LayoutDailyMoney.class);
            backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily);
        }
    };

    public class MoneyInAdapter extends ArrayAdapter<MoneyInDb> {

        private Context context;
        private List<MoneyInDb> moneyIn;

        private MoneyInAdapter(
                Context context,
                List<MoneyInDb> moneyIn) {

            super(context, -1, moneyIn);

            this.context = context;
            this.moneyIn = moneyIn;
        }

        public void updateMoneyIn(List<MoneyInDb> moneyIn) {
            this.moneyIn = moneyIn;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return moneyIn.size();
        }

        @NonNull
        @Override
        public View getView(final int position,
                            View convertView, @NonNull ViewGroup parent) {

            final MoneyInViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new MoneyInViewHolder();
                holder.moneyInEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.moneyInDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.moneyInAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.moneyInCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.moneyInDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (MoneyInViewHolder) convertView.getTag();
            }

            //retrieve moneyInCreatedOn
            holder.moneyInDate.setText(moneyIn.get(position).getMoneyInCreatedOn());

            //retrieve moneyInCat
            holder.moneyInCat.setText(moneyIn.get(position).getMoneyInCat());

            //moneyInAmount and format as currency
            try {
                moneyInS = (String.valueOf(moneyIn.get(position).getMoneyInAmount()));
                if (moneyInS != null && !moneyInS.equals("")) {
                    moneyInD = Double.valueOf(moneyInS);
                } else {
                    moneyInD = 0.0;
                }
                moneyIn2 = currencyFormat.format(moneyInD);
                holder.moneyInAmount.setText(moneyIn2);
            } catch (NumberFormatException e) {
                holder.moneyInAmount.setText(moneyIn2);
            }

            holder.moneyInDelete.setTag(moneyIn.get(position));
            holder.moneyInEdit.setTag(moneyIn.get(position));

            //click on pencil icon
            holder.moneyInEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInDb = (MoneyInDb) holder.moneyInEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    moneyInCatText.setVisibility(View.VISIBLE);
                    moneyInAmountEditText.setVisibility(View.VISIBLE);
                    cancelMoneyInEntryButton.setVisibility(View.VISIBLE);
                    updateMoneyInEntryButton.setVisibility(View.VISIBLE);
                    moneyInLine.setVisibility(View.VISIBLE);

                    moneyInCatText.setText(moneyInDb.getMoneyInCat());

                    moneyInAmountD = moneyInDb.getMoneyInAmount();
                    moneyInAmountS = currencyFormat.format(moneyInAmountD);
                    moneyInAmountEditText.setText(moneyInAmountS);

                    oldMoneyInAmount = general.extractingDollars(moneyInAmountEditText);

                    updateMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                moneyInDb.setMoneyInAmount(Double.valueOf(moneyInAmountEditText.getText().toString()));
                                newMoneyInAmount = Double.valueOf(moneyInAmountEditText.getText().toString());
                            } catch (NumberFormatException e) {
                                moneyInDb.setMoneyInAmount(general.extractingDollars(moneyInAmountEditText));
                                newMoneyInAmount = general.extractingDollars(moneyInAmountEditText);
                            }

                            moneyInAmount = newMoneyInAmount - oldMoneyInAmount;

                            dbManager.updateMoneyIn(moneyInDb);
                            moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
                            notifyDataSetChanged();

                            Toast.makeText(getContext(), "Your changes have been saved",
                                    Toast.LENGTH_LONG).show();

                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            updateCurrentAccountBalanceMoneyIn();
                            updateCurrentAvailableBalanceMoneyIn();

                            backToDaily2 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily2);
                        }
                    });

                    cancelMoneyInEntryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moneyInCatText.setVisibility(View.GONE);
                            moneyInAmountEditText.setVisibility(View.GONE);
                            cancelMoneyInEntryButton.setVisibility(View.GONE);
                            updateMoneyInEntryButton.setVisibility(View.GONE);
                            moneyInLine.setVisibility(View.GONE);

                            backToDaily3 = new Intent(getContext(), LayoutDailyMoney.class);
                            backToDaily3.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(backToDaily3);
                        }
                    });
                }
            });

            //click on trash can icon
            holder.moneyInDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyInAmount = -(Double.valueOf(moneyIn.get(position).getMoneyInAmount()));

                    moneyInDb = (MoneyInDb) holder.moneyInDelete.getTag();
                    dbManager.deleteMoneyIn(moneyInDb);
                    moneyInAdapter.updateMoneyIn(dbManager.getMoneyIns());
                    notifyDataSetChanged();

                    updateCurrentAvailableBalanceMoneyIn();
                    updateCurrentAccountBalanceMoneyIn();

                    backToDaily4 = new Intent(getContext(), LayoutDailyMoney.class);
                    backToDaily4.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(backToDaily4);
                }
            });

            return convertView;
        }
    }

    private static class MoneyInViewHolder {
        public TextView moneyInDate;
        public TextView moneyInCat;
        public TextView moneyInAmount;
        ImageButton moneyInEdit;
        ImageButton moneyInDelete;
    }
}
