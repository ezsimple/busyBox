package io.mkeasy.webapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

public class List2Tree{
	private String iconPath = "../../../images/icon/";
	List<Map<String, Object>> list = null;
	String upperFld = null;
	String codeFld = null;
	String idFld = null;
	String labelFld = null;
	String rootId = null;
	JSONObject json = null;
	
	public List2Tree(List<Map<String, Object>> list, String upperFld, String codeFld, String labelFld, String idFld, String rootId) {

		this.list = list;
		this.upperFld = upperFld;
		this.codeFld = codeFld;
		this.idFld = idFld;
		this.labelFld = labelFld;
		this.rootId = rootId;
		
		json = new JSONObject();
	}

	public Object getTree()throws Exception {
				
		Map<String,List<Map<String,Object>>> keyMap = new HashMap<String, List<Map<String,Object>>>();
		/*keyMap에 상호 참조되도록 노드를 넣는다. */
		for(Map<String,Object> row : list){
			Map<String,Object> rec = new HashMap<String, Object>();
			rec.putAll(row);
		
			String uppId = rec.get(upperFld).toString();
			String code = rec.get(codeFld).toString();
			
			List<Map<String,Object>> dataList = keyMap.get(uppId);
			
			if(dataList==null){
				dataList = new ArrayList<Map<String,Object>>();
				if(!uppId.equals(code)) {
					keyMap.put(uppId, dataList);
				}
			}
		
			List<Map<String,Object>> children = keyMap.get(code);
			
			if(children==null){
				children = new ArrayList<Map<String,Object>>();
				keyMap.put(code, children);
			}
			
			rec.put("children",children);
			
			replaceNode(rec, rootId, uppId);
			
			dataList.add(rec);
			
		
		}
		
		List<Map<String,Object>> tree = keyMap.get(rootId);
		json.put("tree", tree);
		return json.get("tree");
		
	}
	private void replaceNode(Map<String,Object> rec, String rootId, String uppId)throws Exception {
		Object folder = rec.get("isfolder");
		String isfolder = folder==null ? "" : folder.toString();
		String icon = (String)rec.get("icon");

		if(rootId.equals(uppId)){//root노드
			//rec.put("icon", iconPath+"node.png");
			rec.put("isFolder", true);
		}else if(StringUtils.isEmpty(isfolder)){//자식노드 존재

		}else if(!"0".equals(isfolder)){//자식노드 존재
			rec.put("isFolder", true);
			if(StringUtils.isNotEmpty(icon)){
				rec.put("icon", iconPath+icon);
			}else{
				rec.put("icon", "");
			}
		}else{
			if(StringUtils.isNotEmpty(icon)){
				rec.put("icon", iconPath+icon);
			}else{
				rec.put("icon", iconPath+"node.png");
				
			}
		}
		
		

		rec.put("id", rec.get(idFld));
		rec.put("code", rec.get(codeFld));	
		rec.put("title", rec.get(labelFld));
		
		
	}
}
