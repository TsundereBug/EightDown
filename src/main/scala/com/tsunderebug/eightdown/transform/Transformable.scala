package com.tsunderebug.eightdown.transform

import com.tsunderebug.eightdown.draw.Drawable

trait Transformable extends Drawable {

  def transform(t: Transformation): Transformable = t.transform(this)

}
