CSCI3170_Project Group 23:

1) Start the SQL server
mysql --host=projgw --port=2633 -u Group23 -p

2) Start the program
javac Main.java menuPackage/*.java tools/*.java
java -cp ./mysql-jdbc.jar:. Main
