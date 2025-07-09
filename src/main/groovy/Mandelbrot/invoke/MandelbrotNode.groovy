package Mandelbrot.invoke

import cluster_framework.run.NodeRun

/**
 * Before running this code the HostNode MUST executed so that it can print out
 * the IP-address of the host node and also confirm the number of nodes required.
 *
 *
 * A user is expected to create a jar artifact of this
 * code so that it can be invoked directly from a command
 * line using
 *
 * java -jar NetNode.jar host-ip-address
 *
 * This NetNode.jar can be used to load ANY application because the nodes do not
 * vary between application as all the required classes are passed from the host node
 *
 */

class MandelbrotNode {
    static void main(String[] args) {
        // args[0] contains the IP address of the host node
        new NodeRun(args[0]).invoke()
    }
}
