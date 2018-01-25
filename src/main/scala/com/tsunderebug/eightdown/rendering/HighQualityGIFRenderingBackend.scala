package com.tsunderebug.eightdown.rendering
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, FileOutputStream}
import java.util.concurrent.TimeUnit

import com.madgag.gif.fmsware.AnimatedGifEncoder
import com.tsunderebug.eightdown.draw.Drawable

case class HighQualityGIFRenderingBackend(background: Color, transparent: Boolean = false) extends RenderingBackend {

  override def render(w: Int, h: Int, f: File, d: Drawable, framerate: Int, totalTime: Long, unit: TimeUnit): Unit = {
    val nf = unit.toSeconds(totalTime * framerate)
    val os = new FileOutputStream(f)
    val e = new AnimatedGifEncoder
    e.setBackground(background)
    if(transparent) {
      e.setTransparent(background)
    }
    e.setQuality(1)
    e.setRepeat(0)
    e.start(os)
    e.setFrameRate(framerate)
    val df = d.image(w, h)
    (0.0 until nf.toDouble by 1.0).map(_ / nf).foreach((t) => e.addFrame({
      val i = df(t)
      val ni = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
      val ng = ni.getGraphics
      ng.setColor(background)
      ng.fillRect(0, 0, w, h)
      ng.setColor(Color.white)
      ng.drawImage(i, 0, 0, w, h, null)
      ni
    }))
    e.finish()
    os.close()
  }

}
