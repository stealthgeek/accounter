package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.AccountMergeDialog;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeAccountsAction extends Action {

	public MergeAccountsAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		AccountMergeDialog accountMergeDialog = new AccountMergeDialog(
				messages.mergeAccounts(), Accounter.messages()
						.mergeDescription());
		accountMergeDialog.show();
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
		return "merge_account";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}


}
