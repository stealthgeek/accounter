package com.vimukti.accounter.mobile.requirements;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;

public abstract class AccountRequirement extends ListRequirement<Account> {

	public AccountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Account> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setAccountValue(getValue());
		return super.run(context, makeResult, list, actions);
	}

	private void setAccountValue(Object value) {
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Account account = (Account) value;
			account = (Account) currentSession.load(Account.class,
					account.getID());
			super.setValue(account);
		}
	}

	@Override
	public void setValue(Object value) {
		setAccountValue(value);
	}

	@Override
	protected Record createRecord(Account value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().number(), value.getNumber());
		record.add(getMessages().balance(),
				value.getTotalBalanceInAccountCurrency());
		return record;
	}

	@Override
	protected String getDisplayValue(Account value) {
		return value != null ? value.getNumber() + "-" + value.getName() + "-"
				+ CommandUtils.getAccountTypeString(value.getType()) : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createAccount");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().Account());
	}

	@Override
	protected boolean filter(Account e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase())
				|| e.getNumber().startsWith(
						String.valueOf(getNumberFromString(name)));
	}
}
