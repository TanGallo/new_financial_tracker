package ca.gotchasomething.mynance;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class General extends AppCompatActivity {

    public boolean alreadyDetermined,
            leapYear;
    public Calendar debtCal,
            savingsCal;
    public Context context;
    public Date date,
            dateObj,
            debtEndD,
            savingsDateD;
    public Double dbl = 0.0,
            dbl2 = 0.0,
            dObj = 0.0,
            dollars = 0.0,
            numberOfYearsToPayDebt = 0.0,
            percent = 0.0,
            years = 0.0,
            years2 = 0.0;
    public float f1;
    public int position = 0,
            startIndex = 0,
            endIndex = 0,
            numberOfDaysToPayDebt = 0,
            numberOfDaysToSavingsGoal = 0;
    public Intent in;
    public List<String> lastNumOfDays,
            thisWeek,
            yearsList;
    public NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    public SimpleDateFormat sdf,
            debtEndS,
            savingsDateS;
    public String createdOn = null,
            startingString = null,
            startingString2 = null,
            string1 = null,
            str2 = null,
            subStringResult = null,
            subStringResult2 = null,
            percentS = null,
            debtEnd = null,
            savingsDate = null,
            savingsDateB = null;
    String[] yearsList2;
    public Timestamp timestamp;

    public Double dollarsFromTV(TextView tv) {
        //TV that contains dollars
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

    public Double percentFromTV(TextView tv2) {
        //TV that contains percent
        try {
            startingString2 = tv2.getText().toString();
            percentS = startingString2.replace("%", "");
            percent = Double.parseDouble(percentS);
        } catch (NumberFormatException e) {
            percent = 0.0;
        }

        return percent;
    }

    public Date dateFromString(String str1) {
        //str1 = date as string
        String string = str1;
        try {
            dateObj = new SimpleDateFormat("dd-MMM-yyyy").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    public boolean checkForLeapYear(int int1) {
        //int1 = year from selection
        leapYear = false;
        if (int1 % 400 == 0) {
            leapYear = true;
        } else if (int1 % 100 == 0) {
            leapYear = false;
        } else if (int1 % 4 == 0) {
            leapYear = true;
        } else {
            leapYear = false;
        }

        return leapYear;
    }

    public void extractingDates(List<String> dateList1, List<Date> dateList2) {
        //list that contains dates from Db
        //new list with new dates from 1st list
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

    public String stringFromET(EditText et1) {
        //ET that contains string
        if (et1.getText().toString().equals("")) {

            string1 = "null";
        } else {
            string1 = et1.getText().toString();
        }
        return string1;
    }

    public Double dblFromET(EditText et3) {
        //ET that contains number
        if (et3.getText().toString().equals("")) {
            dbl = 0.0;
        } else {
            try {
                dbl = Double.valueOf(et3.getText().toString());
            } catch (NumberFormatException e2) {
                dbl = dollarsFromTV(et3);
            }
        }
        return dbl;
    }

    public Double percentFromET(EditText et4) {
        //ET that contains percent
        if (et4.getText().toString().equals("")) {
            percent = 0.0;
        } else {
            try {
                percent = Double.valueOf(et4.getText().toString());
            } catch (NumberFormatException e3) {
                percent = percentFromTV(et4);
            }
        }
        return percent;
    }

    public void dblASCurrency(String str1, TextView tv1) {
        //string that came from holder's ET
        //textView that will hold final currency amount
        if (!str1.equals("")) {
            dbl2 = Double.valueOf(str1);
        } else {
            dbl2 = 0.0;
        }
        str2 = currencyFormat.format(dbl2);
        tv1.setText(str2);
    }

    public String calcDebtDate(Double dbl1, Double dbl2, Double dbl3, String str1, String str2) {
        //dbl1 = debtOwing
        //dbl2 = debtRate
        //dbl3 = debtPayments
        //str1 = debt_paid
        //str2 = too_far

        if (dbl1 <= 0) { //debtOwing = 0
            debtEnd = str1;
        } else if (dbl3 == 0.0) { //debtPayts = 0
            debtEnd = str2;
        } else if (dbl3 < ((dbl1 * ((dbl2 / 100) / 365)) * 30)) { //payts are less than the interest
            debtEnd = str2;
        } else if (dbl2 == 0.0) { //debtRate = 0
            numberOfYearsToPayDebt = dbl1 / (dbl3 * 12.0); //years = amount owing / annual payments
            numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);
            if (numberOfDaysToPayDebt <= 0) {
                debtEnd = str1;
            } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfYearsToPayDebt.isNaN()) {
                debtEnd = str2;
            } else {
                debtCal = Calendar.getInstance();
                debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
                debtEndD = debtCal.getTime();
                debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
                debtEnd = debtEndS.format(debtEndD);
            }
        } else { //3 values are not 0
            numberOfYearsToPayDebt = -(Math.log(1 - (dbl1 * (dbl2 / 100) / (dbl3 * 12.0))) / (12.0 * Math.log(1 + ((dbl2 / 100) / 12.0))));
            numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);
            if (numberOfDaysToPayDebt <= 0) {
                debtEnd = str1;
            } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfYearsToPayDebt.isNaN()) {
                debtEnd = str2;
            } else {
                debtCal = Calendar.getInstance();
                debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
                debtEndD = debtCal.getTime();
                debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
                debtEnd = debtEndS.format(debtEndD);
            }
        }
        return debtEnd;
    }

    public String calcSavingsDate(Double dbl1, Double dbl2, Double dbl4, Double dbl5, String
            str1, String str2) {
        //savingsGoal
        //savingsAmount
        //savingsRate
        //savingsPayments
        //goal_achieved
        //too_far

        alreadyDetermined = false;

        if (dbl2 == 0.0) {
            dbl2 = .01;
        }
        if (dbl5 == 0.0) {
            dbl5 = .01;
        }

        if (dbl1 == 0.0 || dbl1 <= dbl2) { //if goal is 0 or <= amount then goal achieved
            savingsDateB = str1;
            alreadyDetermined = true;
        } else if (dbl2 == 0.0) { //if amount is 0
            if (dbl5 == 0.0 || dbl5 == .01 || (dbl4 / 100) == 0.0) { //if payments are 0 or rate is 0 then too far
                savingsDateB = str2;
                alreadyDetermined = true;
            }
        } else if ((dbl4 / 100) == 0.0) { //if rate is 0
            years = ((dbl1 - dbl2) / (dbl5 * 12.0)); //years = amount left until goal / annual contribution
            numberOfDaysToSavingsGoal = (int) Math.round(years * 365);
            alreadyDetermined = false;
        } else {
            years2 = 0.0;
            alreadyDetermined = false;
            if (dbl2 == 0.0) {
                dbl2 = .01;
            }
            if (dbl5 == 0.0) {
                dbl5 = .01;
            }
            do {
                years2 = years2 + .00274;
            }
            while (dbl1 >= (dbl2 * (Math.pow((1 + (dbl4 / 100) / 12), 12 * years2))) + (((dbl5 * 12.0) / 12) * (((Math.pow((1 + (dbl4 / 100) / 12), 12 * years2)) - 1) / ((dbl4 / 100) / 12)) * (1 + (dbl4 / 100) / 12)));
            numberOfDaysToSavingsGoal = (int) Math.round(years2 * 365);
        }

        if (!alreadyDetermined) {
            if (years < 0 || years2 < 0 || years > 100 || years2 > 100) { //if years is a negative number then too far
                savingsDate = str2;
            } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal < 0) { //if number of days has too many digits or is < 0 then too far
                savingsDate = str2;
            } else if (numberOfDaysToSavingsGoal == 0) { //if number of days is 0 then goal achieved
                savingsDate = str1;
            } else {//else calculate date
                savingsCal = Calendar.getInstance();
                savingsCal.add(Calendar.DATE, numberOfDaysToSavingsGoal);
                savingsDateD = savingsCal.getTime();
                savingsDateS = new SimpleDateFormat("dd-MMM-yyyy");
                savingsDate = savingsDateS.format(savingsDateD);
            }
        } else {
            savingsDate = savingsDateB;
        }
        return savingsDate;
    }

    public String createTimestamp() {
        date = new Date();
        timestamp = new Timestamp(date.getTime());
        sdf = new SimpleDateFormat("dd-MMM-yyyy");
        createdOn = sdf.format(timestamp);

        return createdOn;
    }

    public List<String> lastNumOfDays(int int1) {
        //int1 = number of days in desired range
        lastNumOfDays = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -int1);
        for (int i = 0; i < int1; i++) {
            cal.add(Calendar.DATE, 1);
            lastNumOfDays.add(sdf.format(cal.getTime()));
        }

        return lastNumOfDays;
    }

    public float convertDblToFloat(Double dbl1) {
        //dbl1 = Double to be converted
        dObj = new Double(dbl1);
        f1 = dObj.floatValue();

        return f1;
    }

    public void buildLimitsPieChart(float fl1, float fl2, PieChart pc, int int1, int int2) {
        //fl1 = amtLeft
        //fl2 = amtSpent
        //pc = pieChart
        //int1 = color #1 (Color.parseColor("#NNNNNN")) - from colour resources list
        //int2 = color #2 (Color.parseColor("#NNNNNN")) - from colour resources list

        pc.setHoleRadius(0);
        pc.getDescription().setEnabled(false);
        pc.setMinAngleForSlices(1);
        pc.getLegend().setEnabled(false);
        pc.setDrawHoleEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(fl1, ""));
        entries.add(new PieEntry(fl2, ""));


        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);

        ArrayList<Integer> pieChartColours = new ArrayList<>();
        pieChartColours.add(int1); //light green
        pieChartColours.add(int2); //gray
        pieDataSet.setColors(pieChartColours);

        pc.setData(pieData);
        pc.invalidate();
    }

    public String[] yearsList(int int1, int int2) {
        //int1 = dbMgr.getEarliestEntry(dbMgr.getYearsList())
        //int2 = dbMgr.getLatestEntry(dbMgr.getYearsList())
        yearsList = new ArrayList<>();
        for (int firstYear = int1; firstYear <= int2; firstYear++) {
            yearsList.add(String.valueOf(firstYear));
        }
        yearsList2 = yearsList.toArray(new String[yearsList.size()]);

        return yearsList2;
    }
}

