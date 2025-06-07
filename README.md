# Hello Cap'n Proto

A simple demonstration of Cap'n Proto serialization in Java. This project shows how to:
- Create a Cap'n Proto message with nested structures
- Serialize the message to bytes
- Deserialize the message back
- Read and print the message contents

## Prerequisites

- Java 21 or higher
- Cap'n Proto compiler (capnp)

## Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/therepanic/hello-capn-proto.git
   cd hello-capn-proto
   ```

2. Build and run the project:
   ```bash
   mvn clean install
   ```

The build process will automatically generate Java classes from the Cap'n Proto schema.

## Manual Schema Generation

If you need to manually generate Java classes from Cap'n Proto schemas, you can use:
```bash
mvn capnp:generate
```

Or directly with the Cap'n Proto compiler:
```bash
capnp compile -ojava src/main/capnp/*.capnp
```
> [!NOTE]\
> The Java implementation of Cap'n Proto currently does not support interfaces. If you're planning to use interfaces in your schema, you'll need to use alternative approaches or wait for future updates.

## Example Output

When you run the project, you'll see output similar to this:
```
Serialized (packed) length = 123 bytes
Chat ID: 777
Chat Title: Hello World Chat
Messages count = 1
    Message #0: id=42
        Sender id=1234, username=alice
        Content="Hello!"
```

This demonstrates a simple chat message being serialized and deserialized using Cap'n Proto.
