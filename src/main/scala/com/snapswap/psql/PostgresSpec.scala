package com.snapswap.psql

import org.scalatest.{BeforeAndAfterAll, Suite}

trait PostgresSpec extends TemplateAware with Suite with BeforeAndAfterAll {
  require(
    {
      val className = this.getClass.getName
      className.endsWith("DaoSpec") && !className.endsWith("DaoSuites")
    },
    s"Spec that use embeded PostgreSQL name must be ended with DaoSpec suffix, for correct test execution, but found '${this.getClass.getName}'"
  )

  override def afterAll =
    if (template.openCount == 1)
      template.close()
}
