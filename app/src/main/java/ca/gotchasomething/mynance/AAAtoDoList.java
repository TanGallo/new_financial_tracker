/*

ISSUES:
NAME CHANGED WHEN UPDATING SAVINGS - CHANGED TO A DEBTS NAME
IN PAYCC TAB, MULTIPLE ITEMS GETTING CHECKED AT A TIME, THE WRONG ITEMS THAT GOT CHECKED DO NOT GET REMOVED FROM PAGE WHEN CLICK ON THE PAID BUTTON, AND TOTAL NOT GOING BACK TO ZERO WHEN UNCHECKING BOX

TO DO:
CHANGE URL FOR RATINGS ONCLICKLISTENER IN LayoutHelp & DailyMoneyOut
CLEAN UP CODE
DOUBLE CHECK THAT ALL STRINGS ARE TRANSLATED

FORMATTING:
CHECK THE LOOK OF EACH SCREEN, INCLUDING THE HELP SCREENS
ADD COLOUR TO HELP SCREENS (TEXT?)

TEST SCENARIOS:
CREATE A DEBT/SAVINGS -- MAKE MONEYOUTS AGAINST IT -- DELETE IT -- ENSURE SPENDING REPORT STILL CONTAINS ENTRIES
TRY TO DELETE A DEBT/SAVING INCOME ITEM ON THE BUDGET
CREATE A DEBT/SAVINGS -- CHANGE ITS NAME -- ENSURE THE BUDGET INCOME/EXPENSE ALSO CHANGED NAMES -- ENSURE SPENDING REPORT HAS NEW NAME TOO

WHEN CREATING A DEBT:
create expense:
    expenseName = debtName (one-to-one)
    expenseAmount = debtPayments (one-to-one)
    expenseFrequency = debtFrequency (one-to-one)
    expensePriority = "A"
    expenseWeekly = "N"
    expenseAnnualAmount = expenseAmount * expenseFrequency
    expenseAAnnualAmount = expenseAnnualAmount
    expenseBAnnualAmount = 0.0
    expenseId = generated

create income:
    incomeName = debtName (one-to-one)
    incomeAmount = 0.0
    incomeFrequency = 1.0
    incomeAnnualAmount = 0.0
    incomeId = generated

debtName = captured
debtLimit = captured
debtAmount = captured
debtRate = captured
debtPayments = captured
debtFrequency = captured
debtAnnualIncome = 0.0
debtEnd = calculated
expRefKeyD = expenseId (one-to-one)
incRefKeyD - incomeId (one-to-one)
debtId - generated

WHEN UPDATING A DEBT:
update expense:
    if(expRefKeyD = expenseId):
        expenseName = new debtName
        expenseAmount = new debtPayments
        expenseFrequency = new debtFrequency
        expensePriority = get from DB
        expenseAnnualAmount = expenseAmount * expenseFrequency
        if(expensePriority = "A" {
        expenseAAnnualAmount = expenseAnnualAmount }
        if(expensePriority = "B") {
        expenseBAnnualAmount = expenseAnnualAmount }

update income:
    if(incRefKeyD = incomeId):
        incomeName = new debtName

debtName = captured
debtLimit = captured
debtAmount = captured
debtRate = captured
debtPayments = captured
debtFrequency = captured
debtEnd = calculated

update money in:
    if(incRefKeyD = incRefKeyMI):
        moneyInCat = new debtName

update money out:
    if(expRefKeyD = expRefKeyMO):
        moneyOutCat = new debtName
    if(debtId = moneyOutChargingDebtId)
        moneyOutDebtCat = new debtName

WHEN DELETING A DEBT:
warning re: income and expense will be also be deleted from budget
    if continue:
        if(expRefKeyD = expenseId) { delete expense }
        if(incRefKeyD = incomeId) { delete income }
        delete debt

WHEN CREATING A SAVINGS:
create expense:
    expenseName = savingsName (one-to-one)
    expenseAmount = savingsPayments (one-to-one)
    expenseFrequency = savingsFrequency (one-to-one)
    expensePriority = "A"
    expenseWeekly = "N"
    expenseAnnualAmount = expenseAmount * expenseFrequency
    expenseAAnnualAmount = expenseAnnualAmount
    expenseBAnnualAmount = 0.0
    expenseId = generated

create income:
    incomeName = savingsName (one-to-one)
    incomeAmount = 0.0
    incomeFrequency = 1.0
    incomeAnnualAmount = 0.0
    incomeId = generated

savingsName = captured
savingsAmount = captured
savingsGoal = captured
savingsPayments = captured
savingsFrequency = captured
savingsRate = captured
savingsIntFrequency = captured
savingsAnnualIncome = 0.0
savingsDate = calculated
expRefKeyS = expenseId (one-to-one)
incRefKeyS = incomeId (one-to-one)
savingsId = generated

WHEN UPDATING A SAVINGS:
update expense:
    if(expRefKeyS = expenseId)
        expenseName = new savingsName
        expenseAmount = new savingsPayments
        expenseFrequency = new savingsFrequency
        expensePriority = get from DB
        expenseAnnualAmount = expenseAmount * expenseFrequency
        if(expensePriority = "A" {
        expenseAAnnualAmount = expenseAnnualAmount }
        if(expensePriority = "B") {
        expenseBAnnualAmount = expenseAnnualAmount }

update income:
    if(incRefKeyS = incomeId)
        incomeName = new savingsName

savingsName = captured
savingsAmount = captured
savingsGoal = captured
savingsPayments = captured
savingsFrequency = captured
savingsRate = captured
savingsIntFrequency = captured
savingsDate = calculated

update money in:
    if(incRefKeyS = incRefKeyMI):
        moneyInCat = new savingsName

update money out:
    if(expRefKeyS = expRefKeyMO):
        moneyOutCat = new savingsName

WHEN DELETING A SAVINGS:
warning re: income and expense will be also be deleted from budget
    if continue:
        if(expRefKeyS = expenseId) { delete expense }
        if(incRefKeyS = incomeId) { delete income }
        delete savings

WHEN CREATING AN EXPENSE:
expenseName = captured
expenseAmount = captured
expenseFrequency = captured
expensePriority = captured
expenseWeekly = captured
expenseAnnualAmount = expenseAmount * expenseFrequency
if(expensePriority = "A"):
    expenseAAnnualAmount = expenseAnnualAmount
if(expensePriority = "B"):
    expenseBAnnualAmount = expenseAnnualAmount
expenseId = generated

WHEN CREATING AN INCOME:
incomeName = captured
incomeAmount = captured
incomeFrequency = captured
incomeAnnualAmount = incomeAmount * incomeFrequency
incomeId = generated

WHEN UPDATING AN EXPENSE:
expenseName = captured
expenseAmount = captured
expenseFrequency = captured
expensePriority = captured
expenseWeekly = captured
expenseAnnualAmount = expenseAmount * expenseFrequency
if(expensePriority = "A"):
    expenseAAnnualAmount = expenseAnnualAmount
if(expensePriority = "B"):
    expenseBAnnualAmount = expenseAnnualAmount

update debt:
    if(expenseId = expRefKeyD):
        debtName = new expenseName
        debtPayments = new expenseAmount
        debtFrequency = new expenseFrequency
        debtEnd = calculated
        debtId = get from DB

update savings:
    if(expenseId = expRefKeyS):
        savingsName = new expenseName
        savingsPayments = new expenseAmount
        savingsFrequency = new expenseFrequency
        savingsDate = calculated

update money out:
    if(expenseId = expRefKeyMO):
        moneyOutCat = new expenseName
        moneyOutPriority = new expensePriority
        moneyOutWeekly = new expenseWeekly
    if(debtId = moneyOutChargingDebtId)
        moneyOutDebtCat = new expenseName

WHEN DELETING AN EXPENSE:
if(expenseId = expRefKeyD):
    warning re: linked to a debt, can change to zero instead

if(expenseId = expRefKeyS):
    warning re: linked to a savings, can change to zero instead

warning re: entries made against this category will no longer be reflected in the reports

delete expense

WHEN UPDATING AN INCOME:
incomeName = captured
incomeAmount = captured
incomeFrequency = captured
incomeAnnualAmount = incomeAmount * incomeFrequency

update debt:
    if(incomeId = incRefKeyD):
        debtName = new incomeName
        debtAnnualIncome = new incomeAnnualAmount
        debtEnd = calculated

update savings:
    if(incomeId = incRefKeyS):
        savingsName = new incomeName
        savingsAnnualIncome = new incomeAnnualAmount
        savingsDate = calculated

update money in:
    if(incomeId = incRefKeyMI):
        moneyInCat = new incomeName

WHEN DELETING AN INCOME:
if(incomeId = incRefKeyD):
    warning re: debt will also be deleted, can change to zero instead
        if(continue) { delete debt }

if(incomeId = incRefKeyS):
    warning re: savings will also be deleted, can change to zero instead
        if(continue) { delete savings }

delete income

WHEN CREATING A MONEY IN:
moneyInCat = incomeName (many-to-one) - get from DB/spinner
moneyInAmount = captured
moneyInCreatedOn = calculated
incRefKeyMI = incomeId (many-to-one) - get from DB/spinner
moneyInId = generated

if(incRefKeyMI = incRefKeyD):
    (check if have enough left in debt):
    if(currentDebtAmount + moneyInAmount > debtLimit):
        warning re: not enough room on debt for cash advance
    else
        debtAmount = currentDebtAmount + moneyInAmount
        debtEnd = calculated

if(incRefKeyMI = incRefKeyS):
    (check if enough left in savings):
    if(currentSavingsAmount - moneyInAmount < 0):
        warning re: not enough left in savings for withdrawal
    else
        savingsAmount = currentSavingsAmount - moneyInAmount
        savingsDate = calculated

create money in

WHEN UPDATING A MONEY IN:
newMoneyInAmount = oldMoneyInAmount - captured moneyInAmount

if(incRefKeyMI = incRefKeyD):
    (check if have enough left in debt):
    if(currentDebtAmount + newMoneyInAmount > debtLimit):
        warning re: not enough room on debt for cash advance
    else
        debtAmount = currentDebtAmount + newMoneyInAmount
        debtEnd = calculated

if(incRefKeyMI = incRefKeyS):
    (check if enough left in savings):
    if(currentSavingsAmount - newMoneyInAmount < 0):
        warning re: not enough left in savings for withdrawal
    else
        savingsAmount = currentSavingsAmount - newMoneyInAmount
        savingsDate = calculated

WHEN DELETING A MONEY IN:
if(incRefKeyMI = incRefKeyD):
    debtAmount = currentDebtAmount - moneyInAmount
    debtEnd = calculated

if(incRefKeyMI = indRefKeyS):
    savingsAmount = currentSavingsAmount + moneyInAmount
    savingsDate = calculated

delete money in
-----------------------------------------------------------------------------------------------
WHEN CREATING A MONEY OUT FROM MONEY OUT TAB:
moneyOutCat = expenseName (many-to-one) - get from DB/spinner
moneyOutPriority = expensePriority (many-to-one) - get from DB/spinner
moneyOutWeekly = expenseWeekly (many-to-one) - get from DB/spinner
moneyOutAmount = captured
moneyOutCreatedOn = calculated
moneyOutCC = "N"
moneyOutDebtCat = "N/A"
moneyOutChargingDebtId = 0
moneyOutToPay = 0
moneyOutPaid = 0
expRefKeyMO = expenseId (many-to-one) - get from DB/spinner
moneyOutId = generated

if(expRefKeyMO = expRefKeyD):
    debtAmount = currentDebtAmount - moneyOutAmount
    debtEnd = calculated

if(expRefKeyMO = expRefKeyS):
    savingsAmount = currentSavingsAmount + moneyOutAmount
    savingsDate = calculated

create money out

WHEN UPDATING A MONEY OUT FROM MONEY OUT TAB:
newMoneyOutAmount = oldMoneyOutAmount - captured moneyOutAmount

if(expRefKeyMO = expRefKeyD):
    debtAmount = currentDebtAmount - newMoneyOutAmount
    debtEnd = calculated

if(expRefKeyMO = expRefKeyS):
    savingsAmount = currentSavingsAmount + newMoneyOutAmount
    savingsDate = calculated

WHEN DELETING A MONEY OUT FROM MONEY OUT TAB:
if(expRefKeyMO = expRefKeyD):
    debtAmount = currentDebtAmount + moneyOutAmount
    debtEnd = calculated

if(expRefKeyMO = expRefKeyS):
    savingsAmount = currentSavingsAmount - moneyOutAmount
    savingsDate = calculated

delete money out

WHEN CREATING A MONEY OUT FROM CREDIT CARD TAB:
moneyOutCat = expenseName (many-to-one) - get from DB/spinner
moneyOutPriority = expensePriority (many-to-one) - get from DB/spinner
moneyOutWeekly = expenseWeekly (many-to-one) - get from DB/spinner
moneyOutAmount = captured
moneyOutCreatedOn = calculated
moneyOutDebtCat = debtName (many-to-one) - get from DB/spinner
moneyOutChargingDebtId = debtId (many-to-one) - get from DB/spinner
moneyOutToPay = 0
moneyOutPaid = 0
expRefKeyMO = expenseId (many-to-one) - get from DB/spinner
moneyOutId = generated

if(expRefKeyMO = expRefKeyD OR expRefKeyMO = expRefKeyS):
    moneyOutCC = "N"
else
    moneyOutCC = "Y"

if(moneyOutChargingDebtId = debtId):
    (check if enough left on debt):
    if(currentDebtAmount + moneyOutAmount > debtLimit):
        warning re: not enough left on debt to make purchase
        if(continue):
            debtAmount = currentDebtAmount + moneyOutAmount
            debtEnd = calculated

        if(expRefKeyMO = expRefKeyD):
            debtAmount = currentDebtAmount - moneyOutAmount
            debtEnd = calculated

        if(expRefKeyMO = expRefKeyS):
            savingsAmount = currentSavingsAmount + moneyOutAmount
            savingsDate = calculated

            create money out

WHEN UPDATING A MONEY OUT FROM CREDIT CARD TAB:
newMoneyOutAmount = oldMoneyOutAmount - captured moneyOutAmount

if(moneyOutChargingDebtId = debtId):
    (check if enough left on debt):
    if(currentDebtAmount + newMoneyOutAmount > debtLimit):
        warning re: not enough left on debt to make purchase
        if(continue):
            debtAmount = currentDebtAmount + newMoneyOutAmount
            debtEnd = calculated

        if(expRefKeyMO = expRefKeyD):
            debtAmount = currentDebtAmount - newMoneyOutAmount
            debtEnd = calculated

        if(expRefKeyMO = expRefKeyS):
            savingsAmount = currentSavingsAmount + newMoneyOutAmount
            savingsDate = calculated

WHEN DELETING A MONEY OUT FROM CREDIT CARD TAB:
if(moneyOutChargingDebtId = debtId):
    debtAmount = currentDebtAmount - moneyOutAmount
    debtEnd = calculated

if(expRefKeyMO = expRefKeyD):
    debtAmount = currentDebtAmount + moneyOutAmount
    debtEnd = calculated

if(expRefKeyMO = expRefKeyS):
    savingsAmount = currentSavingsAmount - moneyOutAmount
    savingsDate = calculated

delete money out

WHEN CREATING A CC PAYMENT:
moneyOutCC
moneyOutToPay
moneyOutPaid

if(moneyOutChargingDebtId = debtId):
    debtAmount = currentDebtAmount - moneyOutAmount

*/