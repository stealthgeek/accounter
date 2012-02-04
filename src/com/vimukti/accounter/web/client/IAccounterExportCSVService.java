package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * for exporting the the list as CSV file
 * 
 * @author Lingarao.R
 * 
 */
public interface IAccounterExportCSVService extends RemoteService {

	String getPayeeListExportCsv(int transactionCategory, boolean isActive);

	String getInvoiceListExportCsv(long fromDate, long toDate,
			int invoicesType, int viewType);

	String getEstimatesExportCsv(int type, int status, long fromDate,
			long toDate);

	String getReceivePaymentsListExportCsv(long fromDate, long toDate,
			int transactionType, int viewType);

	String getCustomerRefundsListExportCsv(long fromDate, long toDate);

	String getBillsAndItemReceiptListExportCsv(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int viewType);

	String getVendorPaymentsListExportCsv(long fromDate, long toDate,
			int viewType);

	String getPaymentsListExportCsv(long fromDate, long toDate, int viewType);

	String getPayeeChecksExportCsv(int type, long fromDate, long toDate,
			int viewType);

	String getFixedAssetListExportCsv(int status);

	String getAccountsExportCsv(int typeOfAccount, boolean isActiveAccount);

	String getJournalEntriesExportCsv(long fromDate, long toDate);

	String getUsersActivityLogExportCsv(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long value);

	String getRecurringsListExportCsv(long fromDate, long toDate);

	String getRemindersListExportCsv();

	String getWarehousesExportCsv();

	String getWarehouseTransfersListExportCsv();

	String getStockAdjustmentsExportCsv();

	String getAllUnitsExportCsv();

	String getItemsExportCsv(boolean isPurchaseType, boolean isSaleType,
			String viewType, int itemType);

	String getTaxItemsListExportCsv(String viewType);

	String getTaxCodesListExportCsv(String selectedValue);

	String getSalesPersonsListExportCsv(String selectedValue);

}
