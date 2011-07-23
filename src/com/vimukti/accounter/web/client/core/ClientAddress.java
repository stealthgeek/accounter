package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientAddress implements IAccounterCore {

	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_BILL_TO = 2;
	public static final int TYPE_SHIP_TO = 3;
	public static final int TYPE_WAREHOUSE = 4;
	public static final int TYPE_LEGAL = 5;
	public static final int TYPE_POSTAL = 6;
	public static final int TYPE_OTHER = 7;
	public static final int TYPE_HOME = 8;
	public static final int TYPE_COMPANY = 9;
	public static final int TYPE_COMPANY_REGISTRATION = 10;

	int type;

	String street = "";
	String city = "";
	String stateOrProvinence = "";
	String zipOrPostalCode = "";
	String countryOrRegion = "";
	boolean isSelected = false;
	String address1 = "";

	/**
	 * @return the id
	 */

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the stateOrProvinence
	 */
	public String getStateOrProvinence() {
		return stateOrProvinence;
	}

	/**
	 * @param stateOrProvinence
	 *            the stateOrProvinence to set
	 */
	public void setStateOrProvinence(String stateOrProvinence) {
		this.stateOrProvinence = stateOrProvinence;
	}

	/**
	 * @return the zipOrPostalCode
	 */
	public String getZipOrPostalCode() {
		return zipOrPostalCode;
	}

	/**
	 * @param zipOrPostalCode
	 *            the zipOrPostalCode to set
	 */
	public void setZipOrPostalCode(String zipOrPostalCode) {
		this.zipOrPostalCode = zipOrPostalCode;
	}

	/**
	 * @return the countryOrRegion
	 */
	public String getCountryOrRegion() {
		return countryOrRegion;
	}

	/**
	 * @param countryOrRegion
	 *            the countryOrRegion to set
	 */
	public void setCountryOrRegion(String countryOrRegion) {
		this.countryOrRegion = countryOrRegion;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public List<String> getAddressTypes() {
		List<String> valueMap = new ArrayList<String>();
		valueMap.add("Bill To");
		valueMap.add("Ship To");
		valueMap.add("1");
		valueMap.add("2");
		valueMap.add("3");
		valueMap.add("4");
		valueMap.add("5");
		valueMap.add("6");
		return valueMap;

	}

	/**
	 * Called From GUI for Address Combo
	 * 
	 * @return
	 */
	public String Stringify() {

		StringBuffer buffer = new StringBuffer();
		switch (type) {
		case ClientAddress.TYPE_BILL_TO:
			// buffer.append("BillTo: ");
			break;

		case ClientAddress.TYPE_BUSINESS:
			buffer.append("Buisness:");
			break;

		case ClientAddress.TYPE_HOME:
			buffer.append("Home: ");
			break;

		case ClientAddress.TYPE_LEGAL:
			buffer.append("Legal: ");
			break;

		case ClientAddress.TYPE_SHIP_TO:
			buffer.append("ShipTo: ");

		default:
			buffer.append("Other: ");
			break;
		}

		buffer.append(address1 != null ? String.valueOf(" " + address1) : "");
		buffer.append(street != null ? String.valueOf(" " + street) : "");
		buffer.append(city != null ? String.valueOf(" " + city) : "");
		buffer.append(stateOrProvinence != null ? String.valueOf(" "
				+ stateOrProvinence) : "");
		buffer.append(zipOrPostalCode != null ? String.valueOf(" "
				+ zipOrPostalCode) : "");
		buffer.append(countryOrRegion != null ? String.valueOf(" "
				+ countryOrRegion) : "");

		return buffer.toString();
	}

	@Override
	public String getName() {

		return Stringify();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ADDRESS;
	}

	@Override
	public long getID(){
		return 0;
//		return this.id;
	}

	@Override
	public void setID(long id){
		// this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {

		return null;
	}
}
