package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label newLabel(Node sommet,boolean marque,double cout,Node pere, ShortestPathData data) {
    	return new LabelStar(sommet,marque,cout,pere,data);
    }
}
