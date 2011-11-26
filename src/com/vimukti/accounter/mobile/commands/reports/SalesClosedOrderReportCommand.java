package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesClosedOrderReportCommand extends
		NewAbstractReportCommand<OpenAndClosedOrders> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(OpenAndClosedOrders record) {
		Record openRecord = new Record(record);
		if (record.getTransactionDate() != null)
			openRecord.add(getMessages().orderDate(),
					UIUtils.getDateByCompanyType(record.getTransactionDate()));
		else
			openRecord.add("", "");
		openRecord.add(Global.get().Customer(),
				record.getVendorOrCustomerName());
		openRecord.add(getMessages().description(), record.getDescription());
		openRecord.add(getMessages().quantity(),
				((Double) record.getQuantity()).toString());
		openRecord.add(getMessages().amount(), record.getAmount());

		return openRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<OpenAndClosedOrders> getRecords() {
		ArrayList<OpenAndClosedOrders> openAndClosedOrders = new ArrayList<OpenAndClosedOrders>();
		try {
			openAndClosedOrders = new FinanceTool().getSalesManager()
					.getClosedSalesOrders(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAndClosedOrders;
	}

	@Override
	protected String addCommandOnRecordClick(OpenAndClosedOrders selection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().reportCommondActivated(
				getMessages().salesCloseOrder());
	}

	@Override
	protected String getShowMessage() {
		return "";
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(getMessages().salesCloseOrder());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesCloseOrder());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().salesCloseOrder());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesCloseOrder());
	}

}
