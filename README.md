#Text Visualization Tool

##Summary
Text visualization tool which shows text as graph. 
It builds graph based on POS tagging. 
Some POS'es are mapped to nodes, other to edges. 
Nodes with same text can be merged to one node on frontend.

On UI graph is splitted to pages to improve readability.
<!--Number of sentances per page is configurable.-->

Demo page:

##Workflow
1. Go to page localhost:5555
2. Paste text to textarea on the top left of the page
3. Press enter or Send button.
4. View and navigate graph. You can navigate between sentances by pressing + and - on numpad or < and > buttons



##Languages
Only english is curently supported

##Configuration:
Server config file:
bin/main.properties


##Set Up
###Requirements
1. JDK 1.8

###Build
1. Clone repository
2. cd {REPO_ROOT}
3. mvn clean install

###Deploy
1. build project
2. run bin/server.sh or bin\server.bat
3. navigate to localhost:5555




