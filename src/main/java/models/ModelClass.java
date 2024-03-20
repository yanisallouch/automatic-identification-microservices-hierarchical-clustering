package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class ModelClass {

	String fullyQualifiedName;
	List<ModelMethod> methods;
	List<ModelInterface> interfaces;
	List<ModelAttribut> attributs;

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

	public List<ModelInterface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<ModelInterface> interfaces) {
		this.interfaces = interfaces;
	}

	public List<ModelAttribut> getAttributs() {
		return attributs;
	}

	public void setAttributs(List<ModelAttribut> attributs) {
		this.attributs = attributs;
	}

	public static List<ModelClass> makeModelClassesFromModel(CtModel model) {
		List<ModelClass> aModelClasses = new ArrayList<ModelClass>();
		List<CtClass> classes = model.getElements(new TypeFilter<>(CtClass.class));

		for (CtClass ctClass : classes) {
			ModelClass aModelClass = new ModelClass();
			aModelClass.setFullyQualifiedName(ctClass.getQualifiedName());

			List<ModelInterface> aModelInterfaces = ModelInterface.makeModelInterfaces(ctClass);
			aModelClass.setInterfaces(aModelInterfaces);

			List<ModelMethod> aModelMethods = ModelMethod.makeModelMethods(ctClass, aModelClass);
			aModelClass.setMethods(aModelMethods);

			List<ModelAttribut> aModelAttributs = ModelAttribut.makeModelAttributs(ctClass);
			aModelClass.setAttributs(aModelAttributs);

			aModelClasses.add(aModelClass);
		}

		return aModelClasses;
	}

	public static List<ModelClass> resolveCallesFromMethodInvocations(CtClass ctClass, CtMethod ctMethod) {
		// TODO Auto-generated method stub
		return null;
	}

}
