/*
LASTPAGEID:
1 LAYOUTMONEYIN
2 LAYOUTBUDGET
3 LAYOUTMONEYOUT
4 LAYOUTWEEKLYLIMITSLIST
5 LAYOUTDEBT
6 LAYOUTSAVINGS
7 LAYOUTCCPUR
8 LAYOUTTRANSFERS
9 LAYOUTWEEKLYLIMITS
10 LAYOUTEDITVIEWTRANSACTIONS

NO ID:
PREFERENCEMANAGER
SLIESLAYOUTONBOARDING
SLIDESONBOARDINGL
SLIDESONBOARDINGP
SLIDESLAYOUTSETUP
SLIDESSETUPL
SLIDESSETUPP
LAYOUTSETUP
SETUPANALYSIS
SETUPFINAL
SETUPFINALL
SETUPFINALP
MAINACTIVITY
MAINNAVIGATION
LAYOUTMONEYINLIST (1)
LAYOUTMONEYOUTLIST (3)
LAYOUTCCPURLIST (7)
LAYOUTTRANSFERSLIST (8)
ADDDEBTS
ADDDEBTSLIST (7, 5, 2)
ADDEXPENSE (4)
ADDEXPENSELIST (4, 3, 2, 7)
ADDINCOME
ADDINCOMELIST (1, 2)
ADDSAVINGS
ADDSAVINGSLIST (6, 2)
LAYOUTRATINGS

LAYOUTINCOMESUMMARY ???
LAYOUTSPENDSUMMARY ???


DOUBLE CHECK GOING TO MAINACTIVITY VS. GOING TO LAYOUTDAILYMONEY //S/B MAINACTIVITY?
DOUBLE CHECK THAT ALL EDITTEXTS HAVE ACCESS TO THE RIGHT KEYBOARD CHARACTERS (SEE FRAG_LIST_8 FOR REFERENCE)
DOUBLE CHECK THAT MENUS WORKING PROPERLY DURING SET UP PROCESS
MAKE SURE ALL REFERENCES TO "MAIN ACCOUNT" REFER TO THE STRING AND NOT SPELLED OUT IN CODE

RETHINK HOW DEBTANNUALPAYMENTS CALCULATED AND ADJUSTED IN DBMGR.UPDATEDEBTRECRETRANSFER & DBMGR.UPDATEALLDEBTBUDGET
RETHINK HOW SAVINGSANNUALPAYMENTS CALCULATED AND ADJUSTED IN DBMGR.UPDATESAVRECRETRANSFER & DBMGR.UPDATEALLSAVBUDGET

CHECK 'DEBT PAID' DATE SHOWING UP WRONGLY

UPDATE MANIFEST
UPDATE HELP PAGES

TRY A SWITCH STATEMENT FOR DEFINING THE INT[] OF SLIDES

PUT WEEKLY SPENDING ALERTS IN PLACE

CLEAN UP CODE
CHECK USAGE OF VARIABLES FOR OVERLAPS

*/