package invokeNodes

import cluster_framework.run.NodeRun

class Node5 {
  static void main(String[] args) {
    new NodeRun("127.0.0.1", "127.0.0.5").invoke()
  }

}
