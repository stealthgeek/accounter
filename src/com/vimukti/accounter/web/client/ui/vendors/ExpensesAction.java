package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ExpensesAction extends Action {

	protected ExpensesListView view;
	public String viewType;

	public ExpensesAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public ExpensesAction(String text, String viewType) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
		this.viewType = viewType;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (viewType == null || viewType.equals(""))
					view = ExpensesListView.getInstance();
				else
					view = new ExpensesListView(viewType);
				MainFinanceWindow.getViewManager().showView(view, null, false,
						ExpensesAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// public void run(Object data, Boolean isDependent, String viewType) {
	// this.viewType = viewType;
	// run(data, isDependent);
	// }

	@Override
	public String getHistoryToken() {
		if (viewType == null) {
			return "expenses";
		} else if (viewType.equals(Accounter.constants().cash())) {
			return "cashExpenses";
		} else if (viewType.equals(Accounter.constants().creditCard())) {
			return "creditCardExpenses";
		} else if (viewType.equals(Accounter.constants().employee())) {
			return "employeeExpenses";
		}
		return "";
	}

}
