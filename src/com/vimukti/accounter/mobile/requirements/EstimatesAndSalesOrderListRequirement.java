package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;

public abstract class EstimatesAndSalesOrderListRequirement extends
		ListRequirement<EstimatesAndSalesOrdersList> {

	public EstimatesAndSalesOrderListRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext,
			ChangeListner<EstimatesAndSalesOrdersList> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(EstimatesAndSalesOrdersList value) {
		Record rec = new Record(value);
		if (value.getType() == ClientTransaction.TYPE_ESTIMATE)
			rec.add("", getMessages().quote());
		else
			rec.add("", getMessages().salesOrder());
		rec.add("", value.getTotal());

		return rec;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().estimate());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().estimate());
	}

	@Override
	protected String getDisplayValue(EstimatesAndSalesOrdersList value) {
		return value != null ? value.getCustomerName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().quote()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().selectTypeOfThis(getMessages().quote());
	}

}
