package com.tsunderebug.eightdown.draw.asset

import java.awt.Color

import com.madgag.gif.fmsware.GifDecoder
import com.tsunderebug.eightdown.transform.Transformable

case class AnimatedGIF(
                        gd: GifDecoder,
                        frame: (Double, Map[Int, Int]) => Int,
                        pos: (Double) => (Double, Double),
                        w: (Double) => Double,
                        h: (Double) => Double
                      ) extends Transformable {

  val fmap = (0 until gd.getFrameCount).map((f) => (f, gd.getDelay(f))).toMap
  val imap = (0 until gd.getFrameCount).map((f) => (f, Image(gd.getFrame(f), pos, w, h))).toMap

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
      val i = frame(t, fmap)
      imap(i).color(x, y)(t)
    }
  }

}
