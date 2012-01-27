package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.BankStatementsView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class BankStatementAction extends Action<ClientTransaction> {
	private BankStatementsView bankStatementsView;

	public BankStatementAction() {
		super();
	}

	@Override
	public String getText() {
		return "Import BankStatement";
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				bankStatementsView = new BankStatementsView();
				MainFinanceWindow.getViewManager().showView(bankStatementsView,
						data, isDependent, BankStatementAction.this);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "bankStatements";
	}

	@Override
	public String getHelpToken() {
		return "Bank-Statement";
	}

}
