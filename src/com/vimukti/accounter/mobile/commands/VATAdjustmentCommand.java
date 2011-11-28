package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.mobile.requirements.TaxItemRequirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;

public class VATAdjustmentCommand extends NewAbstractTransactionCommand {

	private static final String IS_INCREASE_VATLINE = "isIncreaseVatLine";
	private static final String ADJUSTMENT_ACCOUNT = "adjustmentAccount";
	private static final String TAX_AGENCY = "taxAgency";
	private static final String TAX_ITEM = "taxItem";
	private static final String AMOUNT = "amount";
	private static final String IS_SALES = "isSales";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnterName(getMessages().taxAgency()), getMessages()
				.taxAgency(), false, true, new ChangeListner<TAXAgency>() {

			@Override
			public void onSelection(TAXAgency value) {
				get(TAX_ITEM).setValue(null);
			}
		}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().taxAgency());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_SALES, false) {

			@Override
			protected String getTrueString() {
				return "Sales Type Activated";
			}

			@Override
			protected String getFalseString() {
				return "Purchase Type Activated";
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				TAXAgency taxAgency = get(TAX_AGENCY).getValue();
				if (taxAgency.getPurchaseLiabilityAccount() != null
						&& taxAgency.getSalesLiabilityAccount() != null) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (context.getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency.getFormalName(); }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (context.getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue())
		 * .equals(context.getPreferences().getPrimaryCurrency())) { return
		 * super.run(context, makeResult, list, actions); } } return null;
		 * 
		 * } });
		 */

		list.add(new TaxItemRequirement(TAX_ITEM, getMessages()
				.pleaseEnterName(getMessages().taxItem()), getMessages()
				.taxItem(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxItem());
			}

			@Override
			protected List<TAXItem> getLists(Context context) {
				return getTaxItemsListForSelectedTaxAgency();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().taxItem());
			}

			@Override
			protected boolean filter(TAXItem e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new AccountRequirement(ADJUSTMENT_ACCOUNT, getMessages()
				.pleaseEnterName(getMessages().adjustmentAccount()),
				getMessages().adjustmentAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().adjustmentAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive();
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().adjustmentAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getMessages().amount()), getMessages().amount(), false, true));

		list.add(new BooleanRequirement(IS_INCREASE_VATLINE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().increaseVATLine();
			}

			@Override
			protected String getFalseString() {
				return getMessages().decreaseVATLine();
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().orderNo()), getMessages().orderNo(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	protected List<TAXItem> getTaxItemsListForSelectedTaxAgency() {
		Set<TAXItem> taxItems = getCompany().getTaxItems();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ArrayList<TAXItem> arrayList = new ArrayList<TAXItem>();
		if (taxAgency == null || taxItems == null) {
			return arrayList;
		}
		for (TAXItem taxItem : taxItems) {
			if (taxItem.getTaxAgency().getID() == taxAgency.getID()) {
				arrayList.add(taxItem);
			}
		}
		return arrayList;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXAdjustment taxAdjustment = new ClientTAXAdjustment();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		TAXItem taxItem = get(TAX_ITEM).getValue();
		Account account = get(ADJUSTMENT_ACCOUNT).getValue();
		Double amount = get(AMOUNT).getValue();
		boolean isIncreaseVatLine = get(IS_INCREASE_VATLINE).getValue();
		ClientFinanceDate date = get(DATE).getValue();
		String number = get(ORDER_NO).getValue();
		String memo = get(MEMO).getValue();
		taxAdjustment.setTaxItem(taxItem.getID());
		taxAdjustment.setTaxAgency(taxAgency.getID());
		taxAdjustment.setAdjustmentAccount(account.getID());
		taxAdjustment.setTotal(amount);
		taxAdjustment.setIncreaseVATLine(isIncreaseVatLine);
		taxAdjustment.setDate(date.getDate());
		taxAdjustment.setTransactionDate(date.getDate());
		taxAdjustment.setNumber(number);
		if (taxAgency.getSalesLiabilityAccount() != null
				&& taxAgency.getPurchaseLiabilityAccount() != null) {
			taxAdjustment.setSales((Boolean) get(IS_SALES).getValue());
		} else {
			taxAdjustment
					.setSales(taxAgency.getSalesLiabilityAccount() != null);
		}

		taxAdjustment.setMemo(memo);

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * taxAdjustment.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * taxAdjustment.setCurrencyFactor(factor); }
		 */

		create(taxAdjustment, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().vatAdjustment());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().vatAdjustment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_INCREASE_VATLINE).setDefaultValue(true);
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								ClientTransaction.TYPE_ADJUST_VAT_RETURN,
								getCompany()));
		get(MEMO).setDefaultValue(new String());
		get(IS_SALES).setDefaultValue(true);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().vatAdjustment());
	}

}
