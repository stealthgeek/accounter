package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * For every Receive payment entry there will be one or more
 * TransactionReceivePayment entries. These are populated whenever a
 * {@link Invoice} or {@link CustomerRefund} or a {@link JournalEntry} for this
 * customer is made. We can apply discount, writeoff and credits(if any created
 * before) for each TransactionReceivePayment.
 * 
 * @author Chandan
 * 
 */
public class TransactionReceivePayment implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5905968105405594353L;

	long id;

	public long id;

	int version;

	/**
	 * This FinanceDate is given when creating the invoice or customer refund. This is
	 * displayed in the grid of Receive payment which is uneditable there.
	 */
	FinanceDate dueDate;

	/**
	 * The reference of the Invoice which is created to pay in the Receive
	 * Payment
	 */
	@ReffereredObject
	Invoice invoice;

	/**
	 * The amount by which the invoice or customer refund or journal entry is
	 * made.
	 */
	double invoiceAmount = 0D;

	/**
	 * If any discount is applied to invoice or customer refund, the date at
	 * which the discount is applied is stored.
	 */
	FinanceDate discountDate;

	/**
	 * The account from where the discount is applied
	 */
	@ReffereredObject
	Account discountAccount;

	/**
	 * The amount of discount which is applied
	 */
	double cashDiscount = 0D;

	/**
	 * The account from which write off amount is deducted.
	 */
	@ReffereredObject
	Account writeOffAccount;

	/**
	 * The amount that which has to be write off.
	 */
	double writeOff = 0D;

	/**
	 * We can apply any credits for any of the transaction receive payment if
	 * exists.
	 */
	double appliedCredits = 0D;

	/**
	 * The payment is the total amount for this particular transaction receive
	 * payment
	 */
	double payment = 0D;

	/**
	 * The reference of the receive payment where this transaction receive
	 * payment exists.
	 */
	@ReffereredObject
	ReceivePayment receivePayment;

	/**
	 * The List of the {@link TransactionCreditsAndPayments} is maintained for
	 * every TransactionReceivePayment. This is used because we can apply
	 * credits through TransactionCreditsAndPayments for a single
	 * TransactionReceivePayment
	 */
	@ReffereredObject
	List<TransactionCreditsAndPayments> transactionCreditsAndPayments;

	/**
	 * Variable to indicate whether this is a voided transaction or not
	 */
	boolean isVoid = false;

	/**
	 * The reference of the Customer Refund which is created to pay in the
	 * Receive Payment
	 */
	@ReffereredObject
	CustomerRefund customerRefund;

	/**
	 * This transient variable is used to differ between Invoice and Customer
	 * Refund
	 */
	public transient boolean isInvoice;

	/**
	 * The reference of the {@link JournalEntry} that was made to pay in the
	 * Receive Payment
	 */
	@ReffereredObject
	JournalEntry journalEntry;

	transient boolean isImported;

	String number;

	private boolean isOnSaveProccessed;

	public TransactionReceivePayment() {

	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @return the invoiceAmount
	 */
	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * @return the discountDate
	 */
	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	public Account getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	public Account getWriteOffAccount() {
		return writeOffAccount;
	}

	/**
	 * @return the writeOff
	 */
	public double getWriteOff() {
		return writeOff;
	}

	/**
	 * @return the appliedCredits
	 */

	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */

	public void setAppliedCredits(double appliedCredits) {
		this.appliedCredits = appliedCredits;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @return the transaction
	 */
	public ReceivePayment getReceivePayment() {
		return receivePayment;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setReceivePayment(ReceivePayment receivePayment) {
		this.receivePayment = receivePayment;
	}

	public List<TransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	/**
	 * Sets the transactionCreditsAndPayments passed as argument to the
	 * reference transactionCreditsAndPayments. At the same update the
	 * TransactionReceivePayments that are involved with these
	 * transactionCreditsAndPayments
	 * 
	 */
	public void setTransactionCreditsAndPayments(
			List<TransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;

		if (transactionCreditsAndPayments == null)
			return;

		for (TransactionCreditsAndPayments transactionCreditsAndPayment : transactionCreditsAndPayments) {

			transactionCreditsAndPayment.setTransactionReceivePayment(this);
		}
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the customerRefund
	 */
	public CustomerRefund getCustomerRefund() {
		return customerRefund;
	}

	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.stringID = this.stringID == null || this.stringID != null
    && this.stringID.isEmpty() ? SecureUtils.createID()
    : this.stringID;

		if (this.discountAccount != null && DecimalUtil.isGreaterThan(this.cashDiscount,0D)) {

			// update the corresponding payee with discount amount
			this.receivePayment.getCustomer().updateBalance(session,
					this.receivePayment, this.cashDiscount);

			// updating the corresponding discount account current balance with
			// discount amount
			this.discountAccount.updateCurrentBalance(this.receivePayment, -1
					* this.cashDiscount);
			this.discountAccount.onUpdate(session);
		}

		if (this.writeOffAccount != null && DecimalUtil.isGreaterThan(this.writeOff, 0D)) {

			// update the corresponding payee with write off amount
			this.receivePayment.getCustomer().updateBalance(session,
					this.receivePayment, this.writeOff);

			// updating the corresponding write off account current balance with
			// write off amount
			this.writeOffAccount.updateCurrentBalance(this.receivePayment, -1
					* this.writeOff);
			this.writeOffAccount.onUpdate(session);
		}

		// To make the Invoice as Un Void or Un Editable
		if ((this.discountAccount != null && DecimalUtil.isGreaterThan(this.cashDiscount,0.0))
				|| this.writeOffAccount != null && DecimalUtil.isGreaterThan(this.writeOff ,0.0)){
			if (this.invoice != null) {
				this.invoice.setCanVoidOrEdit(Boolean.FALSE);
			} else if (customerRefund != null) {
				this.customerRefund.setCanVoidOrEdit(Boolean.FALSE);
			}

		}

		double amount = (this.cashDiscount) + (this.writeOff)
				+ (this.appliedCredits) + (this.payment);

		if (this.invoice != null) {
			// Update the Payments and the balance due of the corresponding
			// Invoice
			this.invoice.setPayments(this.invoice.getPayments() + amount);
			this.invoice.setBalanceDue(this.invoice.getBalanceDue() - amount);

			// To Decide whether this Invoice is Not paid, Partially paid or
			// Paid

			if (DecimalUtil.isGreaterThan(this.invoice.getBalanceDue(),0D)
					&& DecimalUtil.isLessThan(this.invoice.getBalanceDue(),this.invoice.getTotal())) {
				this.invoice.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
			} else if (DecimalUtil.isEquals(this.invoice.getBalanceDue(),0D)) {
				this.invoice.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		} else if (this.customerRefund != null) {
			// Update the Payments and the balance due of the corresponding
			// customer refund
			this.customerRefund.setPayments(this.customerRefund.getPayments()
					+ amount);
			this.customerRefund.setBalanceDue(this.customerRefund
					.getBalanceDue()
					- amount);

		} else if (this.journalEntry != null) {
			// Update the Payments and the balance due of the corresponding
			// customer's Journal Entry
			this.journalEntry.setBalanceDue(this.journalEntry.getBalanceDue()
					- amount);
		}
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (this.getIsVoid()) {

			// session
			// .createQuery(
			// "delete from TaxRateCalculation where transaction =:transaction")
			// .setEntity("transaction", this.receivePayment)
			// .executeUpdate();
			// this.receivePayment.taxRateCalculationEntriesList.clear();

			if (this.getDiscountAccount() != null
					&& DecimalUtil.isGreaterThan(this.getCashDiscount(),0.0)) {

				Account account = Company.getCompany()
						.getAccountsReceivableAccount();
				account.updateCurrentBalance(this.getReceivePayment(), -this
						.getCashDiscount());
				session.saveOrUpdate(account);
				this.getDiscountAccount().updateCurrentBalance(
						this.getReceivePayment(), -this.getCashDiscount());

				session.saveOrUpdate(this.getDiscountAccount());

				this.getReceivePayment().getCustomer().updateBalance(session,
						this.getReceivePayment(), -this.getCashDiscount());

			}
			if (this.getWriteOffAccount() != null && DecimalUtil.isGreaterThan(this.getWriteOff(),0.0)) {

				Account account = Company.getCompany()
						.getAccountsReceivableAccount();
				account.updateCurrentBalance(this.getReceivePayment(), -this
						.getWriteOff());
				session.saveOrUpdate(account);
				this.getWriteOffAccount().updateCurrentBalance(
						this.getReceivePayment(), -this.getWriteOff());

				session.saveOrUpdate(getWriteOffAccount());
				this.getReceivePayment().getCustomer().updateBalance(session,
						this.getReceivePayment(), -this.getWriteOff());

			}
			session.saveOrUpdate(this.getReceivePayment().getCustomer());

			double amount = (getCashDiscount()) + (getWriteOff())
					+ (getAppliedCredits()) + (getPayment());

			if (DecimalUtil.isGreaterThan(this.getAppliedCredits(), 0.0)) {

				for (TransactionCreditsAndPayments tcp : this
						.getTransactionCreditsAndPayments()) {

					tcp.onEditTransaction(-tcp.amountToUse);
					tcp.amountToUse = 0.0;
					session.saveOrUpdate(tcp);
				}
				this.appliedCredits = 0.0;
			}
			this.cashDiscount = 0.0;
			this.writeOff = 0.0;
			this.payment = 0.0;

			if (this.getInvoice() != null) {
				this.getInvoice().updatePaymentsAndBalanceDue(amount);
				session.saveOrUpdate(this.getInvoice());
				this.invoice = null;
			} else if (this.getCustomerRefund() != null) {
				this.getCustomerRefund().updatePaymentsAndBalanceDue(amount);
				session.saveOrUpdate(this.getCustomerRefund());
				this.customerRefund = null;

			} else if (this.getJournalEntry() != null) {
				this.getJournalEntry().setBalanceDue(
						this.getJournalEntry().getBalanceDue() + amount);
				session.saveOrUpdate(this.getJournalEntry());
				this.journalEntry = null;

			}
			session.saveOrUpdate(this);

		}
		// ChangeTracker.put(this);
		return false;
	}

	public void onVoidTransaction(Session session) {
		this.receivePayment.updateUnUsedAmount(session, this.payment);

		session.saveOrUpdate(this.receivePayment);
		for (TransactionCreditsAndPayments tcp : this
				.getTransactionCreditsAndPayments()) {
			tcp.onEditTransaction(-tcp.amountToUse);
			tcp.amountToUse = 0.0;
			session.saveOrUpdate(tcp);
		}
		this.setPayment(0.0);
		this.setAppliedCredits(0.0);
	}

	public void updateAppliedCredits(double amount) {

		this.appliedCredits -= amount;
		if (this.invoice != null) {
			this.invoice.updatePaymentsAndBalanceDue(amount);
		} else if (this.customerRefund != null) {
			this.customerRefund.updatePaymentsAndBalanceDue(amount);
		} else if (this.journalEntry != null) {
			this.journalEntry.updatePaymentsAndBalanceDue(amount);
		}
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void setCustomerRefund(CustomerRefund customerRefunds) {
		this.customerRefund = customerRefunds;
	}

	public void setWriteOffAccount(Account writeOffAccount) {
		this.writeOffAccount = writeOffAccount;
	}

	public void setWriteOff(double writeOff) {
		this.writeOff = writeOff;
	}

	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	public void setDiscountAccount(Account discountAccount) {

		this.discountAccount = discountAccount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;
		if(this.transactionCreditsAndPayments!=null){
		for (TransactionCreditsAndPayments ti : this.transactionCreditsAndPayments) {
			ti.setImported(true);
		}
		}
	}

	// @Override
	public boolean equals(TransactionReceivePayment tr) {
		// TODO Auto-generated method stub
		ReceivePayment rp = tr.getReceivePayment();
		if (this.receivePayment.id == rp.id
				&& this.appliedCredits == tr.appliedCredits
				&& this.cashDiscount == tr.cashDiscount
				&& this.journalEntry == tr.journalEntry
				&& this.payment == tr.payment
				&& this.writeOff == tr.writeOff
				&& this.transactionCreditsAndPayments.size() == tr.transactionCreditsAndPayments
						.size()) {
			for (int i = 0; i < this.transactionCreditsAndPayments.size(); i++) {
				if (!this.transactionCreditsAndPayments.get(i).equals(
						tr.transactionCreditsAndPayments.get(i)))
					return false;
			}
			return true;
		}
		return false;

	}

	public double updatePayments(Double amtdue) {
		if (DecimalUtil.isGreaterThan(amtdue, 0)) {
			if (DecimalUtil.isGreaterThan(this.getPayment(),amtdue)) {
				updateCredits(this.getPayment() - amtdue, 0.0);
				this.setPayment(amtdue);
				amtdue = 0.0;
			} else {
				amtdue -= this.getPayment();
				if (DecimalUtil.isGreaterThan(this.appliedCredits,amtdue)) {
					updateCredits(this.appliedCredits - amtdue, amtdue);
					this.appliedCredits = amtdue;
				}
			}
		} else {
			updateCredits(this.getPayment(), 0.0);
			this.setPayment(0.0);
		}
		return amtdue;
	}

	private void updateCredits(double creditAmount, double appliedCredits) {
		if (this.transactionCreditsAndPayments != null) {
			for (TransactionCreditsAndPayments transactionCreditsAndPayment : this.transactionCreditsAndPayments) {
				double amtToUse = transactionCreditsAndPayment.amountToUse;
				transactionCreditsAndPayment.onEditTransaction(-amtToUse);
				transactionCreditsAndPayment.amountToUse = appliedCredits;
			}
			this.appliedCredits = appliedCredits;
		}
		if (this.receivePayment.getCreditsAndPayments() != null) {
			this.receivePayment.getCreditsAndPayments().updateBalance(
					this.receivePayment, -creditAmount);

		} else {
			this.createCreditsAndPayments(creditAmount);
		}
	}

	private void createCreditsAndPayments(double creditAmount) {
		CreditsAndPayments creditsAndPayments = new CreditsAndPayments();
		creditsAndPayments.setCreditAmount(this.receivePayment.getTotal());
		creditsAndPayments.setBalance(creditAmount);
		this.receivePayment.subTotal = receivePayment.total - creditAmount;
		creditsAndPayments.setMemo(this.receivePayment.getNumber()
				+ "Receive Payment");
		creditsAndPayments.setTransaction(this.receivePayment);
		creditsAndPayments.payee = this.receivePayment.getCustomer();
		this.receivePayment.setCreditsAndPayments(creditsAndPayments);

		HibernateUtil.getCurrentSession().save(creditsAndPayments);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}
}
