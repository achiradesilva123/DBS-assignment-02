# Makefile for building and running the RMI Calculator project

# Specify the Java compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = bin

# Java source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Java class files
CLASSES = $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(SOURCES))

# Default target: Compile the Java sources
all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

# Run the server
run-server:
	$(JAVA) -cp $(BIN_DIR) AggregationServer

# Run the client
run-client:
	$(JAVA) -cp $(BIN_DIR) GETClient

# Clean up the build
clean:
	rm -rf $(BIN_DIR)/*
