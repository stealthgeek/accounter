package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.PreferencePage;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PreferenceSettingsView extends BaseView<ClientCompanyPreferences> {

	private ScrollPanel pageDetailsPane;
	private AccounterMessages messages = Accounter.messages();
	private List<PreferencePage> preferencePages;
	private List<HTML> optionLinks = new ArrayList<HTML>();

	@Override
	public void init() {
		super.init();
		createControls();
		addStyleName("fullSizePanel");
	}

	private void createControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		final StackPanel stackPanel = new StackPanel();
		pageDetailsPane = new ScrollPanel();
		pageDetailsPane.addStyleName("pre_scroll_table");
		preferencePages = getPreferencePages();
		for (PreferencePage page : preferencePages) {
			VerticalPanel pageView = createPageView(page);
			stackPanel.add(pageView, page.getTitle());
			pageView.getElement().getParentElement()
					.setAttribute("height", "230px");
		}
		stackPanel.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex = stackPanel.getSelectedIndex();
				PreferencePage selectedPage = preferencePages
						.get(selectedIndex);
				pageDetailsPane.clear();
				pageDetailsPane.add(selectedPage);

			}
		}, ClickEvent.getType());

		pageDetailsPane.clear();
		pageDetailsPane.add(preferencePages.get(0));
		mainPanel.add(stackPanel);
		mainPanel.add(pageDetailsPane);
		mainPanel.setCellWidth(pageDetailsPane, "70%");
		mainPanel.setCellWidth(stackPanel, "30%");

		mainPanel.setCellHeight(pageDetailsPane, "96%");
		mainPanel.setCellHeight(stackPanel, "100%");

		pageDetailsPane.setSize("100%", "400px");
		mainPanel.setSize("100%", "100%");
		stackPanel.setSize("250px", "100%");
		mainPanel.addStyleName("fullSizePanel");
		mainPanel.addStyleName("company_stackpanel_view");
		this.add(mainPanel);
		setSize("100%", "100%");
	}

	private List<PreferencePage> getPreferencePages() {
		List<PreferencePage> preferenceList = new ArrayList<PreferencePage>();
		preferenceList.add(getCompanyContactInfoPage());
		preferenceList.add(getCompanyInfoPage());
		preferenceList.add(getCatogiriesInfoPage());
		preferenceList.add(getCustomerAndVendorPage());
		preferenceList.add(getAgningDetailsPage());
		preferenceList.add(getTerminoligies());
		return preferenceList;
	}

	private PreferencePage getAgningDetailsPage() {
		PreferencePage agningDetailsPage = new PreferencePage(
				messages.productAndServices());
		AgeingAndSellingDetailsOption ageingAndSellingDetailsOption = new AgeingAndSellingDetailsOption();
		ProductAndServicesOption productAndServicesOption = new ProductAndServicesOption();
		BillableExpenseTrackingByCustomer billableExpenseTrackingByCustomer = new BillableExpenseTrackingByCustomer();

		agningDetailsPage.addPreferenceOption(productAndServicesOption);
		agningDetailsPage.addPreferenceOption(ageingAndSellingDetailsOption);
		agningDetailsPage
				.addPreferenceOption(billableExpenseTrackingByCustomer);
		return agningDetailsPage;
	}

	private PreferencePage getCustomerAndVendorPage() {
		PreferencePage customerAndVendorPage = new PreferencePage(
				messages.vendorAndPurchases());
		CustomerAndVendorsSettingsOption customerAndVendorsSettingsPage = new CustomerAndVendorsSettingsOption();
		ManageBillsOption manageBillsOption = new ManageBillsOption();
		TrackEstimatesOption estimatesOption = new TrackEstimatesOption();

		DoyouUseShipingsOption shipingsOption = new DoyouUseShipingsOption();
		customerAndVendorPage.addPreferenceOption(manageBillsOption);
		customerAndVendorPage
				.addPreferenceOption(customerAndVendorsSettingsPage);
		customerAndVendorPage.addPreferenceOption(estimatesOption);

		customerAndVendorPage.addPreferenceOption(shipingsOption);
		return customerAndVendorPage;
	}

	private PreferencePage getProductAndServicePage() {
		PreferencePage productAndServicePage = new PreferencePage(
				messages.productAndServices());

		return productAndServicePage;
	}

	private PreferencePage getCatogiriesInfoPage() {
		PreferencePage catogiriesInfoPage = new PreferencePage(Accounter
				.messages().Categories());
		LocationTrackingOption locationTrackingOption = new LocationTrackingOption();
		ClassTrackingOption classTrackingPage = new ClassTrackingOption();
		catogiriesInfoPage.addPreferenceOption(locationTrackingOption);
		catogiriesInfoPage.addPreferenceOption(classTrackingPage);
		return catogiriesInfoPage;
	}

	private PreferencePage getCompanyInfoPage() {
		PreferencePage companyInfoPage = new PreferencePage(Accounter
				.messages().company());

		// OrganisationTypeOption formOption = new OrganisationTypeOption();
		CompanyDateFormateOption formateOption = new CompanyDateFormateOption();
		CompanyEinOption einOption = new CompanyEinOption();
		CompanyFiscalYearOption fiscalYearOption = new CompanyFiscalYearOption();
		DoyouUseOption doyouUseOption = new DoyouUseOption();
		CompanyCurrencyOption currencyOption = new CompanyCurrencyOption();
		CompanyTimeZoneOption timeZoneOption = new CompanyTimeZoneOption();

		// if (getCompany().getCountry().equals(
		// CountryPreferenceFactory.UNITED_STATES)) {
		// companyInfoPage.addPreferenceOption(formOption);
		// }
		companyInfoPage.addPreferenceOption(formateOption);
		companyInfoPage.addPreferenceOption(einOption);
		companyInfoPage.addPreferenceOption(fiscalYearOption);
		companyInfoPage.addPreferenceOption(doyouUseOption);
		companyInfoPage.addPreferenceOption(currencyOption);
		companyInfoPage.addPreferenceOption(timeZoneOption);

		return companyInfoPage;
	}

	private PreferencePage getCompanyContactInfoPage() {
		PreferencePage companyContactInfoPage = new PreferencePage(Accounter
				.messages().comapnyInfo());
		CompanyNameOption name = new CompanyNameOption();
		CompanyAddressOption address = new CompanyAddressOption();
		CompanyEmailOption email = new CompanyEmailOption();
		ComapnyWebsiteOption website = new ComapnyWebsiteOption();
		CompanyPhoneNumberOption phone = new CompanyPhoneNumberOption();

		companyContactInfoPage.addPreferenceOption(name);
		companyContactInfoPage.addPreferenceOption(address);
		companyContactInfoPage.addPreferenceOption(email);
		companyContactInfoPage.addPreferenceOption(website);
		companyContactInfoPage.addPreferenceOption(phone);

		return companyContactInfoPage;
	}

	private PreferencePage getTerminoligies() {
		PreferencePage teriminalogyPreferencePage = new PreferencePage(
				messages.accounterTerminologies());
		CustomerTerminologyOption productAndServicesOption = new CustomerTerminologyOption();
		VendorTerninalogyOption terminalogyOption = new VendorTerninalogyOption();
		teriminalogyPreferencePage
				.addPreferenceOption(productAndServicesOption);
		teriminalogyPreferencePage.addPreferenceOption(terminalogyOption);
		return teriminalogyPreferencePage;
	}

	private VerticalPanel createPageView(final PreferencePage page) {
		final VerticalPanel pageView = new VerticalPanel();
		pageView.setWidth("100%");
		List<AbstractPreferenceOption> options = page.getOptions();
		for (int index = 0; index < options.size(); index++) {
			final AbstractPreferenceOption option = options.get(index);
			final HTML optionLink = new HTML("<a class='stackPanelLink'>"
					+ option.getTitle() + "</a>");
			pageView.add(optionLink);
			// PreferenceOptionLinks.addLink(optionLink);
			if (index == 0) {
				optionLink.getElement().getParentElement()
						.addClassName("contentSelected");
			}
			optionLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (int index = 0; index < pageView.getWidgetCount(); index++) {
						Widget widget = pageView.getWidget(index);
						widget.getElement().getParentElement()
								.removeClassName("contentSelected");
					}
					optionLink.getElement().getParentElement()
							.addClassName("contentSelected");
					pageDetailsPane.ensureVisible(option);

				}
			});
		}
		return pageView;
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.cancelButton = new CancelButton(this);
		saveAndCloseButton.setText(messages.update());
		cancelButton.setText(messages.close());
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(cancelButton);
		saveAndCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (preferencePages == null) {
					return;
				}
				for (PreferencePage page : preferencePages) {
					page.onSave();
					if (!page.canSave) {
						return;
					}
				}
				Accounter.updateCompany(PreferenceSettingsView.this,
						Accounter.getCompany());
				Accounter.reset();
			}
		});
	}

	@Override
	protected void changeButtonBarMode(boolean disable) {

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String errorString = AccounterExceptions.getErrorString(exception
				.getErrorCode());
		Accounter.showError(errorString);
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
