package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOpenOrderServerReport;

@SuppressWarnings("unchecked")
public class PurchaseOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	@SuppressWarnings("unused")
	private boolean isPurchases;

	public PurchaseOpenOrderReport() {
		this.serverReport = new PurchaseOpenOrderServerReport(this);
		isPurchases = FinanceApplication.isPurchases();
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(FinanceApplication.getReportsMessages()
				.all(), FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(), record
					.getTransactionID());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_SALES_PURCAHASE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void makeReportRequest(int status, ClientFinanceDate start,
			ClientFinanceDate end) {
		if (status == 1)
			FinanceApplication.createReportService()
					.getPurchaseOpenOrderReport(start.getTime(), end.getTime(),
							this);
		else if (status == 2)
			FinanceApplication.createReportService()
					.getPurchaseCompletedOrderReport(start.getTime(),
							end.getTime(), this);
		else if (status == 3)
			FinanceApplication.createReportService()
					.getPurchaseCancelledOrderReport(start.getTime(),
							end.getTime(), this);
		else
			FinanceApplication.createReportService().getPurchaseOrderReport(
					start.getTime(), end.getTime(), this);

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
				.valueOf(endDate.getTime())), 134, "", "");

		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 134, "", "");
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase().compareTo(
				obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return obj1.getVendorOrCustomerName().toLowerCase().compareTo(
					obj2.getVendorOrCustomerName().toLowerCase());

			// case 2:
			// // if (isPurchases)
			// return obj1.getDescription().toLowerCase().compareTo(
			// obj2.getDescription().toLowerCase());
			// else
			// return UIUtils
			// .compareDouble(obj1.getAmount(), obj2.getAmount());
			// case 2:
			// return UIUtils
			// .compareDouble(obj1.getQuantity(), obj2.getQuantity());

		case 2:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}
}
