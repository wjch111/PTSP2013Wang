package controllers.utils;

import java.util.Vector;

/**
 * This class contains all frequently used type transformations of Weijia WANG programs
 * @author wj
 * @date 2013/07/11
 */
public class Transformation {
	public static Vector<String> ints2strs(Vector<Integer> ints){
		Vector<String> strs = new Vector<String>();
		for(int i=0; i< ints.size();i++){
			strs.add(""+ints.get(i));
		}
		return strs;
	}
	
	public static Vector<Integer> intAry2Vec(int[] is){
		Vector<Integer> ints = new Vector<Integer>();
		for(int i=0;i<is.length;i++){
			ints.add(is[i]);
		}
		return ints;
	}
	
	public static Vector<Double> doubleAry2Vec(double[] ds){
		Vector<Double> dubs = new Vector<Double>();
		for(int i=0;i<ds.length;i++){
			dubs.add(ds[i]);
		}
		return dubs;	
	}

	public static Vector<String> strAry2Vec(String[] ss){
		Vector<String> strs = new Vector<String>();
		for(int i=0;i<ss.length;i++){
			strs.add(ss[i]);
		}
		return strs;
	}
}
