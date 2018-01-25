package com.tsunderebug.eightdown

import java.awt.Color
import java.io.File
import java.util.concurrent.TimeUnit

import com.tsunderebug.eightdown.draw.{Drawable, Group}
import com.tsunderebug.eightdown.rendering.{HighQualityGIFRenderingBackend, RenderingBackend}
import com.tsunderebug.eightdown.transform.Transformable

class Animation(val groups: Transformable*) extends Group(groups:_*) {

  def renderTo(w: Int, h: Int, f: File, framerate: Int, totalTime: Long, unit: TimeUnit)(implicit renderingBackend: RenderingBackend = HighQualityGIFRenderingBackend(Color.WHITE)): Unit = {
    renderingBackend.render(w, h, f, this, framerate, totalTime, unit)
  }

}

object Animation {

  def apply(groups: Transformable*): Animation = new Animation(groups:_*)

  def unapplySeq(arg: Animation): Option[Seq[Transformable]] = Some(arg.groups)

}