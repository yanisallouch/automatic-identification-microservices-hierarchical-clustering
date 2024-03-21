package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtThisAccessImpl;

public class ModelMethod {
	String fullyQualifiedName;
	// a.k.a. class from which the method is being invoked, i.e.
	// org.package.c1.m1#c2.m2(), caller is c1
	ModelClass caller;
	List<CtInvocation> invocations;
	// a.k.a. class from which the method is being invoked, i.e.
	// org.package.c1.m1#c2.m2(), callee is c2
	List<String> calleesFullyQualifiedName;

	@SuppressWarnings("rawtypes")
	public List<CtInvocation> getInvocations() {
		return invocations;
	}

	@SuppressWarnings("rawtypes")
	public void setInvocations(List<CtInvocation> invocations) {
		this.invocations = invocations;
	}

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

	public List<String> getCalleesFullyQualifiedName() {
		return calleesFullyQualifiedName;
	}

	public void setCalleesFullyQualifiedName(List<String> calleesFullyQualifiedName) {
		this.calleesFullyQualifiedName = calleesFullyQualifiedName;
	}

	@Override
	public String toString() {
		return this.fullyQualifiedName;
	}

	public static List<ModelMethod> makeModelMethods(CtClass ctClass, ModelClass aModelClass) {
		List<ModelMethod> aModelMethods = new ArrayList<ModelMethod>();
		Set<CtMethod> methods = ctClass.getMethods();

		for (CtMethod ctMethod : methods) {
			ModelMethod aModelMethod = new ModelMethod();
			aModelMethod.setFullyQualifiedName(ctClass.getQualifiedName() + "#" + ctMethod.getSignature());
			aModelMethod.setCaller(aModelClass);

			// TODO implementations
			List<CtInvocation> aInvocations = ModelMethod.resolveMethodInvocationsFromClass(ctClass, ctMethod);
			// TODO implemntations
			List<String> aClassesFullyQualifiedName = ModelMethod.resolveCallesFromMethodInvocations(ctClass, ctMethod);
			aModelMethod.setCalleesFullyQualifiedName(aClassesFullyQualifiedName);
			aModelMethods.add(aModelMethod);
		}

		return aModelMethods;
	}

	@SuppressWarnings("rawtypes")
	private static List<CtInvocation> resolveMethodInvocationsFromClass(CtClass ctClass, CtMethod ctMethod) {
		List<CtInvocation> results = new ArrayList<CtInvocation>();
		List<CtInvocation> invocations = ctClass.getElements(new TypeFilter<>(CtInvocation.class));
		for (CtInvocation i : invocations) {
			if (!(i.getPosition().equals(SourcePosition.NOPOSITION))) {
				if (isInvocationWanted(i)) {
					results.add(i);
				}

			}
		}

		return results;
	}

	@SuppressWarnings("rawtypes")
	private static boolean isInvocationWanted(CtInvocation i) {
		CtExpression expr = i.getTarget();
		// TODO add recursively looking for .getTarget()
		if (expr.getClass().equals(CtThisAccessImpl.class)) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	private static CtClass getCalleeClass(CtModel model, CtInvocation i) throws Exception {
		CtTypeReference reference = i.getExecutable().getDeclaringType();
		String referenceName = reference.toString();

		Predicate<CtClass> predicateClass = c -> referenceName
				.equals(c.getParent().toString() + "." + c.getSimpleName());

		Optional<CtClass> matchingClass = model.getElements(new TypeFilter<>(CtClass.class)).stream()
				.filter(predicateClass).findFirst();

		if (matchingClass.isEmpty()) {
			throw new Exception("might be a interface");
		} else {
			return matchingClass.get();
		}
	}

	private static List<String> resolveCallesFromMethodInvocations(CtClass ctClass, CtMethod ctMethod) {
		// TODO implementation
		return null;
	}

}
