package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXFilingFrequencyCombo;
import com.vimukti.accounter.web.client.ui.combo.VATAgencyAccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;

public class TAXAgencyView extends BaseView<ClientTAXAgency> {

	private final String ATTR_PRIMARY = messages.primary();
	private final String ATTR_CONTACT_NAME = messages.contactName();
	private final String ATTR_TITLE = messages.title();
	private final String ATTR_BUSINESS_PHONE = messages.businessPhone();
	private final String ATTR_EMAIL = messages.email();

	TextItem taxAgencyText, fileAsText;

	private TextItem linksText;
	private TextAreaItem memoArea, billToTextAreaItem;
	private CheckboxItem statusCheck;
	private DynamicForm memoForm, accInfoForm, taxAgencyForm;

	private AddressForm addrsForm;
	private PhoneFaxForm phoneFaxForm;
	private EmailForm emailForm;

	private PaymentTermsCombo paymentTermsCombo;
	private VATAgencyAccountCombo liabilitySalesAccountCombo;
	private VATAgencyAccountCombo liabilityPurchaseAccountCombo;

	private ContactsTable gridView;

	private ClientPaymentTerms selectedPaymentTerm;
	private ClientAccount selectedSalesAccount, selectedPurchaseAccount;

	private AddButton addButton;

	private List<String> vatReturnList;

	private SelectCombo vatReturnCombo;
	private SelectCombo taxTypeCombo;

	private static TAXAgencyView taxAgencyView;

	private ArrayList<DynamicForm> listforms;

	private TAXFilingFrequencyCombo tAXFilingFrequency;

	public TAXAgencyView() {
		super();

	}

