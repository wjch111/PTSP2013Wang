package controllers.momcts.utils;

import java.util.Vector;

public class Debug {
	public int counter;
	public long currentTime = -1;
	public long initMemory = -1;

	/**
	* Each time a stopWatch() function is called, it returns the time interval between the current time and the last time the stopWatch() is called
	*/
	public int stopWatch(){
		if(currentTime == -1) {
			currentTime = System.currentTimeMillis();
			return 0;
		} else {
			long now = System.currentTimeMillis();
			int interval = (int) (now - currentTime);
			currentTime = now;
			return interval;
		}
	}
	
	public long resetMemoryCount(){
		initMemory = Runtime.getRuntime().totalMemory();
		return initMemory;
	}
	
	public int getMemoryChange(){
		if(initMemory == -1) {
			resetMemoryCount();
			return 0;
		} else return (int) (Runtime.getRuntime().totalMemory() - initMemory);
	}
	
	public int getMemoryChange(int base){
		return getMemoryChange()/base;
	}
	
	public int getMemoryChangeMB(){
		return getMemoryChange(1024*1024);
	}
	
	
	public static void debug(){
		System.out.println("=OK=");		
	}
	
	public static void debug(String s, String deco){
		System.out.println(deco+s+deco);
	}
	
	public static void debug(String s){
		debug(s,"=");
	}
	
	public static void debug(Object s, String deco){
		debug(""+s, deco);
	}
	
	public static void debug(Object s){
		debug(s, "=");
	}
	
	public static void showSeq(Vector<Integer> is){
		Presentation.showSeq(is);
	}
	
	public void showCounter(){
		System.out.println(counter);
	}
}
