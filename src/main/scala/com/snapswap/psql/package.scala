package com.snapswap

import org.reflections.Reflections
import scala.collection.JavaConversions._

package object psql {
  def specs(pkg: String) = new Reflections(pkg).getSubTypesOf(classOf[PostgresSpec]).toSeq.map(_.newInstance)
}
