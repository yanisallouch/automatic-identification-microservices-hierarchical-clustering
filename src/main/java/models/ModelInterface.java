package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class ModelInterface {
	String fullyQualifiedName;
	List<String> methodsFullyQualifiedName;

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public List<String> getMethods() {
		return methodsFullyQualifiedName;
	}

	public void setMethods(List<String> methodsFullyQualifiedName) {
		this.methodsFullyQualifiedName = methodsFullyQualifiedName;
	}

	public String toString() {
		return this.fullyQualifiedName;
	}

	@SuppressWarnings("rawtypes")
	public static List<ModelInterface> makeModelInterfaces(CtModel model, CtClass ctClass) {
		List<ModelInterface> aModelInterfaces = new ArrayList<ModelInterface>();
		Set<CtTypeReference<?>> interfaces = ctClass.getSuperInterfaces();
		for (CtTypeReference aTypeReference : interfaces) {
			ModelInterface aModelInterface = new ModelInterface();
			// TODO Compare with toString()
			aModelInterface.setFullyQualifiedName(aTypeReference.getQualifiedName());
			aModelInterface.setMethods(resolveMethods(model, aTypeReference));
			aModelInterfaces.add(aModelInterface);
		}

		return aModelInterfaces;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<String> resolveMethods(CtModel model, CtTypeReference aTypeReference) {
		List<String> results = new ArrayList<String>();
		List<CtInterface> aInterface = model.getElements(new TypeFilter<>(CtInterface.class));
		for (CtInterface i : aInterface) {
			String interfaceFound = i.getQualifiedName();
			String interfaceGiven = aTypeReference.getQualifiedName();
			boolean equals = interfaceFound.equals(interfaceGiven);
			if (equals) {
				Set<CtMethod> methods = i.getMethods();
				for (CtMethod m : methods) {
					results.add(i.getQualifiedName() + "#" + m.getSignature());
				}
			}
		}

		return results;
	}

	@SuppressWarnings("rawtypes")
	private static List<String> resolveImplementationsFromInterface(CtModel model, CtTypeReference aTypeReference) {
		List<String> results = new ArrayList<String>();
		List<CtClass> classes = model.getElements(new TypeFilter<CtClass>(CtClass.class));
		String string = aTypeReference.getQualifiedName();
		for (CtClass c : classes) {
			Set<CtTypeReference<?>> interfacesFromClass = c.getSuperInterfaces();
			for (CtTypeReference<?> i : interfacesFromClass) {
				String string2 = i.toString();
				boolean equals = string.equals(string2);
				if (equals) {
					results.add(c.getQualifiedName());
				}
			}
		}

		return results;
	}
}
