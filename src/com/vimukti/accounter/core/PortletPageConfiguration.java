package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class PortletPageConfiguration implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int columns = 2;
	private List<PortletConfiguration> portlets = new ArrayList<PortletConfiguration>();
	private long id;
	private User user;
	private String pageName;

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public List<PortletConfiguration> getPortlets() {
		return portlets;
	}

	public void setPortlets(List<PortletConfiguration> portlets) {
		this.portlets = portlets;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getID() {
		return id;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}
