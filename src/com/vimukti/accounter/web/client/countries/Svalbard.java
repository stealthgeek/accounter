package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Svalbard extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Jan Mayen", "Svalbard" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NOK";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.messages().january();
	}
	
}
