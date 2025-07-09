package Mandelbrot.invoke

import Mandelbrot.classFiles.MandelbrotCollect
import Mandelbrot.classFiles.MandelbrotData
import cluster_framework.run.HostRun

/**
 * The NetHost code must be run first when using s number of PCs.
 * It will print out its IP address, which needs to be passed as an argument to
 * each of the nodes.  It will also confirm the number of separate nodes required.
 *
 * You will see that the Host code does access the class files used in the application.
 * These will be passed to the nodes on as required basis automatically.
 */

class MandelbrotHost {
    static void main(String[] args) {
        // args[0] contains the name of the parsed clic file with no suffix
        String structure = args[0]
        Class emitClass = MandelbrotData
        Class sourceData = MandelbrotCollect
        new HostRun(structure, emitClass, sourceData, "Net").invoke()
    }
}
