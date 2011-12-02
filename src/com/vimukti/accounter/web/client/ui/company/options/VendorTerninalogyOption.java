/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * @author vimukti2
 * 
 */
public class VendorTerninalogyOption extends AbstractPreferenceOption {

	private static VendorTerninalogyOptionUiBinder uiBinder = GWT
			.create(VendorTerninalogyOptionUiBinder.class);
	@UiField
	RadioButton vendorRadioButton;
	@UiField
	RadioButton supplierRadioButton;
	@UiField
	Label vendorsHeaderLabel;
	@UiField
	Label vendorsDescriptionLabel;

	interface VendorTerninalogyOptionUiBinder extends
			UiBinder<Widget, VendorTerninalogyOption> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public VendorTerninalogyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void createControls() {
		vendorsHeaderLabel.setText(messages.useTerminologyFor(Global.get()
				.Vendor()));
		vendorsDescriptionLabel.setText(messages.vendorDescription());
		vendorsDescriptionLabel.setStyleName("organisation_comment");
		vendorRadioButton.setName(messages.Vendor());
		vendorRadioButton.setHTML(messages.Vendor());
		supplierRadioButton.setName(messages.Vendor());
		supplierRadioButton.setHTML(messages.Supplier());
	}

	@Override
	public String getTitle() {
		return "Supplier Terminology";
	}

	@Override
	public void onSave() {
		if (vendorRadioButton.getValue()) {
			getCompanyPreferences().setReferVendors(ClientVendor.VENDOR);
		} else if (supplierRadioButton.getValue()) {
			getCompanyPreferences().setReferVendors(ClientVendor.SUPPLIER);
		}
	}

	@Override
	public String getAnchor() {
		return "Vendor Terminology";
	}

	@Override
	public void initData() {
		int referVendors = getCompanyPreferences().getReferVendors();
		switch (referVendors) {
		case ClientVendor.VENDOR:
			vendorRadioButton.setValue(true);
			break;
		case ClientVendor.SUPPLIER:
			supplierRadioButton.setValue(true);
			break;
		}

	}

}
