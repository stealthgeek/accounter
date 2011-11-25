package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class VendorDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long vendorId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.VENDOR, vendorId, context);
		} catch (AccounterException e) {
			addFirstMessage(
					context,
					"You can no not delete. This vendor Might be participating in some transactions");
		}
		return "Vendors";
	}

}