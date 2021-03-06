plugins {
    scala
}

repositories {
    mavenCentral()
}

// tag::all-dependency[]
configurations.all {
    resolutionStrategy.force("org.scala-lang:scala-library:2.11.12")
}
// end::all-dependency[]

// tag::zinc-dependency[]
configurations.zinc.apply {
    resolutionStrategy.force("org.scala-lang:scala-library:2.10.5")
}
// end::zinc-dependency[]
