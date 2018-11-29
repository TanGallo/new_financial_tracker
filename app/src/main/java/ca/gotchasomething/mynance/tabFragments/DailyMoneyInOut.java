package ca.gotchasomething.mynance.tabFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.gotchasomething.mynance.DbHelper;
import ca.gotchasomething.mynance.HeaderDailyMoney;
import ca.gotchasomething.mynance.LayoutDailyMoney;
import ca.gotchasomething.mynance.R;
import ca.gotchasomething.mynance.data.MoneyInDb;
import ca.gotchasomething.mynance.data.MoneyInDbManager;
import ca.gotchasomething.mynance.data.MoneyOutDb;
import ca.gotchasomething.mynance.data.MoneyOutDbManager;
import ca.gotchasomething.mynance.spinners.MoneyInSpinnerAdapter;
import ca.gotchasomething.mynance.spinners.MoneyOutSpinnerAdapter;

public class DailyMoneyInOut extends Fragment {

    View v;
    EditText moneyInAmountText, moneyOutAmountText, ccTransAmountText;
    Spinner moneyInCatSpinner, moneyOutCatSpinner, ccTransCatSpinner;
    DbHelper moneyInDbHelper, moneyOutDbHelper, moneyOutDbHelper2;
    SQLiteDatabase moneyInDbDb, moneyOutDbDb, moneyOutDbDb2;
    Cursor moneyInCursor, moneyOutCursor, moneyOutCursor2;
    MoneyInSpinnerAdapter moneyInAdapter;
    MoneyOutSpinnerAdapter moneyOutAdapter, moneyOutAdapter2;
    Button moneyInButton, moneyOutButton, ccTransButton;
    MoneyInDb moneyInDb;
    MoneyOutDb moneyOutDb, moneyOutDb2;
    String incomeName, moneyInCatS, moneyInCat, moneyInCreatedOn, moneyOutCatS, moneyOutCatS2, moneyOutCat, moneyOutPriority, moneyOutPriorityS, moneyOutPriorityS2, moneyOutCreatedOn,
            moneyOutCC, ccTransCatS, ccTransPriorityS, moneyOutCatD, moneyOutPriorityD;
    Double moneyInAmount, moneyOutAmount;
    MoneyInDbManager moneyInDbManager;
    MoneyOutDbManager moneyOutDbManager;
    Date moneyInDate, moneyOutDate;
    Timestamp moneyInTimestamp, moneyOutTimestamp;
    SimpleDateFormat moneyInSDF, moneyOutSDF;
    Intent backToDaily, backToDaily2, backToDaily3;
    int position;

    public DailyMoneyInOut() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_daily_money_in_out, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moneyInAmountText = v.findViewById(R.id.moneyInAmount);
        moneyInButton = v.findViewById(R.id.moneyInButton);
        moneyOutAmountText = v.findViewById(R.id.moneyOutAmount);
        moneyOutButton = v.findViewById(R.id.moneyOutButton);
        ccTransAmountText = v.findViewById(R.id.ccTransAmount);
        ccTransButton = v.findViewById(R.id.ccTransButton);

        moneyInButton.setOnClickListener(onClickMoneyInButton);
        moneyOutButton.setOnClickListener(onClickMoneyOutButton);
        ccTransButton.setOnClickListener(onClickCCTransButton);

        moneyInDbManager = new MoneyInDbManager(getContext());
        moneyOutDbManager = new MoneyOutDbManager(getContext());

        moneyInCatSpinner = v.findViewById(R.id.moneyInCatSpinner);
        moneyInDbHelper = new DbHelper(getContext());
        moneyInDbDb = moneyInDbHelper.getReadableDatabase();
        moneyInCursor = moneyInDbDb.rawQuery("SELECT * FROM " +
                DbHelper.INCOME_TABLE_NAME +
                " ORDER BY " +
                DbHelper.INCOMENAME +
                " ASC", null);
        moneyInAdapter = new MoneyInSpinnerAdapter(getContext(), moneyInCursor);
        moneyInCatSpinner.setAdapter(moneyInAdapter);

        moneyOutCatSpinner = v.findViewById(R.id.moneyOutCatSpinner);
        moneyOutDbHelper = new DbHelper(getContext());
        moneyOutDbDb = moneyOutDbHelper.getReadableDatabase();
        moneyOutCursor = moneyOutDbDb.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        moneyOutAdapter = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor);
        moneyOutCatSpinner.setAdapter(moneyOutAdapter);

        ccTransCatSpinner = v.findViewById(R.id.ccTransCatSpinner);
        moneyOutDbHelper2 = new DbHelper(getContext());
        moneyOutDbDb2 = moneyOutDbHelper2.getReadableDatabase();
        moneyOutCursor2 = moneyOutDbDb2.rawQuery("SELECT * FROM " +
                DbHelper.EXPENSES_TABLE_NAME +
                " ORDER BY " +
                DbHelper.EXPENSENAME +
                " ASC", null);
        moneyOutAdapter2 = new MoneyOutSpinnerAdapter(getContext(), moneyOutCursor2);
        ccTransCatSpinner.setAdapter(moneyOutAdapter2);

        moneyInCatSpinner.setOnItemSelectedListener(moneyInSpinnerSelection);
        moneyOutCatSpinner.setOnItemSelectedListener(moneyOutSpinnerSelection);
        ccTransCatSpinner.setOnItemSelectedListener(ccTransSpinnerSelection);

    }

    AdapterView.OnItemSelectedListener moneyInSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            moneyInCatS = moneyInCursor.getString(moneyInCursor.getColumnIndexOrThrow(DbHelper.INCOMENAME));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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

    AdapterView.OnItemSelectedListener ccTransSpinnerSelection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ccTransCatS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSENAME));
            ccTransPriorityS = moneyOutCursor2.getString(moneyOutCursor2.getColumnIndexOrThrow(DbHelper.EXPENSEPRIORITY));

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

            moneyInDbManager.addMoneyIn(moneyInDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyInAmountText.setText("");

            backToDaily = new Intent(getContext(), HeaderDailyMoney.class);
            backToDaily.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily);

            moneyInCatSpinner.setSelection(0, false);


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

            moneyOutDb = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn, moneyOutCC, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            moneyOutAmountText.setText("");

            backToDaily2 = new Intent(getContext(), HeaderDailyMoney.class);
            backToDaily2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(backToDaily2);

            moneyOutCatSpinner.setSelection(0, false);

        }
    };

    View.OnClickListener onClickCCTransButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            moneyOutCat = ccTransCatS;
            moneyOutPriority = ccTransPriorityS;
            moneyOutAmount = Double.valueOf(ccTransAmountText.getText().toString());
            moneyOutDate = new Date();
            moneyOutTimestamp = new Timestamp(moneyOutDate.getTime());
            moneyOutSDF = new SimpleDateFormat("dd-MMM-yyyy");
            moneyOutCreatedOn = moneyOutSDF.format(moneyOutTimestamp);
            moneyOutCC = "Y";

            moneyOutDb2 = new MoneyOutDb(moneyOutCat, moneyOutPriority, moneyOutAmount, moneyOutCreatedOn, moneyOutCC, 0);

            moneyOutDbManager.addMoneyOut(moneyOutDb2);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
            ccTransAmountText.setText("");

            ccTransCatSpinner.setSelection(0, false);

        }
    };
}
