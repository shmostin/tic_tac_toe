all: run

run: client
	java -cp bin:./libs/* SProject.Client

client: src/SProject/Client.java
	javac -Xlint:deprecation -d bin -sourcepath src -cp ./libs/*:. src/SProject/Client.java

clean:
	rm -rf bin/* 
