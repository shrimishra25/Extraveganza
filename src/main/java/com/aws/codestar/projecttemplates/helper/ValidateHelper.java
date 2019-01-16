package com.aws.codestar.projecttemplates.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import java.net.URL;
import java.lang.*;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

public class ValidateHelper
{
    
	public String validate(String id) {
    	ArrayList<String> data = new ArrayList<String>();
		String str = "Append here: ";
        File excelFile = new File("event.xlsx");
		boolean emp_present = false;
		// str += HelloWorldController.class.getResource("event.xlsx").getPath();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
        		File file = new File(classLoader.getResource("./EmployeeList.xlsx").getFile());
			FileInputStream fis = new FileInputStream(file);

			// we create an XSSF Workbook object for our XLSX Excel File
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			// we get first sheet
			XSSFSheet sheet = workbook.getSheetAt(0);
        		System.out.println("Sheet : "+ sheet.getFirstRowNum());
			// we iterate on rows
			Iterator<Row> rowIt = sheet.iterator();
			// int cellSize = sheet.getRow(0).getLastCellNum();
			rowIt.next();
			while (rowIt.hasNext() && !emp_present) {
				Row row = rowIt.next();
				// iterate on cells for the current row
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// change cell type to string as default it was taking as general
					cell.setCellType(Cell.CELL_TYPE_STRING);
					// str += cell.toString()+" ";
					if (id.trim().equals(cell.toString().trim()) || emp_present) {
						System.out.println("Data cell" + cell.toString());
						data.add(cell.toString());
						emp_present = true;
					}
				}
				if (!data.isEmpty()) {
					emp_present = true;
					break;
				}
			}
			workbook.close();
			fis.close();
		} catch (FileNotFoundException e) {
			str += "File not found";
		} catch (IOException e) {
			str += "IO Exception caught";
		}

	
	JSONObject empDetail = new JSONObject();
	empDetail.put("empid", data.get(0));
	empDetail.put("empname", data.get(1));
	empDetail.put("careerlevel", data.get(2));
	empDetail.put("duname", data.get(3));
	empDetail.put("worklocation", data.get(4));
	
	JSONObject getValidateResponse = new JSONObject();
    
    getValidateResponse.put("empexists", new Boolean(emp_present)).toString();
    getValidateResponse.put("empdetail", empDetail);
    getValidateResponse.put("statuscode", "200");
    getValidateResponse.put("statusmessage", "OK");
	
	return getValidateResponse.toString();
}
}
