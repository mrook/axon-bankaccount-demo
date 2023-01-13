package org.demo.projections

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RebuildableProjection(val version: String = "", val rebuild: Boolean = false)
