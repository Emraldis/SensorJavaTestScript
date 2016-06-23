all:
	javac -classpath .:classes:/opt/pi4j/lib/'*' -d . ./src/test/Test.java
run:
	sudo java -classpath ./dist/'*':classes:/opt/pi4j/lib/'*' test/Test