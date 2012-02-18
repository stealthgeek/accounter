package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobProfitability;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class JobProfitabilitySummaryServerReport extends
		AbstractFinaneReport<JobProfitability> {

	private long customerId;
	private String customerName;

	public JobProfitabilitySummaryServerReport(
			IFinanceReport<JobProfitability> reportView) {
		this.reportView = reportView;
	}

	public JobProfitabilitySummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", "Act. Cost", "Act. Revenue", "Difference" };
	}

	@Override
	public String getTitle() {
		return messages.jobProfitabilitySummary();
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", "Act. Cost", "Act. Revenue", "Difference" };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(JobProfitability record) {
		if (sectionDepth == 0) {
			this.customerId = record.getCustomerId();
			this.customerName=	Accounter.getCompany().getCustomer(customerId).getName();
			addSection(new String[] {this.customerName}, new String[] { getMessages().total() },
					new int[] { 1, 2, 3 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (customerId != record.getCustomerId()) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(JobProfitability record, int index) {
		switch (index) {
		case 0:
			return record.getName();
		case 1:
			return record.getCostAmount();
		case 2:
			return record.getRevenueAmount();
		case 3:
			return record.getRevenueAmount() - record.getCostAmount();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(JobProfitability obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(JobProfitability obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
