package com.github.kotlinpoetdsl

import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

class FileBuilder(packageName: String, fileName: String) {

    private val file: FileSpec.Builder = FileSpec.builder(packageName, fileName)

    fun clazz(name: String, block: ClazzBuilder.() -> Unit) {
        file.addType(ClazzBuilder(TypeSpec.classBuilder(name)).apply(block).build())
    }

    fun function(name: String, block: FunctionBuilder.() -> Unit) {
        file.addFunction(FunctionBuilder(FunSpec.builder(name)).apply(block).build())
    }

    fun build(): FileSpec {
        return file.build()
    }
}

class ClazzBuilder(private val clazz: TypeSpec.Builder) {

    fun primaryConstructor(block: FunctionBuilder.() -> Unit) {
        clazz.primaryConstructor(FunctionBuilder(FunSpec.constructorBuilder()).apply(block).build())
    }

    fun annotation(type: KClass<*>, block: AnnotationSpec.Builder.() -> Unit) {
        clazz.addAnnotation(AnnotationSpec.builder(type.asClassName()).apply(block).build())
    }

    fun property(name: String, type: KClass<*>, block: PropertySpec.Builder.() -> Unit = {}) {
        clazz.addProperty(PropertySpec.builder(name, type).apply(block).build())
    }

    fun function(name: String, block: FunctionBuilder.() -> Unit) {
        clazz.addFunction(FunctionBuilder(FunSpec.builder(name)).apply(block).build())
    }

    fun build(): TypeSpec {
        return clazz.build()
    }
}

class FunctionBuilder(private val function: FunSpec.Builder) {

    fun parameter(name: String, type: KClass<*>, block: ParameterSpec.Builder.() -> Unit = {}) {
        function.addParameter(ParameterSpec.builder(name, type).apply(block).build())
    }

    fun body(block: CodeBlock.Builder.() -> Unit) {
        function.addCode(CodeBlock.builder().apply(block).build())
    }

    fun annotation(type: KClass<*>) {
        function.addAnnotation(type)
    }

    fun build(): FunSpec {
        return function.build()
    }
}

@DslMarker
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE)
annotation class KotlinPoetDsl

fun kotlinFile(packageName: String, fileName: String, block: (@KotlinPoetDsl FileBuilder).() -> Unit): FileSpec {
    return FileBuilder(packageName, fileName).apply(block).build()
}

fun kotlinClass(name: String, block: (@KotlinPoetDsl ClazzBuilder).() -> Unit) : TypeSpec {
    return ClazzBuilder(TypeSpec.classBuilder(name)).apply(block).build()
}

fun kotlinFunction(name: String, block: (@KotlinPoetDsl FunctionBuilder).() -> Unit) : FunSpec {
    return FunctionBuilder(FunSpec.builder(name)).apply(block).build()
}