package com.github.kotlinpoetdsl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.StringWriter


class KotlinPoetDslTest {

    @Test
    fun `should generate HelloWorld Kotlin file with Greeter class and main function correctly`() {
        val file = kotlinFile("", "HelloWorld") {

            clazz("Greeter") {

                primaryConstructor {
                    parameter("name", String::class)
                }

                property("name", String::class) {
                    initializer("name")
                }

                function("greet") {
                    body {
                        addStatement("println(%P)", "Hello, \$name")
                    }
                }
            }

            function("main") {
                parameter("args", String::class) {
                    addModifiers(KModifier.VARARG)
                }

                body {
                    addStatement("%T(args[0]).greet()", ClassName("", "Greeter"))
                }
            }

        }

        val generatedCode = StringWriter()
        file.writeTo(generatedCode)

        Assertions.assertEquals("import kotlin.String\n" +
                "\n" +
                "class Greeter(\n" +
                "  val name: String\n" +
                ") {\n" +
                "  fun greet() {\n" +
                "    println(\"\"\"Hello, \$name\"\"\")\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "fun main(vararg args: String) {\n" +
                "  Greeter(args[0]).greet()\n" +
                "}\n", generatedCode.toString())
    }

    @Test
    fun `should generate test method correctly`() {
        val testMethod = kotlinFunction("test string equality") {
            annotation(Test::class)
            body {
                addStatement("assertThat(%1S).isEqualTo(%1S)", "foo")
            }
        }

        Assertions.assertEquals("@org.junit.jupiter.api.Test\n" +
                "fun `test string equality`() {\n" +
                "  assertThat(\"foo\").isEqualTo(\"foo\")\n" +
                "}\n", testMethod.toString())
    }
}