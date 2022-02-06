package io.mkeasy.utils;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import lombok.extern.slf4j.Slf4j;

// http://kingbbode.tistory.com/14

@Slf4j
public class ScriptEngineUtil {

	private static ScriptContext context = new SimpleScriptContext();
	private static Bindings scope = context.getBindings(ScriptContext.ENGINE_SCOPE);
	public static Bindings getScope() { return scope; }

	public static Invocable getInstance(String script) throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
		scope = context.getBindings(ScriptContext.ENGINE_SCOPE);
		engine.eval(script, context);
		Invocable invocable = (Invocable) engine;
		return invocable;
	}
	
	public static void main(String args[]) throws ScriptException, NoSuchMethodException {
	
		String script = "function test(a, b) { return (a+b); }";
		Invocable s = getInstance(script);
		Object result = s.invokeFunction("test", 18, 19);
		log.debug("{}",result);
		
	}

}
