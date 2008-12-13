package org.abratuhi.mmorpg.coordinates;

public class Coordinate {
	
	private int n;
	int[] coordinates;
	
	public Coordinate(int dimension){
		setDim(dimension);
		setXs(new int[this.n]);
	}
	
	public Coordinate(int dimension, int[] coords){
		setDim(dimension);
		setXs(coords);
	}
	
	public int getDim(){
		return this.n;
	}
	
	public int[] getXs(){
		return this.coordinates;
	}
	
	public int getX(int index){
		return this.coordinates[index];
	}
	
	public void setDim(int dimension){
		this.n = dimension;
	}
	
	public void setXs(int[] coords){
		this.coordinates = new int[this.n];
		System.arraycopy(coords, 0, this.coordinates, 0, this.n);
	}
	
	public void setX(int index, int value){
		this.coordinates[index] = value;
	}
	
	public Coordinate clone(){
		Coordinate clonedCoordinate = new Coordinate(this.getDim(), this.getXs());
		return clonedCoordinate;
	}
	
	public void add(Coordinate c){
		for(int i=0; i<n; i++) coordinates[i] += c.getX(i);
	}
	
	public void negate(){
		for(int i=0; i<n; i++) coordinates[i] = -coordinates[i];
	}
	
	public void substract(Coordinate c){
		for(int i=0; i<n; i++) coordinates[i] -= c.getX(i);
	}
	
	public int getMax(){
		int max = Integer.MIN_VALUE;
		for(int i=0; i<n; i++) if (coordinates[i]>max) max = coordinates[i];
		return max;
	}
	
	public int getMin(){
		int min = Integer.MAX_VALUE;
		for(int i=0; i<n; i++) if (coordinates[i]<min) min = coordinates[i];
		return min;
	}
	
	public void abs(){
		for(int i=0; i<n; i++) coordinates[i] = Math.abs(coordinates[i]);
	}
	
	public int getChebyshevDistanceTo(Coordinate c){
		Coordinate t = this.clone();
		t.substract(c);
		t.abs();
		return t.getMax();
	}

}
