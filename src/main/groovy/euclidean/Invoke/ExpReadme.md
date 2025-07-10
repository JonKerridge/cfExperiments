Each of the experiments Exp1 to Exp4 runs a host node each using
a different structure *.clic file located in the folder euclidean.DSLfiles.

Each of the experiments runs in local mode means that it uses a loopback communication
rather than over a real network.  Thus, the performance is limited by the number of
cores in the PC.

Each of the experiments uses different resources in terms of the number of workers per node.
Exp4 has two work clusters, whereas the others hve just one.

All the output is written to files in the ./data folder

Run the host code using the IDE run prompt, making sure that parallel execution is enabled in the IDE.

Now move to the InvokeNode folder and run the required number of Node processes, indicated by the 
terminal output for the host process using the IDE run prompt.

Once the application completes, files will appear in the ./data folder and the host terminal output will
contain the timing data that appears in the corresponding times.csv file.

The folders also contain NetHost and NetNode codes that can be used to run an application on a real
network.  The NetHost will run and output the Ipaddress of the Host process.  The user then has to run the
required number of Node processes indicated using the host IPaddress as argument.  The application will run over the
network and in this case the performance differences should be more apparent.

The project should have built the required jar artifacts.  These should be copied into a user folder on the 
user's PC, together with the required *.clicstruct files.  In the same folder create a folder called 'data' into
which the result files can be written.  The *.clicstruct files only need to be copied onto the PC that will run the Host node.

Then use the java -jar file.jar parameter to run the required artifact from the command line in the folder in which the 
artifacts were copied.

The same mechanisms are provided for the Mandelbrot examples in the Mandelbrot/invoke folder.  In this case there a no 
local Node processes supplied.  The user can invoke the same node processes as supplied in the euclidean application. 
Thereby reinforcing the fact that the node process is not application dependant.


