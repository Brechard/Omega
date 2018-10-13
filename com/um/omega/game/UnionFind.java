package com.um.omega.game;

public class UnionFind {

	private int[] parent;
	private int[] size;
	
	public UnionFind(int size) {
		parent = new int[size];
		this.size = new int[size];
		for(int i = 0; i < size; i++) {
			parent[i] = i;
			this.size[i] = 1;
		}
	}
	
	public UnionFind(UnionFind union) {
		this.parent = union.parent.clone();
		this.size = union.size.clone();
	}
	
	public int getRoot(int i) {
		while(i != parent[i]) {
			parent[i]  = parent[parent[i]];
			i = parent[i];
		}
		return i;
	}
	
	public boolean find(int p, int q) {
		return getRoot(p) == getRoot(q);
	}
	
	public void unite(int p, int q) {
		int rootP = getRoot(p);
		int rootQ = getRoot(q);
		
		if (rootP == rootQ) return;

//		System.out.println("Union cell " +p+ ", parent: " +rootP+ " with cell " +q+ " parent: " +rootQ);
//		System.out.println("Size of rootP is " +size[rootP]+ " of rootQ is " +size[rootQ]);

		if(size[rootP] < size[rootQ]) {
			parent[rootP] = rootQ;
			size[rootQ] += size[rootP];
		} else {
			parent[rootQ] = rootP;
			size[rootP] += size[rootQ];			
		}
	}
	
	public long getCount() {
		long rate = 1;
		long bigGroups = 0;
		for(int i = 0; i < parent.length; i++) {
			if(parent[i] == i) { // He is the root
				rate *= size[i];
				if(size[i] != 1)
					bigGroups += Math.abs(size[i] - 3);
			}
		}
		return rate - bigGroups;
	}	
	
	public void printUnionFind() {
		for (int i = 0; i < parent.length; i++) {
			System.out.println("For cell " +i+ " the parent is: " +parent[i]+ " and the size is: " +size[i]);
		}
	}
	
}
