package ca.gotchasomething.mynance;

import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class General {

    public boolean alreadyDetermined;
    public Calendar debtCal, savingsCal;
    public Date dateObj, debtEndD, savingsDateD;
    public Double dbl = 0.0, dollars = 0.0, percent = 0.0, numberOfYearsToPayDebt = 0.0, years = 0.0, years2 = 0.0, savingsPayments = 0.0, savingsRate = 0.0;
    public int startIndex = 0, endIndex = 0, numberOfDaysToPayDebt = 0, numberOfDaysToSavingsGoal = 0;
    public List<String> thisWeek;
    SimpleDateFormat debtEndS, savingsDateS;
    public String startingString = null, startingString2 = null, subStringResult = null, subStringResult2 = null, percentS = null, debtEnd = null, savingsDate = null, savingsDateB = null;

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

    public Double extractingDouble(TextView tv3) {
        if (tv3.getText().toString().equals("")) {
            dbl = 0.0;
        } else {
            try {
                dbl = Double.valueOf(tv3.getText().toString());
            } catch (NumberFormatException e2) {
                dbl = extractingDollars(tv3);
            }
        }
        return dbl;
    }

    public Double extractingPercent(TextView tv4) {
        if (tv4.getText().toString().equals("")) {
            percent = 0.0;
        } else {
            try {
                percent = Double.valueOf(tv4.getText().toString());
            } catch (NumberFormatException e3) {
                percent = extractingPercents(tv4);
            }
        }
        return percent;
    }

    public String calcDebtDate(
            Double dbl1, //debtAmount
            Double dbl2,  //debtRate
            Double dbl3,  //debtPayments
            Double dbl4,  //debtFrequency
            Double dbl5,  //debtAnnualIncome
            String str1,  //debt_paid
            String str2  //too_far
    ) {

        debtCal = Calendar.getInstance();
        if (dbl2 == 0) { //if rate is 0 then
            numberOfYearsToPayDebt = dbl1 / ((dbl3 * dbl4) - dbl5); //years = amount owing / annual payments minus annual income
        } else { //else calculate date
            numberOfYearsToPayDebt = -(Math.log(1 - (dbl1 * (dbl2 / 100) / ((dbl3 * dbl4) - dbl5))) / (dbl4 * Math.log(1 + ((dbl2 / 100) / dbl4))));
        } //-(Math.log(1 - (dbl1 * .19 / ((dbl3 * dbl4) - 1200))) / (dbl4 * Math.log(1 + ((dbl2 / 100) / dbl4))))

        numberOfDaysToPayDebt = (int) Math.round(numberOfYearsToPayDebt * 365);


        if ((dbl3 * dbl4) == dbl5) { //if annual income = annual payments then too far
            debtEnd = str2;
        } else if (dbl1 <= 0 || numberOfDaysToPayDebt == 0) { //if amount owing is <= 0 or number of days is 0 then debt paid
            debtEnd = str1;
        } else if (numberOfDaysToPayDebt > Integer.MAX_VALUE || numberOfDaysToPayDebt < 0) { //if number of days has too many digits or is < 0 then too far
            debtEnd = str2;
        } else { //else calculate date
            debtCal = Calendar.getInstance();
            debtCal.add(Calendar.DATE, numberOfDaysToPayDebt);
            debtEndD = debtCal.getTime();
            debtEndS = new SimpleDateFormat("dd-MMM-yyyy");
            debtEnd = debtEndS.format(debtEndD);
        }

        return debtEnd;
    }

    public void whatToShowDebt(String str1, String str2, TextView tv5, TextView tv6) {
        if (debtEnd.equals(str1) || debtEnd.equals(str2)) {
            tv5.setVisibility(View.GONE);
        } else {
            tv5.setVisibility(View.VISIBLE);
        }
        tv6.setText(debtEnd);
    }

    /*public Double findSavingsYears(
            Double dbl1, //savingsGoal
            Double dbl2, //savingsAmount
            Double dbl4,  //savingsRate
            Double dbl5,  //savingsPayments
            Double dbl7,  //savingsFrequency
            Double dbl8  //savingsAnnualIncome
    ) {

        if (dbl4 == 0) {
            years = ((dbl1 - dbl2) / ((dbl5 * dbl7) - dbl8));
        } else {
                years = 0.0;
                do {
                    years = years + .00274;
                }
                while (dbl1 >= (dbl2 * (Math.pow((1 + dbl4 / dbl6), dbl6 * years))) + ((((dbl5 * dbl7) - dbl8) / 12) * (((Math.pow((1 + dbl4 / dbl6), dbl6 * years)) - 1) / (dbl4 / dbl6)) * (1 + dbl4 / dbl6)));
            }
        return years;
    }*/

    /*years = 0.0;
            do {
        years = years + .00274;
    }
            while (goal >= (amount * (Math.pow((1 + rate / intFrequency), intFrequency * years))) + ((((payments * frequency) - incomeData()) / 12) * (((Math.pow((1 + rate / intFrequency), intFrequency * years)) - 1) / (rate / intFrequency)) * (1 + rate / intFrequency)));
}
        return years;*/

    /*public Double findSavingsYears(
            Double dbl1, //savingsGoal
            Double dbl2, //savingsAmount
            Double dbl4,  //savingsRate
            Double dbl5,  //savingsPayments
            Double dbl7,  //savingsFrequency
            Double dbl8  //savingsAnnualIncome
    ) {
        years2 = 0.0;
        do {
            years2 = years2 + .00274;
        }
        while (dbl1 >= (dbl2 * (Math.pow((1 + dbl4 / 12), 12 * years2))) + ((((dbl5 * dbl7) - dbl8) / 12) * (((Math.pow((1 + dbl4 / 12), 12 * years2)) - 1) / (dbl4 / 12)) * (1 + dbl4 / 12)));
        return years2;
    }*/

    public String calcSavingsDate(
            Double dbl1, //savingsGoal
            Double dbl2, //savingsAmount
            Double dbl4,  //savingsRate
            Double dbl5,  //savingsPayments
            Double dbl7,  //savingsFrequency
            Double dbl8,  //savingsAnnualIncome
            String str1,  //goal_achieved
            String str2  //too_far
    ) {

        alreadyDetermined = false;

        if (dbl1 == 0 || dbl1 <= dbl2) { //if goal is 0 or <= amount then goal achieved
            savingsDateB = str1;
            alreadyDetermined = true;
        } else if (dbl2 == 0) { //if amount is 0
            if (dbl5 == 0 || dbl4 == 0) { //if payments are 0 or rate is 0 then too far
                savingsDateB = str2;
                alreadyDetermined = true;
            }
        } else if (dbl4 == 0) { //if rate is 0
            if (dbl8 == (dbl5 * dbl7)) { //if annual income = annual contribution then too far
                savingsDateB = str2;
                alreadyDetermined = true;
            } else if (dbl8 != (dbl5 * dbl7)) { //if annual income != annual contribution then
                years = ((dbl1 - dbl2) / ((dbl5 * dbl7) - dbl8)); //years = amount left until goal / annual contribution minus annual income
                numberOfDaysToSavingsGoal = (int) Math.round(years * 365);
                alreadyDetermined = false;
            }
        } else {
            years2 = 0.0;
            do {
                years2 = years2 + .00274;
            }
            while (dbl1 >= (dbl2 * (Math.pow((1 + dbl4 / 12), 12 * years2))) + ((((dbl5 * dbl7) - dbl8) / 12) * (((Math.pow((1 + dbl4 / 12), 12 * years2)) - 1) / (dbl4 / 12)) * (1 + dbl4 / 12)));
            numberOfDaysToSavingsGoal = (int) Math.round(years2 * 365);
            alreadyDetermined = false;
        }

        if (!alreadyDetermined) {
            if (years < 0 || years2 < 0) { //if years if a negative number then too far
                savingsDate = str2;
            } else if (numberOfDaysToSavingsGoal == 0) { //if number of days is 0 then goal achieved
                savingsDate = str1;
            } else if (numberOfDaysToSavingsGoal > Integer.MAX_VALUE || numberOfDaysToSavingsGoal < 0) { //if number of days has too many digits or is < 0 then too far
                savingsDate = str2;
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
}
