package ca.gotchasomething.mynance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.text.NumberFormat;

public class General {

    public Double dollars, percent, percentD, percentDD;
    public int startIndex, startIndex2;
    public int endIndex, endIndex2;
    public String startingString, startingString2, subStringResult, subStringResult2, subStringResult3, percentS;
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

    public Double extractingPercents(TextView tv2) {

        try {
            startingString2 = tv2.getText().toString();
            percentS = startingString2.replace("%","");
            percent = Double.parseDouble(percentS);
        } catch (NumberFormatException e) {
            percent = 0.0;
        }

        return percent;
    }

}
