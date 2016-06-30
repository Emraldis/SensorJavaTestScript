all:
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/SystemController.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/LedIndicator.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/LogicRelay.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/MuxControl.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/Button.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/sensor/Sensor.java
run:
	sudo java -cp .:/opt/pi4j/lib/pi4j-core.jar sensor/Sensor