package com.vimukti.accounter.migration;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;

public class TaxAgencyMigrator implements IMigrator<TAXAgency> {

	@Override
	public JSONObject migrate(TAXAgency obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		// Setting Purchase Liability Account of company
		jsonObject.put("purchaseLiabilityAccount", context.get("Account", obj
				.getPurchaseLiabilityAccount().getID()));
		// Setting Sales Liability Account of Company
		jsonObject.put("salesLiabilityAccount",
				context.get("Account", obj.getSalesLiabilityAccount().getID()));
		// Setting Filed Liability Account of Company
		jsonObject.put("filedLiabilityAccount",
				context.get("Account", obj.getFiledLiabilityAccount().getID()));
		jsonObject.put("taxType", getTaxTypeString(obj.getTaxType()));
		// This is Property not found
		// jsonObject.put("offsetSalesTaxFromPurchaseTax", null);
		jsonObject.put("isInactive", !obj.isActive());
		FinanceDate asDateObject = obj.getLastTAXReturnDate();
		if (asDateObject != null) {
			jsonObject.put("lastFileTaxDate", asDateObject.getAsDateObject()
					.getTime());
		}
		// Setting object PaymentTerm
		PaymentTerms paymentTerm = obj.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObject.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		// RelationShip field
		// identification is not found
		// AutoIdentification , mrOrMs, jobTitle are not found
		jsonObject.put("name", obj.getName());
		jsonObject.put("comments", obj.getMemo());
		jsonObject.put("email", obj.getEmail());
		jsonObject.put("phone", obj.getPhoneNo());
		// MobilePhone and homePhone is not found
		jsonObject.put("fax", obj.getFaxNo());
		JSONObject jsonAddress = new JSONObject();
		for (Address primaryAddress : obj.getAddress()) {
			if (primaryAddress.isSelected()) {
				jsonAddress.put("street", primaryAddress.getStreet());
				jsonAddress.put("city", primaryAddress.getCity());
				jsonAddress.put("stateOrProvince",
						primaryAddress.getStateOrProvinence());
				jsonAddress.put("zipOrPostalCode",
						primaryAddress.getZipOrPostalCode());
				jsonAddress.put("country", primaryAddress.getCountryOrRegion());
			}
		}
		jsonObject.put("address", jsonAddress);
		jsonObject.put("inActive", !obj.isActive());

		// BussinessRelationShip Fields
		jsonObject.put("companyName", obj.getCompany().getTradingName());
		jsonObject.put("payeeSince", obj.getPayeeSince());
		jsonObject.put("webAddress", obj.getWebPageAddress());
		// altEmail and altPhone are not found
		JSONArray jsonContacts = new JSONArray();
		for (Contact contact : obj.getContacts()) {
			JSONObject jsonContact = new JSONObject();
			jsonContact.put("isPrimary", contact.isPrimary());
			jsonContact.put("contactName", contact.getName());
			jsonContact.put("title", contact.getTitle());
			jsonContact.put("businessPhone", contact.getBusinessPhone());
			jsonContact.put("email", contact.getEmail());
			jsonContacts.put(jsonContact);
		}
		jsonObject.put("contacts", jsonContacts);
		// emailPreference is not found
		// printOnCheckAs is not found
		// sendTransactionViaEmail is not found
		// sendTransactionViaPrint is not found
		// sendTransactionViaFax is not found
		jsonObject.put("currency", obj.getCurrency());
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		Account account = obj.getAccount();
		if (account != null) {
			jsonObject.put("account", context.get("Account", account.getID()));
		}
		jsonObject
				.put("since", obj.getPayeeSince().getAsDateObject().getTime());
		jsonObject.put("bankName", obj.getBankName());
		jsonObject.put("bankAccountNumber", obj.getBankAccountNo());
		jsonObject.put("bankBranch", obj.getBankBranch());
		jsonObject.put("serviceTaxRegistrationNo",
				obj.getServiceTaxRegistrationNo());
		TAXCode taxCode = obj.getTAXCode();
		if (taxCode != null) {
			jsonObject.put("taxCode", context.get("TaxCode", taxCode.getID()));
		}
		jsonObject.put("paymentMethod", obj.getPaymentMethod());
		// shipTo and billTo are not found
		jsonObject.put("vATRegistrationNumber", obj.getVATRegistrationNumber());
		TAXItem taxItem = obj.getTAXItem();
		if (taxItem != null) {
			jsonObject.put("taxItem", context.get("TAXItem", taxItem.getID()));
		}
		// modeOfTransport is not found
		jsonObject.put("openingBalance", obj.getOpeningBalance());
		// journalEntry is not found
		jsonObject.put("taxRegistrationNumber", obj.getTINNumber());
		return jsonObject;
	}

	private static String getTaxTypeString(int taxType) {
		if (taxType == ClientTAXAgency.TAX_TYPE_SERVICETAX) {
			return "SalesTax";
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_VAT) {
			return "VAT";
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_SALESTAX) {
			return "ServiceTax";
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_OTHER) {
			return "Other";
		}
		return null;
	}
}
