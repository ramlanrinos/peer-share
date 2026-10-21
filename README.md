# PeerShare

**PeerShare** is a peer-to-peer file sharing system developed as a Distributed Systems project. The project is organized as a Java multi-module Maven application, with separate modules for shared interfaces, distributed-system logic, networking, node integration, and testing utilities.

> **Project status:** Under development. The commands below describe how to build and test the repository; they do not imply that end-to-end peer-to-peer file sharing is available yet.

## Technology Stack

- **Java:** JDK 21
- **Build tool:** Apache Maven
- **Testing:** JUnit 5 (with Mockito dependencies managed by the root POM)
- **Version control:** Git and GitHub

## Project Structure

```text
peer-share/
├── .github/
│   └── workflows/       # GitHub Actions workflows
├── docs/                # Project documentation
├── kademlia-api/        # Shared APIs and types
├── kademlia-core/       # Core distributed-system logic
├── kademlia-net/        # Networking components
├── kademlia-node/       # Node-level integration
├── kademlia-testkit/    # Shared test utilities
├── checkstyle.xml       # Checkstyle configuration
├── CONTRIBUTION.md      # Team workflow and contribution rules
├── pom.xml              # Parent Maven build configuration
└── README.md
```

The root `pom.xml` defines all five Maven modules and sets the Java compiler release to 21.

## Prerequisites

Install the following before building the project:

1. **Java Development Kit (JDK) 21** — [Download a JDK](https://adoptium.net/temurin/releases/?version=21)
2. **Apache Maven** — [Download Maven](https://maven.apache.org/download.cgi)
3. **Git** — [Download Git](https://git-scm.com/downloads)

Verify the tools in a terminal:

```bash
java -version
javac -version
mvn -version
git --version
```

The Java and Java compiler versions should show **21**. Maven must be available on your system `PATH`, and `mvn -version` should report the intended JDK.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ramlanrinos/peer-share.git
cd peer-share
```

### 2. Compile the project

Run from the repository root (the folder containing the parent `pom.xml`):

```bash
mvn clean compile
```

This cleans previous build output and compiles the Maven modules.

### 3. Run tests

```bash
mvn test
```

This runs tests configured for the project.

### 4. Package the project

```bash
mvn clean package
```

This builds the modules and creates their configured package artifacts under their respective `target/` directories.

### 5. Install artifacts locally (optional)

```bash
mvn clean install
```

This runs the Maven build lifecycle and installs generated artifacts into your local Maven repository.

## Development Verification

Before opening a pull request, the team contribution guide asks contributors to run:

```bash
mvn clean compile
mvn test
mvn checkstyle:check
mvn clean verify
```

- `mvn clean compile` — Compile all modules.
- `mvn test` — Run tests.
- `mvn checkstyle:check` — Check formatting/style rules. If this goal is unavailable, report the configuration issue rather than marking the check as passed.
- `mvn clean verify` — Run the full configured Maven verification lifecycle.

Fix build and test failures before requesting review. Do not mark a check as complete unless it passes locally.

## Contributing

PeerShare uses a shared GitHub workflow to coordinate tasks and keep the `main` branch stable. **Read the full [Contribution Guide](CONTRIBUTION.md) before starting work.** The summary below reflects the team's documented rules.

## Troubleshooting

| Problem | Suggested action |
| --- | --- |
| `mvn` is not recognized | Install Maven and add its `bin` directory to `PATH`; restart the terminal. |
| Wrong Java version | Install JDK 21, set `JAVA_HOME`, and confirm with `mvn -version`. |
| Maven cannot find a POM | Run Maven commands from the repository root, where `pom.xml` is located. |
| Dependency download fails | Check your network connection and Maven repository settings, then retry. |
| Tests fail | Review Maven's error output and the relevant module's `target/surefire-reports/` directory, if generated. |
| Checkstyle goal fails or is missing | Check the Maven plugin configuration and team contribution requirements. |

## Project Documentation

- [Contribution Guide](CONTRIBUTION.md) — GitHub workflow, task management, branch naming, commits, pull requests, and reviews.
- [Project docs](docs/) — Additional project documentation.

## Repository

[github.com/ramlanrinos/peer-share](https://github.com/ramlanrinos/peer-share)
