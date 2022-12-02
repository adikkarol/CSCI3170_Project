compile:
	javac interfaces/*.java menus/*.java tools/Database.java Main.java

run:
	java -cp ./mysql-jdbc.jar:. Main

clear_class:
	rm Main.class
	rm menus/*.class
	rm interfaces/*.class
	rm tools/*.class

all:
	make compile
	make run
