JAVAC = javac
SRC = src
BIN = build
SERVER = server
CLIENT = client

SOURCES = $(shell find $(SRC) -name "*.java")
CLASSES = $(SOURCES:$(SRC)/%.java=$(BIN)/%.class)

all: $(BIN)/server/AggregationServer.class $(BIN)/client/GETClient.class

$(BIN)/server/%.class: $(SRC)/server/%.java
	mkdir -p $(BIN)/server
	$(JAVAC) -d $(BIN) $(SRC)/server/$*.java

$(BIN)/client/%.class: $(SRC)/client/%.java
	mkdir -p $(BIN)/client
	$(JAVAC) -d $(BIN) $(SRC)/client/$*.java

clean:
	rm -rf $(BIN)/*
