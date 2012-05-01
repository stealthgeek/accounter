package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientAttendanceManagementItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientEmployee employee;

	private List<ClientAttendanceOrProductionItem> attendanceOrProductionItems = new ArrayList<ClientAttendanceOrProductionItem>();

	private long id;

	private int version;

	private double abscentDays;

	public ClientEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ClientEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "Attendance Management Item";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_MANAGEMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public void setAbscentDays(double abscentDays) {
		this.abscentDays = abscentDays;
	}

	public double getAbscentDays() {
		return abscentDays;
	}

	public List<ClientAttendanceOrProductionItem> getAttendanceOrProductionItems() {
		return attendanceOrProductionItems;
	}

	public void setAttendanceOrProductionItems(
			List<ClientAttendanceOrProductionItem> attendanceOrProductionItems) {
		this.attendanceOrProductionItems = attendanceOrProductionItems;
	}
}