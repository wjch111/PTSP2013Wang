package controllers.utils;

import java.util.Vector;

/**
 * This class maintains a non-dominated point set (Vector<Double>) and operates on these points
 * @author wj
 * @date	created 2013/07/17
 * @date	modified 2013/07/17
 *
 */
public class Archive {
	private Vector<Vector<Double>> points;
	
	public Archive(){
		points = new Vector<Vector<Double>>();
	}
	
	public Vector<Vector<Double>> getPoints(){
		return points;
	}
	
	/**
	 * Add a new point pnt in the archive
	 * @param pnt
	 * @return ture if pnt is not included in points, else false
	 */
	public boolean addNewPoint(Vector<Double> pnt){
		if(points.contains(pnt)) return false;
		else {
			points.add(pnt);
			return true;
		}
	}
	
	public boolean contains(Vector<Double> pnt){
		return points.contains(pnt);
	}
	
	/**
	 * Order the points according to their Pareto rank by executing the Fast Non-dominated Sort algorithm
	 * Then keep only the non-dominated points (Pareto rank = 0) in the archive,and eliminate all other points
	 * @param 	maximize defines the optimization direction,
	 * 			if true, the greater the point values in each dimension is , the better
	 * @return the rank 0 points
	 */
	public Vector<Vector<Double>> rearrange(boolean maximize){
		points = MOOTools.nonDominatedPnts(points, maximize);
		return points;
	}
	
}
