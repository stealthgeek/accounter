package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AddUnitsGrid extends ListGrid<Unit> {

	private AddMeasurementView view;
	boolean isEditMode;
	private AccounterConstants settingsMessages = Accounter.constants();

	public AddUnitsGrid(boolean isMultiSelectionEnable) {
		super(false);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 1:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		}
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		return true;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { settingsMessages.getUnitName(),
				settingsMessages.getFactorName() };
	}

	public void setView(AddMeasurementView addMeasurementView) {
		this.view = addMeasurementView;
	}

	@Override
	protected Object getColumnValue(Unit obj, int col) {
		switch (col) {
		case 0:
			return obj.getType();
		case 1:
			return obj.getFactor();
		}
		return "";
	}

	@Override
	protected String[] getSelectValues(Unit obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(Unit obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(Unit obj, int row, int index) {
		return true;
	}

	@Override
	public void editComplete(Unit item, Object value, int col) {
		switch (col) {
		case 0:
			item.setType(value.toString());
		case 1:
			item.setFactor(Double.parseDouble(value.toString()));
			break;
		}
		updateRecord(item, currentRow, col);
		super.editComplete(item, value, col);
		view.setDefaultComboValue(item);
	}

	@Override
	protected void onClick(Unit obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(Unit obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(Unit obj1, Unit obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addData(Unit obj) {
		super.addData(obj);
	}

}
