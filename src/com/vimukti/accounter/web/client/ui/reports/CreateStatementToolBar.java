package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class CreateStatementToolBar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	private CustomerCombo customerCombo;
	ClientCustomer selectedCusotmer = null;
	private StatementReport statementReport;
	private SelectCombo dateRangeItemCombo;
	private List<String> dateRangeItemList;
	private Button updateButton;

	@SuppressWarnings("unchecked")
	public CreateStatementToolBar(AbstractReportView reportView) {
		this.reportview = reportView;
		createControls();
	}

	public void createControls() {

		String[] dateRangeArray = {
				FinanceApplication.getReportsMessages().all(),
				FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),
				FinanceApplication.getReportsMessages().custom() };

		customerCombo = new CustomerCombo("Choose Customer", false);
		statementReport = new StatementReport();
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						if (selectItem != null) {
							selectedCusotmer = selectItem;
							ClientFinanceDate startDate = fromItem.getDate();
							ClientFinanceDate endDate = toItem.getDate();
							reportview.makeReportRequest(selectedCusotmer
									.getStringID(), startDate, endDate);

						}

					}
				});

		if (UIUtils.isMSIEBrowser()) {
			customerCombo.setWidth("200px");
		}
		// customerCombo.setSelectedItem(1);
		selectedCusotmer = customerCombo.getComboItems().get(0);
		customerCombo.setComboItem(selectedCusotmer);
		dateRangeItemCombo = new SelectCombo(FinanceApplication
				.getReportsMessages().dateRange());
		dateRangeItemCombo.setHelpInformation(true);
		dateRangeItemList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeItemList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo.initCombo(dateRangeItemList);
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeChanged(dateRangeItemCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(FinanceApplication.getStartDate());
		fromItem.setTitle(FinanceApplication.getReportsMessages().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		ClientFinanceDate date = Utility.getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(FinanceApplication.getReportsMessages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new Button(FinanceApplication.getReportsMessages()
				.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());
				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeItemCombo.setDefaultValue(FinanceApplication
						.getReportsMessages().custom());
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());

			}
		});

		Button printButton = new Button(FinanceApplication.getReportsMessages()
				.print());
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItemCombo.setWidth("200px");
		}

		addItems(customerCombo, dateRangeItemCombo, fromItem, toItem);
		add(updateButton);
		updateButton.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(updateButton, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
		// reportRequest();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(selectedCusotmer.getStringID(), startDate,
				endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItemCombo.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItemCombo.setValueMap(dateRanages);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}

	public void reportRequest() {
		reportview.makeReportRequest(selectedCusotmer.getStringID(), startDate,
				endDate);
	}

}
