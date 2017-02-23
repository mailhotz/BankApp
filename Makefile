all: BankApp.class

BankApp.class: BankApp.java
	javac BankApp.java

clean:
	:rm -f *.class
