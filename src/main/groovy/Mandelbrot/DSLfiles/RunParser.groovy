package Mandelbrot.DSLfiles

import cluster_framework.parse.Parser

class RunParser {
  static void main(String[] args) {
    String workingDirectory = System.getProperty("user.dir")
    Parser parser = new Parser("$workingDirectory/src/main/groovy/Mandelbrot/DSLfiles/mandelbrot1n8w1024")
    assert parser.parse() :"Parsing failed"
  }

}