	private void initPaymentTermsCombo() {

		paymentTermsCombo.initCombo(getCompany().getPaymentsTerms());
		if (isInViewMode() && (data.getPaymentTerm()) != 0) {
			selectedPaymentTerm = getCompany().getPaymentTerms(
					data.getPaymentTerm());
			paymentTermsCombo.setComboItem(selectedPaymentTerm);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		VerticalPanel topLayout = getTopLayout();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(topLayout);

		this.add(mainVLay);
		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() {
		updateTaxAgency();
		saveOrUpdate(getData());
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication.messages()
		// .duplicationOfTaxAgencyNameAreNotAllowed());
		// addError(this, messages
		// .duplicationOfTaxAgencyNameAreNotAllowed());
		// else
		// BaseView.errordata.setHTML(FinanceApplication.messages()
		// .failedToUpdate());
		// addError(this, messages.failedToUpdate());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		// if (takenVATAgency == null)
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.messages()
		// .isCreatedSuccessfully());
		// else
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.messages()
		// .isUpdatedSuccessfully());
		super.saveSuccess(result);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// already exists?
		// form validation

		String name = taxAgencyText.getValue().toString();

		ClientTAXAgency taxAgenciesByName = getCompany().getTaxAgenciesByName(
				name);

		if (taxAgenciesByName != null
				&& taxAgenciesByName.getID() != this.getData().getID()) {
			result.addError(taxAgencyText, messages.alreadyExist());
		}

		List<DynamicForm> forms = this.getForms();
		for (DynamicForm form : forms) {
			if (form != null) {
				result.add(form.validate());
			}
		}
		if (!gridView.isEmpty()) {
			gridView.validate(result);
		}

		return result;
	}

	private void updateTaxAgency() {

		// Setting TaxAgency
		data.setName(taxAgencyText.getValue().toString());
		if (vatReturnCombo.getSelectedValue() == "") {
			data.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
		} else if (vatReturnCombo.getSelectedValue().equals("UK VAT")) {
			data.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
		} else {
			data.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
		}

		data.setTaxType(getTaxType(taxTypeCombo.getSelectedValue()));

		// Setting File As
		data.setFileAs(fileAsText.getValue().toString());

		data.setType(ClientPayee.TYPE_TAX_AGENCY);

		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		// Setting Phone
		data.setPhoneNo(phoneFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		data.setFaxNo(phoneFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		data.setActive((Boolean) statusCheck.getValue());

		// Setting Payment Terms
		data.setPaymentTerm(selectedPaymentTerm.getID());

		// Setting Sales Liability account
		if (selectedSalesAccount != null) {
			data.setSalesLiabilityAccount(selectedSalesAccount.getID());
		}

		// Setting Purchase Liability account
		if (getPreferences().isTrackPaidTax()
				&& selectedPurchaseAccount != null) {
			data.setPurchaseLiabilityAccount(selectedPurchaseAccount.getID());
		} else {
			data.setPurchaseLiabilityAccount(0);
		}

		// Setting Contacts

		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		// FIXME--The records from contact grid are added here

		for (ClientContact record : gridView.getRecords()) {
			ClientContact contact = new ClientContact();
			if (record.isPrimary())
				contact.setPrimary(true);
			else
				contact.setPrimary(false);
			contact.setName(record.getName());
			contact.setTitle(record.getTitle());
			contact.setBusinessPhone(record.getBusinessPhone());
			contact.setEmail(record.getEmail());
			// if (contact.getName().isEmpty() || contact.getTitle().isEmpty()
			// || contact.getBusinessPhone().isEmpty()
			// || contact.getEmail().isEmpty()) {
			// continue;
			// }
			allContacts.add(contact);
		}
		data.setContacts(allContacts);

		// Setting Memo
		data.setMemo(UIUtils.toStr(memoArea.getValue()));

		data.setTAXFilingFrequency(tAXFilingFrequency.getSelectedFrequency());

	}

	private VerticalPanel getTopLayout() {
		Label lab;
		lab = new Label(messages.taxAgency());
		taxAgencyText = new TextItem(messages.taxAgency());
		taxAgencyText.setHelpInformation(true);
		lab.removeStyleName("gwt-Label");
		lab.addStyleName(messages.labelTitle());
		lab.setHeight("35px");
		taxAgencyText.setWidth(100);
		taxAgencyText.setRequired(true);
		taxAgencyText.setDisabled(isInViewMode());

		fileAsText = new TextItem(messages.fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);
		fileAsText.setDisabled(isInViewMode());
		taxAgencyText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (((TextBox) event.getSource()).getValue() != null) {
					String val = ((TextBox) event.getSource()).getValue()
							.toString();
					fileAsText.setValue(val);
				}
			}
		});

		taxAgencyForm = UIUtils.form(messages.taxAgency());
		// taxAgencyForm.setWidth("100%");
		// taxAgencyForm.getCellFormatter().setWidth(0, 0, "166px");
		taxAgencyForm.setFields(taxAgencyText);

		accInfoForm = new DynamicForm();
		accInfoForm = UIUtils.form(Accounter.messages().payeeInformation(
				messages.Account()));

		statusCheck = new CheckboxItem(messages.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());

		paymentTermsCombo = new PaymentTermsCombo(messages.paymentTerm());
		paymentTermsCombo.setHelpInformation(true);
		paymentTermsCombo.setDisabled(isInViewMode());
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectedPaymentTerm = (ClientPaymentTerms) selectItem;

					}
				});

		paymentTermsCombo.setRequired(true);

		taxTypeCombo = createTaxTypeSelectCombo();

		vatReturnCombo = new SelectCombo(messages.taxReturn());
		vatReturnCombo.setHelpInformation(true);
		if (getCompany().getCountry().equals(
				CountryPreferenceFactory.UNITED_KINGDOM)) {
			vatReturnCombo.setRequired(true);
		}
		vatReturnCombo.setDisabled(isInViewMode());
		vatReturnList = new ArrayList<String>();
		vatReturnList.add(messages.ukVAT());
		vatReturnList.add(messages.vat3Ireland());
		vatReturnCombo.initCombo(vatReturnList);
		vatReturnCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (vatReturnCombo.getSelectedValue() != null) {
							vatReturnCombo.setSelected(vatReturnCombo
									.getSelectedValue());
						}

					}
				});
		liabilitySalesAccountCombo = new VATAgencyAccountCombo(Accounter
				.messages().salesLiabilityAccount());
		liabilitySalesAccountCombo.setHelpInformation(true);
		liabilitySalesAccountCombo.setDisabled(isInViewMode());
		liabilitySalesAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedSalesAccount = (ClientAccount) selectItem;
					}

				});

		liabilitySalesAccountCombo.setRequired(true);

		liabilityPurchaseAccountCombo = new VATAgencyAccountCombo(Accounter
				.messages().purchaseLiabilityAccount());
		liabilityPurchaseAccountCombo.setHelpInformation(true);
		liabilityPurchaseAccountCombo.setDisabled(isInViewMode());
		liabilityPurchaseAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPurchaseAccount = (ClientAccount) selectItem;
					}

				});

		liabilityPurchaseAccountCombo.setRequired(true);

		tAXFilingFrequency = new TAXFilingFrequencyCombo(
				messages.taxFilingFrequency());
		tAXFilingFrequency.setDisabled(isInViewMode());
		tAXFilingFrequency.initCombo(getTAXFilingFrequencies());
		tAXFilingFrequency.setSelectedItem(0);

		Label contacts = new Label(messages.contacts());
		initListGrid();
		if (getPreferences().isTrackPaidTax()) {
			if (getCompany().getCountry().equals(
					CountryPreferenceFactory.UNITED_KINGDOM)) {
				accInfoForm.setFields(statusCheck, paymentTermsCombo,
						taxTypeCombo, vatReturnCombo,
						liabilitySalesAccountCombo,
						liabilityPurchaseAccountCombo, tAXFilingFrequency);
			} else {
				accInfoForm.setFields(statusCheck, paymentTermsCombo,
						taxTypeCombo, liabilitySalesAccountCombo,
						liabilityPurchaseAccountCombo, tAXFilingFrequency);
			}
		} else {
			if (getCompany().getCountry().equals(
					CountryPreferenceFactory.UNITED_KINGDOM)) {
				accInfoForm.setFields(statusCheck, paymentTermsCombo,
						taxTypeCombo, vatReturnCombo,
						liabilitySalesAccountCombo);
			} else {
				accInfoForm.setFields(statusCheck, paymentTermsCombo,
						taxTypeCombo, liabilitySalesAccountCombo);
			}
		}

		// accInfoForm.setWidth("94%");
		accInfoForm.setStyleName("align-form");

		memoForm = new DynamicForm();
		// memoForm.setWidth("50%");
		memoArea = new TextAreaItem();
		memoArea.setToolTip(Accounter.messages().writeCommentsForThis(
				this.getAction().getViewName()));
		memoArea.setHelpInformation(true);
		memoArea.setDisabled(isInViewMode());
		memoArea.setTitle(messages.memo());
		// memoArea.setWidth("400px");
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		addButton = new AddButton(this);
		addButton.setEnabled(!isInViewMode());
		// addButton.setStyleName("addButton");
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				// clientContact.setName("");
				gridView.add(clientContact);
			}
		});

		// For Editing taxAgency
		if (getData() != null) {

			// Setting TaxAgency Name
			taxAgencyText
					.setValue(data.getName() != null ? data.getName() : "");

			// Setting File as
			fileAsText.setValue(data.getFileAs() != null ? data.getFileAs()
					: "");

			// Setting AddressForm
			addrsForm = new AddressForm(data.getAddress());
			// addrsForm.setWidth("100%");
			addrsForm.setDisabled(isInViewMode());
			// Setting Phone Fax Form
			phoneFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
					.getViewName());
			// phoneFaxForm.setWidth("100%");
			phoneFaxForm.businessPhoneText.setValue(data.getPhoneNo());
			phoneFaxForm.businessFaxText.setValue(data.getFaxNo());
			phoneFaxForm.setDisabled(isInViewMode());

			// Setting Email Form
			emailForm = new EmailForm(null, data.getWebPageAddress(), this,
					this.getAction().getViewName());
			emailForm.businesEmailText.setValue(data.getEmail());
			// emailForm.setWidth("100%");
			emailForm.setDisabled(isInViewMode());

			// Setting Status Check
			statusCheck.setValue(data.isActive());

			// // Setting Payment terms Combo
			// selectedPaymentTerm = takenTaxAgency.getPaymentTerm();
			// paymentTermsCombo.setPaymentTerms(selectedPaymentTerm);

			// Setting Liability accounts Combo
			// selectedAccount = takenTaxAgency.getLiabilityAccount();
			// liabilityAccountCombo.setComboItem(selectedAccount);

			// Setting contacts

			Set<ClientContact> contactsOfEditableTaxAgency = data.getContacts();
			ClientContact records[] = new ClientContact[contactsOfEditableTaxAgency
					.size()];
			int i = 0;
			ClientContact temp = null;
			for (ClientContact contact : contactsOfEditableTaxAgency) {
				records[i] = new ClientContact();
				// FIXME--the contactgrid fields values are populated here

				if (contact.isPrimary()) {
					temp = records[i];
					records[i].setPrimary(Boolean.TRUE);
				}
				records[i].setTitle(contact.getTitle());
				records[i].setName(contact.getName());
				records[i].setBusinessPhone(contact.getBusinessPhone());
				records[i++].setEmail(contact.getEmail());

			}
			if (data.getPaymentTerm() != 0) {
				ClientPaymentTerms payment = getCompany().getPaymentTerms(
						data.getPaymentTerm());
				paymentTermsCombo.setComboItem(payment);
			}

			if (data.getVATReturn() == ClientTAXAgency.RETURN_TYPE_NONE)
				vatReturnCombo.setComboItem("");
			else if (data.getVATReturn() == ClientTAXAgency.RETURN_TYPE_UK_VAT)
				vatReturnCombo.setComboItem(messages.ukVAT());
			else
				vatReturnCombo.setComboItem(messages.vat3Ireland());

			if (data.getSalesLiabilityAccount() != 0) {
				ClientAccount account = getCompany().getAccount(
						data.getSalesLiabilityAccount());
				liabilitySalesAccountCombo.setComboItem(account);
			}

			if (data.getPurchaseLiabilityAccount() != 0) {
				ClientAccount account = getCompany().getAccount(
						data.getPurchaseLiabilityAccount());
				liabilityPurchaseAccountCombo.setComboItem(account);
			}

			// gridView.
			// gridView.setAllRows(Arrays.asList(records));
			int row = 0;
			for (ClientContact clientContact : records) {
				if (clientContact.isPrimary()) {
					gridView.add(clientContact);
					gridView.checkColumn(row, 0, true);
				} else {
					gridView.add(clientContact);
				}
				row++;
			}

			// // if (temp != null)
			// gridView.selectRecord(temp);
			// Setting Memo
			memoArea.setValue(data.getMemo() != null ? data.getMemo() : "");

		} else {
			// For Creating TaxAgency
			setData(new ClientTAXAgency());
			addrsForm = new AddressForm(null);
			// addrsForm.setWidth("100%");
			addrsForm.setDisabled(isInViewMode());
			phoneFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
					.getViewName());
			// phoneFaxForm.setWidth("100%");
			emailForm = new EmailForm(null, null, this, this.getAction()
					.getViewName());
			// emailForm.setWidth("100%");
		}

		// phoneFaxForm.getCellFormatter().setWidth(0, 0, "235");
		// phoneFaxForm.getCellFormatter().setWidth(0, 1, "");
		//
		// addrsForm.getCellFormatter().setWidth(0, 0, "50");
		// addrsForm.getCellFormatter().setWidth(0, 1, "125");
		//
		// emailForm.getCellFormatter().setWidth(0, 0, "235");
		// emailForm.getCellFormatter().setWidth(0, 1, "");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(taxAgencyForm);
		leftVLay.add(accInfoForm);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("100%");
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.add(addrsForm);
		rightVLay.add(phoneFaxForm);
		rightVLay.add(emailForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(5);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel contHLay = new HorizontalPanel();
		contHLay.setSpacing(5);
		contHLay.add(contacts);

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(lab);
		mainVlay.add(topHLay);
		mainVlay.add(contHLay);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(addButton);
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);
		panel.getElement().getStyle().setFloat(Float.RIGHT);

		mainVlay.add(gridView);
		mainVlay.setWidth("100%");
		mainVlay.add(panel);
		// mainVlay.add(memoForm);

		/* Adding dynamic forms in list */
		listforms.add(taxAgencyForm);
		listforms.add(accInfoForm);
		listforms.add(memoForm);

		// if (UIUtils.isMSIEBrowser()) {
		// accInfoForm.getCellFormatter().setWidth(0, 1, "200px");
		// accInfoForm.setWidth("68%");
		// }

		return mainVlay;
	}

	private List<String> getTAXFilingFrequencies() {
		List<String> list = new ArrayList<String>();
		list.add(messages.monthly());
		list.add(messages.quarterly());
		list.add(messages.halfYearly());
		list.add(messages.yearly());
		return list;
	}

	private void initListGrid() {
		gridView = new ContactsTable() {

			@Override
			protected boolean isInViewMode() {
				return TAXAgencyView.this.isInViewMode();
			}
		};
		gridView.setDisabled(true);
		// gridView.setCanEdit(true);
		// gridView.isEnable = false;
		// gridView.init();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {

		if (getData() == null) {
			setData(new ClientTAXAgency());
		} else {
			initPaymentTermsCombo();
			if (isInViewMode()) {
				this.selectedSalesAccount = getCompany().getAccount(
						data.getSalesLiabilityAccount());
				this.selectedPurchaseAccount = getCompany().getAccount(
						data.getPurchaseLiabilityAccount());
			}
			taxTypeSelected(getTaxTypeString(data.getTaxType()));
			liabilitySalesAccountCombo.select(selectedSalesAccount);
			liabilityPurchaseAccountCombo.select(selectedPurchaseAccount);
			tAXFilingFrequency.setSelectedItem(getData()
					.getTAXFilingFrequency());
		}
	}

	// private void initLiabilityAccounts() {
	// List<ClientAccount> liabilityAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	//
	// if (account.getType() != ClientAccount.TYPE_CASH
	// && account.getType() != ClientAccount.TYPE_BANK
	// && account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// liabilityAccounts.add(account);
	// }
	//
	// }
	//
	// liabilitySalesAccountCombo.initCombo(liabilityAccounts);
	// if (takenTaxAgency != null
	// && (takenTaxAgency.getSalesLiabilityAccount()) != null) {
	// selectedAccount = FinanceApplication.getCompany().getAccount(
	// takenTaxAgency.getSalesLiabilityAccount());
	// liabilitySalesAccountCombo.setComboItem(selectedAccount);
	// }
	//
	// }

	public static TAXAgencyView getInstance() {

		taxAgencyView = new TAXAgencyView();

		return taxAgencyView;

	}

	// protected void adjustFormWidths(int titlewidth, int listBoxWidth) {
	//
	// addrsForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	// addrsForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	//
	// phoneFaxForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	// phoneFaxForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	//
	// taxAgencyForm.getCellFormatter().getElement(0, 0).getStyle().setWidth(
	// titlewidth + listBoxWidth, Unit.PX);
	// emailForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + titlewidth + "");
	// emailForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	// accInfoForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// listBoxWidth + "");
	// memoForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	//
	// }

	// @Override
	// protected void onLoad() {
	// int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	// adjustFormWidths(titlewidth, listBoxWidth);
	//
	// adjustEmailFormWidths();
	//
	// super.onLoad();
	// }
	//
	// @Override
	// protected void onAttach() {
	//
	// int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	//
	// adjustFormWidths(titlewidth, listBoxWidth);
	//
	// super.onAttach();
	// }

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.taxAgencyText.setFocus();
	}

	// private void adjustEmailFormWidths() {
	// String width = this.accInfoForm.getCellFormatter().getElement(3, 0)
	// .getOffsetWidth()
	// + "px";
	// this.emailForm.getCellFormatter().getElement(0, 0).setAttribute(
	// "width", width);
	// String w = this.taxAgencyForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth()
	// + "px";
	// this.addrsForm.getCellFormatter().getElement(0, 2).setAttribute(
	// "width", w);
	//
	// }

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.TAXAGENCY, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		addButton.setEnabled(!isInViewMode());
		taxAgencyText.setDisabled(isInViewMode());
		fileAsText.setDisabled(isInViewMode());
		statusCheck.setDisabled(isInViewMode());
		paymentTermsCombo.setDisabled(isInViewMode());
		taxTypeCombo.setDisabled(isInViewMode());
		vatReturnCombo.setDisabled(isInViewMode());
		liabilitySalesAccountCombo.setDisabled(isInViewMode());
		liabilityPurchaseAccountCombo.setDisabled(isInViewMode());
		memoArea.setDisabled(isInViewMode());
		addrsForm.setDisabled(isInViewMode());
		phoneFaxForm.setDisabled(isInViewMode());
		emailForm.setDisabled(isInViewMode());
		gridView.setDisabled(isInViewMode());
		tAXFilingFrequency.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.taxAgency();
	}

	private int getTaxType(String taxType) {
		if (taxType == null) {
			return 0;
		}
		if (taxType.equals(messages.salesTax())) {
			return 1;
		} else if (taxType.equals(messages.vat())) {
			return 2;
		} else if (taxType.equals(messages.serviceTax())) {
			return 3;
		} else if (taxType.equals(messages.tds())) {
			return 4;
		} else if (taxType.equals(messages.other())) {
			return 5;
		} else {
			return 0;
		}
	}

	private String getTaxTypeString(int type) {
		switch (type) {
		case 1:
			return messages.salesTax();
		case 2:
			return messages.vat();
		case 3:
			return messages.serviceTax();
		case 4:
			return messages.tds();
		case 5:
			return messages.other();
		default:
			return "";
		}
	}

	private SelectCombo createTaxTypeSelectCombo() {
		SelectCombo taxTypeCombo = new SelectCombo(messages.taxType());
		String[] types = new String[] { messages.salesTax(), messages.vat(),
				messages.serviceTax(), messages.tds(), messages.other() };
		taxTypeCombo.initCombo(Arrays.asList(types));
		taxTypeCombo.setDisabled(isInViewMode());
		taxTypeCombo.setRequired(true);
		taxTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						taxTypeSelected(selectItem);
					}
				});
		return taxTypeCombo;
	}

	private void taxTypeSelected(String selectedType) {
		int type = getTaxType(selectedType);
		taxTypeCombo.setComboItem(selectedType);
		if (type == ClientTAXAgency.TAX_TYPE_SERVICETAX
				|| type == ClientTAXAgency.TAX_TYPE_VAT) {
			liabilitySalesAccountCombo.setRequired(true);
			liabilityPurchaseAccountCombo.setRequired(true);

			liabilitySalesAccountCombo.setVisible(true);
			liabilityPurchaseAccountCombo.setVisible(true);
		} else if (type == ClientTAXAgency.TAX_TYPE_SALESTAX) {
			liabilitySalesAccountCombo.setRequired(true);
			liabilityPurchaseAccountCombo.setRequired(false);

			liabilitySalesAccountCombo.setVisible(true);
			liabilityPurchaseAccountCombo.setVisible(false);
		} else if (type == ClientTAXAgency.TAX_TYPE_TDS) {
			liabilitySalesAccountCombo.setRequired(false);
			liabilityPurchaseAccountCombo.setRequired(true);

			liabilitySalesAccountCombo.setVisible(false);
			liabilityPurchaseAccountCombo.setVisible(true);
		} else {
			liabilitySalesAccountCombo.setRequired(false);
			liabilityPurchaseAccountCombo.setRequired(false);

			liabilitySalesAccountCombo.setVisible(true);
			liabilityPurchaseAccountCombo.setVisible(true);
		}

		if (type != ClientTAXAgency.TAX_TYPE_VAT) {
			vatReturnCombo.setComboItem("");
			vatReturnCombo.setRequired(false);
			vatReturnCombo.setVisible(false);
		} else {
			List<String> vatReturns = getVatReturns();
			if (vatReturns == null || vatReturns.isEmpty()) {
				return;
			} else if (vatReturns.size() == 1) {
				vatReturnCombo.setComboItem(vatReturns.get(0));
			} else {
				vatReturnCombo.initCombo(vatReturns);
			}
			if (getCompany().getCountry().equals(
					CountryPreferenceFactory.UNITED_KINGDOM)) {
				vatReturnCombo.setRequired(true);
			}
			vatReturnCombo.setVisible(true);
		}
	}

	private List<String> getVatReturns() {
		List<String> vatReturns = new ArrayList<String>();
		vatReturns.add(messages.ukVAT());
		vatReturns.add(messages.vat3Ireland());
		return vatReturns;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
