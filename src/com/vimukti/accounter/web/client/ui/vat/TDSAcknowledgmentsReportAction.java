package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.reports.TDSAcknowledgmentsReportView;

public class TDSAcknowledgmentsReportAction extends
		Action<TDSAcknowledgmentsReport> {

	public TDSAcknowledgmentsReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.tdsAcknowledgmentsReport();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				TDSAcknowledgmentsReportView report = new TDSAcknowledgmentsReportView();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, TDSAcknowledgmentsReportAction.this);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "TDSAckReport";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}