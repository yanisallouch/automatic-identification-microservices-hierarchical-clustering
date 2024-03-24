package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import models.Cluster;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtFieldAccessImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtVariableReadImpl;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class Main {

	protected static CtModel model;
	protected static int alpha;
	protected static int beta;
	protected static int n;
	protected static int totalNbCallsInApp;
	protected static int totalNbLiensTotal;
	protected static int totalNbClasses;
	protected static List<Pair<Cluster, Cluster>> pairsClusters;

	public static void main(String[] args) {

		/*
		 * Building a Dendrogram from OO Source Code
		 */

		Launcher spoon = new Launcher();

		spoon.setArgs(args);
		spoon.run();

		model = spoon.getModel();

		alpha = 1;
		beta = 1;
		n = alpha + beta;
		Collection<CtType<?>> allClassesInterfaces = model.getAllTypes();
		totalNbClasses = allClassesInterfaces.size();
		totalNbCallsInApp = getTotalNbCallsInApp(allClassesInterfaces);

		List<Cluster> clusters = new ArrayList<Cluster>();
		for (CtType<?> ctType : allClassesInterfaces) {
			clusters.add(new Cluster(ctType));
		}

		pairsClusters = createPairsClusters(clusters);
		while (clusters.size() > 1) {
			Double maxQuality = 0D;
			Pair<Cluster, Cluster> bestPairCluster = pairsClusters.get(0);
			for (Pair<Cluster, Cluster> pairCluster : pairsClusters) {
				Double quality = quality(pairCluster);
				if (maxQuality < quality) {
					bestPairCluster = pairCluster;
				}
			}
			Cluster mergeCluster = new Cluster(bestPairCluster.getRight(), bestPairCluster.getLeft());
			clusters.remove(bestPairCluster.getLeft());
			clusters.remove(bestPairCluster.getRight());
			clusters.add(mergeCluster);
			pairsClusters = createPairsClusters(clusters);
		}
		System.out.println();
	}

	private static Double quality(Pair<Cluster, Cluster> pc) {
		Cluster microservice = new Cluster(pc);
		return fMicroservice(microservice);
	}

	private static Double fMicroservice(Cluster microservice) {
		return Double.valueOf(((1 / n) * ((alpha * fStructureBehavior(microservice)) - (beta * fData(microservice)))));
	}

	private static Double fStructureBehavior(Cluster microservice) {
		return Double.valueOf(((1 / n) * ((alpha * fOne(microservice)) - (beta * fAutonomy(microservice)))));
	}

	private static Double fOne(Cluster microservice) {
		return Double.valueOf(((1 / n) * (internalCoupling(microservice) + internalCohesion(microservice))));
	}

	@SuppressWarnings("rawtypes")
	private static Double internalCoupling(Cluster microservice) {
		Double result = Double.valueOf(0D);
		Double sum = Double.valueOf(0D);
		Double mean = Double.valueOf(0D);
		List<Pair<CtType, CtType>> pairsClasses = createPairsClasse(microservice);
		int length = pairsClasses.size();
		Double sumStandDeviation = Double.valueOf(0D);

		for (CtType c1 : microservice.getClasses()) {
			for (CtType c2 : microservice.getClasses()) {
				if (!c1.equals(c2)) {
					sum += couplingPair(c1, c2);
				}
			}
		}
		mean = sum / (length * 2);

		for (CtType c1 : microservice.getClasses()) {
			for (CtType c2 : microservice.getClasses()) {
				if (!c1.equals(c2)) {
					sumStandDeviation += Math.pow(couplingPair(c1, c2) - mean, 2);
				}
			}
		}

		result = sum - Math.sqrt(sumStandDeviation / length);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static Double couplingPair(CtType c1, CtType c2) {
		Double result = 0D;
		result = Double.valueOf((double) ((nbCalls(c1, c2) + nbCalls(c2, c1)) / totalNbCallsInApp));

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int nbCalls(CtType c1, CtType c2) {
		int result = 0;
		if (!c1.equals(c2)) {
			Set<CtMethod<?>> methods1 = c1.getMethods();
			Set<CtMethod<?>> methods2 = c2.getMethods();
			for (CtMethod m1 : methods1) {
				for (CtMethod m2 : methods2) {
					if (invocationOf(m1, m2)) {
						result += 1;
					}
				}

			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static boolean invocationOf(CtMethod m1, CtMethod m2) {
		boolean result = false;
		List<CtInvocation> invocations = getInvocations(m2);
		for (CtInvocation invocation : invocations) {
			if (getTypeInvocation(invocation).equals(getTypeMethod(m1))) {
				result = true;
				continue;
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static String getTypeMethod(CtMethod m1) {
		String result = "b";
		if (m1 != null) {
			CtElement foo = m1.getParent();
			if (foo != null) {
				CtElement bar = foo.getParent();
				if (bar != null) {
					result = bar.toString().concat(".").concat(((CtNamedElement) foo).getSimpleName());
				}
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private static String getTypeInvocation(CtInvocation invocation) {
		Set<CtTypeReference<?>> foo = invocation.getTarget().getReferencedTypes();
		String result = "a";
		Optional<CtTypeReference<?>> bar = foo.stream().findFirst();
		if (bar.isPresent()) {
			result = bar.get().toString();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static List<CtInvocation> getInvocations(CtMethod m2) {
		List<CtStatement> stmts = m2.getBody().getStatements().stream()
				.filter(s -> s.getClass().equals(CtInvocationImpl.class)).toList();
		List<CtInvocation> invocations = new ArrayList<>();
		for (CtStatement stmt : stmts) {
			invocations.add((CtInvocation) stmt);
		}
		return invocations;
	}

	private static Double internalCohesion(Cluster microservice) {
		return nbLiensExistant(microservice) / totalNbLiensTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Double nbLiensExistant(Cluster microservice) {
		Double result = 0D;
		for (CtType c1 : microservice.getClasses()) {
			Set<CtMethod<?>> methods1 = c1.getMethods();
			for (CtType c2 : microservice.getClasses()) {
				if (!c1.equals(c2)) {
					Set<CtMethod<?>> methods2 = c2.getMethods();
					for (CtMethod m1 : methods1) {
						for (CtMethod m2 : methods2) {
							if (accessSameAttribut(m1, m2)) {
								result += 1;
							}
						}
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static boolean accessSameAttribut(CtMethod m1, CtMethod m2) {
		boolean result = false;
		CtBlock b1 = m1.getBody();
		CtBlock b2 = m2.getBody();
		if (b1 == null || b2 == null) {
			return result;
		}
		for (CtStatement stmt1 : b1.getStatements()) {
			for (CtStatement stmt2 : b2.getStatements()) {
				List<CtElement> childs1 = stmt1.getDirectChildren();
				for (CtElement e1 : childs1) {
					if (e1.getClass().equals(CtVariableReadImpl.class)
							|| e1.getClass().equals(CtVariableWriteImpl.class)
							|| e1.getClass().equals(CtFieldAccessImpl.class)) {
						List<CtElement> childs2 = stmt2.getDirectChildren();
						for (CtElement e2 : childs2) {
							if (e2.getClass().equals(CtVariableReadImpl.class)
									|| e2.getClass().equals(CtVariableWriteImpl.class)
									|| e2.getClass().equals(CtFieldAccessImpl.class)) {
								List<CtElement> childsAttribute = e1.getDirectChildren();
								for (CtElement attributeElement1 : childsAttribute) {
									Set<CtTypeReference<?>> rae1 = attributeElement1.getReferencedTypes();
									for (CtTypeReference tr1 : rae1) {
										List<CtElement> childsAttribute2 = e2.getDirectChildren();
										for (CtElement attributeElement2 : childsAttribute2) {
											Set<CtTypeReference<?>> rae2 = attributeElement2.getReferencedTypes();
											for (CtTypeReference tr2 : rae2) {
												if (tr1.toString().equals(tr2.toString())) {
													result = true;
													return result;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static Double fAutonomy(Cluster microservice) {
		return externalCoupling(microservice);
	}

	@SuppressWarnings("rawtypes")
	private static Double externalCoupling(Cluster microservice) {
		Double result = Double.valueOf(0D);
		Double sum = Double.valueOf(0D);
		Double mean = Double.valueOf(0D);
		// TODO
		List<Pair<CtType, CtType>> pairsClasses = createPairsClasse(microservice);
		int length = getNbExternalPairs(pairsClasses);
		Double sumStandDeviation = Double.valueOf(0D);

		for (CtType c1 : microservice.getClasses()) {
			for (CtType c2 : microservice.getClasses()) {
				if (!c1.equals(c2)) {
					sum += couplingPair(c1, c2);
				}
			}
		}
		mean = sum / (length * 2);

		for (CtType c1 : microservice.getClasses()) {
			for (CtType c2 : microservice.getClasses()) {
				if (!c1.equals(c2)) {
					sumStandDeviation += Math.pow(couplingPair(c1, c2) - mean, 2);
				}
			}
		}

		result = sum - Math.sqrt(sumStandDeviation / length);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static int getNbExternalPairs(List<Pair<CtType, CtType>> pairsClasses) {
		int result = 0;
		for (Pair<Cluster, Cluster> cluster : pairsClusters) {
			for (Pair<CtType, CtType> pClasse : pairsClasses) {
				if ((cluster.getRight().getClasses().contains(pClasse.getRight())
						|| cluster.getRight().getClasses().contains(pClasse.getLeft()))
						|| (cluster.getLeft().getClasses().contains(pClasse.getRight())
								|| cluster.getLeft().getClasses().contains(pClasse.getLeft()))) {
					result += 1;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static List<Pair<CtType, CtType>> createPairsClasse(Cluster microservice) {
		List<Pair<CtType, CtType>> results = new ArrayList<>();
		List<CtType> classes = microservice.getClasses();
		for (CtType c1 : classes) {
			for (CtType c2 : classes) {
				if (!c1.equals(c2)) {
					results.add(Pair.of(c1, c2));
				}
			}
		}

		return results;
	}

	private static Double fData(Cluster microservice) {
		// TODO Auto-generated method stub
		return 0D;
	}

	private static List<Pair<Cluster, Cluster>> createPairsClusters(List<Cluster> clusters) {
		List<Pair<Cluster, Cluster>> results = new ArrayList<>();
		for (Cluster c1 : clusters) {
			for (Cluster c2 : clusters) {
				results.add(Pair.of(c1, c2));
			}
		}

		return results;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int getTotalNbCallsInApp(Collection<CtType<?>> allTypes) {
		int total = 0;
		for (CtType<?> ctType : allTypes) {
			Set<CtMethod<?>> methods = ctType.getAllMethods();
			for (CtMethod<?> ctMethod : methods) {
				List<CtInvocation> invocations = ctMethod.getElements(new TypeFilter(CtInvocation.class));
				total += invocations.size();
			}
		}
		return total;
	}
}