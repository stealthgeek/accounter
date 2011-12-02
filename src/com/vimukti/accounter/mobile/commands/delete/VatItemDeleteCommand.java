package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class VatItemDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		long vatItemId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.TAXITEM, vatItemId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					"You can not delete. This VAT Item Might be used in some transactions.");
		}
		return "VAT Items";

	}

}
