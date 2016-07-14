all:
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/LedIndicator.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/SystemController.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/LogicRelay.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/MuxControl.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/Button.java
	javac -cp .:classes:/opt/pi4j/lib/'*' -d . ./src/GPIO_Manager/GPIO_Manager.java
run:
	sudo java -cp .:/opt/pi4j/lib/pi4j-core.jar GPIO_Manager/GPIO_Manager