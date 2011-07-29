package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.customers.CustomerListView;

/**
 * 
 * @author Raj Vimal
 */

public class CustomersAction extends Action {

	protected CustomerListView view;

	public CustomersAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public CustomersAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {

					view = new CustomerListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, CustomersAction.this);
					// / UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}
			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer list...", t);
			}
		});

	}

//	@Override
//	public ParentCanvas<?> getView() {
//		return this.view;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}

//	@Override
//	public String getImageUrl() {
//		return "/images/customers.png";
//	}

	@Override
	public String getHistoryToken() {
		return "customers";
	}

}
