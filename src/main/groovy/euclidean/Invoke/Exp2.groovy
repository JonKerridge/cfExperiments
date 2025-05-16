package euclidean.Invoke

import cluster_framework.run.HostRun
import euclidean.locality.AreaData
import euclidean.locality.AreaLocales
import euclidean.locality.AreaPoICollect
import euclidean.locality.PoILocales

class Exp2 {
  static void main(String[] args) {
    String structure = "./src/main/groovy/euclidean/DSLfiles/exp2"
    Class emitClass = AreaData
    Class sourceData = AreaLocales
    List <Class> workData = [PoILocales]
    Class collectClass = AreaPoICollect
    new HostRun(structure, emitClass, sourceData, workData, collectClass, "Local").invoke()
  }
}
