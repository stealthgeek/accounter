package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;

public class CreatePartialCompanyCommand extends AbstractCompanyCommad {

	private String country;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		list.add(getCompanyNameRequirement());

		list.add(new NameRequirement(LEGAL_NAME, getMessages().pleaseEnter(
				getMessages().legalName()), getMessages().legalName(), true,
				true));

		list.add(new NameRequirement(TAX_ID, getMessages().pleaseEnter(
				getMessages().taxId()), getMessages().taxId(), true, true));

		list.add(new StringListRequirement(COUNTRY, getMessages()
				.pleaseEnterName(getMessages().country()), getMessages()
				.country(), true, true, new ChangeListner<String>() {

			@Override
			public void onSelection(String value) {
				country = value;
			}
		}) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add(getMessages().country(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().country());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCountryList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().country());
			}
		});

		list.add(new StringListRequirement(STATE, getMessages()
				.pleaseEnterName(getMessages().state()), getMessages().state(),
				true, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add(getMessages().state(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().state());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getStatesList(country);
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().state());
			}
		});

		list.add(new NameRequirement(ADDRESS1, getMessages().pleaseEnter(
				getMessages().address1()), getMessages().address1(), true, true));

		list.add(new NameRequirement(ADDRESS2, getMessages().pleaseEnter(
				getMessages().address2()), getMessages().address2(), true, true));

		list.add(new NameRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));

		list.add(new NameRequirement(ZIPCODE, getMessages().pleaseEnter(
				getMessages().zipCode()), getMessages().zipCode(), true, true));

		list.add(new NameRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phone()), getMessages().phone(), true, true));

		list.add(new NameRequirement(FAX, getMessages().pleaseEnter(
				getMessages().fax()), getMessages().fax(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEB_SITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new StringListRequirement(TIME_ZONE, getMessages()
				.pleaseSelect(getMessages().timezone()), getMessages()
				.timezone(), true, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add(getMessages().timezone(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().timezone());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTimeZonesList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().timezone());
			}
		});

		// Second Page
		list.add(new StringListRequirement(INDUSTRY, getMessages()
				.pleaseSelect(getMessages().industry()), getMessages()
				.industry(), false, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().industry());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getIndustryList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().industry());
			}
		});

		list.add(new StringListRequirement(ORGANIZATION_REFER, getMessages()
				.pleaseEnterName(getMessages().companyOrganization()),
				getMessages().companyOrganization(), true, true, null) {

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add(getMessages().organisation(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getSelectString() {
				return getMessages().howisYourCompanyOrganized();
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getOrganizationTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

		});

		list.add(new StringListRequirement(FISCAL_YEAR, getMessages()
				.pleaseSelect(getMessages().fiscalYear()), getMessages()
				.fiscalYear(), true, true, null) {

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add(getMessages().fiscalYear(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				super.setCreateCommand(list);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().fiscalYear());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getFiscalYearMonths();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().fiscalYear());
			}
		});

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String companyName = get(COMPANY_NAME).getValue();
		String industry = get(INDUSTRY).getValue();
		String legalName = get(LEGAL_NAME).getValue();
		String taxId = get(TAX_ID).getValue();
		String countryName = get(COUNTRY).getValue();
		String state = get(STATE).getValue();
		String phoneNum = get(PHONE).getValue();
		String fax = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webSite = get(WEB_SITE).getValue();
		String timeZone = get(TIME_ZONE).getValue();
		String address1 = get(ADDRESS1).getValue();
		String address2 = get(ADDRESS2).getValue();
		String city = get(CITY).getValue();
		String zipCode = get(ZIPCODE).getValue();

		Integer industryType = getIndustryList().indexOf(industry);

		List<TemplateAccount> accounts = getDefaultTemplateAccounts(industryType);
		ClientCompanyPreferences preferences = new ClientCompanyPreferences();

		ICountryPreferences countryPreferences = CountryPreferenceFactory
				.get(countryName);
		if (countryPreferences != null) {
			// preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
			// countryPreferences.getDefaultFiscalYearStartingMonth()));
		}

		ClientAddress address = new ClientAddress();
		preferences.setTradingName(companyName);
		preferences.setLegalName(legalName);
		preferences.setPhone(phoneNum);
		preferences.setCompanyEmail(emailId);
		preferences.setTaxId(taxId);
		preferences.setFax(fax);
		preferences.setWebSite(webSite);
		address.setAddress1(address1);
		address.setStreet(address2);
		address.setCity(city);
		address.setZipOrPostalCode(zipCode);
		address.setStateOrProvinence(state);
		address.setCountryOrRegion(countryName);
		preferences.setTradingAddress(address);
		preferences.setTimezone(timeZone);
		preferences.setIndustryType(industryType);
		String organizationRefer = get(ORGANIZATION_REFER).getValue();
		preferences.setOrganizationType(getOrganizationTypes().indexOf(
				organizationRefer));
		setStartDateOfFiscalYear(preferences);
		preferences.setPrimaryCurrency(getCurrenciesList().get(0));
		AccounterCompanyInitializationServiceImpl.intializeCompany(preferences,
				accounts, context.getIOSession().getClient());

		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().company());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(COUNTRY).setDefaultValue("United Kingdom");
		get(STATE).setDefaultValue("Buckinghamshire");
		get(TIME_ZONE).setDefaultValue(getDefaultTzOffsetStr());
		get(ORGANIZATION_REFER).setDefaultValue(getOrganizationTypes().get(0));
		get(FISCAL_YEAR).setDefaultValue(getMessages().april());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().company());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().partialCompanySetup());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}
}
