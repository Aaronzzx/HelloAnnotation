package com.aaron.compiler.factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
class IdAlreadyUsedException extends Exception {

    FactoryAnnotatedClass factoryAnnotatedClass;

    IdAlreadyUsedException(FactoryAnnotatedClass factoryAnnotatedClass) {
        this.factoryAnnotatedClass = factoryAnnotatedClass;
    }
}
