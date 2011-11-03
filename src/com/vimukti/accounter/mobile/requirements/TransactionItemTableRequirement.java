package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {
	private static final String QUANITY = "Quantity";
	private static final String ITEM = "Item";
	private static final String UNITPTICE = "UnitPrice";
	private static final String DISCOUNT = "Discount";
	private static final String TAXCODE = "TaxCode";
	private static final String TAX = "Tax";
	private static final String DESCRIPTION = "Description";

	public TransactionItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM,
				"Please Select an Item for Transaction", "Item", false, true,
				null) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new AmountRequirement(QUANITY, "Please Enter Quantity",
				"Quantity", true, true));

		list.add(new AmountRequirement(UNITPTICE, "Please Enter Unit Price",
				"Unit Price", true, true));

		list.add(new AmountRequirement(DISCOUNT, "Please Enter Discount",
				"Discount", true, true));

		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			list.add(new TaxCodeRequirement(TAXCODE, "Please Select TaxCode",
					"Tax Code", false, true, null) {

				@Override
				protected List<ClientTAXCode> getLists(Context context) {
					return getClientCompany().getActiveTaxCodes();
				}
			});

		} else {
			list.add(new BooleanRequirement(TAX, true) {

				@Override
				protected String getTrueString() {
					return getConstants().taxable();
				}

				@Override
				protected String getFalseString() {
					return getConstants().taxExempt();
				}
			});
		}

		list.add(new StringRequirement(DESCRIPTION, "Please Enter Description",
				"Description", true, true));
	}

	@Override
	protected String getEmptyString() {
		return "There are no Transaction Items";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		ClientItem clientItem = get(ITEM).getValue();
		obj.setItem(clientItem.getID());
		obj.getQuantity().setValue((Double) get(QUANITY).getValue());
		obj.setUnitPrice((Double) get(UNITPTICE).getValue());
		obj.setDiscount((Double) get(DISCOUNT).getValue());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			obj.setTaxCode(((ClientTAXCode) get(TAXCODE).getValue()).getID());
		} else {
			obj.setTaxable((Boolean) get(TAX).getValue());
		}
		obj.setDescription((String) get(DESCRIPTION).getValue());
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		get(ITEM).setDefaultValue(getClientCompany().getItem(obj.getItem()));
		get(QUANITY).setDefaultValue(obj.getQuantity().getValue());
		get(UNITPTICE).setDefaultValue(obj.getUnitPrice());
		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			get(TAXCODE).setDefaultValue(
					getClientCompany().getTAXCode(obj.getTaxCode()));
		} else {
			get(TAX).setDefaultValue(obj.isTaxable());
		}
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		return new ClientTransactionItem();
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		record.add("", getClientCompany().getItem(t.getItem()).getDisplayName());
		record.add("", t.getQuantity());
		record.add("", t.getUnitPrice());
		if (getClientCompany().getPreferences().isTrackTax()
				&& getClientCompany().getPreferences().isTaxPerDetailLine()) {
			record.add("", getClientCompany().getTAXCode(t.getTaxCode())
					.getDisplayName());
		} else {
			if (t.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().taxExempt());
			}
		}
		record.add("", t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return "Add More Items";
	}

}
