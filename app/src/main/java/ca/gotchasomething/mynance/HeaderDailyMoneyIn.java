package ca.gotchasomething.mynance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.text.NumberFormat;

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.data.MoneyInDbManager;

public class HeaderDailyMoneyIn extends LayoutDailyMoney {

    Cursor headerMoneyInCursor, headerMoneyOutCursor;
    DbHelper headerMoneyInHelper, headerMoneyOutHelper;
    Double currentAccountBalance, currentAvailableBalance, thisMoneyInAmount, newAccountBalance;
    int startIndex, startIndex2, endIndex, endIndex2;
    MoneyInDbManager moneyInDbManager;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    SQLiteDatabase headerMoneyInDb, headerMoneyOutDb;
    String accountNumber, accountNumberResult, availableNumber, availableNumberResult, newAccountBalanceS;
    TextView totalAccountText, availableAccountText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moneyInDbManager = new MoneyInDbManager(this);
        //openOrCreateDatabase("Mynance.db", MODE_PRIVATE, null);

        totalAccountText = findViewById(R.id.totalAccountText);
        availableAccountText = findViewById(R.id.availableAccountText);

        accountNumber = totalAccountText.getText().toString();
        startIndex = accountNumber.indexOf("$") + 1;
        endIndex = accountNumber.length();
        accountNumberResult = accountNumber.substring(startIndex, endIndex);
        currentAccountBalance = Double.parseDouble(accountNumberResult);

        availableNumber = availableAccountText.getText().toString();
        startIndex2 = availableNumber.indexOf("$") + 1;
        endIndex2 = availableNumber.length();
        availableNumberResult = availableNumber.substring(startIndex2, endIndex2);
        currentAvailableBalance = Double.parseDouble(availableNumberResult);

        updateMoneyIn();

    }

    public void updateMoneyIn() {

        newAccountBalance = currentAccountBalance + thisMoneyInAmount;
        newAccountBalanceS = currencyFormat.format(newAccountBalance);
        totalAccountText.setText(newAccountBalanceS);
    }

    /*public void retrieveMoneyIn() {

        headerMoneyInHelper = new DbHelper(this);
        headerMoneyInDb = headerMoneyInHelper.getReadableDatabase();
        headerMoneyInCursor = headerMoneyInDb.rawQuery("SELECT " + DbHelper.MONEYINAMOUNT + " FROM " + DbHelper.MONEY_IN_TABLE_NAME +
                " WHERE " + DbHelper.ID + " = (SELECT max(id) FROM " + DbHelper.MONEY_IN_TABLE_NAME + ")", null);
        headerMoneyInCursor.moveToFirst();
        thisMoneyInAmount = headerMoneyInCursor.getDouble(0);
        headerMoneyInCursor.close();

        newAccountBalance = currentAccountBalance + thisMoneyInAmount;
        newAccountBalanceS = currencyFormat.format(newAccountBalance);
        totalAccountText.setText(newAccountBalanceS);

    }*/
}
