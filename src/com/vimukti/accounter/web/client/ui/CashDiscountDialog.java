package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author venki.p
 * 
 */
@SuppressWarnings("unchecked")
public class CashDiscountDialog extends BaseDialog {
	List<ClientAccount> allAccounts;
	private int key;

	private LinkedHashMap<String, ClientAccount> discountAccounts;

	public ClientAccount selectedDiscountAccount;
	private AmountField discAmtText;
	private Double cashDiscountValue = 0.0d;
	private IGenericCallback<String> callback;
	private boolean canEdit;
	OtherAccountsCombo discAccSelect;
	public DynamicForm form;

	public CashDiscountDialog(List<ClientAccount> allAccounts,
			Double cashDiscountValue, IGenericCallback<String> callback) {
		super(Accounter.getFinanceUIConstants().cashDiscount(), Accounter
				.getFinanceUIConstants().cashDiscountPleaseAddDetails());
		this.callback = callback;
		this.allAccounts = allAccounts;
		this.cashDiscountValue = cashDiscountValue;
		createControls();
		center();
	}

	public CashDiscountDialog() {
		super(Accounter.getFinanceUIConstants().cashDiscount(), Accounter
				.getFinanceUIConstants().cashDiscountPleaseAddDetails());
		createControls();
	}

	public CashDiscountDialog(boolean canEdit, Double discountValue,
			ClientAccount account) {
		super(Accounter.getFinanceUIConstants().cashDiscount(), Accounter
				.getFinanceUIConstants().cashDiscountPleaseAddDetails());
		this.cashDiscountValue = discountValue;
		this.canEdit = canEdit;
		this.selectedDiscountAccount = account;
		createControls();
		center();
	}

	public void setAllAccounts(List<ClientAccount> allAccounts) {
		this.allAccounts = allAccounts;
	}

	public void setSelectedDiscountAccount(ClientAccount selectedDiscountAccount) {
		this.selectedDiscountAccount = selectedDiscountAccount;
	}

	public ClientAccount getSelectedDiscountAccount() {
		return this.selectedDiscountAccount;
	}

	public void setCashDiscountValue(Double cashDiscountValue) {
		this.cashDiscountValue = cashDiscountValue;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	private void createControls() {

		mainPanel.setSpacing(5);
		discAccSelect = new OtherAccountsCombo(Accounter.getCustomersMessages()
				.discountaccount(), false);

		discAccSelect.setComboItem(selectedDiscountAccount);

		discAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						setSelectedDiscountAccount(selectItem);
					}

				});
		discAccSelect.setRequired(true);

		discAmtText = new AmountField(Accounter.getFinanceUIConstants()
				.discountAmount());
		discAmtText.setAmount(cashDiscountValue);

		form = new DynamicForm();
		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancelClick() {

			}

			@Override
			public boolean onOkClick() {
				discountAccounts.put(key + "", selectedDiscountAccount);

				callback.called(discAmtText.getAmount().toString());

				return true;
			}
		});
		form.setFields(discAccSelect, discAmtText);
		if (!canEdit) {
			discAccSelect.setDisabled(true);
			discAmtText.setDisabled(true);
			form.setDisabled(true);
		}
		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(30);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(form);

		setBodyLayout(mainVLay);
		setWidth("350");
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	public boolean validate() throws InvalidEntryException {
		if (getSelectedDiscountAccount() == null) {
			AccounterValidator.validateForm(form, true);
		}
		return true;
	}

	public Double getCashDiscount() {
		cashDiscountValue = discAmtText.getAmount();
		return cashDiscountValue;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		if (core.getID() == this.discAccSelect.getSelectedValue().getID()) {
			this.discAccSelect.addItemThenfireEvent((ClientAccount) core);
		}

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCompanyMessages().cashDiscount();
	}

}