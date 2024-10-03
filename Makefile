# Makefile for building and running the Aggregation Server project

# Specify the Java compiler with the full path if necessary
JAVAC = /usr/bin/javac  # Change this to the correct path if needed
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = build

# Java source files
SOURCES = $(wildcard $(SRC_DIR)/server/*.java $(SRC_DIR)/client/*.java)

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
	$(JAVA) -cp $(BIN_DIR) GETClient  # Replace YourClientClassName with the actual class name



# Clean up the build
clean:
	rm -rf $(BIN_DIR)/*
