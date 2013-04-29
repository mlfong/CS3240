CS3240
======

Languages and Computation

## about

Scanner, parser, lexer.

## clone instructions

    $ git clone https://github.com/mlfong/CS3240.git

## build instructions

	Apache Ant is needed to run the ant files to build the project:
	In the folder with the ant file "build.xml", run:
		ant build   (Compiles and builds the class files)
		ant Driver -DS="path to spec file" -DG="path to grammar file" -DI="path to input file"        (the main class to run)