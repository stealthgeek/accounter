package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class VatItemListAction extends Action {

	private VatItemsListView view;

	public VatItemListAction(String text) {
		super(text);
		this.catagory = Accounter.getVATMessages().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVatItem();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new VatItemsListView();
					MainFinanceWindow.getViewManager().showView(view, null,
							false, VatItemListAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
			}
		});
	}
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/Vat_item.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "vatItems";
	}

}
