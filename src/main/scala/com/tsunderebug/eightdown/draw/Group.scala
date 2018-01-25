package com.tsunderebug.eightdown.draw

import java.awt.Color

import com.tsunderebug.eightdown.transform.Transformable

case class Group(items: Transformable*) extends Transformable {

  /**
    * If the drawable does not contain (x, y) as a point, then this should return None.
    * If the drawable perfectly defines (x, y) as a point, then this should return the Some(color).
    * If the drawable does not perfectly define (x, y) as a point but does define points around it, a weighted average should be chosen.
    *
    * @param x the x relative to the whole image to be retrieved
    * @param y the y relative to the whole image to be retrieved
    * @return an optional color
    */
  override def color(x: Double, y: Double): Double => Option[Color] = (t) =>
    items.map(_.color(x, y)).foldLeft[Option[Color]](None)((o, f) => o match {
      case None => f(t)
      case Some(w) =>
        f(t) match {
          case None => Some(w)
          case Some(a) =>
            val srcs = Seq(a.getAlpha, a.getRed, a.getGreen, a.getBlue).map(_ / 255.0)
            val dsts = Seq(w.getAlpha, w.getRed, w.getGreen, w.getBlue).map(_ / 255.0)
            val zips = srcs.zip(dsts)
            val (srca, dsta) = zips.head
            val rgbt = zips.tail
            val na = srca + (dsta * (1 - srca))
            val Seq(nr, ng, nb) = rgbt.map((ct) => ((ct._1 * srca) + (ct._2 * dsta * (1 - srca))) / na)
            Some(new Color(nr.toFloat, ng.toFloat, nb.toFloat, na.toFloat))
        }
    })

}
