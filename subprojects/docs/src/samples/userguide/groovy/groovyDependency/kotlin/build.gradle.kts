plugins {
    groovy
}

repositories {
    mavenCentral()
}

// tag::groovy-test-dependency[]
dependencies {
    testImplementation("org.codehaus.groovy:groovy:2.5.7")
}
// end::groovy-test-dependency[]

// tag::bundled-groovy-dependency[]
dependencies {
    implementation(localGroovy())
}
// end::bundled-groovy-dependency[]
