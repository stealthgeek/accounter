package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewFiscalYearCommand extends NewAbstractCommand {

	private static final String START_DATE = "start date";
	private static final String END_DATE = "end date";
	private static final String STATUS = "status";
	ClientFiscalYear fiscalYear;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new DateRequirement(START_DATE, getMessages().pleaseEnter(
				getMessages().startDate()), getMessages().startDate(), false,
				true));

		list.add(new DateRequirement(END_DATE, getMessages().pleaseEnter(
				getMessages().endDate()), getMessages().endDate(), false,
				true));

		list.add(new NumberRequirement(STATUS, getMessages().pleaseEnter(
				getMessages().status()), getMessages().status(), false, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate startDate = (ClientFinanceDate) get(START_DATE)
				.getValue();
		ClientFinanceDate endDate = (ClientFinanceDate) get(END_DATE)
				.getValue();
		Integer status = Integer.parseInt((String) get(STATUS).getValue());

		fiscalYear.setStartDate(startDate.getDate());
		fiscalYear.setEndDate(endDate.getDate());
		fiscalYear.setStatus(status.intValue());
		create(fiscalYear, context);

		markDone();

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select Fiscal year to update.");
				return "";
			}
			ClientFinanceDate date = context.getDate();
			Integer integer = context.getInteger();
			ClientFiscalYear year = CommandUtils.getFiscalYearByDate(
					date.getDate(), integer, context.getCompany());
			if (year == null) {
				addFirstMessage(context, "Select a fiscal year to update.");
				return "" + string;
			}
			fiscalYear = year;
			setValues();
		} else {
			if (!string.isEmpty()) {
				get(STATUS).setValue(string);
			}
			fiscalYear = new ClientFiscalYear();
		}
		return null;
	}

	private void setValues() {
		get(START_DATE).setValue(fiscalYear.getStartDate());
		get(END_DATE).setValue(fiscalYear.getEndDate());
		get(STATUS).setValue(String.valueOf(fiscalYear.getStatus()));
	}

	@Override
	protected String getWelcomeMessage() {
		return fiscalYear.getID() == 0 ? "Fiscal year commond is activated"
				: "Update Fiscal year command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return fiscalYear.getID() == 0 ? "Fiscal year is ready with the following values"
				: "Fiscal year is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return fiscalYear.getID() == 0 ? "new fiscal year is created successfully"
				: "Fiscal year updated successfully";
	}

}
