package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */
public class CreditRatingListDialog extends GroupDialog<ClientCreditRating> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	ClientCreditRating creditRating;
	List<ClientCreditRating> creditRatings;
	private InputDialog inputDlg;

	public CreditRatingListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientCreditRating clientCreditRating = (ClientCreditRating) core;
				if (clientCreditRating != null)
					enableEditRemoveButtons(true);
				else
					enableEditRemoveButtons(false);
				return false;
			}
		});
		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientCreditRating) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {

				deleteObject(getSelectedCreditGroup());
				if (creditRatings == null)
					enableEditRemoveButtons(false);

			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
	}

	public void createCreditRatings() {
		ClientCreditRating creditRating = new ClientCreditRating();
		creditRating.setName(inputDlg.getTextItems().get(0).getValue()
				.toString());

		saveOrUpdate(creditRating);
	}

	public long getSelectedCreditGroupId() {

		return ((ClientCreditRating) listGridView.getSelection()).getID();
	}

	public ClientCreditRating getSelectedCreditGroup() {

		return (ClientCreditRating) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientCreditRating rec) {
		creditRating = rec;
		String creditRateString = Accounter.constants().creditRating();
		inputDlg = new InputDialog(this, Accounter.constants().creditRating(),
				"", creditRateString) {
		};

		if (creditRating != null) {
			inputDlg.setTextItemValue(0, creditRating.getName());
		}

		inputDlg.show();
	}

	protected void EditCreditRatings() {
		creditRating.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(creditRating);
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientCreditRating creditRating = (ClientCreditRating) obj;
		if (creditRating != null) {
			switch (index) {
			case 0:
				return creditRating.getName();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().name() };
	}

	@Override
	protected List<ClientCreditRating> getRecords() {
		return (List<ClientCreditRating>) getCompany().getCreditRatings();
	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (creditRating != null) {
			if (!(creditRating.getName().equalsIgnoreCase(
					UIUtils.toStr(inputDlg.getTextItems().get(0).getValue()
							.toString())) ? true : (Utility.isObjectExist(
					company.getItemGroups(),
					UIUtils.toStr(inputDlg.getTextItems().get(0).getValue()
							.toString()))) ? false : true)) {
				result.addError(this, AccounterErrorType.ALREADYEXIST);
			}
		} else {
			if (Utility.isObjectExist(getCompany().getCreditRatings(), inputDlg
					.getTextItems().get(0).getValue().toString())) {
				result.addError(this, Accounter.constants()
						.creditRatingAlreadyExists());
			}
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (creditRating != null) {
			EditCreditRatings();
		} else
			createCreditRatings();
		return true;
	}

}
