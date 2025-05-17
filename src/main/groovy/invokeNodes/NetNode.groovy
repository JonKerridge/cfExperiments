package invokeNodes

import cluster_framework.run.NodeRun

/**
 * A user is expected to create a jar artifact of this
 * code so that it can be invoked directly from a command
 * line using
 *
 * java -jar NetNode.jar host-ip-address
 *
 */

class NetNode {
  static void main(String[] args) {
    // args[0] contains the IP address of the host node
    new NodeRun(args[0]).invoke()
  }

}
