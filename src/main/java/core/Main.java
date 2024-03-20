package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import models.ModelClass;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * Step 1: in this step, the hierarchical clustering algorithm, which produces a
 * binary tree also called dendrogram, is applied on the classes of the OO
 * application.
 * 
 * Step 2: this step aims to obtain disjoint clusters from the dendrogram. Each
 * cluster is considered as a microservice.
 */

public class Main {

	CtClass<?> toto;
	protected static CtModel model;

	public static void main(String[] args) {

		/*
		 * Building a Dendrogram from OO Source Code
		 */

		// Initialiser Spoon
		Launcher spoon = new Launcher();

		// Passer le chemin vers le projet buildModel
		spoon.setArgs(args);
		spoon.buildModel();

		model = spoon.getModel();

		List<CtClass> classes = countTotalClasses(model);

		System.out.println("==============");

		countTotalMethods(model);

		System.out.println("==============");

		countMethodsPerClass(classes);

		System.out.println("==============");

		countTotalMethodsCalls(model);

		NbCall(model);

		/*
		 * Partitioning a Dendrogram to Obtain Microservices
		 */

	}

	/*
	 * je récupère les invocations de méthodes par C2
	 * pour une invocation de méthode par C2
	 * je cherche la classe d'invocation de la méthode par C2
	 * incrémenter de un le nombre d'appel pour cette classe
	 * 
	 */
	
	private static void NbCall(CtModel model) {
		List<CtClass> classes = model.getElements(new TypeFilter<>(CtClass.class));
		Map<ModelClass, Integer> callPerClass = new HashMap<ModelClass, Integer>();
		for (CtClass c1 : classes) {
			int nbCall = 0;
			for (CtClass c2 : classes) {
				
				if (!c1.equals(c2)) {
					
					List<CtInvocation> invocations = c2.getElements(new TypeFilter<>(CtInvocation.class));
//					List<CtMethod> methods1 = c1.getElements(new TypeFilter<>(CtMethod.class));
					CtClass calleeClass;
					CtInterface calleeInterface;
					List<CtClass> classesFromInterfaces;
					
					for (CtInvocation i : invocations) {
				
						boolean invocationIsObject = i.getPosition() == SourcePosition.NOPOSITION;
						if (!invocationIsObject) {
						
							ModelClass aModelClass = new ModelClass();
							try {
								calleeClass = getCalleeClass(i);
								aModelClass.setFullyQualifiedName(calleeClass.getQualifiedName());
								aModelClass.addImplementations(calleeClass);
							} catch (Exception e) {
								try {
									calleeInterface = getCalleeInterface(i);
									classesFromInterfaces = getAllClassFromInterface(calleeInterface);
									aModelClass.setFullyQualifiedName(calleeInterface.getQualifiedName());
									aModelClass.addImplementations(classesFromInterfaces);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
//									e1.printStackTrace();
								}
//								e.printStackTrace();
							}
							nbCall++;
							callPerClass.put(aModelClass, nbCall);
						}
					}
				}
			}
		}
	}

	private static List<CtClass> getAllClassFromInterface(CtInterface calleeInterface) {
		List<CtClass> classes = model.getElements(new TypeFilter(CtClass.class));
		List<CtClass> results = new ArrayList<CtClass>();
		String string = calleeInterface.getQualifiedName();
		for (CtClass c : classes) {
			Set<CtTypeReference<?>> interfacesFromClass = c.getSuperInterfaces();
			for (CtTypeReference<?> i : interfacesFromClass) {
				System.out.println(i);
				String string2 = i.toString();
				boolean equals = string.equals(string2);
				if(equals) {
					results.add(c);
				}
			}
		}

		return results;
	}

	private static List<CtClass> countTotalClasses(CtModel model) {
		// calculer le nombre de classe
		// TODO les interfaces ne sont pas comptabilisées
		List<CtClass> classes = model.getElements(new TypeFilter<>(CtClass.class));
		int counterClass = 0;
		for (CtClass ctClass : classes) {
			counterClass++;
			System.out.println(ctClass.getQualifiedName() + " : " + counterClass);
		}
		return classes;
	}

	private static void countTotalMethods(CtModel model) {
		// calculer le nombre de méthode définie total
		List<CtMethod> methods = model.getElements(new TypeFilter<>(CtMethod.class));
		int counterMethod = 0;
		for (CtMethod m : methods) {
			counterMethod++;
			System.out.println(m.getType() + " /// " + m.getSignature() + " : " + counterMethod);
		}
	}

	private static void countMethodsPerClass(List<CtClass> classes) {
		// calculer le nombre de méthode définie par classe
		// TODO Interfaces non calculés
		for (CtClass c : classes) {
			List<CtMethod> methodsPerClass = c.getElements(new TypeFilter<>(CtMethod.class));
			int counterMethodPerClass = 0;
			for (CtMethod m1 : methodsPerClass) {
				counterMethodPerClass++;
				System.out.println(c.getQualifiedName() + "." + m1.getSignature() + " : " + counterMethodPerClass);
			}
		}
	}

	private static void countTotalMethodsCalls(CtModel model) {
		// calculer le nombre d'appel de méthode total
		List<CtInvocation> invocations = model.getElements(new TypeFilter<>(CtInvocation.class));
		int counterInvocation = 0;
		for (CtInvocation i : invocations) {
			counterInvocation++;
			boolean invocationIsObject = i.getPosition() == SourcePosition.NOPOSITION;
			if (!invocationIsObject) {
				try {
					System.out.println(getCallerClass(i).getSimpleName() + " call  " + getCalleeClass(i).getSimpleName()
							+ "." + getCalleeName(i) + " : " + counterInvocation);
				} catch (Exception e) {
					try {
						System.out.println(
								getCallerClass(i).getSimpleName() + " call  " + getCalleeInterface(i).getSimpleName()
										+ "." + getCalleeName(i) + " : " + counterInvocation);
					} catch (Exception e1) {
						// TODO Auto-generated catch 
//						e1.printStackTrace();
					}
				}
			}
		}
	}

	private static String getCalleeName(CtInvocation i) {
		return i.getExecutable().getSimpleName();
	}

	private static CtClass getCallerClass(CtInvocation i) {
		return i.getParent(CtClass.class);
	}

	private static CtClass getCalleeClass(CtInvocation i) throws Exception {
		// TODO change to fetch the class of the method invocation
		CtTypeReference reference = i.getExecutable().getDeclaringType();
		String referenceName = reference.toString();

		// CtClass matchingClass = model.getElements(new
		// TypeFilter<>(CtClass.class)).stream().filter(c ->
		// c.getDeclaringType().equals(classname)).collect(Collectors.toList()).get(0);

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

	private static CtInterface getCalleeInterface(CtInvocation i) throws Exception {
		// TODO change to fetch the class of the method invocation
		CtTypeReference reference = i.getExecutable().getDeclaringType();
		String referenceName = reference.toString();

		// CtClass matchingClass = model.getElements(new
		// TypeFilter<>(CtClass.class)).stream().filter(c ->
		// c.getDeclaringType().equals(classname)).collect(Collectors.toList()).get(0);

		Predicate<CtInterface> predicateInterface = c -> referenceName
				.equals(c.getParent().toString() + "." + c.getSimpleName());

		Optional<CtInterface> matchingInterface;

		matchingInterface = model.getElements(new TypeFilter<>(CtInterface.class)).stream().filter(predicateInterface)
				.findFirst();
		if (matchingInterface.isEmpty()) {
			throw new Exception("might be a class");
		} else {
			return matchingInterface.get();
		}

	}

}