package models;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtType;

public class Cluster {

	Cluster cluster;
	List<CtType> types;

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Cluster(CtType<?> ctType) {
		types = new ArrayList<CtType>();
		types.add(ctType);
	}

	public List<CtType> getTypes() {
		return types;
	}

	public void setTypes(List<CtType> types) {
		this.types = types;
	}

	public void add(CtType type) {
		this.types.add(type);
	}

	public void addAll(List<CtType> types) {
		this.types.addAll(types);
	}
}
