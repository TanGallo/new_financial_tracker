package ca.gotchasomething.mynance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import ca.gotchasomething.mynance.tabFragments.DailyMoneyInOut;

public class HeaderDailyMoney extends LayoutDailyMoney {

    @Override
    public Double retrieveMoneyInAmount() {
        return super.retrieveMoneyInAmount();
    }

    @Override
    public Double retrieveMoneyOutAmount() {
        return super.retrieveMoneyOutAmount();
    }

    @Override
    public Double moneyInB() {
        return super.moneyInB();
    }

    @Override
    public Double retrieveMoneyOutBAmount() {
        return super.retrieveMoneyOutBAmount();
    }

    /*@Override
    public Double retrieveStartingBalance() {
        return super.retrieveStartingBalance();
    }

    @Override
    public Double retrieveIncomeTotal() {
        return super.retrieveIncomeTotal();
    }

    @Override
    public Double retrieveSpentFromAccountTotal() {
        return super.retrieveSpentFromAccountTotal();
    }

    @Override
    public Double retrieveBPercentage() {
        return super.retrieveBPercentage();
    }

    @Override
    public Double retrieveBSpent() {
        return super.retrieveBSpent();
    }*/

    @Override
    public void dailyHeaderText() {
        super.dailyHeaderText();
    }
}
