package controllers.utils;

import java.util.HashMap;
import java.util.Vector;

/**
 * This class contains functions that are used in operating elements in different kinds of containers
 * @author wj
 * @date created 2013/07/12
 * @date modified 2013/07/12
 */
public class SetOperation {
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
	
	public static <K> Vector<K> complementSet(Vector<K> orgSet, Vector<K> objSet) {
		Vector<K> compSet = new Vector<K>();
		HashMap<K,Boolean> mp = new HashMap<K, Boolean>();
		
		for(K el:objSet){
			mp.put(el, true);
		}
		
		for(K el:orgSet){
			if(!mp.containsKey(el)) compSet.add(el);
		}
		return compSet;
    }
	
	public static void main(String[] args){
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
	
}
