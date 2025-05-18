package euclidean.FileHandling

import euclidean.locality.AreaData

class PrintOutFile {
  static void main(String[] args) {
    String readFileString = "./data/X4Results-0-0.out"
    File objFile = new File(readFileString)
    objFile.withObjectInputStream { inStream ->
      inStream.eachObject {AreaData ad ->
        println "$ad"
      }
    }
  }
}
