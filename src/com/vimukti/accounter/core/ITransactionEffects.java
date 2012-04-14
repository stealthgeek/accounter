package com.vimukti.accounter.core;

public interface ITransactionEffects {

	public void add(Account account, Double amount);

	public void add(Payee payee, Double amount);

	public void add(Item item, Quantity quantity,
			Double unitPriceInBaseCurrency, Warehouse wareHouse);

	public void add(TAXItemGroup taxItemGroup, Double amount);

}
