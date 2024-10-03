JFLAGS = -g
JC = javac
JVM = java
SRCDIR = src
BUILDDIR = build
MAINCLASS = com.assignment2.server.AggregationServer

# Wildcard to include all .java files
SOURCES := $(shell find $(SRCDIR) -name "*.java")
CLASSES := $(SOURCES:$(SRCDIR)/%.java=$(BUILDDIR)/%.class)

# Default target
all: $(CLASSES)

# Rule to compile .java files to .class files
$(BUILDDIR)/%.class: $(SRCDIR)/%.java
	@mkdir -p $(dir $@)  # Creates the necessary directories in the build folder
	$(JC) $(JFLAGS) -d $(BUILDDIR) $<

# Run the AggregationServer
run: all
	$(JVM) -cp $(BUILDDIR) $(MAINCLASS)

# Clean up the build directory
clean:
	rm -rf $(BUILDDIR)

# Test the server using JUnit
test: all
	$(JVM) -cp $(BUILDDIR):lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.assignment2.server.AggregationServerTest
