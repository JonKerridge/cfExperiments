package euclidean.Invoke

import cluster_framework.run.HostRun
import euclidean.locality.AreaData
import euclidean.locality.AreaLocales
import euclidean.locality.AreaPoICollect
import euclidean.locality.PoILocales

class NetHost {

  static void main(String[] args) {
    // args[0] contains the name of the parsed clic file with no suffix
    String structure = args[0]
    Class emitClass = AreaData
    Class sourceData = AreaLocales
    List <Class> workData = [PoILocales]
    Class collectClass = AreaPoICollect
    new HostRun(structure, emitClass, sourceData, workData, collectClass, "Net").invoke()
  }
}
