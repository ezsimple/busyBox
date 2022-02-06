package io.mkeasy.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CellRef {

	public static String getName(Cell cell, int cellIndex) {
		int cellNum = 0;
		if(cell != null) {
			cellNum = cell.getColumnIndex();
		}
		else {
			cellNum = cellIndex;
		}

		return CellReference.convertNumToColString(cellNum);
	}
	
	@SuppressWarnings("deprecation")
	public static String getValue(Cell cell) throws Exception {
		String value = "";
		if(cell==null) return value;

		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			value = String.valueOf(cell);
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue()+"";
			break;
		case Cell.CELL_TYPE_ERROR:
			value = cell.getErrorCellValue()+"";
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			value = cell.getStringCellValue();
		}

		return value;
	}

}
