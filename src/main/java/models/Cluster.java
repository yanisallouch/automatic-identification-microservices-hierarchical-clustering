package models;

import spoon.reflect.declaration.CtType;

public class Cluster {

	Cluster clusterA;
	Cluster clusterB;
	@SuppressWarnings("rawtypes")
	CtType classe;

	public Cluster(CtType<?> ctType) {
		this.classe = ctType;
	}

	public Cluster(Cluster right, Cluster left) {
		this.setClusterA(left);
		this.setClusterB(right);
	}

	public Cluster getClusterA() {
		return clusterA;
	}

	public void setClusterA(Cluster clusterA) {
		this.clusterA = clusterA;
	}

	public Cluster getClusterB() {
		return clusterB;
	}

	public void setClusterB(Cluster clusterB) {
		this.clusterB = clusterB;
	}

	@SuppressWarnings("rawtypes")
	public CtType getTypes() {
		return classe;
	}

	@SuppressWarnings("rawtypes")
	public void setTypes(CtType classe) {
		this.classe = classe;
	}

}
