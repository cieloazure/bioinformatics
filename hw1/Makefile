CC=g++
CCOMPILEFLAGS= -std=c++17 -g
JAVA=javac
JAVACOMPILEFLAGS= -g

hw1-1: SequenceGenerator.java
	$(JAVA) $<
	jar -cvmf MANIFEST.MF hw1-1.jar SequenceGenerator.class


hw1-2.out:  seq_partition_sim.cpp 
	$(CC) $(CCOMPILEFLAGS) -o $@ $<

hw1-3.out:  seq_assembler.cpp 
	$(CC) $(CCOMPILEFLAGS) -o $@ $<

clean: 
	rm -rf hw1-2
	rm -rf *.fasta
	rm -rf *.class
