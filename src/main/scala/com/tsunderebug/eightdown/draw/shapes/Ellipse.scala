package com.tsunderebug.eightdown.draw.shapes

import java.awt.Color

import com.tsunderebug.eightdown.transform.Transformable

case class Ellipse(
                    color: (Double) => Color,
                    pos: (Double) => (Double, Double),
                    w: (Double) => Double,
                    h: (Double) => Double
                  ) extends Transformable {

  /**
    * If the drawable does not contain (x, y) as a point, then this should return None.
    * If the drawable perfectly defines (x, y) as a point, then this should return the Some(color).
    * If the drawable does not perfectly define (x, y) as a point but does define points around it, a weighted average should be chosen.
    *
    * @param x the x relative to the whole image to be retrieved
    * @param y the y relative to the whole image to be retrieved
    * @return an optional color
    */
  override def color(x: Double, y: Double): Double => Option[Color] = {
    (t) => {
      val (ph, pk) = pos(t)
      val rx = w(t) / 2
      val ry = h(t) / 2
      val c = (((x - ph) * (x - ph)) / (rx * rx)) + (((y - pk) * (y - pk)) / (ry * ry))
      if(c <= 1) {
        Some(color(t))
      } else {
        None
      }
    }
  }

}
