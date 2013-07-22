package controllers.momcts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import controllers.momcts.momctsCore.MOMCTS;
import controllers.momcts.momctsCore.MOUCT;
import controllers.momcts.utils.Archive;
import controllers.momcts.utils.Debug;
import controllers.momcts.utils.Presentation;
import controllers.momcts.utils.SetOperation;
import controllers.momcts.utils.Transformation;
import framework.core.Controller;
import framework.core.Game;
import framework.core.GameObject;
import framework.core.Waypoint;
import framework.graph.Node;
import framework.graph.Path;

/**
 * This class is created to solve the point to point navigation problem in the PTSP problem.
 * Macro actions are used in this class.
 * @author wj
 * @date	created 2013/07/17
 * @date	modified 2013/07/19
 */
public class ShortNavigator extends MOMCTS{
    /**
     * Best individual (route) found in the current search step.
     */
    public Integer[] m_bestRandomPath;

    /**
     * Best heuristic cost of the best individual
     */
    public double m_bestFitnessFound;
    
	/**
     * Next generated individual to be evaluated
     */
    public Integer[] m_currentRandomPath;

    /**
     * Next two waypoints in the route to pick up.
     */
    public static int[] m_nextPickups;

    /**
     * Current game state
     */
    public static Game m_currentGameState;

    /**
     * Game state used to roll actions and evaluate  the current path.
     */
    public static Game m_futureGameState;

    /**
     * Cache  for speeding up looks for nodes in the graph.
     */
    public static HashMap<Integer, Node> m_nodeLookup;


    //-----------parameters-------------
    /**
     * Number of macro-actions that form the random path.
     */
    public static int MACRO_ACTIONS_DEPTH = 5;

    /**
     * Number of single actions that form a macro action.
     */
    public static int MACRO_ACTION_LENGTH = 10;
    
    public static int seqTimeGranularity = 10;
    /**
     * Record the user preference which determines the priorities between non-dominated solutions
     */
    private Vector<Double> prefs;
    //-----------end of parameters-------------
    
    
    //-----------Debug use----------------
    public Debug dbg = new Debug();
    //-----------end of debug use--------

	public ShortNavigator() {
		super(ShortNavigator.class.getName()+RdomType , false, 0.5, 10, 0.5);
		//super("dist" , false, 1, 10, 0.1);
		String[] objs = {"pnt","time","dist","fuel","damage"};
		//{"wayPointsLeft", "time", "distanceToNextWayPoint", "fuelConsumption", "damageTake"};
		Vector<String> objv = Transformation.strAry2Vec(objs);
		setObjectives(objv);
		this.setSolNbLimitPerPoint(3);
		this.setEvEConst(metaRewardType, 10);
		this.init(-1);
		
        m_nodeLookup = new HashMap<Integer, Node>();
        prefs = objectivePreferences();
	}

	@Override
	public Vector<Integer> candidateActions(MOUCT parentNode) {
		if(parentNode.getContext().size() < MACRO_ACTIONS_DEPTH){
			return SetOperation.arithmeticProgression(Controller.NUM_ACTIONS);
		}
		return new Vector<Integer>();
	}

	@Override
	public Vector<Double> evaluateSeq(Vector<Integer> actSeq) {
        m_futureGameState = m_currentGameState.getCopy();
        //Create the path by implementing macro actions
        for(int i = 0; i < actSeq.size(); i++){
            //Next macro action:
            m_currentRandomPath[i] = actSeq.get(i);
            //Rollout macro-action in the game
            for(int j =0; j < MACRO_ACTION_LENGTH; j++){
                m_futureGameState.tick(m_currentRandomPath[i]);
            }
        }
        //At the end of the random path, return evaluation of the reached state.
        Vector<Double> rs = gameRewards();
        return rs;
	}

	@Override
	public Vector<Integer> generateRandomPath(MOUCT leafNode) {
		Vector<Integer> rndPath = new Vector<Integer>();
		for(int i= leafNode.getDepth();i < MACRO_ACTIONS_DEPTH; i++){
			rndPath.add(SetOperation.randomElement(candidateActions(leafNode)));
		}
		return rndPath;
	}
	
	private Vector<Double> gameRewards(){
		Vector<Double> gameRwds = new Vector<Double>();//(wayPointsLeft, time, closenessToNextWayPoint, fuelConsumption, damageTaken)
		int wayPointsLeft = m_futureGameState.getWaypointsLeft();
		int timeSpent = m_futureGameState.getTotalTime();
		double distanceToNext;
		double fuelPoints = -m_futureGameState.getFuelTanksCollected();
		double damageTaken = m_futureGameState.getShip().getDamage();

        //all objectives are to be minimized
        if(m_nextPickups == null){
        	distanceToNext = 0;
        } else {
        	Waypoint obj0 = m_futureGameState.getWaypoints().get(m_nextPickups[0]);
            boolean obj0Collected = false;
            obj0Collected = obj0.isCollected();
            
            if(obj0Collected) {
            	//Waypoint obj1 = m_futureGameState.getWaypoints().get(m_nextPickups[1]);
            	//distanceToNext = m_futureGameState.getShip().s.dist(obj1.s);
            	distanceToNext = 0;
            } else {
            	Path pathToFirst = getPathToGameObject(m_futureGameState,obj0, m_nextPickups[0]);
            	distanceToNext = pathToFirst.m_cost;
            }
        }

        gameRwds.add((double) wayPointsLeft);
        gameRwds.add((double) timeSpent);
        gameRwds.add(distanceToNext);
        gameRwds.add(fuelPoints);
        gameRwds.add(0.);//gameRwds.add(damageTaken);

        return gameRwds;
	}
	
    
    /**
     * Generation a weight vector indicating our preference to the points
     * (in order "wayPointsLeft", "time", "distanceToNextWayPoint", "fuelConsumption", "damageTake")
     * @return
     */
    public Vector<Double> objectivePreferences(){
    	Vector<Double> wts = SetOperation.ones(5, 0.);
    	wts.set(0, 1000.);
    	wts.set(1, 1.);
    	wts.set(2, 10.);
    	wts.set(3, 500.);
    	wts.set(4, 50.);
    	return wts;
    }
	
