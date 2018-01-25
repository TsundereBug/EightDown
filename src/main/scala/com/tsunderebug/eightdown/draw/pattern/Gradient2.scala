package com.tsunderebug.eightdown.draw.pattern

import java.awt.Color
import java.awt.geom.Line2D

import com.tsunderebug.eightdown.transform.Transformable

case class Gradient2(
                     color1: (Double) => Color,
                     color2: (Double) => Color,
                     pos1: (Double) => (Double, Double),
                     pos2: (Double) => (Double, Double),
                     repeat: Boolean = false
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
      val c1 = color1(t)
      val c2 = color2(t)
      val (x1, y1) = pos1(t)
      val (x2, y2) = pos2(t)
      val od = Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)))
      val s = (y2 - y1) / (x2 - x1)
      val ps = -1 / s
      val (x3, y3) = (x, y)
      val d = (x3 + (y3 - y1) * s)/(1 + (s * s))
      val (x4, y4) = ((2 * d) - x3, 2 * d * s - y3 + (2 * y1))
      val (ix, iy) = (((((x1 * y2) - (y1 * x2)) * (x3 - x4)) - ((x1 - x2) * ((x3 * y4) - (y3 * x4)))) / (((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4))), ((((x1 * y2) - (y1 * x2)) * (y3 - y4)) - ((y1 - y2) * ((x3 * y4) - (y3 * x4)))) / (((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4))))
      val d1 = Math.sqrt(((ix - x1) * (ix - x1)) + ((iy - y1) * (iy - y1)))
      val d2 = Math.sqrt(((ix - x2) * (ix - x2)) + ((iy - y2) * (iy - y2)))
      val td = d1 + d2
      val ad = (if (td > od && !repeat) {
        if(d1 > d2) {
          td
        } else {
          0
        }
      } else {
        d1
      }) / td
      if(!ad.isNaN) {
        val gr = ad * c1.getRed + (1 - ad) * c2.getRed
        val gg = ad * c1.getGreen + (1 - ad) * c2.getGreen
        val gb = ad * c1.getBlue + (1 - ad) * c2.getBlue
        Some(new Color(gr.toInt, gg.toInt, gb.toInt))
      } else {
        None
      }
    }
  }


}
