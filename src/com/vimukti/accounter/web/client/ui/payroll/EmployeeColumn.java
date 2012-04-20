package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public class EmployeeColumn extends
		ComboColumn<ClientAttendanceManagementItem, ClientEmployee> {

	EmployeeDropDownTable dropdown = new EmployeeDropDownTable(true);

	@Override
	protected ClientEmployee getValue(ClientAttendanceManagementItem row) {
		return row.getEmployee();
	}

	@Override
	protected void setValue(ClientAttendanceManagementItem row,
			ClientEmployee newValue) {
		row.setEmployee(newValue);
	}

	@Override
	public AbstractDropDownTable<ClientEmployee> getDisplayTable(
			ClientAttendanceManagementItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 120;
	}

	@Override
	protected String getColumnName() {
		return messages.employee();
	}
}
