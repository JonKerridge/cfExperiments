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

As well as a sequential implementation there are three parallel ones.  The first parallel version 
provides a direct comparison with the sequential one, in that a single process is used for each
stage of the parallel architecture.  This will actually run more slowly than the sequential version.
The second and third parallel applications have 2 and 4 parallel worker processes and show 
commensurate speedup.
