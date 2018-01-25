package com.tsunderebug.eightdowntesting.memes

import java.awt.Color
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

import com.madgag.gif.fmsware.GifDecoder
import com.tsunderebug.eightdown.Animation
import com.tsunderebug.eightdown.draw.Group
import com.tsunderebug.eightdown.draw.asset.{AnimatedGIF, Image}
import com.tsunderebug.eightdown.draw.pattern.Gradient2
import com.tsunderebug.eightdown.draw.shapes.{Ellipse, Rectangle}
import com.tsunderebug.eightdown.rendering.{HighQualityGIFRenderingBackend, RenderingBackend}
import com.tsunderebug.eightdown.transform.Rotation
import com.tsunderebug.eightdown.transform.layerop.MultiplyLayerOp

import scala.util.Random

object RedSquareTest {

  def main(args: Array[String]): Unit = {
    implicit val backend = HighQualityGIFRenderingBackend(Color.BLACK)
    val mgd = new GifDecoder
    mgd.read("wolfiri.gif")
    val a = Animation(
      AnimatedGIF(
        gd = mgd,
        frame = (t, fm) => (t * (mgd.getFrameCount - 1)).toInt,
        pos = (t) => ((t * 4.0 / 3.0) - 1.0 / 3.0, (t * 4.0 / 3.0) - 1.0 / 3.0),
        w = (_) => 0.75,
        h = (_) => 0.75
      ).transform(
        MultiplyLayerOp(
          Gradient2(
            color1 = (t) => new Color(255, 0, 0),
            color2 = (t) => new Color(0, 255, 0),
            pos1 = (t) => (0, 0.5),
            pos2 = (t) => (1, 0.5)
          ).transform(
            Rotation(
              rot = 2 * Math.PI,
              center = (t) => (t, 1-t),
              endTime = Some(1.0)
            )
          )
        )
      )
    )
    a.renderTo(128, 128, new File("blob.gif"), framerate = 30, totalTime = 20l, TimeUnit.SECONDS)
  }

}
