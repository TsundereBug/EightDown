package com.tsunderebug.eightdown.transform

trait Transformation {

  def transform(t: Transformable): Transformable

}
