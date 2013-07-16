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
 * @date	modified on 2013/07/16
 */
public abstract class MOMCTS {
	//-------------MOMCTS global variables---------------
	private MOUCT root;

	/**
	 * Indicate the objectives to optimize in the MOMCTS planner
	 */
	private Vector<String> objectives;
	
	//-------------end of MOMCTS global variables---------------
	
	//-------------MOMCTS parameters---------------------
	/**
	 * The constant to control the speed of progressive widening
	 */
	private double pwConst;
	
	/**
	 * The Exploration vs. Exploitation constants used in the UCB formula
	 */
	private HashMap<String,Double> EvEConsts;
	
	/**
	 * The rave parameter used to adjust the balance between global rave and local rave
	 */
	private int raveLocal;
	//-------------end of MOMCTS parameters---------------------
	
	public double getEvEConst(String rwdType){
		return SetOperation.getDoubleValue(EvEConsts, rwdType);
	}

	public void setRoot(MOUCT rt){
		root = rt;
	}
	
	public MOUCT getRoot(){
		return root;
	}

	public Vector<String> getObjectives() {
		return objectives;
	}

	public void setObjectives(Vector<String> basicRwdTypes) {
		this.objectives = basicRwdTypes;
	}

	protected double totalNb(Vector<MOUCT> sonNodes, String rwdType){
		double totNb=0;
		for(MOUCT s: sonNodes){
			totNb += s.getNb(rwdType);
		}
		return totNb;
	}

	protected Vector<Double> rewards(Vector<MOUCT> sonNodes, String rwdType){
		Vector<Double> rs = new Vector<Double>();
		for(MOUCT s: sonNodes){
			rs.add(s.avgR(rwdType));
		}
		return rs;
	}
	
	protected Vector<Double> UCBRanks(Vector<MOUCT> sonNodes, String rwdType, double eveConst){
		Vector<Double> rks = new Vector<Double>();
		double totNb= totalNb(sonNodes, rwdType);
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
	 * Given a set of sonNodes, return the vectorial rewards of each sonNode
	 * @param sonNodes
	 * @param rwdTypes
	 * @return
	 */
	protected Vector<Vector<Double>> MOUCBRanks(Vector<MOUCT> sonNodes, Vector<String> rwdTypes){
		Vector<Vector<Double>> rks = new Vector<Vector<Double>>();
		for(String rt : rwdTypes){
			rks.add(UCBRanks(sonNodes, rt));
		}
		return SetOperation.transposeMatrix(rks);
	}

	/**
	 * Return the RAVE values of each action in acts under node.
	 * (RAVE values will be used to estimate action values when one action is 
	 * executed for the first time under certain node
	 * @param acts		the actions whose rave to be calculated
	 * @param rwdType
	 * @param node		if indicated, calculte the weighted sum between global rave and local rave
	 * @return
	 */
	protected Vector<Double> RAVERanks(Vector<Integer> acts, String rwdType, MOUCT node){
		Vector<Double> rks = new Vector<Double>();
		for(int a:acts){
			double gRAVE = root.getRAVE(a, rwdType);//global rave
			if(node != null){
				double beta = ((double)raveLocal)/(raveLocal+node.getRAVEnb(a, rwdType));
				double lRAVE = node.getRAVE(a, rwdType);//local rave
				rks.add((1-beta)*lRAVE+beta*gRAVE);
			} else {
				rks.add(gRAVE);
			}
		}
		return rks;
	}
	
	protected Vector<Double> RAVERanks(Vector<Integer> acts, String rwdType){
		return RAVERanks(acts, rwdType, null);
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
				//Vector<Double> raves = RAVERanks(compActs, );
				
			}
			
			
		}
	}
	
}
