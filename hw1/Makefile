CC=g++
CCOMPILEFLAGS= -std=c++17 -g
JAVA=javac
JAVACOMPILEFLAGS= -g

hw1-1: SequenceGenerator.java
	$(JAVA) $<
	jar -cvmf MANIFEST.MF hw1-1.jar SequenceGenerator.class


hw1-2:  seq_partition_sim.cpp 
	$(CC) $(CCOMPILEFLAGS) -o $@ $<

hw1-3:  seq_assembler.cpp 
	$(CC) $(CCOMPILEFLAGS) -o $@ $<

all:  hw1-1 hw1-2 hw1-3

clean: 
	rm -rf hw1-2
	rm -rf *.fasta
	rm -rf *.class
