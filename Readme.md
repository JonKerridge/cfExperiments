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

A more detailed guide of the actual code the user has to write is given at the end of this document.

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

The net host example is contained in the package *euclidean/Invoke*.  This can be used as a model
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

### Detailed Discussion of User Programming Requirements

The available [DSL options](#dsl-options) are described at the end of this discussion.

This discussion looks at the code and other files the user has to create in order to use the 
clusterFramework library. In both examples the user has to write two class definition files that describe the
processing of the data and its subsequent results collection.  The euclidean example requires a further 
two class definitions associated with the description of the source data that is emitted 
and utilised in the work cluster.

Typically, the data definition class will contain two constructors.  The first is used when the class is 
initially constructed using parameter values passed to it from the application's DSL definition.  
The second constructor is used to define an instance of the data object that will be passed through the 
parallel framework constructed as a result of processing the DSL file.  The second constructor will, 
typically be called in the *create* method the user has to write as required by the *EmitInterface*.

The class used to collect the processed results will contain a single constructor with a single **List**
parameter, which will contain any parameters that need to be passed from the DSL specification 
to the class instance.  The user will have to provide two methods; *collate* and *finalise*, as required by the 
*CollectInterface*.

Parameter values from the DSL specification are always passed as a **List** of values with the same ordering
as that specified in the DSL file.  Thus, the constructor used to instance the class will have a 
single **List** parameter only, even though several values may be passed to the constructor.  The user has to 
extract the individual parameter values from the *List*, ensuring the types specified in the DSL match those 
used in the constructor.

## Mandelbrot

see https://en.wikipedia.org/wiki/Mandelbrot_set for a fuller description of the approach adopted and 
algorithm details.

# classFiles/MandelbrotData
The base class constructor is passed two integer values from the DSL specification in the *List d*. The first
is the number of points to be used in the x-direction.  The second is the escape value which gives the number
of iterations to undertake until the calculation is stopped.  The actual object instances are initialised from 
within the base instance using the *create* method.  The constructor initialises some values that 
are used subsequently.

The *create* method uses the second constructor with values specific to each line of the result set.  The
parallelisation technique used in this example is to split the set into a number of distinct lines 
each of which can be processed in parallel.  The *create* method initialises the line's values and then 
uses them in the constructor.  Once all the lines have been constructed, the *create* method returns a *null*
value to signify the end of object creation.  This *null* value will be passed through the rest of the parallel
framework to terminate all the processing in an orderly manner to recover all the resources used in the application

The method(s) that do the actual processing in the work cluster must also be defined.  In this case it is called
*calculateColour*.  This will be called in the worker processes defined in the nodes of the work cluster.  The name
of the method will be obtained from the DSL specification and so must match precisely.  

All such work methods have a first parameter that is an object that will hold any data read in from a file used by 
the method.  In this case there is no such data and the parameter will be passed as null.

*Explanatory Note*
There was a choice here, either to make work methods part of the **EmitInterface** and thus the user would be limited 
to the names chosen as part of the framework; or leave the name choice up to the user and obtain the method name 
from the DSL specification.  Another reason for making the latter choice is that the user can choose 
meaningful names and can define any number of such methods depending on the number of work clusters.  If the work methods 
had been part of the **EmitInterface** then the number of work cluster would have been limited.  A corollary of this choice
is that the user has to remember to add a first parameter to every work method that is the placeholder for any 
data read as part of a **WorkDataInterface** object.  Any parameters passed to the work method from the DSL specification
will be passed as a **List** parameter.

# classFiles/MandelbrotCollect
The class constructor is passed no parameter values but even so the *List* parameter must be specified.
The constructor simply initialises some properties of the class.

The first parameter of the *collate* method is **always** of the same type as that emitted into the 
parallel architecture.  The second parameter is a *List* parameter containing any values that are passed
from the DSL specification.

The *finalise* method has a *List* parameter to receive any values from the DSL specification.  The method is used
to undertake any processing once all the processed data objects have been input.  In this case it simply prints
the grand totals accumulated during processing.

# DSLfiles/mandelbrot1n4w1024.clic

This DSL specification file defines an architecture comprising a single node 1 worker emit cluster,
followed by a single node 4 worker cluster and finished by a single node 1 worker collect cluster. Each
line has 1024 points in the x-direction and the colour determination terminates after 2048 iterations.

# DSLfiles/mandelbrot1n8w1024.clic

This DSL specification is similar to the previous, except that the work node has 8 worker processes.  Assuming 
the PC on which the application is running has sufficient cores then this version should run nearly twice as fast
as the previous.

# Sequential/RunSeqMandelbrot1024.groovy

Contains a script that runs the Mandelbrot application in a sequential manner, using the classes defined previously.
This provides a means of ensuring the classes work correctly and also gives a base sequential performance
when undertaking parallel speedup and efficiency determination.

# invoke/RunLocalHost4_1024

Contains a main class that will run the DSL specification *mandelbrot1n4w1024*.  This version will
run on a single multicore PC.  It can be observed the parsed DSL file, and the two classes are 
passed as parameters to the *HostRun* method.  The *nature* is passed as **Local**, meaning the 
application will run on a single PC.

# invoke/RunLocalHost8_1024

Invokes the same classes as the previous version but uses the DSL specification *mandelbrot1n8w1024*.

# invokeNodes
The codes contained in *Node2..5* each invoke a local node that can be used for any application 
when it is being tested using a code such as *RunLocalHost**n**_1024), or any of the codes in
*euclidean/Invoke/Exp1..4*.  This emphasises that the code that invokes a node is 
independent of the application.  All these codes assume the Host node is running on the local processor 
127.0.0.1.  The parameter *nature* is set to *Local*.


