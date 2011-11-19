package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewVendorPrepaymentCommand extends NewAbstractTransactionCommand {
	ClientPayBill paybill;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context,
						"Select a Vendor Prepayment to update.");
				return "Vendor Payments List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientPayBill invoiceByNum = (ClientPayBill) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.PAYBILL);
			if (invoiceByNum == null) {
				addFirstMessage(context,
						"Select a Vendor Prepayment to update.");
				return "Vendor Payments List " + string;
			}
			paybill = invoiceByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			paybill = new ClientPayBill();
		}
		return null;
	}

	private void setValues() {
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(paybill.getVendor(),
						AccounterCoreType.VENDOR));
		get(BILL_TO).setValue(paybill.getAddress());
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(paybill.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(CURRENCY_FACTOR).setValue(paybill.getCurrencyFactor());
		get(AMOUNT).setValue(paybill.getTotal());
		get(PAYMENT_METHOD).setValue(paybill.getPaymentMethod());
		get(TO_BE_PRINTED).setValue(paybill.isToBePrinted());
		get(MEMO).setValue(paybill.getMemo());
		get(CHEQUE_NO).setValue(paybill.getCheckNumber());
		get(DATE).setValue(paybill.getDate());
	}

	@Override
	protected String getWelcomeMessage() {
		return paybill.getID() == 0 ? getMessages().create(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: "Update Vendor Prepayment command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return paybill.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: "Vendor prepayment is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_VENDOR_PAYMENT,
						context.getCompany()));
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
		get(CURRENCY).setDefaultValue(null);
	}

	@Override
	public String getSuccessMessage() {

		return paybill.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: getMessages().updateSuccessfully(
						getMessages().payeePrePayment(Global.get().Vendor()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getConstants().Vendor()), getConstants().vendor(), false, true,
				null)

		{

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return new ArrayList<Vendor>(context.getCompany().getVendors());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (context.getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(context.getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_BANK
									|| e.getType() == Account.TYPE_OTHER_ASSET) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getConstants().cash(), getConstants().creditCard(),
						getConstants().check(), getConstants().directDebit(),
						getConstants().masterCard(),
						getConstants().onlineBanking(),
						getConstants().standingOrder(),
						getConstants().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});
		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), false, true));

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getConstants().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return "Not Printed ";
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) get(TO_BE_PRINTED).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Vendor vendor = (Vendor) get(VENDOR).getValue();
		ClientAddress billTo = (ClientAddress) get(BILL_TO).getValue();
		Account pay = (Account) get(PAY_FROM).getValue();

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				paybill.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			paybill.setCurrencyFactor(factor);
		}
		double amount = get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		ClientFinanceDate transactionDate = get(DATE).getValue();
		paybill.setDate(transactionDate.getDate());
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setVendor(vendor.getID());
		paybill.setAddress(billTo);
		paybill.setPayFrom(pay.getID());
		paybill.setTotal(amount);
		paybill.setStatus(ClientPayBill.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED);
		paybill.setPaymentMethod(paymentMethod);
		paybill.setMemo(memo);
		paybill.setToBePrinted(toBePrinted);
		paybill.setCheckNumber(chequeNumber);
		create(paybill, context);
		return null;
	}

}