package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        //DEBUT DE COPIE
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of distances.
        double[] distances = new double[nbNodes];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[data.getOrigin().getId()] = 0;

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        // Actual algorithm, we will assume the graph does not contain negative
        // cycle...
        
        BinaryHeap<Label> TasLabel = new BinaryHeap<Label>();
    
        
        Node currentNode = null;
       // Arc bestArc = null;
        Label[] label = new Label[nbNodes];
        label[data.getOrigin().getId()]= newLabel(data.getOrigin(),false,0,data.getOrigin(),data);
        TasLabel.insert(label[data.getOrigin().getId()]);
        boolean found = false;
      
        
        for (int i = 0; (!found) && (i < nbNodes)&&(!TasLabel.isEmpty()); ++i) {
        	
        	
        	
        	Label minLabel = TasLabel.deleteMin();
        	minLabel.setMarque(true);
        	label[minLabel.getSommet().getId()] = minLabel;
        	currentNode = minLabel.getSommet();
        	notifyNodeMarked(currentNode);
        	System.out.println("On sort le min");
        	System.out.println("Node "+currentNode.getId());
        	
        	if(currentNode == data.getDestination()) {
        		System.out.println(data.getDestination().getId());
        		System.out.println("On est ariive");
        		found = true;
        	}		
        	
        	else {
        		for(Arc arc: currentNode.getSuccessors()) {
        			if (!data.isAllowed(arc)) {
                        continue;
                    }
        			
        			if(label[arc.getDestination().getId()] == null) {
        				System.out.println("Null");
        				label[arc.getDestination().getId()] = newLabel(arc.getDestination(),false,label[currentNode.getId()].getCost()+data.getCost(arc),currentNode,data);
        				TasLabel.insert(label[arc.getDestination().getId()]);	
        				// notifyNodeReached(arc.getDestination());
        				 predecessorArcs[arc.getDestination().getId()] = arc;
        			}
        			else {
        				if(label[arc.getDestination().getId()].isMarque() == false) {
        					System.out.println("false");
        					double w = data.getCost(arc);
        					double oldDistance = label[arc.getDestination().getId()].getCost();
        	                double newDistance = label[currentNode.getId()].getCost() + w;
        	                
        	                if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                                notifyNodeReached(arc.getDestination());
                            }
        	                
        					
            				if (newDistance < oldDistance) {
            					
            					TasLabel.remove(label[arc.getDestination().getId()]);
                				label[arc.getDestination().getId()] = newLabel(arc.getDestination(),false,label[currentNode.getId()].getCost()+w,currentNode,data);
                				TasLabel.insert(label[arc.getDestination().getId()]);
                				System.out.println(arc.getDestination().getId());
                                predecessorArcs[arc.getDestination().getId()] = arc;
                            }
        				}
        				
        			}
        		}
        	}
          
            	
           
          
            
        }
        
        //FIN DE COPIE
        
        ShortestPathSolution solution = null;
        //COPIER A PARTIR DE LA
     // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
        	System.out.println("Infaisable");
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            if(solution.isFeasible()) {
            	System.out.println("faisable");
            	return solution;
            }
        }
      
        	return solution;
        
        
    }
    
    protected Label newLabel(Node sommet,boolean marque,double cout,Node pere,ShortestPathData data) {
    	return new Label(sommet,marque,cout,pere);
    }

}