# invoke/MandelbrotHost

Contains a **main** class the calls *HostRun*.  In this the structure file is passed as 
an argument to the program.  The call to *HostRun* specifies the *nature* as **Net**, which
means that the code will run on its own PC workstation and all the nodes in the applications clusters 
will be located on their own PC workstation.  Communication will take place over a TCP/IP connection.

Typically, this code will be created as a jar artifact so that it can be invoked from a command line
by   

    java -jar MandelbrotHost.jar structureFileName

This formulation assumes that a directory holds the MandelbrotHost.jar and structure file.  The program will run 
and display on its terminal the IP-address of the host and the number of other nodes required to run the application.

# invoke/MandelbrotNode

Similarly, contains a **main** class which will run a node of the application architecture.  It will also
be created as an artifact jar file.  A node does not require direct access to the class files of the application as
these will be communicated from the host on an as needed basis.  The program needs a single parameter which is the 
IP-address of the host node.  It is invoked from a command line by   

    java -jar MandelbrotNode.jar host-ip-address

The same artifact can be used to run any node in any application provided they have been created using the same
version of the software.

## euclidean

This application uses more of the clusterFramework capabilities, in that the data to be processed is obtained
from a file.  Further, the method that undertakes the worker process calculations also requires access to other data
which is also obtained from a file.  Each Worker node reads the data file once and the Worker processes then access 
the data on a shared read-only basis.  Modification of the work data at run time is not permitted.

A folder/directory called *data* has been created at the top level of the project structure.  This holds 
all the files used in the application and is also the folder to which all result files are written,

# euclidean/DSLfiles

This folder contains the definition DSL files for 4 experiments called exp*n*.clic (*n* = 1..4).  Specifications 
exp1 to exp 3 do the same operation but with different numbers of worker processes in the work cluster.
All clusters comprise a single node.  The emit and collect clusters have a single worker process.
Specification exp4 differs in that there are two work clusters each comprising a single node with 4 
worker processes.  The work clusters undertake a different operation on the data but use the same data 
file for undertaking the calculations.

A detailed description of exp4.clic is presented.
    version 2.0.4
    emit -nodes 1 -workers 1  -f ./data/areas25000.loc
    work -n 1 -w 4 -m distance -p double!3.0 -f ./data/pois5000.loc
    work -n 1 -w 4 -m adjacent -p double,double!3.2,6.1 -f ./data/pois5000.loc
    collect -n 1 -w 1 -p String!./data/X4Results

Each cluster has a single node (-n).  The emit and collect cluster each have a single worker process (-w). The
two work clusters both use 4 worker processes (cores).  The emit cluster obtains data from the file *./data/areas25000.loc*.
If there were more workers in the emit cluster then each worker would have access to its own source data file.
The work clusters get their data from the file *./data/pois5000.loc*.  The first work cluster undertakes the method 
*distance* and the second the method *adjacent*. the method *distance* is passed the single double parameter 3.0.
The method *adjacent* is passed two double parameters with values 3.2 and 6.1.  The same parameter values are passed to 
all worker processes.  The *collate* method in the collect cluster is passed a single string parameter which is
the name (*./data/X4Results*) of the file to which results will be written.

