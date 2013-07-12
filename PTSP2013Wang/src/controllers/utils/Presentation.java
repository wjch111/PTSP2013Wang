package controllers.utils;

import java.text.NumberFormat;
import java.util.Vector;

/**
 * This class contains all functions that are used to facilitate the presentation
 * of strings and numbers in console mode of java programs
 * @author wj
 * @date created on 2013/07/11
 * @date modified on 2013/07/12
 *
 */
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
	
	public static void spr(){
		spr("-");
	}
	
	/**
	 * Given a decimal number, show the decimal with accuracy to indicated decimal places
	 * @param n the number to be shown
	 * @param precision the number of decimals to show
	 * @return
	 */
	public static String ndigits(double nb, int precision){
		NumberFormat ddf1=NumberFormat.getNumberInstance() ;
		ddf1.setMaximumFractionDigits(precision);
		return ddf1.format(nb);
	}
	
	public static String ndigits(double nb){
		return ndigits(nb,2);
	}
	
	public static void main(String[] args){
		double[] is = {1,2,3,4,5};
		Vector<Double> ints = Transformation.doubleAry2Vec(is);
		showSeqln(ints);
	}
	
}
