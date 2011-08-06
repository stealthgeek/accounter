package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientPayVAT extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayFrom
	 */
	long payFrom;

	/**
	 * Bills Due On or Before.
	 */
	long returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	long vatAgency;

	double endingBalance;

	boolean isEdited = false;

	List<ClientTransactionPayVAT> transactionPayVAT;

	@Override
	public String getClientClassSimpleName() {
		// its not using any where
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAYVAT;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	/**
	 * @return the payFrom
	 */
	public long getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public long getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(long returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
	}

	/**
	 * @return the vatAgency
	 */
	public long getVatAgency() {
		return vatAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setVatAgency(long vatAgency) {
		this.vatAgency = vatAgency;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @param endingBalance
	 *            the endingBalance to set
	 */
	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the isEdited
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @return the clientTransactionPayVAT
	 */
	public List<ClientTransactionPayVAT> getClientTransactionPayVAT() {
		return transactionPayVAT;
	}

	/**
	 * @param clientTransactionPayVAT
	 *            the clientTransactionPayVAT to set
	 */
	public void setClientTransactionPayVAT(
			List<ClientTransactionPayVAT> clientTransactionPayVAT) {
		this.transactionPayVAT = clientTransactionPayVAT;
	}

	public ClientPayVAT clone() {
		ClientPayVAT clientPayVATClone = (ClientPayVAT) this.clone();
		List<ClientTransactionPayVAT> transactionPayVATList = new ArrayList<ClientTransactionPayVAT>();
		for (ClientTransactionPayVAT clientTransactionPayVAT : this.transactionPayVAT) {
			transactionPayVATList.add(clientTransactionPayVAT.clone());
		}
		clientPayVATClone.transactionPayVAT = transactionPayVATList;

		return clientPayVATClone;
	}

}
