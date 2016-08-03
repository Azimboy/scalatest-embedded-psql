package com.snapswap.psql

private[psql] trait TemplateAware {
  def template: EmbeddedPostgresTemplate[_]
}
