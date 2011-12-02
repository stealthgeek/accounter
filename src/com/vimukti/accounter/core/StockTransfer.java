package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Transfers items from one warehouse to another.
 * 
 * @author Srikanth J
 * 
 */
public class StockTransfer extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711393907542349978L;

	private Warehouse fromWarehouse;
	private Warehouse toWarehouse;

	private Set<StockTransferItem> stockTransferItems;

	private String memo;

	public StockTransfer() {
		super();
	}

	public Warehouse getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(Warehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public Warehouse getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(Warehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public void setStockTransferItems(Set<StockTransferItem> stockTransferItems) {
		this.stockTransferItems = stockTransferItems;
	}

	public Set<StockTransferItem> getStockTransferItems() {
		return stockTransferItems;
	}

	public long getId() {
		return id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		for (StockTransferItem item : stockTransferItems) {
			ItemStatus fromitemStatus = fromWarehouse.getItemStatus(item
					.getItem());
			double value = (item.getQuantity().getValue() * item.getQuantity()
					.getUnit().getFactor())
					/ fromitemStatus.getQuantity().getUnit().getFactor();
			fromWarehouse.updateItemStatus(item.getItem(), value, true);

			toWarehouse.updateItemStatus(item.getItem(), value, false);
			item.setCompany(getCompany());
		}
		session.saveOrUpdate(fromWarehouse);
		session.saveOrUpdate(toWarehouse);
		ChangeTracker.put(fromWarehouse);
		ChangeTracker.put(toWarehouse);
		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		for (StockTransferItem item : stockTransferItems) {
			ItemStatus fromitemStatus = fromWarehouse.getItemStatus(item
					.getItem());
			double value = (item.getQuantity().getValue() * item.getQuantity()
					.getUnit().getFactor())
					/ fromitemStatus.getQuantity().getUnit().getFactor();
			fromWarehouse.updateItemStatus(item.getItem(), value, false);

			toWarehouse.updateItemStatus(item.getItem(), value, true);
		}
		session.saveOrUpdate(fromWarehouse);
		session.saveOrUpdate(toWarehouse);
		ChangeTracker.put(fromWarehouse);
		ChangeTracker.put(toWarehouse);
		return super.onDelete(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.STOCK_TRANSFER;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
