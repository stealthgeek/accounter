package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.reports.RecincilationDetailsByAccountReport;

public class ReconcilationDetailByAccountAction extends Action {
	private RecincilationDetailsByAccountReport reconcilationByAccountReport;
	private long accountId;

	public ReconcilationDetailByAccountAction(long id) {
		super();
		this.accountId = id;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				reconcilationByAccountReport = new RecincilationDetailsByAccountReport(
						accountId);
				MainFinanceWindow.getViewManager().showView(
						reconcilationByAccountReport, data, isDependent,
						ReconcilationDetailByAccountAction.this);
			}

			public void onCreateFailed(Throwable t) {
				/* UIUtils.logError */System.err
						.println("Failed to Load Report.." + t);
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return messages.reconcilationDetailByAccount();
	}

	@Override
	public String getHelpToken() {
		return messages.reconcilationDetailByAccount();
	}

	@Override
	public String getText() {
		return messages.reconcilationDetailByAccount();
	}
}