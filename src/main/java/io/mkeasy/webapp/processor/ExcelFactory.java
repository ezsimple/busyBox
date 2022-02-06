package io.mkeasy.webapp.processor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import io.mkeasy.resolver.CommandMap;
import io.mkeasy.utils.MapUtil;
import io.mkeasy.utils.StringUtil;
import io.mkeasy.utils.csv.reader.CsvParser;
import io.mkeasy.utils.csv.reader.CsvReadOption;
import io.mkeasy.utils.csv.reader.CsvReader;
import io.mkeasy.utils.csv.reader.CsvRow;
import io.mkeasy.utils.excel.ExcelRead;
import io.mkeasy.utils.excel.ExcelReadOption;
import io.mkeasy.utils.excel.ExcelWrite;
import io.mkeasy.utils.excel.ExcelWriteOption;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelFactory {
	
    @Autowired
    FileFactory fileFactory;

	@Autowired
	ServletContext context;

	final String[] cols = {
        "A",  "B",  "C",  "D",  "E",  "F",  "G",  "H",  "I",  "J",  "K",  "L",  "M",  "N",  "O",  "P",  "Q",  "R",  "S",  "T",  "U",  "V",  "W",  "X",  "Y",  "Z",
        "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
        "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
        "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
        "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ",
        "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ",
        "FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ",
        "IA", "IB", "IC", "ID", "IE", "IF", "IG", "IH", "II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP", "IQ", "IR", "IS", "IT", "IU", "IV", "IW", "IX", "IY", "IZ"
	};

	public Map<String, String> getHeader(String filePath, int headerRowNo) throws Exception {
		if(filePath == null)
			throw new Exception(filePath +"가 존재하지 않습니다.");

		// ==============================================
		// war 배포의 경우 파일을 가져올 수 없습니다.
		// File file = new File(filePath);
		// InputStream 으로 대체 검토 필요합니다. 
		// ==============================================
		File file = new File(filePath);
		String ext = FilenameUtils.getExtension(filePath);
		File tempFile = File.createTempFile("temp-","."+ext); 
		tempFile.deleteOnExit();

		FileUtils.copyFile(file, tempFile);
		
		if(headerRowNo < 0)
			throw new Exception("헤더의 번호는 0보다 작을 수 없습니다.");

		if(StringUtils.equalsIgnoreCase("xls", ext)
				|| StringUtils.equalsIgnoreCase("xlsx", ext))
            return readExcelHeader(tempFile, headerRowNo);

		if(StringUtils.equalsIgnoreCase("csv", ext))
            return readCsvHeader(tempFile, headerRowNo);
		
		throw new Exception("지원하지 않는 파일 포맷 입니다.");

	}

	// 대개의 경우 첫번째 라인이 header 입니다.
	public Map<String, String> getHeader(String filePath) throws Exception {
		return getHeader(filePath, 1); // 첫번째 라인을 헤더로 인식합니다.
	}

	private Map<String, String> readCsvHeader(File tempFile, int headerRowNo) throws Exception {

		CsvReadOption co = new CsvReadOption();
		co.setOutputColumns(cols);

		CsvReader csvReader = new CsvReader();
		csvReader.setContainsHeader(false);

		List<String > cols = co.getOutputColumns();
        Map<String, String> header = new HashMap<String, String>();
        if(headerRowNo<=0) return header;

		try (CsvParser csvParser = csvReader.parse(tempFile, StandardCharsets.UTF_8)) {
			int rowCount = 0;
		    CsvRow row;
		    while ((row = csvParser.nextRow()) != null) {
		    	if(rowCount++ > 0) break;
                int fieldCount = row.getFieldCount();
		    	for(int i = 0; i <fieldCount; i++) {
		    		String value = row.getField(i)==null?"":row.getField(i);
                    header.put(cols.get(i), value);
		    	}
		    }
		}
		return header;
	}

	private Map<String, String> readExcelHeader(File tempFile, int headerRowNo) throws Exception {
		ExcelReadOption ro = new ExcelReadOption();
		ro.setFilePath(tempFile.getAbsolutePath());
		ro.setOutputColumns(cols);
		ro.setStartRow(headerRowNo); // 읽어들일 헤더의 라인번호
		List<Map<String, String>> result = ExcelRead.readHeader(ro);
		FileUtils.deleteQuietly(tempFile);
		return result!=null?result.get(0):MapUtil.EMPTY;
	}

	// 대게의 경우 첫번째 라인이 헤더 입니다.
	private Map<String, String> readExcelHeader(File tempFile) throws Exception {
		return readExcelHeader(tempFile, 1);// 읽어들일 헤더의 라인번호
	}

	public List<Map<String, String>> upload(String filePath, ModelMap model, CommandMap commandMap, int readRowNo) throws Exception {

		if(filePath == null)
			throw new Exception(filePath +"가 존재하지 않습니다.");

		File file = new File(filePath);
		String ext = FilenameUtils.getExtension(file.getName());
		File tempFile = File.createTempFile("temp-","."+ext); 
		tempFile.deleteOnExit();

		FileUtils.copyFile(file, tempFile);
		
		// HIDDEN PARAMETER : 
		// 대개의 경우 첫번째줄은 Header이고, 2번째 라인부터는 내용이므로 
		// 특수한 경우에만 사용하도록 한다.
		// 아직 CSV의 경우는 미구현 상태입니다.
		if(readRowNo < 1)
			throw new Exception("readRowNo는 1보다 작을 수 없습니다.");
		// 헤더가 있는 경우 readRowNo 값은 2입니다.

//		String _readRowNo = commandMap.getParam("_readRowNo");
//		int readRowNo = 2;
//		if(!StringUtils.isEmpty(_readRowNo) && NumberUtils.isDigits(_readRowNo))
//			readRowNo = Integer.parseInt(_readRowNo);
		
		if(StringUtils.equalsIgnoreCase("xls", ext)
				|| StringUtils.equalsIgnoreCase("xlsx", ext))
            return readExcel(tempFile, readRowNo);

		if(StringUtils.equalsIgnoreCase("csv", ext))
            return readCsv(tempFile, readRowNo);
		
		throw new Exception("지원하지 않는 파일 포맷 입니다.");
	}

	private List<Map<String, String>> readCsv(File tempFile, int readRowNo) throws Exception {
		CsvReadOption co = new CsvReadOption();
		co.setOutputColumns(cols);

		CsvReader csvReader = new CsvReader();
		if(readRowNo==2) csvReader.setContainsHeader(true);
		List<String> columns = co.getOutputColumns();
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try (CsvParser csvParser = csvReader.parse(tempFile, StandardCharsets.UTF_8)) {
		    CsvRow row;
		    while ((row = csvParser.nextRow()) != null) {
                Map<String, String> map = new HashMap<String, String>();
                int fieldCount = row.getFieldCount();
		    	for(int i = 0; i <fieldCount; i++) {
		    		String value = row.getField(i)==null?"":row.getField(i);
                    map.put(columns.get(i), value);
		    	}
                result.add(map);
		    }
		}
		return result;
	}

	private List<Map<String, String>> readExcel(File tempFile, int readRowNo) throws Exception {
		ExcelReadOption ro = new ExcelReadOption();
		ro.setFilePath(tempFile.getAbsolutePath());
		ro.setOutputColumns(cols);

		if(readRowNo < 1)
			throw new Exception("읽을 라인 번호는 1보다 작을 수 없습니다.");

		ro.setStartRow(readRowNo); // 읽기 시작할 라인번호 
		List<Map<String, String>> result = ExcelRead.read(ro);
		FileUtils.deleteQuietly(tempFile);
		return result;
	}

	private List<Map<String, String>> readExcel(File tempFile) throws Exception {
		return readExcel(tempFile, 2); // skip first rows (skip titles)
	}

	public void download(HttpServletRequest request, HttpServletResponse response
			,String dnFileName // 다운로드 엑셀파일명
			,List<String> headerNames // 엑셀파일 컬럼명
			,List<String> fieldNames  // 쿼리결과 필드명
			,List<Map<String, Object>> result) throws Exception {

    	String ext = FilenameUtils.getExtension(dnFileName);
    	if(!(StringUtils.equals(ext, "xls") 
    			|| StringUtils.equals(ext, "xlsx"))) {
    		log.error("{}", "download filename is not Excel");
    		return;
    	}
    	
    	File tempExcel = File.createTempFile("temp-", "."+ext); 
    	tempExcel.deleteOnExit();

    	String path = tempExcel.getAbsolutePath();
		String fileName = tempExcel.getName();
		String filePath = path+fileName;
		String sheetName = "Sheet1";

		File xlsFile = new File(filePath);
		if(xlsFile.exists())
			FileUtils.forceDelete(new File(filePath));

		ExcelWriteOption wo = new ExcelWriteOption();

		wo.setFileName(fileName);
		wo.setFilePath(path);
		wo.setSheetName(sheetName);
		wo.setTitles(headerNames);
		
		List<String[]> excelRows = new ArrayList<String[]>();
		for(Map<String, Object> item : result) {
            List<String> tmpRow = new ArrayList<String>();
			for(int i = 0; i<fieldNames.size(); i++) {
				String field = fieldNames.get(i);
                String value = StringUtil.trimToEmpty(item.get(field));
                tmpRow.add(i, value);
			}
			String[] row = tmpRow.toArray(new String[tmpRow.size()]);
			excelRows.add(row);
		}
		wo.setContents(excelRows);
		
		File excelFile = ExcelWrite.write(wo);	
		
		fileFactory.download(request, response, excelFile, dnFileName);

		if(xlsFile.exists())
			FileUtils.forceDelete(new File(filePath));
	}
	
}
