package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.CustomerCenterView;

public class CustomerCentreAction extends Action {

	protected CustomerCenterView view;

	public CustomerCentreAction() {
		super();
		this.catagory = Accounter.messages().company();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerCenterView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerCentreAction.this);

			}

		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}

	@Override
	public String getHistoryToken() {
		return "customercentre";
	}

	@Override
	public String getHelpToken() {
		return "customercentre";
	}

	@Override
	public String getText() {
		return messages.customerCentre(Global.get().Customer());
	}

}
