package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.Sorting;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.grids.CustomTable;

/**
 * ReportGrid is Table which is used to display reports data in tree structure.
 * 
 * @author kumar kasimala
 * 
 * @param <R>
 */
public class ReportGrid<R> extends CustomTable {

	private String[] columns = {};
	List<R> list = new ArrayList<R>();

	public R selectedObjecd;
	public int maxDepth = 0;

	public AbstractReportView<R> reportView;

	private int columnTypes[];

	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_AMOUNT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_NUMBER = 4;
	public static final int COLUMN_TYPE_PERCENTAGE = 5;

	Sorting<R> sorting;
	Element bodyrowElem;

	public ReportGrid(String[] columns) {
		super(false, true);
		this.columns = columns;
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		setStyleName(Accounter.constants().reportGrid());
	}

	public ReportGrid(String[] columns, boolean showFooter) {
		super(false, showFooter);
		this.columns = columns;
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		setStyleName(Accounter.constants().reportGrid());
	}

	/**
	 * Clear All Rows in Table
	 */
	@Override
	public void clear() {
		this.removeAllRows();
	}

	/**
	 * Add row to this table
	 * 
	 * @param record
	 *            is actual object
	 * @param depth
	 * @param values
	 * @param bold
	 *            whether row font bold or not
	 * @param underline
	 * @param border
	 */
	public void addRow(R record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {

		list.add(record);
		int rowCount = this.getRowCount();

		addDefaultStyleNames(rowCount, bold, underline);

		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					addCell(rowCount, i, bold, "", depth, false);
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					addCell(rowCount, i, bold, getValue(values[i]), depth,
							underline);
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					addCell(rowCount, i, bold, getValue(values[i]) + " %",
							depth, false);
				} else {
					addCell(rowCount, i, bold, values[i].toString(), depth,
							false);
				}
			} else {
				addCell(rowCount, i, bold, "", depth, false);
			}

