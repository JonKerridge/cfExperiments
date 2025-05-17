### Preamble

This repo holds examples of the use of the cluster_framework library available from

https://github.com/JonKerridge/clusterFramework

The library enables simpler parallelization of a sequential code 
written in Java or Groovy.

The user does not have to fully understand the intricacies of parallel programming to 
be able to exploit parallelism.  The basic sequential code for an application does not have
to be changed but has to be slightly repackaged using a few simple interfaces.  The resulting
classes can also be executed in a sequential manner without alteration, enabling measurement 
of the real effect of parallelization.

The library Readme file gives more information how the application has to be modified
and details of how the DSL specification has to be constructed.

The examples are
1. Mandelbrot Set - package Mandelbrot
2. Euclidean Distance calculation - package euclidean

The package *invokeNodes* contains codes that can run the required nodes other than 
the host node.  These can be used for any application because the node process 
is application independent.  Each of the example packages has its own *invoke*
package which contains the code to run a host node.  The host node *MUST* be 
executed first after which the required node process can be started.

### Mandelbrot

The Mandelbrot set is a very simple application to both parallelize and to demonstrate the effects
of parallelization.  It does not require access to any external data files as the application can
be expressed in terms of just a few parameter values.

Two parallel implementations are provided mandelbrot1n4w1024 and mandelbrot1n8w1024.  The
first uses 4 parallel worker nodes and the second 8 worker nodes, together with a sequential 
version.

### Euclidean Distance

This application calculates the *crow's flight* distance between a number of points.
One set of points comprises 25000 [x,y] points in a square space of 175 units.  
The other set of points comprises 5000 points.  The purpose of the application is to find
all the points in the second set that are with 3 units of every point in the first set.

The example uses two datafiles.  One is used to provide source data for the 25000 points and 
is used as input to the application.  The second is used to hold the 5000 points and is loaded
into worker nodes and shared between all the worker processes in that node.

As well as a sequential implementation there are three parallel ones (Exp1..3).  The first parallel version 
provides a direct comparison with the sequential one, in that a single process is used for each
stage of the parallel architecture.  This will actually run more slowly than the sequential version.
The second and third parallel applications have 2 and 4 parallel worker processes and show 
commensurate speedup.

Exp4 shows how a pipeline of work clusters can be created using 2 work clusters.  In this example
he second work cluster just repeats the work in the first cluster using a different parameter
value.  In reality the second work cluster would do a further operation on the data.  Please note that
a List of classes is passed, one for each work cluster, indicating the type of the data file to be loaded. 
If a work cluster does not require a data file then the value *null* should be used.  There should be as 
many entries in the List as there are work clusters.

### Real Network Operation

The simplest way of running an application on a real network is to create two jar files; one for the
host node and the other that can be used on all the other nodes. Examples of such codes are available.

The net node example is contained in the package *invokeNodes*, called NetNode. It can be used for 
any application as the node code is application independent.  The NetNode code MUST be executed only after 
the host node has started.  The host node will display the IP-address it is using to enable NetNode 
to pass that address as its single parameter.

The net host example is contained in the package *euclidean.invoke*.  This can be used as a model
for any other application.

The software was developed using the Intellij IDE and the build contains the required creation of the jar artifacts.
The jars once created can then be invoked from the command line using the usual java command.  The location of
any required data files will need to be included in the.clic file and is made easier if a globally accessible
file store is available.  If not the .clic file can contain the IP-address to which a cluster is allocated
so that any data files can be stored at that node.  Simply add the required IP-address(es) to the cluster definition
and the framework will ensure the node(s) will be allocated to the specified node(s).  Obviously, the specifically allocated
nodes will have to be ones that run the NetNode code.  The host node will check to enaure the required nodes are in the set
of nodes allocated to the application at run time.

The required jars will be found in the *./out/artifacts/* folder in the project.
