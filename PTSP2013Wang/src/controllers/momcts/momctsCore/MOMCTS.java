package controllers.momcts.momctsCore;

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
	private double pwConstant;
	
	//TODO RAVE rank, UCB rank
	
	
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
	public void playOneSequence(boolean pwEnabled, String nbType){
		//The sequence of nodes visited during the tree walk
		Vector<MOUCT> nodes = new Vector<MOUCT>();
		nodes.add(root);
		MOUCT currNd = nodes.lastElement();
		
		while(currNd.isRoot() || !currNd.isLeaf()){
			Vector<Integer> candAs = candidateActions(currNd);
			
			boolean newSonToAdd = false;
			if(pwEnabled) {
				int n1 = (int) Math.pow(currNd.getNb(nbType), pwConstant);
				int n2 = (int) Math.pow(currNd.getNb(nbType)+1, pwConstant);
				if(n2>n1 && currNd.getSons().size() < candAs.size()) newSonToAdd = true;
			}
			
			if(newSonToAdd){
				//Unexecuted actions
				Vector<Integer> compActs = SetOperation.complementSet(candAs, currNd.sonActions());
				
			}
			
			
		}
	}
	
}
