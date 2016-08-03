package com.snapswap.psql

import org.scalatest.{BeforeAndAfterAll, Suites}

abstract class DaoSuites[T](pkg: String) extends Suites(specs(pkg): _*) with TemplateAware with BeforeAndAfterAll {
  override def afterAll() = template.close()
}