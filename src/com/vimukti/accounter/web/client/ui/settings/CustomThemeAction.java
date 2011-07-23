package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CustomThemeAction extends Action {

	public CustomThemeAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			CustomThemeDialog customThemeDialog = new CustomThemeDialog(
					Accounter.getSettingsMessages()
							.newBrandThemeLabel(), "");
			customThemeDialog.show();
			customThemeDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
