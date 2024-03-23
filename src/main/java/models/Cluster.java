package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import spoon.reflect.declaration.CtType;

public class Cluster {

	Cluster clusterA;
	Cluster clusterB;
	@SuppressWarnings("rawtypes")
	List<CtType> classes;

	public Cluster(CtType<?> ctType) {
		this.classes = new ArrayList<>();
		this.classes.add(ctType);
	}

	public Cluster(Cluster right, Cluster left) {
		this.setClusterA(left);
		this.setClusterB(right);
	}

	public Cluster(Pair<Cluster, Cluster> pc) {
		this.classes = new ArrayList<>();
		this.classes.addAll(pc.getRight().getClasses());
		this.classes.addAll(pc.getLeft().getClasses());
	}

	public List<CtType> getClasses() {
		return classes;
	}

	public void setClasses(List<CtType> classes) {
		this.classes = classes;
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

}
