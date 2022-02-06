package io.mkeasy.webapp.utils;

// import java.lang.reflect.Method;
// import java.util.HashMap;
import java.util.Map;

// import java.lang.reflect.Method;
// import java.util.HashMap;
// import java.util.Map;

// import javax.servlet.jsp.PageContext;
// import javax.servlet.jsp.el.Expression;
// import javax.servlet.jsp.el.FunctionMapper;
// import org.apache.commons.el.ExpressionEvaluatorImpl;
// import org.apache.commons.el.VariableResolverImpl;
// import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;

// 참고용으로 남겨둡니다.
@SuppressWarnings("deprecation")
public class ELUtil {


//	private static FunctionMapper  fm = new FunctionMapper() {
//		Map<String, Method> functionMap = null;
//		public void init(){
//			Map<String, Method> tempFunctionMap = new HashMap<String, Method>();
//
//			Method[] ms = ELFunctions.class.getDeclaredMethods();
//			for(Method m : ms){
//				String fname1 = m.toString();
//				System.out.println(fname1);
//				String fname = "f:"+m.getName();
//				if(tempFunctionMap.containsKey(fname)){
//					throw new RuntimeException(ELFunctions.class.getName() + " 클래스에 동일이름의 클래스가 존재합니다. 이클래스에는 동일 명의 클래스를 정의하지 마세요.");
//				}
//				tempFunctionMap.put(fname, m);
//			}
//			functionMap = tempFunctionMap;
//		}
//
//		
//		public Method resolveFunction(String g, String n) {
//			if(functionMap==null){
//				init();
//			}
//			String fname = g+":"+n;
//
//			return functionMap.get(fname);
//		}
//	};

	public static Object evaluate(String src, Map<String, Object> map) throws Exception{
		Object value = src;
		// PageContext pc = new PageContextImpl();
			
		for(String key : map.keySet()){
			Object data = map.get(key);
			
			// pc.setAttribute(key.toLowerCase(), data);
		}
		
		// return evaluate(src, pc);
		return evaluate(src);
	}
	
	// public static Object evaluate(String src, PageContext pc) throws Exception{
	public static Object evaluate(String src) throws Exception{
		Object value = src;
		
//		VariableResolverImpl varResolver = new VariableResolverImpl(pc); 
//		
//		ExpressionEvaluatorImpl exprEval = new ExpressionEvaluatorImpl();
//	
//	    Expression expression = exprEval.parseExpression(src, String.class, fm);
//	    value = expression.evaluate(varResolver);
	
		return value;
	}
	
}
