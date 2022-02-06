package io.mkeasy.webapp.utils;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Function {
	static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static String formatDate(Date date, String pattern) {
		if(date==null) return "";
		SimpleDateFormat DateFormater = new SimpleDateFormat(pattern);

		return DateFormater.format(date);
	}
	public static String formatDate(String pattern) {
		return formatDate(new Date(), pattern);
	}
	public static Date now() {
		return (new Date());
	}
	public static long double2long(double val) {
		
		return (new Double(val)).longValue();
	}
	public static long replaceData(double val) {
		
		return (new Double(val)).longValue();
	}
//	public static String escapeXml(String val) {
//		return org.apache.taglibs.standard.functions.Functions.escapeXml(val).replaceAll("\n", "<br>");
//	}
	public static String concat(String val1, String val2) {
		return val1+val2;
	}
	
	public static void mkDir(String path) {
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
	}
	public static String security(String val, String type){
		type=type.toLowerCase();
		
		if("email".equals(type)){
			int at=val.indexOf("@");
			val = StringUtils.substring(val,0 ,at-3) + "***" + StringUtils.substring(val,at,val.length());
		}else if("name".equals(type)){
			int l = val.length()==2 ? 1 : 2;
			val = StringUtils.rightPad(val.substring(0,l), val.length(), "*");
		}else if("tel".equals(type)){
			String[] tels = val.split("-");
			if(tels.length==3){
				val = tels[0] + "-" + StringUtils.substring(tels[1],0, tels[1].length()-2) + "**-*" +StringUtils.substring(tels[2],1);
			}else {
				val = StringUtils.substring(val,0, 5) + "***" +StringUtils.substring(val,8,val.length());
			}
		}else if("com_num".equals(type)){
			val = StringUtils.substring(val,0, val.length()-3) + "***";
		}else if("biz_num".equals(type)){
			val = StringUtils.substring(val,0, 3) + "-" + StringUtils.substring(val,3, 5) + "-" + StringUtils.substring(val,5, val.length());
		}else{
			val = Function.masking(val, type);
		}
		return val;
	}
	/**
	 * 보안을 위해 문자열을 마킹한다.
	 * @param val
	 * @param format
	 * @return
	 */
	public static String masking(String val, String format) {
		int p = 0;
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<format.length(); i++){
			char f = format.charAt(i);
			
			if(i == val.length()){
				break;
			}
			
			char c = val.charAt(i);

			if(f=='_'){
				sb.append(c);
			}else{
				sb.append(f);
			}
		}
		return sb.toString();
	}
	/**
	 * 숫자로된 문자열을 전화번호형식이나 우편번호등의 포맷으로 변환
	 * ___-___
	 * @param val
	 * @param format
	 * @return
	 */
	public static String format(String val, String format) {
		int p = 0;
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<val.length(); i++){
			char c = val.charAt(i);
			
			if(p > format.length()){
				sb.append(c);
				continue;
			}
			
			char f = format.charAt(p);
			p++;
			
			if(f=='_'){
				sb.append(c);
			}else{
				sb.append(f);
				sb.append(c);
				p++;
			}
		}
		return sb.toString();
	}

	public static File[] getFiles(String root, String path) {
		if (!("".equals(path))) {
			if (path.startsWith("."))
				path = "";
			else if (!(path.startsWith("/")))
				path = "/" + StringUtils.substringBeforeLast(path, "/");
		}
		File f = new File(root + path);
		if (!(f.isDirectory()))
			f = f.getParentFile();
		return f.listFiles();
	}
	
	public static void main(String[] args) {
		System.out.println(		format("12121232", "___@___.___"));
	}
	
	public static Map str2jsonObj(String str) {
		if(!str.startsWith("{")){
			str = "{" + str + "}";
		}
		return JSONObject.fromObject(str);
	}
	public static boolean isType(Object obj, String type) {
		if(obj==null){
			return false;
		}
		
		List<Class> list = ClassUtils.getAllInterfaces(obj.getClass());
		
		for(Class cls : list){
			if(cls.getSimpleName().equalsIgnoreCase(type)){
				return true;
			}
		}
		
		list = ClassUtils.getAllSuperclasses(obj.getClass());
		
		for(Class cls : list){
			if(cls.getSimpleName().equalsIgnoreCase(type)){
				return true;
			}
		}
		return obj.getClass().getSimpleName().equalsIgnoreCase(type);
	}
	public static String replaceValue(String str, Map map) throws Exception {
		String rtn = str;
		String[] fields = StringUtils.substringsBetween(str, "${", "}");
		
		for(String field : fields){
			Object obj = map.get(field.trim());
			if(obj==null){
				throw new Exception(str + "의 ${" + field + "}가 정의되지 않은 값을 참조할려고 합니다.");
			}
			rtn = StringUtils.replace(rtn, "${" + field + "}", obj.toString());
		}
		
		return rtn;
	}
	public static Object list2Tree(List<Map<String, Object>> list, String upperFld, String codeFld, String labelFld, String idFld, String rootId)  throws Exception {
		List2Tree l2t = new  List2Tree(list, upperFld, codeFld, labelFld, idFld, rootId);
		return l2t.getTree();
	}
	
	private static String list2chartIY(List<Map<String, Object>> list, String yFld, String labelFld){

		JSONObject result = new JSONObject();
		String type = "";
		JSONArray jaList = new JSONArray();
		
		for(int i=0; i<list.size(); i++){
			Map<String, Object> row = list.get(i);
			
			JSONObject item = new JSONObject();

			item.put("data", "[[" + i + "," + row.get(yFld) + "]]");
			item.put("label", row.get(labelFld));
			jaList.add(item);
		}
		
		result.put("data", jaList);
		result.put("type", type);
		return result.toString();
	}
	
	private static String list2chartXY(List<Map<String, Object>> list, String xFld, String yFld){
		long dum = 3600000*9;//그래프의 x축선이 9시에 그려지는 문제로 9시간을 더해 줌
		String type = "";
		
		JSONObject result = new JSONObject();
		JSONArray jaList = new JSONArray();
		JSONObject item = new JSONObject();
		Object x = null;
		JSONArray data = new JSONArray();
		
		for(int i=0; i<list.size(); i++){
			Map<String, Object> row = list.get(i);

			x = row.get(xFld);
			
			if (x instanceof Timestamp) {
				type = "time";
				Timestamp t = (Timestamp) x;
				x = t.getTime() + dum;
			}else if(x instanceof Date){
				type = "time";
				Date t = (Date) x;
				x = t.getTime() + dum;
			}
			data.add("[" + x + "," + row.get(yFld) + "]");
		}
		
		item.put("data", data);
		jaList.add(item);
		
		result.put("data", jaList);
		result.put("type", type);
		return result.toString();
	}
	private static String list2chartIXYZ(List<Map<String, Object>> list, String xFld, String yFld, String zFld, String labelFld){
		boolean hasZ = StringUtils.isNotEmpty(zFld);
		long dum = 0;//360000*115;//그래프의 x축선이 9시에 그려지는 문제로 9시간을 더해 줌
		String type = "";
		Map<String, Map<String, Object>> itemMap = new HashMap<String, Map<String,Object>>();
		int itemCount = 0;
		
		JSONObject result = new JSONObject();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();

		for(int i=0; i<list.size(); i++){
			Map<String, Object> row = list.get(i);
			String label = (String)row.get(labelFld);
			
			Map<String, Object> item = itemMap.get(label);
			
			if(item==null){
				item = new HashMap<String, Object>();
				item.put("data",new ArrayList<Object>());
				item.put("label",label);
				item.put("idx",itemCount);
				
				dataList.add(item);
				itemMap.put(label, item);
				
				itemCount++;
			}
		}
		
		long width = itemCount==0 ? 100 : 3600000*20/itemCount;
		long minX = Long.MAX_VALUE;
		long maxX = 0;
		long minY = Long.MAX_VALUE;
		long maxY = 0;
		long maxZ = 0;
		long v = 0;
		Object valX = 0;
		Object valY = 0;
		Object valZ = 0;
		String val;
		
		for(int i=0; i<list.size(); i++){
			List<Object> data;
			Object x = null;
			Map<String, Object> row = list.get(i);
			String label = (String)row.get(labelFld);
			
			
			Map<String, Object> item = itemMap.get(label);
			int idx = (Integer)item.get("idx");
			data = (List)item.get("data");

			x = row.get(xFld);
			
			if (x instanceof Timestamp) {
				type = "time";
				Timestamp t = (Timestamp) x;
				x = t.getTime();// + dum + idx*width;
			}else if(x instanceof Date){
				type = "time";
				Date t = (Date) x;
				x = t.getTime();// + dum + idx*width;
			}else{
				valX = row.get(xFld) ;
				val = StringUtils.split(valX.toString(), '.')[0];
				v = Long.parseLong(val);
				
				if(maxX<v){
					maxX = v;
				}
				if(minX>v){
					minX = v;
				}				
			}
			

			valY = row.get(yFld) ;
			val = StringUtils.split(valY.toString(), '.')[0];
			if(val.startsWith("0E")){
				v = 0;
			}else{
				v = Long.parseLong(val);
			}
			
			if(maxY<v){
				maxY = v;
			}
			if(minY>v){
				minY = v;
			}
			
			if(hasZ){
				valZ = row.get(zFld) ;
				val = StringUtils.split(valZ.toString(), '.')[0];
				v = Long.parseLong(val);
				
				if(maxZ<v){
					maxZ = v;
				}
			}
			
			data.add("[" + x + "," + row.get(yFld) + (hasZ ? "," + valZ : "") + "]");
			
		}

		result.put("data", dataList);
		result.put("itemCount", itemCount);
		result.put("type", type);
		result.put("minX", minX);
		result.put("maxX", maxX);
		result.put("minY", minY);
		result.put("maxY", maxY);
		result.put("maxZ", maxZ);
		
		return result.toString();
	}
	public static String list2chart(List<Map<String, Object>> list, String labelFld, String xFld, String yFld, String zFld) throws Exception{
		boolean hasZ = StringUtils.isNotEmpty(zFld);
		boolean hasX = StringUtils.isNotEmpty(xFld);
		boolean hasL = StringUtils.isNotEmpty(labelFld);

		try {
			if(hasZ){
				return list2chartIXYZ(list, xFld, yFld, zFld, labelFld);
			}else if(hasX){
				if(hasL){
					return list2chartIXYZ(list, xFld, yFld, zFld, labelFld);
				}else{
					return list2chartXY(list, xFld, yFld);
				}
			}else{
				return list2chartIY(list, yFld, labelFld);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	public static String today() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ();
		return formatter.format (currentTime);	
	}
	
	public static Map<String, Object> sortField(Map<String, Object> colMap, String fields){
		if(StringUtils.isEmpty(fields)){
			return colMap;
		}
		
		String[] fList = fields.split(",");
		
		Map<String, Object> cols =  new LinkedCaseInsensitiveMap<Object>();
		
		for(String key : fList){
			Object obj = colMap.get(key);
			if(obj!=null){
				cols.put(key, obj);
			}
		}
		for(String key : colMap.keySet()){
			if(!cols.containsKey(key)){
				cols.put(key, cols.get(key));
			}
		}

		return cols;		
	}

}
