package euclidean.Invoke

import cluster_framework.run.HostRun
import euclidean.locality.AreaData
import euclidean.locality.AreaLocales
import euclidean.locality.AreaPoICollect
import euclidean.locality.PoILocales

/**
 * The NetHost code must be run first when using s number of PCs.
 * It will print out its IP address, which needs to be passed as an argument to
 * each of the nodes.  It will also confirm the number of separate nodes required.
 *
 * You will see that the Host code does access the class files used in the application.
 * These will be passed to the nodes on as required basis automatically.
 */

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
