package com.tsunderebug.eightdown.transform
import java.awt.Color

/**
  * @param rot rotation in Radians
  * @param center center of the rotation
  * @param startTime starting time [0.0, 1.0) of the rotation
  * @param endTime ending time [0.0, 1.0) of the rotation
  */
case class Rotation(rot: Double, center: (Double) => (Double, Double) = (_) => (0.0, 0.0), startTime: Double = 0.0, endTime: Option[Double] = None) extends Transformation {

  override def transform(t: Transformable): Transformable = (x: Double, y: Double) => {
    (time) => {
      if(time < startTime) {
        t.color(x, y)(time)
      } else if(time >= startTime) {
        val angle: Double = -(endTime match {
          case Some(end) => if(time <= end) { rot * (time / (end - startTime)) } else { rot }
          case None => rot
        })
        val s = Math.sin(angle)
        val c = Math.cos(angle)
        val nx = x - center(time)._1
        val ny = y - center(time)._2
        val tx = nx * c - ny * s
        val ty = nx * s + ny * c
        val ox = tx + center(time)._1
        val oy = ty + center(time)._2
        t.color(ox, oy)(time)
      } else {
        None
      }
    }
  }

}
