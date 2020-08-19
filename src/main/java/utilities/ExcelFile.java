package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFile {
	private String filePath;
	private String sheetName;
	private boolean fileExists = false;
	private File file;

	public ExcelFile(String filePath) {
		this(filePath, "Sheet1");
	}

	public ExcelFile(String filePath, String sheetName) {
		this.filePath = filePath;
		this.sheetName = sheetName;
		file = new File(filePath);
		if (file.exists())
			fileExists = true;
	}

	/**
	 * This method is used to store the row and column indexes in a Hashmap
	 * 
	 * @param rowIndex    - zero-based index of the row
	 * @param columnIndex - zero-based index of the column
	 * @return - A Hashmap containing the key-value pairs corresponding to rowIndex
	 *         and columnIndex
	 */
	private static Map<String, Integer> cellAddress(int rowIndex, int columnIndex) {
		Map<String, Integer> cellAddress = new HashMap<String, Integer>();
		cellAddress.put("rowIndex", rowIndex);
		cellAddress.put("columnIndex", columnIndex);
		return cellAddress;
	}

	/**
	 * This method is used to get the data present in a cell
	 * 
	 * @param cell - reference to the desired cell whose value/data is to be
	 *             retrieved
	 * @return - data stored by the cell
	 */
	private static Object getCellData(Cell cell) {
		Object data = null;
		switch (cell.getCellType()) {
		case BOOLEAN:
			data = cell.getBooleanCellValue();
			break;
		case NUMERIC:
			data = cell.getNumericCellValue();
			break;
		case STRING:
			data = cell.getStringCellValue();
			break;
		}
		return data;
	}

	/**
	 * This method is used to write a String data to the given excel cell of the
	 * given workbook
	 * 
	 * @param cellAddress - a Hashmap containing the address of the cell to which
	 *                    the data is to be written
	 * @param data        - String data to be written to the cell
	 * @throws IOException
	 */
	public void write(Map<String, Integer> cellAddress, String data) throws IOException {
		FileOutputStream fos;
		if (!fileExists) {
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet(sheetName);
			Row row = sheet.createRow(cellAddress.get("rowIndex"));
			Cell cell = row.createCell(cellAddress.get("columnIndex"));
			cell.setCellValue(data);
			fos = new FileOutputStream(file);
			workBook.write(fos);
			workBook.close();
		} else {
			FileInputStream fis = new FileInputStream(file);
			Workbook workBook = WorkbookFactory.create(fis);
			fis.close();
			Sheet sheet = workBook.getSheet(sheetName);
			if (sheet == null)
				sheet = workBook.createSheet(sheetName);
			Row row = sheet.getRow(cellAddress.get("rowIndex"));
			if (row == null)
				row = sheet.createRow(cellAddress.get("rowIndex"));
			Cell cell = row.getCell(cellAddress.get("columnIndex"));
			if (cell == null)
				cell = row.createCell(cellAddress.get("columnIndex"));
			cell.setCellValue(data);
			fos = new FileOutputStream(file);
			workBook.write(fos);
			workBook.close();
		}
		fos.close();
	}

	/**
	 * This method is used to write a String data to the given excel cell of the
	 * given workbook
	 * 
	 * @param rowIndex    - zero-based rowIndex of the cell to be written
	 * @param columnIndex - zero-based columnIndex of the cell to be written
	 * @param data        - String data to be written to the cell
	 * @throws IOException
	 */
	public void write(int rowIndex, int columnIndex, String data) throws IOException {
		Map<String, Integer> cellAddress = cellAddress(rowIndex, columnIndex);
		write(cellAddress, data);
	}

	/**
	 * This method is used to read all the data from the specified sheet of the
	 * given workbook. The sheet should contain the column headers in the first row
	 * 
	 * @return - sheet's data in form of an ArrayList. Each item in the list
	 *         represents the data contained in a row which is represented by a
	 *         Hashmap which maps column's header with the column's value
	 * @throws IOException
	 */
	public List<Map<String, Object>> read() throws IOException {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (fileExists) {
			FileInputStream fis = new FileInputStream(file);
			Workbook workBook = WorkbookFactory.create(fis);
			fis.close();
			Sheet sheet = workBook.getSheet(sheetName);
			if (sheet != null) {
				Iterator<Row> rowIterator = sheet.rowIterator();
				Row headerRow = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0) {
						headerRow = row;
						continue;
					} else {
						Map<String, Object> currentRowData = new LinkedHashMap<String, Object>();
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							Cell headerCell = headerRow.getCell(cell.getColumnIndex());
							currentRowData.put((String) getCellData(headerCell), getCellData(cell));
						}
						data.add(currentRowData);
					}
				}
			}
		}
		return data;
	}

	/**
	 * This method is used to read the data of the given row of the specified sheet
	 * of the specified workbook. The sheet should contain the column headers in the
	 * first row
	 * 
	 * @param row - row number whose data is to be fetched
	 * @return - row data in form of a hash map mapping each column's header with
	 *         the column's data for the given row
	 * @throws IOException
	 */
	public Map<String, Object> read(int row) throws IOException {
		List<Map<String, Object>> data = read();
		Map<String, Object> rowData = new LinkedHashMap<String, Object>();
		if (data == null)
			rowData = null;
		else {
			if (row > data.size())
				rowData = null;
			else
				rowData = data.get(row - 1);
		}
		return rowData;
	}

	/**
	 * This method is used to read the data of the given column in the given row of
	 * the specified sheet of the specified workbook. The sheet should contain the
	 * column headers in the first row
	 * 
	 * @param row        - row number from where a column's data is to be fetched
	 * @param columnName - name of the column header whose value is to be fetched
	 *                   from the given row
	 * @return - data fetched from the given column in the specified row
	 * @throws IOException
	 */
	public Object read(int row, String columnName) throws IOException {
		Object data = null;
		Map<String, Object> rowData = read(row);
		if (rowData != null)
			data = rowData.get(columnName);
		return data;
	}
}
