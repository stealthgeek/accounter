package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */

public class AddressDialog extends BaseDialog<ClientAddress> {

	ClientAddress addNew;
	TextAreaItem textAreaItem;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private TextItem address1;
	private TextItem address2;
	private TextItem city;
	private TextItem state;
	private TextItem country;
	private TextItem zip;
	private String addressType;

	public AddressDialog(String title, String description,
			final TextAreaItem textAreaItem, final String addressType,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {
		super(Accounter.messages().address(), "");
		this.addressType = addressType;
		this.allAddresses = allAddresses;
		createControls(textAreaItem, allAddresses);

	}

	protected void createControls(TextAreaItem textAreaItem,
			LinkedHashMap<Integer, ClientAddress> allAddresses) {

		this.textAreaItem = textAreaItem;
		ClientAddress add = null;
		if (allAddresses != null) {
			add = allAddresses.get(UIUtils.getAddressType(addressType));
		}
		address1 = new TextItem(Accounter.messages().address1());
		address1.setHelpInformation(true);

		address2 = new TextItem(Accounter.messages().address2());
		address2.setHelpInformation(true);

		city = new TextItem(Accounter.messages().city());
		city.setHelpInformation(true);

		state = new TextItem(Accounter.messages().state());
		state.setHelpInformation(true);

		country = new TextItem(Accounter.messages().country());
		country.setHelpInformation(true);

		zip = new TextItem(Accounter.messages().postalCode());
		zip.setHelpInformation(true);

		if (add != null) {
			if (add.getAddress1() != null)
				address1.setValue(add.getAddress1());
			if (add.getStreet() != null)
				address2.setValue(add.getStreet());
			if (add.getCity() != null)
				city.setValue(add.getCity());
			if (add.getStateOrProvinence() != null)
				state.setValue(add.getStateOrProvinence());
			if (add.getZipOrPostalCode() != null)
				zip.setValue(add.getZipOrPostalCode());
			if (add.getCountryOrRegion() != null)
				country.setValue(add.getCountryOrRegion());
		}

		DynamicForm AddressForm = new DynamicForm();
		AddressForm.setCellSpacing(10);
		AddressForm.setWidth("100%");
		AddressForm.setFields(address1, address2, city, state, zip, country);
		addNew = add;

		VerticalPanel v1 = new VerticalPanel();

		v1.add(AddressForm);
		v1.setHeight("100%");
		v1.setWidth("100%");

		setBodyLayout(v1);
		setAddressToTextAread(new ValidationResult());
		center();

	}

	public void close() {
		removeFromParent();
	}

	@Override
	public Object getGridColumnValue(ClientAddress obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	public String[] getCountryList() {
		// List list = new ArrayList();
		String[] list = { Accounter.messages().australia(),
				Accounter.messages().belgium(),
				Accounter.messages().canada(), Accounter.messages().cyprus(),
				Accounter.messages().france(),
				Accounter.messages().germany(),
				Accounter.messages().greece(), Accounter.messages().india(),
				Accounter.messages().ireland(), Accounter.messages().italy(),
				Accounter.messages().kenya(), Accounter.messages().malta(),
				Accounter.messages().mauritius(),
				Accounter.messages().mozabique(),
				Accounter.messages().netherlands(),
				Accounter.messages().newZeland(),
				Accounter.messages().nigeria(),
				Accounter.messages().pakistan(),
				Accounter.messages().portugal(),
				Accounter.messages().southAfrica(),
				Accounter.messages().spain(),
				Accounter.messages().switzerland(),
				Accounter.messages().thailand(), Accounter.messages().uk(),
				Accounter.messages().usa(), Accounter.messages().others() };

		return list;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		setAddressToTextAread(result);
		return result;
	}

	private void setAddressToTextAread(ValidationResult result) {
		String toBeSet = "";
		int isEmptyCounter = 0;

		ClientAddress value = new ClientAddress();
		value.setType(UIUtils.getAddressType(addressType));
		if (address1.getValue().toString().trim() != null
				&& !address1.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet = address1.getValue().toString().trim() + "\n";
			value.setAddress1(address1.getValue().toString().trim());
		}
		if (address2.getValue().toString().trim() != null
				&& !address2.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += address2.getValue().toString().trim() + "\n";
			value.setStreet(address2.getValue().toString().trim());
		}

		if (city.getValue() != null
				&& !city.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += city.getValue().toString().trim() + "\n";
			value.setCity(city.getValue().toString().trim());
		}

		if (state.getValue() != null
				&& !state.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += state.getValue().toString().trim() + "\n";
			value.setStateOrProvinence(state.getValue().toString().trim());
		}

		if (zip.getValue() != null && !zip.getValue().toString().isEmpty()) {
			isEmptyCounter++;
			toBeSet += zip.getValue().toString().trim() + "\n";
			value.setZipOrPostalCode(zip.getValue().toString().trim());
		}

		if (country.getValue() != null
				&& !country.getValue().toString().trim().isEmpty()) {
			isEmptyCounter++;
			toBeSet += country.getValue().toString().trim();
			value.setCountryOrRegion(country.getValue().toString().trim());
		}

		if (toBeSet != null && !toBeSet.isEmpty() && isEmptyCounter != 0) {
			textAreaItem.setValue(toBeSet);
			allAddresses.put(UIUtils.getAddressType(addressType), value);
		} else {
			textAreaItem.setValue("");
			result.addError(this, Accounter.messages().shouldNotEmpty());
		}
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {
		address1.setFocus();

	}
}
