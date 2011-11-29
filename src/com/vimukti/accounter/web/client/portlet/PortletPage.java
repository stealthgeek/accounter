package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.Portlet;

public class PortletPage extends AbsolutePanel implements DragHandler {

	public static final String DASHBOARD = "dashboard";

	private String name;
	public ClientPortletPageConfiguration config;
	private PortletColumn[] columns;
	private PickupDragController dragController;
	private ImageButton settingsButton;
	public boolean haveToRefresh = true;

	public PortletPage(String pageName) {
		this.name = pageName;
		refreshPage();
	}

	public void refreshPage() {
		this.addStyleName("portletPage");

		Accounter.createHomeService().getPortletPageConfiguration(name,
				new AsyncCallback<ClientPortletPageConfiguration>() {

					@Override
					public void onSuccess(ClientPortletPageConfiguration arg0) {
						config = arg0;
						setup();
						refreshWidgets();
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setup() {
		createTitlePanel();
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		this.add(panel);
		dragController = new PickupDragController(this, false);
		dragController.setBehaviorMultipleSelection(false);
		dragController.addDragHandler(this);
		// create columns
		columns = new PortletColumn[config.getColumnsCount()];
		for (int x = 0; x < config.getColumnsCount(); x++) {
			columns[x] = new PortletColumn(x);
			panel.add(columns[x]);
			dragController.registerDropController(columns[x]
					.getDropController());
		}
		// create the portlets in them
		for (ClientPortletConfiguration pc : config.getPortletConfigurations()) {
			addPortletToPage(pc);
		}

	}

	private void addPortletToPage(ClientPortletConfiguration pc) {
		Portlet portlet = createPortlet(pc);
		portlet.setPortletPage(this);
		columns[pc.column].addPortlet(portlet);
		dragController.makeDraggable(portlet, portlet.getHeader());
	}

	private void createTitlePanel() {
		HorizontalPanel pageTitlePanel = new HorizontalPanel();
		settingsButton = new ImageButton(Accounter.messages()
				.configurePortlets(), Accounter.getFinanceImages()
				.portletPageSettings());
		pageTitlePanel.add(settingsButton);
		settingsButton.addStyleName("settingsButton");
		this.add(settingsButton);
		settingsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createSettingsDialog();
			}
		});
	}

	protected void createSettingsDialog() {
		updateConfiguration();
		PortletPageConfigureDialog configureDialog = new PortletPageConfigureDialog(
				Accounter.messages().configurePortlets(), config, this);
		configureDialog.showRelativeTo(settingsButton);

	}

	private Portlet createPortlet(ClientPortletConfiguration pc) {
		return PortletFactory.get().createPortlet(pc, this);
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		refreshWidgets();
		haveToRefresh = false;
		updatePortletPage();
	}

	public void updatePortletPage() {
		Accounter.createHomeService().savePortletConfig(config,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean arg0) {
						if (arg0 && haveToRefresh) {
							clear();
							refreshPage();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						System.err.println(arg0.toString());
					}
				});
	}

	private void updateConfiguration() {
		ArrayList<ClientPortletConfiguration> configs = new ArrayList<ClientPortletConfiguration>();
		for (PortletColumn column : columns) {
			for (Portlet portlet : column.getPortlets()) {
				configs.add(portlet.getConfiguration());
			}
		}
		config.setPortletsConfiguration(configs);
		config.setPageName(name);
	}

	@Override
	public void onDragStart(DragStartEvent event) {

	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {

	}

	public DragController getDragController() {
		return dragController;
	}

	public void refreshWidgets() {
		if (columns != null)
			for (PortletColumn column : this.columns) {
				column.refreshWidgets();
			}
	}

}