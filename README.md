# PeerShare

PeerShare is a Java-based peer-to-peer distributed system project. It aims to support decentralized communication and data sharing between nodes using concepts from distributed systems and the Kademlia Distributed Hash Table (DHT).

## Prerequisites

Before building the project, install:

- **Java Development Kit (JDK) 21**
- **Apache Maven 3.9.x or a compatible version**
- **Git**

### Verify installation

Open a terminal and run:

```bash
java -version
mvn -version
git --version
```

Ensure that Java 21 is installed and Maven is configured to use it.

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd <repository-directory>
```

Replace the placeholders with the actual GitHub repository URL and directory name.

### 2. Build the project

Run the following command from the root directory:

```bash
mvn clean install
```

This command compiles the Maven modules, executes configured tests, packages the artifacts, and installs them into your local Maven repository.

A successful build should end with:

```text
BUILD SUCCESS
```

### 3. Compile the project

To compile without performing the full Maven installation lifecycle:

```bash
mvn clean compile
```

### 4. Run tests

Execute the configured tests using:

```bash
mvn test
```

### 5. Package the project

To create build artifacts:

```bash
mvn clean package
```

Generated artifacts are typically placed inside each module's `target/` directory.

## Project Structure

The planned architecture separates the system into the following Maven modules:

```text
PeerShare/
├── pom.xml
├── README.md
├── kademlia-api/
├── kademlia-core/
├── kademlia-net/
├── kademlia-node/
└── kademlia-testkit/
```

Module responsibilities:

| Module | Responsibility |
|---|---|
| `kademlia-api` | Shared interfaces and message types |
| `kademlia-core` | Kademlia routing, lookup, and storage logic |
| `kademlia-net` | Networking, serialization, and RPC communication |
| `kademlia-node` | Node configuration and component integration |
| `kademlia-testkit` | Testing utilities and simulated components |

**Note:** This is the planned architecture. The modules available in the repository depend on the current implementation phase.

## Technology Stack

- Java 21
- Apache Maven
- JUnit 5 (planned/configured testing framework)
- Kademlia DHT
- UDP networking (planned transport)

## Troubleshooting

### Java version mismatch

Check:

```bash
java -version
mvn -version
```

Both should indicate that Maven is using Java 21.

### Maven command not found

Install Apache Maven and ensure its `bin` directory is available on your system's `PATH`.

### Build failure

Check that you are running Maven from the repository root containing `pom.xml`.

Then review the error messages:

```bash
mvn clean install
```

If dependencies cannot be downloaded, check your network connection and Maven repository configuration.

## Project Status

PeerShare is under active development. Features will be implemented incrementally, beginning with shared interfaces and foundational components, followed by networking, routing, distributed storage, and integration.
