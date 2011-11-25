package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccountDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long accountId = Long.parseLong(context.getString());
		Account account = (Account) CommandUtils.getServerObjectById(accountId,
				AccounterCoreType.ACCOUNT);

		try {
			delete(AccounterCoreType.ACCOUNT, accountId, context);
		} catch (AccounterException e) {
			addFirstMessage(
					context,
					"You can no not delete. This Account Might be participating in some transactions");
		}
		if (account.getType() == Account.TYPE_BANK) {
			return "BankAccounts";
		} else {
			return "Accounts";
		}

	}

}
