package controllers.momcts;

import java.util.Vector;

import controllers.momcts.momctsCore.MOMCTS;
import controllers.momcts.momctsCore.MOUCT;

/**
 * This class is created to solve the point to point navigation problem in the PTSP problem.
 * Macro actions are used in this class.
 * @author wj
 * @date	created 2013/07/17
 * @date	modified 2013/07/17
 */
public class ShortNavigator extends MOMCTS{

	public ShortNavigator(String metaRewardType, boolean maximize,
			double pwConst, int raveLocal, double defDiscount) {
		super(metaRewardType, maximize, pwConst, raveLocal, defDiscount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector<Integer> candidateActions(MOUCT parentNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Double> evaluateSeq(Vector<Integer> actSeq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Integer> generateRandomPath(MOUCT leafNode) {
		// TODO Auto-generated method stub
		return null;
	}

}
