package louis.general.util;

import android.util.Log;

import louis.general.BuildConfig;

public class LogUtil
{
	static final String TAG="NRDT";
	private static final boolean debug= BuildConfig.DEBUG;
	public static void debug(String msg){
		if(debug){
			Log.d(TAG+"Debug",msg);
		}
	}
	public static void error(String msg){
		if(debug){
			Log.e(TAG+"Error",msg);
		}
	}
	public static void other(String msg){
		if(debug){
			Log.e(TAG+"Other",msg);
		}
	}
	public static void printCurLineNumber(){
		StackTraceElement[] stackTraceElements=new Throwable().getStackTrace();
		debug("class->"+stackTraceElements[1].getClassName()+";step->"+stackTraceElements[1].getLineNumber());
	}
}
