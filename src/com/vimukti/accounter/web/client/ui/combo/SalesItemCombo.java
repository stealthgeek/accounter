package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class SalesItemCombo extends AccountCombo {
	private ArrayList<ClientAccount> filtrdAccounts;

	public SalesItemCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		return Accounter.getCompany().getActiveAccounts();
	}

	public List<ClientAccount> getFilterdAccounts() {
		filtrdAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : Accounter.getCompany()
				.getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
					&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
					&& account.getType() != ClientAccount.TYPE_EXPENSE
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					&& account.getType() != ClientAccount.TYPE_FIXED_ASSET
					&& account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
					&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
					&& account.getType() != ClientAccount.TYPE_EQUITY) {
				filtrdAccounts.add(account);
			}
		}

		return filtrdAccounts;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		action.setAccountTypes(Arrays.asList(ClientAccount.TYPE_INCOME));
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
