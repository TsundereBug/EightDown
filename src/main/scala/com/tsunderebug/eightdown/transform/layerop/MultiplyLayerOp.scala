package com.tsunderebug.eightdown.transform.layerop

import java.awt.Color

import com.tsunderebug.eightdown.draw.Drawable
import com.tsunderebug.eightdown.transform.{Transformable, Transformation}

case class MultiplyLayerOp(mask: Drawable) extends Transformation {

  override def transform(tr: Transformable): Transformable = {
    new Transformable {

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
          (tr.color(x, y)(t), mask.color(x, y)(t)) match {
            case (Some(b), Some(f)) =>
              val bfr = b.getRed
              val bfg = b.getGreen
              val bfb = b.getBlue
              val bfa = b.getAlpha
              val ffr = f.getRed / 255.0
              val ffg = f.getGreen / 255.0
              val ffb = f.getBlue / 255.0
              Some(new Color((bfr * ffr).toInt, (bfg * ffg).toInt, (bfb * ffb).toInt, bfa))
            case (Some(b), None) => Some(b)
            case (None, _) => None
          }
        }
      }

    }
  }

}
