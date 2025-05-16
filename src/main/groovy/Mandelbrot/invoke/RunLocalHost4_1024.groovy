package Mandelbrot.invoke

import cluster_framework.run.HostRun
import Mandelbrot.classFiles.MandelbrotCollect
import Mandelbrot.classFiles.MandelbrotData

class RunLocalHost4_1024 {
  static void main(String[] args) {
    String structureFile = "./src/main/groovy/Mandelbrot/DSLfiles/mandelbrot1n4w1024"
    Class  emitClass = MandelbrotData
    Class collectClass = MandelbrotCollect
    new HostRun(structureFile, emitClass, collectClass, "Local").invoke()

  }

}
