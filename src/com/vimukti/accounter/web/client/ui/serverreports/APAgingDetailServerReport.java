package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * Modified By Ravi Kiran.G
 * 
 */
public class APAgingDetailServerReport extends
		AbstractFinaneReport<AgedDebtors> {

	private String sectionName = "";
	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();

	private String curentParent;
	private int precategory;

	public APAgingDetailServerReport(IFinanceReport<AgedDebtors> reportView) {
		this.reportView = reportView;

	}

	public APAgingDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().name(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), /*
												 * "Void", "Payment Terms" ,
												 * "Due Date",
												 */
				getMessages().ageing(), getMessages().amount(), /* "Total" */};
	}

	@Override
	public String getTitle() {
		return getMessages().apAgeingDetails();
	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().financialYearToDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 2)
			return 160;
		else if (index == 1)
			return 100;
		else if (index == 3)
			return 70;
		else if (index == 4)
			return 100;
		else if (index == 5)
			return 150;
		else
			return -1;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// try {
		// onSuccess(this.financeTool.getAgedDebtors(start, end));
		// } catch (DAOException e) {
		// }
		// sectiontypes.clear();
	}

	@Override
	public Object getColumnData(AgedDebtors record, int columnIndex) {
		switch (columnIndex) {
		case 2:
			return ReportUtility.getTransactionName(record.getType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 0:
			return record.getName();
			// return record.getReference();
			// case 4:
			// return record.getIsVoid() ?
			// FinanceApplication.constants()
			// .yes() : FinanceApplication.constants().no();
			// case 5:
			// return record.getPaymentTermName();
			// case 4:
			// return record.getDueDate();
		case 4:
			return record.getAgeing();
		case 5:
			return record.getTotal();
			// case 8:
			// return record.getTotal();
		}
		return null;
	}

	@Override
	public void processRecord(AgedDebtors record) {

		// if (sectionDepth == 0) {
		// // addSection(new String[] { "", "" }, new String[] { "", "", "",
		// // "",
		// // FinanceApplication.constants().total() },
		// // new int[] { 5 });
		// }else
		if (precategory == 1001 || precategory == record.getCategory()) {
			if (addCategoryTypes(record)) {
				return;
			}
		} else if (precategory != 1001 && record.getCategory() != precategory) {
			precategory = record.getCategory();
			endSection();
		} else {
			return;
		}
		// if (sectionDepth == 0) {
		// addSection("", FinanceApplication.constants().total(),
		// new int[] { 7, 8 });
		// } else if (sectionDepth == 1) {
		// // First time
		// this.sectionName = record.getName();
		// addSection(sectionName, "", new int[] { 7, 8 });
		// } else if (sectionDepth == 2) {
		// // No need to do anything, just allow adding this record
		// if (!sectionName.equals(record.getName())) {
		// endSection();
		// endSection();
		// } else {
		// return;
		// }
		// }
		// // Go on recursive calling if we reached this place
		processRecord(record);
	}

	private boolean addCategoryTypes(AgedDebtors record) {

		if (record.getCategory() == 1) {
			precategory = record.getCategory();
			return addOneTothirty(record);
		} else if (record.getCategory() == 2) {
			precategory = record.getCategory();
			return addThirtyToSixty(record);
		} else if (record.getCategory() == 3) {
			precategory = record.getCategory();
			return addSixtyTo90(record);
		} else if (record.getCategory() == 4) {
			precategory = record.getCategory();
			return addGreaterThan90(record);
		} else if (record.getCategory() == 5) {
			precategory = record.getCategory();
			return addTotalBalance(record);
		}
		return true;
	}

	//
	// @Override
	// protected void endAllSections() {
	// for (int i = this.sections.size() - 1; i >= 0; i--) {
	// if (i == 1) {
	// sections.get(0).isaddFooter = true;
	// endSection();
	// } else
	// endSection();
	// }
	// }

	private void addCurrent(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().current())) {
			// closeAllSection();
			addTypeSection(getMessages().current(), "");
		}
	}

	private boolean addOneTothirty(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().days30())) {
			addTypeSection(getMessages().days30(), "");
			return false;
		}
		return true;
	}

	private boolean addThirtyToSixty(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().days60())) {
			addTypeSection(getMessages().days60(), "");
			return false;
		}
		return true;

	}

	private boolean addSixtyTo90(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().days90())) {
			addTypeSection(getMessages().days90(), "");
			return false;
		}
		return true;

	}

	private boolean addGreaterThan90(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().older())) {
			addTypeSection(getMessages().older(), "");
			return false;
		}
		return true;

	}

	private boolean addTotalBalance(AgedDebtors record) {
		if (!sectiontypes.contains(getMessages().totalBalance())) {
			addTypeSection(getMessages().totalBalance(), "");
			return false;
		}
		return true;
	}

	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { title }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 5 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	public void print() {

	}

	// private void printDataForOtherBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
	// headerhtml = headerhtml.replaceAll("td", "th");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
	// headerhtml.indexOf("</tbody>"));
	//
	// String firsRow = "<tr class=\"ReportGridRow\">"
	// + "" + "</tr>";
	// headerhtml = headerhtml + firsRow;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replaceAll("<tbody>", "");
	// gridhtml = gridhtml.replaceAll("</tbody>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }

	// private void printDataForIEBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	// String footerHtml = grid.getFooter();
	//
	// gridhtml = gridhtml.replaceAll("\r\n", "");
	// headerhtml = headerhtml.replaceAll("\r\n", "");
	// footerHtml = footerHtml.replaceAll("\r\n", "");
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(footerHtml, "");
	//
	// headerhtml = headerhtml.replaceAll("TD", "TH");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
	// headerhtml.indexOf("</TBODY>"));
	//
	// String firsRow = "<TR class=ReportGridRow>"
	// + "" + "</TR>";
	// firsRow = firsRow.replaceAll("\r\n", "");
	// headerhtml = headerhtml + firsRow;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replaceAll("<TBODY>", "");
	// gridhtml = gridhtml.replaceAll("</TBODY>", "");
	//
	// String dateRangeHtml = null;
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }

	@Override
	public ClientFinanceDate getEndDate(AgedDebtors obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(AgedDebtors obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	public int sort(AgedDebtors obj1, AgedDebtors obj2, int col) {

		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareTo(obj1.getDate(), obj2.getDate());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());

		case 3:
			// return compareInt(Integer.parseInt(obj1.getNumber()),
			// Integer.parseInt(obj2.getNumber()));
			return UIUtils.compareTo(obj1.getNumber(), obj2.getNumber());
			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 4:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		precategory = 1001;
		types.clear();
		sectiontypes.clear();
		curentParent = "";
		super.resetVariables();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().name(),
				getMessages().date(), getMessages().type(),
				getMessages().number(), /*
												 * "Void", "Payment Terms" ,
												 * "Due Date",
												 */
				getMessages().ageing(), getMessages().amount(), /* "Total" */};
	}
	
}
