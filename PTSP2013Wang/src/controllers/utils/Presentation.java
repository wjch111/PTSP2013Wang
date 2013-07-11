package controllers.utils;

import java.util.Vector;

public class Presentation {
	public static void showSeq(Vector seq){
		showSeq(seq, "\t");
	}
	
	public static void showSeq(Vector seq, String sp){
		for(int i=0; i<seq.size(); i++){
			if(i!=0) System.out.print(sp);
			System.out.print(seq.get(i));
		}
	}
	
	public static void showSeqln(Vector seq){
		showSeqln(seq, "\t");
	}
	
	public static void showSeqln(Vector seq, String sp){
		showSeq(seq, sp);
		System.out.println();
	}

	public static void showSeq(int[] is) {
		showSeq(Transformation.intAry2Vec(is));
	}
	
	public static void showSeq(double[] is) {
		showSeq(Transformation.doubleAry2Vec(is));
	}
	
	public static void showSeq(String[] is) {
		showSeq(Transformation.strAry2Vec(is));
	}

	public static void multiOut(String pattern){
		multiOut(pattern,5);
	}
	
	public static void multiOut(String pattern, int repNb){
		for(int i=0; i<repNb;i++){
			System.out.print(pattern);
		}
	}
	
	/**
	 * Print a separation line
	 */
	public static void spr(String s){
		multiOut(s,10);
		System.out.println();
	}
	
	
	public static void main(String[] args){
		double[] is = {1,2,3,4,5};
		Vector<Double> ints = Transformation.doubleAry2Vec(is);
		showSeqln(ints);
	}
	
}
