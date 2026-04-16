To run the java application navigate to the jar file and run
java -jar AnalogiesProject-1-0-SNAPSHOT.jar
And to access the source code run
jar -xvf AnalogiesProject-1.0-SNAPSHOT.jar
the source files can then be found at org/main


*********************
| Config file setup |
*********************

Config file works simply enough,
it has a series of keywords, when running the program you can specify the path to
the config file you'd like it to use. If you don't provide one, it will try to find
config.txt, if it cant do that it will just make a new blank config.txt which you can modify.

*********************
|     Keywords      |
*********************

When writing the config file the pattern will look like this

keyword=argument

no space needed.
There are 6 keywords
rules: the file path to the ruleset you want the file to read
analogies: the file path to the analogues you want to read
rewrite: true\false: if true it will only intake the first version of the analogy, and then rewrite it using all the rules ignoriny any analogies already there
targets: if you want specific analogies only, its input will should look like this
targets=a,b,c,d
jumps: take a number, used to indicate the space between the main analogies in the file,
by default this is 3, because of the structure of the analogies file given, if you want to change it
then you will need to change the jump valuetat
