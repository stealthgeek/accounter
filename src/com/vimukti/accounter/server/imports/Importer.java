package com.vimukti.accounter.server.imports;

import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public interface Importer<T extends IAccounterCore> {

	/**
	 * Returns Output Object that is Generated after Matching
	 * 
	 * Warning: This Method should me called after loading the Data
	 * 
	 * @return
	 */
	public T getData();

	/**
	 * 
	 * Loads the Data(ColumnName,ColumnValue) from File
	 * 
	 * @param data
	 */
	public void loadData(Map<String, String> data);

	/**
	 * Updates the All Fields in the Importer with CSVColumnName
	 * 
	 * @param importMap
	 *            <FieldName,CSVColumnName>
	 */
	public void updateFields(Map<String, String> importMap);

	/**
	 * Returns the List of Fields that importer have
	 * 
	 * @return
	 */
	public List<ImportField> getFields();

}