			addStyleNameByCol(rowCount, columnTypes[i], i, this.cellFormatter);
		}
		if (rowCount == 0)
			this.adjustCellsWidth(rowCount, body);

		super.fixHeader();

	}

	private void addStyleNameByCol(int rowCount, int coltype, int col,
			CellFormatter cellFormatter) {
		if (Arrays.asList(COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE).contains(coltype)) {
			cellFormatter.addStyleName(rowCount, col, "gridDecimalCell");
		}

	}

	private String getValue(Object object) {
		return (object instanceof Double ? DataUtils.amountAsStringWithCurrency((Double) object,
				Accounter.getCompany().getPreferences().getPrimaryCurrency())
				: object.toString());
	}

	private void addDefaultStyleNames(int rowCount, boolean bold,
			boolean underline) {
		rowFormatter.addStyleName(rowCount, Accounter.constants()
				.reportGridRow());
		if (!bold && !underline) {
			rowFormatter.addStyleName(rowCount, Accounter.constants().depth());
		}
	}

	public void addFooter(Object[] values) {
		for (int i = 0; i < columns.length; i++) {
			if (values.length > i && values[i] != null) {
				if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					this.footer.setText(0, i, getValue(values[i]));
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					this.footer.setText(0, i, getValue(values[i]) + " %");
				} else {
					this.footer.setText(0, i, values[i].toString());
				}
			} else {
				this.footer.setHTML(0, i, "<div></div>");
			}
			this.footer.getCellFormatter().setAlignment(0, i,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);

			addStyleNameByCol(0, columnTypes[i], i,
					this.footer.getCellFormatter());
		}
	}

	/**
	 * Add Header to Table
	 */
	@Override
	protected void initHeader() {
		int rowCount = this.header.getRowCount();

		if (columns != null) {
			for (int i = 0; i < columns.length; i++) {
				this.header.setText(0, i, columns[i].toString());

				this.header.getCellFormatter().addStyleName(rowCount, i,
						Accounter.constants().reportGridheaderStyle());
				this.header.getCellFormatter().setAlignment(rowCount, i,
						HasHorizontalAlignment.ALIGN_LEFT,
						HasVerticalAlignment.ALIGN_MIDDLE);
				this.header.getCellFormatter().getElement(rowCount, i)
						.getStyle().setFontWeight(FontWeight.BOLD);

				addStyleNameByCol(0, columnTypes[i], i,
						this.header.getCellFormatter());
			}
		}
		rowFormatter.addStyleName(rowCount, Accounter.constants()
				.reportGridHeader());

	}

	/**
	 * add a Cell to table
	 * 
	 * @param row
	 * @param column
	 * @param bold
	 * @param cellValue
	 * @param depth
	 * @param isPrimaryData
	 */
	private void addCell(int row, int column, boolean bold, String cellValue,
			int depth, boolean underline) {

		this.setText(row, column, cellValue);

		if (bold) {
			cellFormatter.addStyleName(row, column, Accounter.constants()
					.boldCell());
		}

		addDepthStyle(row, column, depth);

		if (underline) {
			cellFormatter.addStyleName(row, column, Accounter.constants()
					.underline());
		}
		cellFormatter.addStyleName(row, column, Accounter.constants()
				.reportGridcustomFont());
		cellFormatter.setAlignment(row, column,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

	}

	private void addDepthStyle(int row, int column, int depth) {
		boolean canAdd = (reportView instanceof ProfitAndLossReport || reportView instanceof BalanceSheetReport);
		boolean flag = false;

		if (column == 0 && !canAdd) {
			flag = true;
		} else if (column == 1 && canAdd) {
			flag = true;
		}
		if (flag)
			cellFormatter.addStyleName(row, column, Accounter.constants()
					.depth() + depth);

	}

	/**
	 * get Actual object of Row
	 * 
	 * @param event
	 * @return
	 */
	public R getRecordForEvent(ClickEvent event) {
		return selectedObjecd;
	}

	@Override
	protected void cellClicked(int rowIndex, int cellIndex) {
		if (list.size() > rowIndex && list.get(rowIndex) != null) {
			selectedObjecd = list.get(rowIndex);
			reportView.OnRecordClick(selectedObjecd);
		}

	}

	@Override
	public void cellDoubleClicked(int row, int col) {

	}

	@Override
	protected int getCellWidth(int index) {
		// if (index == 0) {
		// return 200;
		// }
		// return -1;
		return reportView.getColumnWidth(index);
	}

	@Override
	protected String[] getColumns() {
		return columns;
	}

	@Override
	public void headerCellClicked(int colIndex) {
		if (sorting != null) {
			sorting.isDecending = isDecending;
			sorting.sort(reportView.getRecords(), colIndex);
			isDecending = !sorting.isDecending;
			reportView.refresh();
		}
	}

	public void setReportView(AbstractReportView<R> reportView) {
		this.reportView = reportView;
		sorting = new Sorting<R>(reportView);
	}

	@Override
	protected void adjustCellsWidth(int row, FlexTable table) {

		try {
			int parentWidth = table.getOffsetWidth();

			int[] colsUpdate = new int[nofCols];

			int cellWidth = -1;
			int colCounts = 0;
			for (int i = 0; i < nofCols; i++) {
				cellWidth = getCellWidth(i);
				if (isReportViewHasToCols() && i == nofCols - 2) {
					colCounts++;
					continue;
				}
				if (cellWidth == -2)
					continue;
				if (cellWidth == -1) {
					colsUpdate[colCounts++] = i;
				} else {
					int cellSize = table.getCellCount(row);
					if (cellSize <= i) {
						continue;
					}
					Element cell = table.getCellFormatter().getElement(row, i);
					parentWidth = parentWidth - cellWidth;
					cell.setAttribute(Accounter.constants().width(), ""
							+ cellWidth);
				}
			}

			// cellWidth = parentWidth / colCounts;
			//
			// for (int col : colsUpdate) {
			// if (col != 0) {
			// Element cell = table.getCellFormatter()
			// .getElement(row, col);
			// cell.setAttribute(FinanceApplication.constants()
			// .width(), "" + cellWidth);
			// }
			// }

			if (isMultiSelectionEnable) {
				// table.getCellFormatter().getElement(row, 0)
				// .setAttribute(Accounter.constants().width(), "" + 15);
			}

		} catch (Exception e) {

		}

	}

	public boolean isReportViewHasToCols() {
		return reportView instanceof CashFlowStatementReport ? true : false;
	}

	public void setColumnTypes(int columnTypes[]) {
		this.columnTypes = columnTypes;
	}

	public void setHeaderText(String text, int column) {
		this.header.setText(0, column, text);
	}

	@Override
	public void removeAllRows() {
		if (this.list != null)
			this.list.clear();
		super.removeAllRows();
	}

	public String getHeader() {
		return this.header.toString();
	}

	public String getFooter() {
		return this.footer != null ? this.footer.toString() : "";
	}

	public FlexTable getFooterTable() {
		return this.footer;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			bodyrowElem = (Element) DOM.eventGetTarget(event)
					.getParentElement();
			if (DOM.isOrHasChild(this.body.getElement(), bodyrowElem)) {
				bodyrowElem.addClassName("report-hover");

			}
			break;
		case Event.ONMOUSEOUT:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			break;
		}
		super.onBrowserEvent(event);
	}

	public void removeEmptyStyle() {
		this.body.removeStyleName("no_records");
	}

	// protected void addEmptyMessage(String msg) {
	// this.body.setText(0, 0, msg);
	// this.cellFormatter.setHorizontalAlignment(0, 0,
	// HasHorizontalAlignment.ALIGN_CENTER);
	// }
}
