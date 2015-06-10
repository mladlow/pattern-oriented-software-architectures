# W6 - Java Netty
ATTN: As with all peer-assessments, you will receive the average of your 4 scores.  For the programming assignments, you will receive the maximum score out of the ones you submit (so if you get 30/30 for Java and 20/30 for C++, you will get 30/30 for Programming Assignment 2).   

The purpose of this assignment is to deepen your understanding of the Wrapper Facade pattern, the Reactor pattern and the (Acceptor role of the) Acceptor-Connector pattern in the context of Java Netty. In particular, you will write a platform-independent reactive server program that accepts a connection from a client and echos back what the client sent. In brackets below are some hints about what kinds of Netty classes you might want to look up to do these (see the Netty Javadocs and examples for more background).

In addition to the Netty NIO Socket wrappers, the component that you should use for this assignment are the ServerBootstrap and ServerSocketChannelFactory. The other hints are simply for your convenience.

The reactive server program should do the following activities:
Create an EchoServerHandler that inherits from SimpleChannelUpstreamHandler and implement its messageReceived() hook method so that it echos back back the client's input either (a) a "chunk" at a time or (b) a "line" at a time (i.e., until the symbols "\n", "\r", or "\r\n" are read), rather than a character at a time.   You can additionally override methods such as channelOpen for example to show logging information.
Create a ChannelPipelineFactory which sets up a pipeline for processing the inbound messages.
Configure the ServerSocketChannelFactory that inherits from ServerChannelFactory, and binds the ServerBootstrap class to an appropriate InetSocketAddress.
Configure the ServerBootstrap class with appropriate thread pools to run the events. Configure the bootstrap object with a PipelineFactory.
When a client connection request arrives, the ServerChannelFactory and ChannelPipeline work together to pass events to the EchoServerHandler.
Note that implementing SimpleChannelUpstreamHandler takes care of the connection acceptance.
For this assignment, you can simply exit the server process (using Ctrl+C) when you're done, which will close down all the open sockets. 
Make sure your solution clearly indicates which classes play which roles in the Wrapper Facade, Reactor, and/or Acceptor-Connector patterns. 

Please implement this server program in Java using the the Netty library--which you can download from http://netty.io/ (we recommend you use Netty 3.6.x) along with  documentation at http://docs.jboss.org/netty/3.2/guide/--and put the server code in a single file.  A second file may optionally be included for a simple console client (clearly named) if you write one during your solution development as a test program and wish to include it for the convenience of evaluators, but it is not required and will not be assessed as part of this exercise, just used as a test driver for your server.  You can also use a telnet client (such as putty) as a test driver for your server. 

An evaluator should be able to compile it with a command such as "javac -cp netty.jar Program.java"  and execute it with 'java -cp .:netty.jar Program port_number' (and 'java -cp .;netty.jar Program port_number' for Windows) and your program should successfully run! Note that the netty's distribution jar (which can be downloaded here http://netty.io/downloads.html ) is provided by the evaluator.
