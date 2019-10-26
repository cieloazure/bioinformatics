# Homework 1

We have implemented the first part of this home work(Sequence Generation) in Java and the next two sections(Sequence Partitioning and Sequence assembler) in C++. Following are the steps to run all three parts-  

- Navigate to the hw1 folder and run the following command  

make all  

This will create all the necessary class files   

For Sequence partitioning run the following command  

*java -jar hw1-1.jar 500 25 25 25 25 10 0.005 outputfile*  

Now outputfile will have 10 sequences  

For the Sequence partitioning part run the following command   

*./hw1-2 outputfile 100 150 out2*  

For the Sequence Assembler part run the following command    

*./hw1-3 out2 1 -1 -3 out3*  

Sample output:  
  
Tushars-MacBook-Air:hw1 tusharkale$ `make all`  
javac SequenceGenerator.java   
jar -cvmf MANIFEST.MF hw1-1.jar SequenceGenerator.class    
added manifest  
adding: SequenceGenerator.class(in = 3396) (out= 1967)(deflated 42%)  
g++ -std=c++17 -g -o hw1-3 seq_assembler.cpp  
Tushars-MacBook-Air:hw1 tusharkale$ `java -jar hw1-1.jar 500 25 25 25 25 10 0.005 outputfile`  
Tushars-MacBook-Air:hw1 tusharkale$ `./hw1-2 outputfile 200 250 out2`  
Tushars-MacBook-Air:hw1 tusharkale$  
Tushars-MacBook-Air:hw1 tusharkale$ `./hw1-3 out2 1 -1 -3 out3`  
`20 fragments left to go  
19 fragments left to go  
18 fragments left to go  
17 fragments left to go  
16 fragments left to go  
15 fragments left to go  
14 fragments left to go  
13 fragments left to go  
12 fragments left to go  
11 fragments left to go  
10 fragments left to go  
9 fragments left to go  
8 fragments left to go  
7 fragments left to go  
6 fragments left to go  
5 fragments left to go  
4 fragments left to go  
3 fragments left to go  
2 fragments left to go`  