the specification exp 1 to 3 are basically the same but with only the work cluster using the method *distance* defined.
All that varies between the specifications is the number of worker processes in the cluster.  Specification exp1 has just 
a single worker process, which means the overhead occurring due to parallelisation can be assessed when the processing
time for exp1 and the sequential version are compared. 

# euclidean/locality

This folder contains the definitions of all the classes used in the application.  The aim of the application is, to
find, for each location in the *./data/areas25000.loc* file, which of the places (Points of Interest) in the *./data/pois5000.loc*
file are within 3 units of each other.  The simulated space is 175 units square.  There are 25000 unique locations in the
*./data/areas25000.loc* file.  Similarly, there are 5000 random points of interest (PoI) within the *./data/pois5000.loc* file. The distance
between points is determined using Pythagoras' theorem.  One location may have several PoI close to it or none.  Some PoIs may have 
no close locations.  It can be observed that for every location emitted a distance test has to be made against all the PoI locations.

# euclidean/locality/Location

This class defines a location with its [x,y] coordinates, its type, location or PoI, and a unique
identification number.

The method *euclideanDistance* calculates the Pythagorean distance between the object's **Location** and another point, passed as
a parameter.

Other methods are provided that are not used by this application.

# euclidean/locality/AreaData

AreaData is the object that contains the emitted data and the methods that are called from a worker cluster.  It 
has two constructors, the first has a single **List** parameter that would pass any values from the DSL specification, 
but in this case there are none.  The other constructor is the one used to create an emitted object using a **Location**
parameter, that will be obtained from a source data file.

The required *create* method will be passed a **Location** value, which it will return, until the end of the data when it 
returns null.  The *create* method is called by the underlying emit process in framework architecture.

The method *distance* obtains the Pythagorean distance between the **Location** in the object and **Locations**s
held in the work data.  The method gets the size of the work data to be able to iterate through the work data.
It is assumed that the object's property values will be tested against all or a subset of the values held in the work data.
The methods *getWorkDataSize* and *getFilteredData* are defined in the class that implements **WorkDataInterface** object,
which in this case is *PoILocales*.

AreaData obtains the emitted data from a file that is defined in the object *AreaLocales*

# euclidean/locality/AreaLocales
AreaLocales implements the *SourceDataInterface* and the constructor obtains the name of the file holding
the source data is obtained from the DSL specification.  The way in which the input of data is handled is up 
to the user, as is the structure of the file.  In this case, a file of **Location** objects has been written
to a file previously.  The constructor reads all the objects into memory and then iterates through the objects.

The interface requires a single method called *getSourceData*  which returns either the next data item or null
if the end of said data has been reached.  This method is called as part of the object creation method 
in the emit process and returned as the parameter of the *create* method.

# euclidean/locality/PoILocales
PoILocales implements the *WorkDataInterface* which requires two methods called *getWorkDataSize* 
and *getFilteredWorkData*.  

This interface is used by classes that are to be loaded into a work cluster.  
The data is loaded from a file, the name of which is specified in the DSL specification.  Each node in 
the work cluster will load the same file.  The workers in each node get shared read-only access to the 
loaded data.  This means that the worker application method MUST NOT write to the shared data.  It is also
assumed the work can be accessed using a subscript and has an ordering such that the items follow one after the other..

It is assumed that every emitted data object will access all, or a subset of, the items in the loaded shared data.

The method *getWorkDataSize* returns the number of items in the work data.

The method *getFilteredWorkData* has two parameters; *index* which is the subscript of the data item in the work 
data to be returned, and the **List** *p* contains the values that are to be used to filter the data or null.  The method
returns a **List** containing the index of the item that follows the one being returned and the item value.

If the parameter *p* is not **null**, then the values contained in it can be used to filter data items.  The filter should
check the data items in turn and should return the first item it finds that satisfies the filter values.  If the data items
need to be processed in a specific order then the items must be loaded in that order.  The filter values specified in *p*
will be tested against values in the emitted object being processed.  

In this example the value of *p* is **null** so all the data items in the work data are processed.

The example assesses whether each of the locations in source data is close to each of the locations, or Points-of-Interest in the work data, 
and if so keeps a record in the data object of the close locations.

# euclidean/locality/AreaPoICollect
This class implements the methods of the *CollectInterface*.  The constructor has one parameter, which is the 
name of the file to which the results will be written.  This value is obtained from the DSL specification.  The *collate*
method adds the incoming data object to the list of such objects maintained in the property *allAreas*.  
The data is written to the file and the list of close locations is appended to the property *accessedPoIs*.

