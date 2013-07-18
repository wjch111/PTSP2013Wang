package controllers.momcts.momctsCore;

import java.util.HashMap;
import java.util.Vector;

import controllers.utils.Debug;
import controllers.utils.Logger;
import controllers.utils.SetOperation;
import controllers.utils.Transformation;

/**
 * This planner is an instantiation of the abstract class MOMCTS. It is created to test the MOMCTS algorithm
 * @author wj
 * @date	created on 2013/07/17
 * @date	modified on 2013/07/17
 *
 */
public class TestPlanner extends MOMCTS{
	public TestPlanner(){
		//super(objectives, RdomType, maximize, raveLocal, defDiscount, raveLocal, defDiscount);
		super("test"+RdomType, true, 0.5, 10, 0.95);
		String[] objs = {"a","b","c"};
		Vector<String> objv = Transformation.strAry2Vec(objs);
		setObjectives(objv);
		this.setSolNbLimitPerPoint(3);
	}

	@Override
	public Vector<Integer> candidateActions(MOUCT parentNode) {
		Vector<Integer> acts = new Vector<Integer>();
		if(parentNode != null && parentNode.getContext().size()>5) return acts;
		for(int i=0; i< 4 ;i++){
			acts.add(i);
		}
		return acts;
	}

	@Override
	public Vector<Double> evaluateSeq(Vector<Integer> actSeq) {
		HashMap<Integer, Double> freq = SetOperation.frequencePortion(actSeq);
		Vector<Double> rwds = new Vector<Double>();
		rwds.add(SetOperation.getDoubleValue(freq, 0));
		rwds.add(SetOperation.getDoubleValue(freq, 1));
		rwds.add(SetOperation.getDoubleValue(freq, 2));
		return rwds;
	}

	@Override
	public Vector<Integer> generateRandomPath(MOUCT leafNode) {
		Vector<Integer> rndPth = new Vector<Integer>();
		for(int i=0; i<5; i++){
			rndPth.add(SetOperation.randomElement(candidateActions(null)));
		}
		return rndPth;
	}
	
	public static void main(String[] args){
		TestPlanner planner = new TestPlanner();
		Logger log1 = new Logger("rootLog");
		Logger log2 = new Logger("leafLog");
		int time = 1000;
		while(time >= 0){
			Vector<MOUCT>  path = planner.playOneSequence();
			log1.write(""+planner.getSmt()+"\t"+path.firstElement().getRwd(planner.getMetaRewardType()));
			log2.write(""+planner.getSmt()+"\t"+path.lastElement().getRwd(planner.getMetaRewardType()));			
			time--;
		}
		planner.getRoot().showSelf(1, SetOperation.singleton(planner.getMetaRewardType()), false);
		Debug.debug(planner.getArchive().getPoints().size());//.showPntSols();
		Debug.debug(planner.getOptimalSolutions().size());
	}
}
