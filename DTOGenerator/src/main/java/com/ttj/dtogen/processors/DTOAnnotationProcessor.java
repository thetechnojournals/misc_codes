package com.ttj.dtogen.processors;

import com.ttj.dtogen.annotations.DtoClass;
import com.ttj.dtogen.annotations.DtoProperty;
import com.ttj.dtogen.utils.JavaFileBuilder;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.*;
import java.io.File;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

public class DTOAnnotationProcessor extends AbstractProcessor{
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }
    private String getValue(String value, String defaultValue){
        if(value==null || value.length()<1)
            return defaultValue;
        else
            return value;
    }
    private String[] getClassDetails(DtoClass classAnnotation, TypeElement classElem){
        String[] classDetails = new String[2];
        String parentQualifiedClassName = classElem.getQualifiedName().toString();
        String parentClassName = classElem.getSimpleName().toString();
        String parentPackage = parentQualifiedClassName.substring(0, parentQualifiedClassName.indexOf(parentClassName));
        //set package details
        classDetails[0] = getValue(classAnnotation.classPackage(), (parentPackage==null?"":parentPackage)+"dto");
        //set class name
        classDetails[1] = getValue(classAnnotation.name(), parentClassName+"Dto");

        return classDetails;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for(Element element : roundEnv.getElementsAnnotatedWith(DtoClass.class)){
            try {
                DtoClass classAnnotation = element.getAnnotation(DtoClass.class);

                String[] classDetails = getClassDetails(classAnnotation, (TypeElement)element);
                String qualifiedGenClass = classDetails[0]+"."+classDetails[1];
                JavaFileObject javaFileObject = filer.createSourceFile(qualifiedGenClass);

                if(new File(javaFileObject.toUri()).exists()) {
                    continue;
                }

                Writer writer = javaFileObject.openWriter();

                JavaFileBuilder javaFileBuilder = new JavaFileBuilder(classDetails[1], classDetails[0]);

                //iterating through annotated fields
                for(Element fieldElem : element.getEnclosedElements()){
                    DtoProperty propAnnotation = fieldElem.getAnnotation(DtoProperty.class);
                    String fieldName = null;
                    String fieldType = null;
                    boolean isGetter = true, isSetter = true;
                    if(propAnnotation!=null) {
                        fieldName = propAnnotation.name();
                        isGetter = propAnnotation.getter();
                        isSetter = propAnnotation.setter();
                    }
                    if(propAnnotation!=null || classAnnotation.includeAllFields()) {
                        if(fieldElem instanceof VariableElement) {
                            if (fieldName == null || fieldName.length() < 1) {
                                fieldName = fieldElem.getSimpleName().toString();
                            }
                            VariableElement varElem = (VariableElement) fieldElem;
                            fieldType = varElem.asType().toString();
                            messager.printMessage(Diagnostic.Kind.NOTE,
                                    MessageFormat.format("[Class: %s] Processing field[%s] for type[%s]",
                                            qualifiedGenClass, fieldName, fieldType));
                            javaFileBuilder.addField(fieldType, fieldName, isGetter, isSetter);
                        }
                    }
                }
                writer.write(javaFileBuilder.toClassCodeString());
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>();
        annotationTypes.add(DtoClass.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
