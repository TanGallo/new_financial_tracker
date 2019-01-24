/*

WHEN CREATING A DEBT ITEM: (DEBTNAME, debtLimit, debtAmount, debtRate, DEBTPAYMENTS, DEBTFREQUENCY, debtEnd, expRefKeyD, incRefKeyD, debtId)
        create income item:
            incomeName = DEBTNAME
            incomeAmount = "0.0"
            incomeFrequency = "0.0"
            incomeAnnualAmount = "0.0"

        create expense item:
            expenseName = DEBTNAME
            expenseAmount = DEBTPAYMENTS
            expenseFrequency = DEBTFREQUENCY
            expensePriority = "A"
            expenseWeekly = "N"
            expenseAnnualAmount = DEBTPAYMENTS * DEBTFREQUENCY
            expenseAAnnualAmount = DEBTPAYMENTS * DEBTFREQUENCY
            expenseBAnnualAmount = "0.0"

WHEN CREATING A SAVINGS ITEM: (SAVINGSNAME, savingsAmount, savingsGoal, SAVINGSPAYMENTS, SAVINGSFREQUENCY, savingsRate, savingsIntFrequency, savingsDate, expRefKeyS, incRefKeyS, savingsId)
        create income item:
            incomeName = SAVINGSNAME
            incomeAmount = "0.0"
            incomeFrequency = "0.0"
            incomeAnnualAmount = "0.0"

        create expense item:
            expenseName = SAVINGSNAME
            expenseAmount = SAVINGSPAYMENTS
            expenseFrequency = SAVINGSFREQUENCY
            expensePriority = "A"
            expenseWeekly = "N"
            expenseAnnualAmount = SAVINGSPAYMENTS * SAVINGSFREQUENCY
            expenseAAnnualAmount = SAVINGSPAYMENTS * SAVINGSFREQUENCY
            expenseBAnnualAmount = "0.0"

WHEN UPDATING AN INCOME ITEM: (INCOMENAME, INCOMEAMOUNT, INCOMEFREQUENCY, incomeAnnualAmount, incomeId)
        if(incomeId = incRefKeyD) {
            debtName = INCOMENAME
            debtLimit = doesn't change
            debtAmount = doesn't change
            debtRate = doesn't change
            debtPayments = ((currentDebtPayments * currentDebtFrequency) - (newINCOMEAMOUNT * INCOMEFREQUENCY)) / currentDebtFrequency
            debtFrequency = doesn't change
            debtEnd = CALCULATED }

        if(incomeId = incRefKeyS) {
            savingsName = INCOMENAME
            savingsAmount = doesn't change
            savingsGoal = doesn't change
            savingsPayments = ((currentSavingsPayments * currentSavingsFrequency) - (newINCOMEAMOUNT * INCOMEFREQUENCY)) / currentSavingsFrequency
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        if(incomeId = incRefKeyMI) {
            moneyInCat = INCOMENAME
            moneyInAmount = doesn't change
            moneyInCreatedOn = doesn't change}

WHEN UPDATING AN EXPENSE ITEM: (EXPENSENAME, EXPENSEAMOUNT, EXPENSEFREQUENCY, EXPENSEPRIORITY, EXPENSEWEEKLY, expenseAnnualAmount, expenseAAnnualAmount, expenseBAnnualAmount, expenseId)
        if(expenseId = expRefKeyD) {
            debtName = EXPENSENAME
            debtLimit = doesn't change
            debtAmount = doesn't change
            debtRate = doesn't change
            debtPayments = ((currentDebtPayments * currentDebtFrequency) + (newEXPENSEAMOUNT * EXPENSEFREQUENCY)) / EXPENSEFREQUENCY
            debtFrequency = EXPENSEFREQUENCY
            debtEnd = CALCULATED}

        if(expenseId = expRefKeyS) {
            savingsName = EXPENSENAME
            savingsAmount = doesn't change
            savingsGoal = doesn't change
            savingsPayments = ((currentSavingsPayments * currentSavingsFrequency) + (newEXPENSEAMOUNT * EXPENSEFREQUENCY)) / EXPENSEFREQUENCY
            savingsFrequency = EXPENSEFREQUENCY
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        if(expenseId = expRefKeyMO) {
            moneyOutCat = EXPENSENAME
            moneyOutPriority = EXPENSEPRIORITY
            moneyOutWeekly = EXPENSEWEEKLY
            moneyOutAmount = doesn't change
            moneyOutCreatedOn = doesn't change
            moneyOutCC = doesn't change
            moneyOutDebtCat = doesn't change
            moneyOutChargingDebtId = doesn't change
            moneyOutToPay = doesn't change
            moneyOutPaid = doesn't change}

WHEN CREATING A MONEY IN ITEM: (MONEYINCAT, moneyInAmount, moneyInCreatedOn, incRefKeyMI, moneyInId)
        if(incRefKeyMI = incRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount + MONEYINAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED }

        if(incRefKeyMI = incRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount - MONEYINAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

WHEN UPDATING A MONEY IN ITEM: (MONEYINAMOUNT)
        if(incRefKeyMI = incRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount + newMONEYINAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED }

        if(incRefKeyMI = incRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount - newMONEYINAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

WHEN CREATING A MONEY OUT ITEM: (moneyOutCat, moneyOutPriority, moneyOutWeekly, MONEYOUTAMOUNT, moneyOutCreatedOn, moneyOutCC, moneyOutDebtCat, moneyOutChargingDebtId, moneyOutToPay, moneyOutPaid, expRefKeyMO, moneyOutId)
        IN MONEY OUT TAB:
        if(expRefKeyMO = expRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount - MONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

        if(expRefKeyMO = expRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount + MONEYOUTAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        IN CREDIT CARD TAB:
        if(expRefKeyMO = expRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount - MONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

        if(expRefKeyMO = expRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount + MONEYOUTAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        if(moneyOutChargingDebtId = debtId) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount + MONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

WHEN UPDATING A MONEY OUT ITEM: (MONEYOUTAMOUNT)
        IN MONEY OUT TAB:
        if(expRefKeyMO = expRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount - newMONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

        if(expRefKeyMO = expRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount + newMONEYOUTAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        IN CREDIT CARD TAB:
        if(expRefKeyMO = expRefKeyD) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount - newMONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

        if(expRefKeyMO = expRefKeyS) {
            savingsName = doesn't change
            savingsAmount = currentSavingsAmount + newMONEYOUTAMOUNT
            savingsGoal = doesn't change
            savingsPayments = doesn't change
            savingsFrequency = doesn't change
            savingsRate = doesn't change
            savingsIntFrequency = doesn't change
            savingsDate = CALCULATED}

        if(moneyOutChargingDebtId = debtId) {
            debtName = doesn't change
            debtLimit = doesn't change
            debtAmount = currentDebtAmount + newMONEYOUTAMOUNT
            debtRate = doesn't change
            debtPayments = doesn't change
            debtFrequency = doesn't change
            debtEnd = CALCULATED}

WHEN UPDATING A DEBT ITEM: (DEBTNAME, debtLimit, debtAmount, debtRate, DEBTPAYMENTS, DEBTFREQUENCY, DEBTEND, expRefKeyD, incRefKeyD, debtId)
        FIRST DO:
        set DEBTNAME = DEBTNAME
        set DEBTLIMIT = DEBTLIMIT
        set DEBTAMOUNT = DEBTAMOUNT
        set DEBTRATE = DEBTRATE
        set DEBTFREQUENCY = DEBTFREQUENCY

        THEN DO:
        if(incRefKeyD = incomeId) {
            incomeName = DEBTNAME
            incomeAmount = getINCOMEAMOUNT
            incomeFrequency = getINCOMEFREQUENCY
            incomeAnnualAmount = doesn't change}

        THEN DO:
        set DEBTPAYMENTS = ((DEBTPAYMENTS * DEBTFREQUENCY) - (INCOMEAMOUNT * INCOMEFREQUENCY)) / DEBTFREQUENCY
        set DEBTEND = CALCULATED

        if(expRefKeyD = expenseId) {
            expenseName = DEBTNAME
            expenseAmount = DEBTPAYMENTS
            expenseFrequency = DEBTFREQUENCY
            expensePriority = getEXPENSEPRIORITY
            expenseWeekly = doesn't change
            expenseAnnualAmount = DEBTPAYMENTS * DEBTFREQUENCY
            if(EXPENSEPRIORITY = A) {
            expenseAAnnualAmount = DEBTPAYMENTS * DEBTFREQUENCY }
            if(EXPENSEPRIORITY = B) {
            expenseBAnnualAmount = DEBTPAYMENTS * DEBTFREQUENCY} }

        if(expRefKeyD = expRefKeyMO) {
            moneyOutCat = DEBTNAME
            moneyOutPriority = doesn't change
            moneyOutWeekly = doesn't change
            moneyOutAmount = doesn't change
            moneyOutCreatedOn = doesn't change
            moneyOutCC = doesn't change
            moneyOutDebtCat = doesn't change
            moneyOutChargingDebtId = doesn't change
            moneyOutToPay = doesn't change
            moneyOutPaid = doesn't change}

        if(moneyOutChargingDebtId = DEBTID) {
            moneyOutCat = doesn't change
            moneyOutPriority = doesn't change
            moneyOutWeekly = doesn't change
            moneyOutAmount = doesn't change
            moneyOutCreatedOn = doesn't change
            moneyOutCC = doesn't change
            moneyOutDebtCat = DEBTNAME
            moneyOutChargingDebtId = doesn't change
            moneyOutToPay = doesn't change
            moneyOutPaid = doesn't change}

        if(incRefKeyD = incRefKeyMI) {
            moneyInCat = DEBTNAME
            moneyInAmount = doesn't change
            moneyInCreatedOn = doesn't change}

WHEN UPDATING A SAVINGS ITEM: (SAVINGSNAME, savingsAmount, savingsGoal, savingsPayments, savingsFrequency, savingsRate, savingsIntFrequency, savingsDate, expRefKeyS, incRefKeyS, savingsId)
        FIRST DO:
        set SAVINGSNAME = SAVINGSNAME
        set SAVINGSAMOUNT = SAVINGSAMOUNT
        set SAVINGSGOAL = SAVINGSGOAL
        set SAVINGSFREQUENCY = SAVINGSFREQUENCY
        set SAVINGSRATE = SAVINGSRATE
        set SAVINGSINTFREQUENCY = SAVINGSINTFREQUENCY

        THEN DO:
        if(incRefKeyS = incomeId) {
            incomeName = SAVINGSNAME
            incomeAmount = getINCOMEAMOUNT
            incomeFrequency = getINCOMEFREQUENCY
            incomeAnnualAmount = doesn't change}

        THEN DO:
        set SAVINGSPAYMENTS = ((SAVINGSPAYMENTS * SAVINGSFREQUENCY) - (INCOMEAMOUNT * INCOMEFREQUENCY)) / SAVINGSFREQUENCY
        set SAVINGSDATE = CALCULATED

        if(expRefKeyS = expenseId) {
            expenseName = SAVINGSNAME
            expenseAmount = SAVINGSPAYMENTS
            expenseFrequency = SAVINGSFREQUENCY
            expensePriority = getEXPENSEPRIORITY
            expenseWeekly = doesn't change
            expenseAnnualAmount = SAVINGSPAYMENTS * SAVINGSFREQUENCY
            if(EXPENSEPRIORITY = A) {
            expenseAAnnualAmount = SAVINGSPAYMENTS * SAVINGSFREQUENCY }
            if(EXPENSEPRIORITY = B) {
            expenseBAnnualAmount = SAVINGSPAYMENTS * SAVINGSFREQUENCY}

        if(expRefKeyS = expRefKeyMO) {
            moneyOutCat = SAVINGSNAME
            moneyOutPriority = doesn't change
            moneyOutWeekly = doesn't change
            moneyOutAmount = doesn't change
            moneyOutCreatedOn = doesn't change
            moneyOutCC = doesn't change
            moneyOutDebtCat = doesn't change
            moneyOutChargingDebtId = doesn't change
            moneyOutToPay = doesn't change
            moneyOutPaid = doesn't change}

        if(incRefKeyS = incRefKeyMI) {
            moneyInCat = SAVINGSNAME
            moneyInAmount = doesn't change
            moneyInCreatedOn = doesn't change}

MAKE SURE CORRECT KEYBOARDS ON EACH SCREEN
ADD RATINGS REQUEST
HANDLE ZEROS & EMPTY FIELDS
MAKE SURE STRINGS ARE STRING RESOURCES
CLEAN UP CODE
ADD CONTENT DESCRIPTIONS
TRANSLATE STRINGS TO FRENCH
CHANGE URL FOR RATINGS ONCLICKLISTENER IN LayoutHelp

FORMATTING:
CHECK THE LOOK OF EACH SCREEN, INCLUDING THE HELP SCREENS
ADD COLOUR TO HELP SCREENS (TEXT?)

*/