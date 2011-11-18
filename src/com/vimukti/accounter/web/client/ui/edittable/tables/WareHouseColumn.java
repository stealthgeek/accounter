package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientWareHouseAllocation;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.WareHouseDropDownTable;

public class WareHouseColumn extends
		ComboColumn<ClientWareHouseAllocation, ClientWarehouse> {
	private AbstractDropDownTable<ClientWarehouse> wareHouseTable = new WareHouseDropDownTable();

	@Override
	public AbstractDropDownTable<ClientWarehouse> getDisplayTable(
			ClientWareHouseAllocation row) {
		return wareHouseTable;
	}

	@Override
	protected ClientWarehouse getValue(ClientWareHouseAllocation row) {
		return Accounter.getCompany().getWarehouse(row.getWareHouse());
	}

	@Override
	protected void setValue(ClientWareHouseAllocation row,
			ClientWarehouse newValue) {
		row.setWareHouse(newValue.getID());
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return Accounter.messages().wareHouse();
	}
}
