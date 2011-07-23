package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListDetailServerReport;

@SuppressWarnings("unchecked")
public class ECSalesListDetailReport extends
		AbstractReportView<ECSalesListDetail> {

	private String vatAgency;

	public ECSalesListDetailReport() {
		this.serverReport = new ECSalesListDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(ECSalesListDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(), record
					.getTransactionStringId()
					+ "");
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		ECSalesList bySalesDetail = (ECSalesList) this.data;

		Accounter.createReportService().getECSalesListDetailReport(
				bySalesDetail.getName(), start.getTime(), end.getTime(), this);

		this.vatAgency = bySalesDetail.getName();

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 143, "", "", vatAgency);

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(ECSalesListDetail obj1, ECSalesListDetail obj2, int col) {
		int ret = obj1.getName().toLowerCase().compareTo(
				obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(), obj2
					.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return obj1.getTransactionNumber().compareTo(
					obj2.getTransactionNumber());
		case 3:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase().compareTo(
					obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getSalesPrice(), obj2
					.getSalesPrice());
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 143, "", "", vatAgency);
	}
}
