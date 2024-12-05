import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))

    implementation("io.netty:netty-buffer:4.1.113.Final")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}