package com.vimukti.accounter.core;

import java.util.List;

import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class CompanyPreferences implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169837986493037863L;

	private static int GENERAL_TIME_FORMAT_MINUTES;

	private static int GENERAL_TIME_FORMAT_DECIMAL;

	private static final int SHOW_SUMMARY = 10;

	private static final int SHOW_LIST = 20;

	public static int VAT_REPORTING_PERIOD_MONTHLY = 1;
	public static int VAT_REPORTING_PERIOD_BIMONTHLY = 2;
	public static int VAT_REPORTING_PERIOD_QUARTERLY = 3;
	public static int VAT_REPORTING_PERIOD_ANUALLY = 4;

	public static int VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC = 1;
	public static int VAT_REP_ENDPERIOD_APR_JUL_OCT_JAN = 2;
	public static int VAT_REP_ENDPERIOD_MAY_AUG_NOV_FEB = 3;

	// Company Preferrences

	/**
	 * To confirm whether we can use Account Numbers or not.
	 */
	boolean useAccountNumbers = true;
	// property existed from our older QuickBooks Code
	boolean useClasses = false;
	// property existed from our older QuickBooks Code
	boolean useJobs = false;
	// property existed from our older QuickBooks Code
	boolean useChangeLog = false;
	// property existed from our older QuickBooks Code
	double logSpaceUsed = 0D;
	/**
	 * To Confirm whether we can allow duplicate {@link Transaction} Numbers or
	 * not
	 */
	boolean allowDuplicateDocumentNumbers = true;

	int ageingFromTransactionDateORDueDate = 1;
	/**
	 * whether the Customers in our company pay Sales Tax or not
	 */
	boolean doYouPaySalesTax = true;
	/**
	 * if True then Accural (when customer is Invoiced), if False then Cash
	 * Basis(when customer pays Invoice)
	 * 
	 * @see Invoice
	 * @see EnterBill
	 */
	boolean isAccuralBasis = true;

	FinanceDate startOfFiscalYear = new FinanceDate();
	FinanceDate endOfFiscalYear = new FinanceDate();
	boolean useForeignCurrency = true;
	boolean isMyAccountantWillrunPayroll = false;
	// Multi currency property
	boolean enableMultiCurrency = false;

	FinanceDate startDate = new FinanceDate();

	private FinanceDate depreciationStartDate = new FinanceDate();

	FinanceDate preventPostingBeforeDate = new FinanceDate();
	private boolean doyouKeepTrackofBills;

	private boolean doYouKeepTrackOfTime;
	// Customer Preferrences
	private boolean doYouChargesalesTax;
	/**
	 * whether we can use Id's for the Customer while creating them or not.
	 */
	boolean useCustomerId = true;

	/**
	 * The Shipping Term which can be displayed in the required
	 * {@link Transaction}s.
	 */
	ShippingTerms defaultShippingTerm;

	int defaultAnnualInterestRate = 0;
	double defaultMinimumFinanceCharge = 0D;
	int graceDays = 0;
	boolean doesCalculateFinanceChargeFromInvoiceDate = false; // If true then
	// finance
	// chagrge will
	// calculate from
	// Invoice date other
	// wise from Due Date.

	// Vendor Preferrences

	boolean useVendorId = true;
	private boolean salesOrderEnabled;
	boolean useItemNumbers = true;
	boolean checkForItemQuantityOnHand = false;
	boolean updateCostAutomatically = false;

	// VAT Preferences
	boolean enterVATinformationNow = true;
	String VATregistrationNumber = "";
	int VATreportingPeriod = VAT_REPORTING_PERIOD_QUARTERLY;
	int endingPeriodForVATreporting = VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC;
	boolean reportVATonAccuralBasis = true;
	boolean trackVAT = true;
	String VATtaxAgencyName = AccounterConstants.DEFAULT_VAT_AGENCY_NAME;

	private String dateFormat;
	public long id;

	private boolean isSalesPersonEnabled;

	private boolean sellServices;
	private boolean sellProducts;

	// currency related properties
	private Currency primaryCurrency;
	private List<Currency> supportingCurrenciesList;

	// Organization type
	private int organizationType;

	// for tracking employes in setup page
	boolean haveEpmloyees;
	boolean haveW_2Employees;
	boolean have1099contractors;
	boolean trackEmployeeExpenses;
	private String referCustomers;
	private String referSuplliers;
	private String referAccounts;
	// for select fiscal year in setup

	int fiscalYearFirstMonth;
	boolean isBeginingorTodaysdate;
	ClientFinanceDate trackFinanceDate;

	public ClientFinanceDate getTrackFinanceDate() {
		return trackFinanceDate;
	}

	public void setTrackFinanceDate(ClientFinanceDate trackFinanceDate) {
		this.trackFinanceDate = trackFinanceDate;
	}

	public boolean isBeginingorTodaysdate() {
		return isBeginingorTodaysdate;
	}

	public void setBeginingorTodaysdate(boolean isBeginingorTodaysdate) {
		this.isBeginingorTodaysdate = isBeginingorTodaysdate;
	}

	public int getFiscalYearFirstMonth() {
		return fiscalYearFirstMonth;
	}

	public void setFiscalYearFirstMonth(int fiscalYearFirstMonth) {
		this.fiscalYearFirstMonth = fiscalYearFirstMonth;
	}

	public boolean isHaveEpmloyees() {
		return haveEpmloyees;
	}

	public void setHaveEpmloyees(boolean haveEpmloyees) {
		this.haveEpmloyees = haveEpmloyees;
	}

	public boolean isHaveW_2Employees() {
		return haveW_2Employees;
	}

	public void setHaveW_2Employees(boolean haveW_2Employees) {
		this.haveW_2Employees = haveW_2Employees;
	}

	public boolean isHave1099contractors() {
		return have1099contractors;
	}

	public void setHave1099contractors(boolean have1099contractors) {
		this.have1099contractors = have1099contractors;
	}

	public boolean isTrackEmployeeExpenses() {
		return trackEmployeeExpenses;
	}

	public void setTrackEmployeeExpenses(boolean trackEmployeeExpenses) {
		this.trackEmployeeExpenses = trackEmployeeExpenses;
	}

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}

	public List<Currency> getSupportingCurrenciesList() {
		return supportingCurrenciesList;
	}

	public void setSupportingCurrenciesList(
			List<Currency> supportingCurrenciesList) {
		this.supportingCurrenciesList = supportingCurrenciesList;
	}

	public boolean isSellServices() {
		return sellServices;
	}

	public void setSellServices(boolean sellServices) {
		this.sellServices = sellServices;
	}

	public boolean isSellProducts() {
		return sellProducts;
	}

	public void setSellProducts(boolean sellProducts) {
		this.sellProducts = sellProducts;
	}

	// String legalName = "";

	public CompanyPreferences() {
	}

	/**
	 * @return the legalName
	 */
	// public String getLegalName() {
	// return legalName;
	// }

	/**
	 * @param legalName
	 *            the legalName to set
	 */
	// public void setLegalName(String legalName) {
	// this.legalName = legalName;
	// }

	public String getVATtaxAgencyName() {
		return VATtaxAgencyName;
	}

	public void setVATtaxAgencyName(String ttaxAgencyName) {
		VATtaxAgencyName = ttaxAgencyName;
	}

	/**
	 * @return the enterVATinformationNow
	 */
	public boolean isEnterVATinformationNow() {
		return enterVATinformationNow;
	}

	/**
	 * @param enterVATinformationNow
	 *            the enterVATinformationNow to set
	 */
	public void setEnterVATinformationNow(boolean enterVATinformationNow) {
		this.enterVATinformationNow = enterVATinformationNow;
	}

	/**
	 * @return the vATregistrationNumber
	 */
	public String getVATregistrationNumber() {
		return VATregistrationNumber;
	}

	/**
	 * @param vATregistrationNumber
	 *            the vATregistrationNumber to set
	 */
	public void setVATregistrationNumber(String vATregistrationNumber) {
		VATregistrationNumber = vATregistrationNumber;
	}

	/**
	 * @return the vATreportingPeriod
	 */
	public int getVATreportingPeriod() {
		return VATreportingPeriod;
	}

	/**
	 * @param vATreportingPeriod
	 *            the vATreportingPeriod to set
	 */
	public void setVATreportingPeriod(int vATreportingPeriod) {
		VATreportingPeriod = vATreportingPeriod;
	}

	/**
	 * @return the endingPeriodForVATreporting
	 */
	public int getEndingPeriodForVATreporting() {
		return endingPeriodForVATreporting;
	}

	/**
	 * @param endingPeriodForVATreporting
	 *            the endingPeriodForVATreporting to set
	 */
	public void setEndingPeriodForVATreporting(int endingPeriodForVATreporting) {
		this.endingPeriodForVATreporting = endingPeriodForVATreporting;
	}

	/**
	 * @return the reportVATonAccuralBasis
	 */
	public boolean isReportVATonAccuralBasis() {
		return reportVATonAccuralBasis;
	}

	/**
	 * @param reportVATonAccuralBasis
	 *            the reportVATonAccuralBasis to set
	 */
	public void setReportVATonAccuralBasis(boolean reportVATonAccuralBasis) {
		this.reportVATonAccuralBasis = reportVATonAccuralBasis;
	}

	/**
	 * @return the trackVAT
	 */
	public boolean isTrackVAT() {
		return trackVAT;
	}

	/**
	 * @param trackVAT
	 *            the trackVAT to set
	 */
	public void setTrackVAT(boolean trackVAT) {
		this.trackVAT = trackVAT;
	}

	/**
	 * @param logSpaceUsed
	 *            the logSpaceUsed to set
	 */
	public void setLogSpaceUsed(double logSpaceUsed) {
		this.logSpaceUsed = logSpaceUsed;
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setAccuralBasis(boolean isAccuralBasis) {
		this.isAccuralBasis = isAccuralBasis;
	}

	/**
	 * @param isMyAccountantWillrunPayroll
	 *            the isMyAccountantWillrunPayroll to set
	 */
	public void setMyAccountantWillrunPayroll(
			boolean isMyAccountantWillrunPayroll) {
		this.isMyAccountantWillrunPayroll = isMyAccountantWillrunPayroll;
	}

	/**
	 * @return the useAccountNumbers
	 */
	public boolean getUseAccountNumbers() {
		return useAccountNumbers;
	}

	/**
	 * @param useAccountNumbers
	 *            the useAccountNumbers to set
	 */
	public void setUseAccountNumbers(boolean useAccountNumbers) {
		this.useAccountNumbers = useAccountNumbers;
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getPreventPostingBeforeDate() {
		return preventPostingBeforeDate;
	}

	public void setPreventPostingBeforeDate(FinanceDate preventPostingBeforeDate) {
		this.preventPostingBeforeDate = preventPostingBeforeDate;
	}

	/**
	 * @return the useClasses
	 */
	public boolean getUseClasses() {
		return useClasses;
	}

	/**
	 * @param useClasses
	 *            the useClasses to set
	 */
	public void setUseClasses(boolean useClasses) {
		this.useClasses = useClasses;
	}

	/**
	 * @return the useJobs
	 */
	public boolean getUseJobs() {
		return useJobs;
	}

	/**
	 * @param useJobs
	 *            the useJobs to set
	 */
	public void setUseJobs(boolean useJobs) {
		this.useJobs = useJobs;
	}

	/**
	 * @return the useChangeLog
	 */
	public boolean getUseChangeLog() {
		return useChangeLog;
	}

	/**
	 * @param useChangeLog
	 *            the useChangeLog to set
	 */
	public void setUseChangeLog(boolean useChangeLog) {
		this.useChangeLog = useChangeLog;
	}

	/**
	 * @return the logSpaceUsed
	 */
	public double getLogSpaceUsed() {
		return logSpaceUsed;
	}

	/**
	 * @return the allowDuplicateDocumentNumbers
	 */
	public boolean getAllowDuplicateDocumentNumbers() {
		return allowDuplicateDocumentNumbers;
	}

	/**
	 * @param allowDuplicateDocumentNumbers
	 *            the allowDuplicateDocumentNumbers to set
	 */
	public void setAllowDuplicateDocumentNumbers(
			boolean allowDuplicateDocumentNumbers) {
		this.allowDuplicateDocumentNumbers = allowDuplicateDocumentNumbers;
	}

	/**
	 * @return the doYouPaySalesTax
	 */
	public boolean getDoYouPaySalesTax() {
		return doYouPaySalesTax;
	}

	/**
	 * @param doYouPaySalesTax
	 *            the doYouPaySalesTax to set
	 */
	public void setDoYouPaySalesTax(boolean doYouPaySalesTax) {
		this.doYouPaySalesTax = doYouPaySalesTax;
	}

	/**
	 * @return the isAccuralBasis
	 */
	public boolean getIsAccuralBasis() {
		return isAccuralBasis;
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setIsAccuralBasis(boolean isAccuralBasis) {
		this.isAccuralBasis = isAccuralBasis;
	}

	/**
	 * @return the startOfFiscalYear
	 */
	public FinanceDate getStartOfFiscalYear() {
		return startOfFiscalYear;
	}

	/**
	 * @param startOfFiscalYear
	 *            the startOfFiscalYear to set
	 */
	public void setStartOfFiscalYear(FinanceDate startOfFiscalYear) {
		this.startOfFiscalYear = startOfFiscalYear;
	}

	/**
	 * @return the endOfFiscalYear
	 */
	public FinanceDate getEndOfFiscalYear() {
		return endOfFiscalYear;
	}

	/**
	 * @param endOfFiscalYear
	 *            the endOfFiscalYear to set
	 */
	public void setEndOfFiscalYear(FinanceDate endOfFiscalYear) {
		this.endOfFiscalYear = endOfFiscalYear;
	}

	/**
	 * @return the isMyAccountantWillrunPayroll
	 */
	public boolean getIsMyAccountantWillrunPayroll() {
		return isMyAccountantWillrunPayroll;
	}

	/**
	 * @return the useCustomerId
	 */
	public boolean getUseCustomerId() {
		return useCustomerId;
	}

	/**
	 * @param useCustomerId
	 *            the useCustomerId to set
	 */
	public void setUseCustomerId(boolean useCustomerId) {
		this.useCustomerId = useCustomerId;
	}

	/**
	 * @return the defaultShippingTerm
	 */
	public ShippingTerms getDefaultShippingTerm() {
		return defaultShippingTerm;
	}

	/**
	 * @param defaultShippingTerm
	 *            the defaultShippingTerm to set
	 */
	public void setDefaultShippingTerm(ShippingTerms defaultShippingTerm) {
		this.defaultShippingTerm = defaultShippingTerm;
	}

	/**
	 * @return the defaultAnnualInterestRate
	 */
	public int getDefaultAnnualInterestRate() {
		return defaultAnnualInterestRate;
	}

	/**
	 * @param defaultAnnualInterestRate
	 *            the defaultAnnualInterestRate to set
	 */
	public void setDefaultAnnualInterestRate(int defaultAnnualInterestRate) {
		this.defaultAnnualInterestRate = defaultAnnualInterestRate;
	}

	/**
	 * @return the defaultMinimumFinanceCharge
	 */
	public double getDefaultMinimumFinanceCharge() {
		return defaultMinimumFinanceCharge;
	}

	/**
	 * @param defaultMinimumFinanceCharge
	 *            the defaultMinimumFinanceCharge to set
	 */
	public void setDefaultMinimumFinanceCharge(
			double defaultMinimumFinanceCharge) {
		this.defaultMinimumFinanceCharge = defaultMinimumFinanceCharge;
	}

	/**
	 * @return the graceDays
	 */
	public int getGraceDays() {
		return graceDays;
	}

	/**
	 * @param graceDays
	 *            the graceDays to set
	 */
	public void setGraceDays(int graceDays) {
		this.graceDays = graceDays;
	}

	/**
	 * @return the ageing
	 */
	public int getAgeingFromTransactionDateORDueDate() {
		return ageingFromTransactionDateORDueDate;
	}

	/**
	 * @param ageing
	 *            the ageing to set
	 */
	public void setAgeingFromTransactionDateORDueDate(
			int ageingFromTransactionDateORDueDate) {
		this.ageingFromTransactionDateORDueDate = ageingFromTransactionDateORDueDate;
	}

	/**
	 * @return the doesCalculateFinanceChargeFromInvoiceDate
	 */
	public boolean getDoesCalculateFinanceChargeFromInvoiceDate() {
		return doesCalculateFinanceChargeFromInvoiceDate;
	}

	/**
	 * @param doesCalculateFinanceChargeFromInvoiceDate
	 *            the doesCalculateFinanceChargeFromInvoiceDate to set
	 */
	public void setDoesCalculateFinanceChargeFromInvoiceDate(
			boolean doesCalculateFinanceChargeFromInvoiceDate) {
		this.doesCalculateFinanceChargeFromInvoiceDate = doesCalculateFinanceChargeFromInvoiceDate;
	}

	/**
	 * @return the useVendorId
	 */
	public boolean getUseVendorId() {
		return useVendorId;
	}

	/**
	 * @param useVendorId
	 *            the useVendorId to set
	 */
	public void setUseVendorId(boolean useVendorId) {
		this.useVendorId = useVendorId;
	}

	/**
	 * @return the useItemNumbers
	 */
	public boolean getUseItemNumbers() {
		return useItemNumbers;
	}

	/**
	 * @param useItemNumbers
	 *            the useItemNumbers to set
	 */
	public void setUseItemNumbers(boolean useItemNumbers) {
		this.useItemNumbers = useItemNumbers;
	}

	/**
	 * @return the checkForItemQuantityOnHand
	 */
	public boolean getCheckForItemQuantityOnHand() {
		return checkForItemQuantityOnHand;
	}

	/**
	 * @param checkForItemQuantityOnHand
	 *            the checkForItemQuantityOnHand to set
	 */
	public void setCheckForItemQuantityOnHand(boolean checkForItemQuantityOnHand) {
		this.checkForItemQuantityOnHand = checkForItemQuantityOnHand;
	}

	/**
	 * @return the updateCostAutomatically
	 */
	public boolean getUpdateCostAutomatically() {
		return updateCostAutomatically;
	}

	/**
	 * @param updateCostAutomatically
	 *            the updateCostAutomatically to set
	 */
	public void setUpdateCostAutomatically(boolean updateCostAutomatically) {
		this.updateCostAutomatically = updateCostAutomatically;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public FinanceDate getDepreciationStartDate() {
		return depreciationStartDate;
	}

	public void setDepreciationStartDate(FinanceDate depreciationStartDate) {
		this.depreciationStartDate = depreciationStartDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isSalesOrderEnabled() {
		return salesOrderEnabled;
	}

	public void setSalesOrderEnabled(boolean salesOrderEnabled) {
		this.salesOrderEnabled = salesOrderEnabled;
	}

	public void setEnableMultiCurrency(boolean enableMultiCurrency) {
		this.enableMultiCurrency = enableMultiCurrency;
	}

	public boolean isSalesPersonEnabled() {
		return isSalesPersonEnabled;
	}

	public void setSalesPerson(boolean isSalesPersonEnabled) {
		this.isSalesPersonEnabled = isSalesPersonEnabled;
	}

	public int getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	public boolean isDoYouChargesalesTax() {
		return doYouChargesalesTax;
	}

	public void setDoYouChargesalesTax(boolean doYouChargesalesTax) {
		this.doYouChargesalesTax = doYouChargesalesTax;
	}

	public boolean isDoyouKeepTrackofBills() {
		return doyouKeepTrackofBills;
	}

	public void setDoyouKeepTrackofBills(boolean doyouKeepTrackofBills) {
		this.doyouKeepTrackofBills = doyouKeepTrackofBills;
	}

	public boolean isDoYouKeepTrackOfTime() {
		return doYouKeepTrackOfTime;
	}

	public void setDoYouKeepTrackOfTime(boolean doYouKeepTrackOfTime) {
		this.doYouKeepTrackOfTime = doYouKeepTrackOfTime;
	}

	public String getReferCustomers() {
		return referCustomers;
	}

	public void setReferCustomers(String referCustomers) {
		this.referCustomers = referCustomers;
	}

	public String getReferSuplliers() {
		return referSuplliers;
	}

	public void setReferSuplliers(String referSuplliers) {
		this.referSuplliers = referSuplliers;
	}

	public String getReferAccounts() {
		return referAccounts;
	}

	public void setReferAccounts(String referAccounts) {
		this.referAccounts = referAccounts;
	}

	/*
	 * // Accounting Preferences
	 * 
	 * 
	 * boolean requireAccounts; boolean showLowestSubAccountOnly; Boolean
	 * useClassTracking; boolean promptToAssignClasses; Boolean
	 * automaticallyAssignGeneralJournalEntryNumber = Boolean.TRUE; Boolean
	 * warnWhenPostingATransactionToRetainedEarnings = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysPast = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysFuture = Boolean.TRUE;
	 * 
	 * int deafaultPastDays = 90; int defaultFuturedays = 30;
	 * 
	 * Date closingDate;
	 * 
	 * String closingDatePassword; String closingDatePasswordConfirmation;
	 * 
	 * // Bills Preferences int defaultBillsDueDate = 10; Boolean
	 * warnAboutDuplicateBillNumbersFromSameVendors; Boolean
	 * automaticallyUseDiscountsAndCredits; Account defaultDiscountAccount;
	 * 
	 * // Checking Preferences
	 * 
	 * boolean printAccountNamesOnVoucher; Boolean
	 * changeCheckDateWhenCheckIsPrinted; boolean startWithPayeeFieldOnCheck;
	 * boolean warnAboutDuplicateCheckNumbers = Boolean.TRUE; Boolean
	 * autofillPayeeAccountNumberinCheckMemo = Boolean.TRUE; Boolean
	 * enableCreatePayChecksWithDefaultAccount; Boolean
	 * enablePayOfPayRollLiablitiesWithDefaultAccount; Account
	 * defaultCreatePayCheckAccount; Account defaultPay_PayRollLiabalities;
	 * 
	 * // DeskTop View Preferences
	 * 
	 * boolean enableInvoices = Boolean.TRUE; boolean enableSalesReciepts =
	 * Boolean.TRUE; boolean enableStatementsAndStatementCharges = Boolean.TRUE;
	 * boolean enterBillsAndPayBills = Boolean.TRUE;
	 * 
	 * // Finance Charge Preferences double annualIntrestRateinPercent; Double
	 * minimumFinanceCharge; Integer minimumGracePeriodinDays; Account
	 * financeChargeAccount; Boolean
	 * accessFinanceChargesOnOverDueFinancialCharges; Date calculateChargesFrom;
	 * boolean markFinanceChargeInvoices; int timeFormat =
	 * GENERAL_TIME_FORMAT_MINUTES; boolean alwaysShowYearsAsFourDigit =
	 * Boolean.TRUE; boolean neverUpdateNameinformationWhenSavingTransactions;
	 * boolean saveTransactionsBeforePrinting = Boolean.TRUE;
	 * 
	 * // Integrated Applications
	 * 
	 * boolean allowApplicationsToAccessCompanyFile; Boolean
	 * notifyTheUserBeforeRunningTheApplicationWhoseCertificateHasExpired;
	 * 
	 * // Items And InventoryPreferences
	 * 
	 * boolean inventoryAndPurchaseOrdersAreActive; Boolean
	 * warnAboutDuplicatePurchaseNumbers; boolean deductQuantityOnSalesOrders;
	 * boolean warnIfNotInventoryToSale; int warnType; Boolean
	 * enableUnitOfMeasure;
	 * 
	 * // Jobs And Estimates
	 * 
	 * String pendingStatus; String awarded; String inProgress; String closed;
	 * String notAwarded; boolean enableCreateEstimates; Boolean
	 * enableProgressInvoicing; boolean warnAboutDuplicateEstimateNumbers;
	 * boolean printZeroAmount;
	 * 
	 * // Multiple Currencies
	 * 
	 * boolean singleCurrency; Set<Currency> currencies;
	 * 
	 * // PayRoll & Employee Preferences
	 * 
	 * int payRollFeatureType;
	 * 
	 * PayRollPrintingPreferences payrollPrintingOptions;
	 * 
	 * // Reminders Preferences
	 * 
	 * int checksToPrint; int payChecksToPrint; int
	 * invoicesOrCreditMemosToPrint; int overdueInvoices; int
	 * salesRecieptsToPrint; int salesOrdersToPrint; int inventoryToReorder; int
	 * assemblyItemsToBuild; int billsToPay; int memorizedTransactionsDue; int
	 * moneyToDeposit; int purchaseOrdersToPrint; int todoNotes;
	 *//**
	 * 
	 */
	/*
	 * int openAuthorizationsToCapture;
	 * 
	 * // Reports And Graph Preferences
	 * 
	 * int summaryBasedOn; int showAccountBy; int ageingReportsType;
	 * 
	 * // Sales And Customers Preferences
	 * 
	 * int usualShippingMethod; boolean usePriceLevels; Boolean
	 * warnAboutDuplicateInvoiceNumbers; Boolean
	 * dontPrintWithZeroAmountsWhenConvertingToInvoice;
	 * 
	 * boolean automaticallyApplyPayments; Boolean
	 * automaticallyCalculatePayments; Boolean
	 * useUndepositedFundAsADefaultDepositToCode;
	 * 
	 * // Sales Tax Preferences
	 * 
	 * boolean chargeSalesTax;
	 * 
	 * boolean enableProductsAndServicesForIncomeAccounts;
	 * 
	 * boolean enableLocationTracking;
	 * 
	 * int locationType;
	 * 
	 * boolean enableClassTracking;
	 * 
	 * boolean enableClassWarning;
	 * 
	 * int typeOfClassesOnSales; // One Per Transaction or One Per Detail line
	 * 
	 * boolean enableCustomTransactionNumbes;
	 * 
	 * boolean enableDelayedCharges;
	 * 
	 * boolean enableDeposits;
	 * 
	 * boolean enableEstimates;
	 * 
	 * int estimatesBillingMethod;
	 * 
	 * String messageToCustomersOnEstimates;
	 * 
	 * boolean enableQuantityAndRate;
	 * 
	 * boolean enableSalesTax;
	 * 
	 * boolean makeAllExistingProductsAndServiesTaxable;
	 * 
	 * boolean makeAllExistingCustomersTaxable;
	 * 
	 * boolean newCustomersAreTaxable;
	 * 
	 * boolean newProductsAndServicesAreTaxable;
	 * 
	 * Account salesTaxAccount;
	 * 
	 * double salesTaxRate;
	 * 
	 * boolean enableServiceDates;
	 * 
	 * boolean enableShipping;
	 * 
	 * Account shippingAccount;
	 * 
	 * int defaultInvoiceTerms;
	 * 
	 * boolean automaticallyApplyCredits;
	 * 
	 * int salesFormDefaultDeliveryMethod;
	 * 
	 * boolean insertAGreetingBeforeMessageText;
	 * 
	 * int greetingFormat;
	 * 
	 * int messageFor;
	 * 
	 * String emailSubject;
	 * 
	 * String emailMessage;
	 * 
	 * boolean copyMeOnAllFormsSentByEmail;
	 * 
	 * boolean printACopyOnAllFormsSebtByEmail;
	 * 
	 * boolean enableEmailSalesFormAsAttachment;
	 * 
	 * int emailBodyAttachmentType;
	 * 
	 * boolean showAgingInformation;
	 * 
	 * boolean showSummary;
	 * 
	 * int invoiceAutomationMethod;
	 * 
	 * boolean manageBillsToPayLater;
	 * 
	 * boolean enableExpenseTrackingByCustomer;
	 * 
	 * boolean useBillableExpenses;
	 * 
	 * double defaultMarkUpRate;
	 * 
	 * int typeOfBillableExpenseIncomeAccount;
	 * 
	 * Account incomeAccount;
	 * 
	 * boolean enableMultipleSpliLines;
	 * 
	 * boolean duplicateCheckWarning;
	 * 
	 * boolean duplicateBillWarning;
	 * 
	 * int defaultBillTerms;
	 * 
	 * boolean automaticallyApplyBillPayments;
	 * 
	 * boolean useServiesForTimeTracking;
	 * 
	 * boolean billCustomerForTime;
	 * 
	 * boolean showBillingRateToEmployeesAndSubContractors;
	 */

}
