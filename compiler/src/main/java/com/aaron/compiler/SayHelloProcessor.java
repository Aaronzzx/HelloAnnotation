package com.aaron.compiler;

import com.aaron.annotation.SayHello;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@AutoService(Processor.class)
public final class SayHelloProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(SayHello.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(SayHello.class)) {
            String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String clazzName = element.getSimpleName().toString();
            SayHello annotation = element.getAnnotation(SayHello.class);
            String text = annotation.value();
            try {
                generate(pkgName, clazzName, text);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Failed!");
            }
        }
        return true;
    }

    private void generate(String pkgName, String clazzName, String text) throws IOException {
        ClassName annotation = ClassName.get("androidx.annotation", "NonNull");
        ParameterSpec parameterSpec = ParameterSpec.builder(String.class, "what")
                .addAnnotation(annotation)
                .build();
        MethodSpec main = MethodSpec.methodBuilder("say")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterSpec)
                .addCode("// Generate automatically.\n")
                .addStatement("$T.out.println($S)", System.class, text)
                .addStatement("$T.out.println($N)", System.class, parameterSpec)
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder(String.format("Hello%s", clazzName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
        JavaFile javaFile = JavaFile.builder(pkgName, typeSpec)
                .addFileComment("Generate automatically.")
                .indent("    ")
                .build();
        javaFile.writeTo(filer);
    }
}
