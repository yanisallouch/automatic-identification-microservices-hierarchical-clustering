package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import models.Cluster;
import processors.AnalysisProcessor;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class Main {

	protected static CtModel model;
	protected static int alpha;
	protected static int beta;
	protected static int n;
	protected static int totalNbCallsInApp;
	protected static int totalNbLiensTotal;
	protected static int totalNbClasses;

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

		List<Pair<Cluster, Cluster>> pairsClusters = createPairsClusters(clusters);
		while (clusters.size() > 1) {
			Double maxQuality = 0D;
			Pair<Cluster, Cluster> bestPairCluster = pairsClusters.get(0);
			for (Pair<Cluster, Cluster> pairCluster : pairsClusters) {
				Double quality = quality(pairCluster);
				if (maxQuality < quality) {
					bestPairCluster = pairCluster;
				}
			}
			Cluster mergeCluster = new Cluster(bestPairCluster.getRight(),bestPairCluster.getLeft());
			clusters.remove(bestPairCluster.getLeft());
			clusters.remove(bestPairCluster.getRight());
			clusters.add(mergeCluster);
			pairsClusters = createPairsClusters(clusters);
		}
		System.out.println();
	}

	private static Double quality(Pair<Cluster, Cluster> pc) {
		// TODO Auto-generated method stub
		return null;
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