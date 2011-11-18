package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class SelectItemTypeDialog extends BaseDialog<ClientItem> {
	RadioGroupItem typeRadio;
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	// private ViewConfiguration configuration;
	boolean forCustomer;
	private DynamicForm typeForm;
	private boolean isDependent = true;
	private String itemname;

	public SelectItemTypeDialog(boolean isGeneratedFromCustomer) {
		super(Accounter.messages().selectItemType(), Accounter.messages()
				.selectOneOfItem());
		this.forCustomer = isGeneratedFromCustomer;
		createControls();
		center();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		// LinkedHashMap<String, String> typeMap = new LinkedHashMap<String,
		// String>();
		// typeMap.put("service", "Service");
		// typeMap.put("non-inventory", "Non-Inventory&nbsp;Item");

		if (getPreferences().isInventoryEnabled()) {
			typeRadio.setValueMap(Accounter.messages().serviceItem(),
					Accounter.messages().inventoryItem(), Accounter
							.messages().nonInventoryItem());
			typeRadio.setDefaultValue(Accounter.messages().serviceItem());
		} else {
			typeRadio.setValueMap(Accounter.messages().serviceItem(),
					Accounter.messages().productItem());
			typeRadio.setDefaultValue(Accounter.messages().serviceItem());
		}

		typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("320px");
		show();
	}

	@Override
	protected ValidationResult validate() {
		return typeForm.validate();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			if (radio.equals(Accounter.messages().serviceItem())) {
				NewItemAction action = new NewItemAction(Accounter.messages()
						.newItem(), forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_SERVICE);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(Accounter.messages().inventoryItem())) {
				NewItemAction action = new NewItemAction(Accounter.messages()
						.newItem(), forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(Accounter.messages().nonInventoryItem())) {
				NewItemAction action = new NewItemAction(Accounter.messages()
						.newItem(), forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(Accounter.messages().productItem())) {
				NewItemAction action = new NewItemAction(Accounter.messages()
						.newItem(), forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			}
			// UIUtils.setCanvas(itemView, configuration);
		}

		return true;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
}
