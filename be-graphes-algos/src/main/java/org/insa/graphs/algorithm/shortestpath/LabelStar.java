package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.*;

public class LabelStar extends Label implements Comparable<Label>{
	private double cout_estime;
	public LabelStar(Node sommet, boolean marque, double cout, Node pere, ShortestPathData data) {
		super(sommet,marque,cout,pere);
		this.cout_estime = coutEstime(sommet,data);
	}
	
	public double getCoutEstime() {
		return this.cout_estime;
	}
	
	public double setCoutEstime() {
		return this.cout_estime;
	}
	public double getTotalCost() {
		return this.getCost()+this.getCoutEstime();
	}
	public double coutEstime(Node sommet,ShortestPathData data) {
		if(data.getMode() == AbstractInputData.Mode.LENGTH) {
			return (double)Point.distance(sommet.getPoint(), data.getDestination().getPoint());
		}
		else {
			int vitesse = Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed());
			return (double)Point.distance(sommet.getPoint(), data.getDestination().getPoint())/(vitesse*1000.0/3600.0);
		}
		
	}
	public int compareTo(LabelStar o) {
		if(Double.compare(this.getTotalCost(), o.getTotalCost())==0) {
			return Double.compare(this.getCoutEstime(), o.getCoutEstime());
		}
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}

}
