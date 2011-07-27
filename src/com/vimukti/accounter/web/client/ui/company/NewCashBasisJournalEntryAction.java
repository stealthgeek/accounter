package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewCashBasisJournalEntryAction extends Action {
	@SuppressWarnings("unused")
	private ClientJournalEntry journalEntry;
	@SuppressWarnings("unused")
	private boolean isEdit;

	public NewCashBasisJournalEntryAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public NewCashBasisJournalEntryAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {

		try {
			MainFinanceWindow.getViewManager().showView(new JournalEntryView(),
					null, false, this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getCompanyMessages()
					.failedToLoadCashBasisJournalEntryFailed());
		}

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newCashBasisJournalEntry";
	}

}
