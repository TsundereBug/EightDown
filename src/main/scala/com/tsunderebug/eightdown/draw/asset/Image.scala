package com.tsunderebug.eightdown.draw.asset

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File

import com.tsunderebug.eightdown.draw.Drawable
import com.tsunderebug.eightdown.transform.Transformable

case class Image(i: BufferedImage, pos: (Double) => (Double, Double), w: (Double) => Double, h: (Double) => Double) extends Transformable {

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
      val (tx, ty) = pos(t)
      val tw = w(t)
      val th = h(t)
      val nx = x - tx
      val ny = y - ty
      val sx = nx * (i.getWidth / tw)
      val sy = ny * (i.getHeight / th)
      val ax = sx + (i.getWidth / 2)
      val ay = sy + (i.getHeight / 2)
      if(ax >= 0 && ax < i.getWidth && ay >= 0 && ay < i.getHeight) {
        val ca: Array[Int] = Array[Int](0, 0, 0, 0)
        val ci = i.getRaster.getPixel(ax.toInt, ay.toInt, ca)
        val c = new Color(ci(0), ci(1), ci(2), ci(3))
        if(c.getAlpha == 0) {
          None
        } else {
          Some(c)
        }
      } else {
        None
      }
    }
  }

}
