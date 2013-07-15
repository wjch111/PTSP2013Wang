package controllers.momcts.momctsCore;

import java.util.HashMap;
import java.util.Vector;

import controllers.utils.SetOperation;

/**
 * Based on Monte-Carlo Tree Search algorithm, MOMCTS (Multi-Objective Monte-Carlo Tree Search) 
 * is used in Multi-objective squential decision making problems.
 * This class is a general purpose MOMCTS planner which accepts 1) initial state, 2) action number/action rule, 
 * and 3) simulator as input,
 * and returns optimal action sequence and predictive state model as output
 * @author wj 
 * @date 	created on 2013/07/11
 * @date	modified on 2013/07/12
 */
public abstract class MOMCTS {
	private double pwConst;
	@SuppressWarnings("unused")
	private HashMap<String,Double> EvEConsts;
	
	public double getEvEConst(String rwdType){
		return SetOperation.getDoubleValue(EvEConsts, rwdType);
	}
	
	//TODO MOucb, MOucb+RaveRanks
	protected Vector<Double> UCBRanks(Vector<MOUCT> sonNodes, String rwdType, double eveConst){
		Vector<Double> rks = new Vector<Double>();
		double totNb=0;
		for(MOUCT s: sonNodes){
			totNb += s.getNb(rwdType);
		}
		for(MOUCT s: sonNodes){
			double rk = s.avgR(rwdType)+ eveConst*Math.sqrt(Math.log10(totNb)/s.getNb(rwdType));
			rks.add(rk);
		}
		return rks;	
	}
	
	protected Vector<Double> UCBRanks(Vector<MOUCT> sonNodes, String rwdType){
		return UCBRanks(sonNodes, rwdType, getEvEConst(rwdType));
	}
	
	public MOUCT bestUCB(Vector<MOUCT> sonNodes, String rwdType){
		return SetOperation.maxItem(sonNodes, UCBRanks(sonNodes, rwdType));
	}
	
	/**
	 * Return the RAVE values of each action in sonNodes.
	 * @param sonNodes
	 * @param rwdType
	 * @return
	 */
	protected Vector<Double> RAVERanks(Vector<MOUCT> sonNodes, String rwdType){
		Vector<Double> rks = new Vector<Double>();
		for(MOUCT s:sonNodes){
			rks.add(root.getRAVE(s.getAction(), rwdType));
		}
		return rks;
	}
	
	protected Vector<Double> UcbRaveRanks(Vector<MOUCT> sonNodes, String rwdType){
		Vector<Double> rks = new Vector<Double>();
		
		
		
		return rks;
	}
	
	
	/**
	 * This class defines several global variables that are used in MOMCTS algorithm.
	 */
	private MOUCT root;

	public void setRoot(MOUCT rt){
		root = rt;
	}
	
	public MOUCT getRoot(){
		return root;
	}

	/**
	 * Given a parent node, return the candidate actions that are feasible in this node
	 * @param parentNode The node containing the state information
	 * @return candidate actions that are feasible
	 */
	public abstract Vector<Integer> candidateActions(MOUCT parentNode);
	
	/**
	 * Given a sequence of actions, evaluate such sequence and return the vectorial reward
	 * @param actSeq the action sequence to be evaluated
	 * @return the rewards obtained by executing actSeq
	 */
	public abstract Vector<Double> evaluateSeq(Vector<Integer> actSeq);
	
	
	
	
	/**
	 * Simulate one tree-walk in MOMCTS
	 */
	//TODO
	public void playOneSequence(boolean pwEnabled, String nbType){
		//The sequence of nodes visited during the tree walk
		Vector<MOUCT> nodes = new Vector<MOUCT>();
		nodes.add(root);
		MOUCT currNd = nodes.lastElement();
		
		while(currNd.isRoot() || !currNd.isLeaf()){
			Vector<Integer> candAs = candidateActions(currNd);
			
			boolean newSonToAdd = false;
			if(pwEnabled) {
				int n1 = (int) Math.pow(currNd.getNb(nbType), pwConst);
				int n2 = (int) Math.pow(currNd.getNb(nbType)+1, pwConst);
				if(n2>n1 && currNd.getSons().size() < candAs.size()) newSonToAdd = true;
			}
			
			if(newSonToAdd){
				//Unexecuted actions
				Vector<Integer> compActs = SetOperation.complementSet(candAs, currNd.sonActions());
				
			}
			
			
		}
	}
	
}
