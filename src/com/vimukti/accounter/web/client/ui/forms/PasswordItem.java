package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;

public class PasswordItem extends FormItem<String> {

	public PasswordTextBox passwordBox;

	// TextBoxItem textBoxItem;
	public PasswordItem(String lable) {
		super(lable,"PasswordItem");
		passwordBox = new PasswordTextBox();
		setPlaceholder(lable, passwordBox);
		passwordBox.addStyleName("passwordItem");
		this.passwordBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				PasswordItem.this.showValidated();

			}
		});

		this.add(passwordBox);

	}

	public static void setPlaceholder(String text, TextBox box) {
		box.getElement().setAttribute("placeholder", text);
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		passwordBox.setTitle(toolTip);
	}

	@Override
	public String getValue() {
		if (passwordBox.getText() == null)
			return "";
		return passwordBox.getText();
	}

	@Override
	public String getDisplayValue() {
		if (passwordBox.getText() == null)
			return "";
		return passwordBox.getText();

	}

	@Override
	public void setValue(String value) {
		if (value != null)
			this.passwordBox.setValue(value);

	}

	public void setKeyPressHandler(KeyPressListener keyPressListener) {
		this.passwordBox.addKeyPressHandler((KeyPressHandler) keyPressListener);
	}

	@Override
	public void addBlurHandler(BlurHandler blurHandler) {
		passwordBox.addBlurHandler(blurHandler);

	}

	public void setKeyBoardHandler(KeyPressHandler keyPressHandler) {
		passwordBox.addKeyPressHandler(keyPressHandler);
	}

	public void setKeyDownHandler(KeyDownHandler keyDownHandler) {
		passwordBox.addKeyDownHandler(keyDownHandler);
	}

	@Override
	public void addChangeHandler(ChangeHandler changeHandler) {
		passwordBox.addChangeHandler(changeHandler);
	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		passwordBox.addClickHandler(handler);
	}

	public void setDefaultValue(int i) {
		// NOTHING TO DO.
	}

	// void helpimformationsetposition(){
	//
	// }

	public void setHint(String string) {

	}

	public void focusInItem() {
		this.passwordBox.setFocus(true);

	}

	private void setBorder() {

	}

	@Override
	public FocusWidget getMainWidget() {
		return passwordBox;
	}

	@Override
	public void setEnabled(boolean b) {
		if (!b) {
			this.passwordBox.addStyleName("disable-TextField");
		} else {
			this.passwordBox.setStyleName("gwt-TextBox");
		}
		this.passwordBox.setEnabled(b);

	}

	public void removeStyleName(String style) {
		passwordBox.removeStyleName(style);
	}

}