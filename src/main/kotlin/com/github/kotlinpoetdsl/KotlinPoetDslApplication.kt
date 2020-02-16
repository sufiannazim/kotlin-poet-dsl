package com.github.kotlinpoetdsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinPoetDslApplication

fun main(args: Array<String>) {
	runApplication<KotlinPoetDslApplication>(*args)
}
