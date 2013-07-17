package controllers.momcts.momctsCore;

import java.util.HashMap;
import java.util.Vector;

import controllers.utils.Debug;
import controllers.utils.Presentation;
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
		super("test"+RdomType, true, 0.5, 10, 1);
		String[] objs = {"a","b","c"};
		Vector<String> objv = Transformation.strAry2Vec(objs);
		setObjectives(objv);
	}

	@Override
	public Vector<Integer> candidateActions(MOUCT parentNode) {
		Vector<Integer> acts = new Vector<Integer>();
		if(parentNode != null && parentNode.getContext().size()>=5) return acts;
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
		int time = 1000;
		while(time >= 0){
			if(time%100==0){
				Debug.debug(planner.smt);
				//planner.getRoot().showSelf();
				//Presentation.spr();
			}
			planner.playOneSequence();
			time--;
		}
		
		planner.getRoot().showSelf(2, planner.getMetaRewardType());
		Presentation.showMatrix(planner.getArchive().getPoints());
	}

}
