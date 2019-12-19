package com.aaron.compiler.factory;

import com.aaron.annotation.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
final class FactoryAnnotatedClass {

    TypeElement annotatedClassElement;
    String qualifiedSuperClassName;
    String simpleTypeName;
    String id;

    FactoryAnnotatedClass(TypeElement annotatedClassElement) throws IllegalArgumentException {
        this.annotatedClassElement = annotatedClassElement;
        Factory annotation = annotatedClassElement.getAnnotation(Factory.class);
        id = annotation.id();
        if (id.length() == 0) {
            throw new IllegalArgumentException(String.format("id() in @%s for class %s is null or empty! that's not allowed",
                    Factory.class.getSimpleName(), annotatedClassElement.getQualifiedName().toString()));
        }
        try {
            Class<?> clazz = annotation.type();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }
}
