package com.aaron.compiler.factory;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
final class FactoryGroupedClasses {

    private static final String SUFFIX = "Factory";

    String qualifiedClassName;
    private Map<String, FactoryAnnotatedClass> items = new LinkedHashMap<>();

    FactoryGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    void add(FactoryAnnotatedClass toInsert) throws IdAlreadyUsedException {
        FactoryAnnotatedClass existing = items.get(toInsert.id);
        if (existing != null) {
            throw new IdAlreadyUsedException(existing);
        }
        items.put(toInsert.id, toInsert);
    }

    void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(qualifiedClassName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;
        String pkgName = elementUtils.getPackageOf(superClassName).getQualifiedName().toString();

        ParameterSpec parameterSpec = ParameterSpec.builder(String.class, "id", Modifier.FINAL)
                .build();
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .beginControlFlow("if ($N == null)", parameterSpec)
                .addStatement("throw new $T($S)", IllegalArgumentException.class, "id is null!")
                .endControlFlow()
                .beginControlFlow("switch ($N)", parameterSpec);
        for (FactoryAnnotatedClass item : items.values()) {
            codeBuilder.add("case $S: ", item.id)
                    .add("return new $T();\n", ClassName.get(item.annotatedClassElement));
        }
        CodeBlock code = codeBuilder.add("")
                .endControlFlow()
                .addStatement("throw new $T($S)", IllegalArgumentException.class, "id does not exist!")
                .build();
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();
        MethodSpec create = MethodSpec.methodBuilder("create")
                .returns(ClassName.get(superClassName))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterSpec)
                .addCode(code)
                .build();
        TypeSpec factory = TypeSpec.classBuilder(factoryClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(create)
                .addMethod(constructor)
                .build();
        JavaFile javaFile = JavaFile.builder(pkgName, factory)
                .addFileComment("Generate automatically, do not edit.")
                .indent("    ")
                .build();
        javaFile.writeTo(filer);
    }
}
