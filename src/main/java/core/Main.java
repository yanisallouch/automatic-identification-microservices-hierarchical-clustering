package core;

import java.util.List;

import processors.AnalysisProcessor;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.filter.AbstractFilter;

public class Main {

	protected static CtModel model;

	public static void main(String[] args) {

		/*
		 * Building a Dendrogram from OO Source Code
		 */

		Launcher spoon = new Launcher();

		spoon.setArgs(args);
		spoon.run();

		model = spoon.getModel();

		CtPackage rootPackage = model.getRootPackage();

		List<Object> methods = rootPackage.filterChildren(new AbstractFilter<CtElement>(CtElement.class) {
			@Override
			public boolean matches(CtElement element) {
				return element instanceof CtClass || element instanceof CtInterface;
			}
		}).map((CtElement clazz) -> clazz.getValueByRole(CtRole.METHOD))
				.map((CtMethod<?> method) -> method.getSimpleName()).list();

		for (Object object : methods) {
			System.out.println("==========");
			System.out.println(object);
			System.out.println("==========");
		}
	}
}