package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.DepositDetailReport;

public class DepositDetailAction extends Action {

	public DepositDetailAction(String text) {
		super(text);
		this.catagory = Accounter.getReportsMessages().report();
	}

	public DepositDetailAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getReportsMessages().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					AbstractReportView<DepositDetail> report = new DepositDetailReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, DepositDetailAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return  Accounter.getFinanceMenuImages().reports();

	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}

