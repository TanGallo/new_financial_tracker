package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.text.NumberFormat;

public class General {

    public Double dollars,startingAmount;
    public int startIndex, startIndex2;
    public int endIndex, endIndex2;
    public NumberFormat currencyFormat;
    public String startingString, subStringResult, subStringResult2;
    public TextView tv, tv2;

    public Double extractingDollars(TextView tv) {

        startingString = tv.getText().toString();
        if(!startingString.contains(",")) {

            startIndex = startingString.indexOf("$") + 1;
            endIndex = startingString.length();
            subStringResult = startingString.substring(startIndex, endIndex);
            dollars = Double.parseDouble(subStringResult);

        } else {
            startIndex = startingString.indexOf("$") + 1;
            endIndex = startingString.length();
            subStringResult = startingString.substring(startIndex, endIndex);
            subStringResult2 = subStringResult.replace(",", "");
            dollars = Double.parseDouble(subStringResult2);
        }

        return dollars;
    }

}
