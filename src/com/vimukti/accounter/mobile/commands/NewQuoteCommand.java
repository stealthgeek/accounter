package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

/**
 * 
 * @author SaiPrasad N
 * 
 */
public class NewQuoteCommand extends NewAbstractTransactionCommand {

	private static final String PHONE = "phone";
	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String EXPIRATION_DATE = "expirationDate";
	private static final String PAYMENT_TERMS = "paymentTerms";

	private ClientEstimate estimate;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().payeeNumber(Global.get().Customer()), false,
				true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return new ArrayList<Customer>(context.getCompany()
						.getCustomers());
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
		 * .equals(context.getPreferences() .getPrimaryCurrency())) { return
		 * super.run(context, makeResult, list, actions); } } return null; } });
		 */

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				false, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.getType() == Item.TYPE_SERVICE) {
						items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnterName(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getConstants().contactName()), getConstants().contacts(), true,
				true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(((Customer) NewQuoteCommand.this
						.get(CUSTOMER).getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((Customer) get(CUSTOMER).getValue()).getName();
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phone(), true,
				true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));

		list.add(new DateRequirement(EXPIRATION_DATE, getMessages()
				.pleaseEnter(getConstants().expirationDate()), getConstants()
				.expirationDate(), true, false));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& !context.getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Customer customer = get(CUSTOMER).getValue();
		estimate.setCustomer(customer.getID());
		estimate.setEstimateType(ClientEstimate.QUOTES);
		estimate.setType(ClientTransaction.TYPE_ESTIMATE);
		ClientFinanceDate date = get(DATE).getValue();
		estimate.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		estimate.setNumber(number);

		Contact contact = get(CONTACT).getValue();
		if (contact != null)
			estimate.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		estimate.setAddress(billTo);

		String phone = get(PHONE).getValue();
		estimate.setPhone(phone);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			estimate.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * estimate.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * estimate.setCurrencyFactor(factor); }
		 */

		estimate.setTransactionItems(items);

		PaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		estimate.setPaymentTerm(paymentTerm.getID());

		ClientFinanceDate d = get(DATE).getValue();
		estimate.setDate(d.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		estimate.setDeliveryDate(deliveryDate.getDate());
		ClientFinanceDate expiryDdate = get(EXPIRATION_DATE).getValue();
		estimate.setExpirationDate(expiryDdate.getDate());
		estimate.setCurrencyFactor(1);
		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);
		double taxTotal = updateTotals(context, estimate, true);
		estimate.setTaxTotal(taxTotal);
		create(estimate, context);

		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return estimate.getID() == 0 ? getMessages().creating(
				getConstants().quote()) : "Quote updating..";
	}

	@Override
	protected String getDetailsMessage() {
		return estimate.getID() == 0 ? getMessages().readyToCreate(
				getConstants().quote())
				: "Quote is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get("expirationDate").setDefaultValue(new ClientFinanceDate());

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */

	}

	@Override
	public String getSuccessMessage() {
		return estimate.getID() == 0 ? getMessages().createSuccessfully(
				getConstants().quote()) : getMessages().updateSuccessfully(
				getConstants().quote());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Quote to update.");
				return "Quotes List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientEstimate estimateByNum = (ClientEstimate) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.ESTIMATE);
			if (estimateByNum == null) {
				addFirstMessage(context, "Select a Quote to update.");
				return "Quotes List " + string;
			}
			estimate = estimateByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			estimate = new ClientEstimate();
		}
		return null;
	}

	private void setValues() {
		get(DATE).setValue(estimate.getDate());
		get(NUMBER).setValue(estimate.getNumber());
		get(ITEMS).setValue(estimate.getTransactionItems());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(estimate.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(estimate.getDeliveryDate()));
		get(EXPIRATION_DATE).setValue(
				new ClientFinanceDate(estimate.getExpirationDate()));
		get(CONTACT).setValue(toServerContact(estimate.getContact()));
		get(BILL_TO).setValue(estimate.getAddress());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(estimate.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(MEMO).setValue(estimate.getMemo());
		/* get(CURRENCY_FACTOR).setValue(estimate.getCurrencyFactor()); */
		get(IS_VAT_INCLUSIVE).setValue(estimate.isAmountsIncludeVAT());
		get(PHONE).setValue(estimate.getPhone());
	}

}
