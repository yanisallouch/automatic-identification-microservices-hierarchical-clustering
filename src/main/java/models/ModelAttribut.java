package models;

import java.util.List;

import spoon.reflect.declaration.CtClass;

public class ModelAttribut {
	String fullyQualifiedName;
	List<ModelClass> writingModelClass;
	List<ModelClass> readingModelClass;

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public List<ModelClass> getWritingModelClass() {
		return writingModelClass;
	}

	public void setWritingModelClass(List<ModelClass> writingModelClass) {
		this.writingModelClass = writingModelClass;
	}

	public List<ModelClass> getReadingModelClass() {
		return readingModelClass;
	}

	public void setReadingModelClass(List<ModelClass> readingModelClass) {
		this.readingModelClass = readingModelClass;
	}

	@Override
	public String toString() {
		return this.fullyQualifiedName;
	}

	public static List<ModelAttribut> makeModelAttributs(CtClass ctClass) {
		// TODO implementation
		return null;
	}
}
