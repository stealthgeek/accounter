package com.vimukti.accounter.web.client.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class AddNewButton extends ImageButton {

	private AbstractBaseView<?> view;

	public AddNewButton(AbstractBaseView<?> baseView) {
		super(Accounter.constants().addNew(), Accounter.getFinanceImages()
				.addIcon());
		this.view = baseView;
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.onSave(false);
			}
		});
	}

}