    /**
     * Gets the path from the current location of the ship to the object passed as parameter.
     * @param a_game copy of the current game state.
     * @param a_gObj object ot get the path to.
     * @param a_objKey index of the object to look for.
     * @return the path from the current ship position to  a_gObj.
     */
    private static Path getPathToGameObject(Game a_game, GameObject a_gObj, int a_objKey){
        //The closest node to the ship's location.
        Node shipNode = MOMCTSController.m_graph.getClosestNodeTo(a_game.getShip().s.x, a_game.getShip().s.y);

        //The closest node to the target's location (checking the cache).
        Node objectNode = null;
        if(m_nodeLookup.containsKey(a_objKey))
            objectNode = m_nodeLookup.get(a_objKey);
        else{
            objectNode = MOMCTSController.m_graph.getClosestNodeTo(a_gObj.s.x, a_gObj.s.y);
            m_nodeLookup.put(a_objKey, objectNode);
        }
        //Get the parh between the nodes.
        return MOMCTSController.m_graph.getPath(shipNode.id(), objectNode.id());
    }
    
	/**
     * Updates  m_nextPickups, that indicates the next a_howMany waypoints to follow.
     * @param a_howMany number of waypoints to include in the search.
     */
    public static void updateNextWaypoints(int a_howMany){
        m_nextPickups = null;
        //try{
            //All my waypoints
            LinkedList<Waypoint> waypoints = m_currentGameState.getWaypoints();

            //Number of waypoints visited.
            int nVisited = m_currentGameState.getWaypointsVisited();
            if(nVisited != waypoints.size()){
                //Array with the next waypoints to visit, considering the case where there are less available.
                m_nextPickups = new int[Math.min(a_howMany, waypoints.size() - nVisited)];
                int pLength =  m_nextPickups.length; //number of elements to pick up.
                int bestPath[] = MOMCTSController.m_tspGraph.getBestPath();
                
                //Go through the best path and check for what is collected.
                for(int i = 0, j = 0; j < pLength && i<bestPath.length; ++i){
                    int key = bestPath[i];
                    if(!waypoints.get(key).isCollected()){
                        //The first pLength elements not visited are selected.
                        m_nextPickups[j++] = key;
                    }
                }
            }
        //}catch(Exception e){
            //e.printStackTrace();
        //}
    }
    
    /**
     * Initializes the random search engine. This function is also called to reset it.
     * @param m_lastMacroActionExecuted 
     */
    public void init(int lastActionExecuted){
        //Resetting the random paths found and best fitness.
        m_bestRandomPath = new Integer[MACRO_ACTIONS_DEPTH];        
        m_currentRandomPath = new Integer[MACRO_ACTIONS_DEPTH];
        
        for(int i=0; i<MACRO_ACTIONS_DEPTH; i++){
        	m_bestRandomPath[i] = 0;
        	m_currentRandomPath[i] = 0;
        }
        m_bestFitnessFound = 100000;
        
        MOUCT s = null;//root.findSon(lastActionExecuted);
        if(s==null) root = new MOUCT(-1);
        else {
        	s.resetAsRoot();
        	root = s;
        }
        archive = new Archive();
    }
    
    /**
     * Runs the Random Search engine for one cycle.
     * @param a_gameState Game state where the macro-action to be decided must be executed from.
     * @param a_timeDue When this function must end.
     * @return  the action decided to be executed.
     */
    public int run(Game a_gameState, long a_timeDue)
    {
        m_currentGameState = a_gameState;
        updateNextWaypoints(2);
        double remaining = (a_timeDue-System.currentTimeMillis());

    	//root.showSelf(2);
        //check that we don't overspend
        while(remaining > seqTimeGranularity){
        	playOneSequence(a_timeDue);
            //create and evaluate a new random path.
        	Vector<Double> optPnt = archive.getBestPoint(prefs, maximize);
            double optPathFitness = SetOperation.weightedSum(optPnt, prefs);
            //keep the best one.
        	if(SetOperation.isBetter(optPathFitness, m_bestFitnessFound, maximize)){
            	Vector<Integer> optSol = archive.getOneSol(optPnt);
            	optSol.toArray(m_currentRandomPath);
        		m_bestFitnessFound = optPathFitness;
            	System.arraycopy(m_currentRandomPath, 0, m_bestRandomPath,0,MACRO_ACTIONS_DEPTH);
            }
            //update remaining time.
            remaining = (a_timeDue-System.currentTimeMillis());
        }
        //Presentation.spr();

        //take the best one so far, the best macroaction is the first one of the path.
        return m_bestRandomPath[0];
    }
    
}
