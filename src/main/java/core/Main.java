package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

		while (clusters.size() > 1) {
			// TODO
		}
		System.out.println();
	}

	private static double quality(Cluster cluster, Cluster cluster2) {
		// TODO Auto-generated method stub
		return 0;
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