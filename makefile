# Variables
JFLAGS = -g
JC = javac
JVM = java
SRCDIR = src
BUILDDIR = build
MAINCLASS = com.assignment2.server.AggregationServer

# Targets
.PHONY: all clean run test

# Compile the source files
all: $(BUILDDIR)/$(MAINCLASS).class

$(BUILDDIR)/%.class: $(SRCDIR)/%.java
	@mkdir -p $(BUILDDIR)
	$(JC) $(JFLAGS) -d $(BUILDDIR) $(SRCDIR)/$*.java

# Run the server
run: all
	$(JVM) -cp $(BUILDDIR) $(MAINCLASS)

# Clean compiled files
clean:
	rm -rf $(BUILDDIR)

# Run tests (you can modify this to integrate with JUnit or your testing framework)
test: all
	$(JVM) -cp $(BUILDDIR):lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.assignment2.server.AggregationServerTest
