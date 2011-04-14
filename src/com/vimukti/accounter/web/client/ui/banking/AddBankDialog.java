package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class AddBankDialog extends AbstractBaseDialog {

	private BankingMessages bankingConstants;
	private FinanceConstants financeConstants;

	public AddBankDialog(AbstractBaseView parent) {
		super(parent);
		createControls();
	}

	private void createControls() {

		bankingConstants = GWT.create(BankingMessages.class);
		financeConstants = GWT.create(FinanceConstants.class);
		setTitle(bankingConstants.addBank());

		final TextItem bankText = new TextItem(bankingConstants.bankName());
		bankText.setRequired(true);
		// bankText.setWrapTitle(false);
		final DynamicForm bankForm = new DynamicForm();
		// bankForm.setMargin(20);
		bankForm.setFields(bankText);

		Button helpButt = new Button(financeConstants.help());
		// helpButt.setAutoFit(true);
		Button okButt = new Button(financeConstants.ok());
		// okButt.setAutoFit(true);
		Button canButt = new Button(financeConstants.cancel());
		// canButt.setAutoFit(true);

		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(helpButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setWidth("100%");
		// buttHLay.setMargin(20);
		// buttHLay.setMembersMargin(5);
		// buttHLay.setAlign(Alignment.RIGHT);
		buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(okButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		canButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(canButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		okButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (bankForm.validate(true)) {

				}
			}
		});

		canButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// FIXME-- it needs an action
				// cancelClick();
			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(30);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(bankForm);
		mainVLay.add(buttHLay);
		add(mainVLay);
		setSize("275", "150");
		show();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
}
