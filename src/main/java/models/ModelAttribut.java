package models;

import java.util.List;

import spoon.reflect.declaration.CtClass;

public class ModelAttribut {
	String fullyQualifiedName;
	List<ModelClass> writingModelClass;
	List<ModelClass> readingModelClass;
	public static List<ModelAttribut> makeModelAttributs(CtClass ctClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
