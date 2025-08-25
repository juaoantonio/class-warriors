plugins {
    java
    jacoco
    application
    id("com.diffplug.spotless") version "6.25.0"
    id("io.freefair.lombok") version "8.14"
}

group = "br.dev.joaobarbosa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("br.dev.joaobarbosa.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.19.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

val jacocoCoverageVerification by tasks.registering(JacocoCoverageVerification::class) {
    dependsOn(tasks.jacocoTestReport)

    classDirectories.setFrom(
        fileTree("${layout.buildDirectory}/classes/java/main") {
            // Exemplo: exclui classes utilitárias ou modelos se necessário
            // exclude("**/dto/**", "**/config/**")
        }
    )

    executionData.setFrom(file("${layout.buildDirectory}/jacoco/test.exec"))

    violationRules {
        rule {
            element = "CLASS"
            limit {
                minimum = "0.80".toBigDecimal() // 80% obrigatório
            }
        }
    }
}

tasks.check {
    dependsOn(jacocoCoverageVerification)
}

spotless {
    java {
        googleJavaFormat() // Usa o padrão oficial do Google
        target("src/**/*.java") // Formata todos os arquivos .java
    }
}
