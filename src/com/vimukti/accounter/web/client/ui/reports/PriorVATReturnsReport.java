package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;

public class PriorVATReturnsReport extends AbstractReportView<VATSummary> {

	private int row = -1;
	private long vatAgency;

	public PriorVATReturnsReport() {

		super(false, Accounter.messages()
				.pleaseSelectVATAgencyAndEndingDateToViewReport());
		isVATPriorReport = true;
		isVATSummaryReport = true;
		this.serverReport = new PriorVATReturnsServerReport(this);

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {
			List<VATSummary> summary = (List<VATSummary>) data;
			TaxAgencyStartDateEndDateToolbar toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
			// TODO set taxagency item to combo
			toolBar.taxAgencyCombo.setDisabled(true);
			toolBar.fromItem.setEnteredDate(this.startDate);
			toolBar.toItem.setEnteredDate(this.endDate);
			toolBar.fromItem.setDisabled(true);
			toolBar.toItem.setDisabled(true);
			toolBar.updateButton.setEnabled(false);
			this.serverReport.initRecords(summary);
		} else {
			super.initData();
		}
	}

	@Override
	public void OnRecordClick(VATSummary record) {

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_TAXAGENCY;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate end) {
		this.row = -1;
		if (this.serverReport instanceof PriorVATReturnsServerReport)
			((PriorVATReturnsServerReport) this.serverReport).row = -1;
		Accounter.createReportService().getPriorReturnVATSummary(vatAgency,
				end, this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.row = -1;
		if (this.serverReport instanceof PriorVATReturnsServerReport)
			((PriorVATReturnsServerReport) this.serverReport).row = -1;
		Accounter.createReportService().getPriorReturnVATSummary(vatAgency,
				endDate, this);
		this.vatAgency = vatAgency;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		removeLoadingImage();

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getDate())), Integer.parseInt(String
				.valueOf(((PriorVATReturnToolBar) toolbar).getSelectedEndDate()
						.getDate())), 136, "", "", vatAgency);
	}

	@Override
	public void printPreview() {

	}

	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().toLowerCase().compareTo(
		// obj2.getName().toLowerCase());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getDate())), Integer.parseInt(String
				.valueOf(((PriorVATReturnToolBar) toolbar).getSelectedEndDate()
						.getDate())), 136, "", "", vatAgency);
	}

}
