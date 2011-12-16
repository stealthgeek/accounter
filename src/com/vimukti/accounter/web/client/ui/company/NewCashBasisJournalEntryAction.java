package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewCashBasisJournalEntryAction extends Action {

	private ClientJournalEntry journalEntry;

	private boolean isEdit;

	public NewCashBasisJournalEntryAction() {
		super();
		this.catagory = Accounter.messages().company();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {

		try {
			MainFinanceWindow.getViewManager().showView(new JournalEntryView(),
					null, false, this);
		} catch (Exception e) {
			Accounter.showError(Accounter.messages()
					.failedToLoadCashBasisJournalEntry());
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

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.newCashBasisJournalEntry();
	}

}
