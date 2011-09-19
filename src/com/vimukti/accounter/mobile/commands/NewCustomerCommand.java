package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class NewCustomerCommand extends AbstractTransactionCommand {
	private static final String INPUT_ATTR = "input";
	private static final int SALESPERSON_TO_SHOW = 5;
	private static final int PRICELEVEL_TO_SHOW = 5;
	private static final int CREDITRATING_TO_SHOW = 5;
	private static final int CUSTOMERGROUP_TO_SHOW = 5;
	protected static final String NUMBER = "customerNumber";
	protected static final String BALANCE = "balance";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBADRESS = "webPageAdress";
	private static final String BANK_NAME = "bankName";
	private static final String BANK_ACCOUNT_NUM = "bankAccountNum";
	private static final String BANK_BRANCH = "bankBranch";
	private static final String VATREGISTER_NUM = "vatRegisterationNum";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("customerName", false, true));
		list.add(new Requirement("customerNumber", false, true));
		list.add(new ObjectListRequirement("customerContact", true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("primary", true, true));
				list.add(new Requirement("contactName", false, true));
				list.add(new Requirement("title", true, true));
				list.add(new Requirement("businessPhone", true, true));
				list.add(new Requirement("email", true, true));

			}
		});
		list.add(new Requirement("isactive", true, true));
		list.add(new Requirement("customerSinceDate", true, true));
		list.add(new Requirement("balance", true, true));
		list.add(new Requirement("balanceAsOfDate", true, true));
		list.add(new Requirement("address", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("fax", true, true));
		list.add(new Requirement("email", true, true));
		list.add(new Requirement("webPageAdress", true, true));
		list.add(new Requirement("salesPerson", true, true));
		list.add(new Requirement("priceLevel", true, true));
		list.add(new Requirement("creditRating", true, true));
		list.add(new Requirement("bankName", true, true));
		list.add(new Requirement("bankAccountNum", true, true));
		list.add(new Requirement("bankBranch", true, true));
		list.add(new Requirement("paymentMethod", true, true));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("cusomerGroup", true, true));
		list.add(new Requirement("vatRegisterationNum", true, true));
		list.add(new Requirement("customerVatCode", true, true));

	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(CONTACT_PROCESS)) {
				result = contactProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = customerNameRequirement(context);
		if (result == null) {
			// TODO
		}
		result = customerNumberRequirement(context);
		if (result == null) {
			// TODO
		}

		result = optionalRequirements(context);
		if (result == null) {
			// TODO
		}
		return createCustomerObject(context);
	}

	/*
	 * * customer Number.
	 * 
	 * @param context
	 * 
	 * @return {@link Result}
	 */
	private Result customerNumberRequirement(Context context) {
		Requirement customerNumReq = get("customerNumber");
		if (!customerNumReq.isDone()) {
			String customerNum = context.getString();
			if (customerNum != null) {
				customerNumReq.setValue(customerNum);
			} else {
				return number(context, "Please Enter the Customer Number.",
						null);
			}
		}
		String input = (String) context.getAttribute("input");
		if (input.equals(NUMBER)) {
			customerNumReq.setValue(input);
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createCustomerObject(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result optionalRequirements(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				return contact(context, "Enter the Contact Details", null);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		ResultList list = new ResultList("values");

		String customerName = (String) get("customerName").getValue();
		Record nameRecord = new Record(customerName);
		nameRecord.add("Name", "customerName");
		nameRecord.add("Value", customerName);
		list.add(nameRecord);

		Requirement contactReq = get("customerContact");
		List<Contact> contacts = contactReq.getValue();
		selection = context.getSelection("customerContact");
		if (selection != null) {
			Result contact = contact(context, "customer contact",
					(Contact) selection);
			if (contact != null) {
				return contact;
			}
		}

		// String customerContact = (String) get("customerContact").getValue();
		// Record customerContactRecord = new Record(customerContact);
		// customerContactRecord.add("Name", "customerContact");
		// customerContactRecord.add("Value", customerContact);
		// list.add(customerContactRecord);

		boolean isActive = (Boolean) get("isactive").getDefaultValue();
		Record isActiveRecord = new Record(isActive);
		isActiveRecord.add("Name", "Is Active");
		isActiveRecord.add("Value", isActive);
		list.add(isActiveRecord);

		Result result = customerSinceDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = balanceRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = balanceAsOfDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = faxNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = emailRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = phoneNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = webAdressRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = salesPersonRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = priceLevelRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = creditRatingRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankNameRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankAccountNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankBranchRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = customerGroupRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = vatRegisterationNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String vatRegisterationNum = (String) get("vatRegisterationNum")
				.getValue();
		Record vatRegisterationNumRecord = new Record("vatRegisterationNum");
		vatRegisterationNumRecord.add("Name", "Vat Registeration Number");
		vatRegisterationNumRecord.add("Value", vatRegisterationNum);
		list.add(vatRegisterationNumRecord);

		result = customerVatCodeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Customer is ready to create with following values.");
		result.add(list);
		result.add("Items:-");
		ResultList items = new ResultList("customerContact");
		for (Contact item : contacts) {
			Record itemRec = new Record(item);
			itemRec.add("primary", item.getVersion());
			itemRec.add("contactName", item.getName());
			itemRec.add("title", item.getTitle());
			itemRec.add("businessPhone", item.getBusinessPhone());
			itemRec.add("email", item.getEmail());
		}

		result.add(items);
		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_CONTACTS);
		moreItems.add("", "Add more contacts");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Customer.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	private Result vatRegisterationNumRequirement(Context context,
			ResultList list, Object selection) {

		Requirement req = get("vatRegisterationNum");
		String vatRegisterationNum = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("vatRegisterationNum")) {
			String order = context.getSelection(VATREGISTER_NUM);
			if (order == null) {
				order = context.getString();
			}
			vatRegisterationNum = order;
			req.setDefaultValue(vatRegisterationNum);
		}

		if (selection == vatRegisterationNum) {
			context.setAttribute(INPUT_ATTR, "vatRegisterationNum");
			return text(context, "Enter vatRegisteration Number ",
					vatRegisterationNum);
		}

		Record vatRegisterationNumRecord = new Record(vatRegisterationNum);
		vatRegisterationNumRecord.add("Name", "vatRegisterationNum");
		vatRegisterationNumRecord.add("Value", vatRegisterationNum);
		list.add(vatRegisterationNumRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankBranchRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("bankBranch");
		String bankBranch = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("bankBranch")) {
			String order = context.getSelection(BANK_BRANCH);
			if (order == null) {
				order = context.getString();
			}
			bankBranch = order;
			req.setDefaultValue(bankBranch);
		}

		if (selection == bankBranch) {
			context.setAttribute(INPUT_ATTR, "bankBranch");
			return text(context, "Enter bankBranch Name ", bankBranch);
		}

		Record bankBranchRecord = new Record(bankBranch);
		bankBranchRecord.add("Name", "bankBranch");
		bankBranchRecord.add("Value", bankBranch);
		list.add(bankBranchRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankAccountNumRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("bankAccount");
		String bankAccountNumber = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("bankAccount")) {
			String order = context.getSelection(BANK_ACCOUNT_NUM);
			if (order == null) {
				order = context.getString();
			}
			bankAccountNumber = order;
			req.setDefaultValue(bankAccountNumber);
		}

		if (selection == bankAccountNumber) {
			context.setAttribute(INPUT_ATTR, "bankAccount");
			return text(context, "Enter bankAccount Number ", bankAccountNumber);
		}

		Record bankAccountNumRecord = new Record(bankAccountNumber);
		bankAccountNumRecord.add("Name", "bankAccount");
		bankAccountNumRecord.add("Value", bankAccountNumber);
		list.add(bankAccountNumRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankNameRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("webPageAdress");
		String bankName = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("webPageAdress")) {
			String order = context.getSelection(BANK_NAME);
			if (order == null) {
				order = context.getString();
			}
			bankName = order;
			req.setDefaultValue(bankName);
		}

		if (selection == bankName) {
			context.setAttribute(INPUT_ATTR, "webPageAdress");
			return text(context, "Enter webPageAdress ", bankName);
		}

		Record bankNameRecord = new Record(bankName);
		bankNameRecord.add("Name", "webPageAdress");
		bankNameRecord.add("Value", bankName);
		list.add(bankNameRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result webAdressRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("webPageAdress");
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("webPageAdress")) {
			String order = context.getSelection(WEBADRESS);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, "webPageAdress");
			return text(context, "Enter webPageAdress ", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "webPageAdress");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result emailRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("email");
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("email")) {
			String order = context.getSelection(EMAIL);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, "email");
			return text(context, "Enter email ", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "email");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result faxNumRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("fax");
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("fax")) {
			String order = context.getSelection(FAX);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, "fax");
			return text(context, "Enter Fax Number", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "fax");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result phoneNumRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("phone");
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("phone")) {
			String order = context.getSelection(PHONE);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, "phone");
			return text(context, "Enter Phone Number", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "phone");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result balanceRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("balance");
		Double balance = (Double) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("balance")) {
			Double order = context.getSelection(BALANCE);
			if (order == null) {
				order = context.getDouble();
			}
			balance = order;
			req.setDefaultValue(balance);
		}

		if (selection == balance) {
			context.setAttribute(INPUT_ATTR, "balance");
			return amount(context, "Enter Balance", balance);
		}

		Record balanceRecord = new Record(balance);
		balanceRecord.add("Name", "Balance");
		balanceRecord.add("Value", balance);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result customerVatCodeRequirement(Context context, ResultList list,
			Object selection) {

		Object customerVatCodeObj = context.getSelection("customerVatCode");
		Requirement customerVatCodeReq = get("customerVatCode");
		TAXCode vatCode = (TAXCode) customerVatCodeReq.getValue();

		if (selection == vatCode) {
			return taxCode(context, vatCode);
		}

		if (customerVatCodeObj != null) {
			vatCode = (TAXCode) customerVatCodeObj;
			customerVatCodeReq.setDefaultValue(vatCode);
		}

		Record customerVatCodeRecord = new Record(vatCode);
		customerVatCodeRecord.add("Name", "Customer VatCode");
		customerVatCodeRecord.add("Value", vatCode.getName());
		list.add(customerVatCodeRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * CustomerGroup
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link CustomerGroupResult}
	 */
	private Result customerGroupRequirement(Context context, ResultList list,
			Object selection) {

		Object customerGroupObj = context.getSelection("cusomerGroup");
		Requirement customerGroupReq = get("cusomerGroup");
		CustomerGroup customerGroup = (CustomerGroup) customerGroupReq
				.getValue();

		if (selection == customerGroup) {
			return customerGroups(context, customerGroup);
		}

		if (customerGroupObj != null) {
			customerGroup = (CustomerGroup) customerGroupObj;
			customerGroupReq.setDefaultValue(customerGroup);
		}

		Record customerGroupRecord = new Record(customerGroup);
		customerGroupRecord.add("Name", "Cusomer Group");
		customerGroupRecord.add("Value", customerGroup.getName());
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result customerGroups(Context context,
			CustomerGroup oldCustomerGroup) {
		List<CustomerGroup> customerGroups = getCustomerGroupsList();
		Result result = context.makeResult();
		result.add("Select CustomerGroup");

		ResultList list = new ResultList("customerGroup");
		int num = 0;
		if (oldCustomerGroup != null) {
			list.add(createTAXCodeRecord(oldCustomerGroup));
			num++;
		}
		for (CustomerGroup customerGroup : customerGroups) {
			if (customerGroup != oldCustomerGroup) {
				list.add(createTAXCodeRecord(customerGroup));
				num++;
			}
			if (num == CUSTOMERGROUP_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create CustomerGroup");
		result.add(commandList);

		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result paymentMethodRequirement(Context context, ResultList list,
			Object selection) {

		Result result = context.makeResult();

		result.add("Select PaymentMethod");

		ResultList paymethodResultList = new ResultList("paymentmethod");

		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.cash()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.check()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.creditCard()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.directDebit()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.masterCard()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.standingOrder()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.onlineBanking()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.switchMaestro()));

		result.add(paymethodResultList);

		return result;

	}

	private Record createPayMentMethodRecord(String paymentMethod) {
		Record record = new Record(paymentMethod);
		record.add("Name", "Payment Method");
		record.add("value", paymentMethod);
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result creditRatingRequirement(Context context, ResultList list,
			Object selection) {

		Object crediRatingObj = context.getSelection("creditRating");
		Requirement creditRatingReq = get("creditRating");
		CreditRating creditRating = (CreditRating) creditRatingReq.getValue();

		if (selection == creditRating) {
			return creditRatings(context, creditRating);
		}

		if (crediRatingObj != null) {
			creditRating = (CreditRating) crediRatingObj;
			creditRatingReq.setDefaultValue(creditRating);
		}

		Record priceLevelRecord = new Record(creditRating);
		priceLevelRecord.add("Name", "Credit Rating");
		priceLevelRecord.add("Value", creditRating.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result creditRatings(Context context, CreditRating oldCreditRating) {

		List<CreditRating> creditRatings = getCreditRatingsList();
		Result result = context.makeResult();
		result.add("Select CreditRating");

		ResultList list = new ResultList("creditRating");
		int num = 0;
		if (oldCreditRating != null) {
			list.add(createCreditRatingRecord(oldCreditRating));
			num++;
		}
		for (CreditRating priceLevel : creditRatings) {
			if (priceLevel != oldCreditRating) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == CREDITRATING_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create creditRating");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldCreditRating
	 * @return
	 */
	private Record createCreditRatingRecord(CreditRating oldCreditRating) {
		Record record = new Record(oldCreditRating);
		record.add("Name", oldCreditRating.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result priceLevelRequirement(Context context, ResultList list,
			Object selection) {

		Object priceLevelObj = context.getSelection("priceLevel");
		Requirement priceLevelReq = get("priceLevel");
		PriceLevel priceLevel = (PriceLevel) priceLevelReq.getValue();

		if (selection == priceLevel) {
			return priceLevels(context, priceLevel);
		}

		if (priceLevelObj != null) {
			priceLevel = (PriceLevel) priceLevelObj;
			priceLevelReq.setDefaultValue(priceLevel);
		}

		Record priceLevelRecord = new Record(priceLevel);
		priceLevelRecord.add("Name", "Price Level");
		priceLevelRecord.add("Value", priceLevel.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result priceLevels(Context context, PriceLevel oldPriceLevel) {

		List<PriceLevel> priceLevels = getPriceLevelsList();
		Result result = context.makeResult();
		result.add("Select PriceLevel");

		ResultList list = new ResultList("priceLevel");
		int num = 0;
		if (oldPriceLevel != null) {
			list.add(createCreditRatingRecord(oldPriceLevel));
			num++;
		}
		for (PriceLevel priceLevel : priceLevels) {
			if (priceLevel != oldPriceLevel) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == PRICELEVEL_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create priceLevel");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldPriceLevel
	 * @return
	 */
	private Record createCreditRatingRecord(PriceLevel oldPriceLevel) {
		Record record = new Record(oldPriceLevel);
		record.add("Name", oldPriceLevel.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result salesPersonRequirement(Context context, ResultList list,
			Object selection) {

		Object salesPersonObj = context.getSelection("salesPerson");
		Requirement salesPersonReq = get("salesPerson");
		SalesPerson salesPerson = (SalesPerson) salesPersonReq.getValue();

		if (selection == salesPerson) {
			return salesPersons(context, salesPerson);
		}
		if (salesPersonObj != null) {
			salesPerson = (SalesPerson) salesPersonObj;
			salesPersonReq.setDefaultValue(salesPerson);
		}

		Record salesPersonRecord = new Record(salesPerson);
		salesPersonRecord.add("Name", "sales Person");
		salesPersonRecord.add("Value", salesPerson.getName());
		list.add(salesPersonRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * Bill To Address
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result billToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("billTo");
		Address billTo = (Address) req.getValue();

		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("billTo")) {
			Address input = context.getSelection("address");
			if (input == null) {
				input = context.getAddress();
			}
			billTo = input;
			req.setDefaultValue(billTo);
		}

		if (selection == billTo) {
			context.setAttribute("input", "billTo");
			return address(context, "billTo", billTo);
		}

		Record billToRecord = new Record(billTo);
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo.toString());
		list.add(billToRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * balanceAsOfDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result balanceAsOfDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get("date");
		Date balanceAsofdate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("balanceAsOfDate")) {
			Date date = context.getSelection("date");
			if (date == null) {
				date = context.getDate();
			}
			balanceAsofdate = date;
			dateReq.setDefaultValue(balanceAsofdate);
		}
		if (selection == balanceAsofdate) {
			context.setAttribute("input", "balanceAsOfDate");
			return date(context, "Enter BalanceAsOf  Date", balanceAsofdate);
		}

		Record transDateRecord = new Record(balanceAsofdate);
		transDateRecord.add("Name", "Balance AsOf Date");
		transDateRecord.add("Value", balanceAsofdate.toString());
		list.add(transDateRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * customerSinceDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result customerSinceDateRequirement(Context context,
			ResultList list, Object selection) {
		Requirement dateReq = get("date");
		Date customerSincedate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("customerSinceDate")) {
			Date date = context.getSelection("date");
			if (date == null) {
				date = context.getDate();
			}
			customerSincedate = date;
			dateReq.setDefaultValue(customerSincedate);
		}
		if (selection == customerSincedate) {
			context.setAttribute("input", "customerSinceDate");
			return date(context, "Enter Customer Since Date", customerSincedate);
		}

		Record transDateRecord = new Record(customerSincedate);
		transDateRecord.add("Name", "Customer SinceDate");
		transDateRecord.add("Value", customerSincedate.toString());
		list.add(transDateRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result customerNameRequirement(Context context) {
		Requirement requirement = get("customerName");
		if (!requirement.isDone()) {
			String customerName = context.getSelection(TEXT);
			if (customerName != null) {
				requirement.setValue(customerName);
			} else {
				return text(context, "Please enter the  Customer Name", null);
			}
		}
		String input = (String) context.getAttribute("input");
		if (input.equals("name")) {
			requirement.setValue(input);
		}
		return null;
	}

	/**
	 * paymentTerms
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link PaymentTerms Result}
	 */
	private Result paymentTermRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentObj = context.getSelection(PAYMENT_TERMS);
		Requirement paymentReq = get("paymentTerms");
		PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();

		if (selection == paymentTerm) {
			return paymentTerms(context, paymentTerm);

		}
		if (payamentObj != null) {
			paymentTerm = (PaymentTerms) payamentObj;
			paymentReq.setDefaultValue(paymentTerm);
		}

		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * SalesPerson
	 * 
	 * @param context
	 * 
	 * @param string
	 * @return {@link SalesPerson Result}
	 */
	protected Result salesPersons(Context context, SalesPerson oldsalesPerson) {
		List<SalesPerson> salesPersons = getsalePersonsList();
		Result result = context.makeResult();
		result.add("Select SalesPerson");

		ResultList list = new ResultList("salesPerson");
		int num = 0;
		if (oldsalesPerson != null) {
			list.add(createSalesPersonRecord(oldsalesPerson));
			num++;
		}
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson != oldsalesPerson) {
				list.add(createSalesPersonRecord(salesPerson));
				num++;
			}
			if (num == SALESPERSON_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create SalesPerson");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldsalesPerson
	 * @return {@link Record}
	 */
	private Record createSalesPersonRecord(SalesPerson oldsalesPerson) {
		Record record = new Record(oldsalesPerson);
		record.add("Name", oldsalesPerson.getName());
		return record;
	}

	private List<SalesPerson> getsalePersonsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CustomerGroup> getCustomerGroupsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param oldCustomerGroup
	 * @return
	 */
	private Record createTAXCodeRecord(CustomerGroup oldCustomerGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CreditRating> getCreditRatingsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<PriceLevel> getPriceLevelsList() {
		// TODO Auto-generated method stub
		return null;
	}
}
