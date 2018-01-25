package com.tsunderebug.eightdown.rendering

import java.io.File
import java.util.concurrent.TimeUnit

import com.tsunderebug.eightdown.draw.Drawable

trait RenderingBackend {

  def render(w: Int, h: Int, f: File, d: Drawable, framerate: Int, totalTime: Long, unit: TimeUnit): Unit

}