The *finalise* method processes the *allAreas* **List** to determine, which, if any, of the source areas are not 
close to any of the points of interest and which, if any, of the points of interest are not close to any source area.
These are then printed out to the application display and finally the file to which all the data has been written is 
closed.

# euclidean/FileHandling
This folder contains the programs that are used to create the required source and work data files.  There is also a
program that will print out the contents of either of the source files.  The name of the file has to be changed in the
code.

# euclidean/locality/Invoke
This folder contains programs that invoke each of the DSL specifications.  Program Exp*n* loads DSL file exp*n*.clic.
The programs identify the classes that are required.  The property *workData* is a **List** that contains the class
used for each work cluster.  In this case the same work data is used in both clusters.  If an application has more than 
one work cluster then any cluster that does not require a work data class must be specified as **null** in the list entry
corresponding to the cluster number.

The program *euclidean/locality/Invoke/NetHost* can be used to create a jar artifact that can be used to invoke
the application on a network.  The program argument contains the name of the DSL specification file to be used.  The program
artifact *euclidean/invokeNodes/NetNode* would then be used to invoke each of the required nodes.

# euclidean/Sequential/RunSeq
This runs the application using the above class definitions in a sequential manner.  It iterates through each of the 
source file area locations and then processes each one against all the work point-of-interest locations.  The program
prints out the elapsed time.  This time can be used to evaluate parallel speedup and efficiency when running the 
experiments in Invoke.  Recall that Exp1 is essentially a replication of the sequential version in that there is
only one node in the work cluster with just one worker process.  This allows the evaluation of the parallel overhead.

## DSL options
A typical DSL specification comprises a number of lines that appear in a specific order as shown below.

    version 2.0.4
    emit -nodes 1 -workers 1  -file ./data/areas25000.loc
    work -n 1 -w 4 -method distance -p double!3.0 -f ./data/pois5000.loc
    work -n 1 -w 4 -m adjacent -p double,double!3.2,6.1 -f ./data/pois5000.loc
    collect -n 1 -w 1 -p String!./data/X4Results

The first line is always the *version* specification.  This must match the version of the software being used.
The second line always specifies the emit cluster definition.
The third and any subsequent lines specify the work cluster in the order they are to be applied to the data.
The last line always specifies the collect cluster.

Some of the specification options are common to all the specifications.
They are all introduced  with a - (minus) character and take the form used in a command line interface.

n(odes) specifies the number of nodes (workstations) in a cluster

w(orkers) specifies the number of worker processes (cores) in each node in a cluster

f(ile) specifies a file name, several file names may be specified separated by a comma

m(ethod) specifies the name of a method to be used in a work cluster

p specifies a set of parameter values.  These take the form of a string of values separated by !  
>The first string comprises a comma separated string of parameter types (int, float, double, long, String, boolean), one per required parameter value.
>Subsequent strings comprise the values to be passed as parameters.  The n'th value must correspond in type to the n'th parameter type 
>The use of parameters varies depending on the cluster being specified.

cp specifies any parameters for the *collate* method in the *CollectInterface*

fp specifies any parameters for the *finalise* method in the *CollectInterface*

Additionally, the IP-addresses to which nodes in an application are to be allocated can be specified.  In this case
all the node IP-addresses must be specified for all the clusters.  The cluster IP-addresses can be placed anywhere in the 
specification.  A check is made at application load time that sufficent nodes have commenced and that all the 
required IP-addresses are available.

### emit specification

-p used to specify parameters for the emit class constructor.  There must be *nodes* x *workers* parameter value strings.

-f used to specify file names that hold source data files. There must be *nodes* x *workers* filenames

### work specification

-m the name of the method to be used in this cluster

-p a parameter string which has the type string and ONLY one set of parameter values, 
all the workers in the cluster have the same parameter values

-f a single file name giving the name of the file holding the work data, all nodes have the same file

### collect specification

-p a parameter string which has the type string and *nodes* x *workers* sets of parameter values, one set per worker,
each worker in the cluster has different parameter values

-cp a parameter string which has the type string and *nodes* x *workers* sets of parameter values, one set per worker,
each worker in the cluster has different parameter values

-fp a parameter string which has the type string and *nodes* x *workers* sets of parameter values, one set per worker,
each worker in the cluster has different parameter values