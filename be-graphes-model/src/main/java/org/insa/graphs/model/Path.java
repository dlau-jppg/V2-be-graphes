package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * 
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        // TODO:
        
        Node noeud_depart;
        Node destination;
        if (nodes.size()==0) {
        	return new Path(graph);
        }
        else if (nodes.size()==1) {
        	return new Path(graph,nodes.get(0));
        }
        else {
        	for(int i=0;i < nodes.size()-1; i++) {
        		noeud_depart = nodes.get(i);
        		destination = nodes.get(i+1);
        		Arc arc_bon = null;
        		double temps_arrete =0;
        		int num_dest = destination.getId();
        		int nb_passage = 0;
        		List<Arc> ark = noeud_depart.getSuccessors();
        		for(Arc a : ark) {
        			if(a.getDestination().getId() == num_dest) {
        				nb_passage++;
        				if(nb_passage==1) {
        					temps_arrete= a.getMinimumTravelTime();
        					arc_bon =a;
        				}
        				if(temps_arrete >= a.getMinimumTravelTime()) {
        					temps_arrete = a.getMinimumTravelTime();
        					arc_bon = a;
        				}
        			}
        		}
        		if(nb_passage ==0) {
        			throw new IllegalArgumentException();
        		}
        		arcs.add(arc_bon);
        	}
        }
        return new Path(graph, arcs);
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * 
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        int cpt = nodes.size();
        Node node = null;
        Node nest =null;
        if(cpt==0) {
        	return new Path(graph);
        }
        else if(cpt==1) {
        	return new Path(graph,nodes.get(0));
        }
        else {
        Node next;
       /* for(Node node: nodes) {
        	int i = 0;*/
        	for(int i =0;i < cpt-1;i++ ){ 
        
        	Arc premier =null;
        	/*if(node.getId()+1 == cpt) {
        		next = null;
        	}*/
        	
        	//else {
        		node = nodes.get(i); 
        		next = nodes.get(i+1);
        	//}
        	List<Arc> ark= node.getSuccessors();
        	for(Arc a: ark) {
        		if(a.getDestination().equals(next)) {
        			if(premier == null) {
        				premier = a;
        			}
        			else {
        				if(premier.getLength() > a.getLength() ) {
        					premier =a;
        				}
        			}
        		}
        		/*else {
        			throw new IllegalArgumentException();
        		}*/
        	
        	}
        	/*if((premier ==null)&&(next.equals(node))){
            	System.out.println("Dernier noeud");
            }*/
        	if(premier == null) {
        		throw new IllegalArgumentException();
        	}
        	
        	else {
        		arcs.add(premier);
        	}
        	
        	
       // 	i++;	
        //}
        	}	
        	
        }
        return new Path(graph,arcs);
        
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     * 
     */
    public boolean isValid() {
       
    	boolean valid = true;
    	if((this.origin != null)&&(this.arcs.size()!=0)) {
    		if(arcs.get(0).getOrigin() != origin) {
    			valid=false;
    		}
    		for(int i = 0 ; i<arcs.size()-1;i++) {
    			if(arcs.get(i).getDestination() != arcs.get(i+1).getOrigin()) {
    				valid = false;
    			}
    		}
    	}
    	else if(this.origin != null) {
    		if(arcs==null) {
    			valid=false;
    		}
    	}
    	else if (this.isEmpty()!=true) {
    		valid=false;
    	}
        return valid;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     * 
     */
    public float getLength() {
    	float longueur = 0;
    	for(Arc a:this.arcs) {
    		longueur += a.getLength();
    	}
        return longueur;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     * 
     */
    public double getTravelTime(double speed) {
    	double tempsvog = 0 ;
    	for(Arc a : this.arcs) {
    		tempsvog += a.getTravelTime(speed);
    	}
    	
        return tempsvog;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     *
     */
    public double getMinimumTravelTime() {
        // TODO:
    	double tempsvog = 0 ;
    	for(Arc a:this.arcs) {
    		tempsvog += a.getMinimumTravelTime();
    	}
    	
        return tempsvog;
       
    }

}
