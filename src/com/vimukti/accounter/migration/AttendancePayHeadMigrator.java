package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AttendancePayHead;

public class AttendancePayHeadMigrator implements IMigrator<AttendancePayHead> {

	@Override
	public JSONObject migrate(AttendancePayHead obj, MigratorContext context)
			throws JSONException {
		JSONObject payHead = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType",
				PicklistUtilMigrator.getPayHeadType(obj.getType()));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		Account expenseAccount = obj.getAccount();
		if (expenseAccount != null) {
			payHead.put("expenseAccount",
					context.get("Account", expenseAccount.getID()));
		}
		payHead.put("calculationType", PicklistUtilMigrator
				.getCalculationType(obj.getCalculationType()));
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			payHead.put("assetAccount",
					context.get("Account", assetAccount.getID()));
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			payHead.put("statutoryLiabilityAccount",
					context.get("Account", liabilityAccount.getID()));
		}
		payHead.put("calculationPeriod", PicklistUtilMigrator
				.getCalculationPeriod(obj.getCalculationPeriod()));
		if (PicklistUtilMigrator.getCalculationType(obj.getCalculationType())
				.equals("OnProduction")) {
			payHead.put("productionType", context.get(
					"AttendanceOrProductionType", obj.getProductionType()
							.getID()));
		} else {
			payHead.put("userDefinedCalendar", context.get(
					"AttendanceOrProductionType", obj.getProductionType()
							.getID()));
		}
		payHead.put("perDayCalculationBasis", PicklistUtilMigrator
				.getPerdayCalculationBasis(obj.getCalculationPeriod()));
		payHead.put("earningDeductionOn",
				PicklistUtilMigrator.getAttendanceType(obj.getAttendanceType()));
		payHead.put("otherPayHead", obj.getPayhead());
		// TODO PayHead.obj has
		// isFromTimeSheet,lastComputedValue,
		// attendanceLeaveWithPay
		return payHead;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}