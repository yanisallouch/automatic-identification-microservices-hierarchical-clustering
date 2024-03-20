package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

public class ModelInterface {
	String fullyQualifiedName;
	List<ModelMethod> methods;
	List<ModelClass> implementations;

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public List<ModelMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<ModelMethod> methods) {
		this.methods = methods;
	}

	public List<ModelClass> getImplementations() {
		return implementations;
	}

	public void setImplementations(List<ModelClass> implementations) {
		this.implementations = implementations;
	}

	public static List<ModelInterface> makeModelInterfaces(CtClass ctClass) {
		List<ModelInterface> aModelInterfaces = new ArrayList<ModelInterface>();
		Set<CtTypeReference<?>> interfaces = ctClass.getSuperInterfaces();
		for (CtTypeReference aTypeReference : interfaces) {
			ModelInterface aModelInterface = new ModelInterface();

			// TODO Compare with toString()
			aModelInterface.setFullyQualifiedName(aTypeReference.getQualifiedName());
			// TODO
			aModelInterface.setImplementations(null);
			// TODO
			aModelInterface.setMethods(null);

			aModelInterfaces.add(aModelInterface);
		}
		
		return aModelInterfaces;
	}
}
