package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.vimukti.accounter.web.client.ui.grids.columns.ClickImage;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class UsersActivityList extends CellTable<ClientActivity> {

	private AsyncDataProvider<ClientActivity> listDataProvider;
	private ClientFinanceDate fromDate, endDate;
	protected AccounterMessages messages = Global.get().messages();
	boolean addButton = true;
	private long value;
	private ClientTransaction trans;

	public UsersActivityList() {
		createControls();
		HTML emptyMessage = new HTML(messages.noRecordsToShow());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	public UsersActivityList(ClientFinanceDate fromdate,
			ClientFinanceDate endDate, long value) {
		this.setFromDate(fromdate);
		this.setEndDate(endDate);
		this.setCustomiseValue(value);
		createControls();
		HTML emptyMessage = new HTML(messages.noRecordsToShow());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	private void createControls() {
		listDataProvider = new AsyncDataProvider<ClientActivity>() {
			@Override
			protected void onRangeChanged(HasData<ClientActivity> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				Accounter.createHomeService().getUsersActivityLog(
						getFromDate(), getEndDate(), start, length,
						getCustomiseValue(),
						new AsyncCallback<PaginationList<ClientActivity>>() {

							@Override
							public void onSuccess(
									PaginationList<ClientActivity> result) {
								updateRowData(start, result);
								setRowCount(result.getTotalCount());
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
			}
		};
		setPageSize(50);
		listDataProvider.addDataDisplay(this);

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
		updateGridData();
	}

	private void updateGridData() {

		// Accounter.createHomeService().getUsersActivityLog(fromDate, endDate,
		// new AsyncCallback<ArrayList<ClientActivity>>() {
		//
		// @Override
		// public void onSuccess(ArrayList<ClientActivity> result) {
		// listDataProvider.setList(result);
		// listDataProvider.refresh();
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		//
		// }
		// });

	}

	private void initTableColumns() {

		TextColumn<ClientActivity> dateColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				DateTimeFormat datefmt = DateTimeFormat.getFormat(Accounter
						.getCompany().getPreferences().getDateFormat());
				String dateformat = datefmt.format(new Date(object.getTime()));
				DateTimeFormat timefmt = DateTimeFormat.getFormat("h:mm a");
				String timeFormat = timefmt.format(new Date(object.getTime()));
				return dateformat + " " + timeFormat;
			}
		};
		// dateColumn.setSortable(false);

		TextColumn<ClientActivity> userNameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {

				return object.getUserName();
			}
		};

		Column<ClientActivity, ClientActivity> activityColumn = getActivityColumn();

		TextColumn<ClientActivity> idColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getId());
			}
		};
		TextColumn<ClientActivity> nameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return object.getName();
			}
		};

		TextColumn<ClientActivity> transactionDateColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getDate() != null ? object
						.getDate() : "");
			}
		};
		TextColumn<ClientActivity> amountColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				if (object.getAmount() != null) {
					return DataUtils.amountAsStringWithCurrency(
							object.getAmount(), object.getCurrency());
				}
				return "";
			}
		};

		Column<ClientActivity, String> auditHistoryColumn = new Column<ClientActivity, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientActivity object) {
				if (object.getObjectID() == 0) {
					return null;
				} else {
					return messages.Get();
				}

			}
		};
		auditHistoryColumn
				.setFieldUpdater(new FieldUpdater<ClientActivity, String>() {

					@Override
					public void update(int index, final ClientActivity object,
							String value) {

					}
				});

		/*
		 * ImageColumn<ClientActivity> imageColumn = new
		 * ImageColumn<ClientActivity>() {
		 * 
		 * @Override public ImageResource getValue(ClientActivity object) { if
		 * (object.getObjectID() == 0) { return null; } else { return
		 * Accounter.getFinanceMenuImages() .accounterRegisterIcon(); } } };
		 * 
		 * imageColumn .setFieldUpdater(new FieldUpdater<ClientActivity,
		 * ImageResource>() {
		 * 
		 * @Override public void update(int index, ClientActivity object,
		 * ImageResource value) {
		 * 
		 * } });
		 */

		Column<ClientActivity, String> imageColumn = new Column<ClientActivity, String>(
				new ClickImage()) {
			@Override
			public String getValue(ClientActivity object) {
				if (object.getObjectID() == 0) {
					return null;
				} else {
					return "->";
				}
			}
		};
		imageColumn.setFieldUpdater(new FieldUpdater<ClientActivity, String>() {
			@Override
			public void update(int index, ClientActivity object, String value) {
				if (object.getObjectID() != 0) {

					ActionFactory.getAuditHistory(object).run();
				}
			}
		});

		this.addColumn(dateColumn, messages.modifiedTime());
		this.addColumn(userNameColumn, messages.userName());
		this.addColumn(activityColumn, messages.activity());
		this.addColumn(nameColumn, messages.name());
		this.addColumn(transactionDateColumn, messages.date());
		this.addColumn(amountColumn, messages.amount());
		this.addColumn(imageColumn, messages.history());

		this.setColumnWidth(dateColumn, "170px");
		this.setColumnWidth(userNameColumn, "160px");
		this.setColumnWidth(activityColumn, "200px");
		this.setColumnWidth(imageColumn, "50px");

	}

	private Column<ClientActivity, ClientActivity> getActivityColumn() {

		List<HasCell<ClientActivity, ?>> cells = new ArrayList<HasCell<ClientActivity, ?>>();
		cells.add(new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return getActivityType(object);
			}
		});
		Column<ClientActivity, SafeHtml> linkColumn = new Column<ClientActivity, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ClientActivity object) {

				final String value = object.getDataType() != null ? object
						.getDataType() : "";
				SafeHtmlBuilder shb = new SafeHtmlBuilder();
				if (value.equalsIgnoreCase(messages.issuePayment())) {
					shb.appendEscaped(value);
				} else {
					shb.appendHtmlConstant("<a href='#'>");
					shb.appendEscaped(value);
					shb.appendHtmlConstant("</a>");
				}
				return shb.toSafeHtml();
			}
		};
		linkColumn
				.setFieldUpdater(new FieldUpdater<ClientActivity, SafeHtml>() {

					@Override
					public void update(int index, ClientActivity object,
							SafeHtml safeHtml) {
						openLinkAction(object);
					}
				});
		cells.add(linkColumn);

		Column<ClientActivity, ClientActivity> column = new Column<ClientActivity, ClientActivity>(
				new CompositeCell<ClientActivity>(cells)) {

			@Override
			public ClientActivity getValue(ClientActivity object) {
				return object;
			}
		};
		return column;

	}

	protected String getActivityType(ClientActivity activity) {
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return messages.loggedIn();
		case 1:
			return messages.loggedOut();
		case 2:
			buffer.append(messages.added());
			buffer.append(" : ");
			if (activity.getObjStatus() == ClientTransaction.STATUS_DRAFT) {
				buffer.append("( " + messages.draft() + " ) ");
			}
			return buffer.toString();
		case 3:
			buffer.append(messages.edited());
			buffer.append(" : ");
			if (activity.getObjStatus() == ClientTransaction.STATUS_DRAFT) {
				buffer.append("( " + messages.draft() + " ) ");
			}
			return buffer.toString();
		case 4:
			buffer.append(messages.deleted());
			buffer.append(" : ");
			if (activity.getObjStatus() == ClientTransaction.STATUS_DRAFT) {
				buffer.append("( " + messages.draft() + " ) ");
			}
			return buffer.toString();
		case 5:
			return messages.updatedPreferences();
		case 7:
			buffer.append(messages.voided());
			buffer.append(" : ");
			return buffer.toString();
		case 8:
			buffer.append(messages.merge());
			buffer.append(":");
			return buffer.toString();
		default:
			break;
		}
		return null;
	}

	public void sortRowData(int columnIndex, boolean isAscending) {
		redraw();
	}

	public ClientFinanceDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(ClientFinanceDate fromDate) {
		this.fromDate = fromDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	private void openLinkAction(ClientActivity object) {
		if (object.getDataType().equals(messages.user())) {
			ClientUserInfo userById = Accounter.getCompany().getUserById(
					object.getObjectID());
			if (userById == null)
				return;
		}
		if (object.getActivityType() == ClientActivity.VOIDED) {
			return;
		}
		if (object.getObjType() == ClientTransaction.TYPE_ISSUE_PAYMENT) {
			return;
		} else {
			ReportsRPC.openTransactionView(object.getObjType(),
					object.getObjectID());
		}

	}

	public void setCustomiseValue(long value) {
		this.value = value;
	}

	public long getCustomiseValue() {
		return value;
	}
}