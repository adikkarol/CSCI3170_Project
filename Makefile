compile:
	javac interfaces/*.java menus/*.java tools/Database.java Main.java

run:
	java -cp ./mysql-jdbc.jar:. Main

clear_class:
	rm menus/*.class
	rm interfaces/*.class
	rm tools/*.class
	rm Main.class

all:
	# make clear_class
	make compile
	make run

