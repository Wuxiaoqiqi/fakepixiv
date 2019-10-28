// Top-level build file where you can add configuration options common to all sub-projects/modules.
//apply {
//    from("${rootProject.rootDir.absolutePath}/config.gradle")
//}
//
//apply {
//    from("${rootProject.rootDir.absolutePath}/utils.gradle.kts")
//}

buildscript {
    val gradleVer by extra("3.2.1")
    repositories {
        //maven{ url 'http://maven.aliyun.com/nexus/content/repositories/central/'}
        maven ("https://jitpack.io")
        jcenter()
        mavenCentral()
        google()
        //maven { url 'https://dl.bintary.com/objectbox/objectbox' }
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:$gradleVer")
        classpath (kotlin("gradle-plugin", Vers.kotlin_version))
        classpath (kotlin("android-extensions", Vers.kotlin_version))
        //classpath ("com.tencent.tinker:tinker-patch-gradle-plugin:${Vers.tinker_version}")

    }
}

allprojects {
    repositories {
        //maven{ url 'http://maven.aliyun.com/nexus/content/repositories/central/'}
        maven ("https://jitpack.io" )
        jcenter()
        mavenCentral()
        google()
        maven ("https://maven.google.com" )
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
