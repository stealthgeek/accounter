package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 *  This is a type of {@link Payee)  It refers to a real time entity, supplier or vendor, in accounting terms, to whom the company has debt to pay. This has a 'openingBalance' and 'balance' fields to note its corresponding opening balances and present balance. And this balance is recorded as per the balanceAsOf date provided while creation.
 * 
 * @author Chandan
 *
 */

public class Vendor extends Payee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3859959561067390029L;

	String vendorNumber;


	/**
	 * This account defaults the Grid Accounts in any Transaction while
	 * selecting this Vendor.
	 */
	Account expenseAccount;

	double creditLimit;

	/**
	 * The way in which the Shipping of the goods has done for this Vendor.
	 */
	ShippingMethod shippingMethod;

	/**
	 * The terms in which we should pay the bills to the Vendor.
	 */
	PaymentTerms paymentTerms;

	/**
	 * This is to categorize this Vendor
	 */
	VendorGroup vendorGroup;

	String federalTaxId;

	// Balance due fields

	double current;
	double overDueOneToThirtyDays;
	double overDueThirtyOneToSixtyDays;
	double overDueSixtyOneToNintyDays;
	double overDueOverNintyDays;
	double overDueTotalBalance;

	// Sales Information

	double monthToDate;
	double yearToDate;
	double lastYear;
	double lifeTimePurchases;

	String taxId;
	boolean isTrackPaymentsFor1099;
	boolean tdsApplicable;

	/**
	 * @return the instanceVersion
	 */
	public Vendor() {
	}


	/**
	 * @return the accountNumber
	 */
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}



	/**
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(Account expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the paymentTerms
	 */
	public PaymentTerms getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(PaymentTerms paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	/**
	 * @return the vendorGroup
	 */
	public VendorGroup getVendorGroup() {
		return vendorGroup;
	}

	public void setVendorGroup(VendorGroup vendorGroup) {
		this.vendorGroup = vendorGroup;
	}

	/**
	 * @return the federalTaxId
	 */
	public String getFederalTaxId() {
		return federalTaxId;
	}

	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}

	/**
	 * @return the overDueOneToThirtyDays
	 */
	public double getOverDueOneToThirtyDays() {
		return overDueOneToThirtyDays;
	}

	/**
	 * @return the overDueThirtyOneToSixtyDays
	 */
	public double getOverDueThirtyOneToSixtyDays() {
		return overDueThirtyOneToSixtyDays;
	}

	/**
	 * @return the overDueSixtyOneToNintyDays
	 */
	public double getOverDueSixtyOneToNintyDays() {
		return overDueSixtyOneToNintyDays;
	}

	/**
	 * @return the overDueOverNintyDays
	 */
	public double getOverDueOverNintyDays() {
		return overDueOverNintyDays;
	}

	/**
	 * @return the overDueTotalBalance
	 */
	public double getOverDueTotalBalance() {
		return overDueTotalBalance;
	}

	/**
	 * @return the monthToDate
	 */
	public double getMonthToDate() {
		return monthToDate;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @return the lastYear
	 */
	public double getLastYear() {
		return lastYear;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public boolean isTrackPaymentsFor1099() {
		return isTrackPaymentsFor1099;
	}

	public void setTrackPaymentsFor1099(boolean isTrackPaymentsFor1099) {
		this.isTrackPaymentsFor1099 = isTrackPaymentsFor1099;
	}

	/**
	 * @return the lifeTimePurchases
	 */
	public double getLifeTimePurchases() {
		return lifeTimePurchases;
	}

	public Contact getPrimaryContact() {

		Contact primaryContact = null;

		if (this.contacts != null) {
			for (Contact contact : contacts) {
				if (contact.isPrimary())
					primaryContact = contact;
			}
		}

		return primaryContact;

	}

	public Set<String> getContactsPhoneList() {

		Set<String> phnos = new HashSet<String>();

		if (this.contacts != null) {

			for (Contact contact : contacts) {

				phnos.add(contact.getBusinessPhone());
			}

		}

		return phnos;

	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.VENDOR);
		ChangeTracker.put(accounterCore);
		return false;
	}



	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		setType(Payee.TYPE_VENDOR);
		if (this.vendorNumber == null || this.vendorNumber.trim().isEmpty()) {
			this.vendorNumber = NumberUtils
					.getNextAutoVendorNumber(getCompany());
		}
		return onUpdate(session);
	}



	@Override
	public Account getAccount() {
		return getCompany().getAccountsPayableAccount();

	}



	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Vendor vendor = (Vendor) clientObject;
		Query query = session.getNamedQuery("getVendor.by.name")
				.setString("name", vendor.name)
				.setEntity("company", vendor.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			Vendor newVendor = (Vendor) list.get(0);
			if (vendor.id != newVendor.id) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A Supplier already exists with this name");
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Vendor Name:" + this.name + "  Balance:" + this.balance;
	}

	public boolean isTdsApplicable() {
		return tdsApplicable;
	}

	public void setTdsApplicable(boolean tdsApplicable) {
		this.tdsApplicable = tdsApplicable;
	}
}
