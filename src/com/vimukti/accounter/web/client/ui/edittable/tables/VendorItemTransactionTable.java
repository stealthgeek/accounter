package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.CustomerColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.NewQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionBillableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorItemTransactionTable extends VendorTransactionTable {

	public VendorItemTransactionTable(boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, currencyProvider);
	}

	public VendorItemTransactionTable(boolean enableTax, boolean showTaxCode,
			boolean enableDisCount, boolean showDisCount,
			ICurrencyProvider currencyProvider) {
		super(1, enableDisCount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDisCount;
		addEmptyRecords();
	}

	public VendorItemTransactionTable(boolean needDiscount, boolean enableTax,
			boolean showTaxCode, ICurrencyProvider currencyProvider,
			boolean isCustomerAllowedToAdd) {
		super(1, needDiscount, isCustomerAllowedToAdd, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		addEmptyRecords();
	}

	public VendorItemTransactionTable(boolean needDiscount, boolean enableTax,
			boolean showTaxCode, ICurrencyProvider currencyProvider) {
		super(1, needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		addEmptyRecords();
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	@Override
	public void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			ClientTransactionItem item = new ClientTransactionItem();
			item.setType(ClientTransactionItem.TYPE_ITEM);
			add(item);
		}
	}

	@Override
	protected void initColumns() {

		ItemNameColumn transactionItemNameColumn = new ItemNameColumn(
				isSales(), currencyProvider) {

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				updateDiscountValues(row);
				super.setValue(row, newValue);
				// Unit Price is different. So that overriden the code here
				if (newValue != null) {
					// row.setUnitPrice(newValue.getPurchasePrice());
					row.setTaxable(newValue.isTaxable());
					if (row.getQuantity() == null) {
						ClientQuantity quantity = new ClientQuantity();
						quantity.setValue(1.0);
						row.setQuantity(quantity);
					}
					if (row.getUnitPrice() == null) {
						row.setUnitPrice(new Double(0));
					}
					if (row.getDiscount() == null) {
						row.setDiscount(new Double(0));
					}
					double lt = row.getQuantity().getValue()
							* row.getUnitPrice();
					double disc = row.getDiscount();
					row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
							* disc / 100))
							: lt);
					if (newValue.getType() == ClientItem.TYPE_INVENTORY_PART) {
						ClientMeasurement measurement = Accounter.getCompany()
								.getMeasurement(newValue.getMeasurement());
						row.getQuantity().setUnit(
								measurement.getDefaultUnit().getId());
						row.setWareHouse(newValue.getWarehouse());
						row.setDescription(newValue.getPurchaseDescription());
					}

				}
				update(row);
			}

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isIBuyThisItem();
					}
				};
			}

			@Override
			protected String getDiscription(ClientItem item) {
				return item.getPurchaseDescription();
			}
		};
		transactionItemNameColumn.setItemForCustomer(false);

		this.addColumn(transactionItemNameColumn);

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new NewQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider));

		if (needDiscount) {
			if (showDiscount) {
				this.addColumn(new TransactionDiscountColumn(currencyProvider));
			}
		}

		this.addColumn(new TransactionTotalColumn(currencyProvider, true));

		if (enableTax) {
			if (showTaxCode) {
				this.addColumn(new TransactionVatCodeColumn() {

					@Override
					protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
						return new ListFilter<ClientTAXCode>() {

							@Override
							public boolean filter(ClientTAXCode e) {
								if (!e.isTaxable()
										|| e.getTAXItemGrpForPurchases() != 0) {
									return true;
								}
								return false;
							}
						};
					}

					@Override
					protected void setValue(ClientTransactionItem row,
							ClientTAXCode newValue) {
						super.setValue(row, newValue);
						update(row);
					}

					@Override
					protected boolean isSales() {
						return false;
					}
				});

				this.addColumn(new TransactionVatColumn(currencyProvider));
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}

		if (isCustomerAllowedToAdd) {
			this.addColumn(new CustomerColumn());
			this.addColumn(new TransactionBillableColumn());
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}
}
