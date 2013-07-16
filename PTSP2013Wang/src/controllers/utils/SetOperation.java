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
	 * Return the elements indicated by inds in v
	 * @param v
	 * @param inds
	 */
	public static <T> Vector<T> getElements(Vector<T> v, Vector<Integer> inds){
		Vector<T> elems = new Vector<T>();
		for(int i : inds){
			elems.add(v.get(i));
		}
		return elems;
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
	
	/**
	 * Return the value of key k in map, if k does not exist in map, then return 0.0
	 * @param map
	 * @param k
	 * @return
	 */
	public static <K,Double> double getDoubleValue(Map<K,Double> map, K k){
		Double v = map.get(k);
		if(v == null) return 0.0;
		else return (java.lang.Double) v;
	}

	//---------Vector arithmatic Operators-----------
	/**
	 * Calculate addition of vectors a and b
	 * @param a
	 * @param b
	 * @assure a and b should have the same size
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static Vector<Double> adds(Vector<Double> as, Vector<Double> bs){
		if(as.size()!=bs.size()) return null;
		Vector<Double> addition = new Vector<Double>();
		for(int i=0; i<as.size();i++){
			BigDecimal a = new BigDecimal(as.get(i).toString());
			BigDecimal b = new BigDecimal(bs.get(i).toString());
			BigDecimal sum = a.add(b);
			addition.add(java.lang.Double.valueOf(sum.toString()));//Double.valueOf(sum));
		}
		return addition;
	}
	
	/**
	 * Calculate the dot product between vector as and bs
	 * @param as
	 * @param bs
	 * @assure a and b should have the same size
	 * @return
	 */
	public static Vector<Double> muls(Vector<Double> as, Vector<Double> bs){
		if(as.size()!=bs.size()) return null;
		Vector<Double> multiplication = new Vector<Double>();
		for(int i=0; i< as.size();i++){
			BigDecimal a = new BigDecimal(as.get(i).toString());
			BigDecimal b = new BigDecimal(bs.get(i).toString());
			multiplication.add(java.lang.Double.valueOf(a.multiply(b).toString()));
		}
		return multiplication;
	}
	
	public static Vector<Double> muls(Vector<Double> as, double b){
		Vector<Double> multiplication = new Vector<Double>();
		for(int i=0; i< as.size();i++){
			multiplication.add(as.get(i)*b);
		}
		return multiplication;
	}
	
	//---------end of Vector arithmatic Operators-----------
	
	//---------vector operations----------
	public static <T> T pop(Vector<T> v, int elemInd){
		T elem = v.get(elemInd);
		v.remove(elemInd);
		return elem;
	}
	
	public static <T> T popFront(Vector<T> v){
		return pop(v, 0);
	}
	
	public static <T> T popEnd(Vector<T> v){
		return pop(v, v.size()-1);
	}
	
	public static <T> Vector<T> subSeq(Vector<T> v, int beginInd, int rangeLength){
		Vector<T> subS = new Vector<T>();
		int initPos = Math.max(0, beginInd);
		int endPos = Math.min(v.size(), initPos+rangeLength);
		for(int i= initPos; i< endPos; i++){
			subS.add(v.get(i));
		}
		return subS;
	}
	
	public static <T> Vector<T> subSeq(Vector<T> v, int beginInd){
		return subSeq(v, beginInd, v.size());
	}
	
	public static <T> Vector<T> firstNElements(Vector<T> v, int n){
		return subSeq(v, 0, n);
	}
	
	public static <T> boolean exists(Vector<T> vt, int pos){
		if(pos >= 0 && pos < vt.size()){
			return true;
		} else {
			return false;
		}
	}
	//---------end of vector operations-----------

	//---------matrix operations----------
	public static <T> Vector<Vector<T>> matrixOnes(int rowSz, int colSz, T initV){
		Vector<Vector<T>> mtr = new Vector<Vector<T>>();
		if(colSz == 0) colSz = rowSz;
		for(int i=0; i<rowSz; i++){
			Vector<T> oneRow = new Vector<T>();
			for(int j=0; j<colSz; j++){
				oneRow.add(initV);
			}
			mtr.add(oneRow);
		}
		return mtr;
	}
	
	public static <T> Vector<Vector<T>> matrixOnes(int rowSz, T initV){
		return matrixOnes(rowSz, rowSz, initV);
	}
	
	/**
	 * Check if element (i,j) exists in matrix mx
	 * @return
	 */
	public static <T> boolean exists(Vector<Vector<T>> mx, int row, int col){
		if(row >= 0 && row < mx.size() ){
			return exists(mx.get(row),col);
		} else {
			return false;
		}
	}
	
	public static <T> void assignMatrix(Vector<Vector<T>> mx, int row, int col, T value){
		if(exists(mx,row, col)) {
			Vector<T> line = mx.get(row);
			line.set(col, value);
		}		
	}
	
	public static <T> Vector<T> extractCol(Vector<Vector<T>> mx, int colNb){
		Vector<T> colN = new Vector<T>();
		for(Vector<T> line : mx){
			colN.add((T) pop(line, colNb));
		}
		return colN;	
	}
	
	/**
	 * @assure that all rows in mx have the same size 
	 * @param mx
	 * @return the transposation of matrix mx
	 */
	public static <T> Vector<Vector<T>> transposeMatrix(Vector<Vector<T>> mx){
		Vector<Vector<T>> trMt = new Vector<Vector<T>>();
		if(mx.size()==0) return trMt;
		int trRowSz = mx.get(0).size();
		trMt = matrixOnes(trRowSz, mx.size(), (T)new Object());
		for(int i=0; i< mx.size(); i++)
			for(int j=0; j< mx.get(i).size(); j++)
				assignMatrix(trMt, j, i, mx.get(i).get(j));
		
		return trMt;
	}
	
	public static <T> Vector<Vector<T>> duplicateMatrix(Vector<Vector<T>> mx){
		Vector<Vector<T>> dupMt = new Vector<Vector<T>>();
		for(Vector<T> row : mx){
			Vector<T> newRow = new Vector<T>();
			for(T elem : row){
				newRow.add(elem);
			}
			dupMt.add(newRow);
		}
		return dupMt;
	}
	
	//---------end of matrix operations-----------
	
	
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
		double[] as = {1.1,2.3,3.4};
		double[] bs = {1.1,2.3,3.4};
		Vector<Double> av = Transformation.doubleAry2Vec(as);
		Vector<Double> bv = Transformation.doubleAry2Vec(bs);
		Vector<Vector<Double>> mx = new Vector<Vector<Double>>();
		mx.add(av);
		mx.add(bv);
		
		Map<Vector<Double>, Boolean> mp = new HashMap<Vector<Double>, Boolean>();
		mp.put(av, true);
		
		System.out.println(mp.containsKey(bv));

	}
	
}
