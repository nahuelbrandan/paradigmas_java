CC = javac
CFLAGS  = -cp lib/commons-cli-1.3.1.jar:lib/commons-io-2.5.jar
SOURCES = $(wildcard src/*.java)
OBJECTS = $(SOURCES:.java=.class)

RemoveTemp: Traductor
	rm tempMF

Traductor: Compile CreateTemp CreateFolder
	jar -cfm bin/Traductor.jar tempMF -C src/ Traductor.class -C src/ DiccionarioHash.class -C src/ DiccionarioLinked.class -C src/ Ignoradas.class -C src/ Archivo.class -C src/ EmptyFileException.class 

CreateFolder:
	mkdir -p bin

CreateTemp:
	echo "Class-Path: ../src/  ../lib/commons-cli-1.3.1.jar ../lib/commons-io-2.5.jar">tempMF
	echo "Main-Class: Traductor">>tempMF

Compile:
	$(CC) $(CFLAGS) $(SOURCES)

.PHONY : clean

clean  :
	rm -f *~ src/*~ src/*.class bin/Traductor.jar
