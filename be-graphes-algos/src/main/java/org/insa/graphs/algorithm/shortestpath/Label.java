package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
	
	private boolean marque;
	
	private Node pere;
	
	private Node sommet;
	
	private double cout;
	
	public Label(Node sommet,boolean marque, double cout, Node pere) {
	this.sommet = sommet;
	this.marque = marque;
	this.cout = cout;
	this.pere = pere;
	}

	public boolean isMarque() {
		return marque;
	}

	public void setMarque(boolean marque) {
		this.marque = marque;
	}

	public Node getPere() {
		return pere;
	}

	public void setPere(Node pere) {
		this.pere = pere;
	}

	public Node getSommet() {
		return sommet;
	}

	public void setSommet(Node sommet) {
		this.sommet = sommet;
	}

	public double getCost() {
		return cout;
	}

	public void setCost(double cout) {
		this.cout = cout;
	}
	public double getTotalCost() {
		return this.cout;
	}

	public int compareTo(Label o) {
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}
			
}