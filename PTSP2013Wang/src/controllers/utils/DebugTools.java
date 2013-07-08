package controllers.utils;

public class DebugTools {
	public static void debug(){
		System.out.println("=OK=");		
	}
	
	public static void debug(String s){
		System.out.println(s);
	}
	
	public static void showSeq(int[] is){
		showSeq(is,"\t");
	}
	
	public static void showSeq(int[] is, String sep){
		for(int i=0; i<is.length;i++){
			if(i!=0) System.out.print(sep);
			System.out.print(is[i]);
		}
		System.out.println();
	}

}
