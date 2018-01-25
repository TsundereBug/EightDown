package com.tsunderebug.eightdown.draw

import java.awt.Color
import java.awt.image.BufferedImage

/**
  * All units should be fractional ranging [0.0, 1.0) to fit inside the image.
  * All return values are functions due to needing time as an input.
  * It is important that these method implementations or functions do not cause side effects, as that may produce unwanted rendering
  */
trait Drawable {

  /**
    * If the drawable does not contain (x, y) as a point, then this should return None.
    * If the drawable perfectly defines (x, y) as a point, then this should return the Some(color).
    * If the drawable does not perfectly define (x, y) as a point but does define points around it, a weighted average should be chosen.
    * @param x the x relative to the whole image to be retrieved
    * @param y the y relative to the whole image to be retrieved
    * @return an optional color
    */
  def color(x: Double, y: Double): (Double) => Option[Color]

  /**
    * Generates an image for a time.
    * Square resolutions work best.
    * @param w width of output
    * @param h height of output
    * @return function to generate an image based on time [0.0, 1.0)
    */
  def image(w: Int, h: Int): (Double) => BufferedImage = {
    val pr = (0 until w).flatMap((dw) => (0 until h).map((dw, _)))
    (t) => {
      val bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
      val r = bi.getRaster
      pr.foreach {
        case (x, y) =>
          r.setPixel(x, y, color(x.toDouble / w.toDouble, y.toDouble / h.toDouble)(t) match {
            case Some(c) => Array(c.getRed, c.getGreen, c.getBlue, c.getAlpha)
            case None => Array(0, 0, 0, 0)
          })
      }
      bi.setData(r)
      bi
    }
  }

}
