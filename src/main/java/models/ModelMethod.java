package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

public class ModelMethod {
	String fullyQualifiedName;
	// a.k.a. class from which the method is being invoked, i.e.
	// org.package.c1.m1#c2.m2(), caller is c1
	ModelClass caller;
	// a.k.a. class from which the method is being invoked, i.e.
	// org.package.c1.m1#c2.m2(), callee is c2
	List<ModelClass> callees;

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public ModelClass getCaller() {
		return caller;
	}

	public void setCaller(ModelClass caller) {
		this.caller = caller;
	}

	public List<ModelClass> getCallees() {
		return callees;
	}

	public void setCallee(List<ModelClass> callees) {
		this.callees = callees;
	}

	public static List<ModelMethod> makeModelMethods(CtClass ctClass, ModelClass aModelClass) {
		List<ModelMethod> aModelMethods = new ArrayList<ModelMethod>();
		Set<CtMethod> methods = ctClass.getMethods();

		for (CtMethod ctMethod : methods) {
			ModelMethod aModelMethod = new ModelMethod();
			aModelMethod.setFullyQualifiedName(ctClass.getQualifiedName() + ctMethod.getSignature());
			aModelMethod.setCaller(aModelClass);
			List<ModelClass> aModelClasses = ModelClass.resolveCallesFromMethodInvocations(ctClass, ctMethod);
			aModelMethod.setCallee(aModelClasses);
			aModelMethods.add(aModelMethod);
		}

		return aModelMethods;
	}

}
