package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddBankDialog extends BaseDialog {

	private TextItem bankNameText;
	private AccounterAsyncCallback<ClientBank> callBack;

	public AddBankDialog(AbstractBaseView<ClientBank> parent) {
		super(Accounter.constants().addBank(), null);
		createControls();
		center();
	}

	private void createControls() {

		setText(Accounter.constants().addBank());

		bankNameText = new TextItem(Accounter.constants().bankName());
		bankNameText.setRequired(true);
		DynamicForm bankForm = new DynamicForm();
		bankForm.setFields(bankNameText);

		setBodyLayout(bankForm);
		setWidth("275");
	}

	protected void createBank() {
		ClientBank bank = new ClientBank();
		bank.setName(UIUtils.toStr(bankNameText.getValue()));
		saveOrUpdate(bank);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (!bankNameText.validate()) {
			result.addError(bankNameText, bankNameText.getTitle());
		}
		if (Utility.isObjectExist(company.getTaxItems(), bankNameText
				.getValue().toString())) {
			result.addError(this, AccounterErrorType.ALREADYEXIST);
		}
		return result;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (callBack != null) {
			callBack.onResultSuccess((ClientBank) object);
		}
		// Accounter.showInformation(FinanceApplication.constants()
		// .bankCreated());
		removeFromParent();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(Throwable exception) {
		Accounter.showError(Accounter.constants().failedToCreateBank());
		super.saveFailed(exception);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// its not using any where

	}

	public void addCallBack(AccounterAsyncCallback<ClientBank> callback) {
		this.callBack = callback;
	}

	@Override
	protected boolean onOK() {
		createBank();
		return false;
	}

}
