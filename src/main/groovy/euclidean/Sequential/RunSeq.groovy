package euclidean.Sequential

import cluster_framework.records.SourceDataInterface
import euclidean.locality.AreaData
import euclidean.locality.AreaLocales
import euclidean.locality.AreaPoICollect
import euclidean.locality.PoILocales

class RunSeq {
  static void main(String[] args) {
    long startTime = System.currentTimeMillis()
    double gap = 3.0
    AreaLocales sourceData = new AreaLocales("./data/areas25000.loc")
    PoILocales workData = new PoILocales("./data/pois5000.loc")
    AreaPoICollect areaPoICollect = new AreaPoICollect(["./data/SeqResults"])
    AreaData baseArea = new AreaData(null)
    AreaData areaData
    areaData = baseArea.create (sourceData.getSourceData())
    while (areaData != null) {
      areaData.distance(workData, [gap])
      areaPoICollect.collate(areaData, null)
      areaData = baseArea.create (sourceData.getSourceData())
    }
    areaPoICollect.finalise(null)
    long endTime = System.currentTimeMillis()
    println "elapsed time = ${endTime - startTime}"
  }
}
