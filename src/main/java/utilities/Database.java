package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Database {
	private Connection connection;

	public Database(String url, String username, String password) {
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			connection = null;
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to close the connection to the database
	 * 
	 * @return - true, iff the connection was closed without any issues; else, false
	 */
	public boolean close() {
		if (connection != null) {
			try {
				connection.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}

	/**
	 * This method is used to read the data from a DB table
	 * 
	 * @param query  - query to be executed on a table to fetch the data
	 * @param params - additional parameters required to prepare the SQL statement
	 * @return - data stored in the result-set in the form of List of Maps between
	 *         column headers and column values for each row. Each item in the list
	 *         represents a row's data. For example [{COL1NAME=ROW1_COL1_VALUE,
	 *         COL2NAME=ROW1_COL2_VALUE...},{COL1NAME=ROW2_COL1_VALUE,
	 *         COL2NAME=ROW2_COL2_VALUE...}...]
	 */
	public List<Map<String, Object>> read(String query, Object[] params) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (connection != null) {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				if (params != null)
					for (int i = 1; i <= params.length; i++)
						preparedStatement.setObject(i, params[i - 1]);
				ResultSet resultSet = preparedStatement.executeQuery();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				while (resultSet.next()) {
					Map<String, Object> currentRowData = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
						currentRowData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
					data.add(currentRowData);
				}
			} catch (SQLException e) {
				data = null;
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * This method is used to write data to a DB table by executing a DDL or DML
	 * statement.
	 * 
	 * @param query   - query to be executed on the table
	 * @param params- array of parameters required to prepare the DDL or DML
	 *                statement
	 */
	public void write(String query, Object[] params) {
		if (connection != null) {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				if (params != null)
					for (int i = 1; i <= params.length; i++)
						preparedStatement.setObject(i, params[i - 1]);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to write data to a DB table by executing a DDL or DML
	 * statement. The query can be executed multiple times with different sets of
	 * parameters
	 * 
	 * @param query      - query to be executed on the table
	 * @param paramsList - List of parameters. The size of the list will be the
	 *                   number of times the query will be executed. Each item in
	 *                   the list has a set of parameters required to construct the
	 *                   query.
	 */
	public void write(String query, List<Object[]> paramsList) {
		if (connection != null) {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				if (paramsList != null)
					for (Object[] params : paramsList) {
						for (int i = 1; i <= params.length; i++)
							preparedStatement.setObject(i, params[i - 1]);
						preparedStatement.addBatch();
					}
				preparedStatement.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to execute multiple DDL or DML statements with the
	 * capability of each statement getting executed multiple times with different
	 * sets of parameters each time
	 * 
	 * @param queriesAndparamsList - A collection of Queries and List of Parameters
	 *                             for each query to be executed on the DB table
	 */
	public void write(Map<String, List<Object[]>> queriesAndparamsList) {
		for (Map.Entry<String, List<Object[]>> queryAndParamsList : queriesAndparamsList.entrySet())
			write(queryAndParamsList.getKey(), queryAndParamsList.getValue());
	}
}
