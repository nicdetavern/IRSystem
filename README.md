# IRSystem
Reads large amounts of raw data and parses terms into key-value pairs. These KVP's are indexed and leveraged by a user to locate relevant listings. KVP's in the IRSystem contain frequency, location, and soundex form values. All KVP's share a common key &lt;term>.

**INSTRUCTIONS BELOW**

First, ensure any version of the JDK is installed to compile the code.\
In command prompt change the directory to the unzipped file(s) location. If you are unfamiliar the syntax in windows is: \
cd /D drive_letter_here:\folder_here\ ...\IRSystem\
Compile then run the Assignment2.java file, ensure either the provided data folder(alldocs) is present in the IRSystem directory or provide your own data(in the IRSystem directory). \
The compile command is: javac Assignment2.java\
The run command is: java Assignment2.java

**LIMITATIONS**

The IRSystem is contained within the IRSystem folder as the scope for this project was nothing more than demonstration of skill.
  Extending functionality to include user-defined file path inputs would be reasonable to achieve if desired.\
\
Any type of text file should be supported, the provided data folder utilizes .dat files; other types haven't been tested.\
\
Previously generated output files will be parsed not dissimilar to the raw data used to generate aforementioned output; 
this could be improved upon and potentially reduce resources needed if the previous output were taken into consideration.
However, currently the IRSystem utilizes parsed data present in memory which would not be suitable for (much) larger-scale applications. 
This does lead to a more fast response time to user queries, fortunately, but the gained response time is likely negligible compared 
to iterating through indexed data to return relevant results from user queries.

**FULL DESCRIPTION**

The IRSystem is a program designed to permit a user to quickly identify data of interest to themselves. This is accomplished via KVP hashmaps, all of which share a common 'term' key. Before all else, the user is prompted for the desired data folder to be parsed and indexed. This folder must be in the same directory as the IRSystem and Assignment2 java files, and additionally must be IN a FOLDER. A file list is created at program execution, this list is used to populate the hashmaps for data processing until program termination via the sentinel value (-99) or other forced exit(CTRL+C for example). Presence of the hashmap(s) throughout program runtime is useful to process and effectively deliver relevant results to user queries, which are compared to relevant values or programmatically determined related values. Not only does the program permit a user to query the given data, it will also aid in finding relevant data if no direct matches are found via edit distance from the given query. In practice this could give a completely unrelated result, yet it still provides a modest amount of direction to aid the user if the input were simply mis-spelled, for example. All user prompts are designed to be as concise and helpful as possible, while still maintaining a small word count of ~<5.
