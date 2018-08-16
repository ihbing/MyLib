package louis.general.util;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.content.pm.*;
import de.robv.android.xposed.*;

public class XposedUtil
{
	/*查找变量*/
	public static List<Field> findFields(Class<?> cls){
		List<Field> listFields=new ArrayList<Field>();
		while(cls!=null){
			for(Field f:cls.getDeclaredFields())listFields.add(f);
			cls=cls.getSuperclass();
		}
		return listFields;

	}

	/*修改返回值*/
	public static void findAndHookMethodReturn(Class<?> cls,String methodName,Object result){
		try{XposedHelpers.findAndHookMethod(cls,methodName,XC_MethodReplacement.returnConstant(result));}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	/*对hook方法统一加异常捕获*/
	public static Class<?> findClass(String clsName,ClassLoader loader){
		try{return XposedHelpers.findClass(clsName,loader);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}
		return null;
	}
	
	public static void hookAllConstructors(Class<?> cls,XC_MethodHook hook){
		try{XposedBridge.hookAllConstructors(cls,hook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static void hookAllConstructors(String clsName,ClassLoader loader,XC_MethodHook hook){
		try{XposedBridge.hookAllConstructors(XposedHelpers.findClass(clsName,loader),hook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static void hookAllMethods(String clsName,ClassLoader loader,String methodName,XC_MethodHook hook){
		try{XposedBridge.hookAllMethods(XposedHelpers.findClass(clsName,loader),methodName,hook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}
	}
	
	public static void hookAllMethods(Class<?> cls,String methodName,XC_MethodHook hook){
		try{XposedBridge.hookAllMethods(cls,methodName,hook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static void findAndHookConstructor(Class<?> cls,Object... paramTypeAndHook){
		try{XposedHelpers.findAndHookConstructor(cls,paramTypeAndHook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}

	public static void findAndHookConstructor(String clsName,ClassLoader loader,Object... paramTypeAndHook){
		try{XposedHelpers.findAndHookConstructor(clsName,loader,paramTypeAndHook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static void findAndHookMethod(String clsName,ClassLoader loader,String methodName,Object... paramTypeAndHook){
		try{XposedHelpers.findAndHookMethod(clsName,loader,methodName,paramTypeAndHook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static void findAndHookMethod(Class<?> cls,String methodName,Object... paramTypeAndHook){
		try{XposedHelpers.findAndHookMethod(cls,methodName,paramTypeAndHook);}catch(Throwable t){
			LogUtil.other(t.getMessage());
		}

	}
	
	public static Boolean mequals(String str, String str1)
	{
		if (str.startsWith(str1) & str.endsWith(str1)) return true;
		return false;
	}
	
	public static Boolean isSystemApp(LoadPackageParam lpparam){
		return (lpparam.appInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0;
	}
	
	/*禁用 xposed*/
	public static Boolean disXposed(boolean isDisXposed){
		try
		{
			Field fieldDisHooks=ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedBridge").getDeclaredField("disableHooks");
			fieldDisHooks.setAccessible(true);
			fieldDisHooks.set(null,Boolean.valueOf(isDisXposed));
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
}
