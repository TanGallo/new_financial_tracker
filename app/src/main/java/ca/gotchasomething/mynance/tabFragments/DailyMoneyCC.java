package ca.gotchasomething.mynance.tabFragments;

import android.content.Context;
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
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyCC extends Fragment {

    boolean possible = true;
    Button ccTransButton, cancelCCTransEntryButton, updateCCTransEntryButton;
    CCTransAdapter ccTransAdapter;
    Cursor moneyOutCursor2;
    Date moneyOutDate;
    DbHelper moneyOutDbHelper2;
    DbManager dbManager;
    Double moneyOutAmount, ccTransAmountD, oldMoneyOutAmount, newMoneyOutAmount, ccTransAmountD2;
    EditText ccTransAmountText, ccTransAmountEditText;
    General general;
    int moneyOutToPay, moneyOutPaid;
    ListView ccTransList;
    long moneyOutRefKeyMO, expRefKeyMO;
    MoneyOutDb moneyOutDb;
    MoneyOutSpinnerAdapter ccTransSpinnerAdapter;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat moneyOutSDF;
    Spinner ccTransCatSpinner;
    SQLiteDatabase moneyOutDbDb2;
    String moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutCreatedOn, moneyOutCC, ccTransCatS, ccTransPriorityS, moneyOutWeeklyS, ccTransAmountS,
            ccTransAmount2, ccTransAmountS2;
    TextView ccTransCatText;
    Timestamp moneyOutTimestamp;
    View v, ccTransLine;

    public DailyMoneyCC() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_cc, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = new General();

        ccTransAmountText = v.findViewById(R.id.ccTransAmount);
        ccTransButton = v.findViewById(R.id.ccTransButton);
        ccTransList = v.findViewById(R.id.ccTransList);
        ccTransCatText = v.findViewById(R.id.ccTransCatText);
        ccTransCatText.setVisibility(View.GONE);
        ccTransAmountEditText = v.findViewById(R.id.ccTransAmountEditText);
        ccTransAmountEditText.setVisibility(View.GONE);
        cancelCCTransEntryButton = v.findViewById(R.id.cancelCCTransEntryButton);
        cancelCCTransEntryButton.setVisibility(View.GONE);
        updateCCTransEntryButton = v.findViewById(R.id.updateCCTransEntryButton);
        updateCCTransEntryButton.setVisibility(View.GONE);
        ccTransLine = v.findViewById(R.id.ccTransLine);
        ccTransLine.setVisibility(View.GONE);

        ccTransButton.setOnClickListener(onClickCCTransButton);

        dbManager = new DbManager(getContext());
        ccTransAdapter = new CCTransAdapter(getContext(), dbManager.getCCTrans());
        ccTransList.setAdapter(ccTransAdapter);

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        moneyOutDbHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutDbHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT * FROM " + DbHelper.EXPENSES_TABLE_NAME + " ORDER BY " + DbHelper.EXPENSENAME + " ASC", null);
        ccTransSpinnerAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor2);
        ccTransCatSpinner.setAdapter(ccTransSpinnerAdapter);

        ccTransCatSpinner.setOnItemSelectedListener(ccTransSpinnerSelection);

    }

    AdapterView.OnItemSelectedListener ccTransSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ccTransCatS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            ccTransPriorityS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));
            moneyOutWeeklyS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSEWEEKLY));
            moneyOutRefKeyMO = moneyOutCursor2.getLong(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.ID));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener onClickCCTransButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = ccTransCatS;
            moneyOutPriority = ccTransPriorityS;
            moneyOutWeekly = moneyOutWeeklyS;
            moneyOutAmount = Double.valueOf(ccTransAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "Y";
            moneyOutToPay = 0;
            moneyOutPaid = 0;
            expRefKeyMO = moneyOutRefKeyMO;

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutWeekly, moneyOutAmount, moneyOutCreatedOn,
                    moneyOutCC, moneyOutToPay, moneyOutPaid, expRefKeyMO,  0);

            dbManager.addMoneyOut(moneyOutDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            ccTransAmountText.setText("");
            ccTransCatSpinner.setSelection(0, false);

            ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
            ccTransAdapter.notifyDataSetChanged();
        }
    };

    public class CCTransAdapter extends ArrayAdapter<MoneyOutDb> {

        private Context context;
        private List<MoneyOutDb> ccTrans;

        private CCTransAdapter(
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

            final CCTransViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fragment_list_daily_money_in_out,
                        parent, false);

                holder = new CCTransViewHolder();
                holder.ccTransEdit = convertView.findViewById(R.id.editMoneyInOutButton);
                holder.ccTransDelete = convertView.findViewById(R.id.deleteMoneyInOutButton);
                holder.ccTransAmount = convertView.findViewById(R.id.moneyInOutAmount);
                holder.ccTransCat = convertView.findViewById(R.id.moneyInOutCat);
                holder.ccTransDate = convertView.findViewById(R.id.moneyInOutDate);
                convertView.setTag(holder);

            } else {
                holder = (CCTransViewHolder) convertView.getTag();
            }

            //retrieve moneyOutCreatedOn
            holder.ccTransDate.setText(ccTrans.get(position).getMoneyOutCreatedOn());

            //retrieve ccCat
            holder.ccTransCat.setText(ccTrans.get(position).getMoneyOutCat());

            //retrieve ccAmount and format as currency
            try {
                ccTransAmountS = (String.valueOf(ccTrans.get(position).getMoneyOutAmount()));
                if (ccTransAmountS != null && !ccTransAmountS.equals("")) {
                    ccTransAmountD = Double.valueOf(ccTransAmountS);
                } else {
                    ccTransAmountD = 0.0;
                }
                ccTransAmount2 = currencyFormat.format(ccTransAmountD);
                holder.ccTransAmount.setText(ccTransAmount2);
            } catch (NumberFormatException e) {
                holder.ccTransAmount.setText(ccTransAmount2);
            }

            holder.ccTransDelete.setTag(ccTrans.get(position));
            holder.ccTransEdit.setTag(ccTrans.get(position));

            //click on pencil icon
            holder.ccTransEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneyOutDb = (MoneyOutDb) holder.ccTransEdit.getTag();

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dbManager = new DbManager(getContext());

                    ccTransCatText.setVisibility(View.VISIBLE);
                    ccTransAmountEditText.setVisibility(View.VISIBLE);
                    cancelCCTransEntryButton.setVisibility(View.VISIBLE);
                    updateCCTransEntryButton.setVisibility(View.VISIBLE);
                    ccTransLine.setVisibility(View.VISIBLE);

                    ccTransCatText.setText(moneyOutDb.getMoneyOutCat());

                    ccTransAmountD2 = moneyOutDb.getMoneyOutAmount();
                    ccTransAmountS2 = currencyFormat.format(ccTransAmountD2);
                    ccTransAmountEditText.setText(ccTransAmountS2);

                    oldMoneyOutAmount = general.extractingDollars(ccTransAmountEditText);
                }
            });

            updateCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        moneyOutDb.setMoneyOutAmount(Double.valueOf(ccTransAmountEditText.getText().toString()));
                        newMoneyOutAmount = Double.valueOf(ccTransAmountEditText.getText().toString());
                    } catch(NumberFormatException e) {
                        moneyOutDb.setMoneyOutAmount(general.extractingDollars(ccTransAmountEditText));
                        newMoneyOutAmount = general.extractingDollars(ccTransAmountEditText);
                    }

                    moneyOutAmount = newMoneyOutAmount - oldMoneyOutAmount;

                    dbManager.updateMoneyOut(moneyOutDb);
                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    notifyDataSetChanged();

                    Toast.makeText(getContext(), "Your changes have been saved",
                            Toast.LENGTH_LONG).show();

                    ccTransCatText.setVisibility(View.GONE);
                    ccTransAmountEditText.setVisibility(View.GONE);
                    cancelCCTransEntryButton.setVisibility(View.GONE);
                    updateCCTransEntryButton.setVisibility(View.GONE);
                    ccTransLine.setVisibility(View.GONE);
                }
            });

            cancelCCTransEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ccTransCatText.setVisibility(View.GONE);
                    ccTransAmountEditText.setVisibility(View.GONE);
                    cancelCCTransEntryButton.setVisibility(View.GONE);
                    updateCCTransEntryButton.setVisibility(View.GONE);
                    ccTransLine.setVisibility(View.GONE);
                }
            });

            //click on trash can icon
            holder.ccTransDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moneyOutDb = (MoneyOutDb) holder.ccTransDelete.getTag();
                    dbManager.deleteMoneyOut(moneyOutDb);
                    ccTransAdapter.updateCCTrans(dbManager.getCCTrans());
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class CCTransViewHolder {
        public TextView ccTransCat;
        public TextView ccTransAmount;
        public TextView ccTransDate;
        ImageButton ccTransDelete;
        ImageButton ccTransEdit;
    }
}
