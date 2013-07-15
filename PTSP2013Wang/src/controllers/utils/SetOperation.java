package controllers.utils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class contains functions that are used in operating elements in different kinds of containers
 * @author wj
 * @date created 2013/07/12
 * @date modified 2013/07/12
 */
public class SetOperation {

	public static <K,V> Vector<K> getKeys(Map<K,V> mp){
		Vector<K> keys = new Vector<K>();
		for(K k:mp.keySet()){
			keys.add(k);
		}
		return keys;
	}
	
	public static <K> Vector<K> complementSet(Vector<K> orgSet, Vector<K> objSet) {
		Vector<K> compSet = new Vector<K>();
		Map<K,Boolean> mp = new HashMap<K, Boolean>();
		
		for(K el:objSet){
			mp.put(el, true);
		}
		
		for(K el:orgSet){
			if(!mp.containsKey(el)) compSet.add(el);
		}
		return compSet;
    }	
	
	/**
	 * An example class to define equals() method
	 * @author wj
	 *
	 */
	public class A{
		public int av;
		public A(int a){
			av = a;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + av;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			A other = (A) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (av != other.av)
				return false;
			return true;
		}
		
		public String toString(){
			return ""+av;
		}

		private SetOperation getOuterType() {
			return SetOperation.this;
		}
	}

	
	public class MapComparator implements Comparator{
		private Map map;
		private boolean ascendent;
		public MapComparator(Map m, boolean asc){
			map = m;
			ascendent = asc;
		}
		public MapComparator(Map m){
			map = m;
			ascendent = true;
		}
		

		@Override
		@SuppressWarnings("unchecked")
		public int compare(Object arg0, Object arg1) {
			Comparable v0 = (Comparable) map.get(arg0);
			Comparable v1 = (Comparable) map.get(arg1);
			if(ascendent) return v0.compareTo(v1);
			else return v1.compareTo(v0);
		}
		
	}

	/**
	 * Given a map contains elements as keys and their ranks as values, return the sorted key list
	 * @param mp
	 * @param ascendent		defines the order of sorting, by default, true
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Vector<K> sortItems(Map<K,V> mp, boolean ascendent){
		Vector<K> ks = getKeys(mp);
		SetOperation st = new SetOperation();
		SetOperation.MapComparator cmp = st.new MapComparator(mp, ascendent);
		Collections.sort(ks, cmp);	
		return ks;
	}
	
	public static <K,V> Vector<K> sortItems(Map<K,V> mp){
		return sortItems(mp, true);
	}
	
	/**
	 * Given a list of keys in ks, whose ranks are listed correspondingly in vs, return the sorted key list
	 * @param ks
	 * @param vs
	 * @param ascendent		defines the order of sorting, by default, true
	 * @return
	 */
	public static <K,V> Vector<K> sortItems(Vector<K> ks, Vector<V> vs, boolean ascendent){
		return sortItems(Transformation.vec2map(ks, vs), ascendent);
	}
	
	public static <K,V> Vector<K> sortItems(Vector<K> ks, Vector<V> vs){
		return sortItems(ks,vs, true);
	}
	
	public static <K,V> K getBestItem(Vector<K> ks, Vector<V> vs, boolean max){
		return sortItems(ks, vs, max).lastElement();
	}
	
	public static <K,V> K getBestItem(Map<K,V> mp, boolean max){
		return sortItems(mp, max).lastElement();
	}
	
	public static <K,V> K maxItem(Vector<K> ks, Vector<V> vs){
		return getBestItem(ks,vs, true);
	}
	
	public static <K,V> K maxItem(Map<K,V> mp){
		return getBestItem(mp, true);
	}

	public static <K,V> K minItem(Vector<K> ks, Vector<V> vs){
		return getBestItem(ks,vs, false);
	}
	
	public static <K,V> K minItem(Map<K,V> mp){
		return getBestItem(mp, false);
	}
	
	public static <K,Double> double getDoubleValue(Map<K,Double> map, K k){
		Double v = map.get(k);
		if(v == null) return 0.0;
		else return (java.lang.Double) v;
	}

	//---------Vector Operators-----------
	/**
	 * Calculate addition of vectors a and b
	 * @param a
	 * @param b
	 * @assure a and b should have the same size
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <Double> Vector<Double> adds(Vector<Double> as, Vector<Double> bs){
		if(as.size()!=bs.size()) return null;
		Vector<Double> addition = new Vector<Double>();
		for(int i=0; i<as.size();i++){
			BigDecimal a = new BigDecimal(as.get(i).toString());
			BigDecimal b = new BigDecimal(bs.get(i).toString());
			BigDecimal sum = a.add(b);
			addition.add((Double) java.lang.Double.valueOf(sum.toString()));//Double.valueOf(sum));
		}
		return addition;
	}
	
	
	//---------end of Vector Operators-----------
	
	
	
	
	
	//---------Test Programs--------------
	public void testEquals(){
		int[] as = {1,2,3,4,5,6};
		int[] bs = {4,5,6,7,8};
		
		SetOperation st = new SetOperation();
		
		Vector<A> vas = new Vector<A>();
		Vector<A> vbs = new Vector<A>();
		
		for(int a:as){
			A ma = st.new A(a);
			vas.add(ma);
		}
		
		for(int b:bs){
			A mb = st.new A(b);
			vbs.add(mb);
		}		
		//Presentation.showSeqln(complementSet(Transformation.intAry2Vec(as), Transformation.intAry2Vec(bs)));
		Presentation.showSeqln(complementSet(vas, vbs));
	}
	
	public void testSort(){
		Map<Integer, Double> mp = new HashMap<Integer, Double>();
		mp.put(1, 0.5);
		mp.put(2, 0.8);
		mp.put(3, 0.2);
		mp.put(4, 0.1);
		
		System.out.println(mp);
		System.out.print("keys:"+sortItems(mp));
		System.out.print(minItem(mp));
	}
	//---------end of Test Programs--------------
	public static void main(String[] args){
		SetOperation st = new SetOperation();
		//st.testSort();
		double[] as = {1.234324232323,2.3,3.4};
		double[] bs = {2.2,3.3,4.4};
		Vector<Double> ds = adds(Transformation.doubleAry2Vec(as),Transformation.doubleAry2Vec(bs));
		System.out.println(ds);
	}
	
}
