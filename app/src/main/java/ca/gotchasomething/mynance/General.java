package ca.gotchasomething.mynance;

import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.gotchasomething.mynance.data.MoneyOutDb;

public class General {

    public Date dateObj;
    public Double dollars = 0.0, percent = 0.0;
    public int startIndex = 0, endIndex = 0;
    public List<String> thisWeek;
    public List<String> yearsList;
    public String startingString = null, startingString2 = null, subStringResult = null, subStringResult2 = null, subStringResult3 = null, percentS = null;
    public TextView tv, tv2;

    public Double extractingDollars(TextView tv) {

        startingString = tv.getText().toString();
        if (!startingString.contains(",")) {

            startIndex = startingString.indexOf("$") + 1;
            endIndex = startingString.length();
            subStringResult = startingString.substring(startIndex, endIndex);
            try {
                dollars = Double.parseDouble(subStringResult);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } else {
            startIndex = startingString.indexOf("$") + 1;
            endIndex = startingString.length();
            subStringResult = startingString.substring(startIndex, endIndex);
            subStringResult2 = subStringResult.replace(",", "");
            try {
                dollars = Double.parseDouble(subStringResult2);
            } catch (NumberFormatException e2) {
                e2.printStackTrace();
            }
        }

        return dollars;
    }

    public Double extractingPercents(TextView tv2) {

        try {
            startingString2 = tv2.getText().toString();
            percentS = startingString2.replace("%", "");
            percent = Double.parseDouble(percentS);
        } catch (NumberFormatException e) {
            percent = 0.0;
        }

        return percent;
    }

    public void extractingDates(List<String> dateList1, List<Date> dateList2) {

        for (String s : dateList1) {
            try {
                dateObj = new SimpleDateFormat("dd-MMM-yyyy").parse(s);
                dateList2.add(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> thisWeek() {

        thisWeek = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                thisWeek.add(sdf.format(today));
                break;
            case Calendar.TUESDAY:
                cal.add(Calendar.DAY_OF_WEEK, -2);
                for (int i = 0; i < 3; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
            case Calendar.WEDNESDAY:
                cal.add(Calendar.DAY_OF_WEEK, -3);
                for (int i = 0; i < 3; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
            case Calendar.THURSDAY:
                cal.add(Calendar.DAY_OF_WEEK, -4);
                for (int i = 0; i < 4; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
            case Calendar.FRIDAY:
                cal.add(Calendar.DAY_OF_WEEK, -5);
                for (int i = 0; i < 5; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
            case Calendar.SATURDAY:
                cal.add(Calendar.DAY_OF_WEEK, -6);
                for (int i = 0; i < 6; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
            case Calendar.SUNDAY:
                cal.add(Calendar.DAY_OF_WEEK, -7);
                for (int i = 0; i < 7; i++) {
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    thisWeek.add(sdf.format(cal.getTime()));
                }
                break;
        }

        return thisWeek;
    }

}

