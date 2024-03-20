package models;

import java.util.List;
import java.util.Map;

import spoon.reflect.declaration.CtClass;

public class ModelClass {
	
	String fullyQualifiedName;
	List<CtClass> implementations;
	
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}
	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}
	public List<CtClass> getImplementations() {
		return implementations;
	}
	public void setImplementations(List<CtClass> implementations) {
		this.implementations = implementations;
	}
	
	public void addImplementations(List<CtClass> implementations) {
		this.implementations.addAll(implementations);
	}
	
	public void addImplementations(CtClass implementation) {
		this.implementations.add(implementation);
	}
	
}
