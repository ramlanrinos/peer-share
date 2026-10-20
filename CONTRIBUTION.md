# 🚀 Team Workflow and Contribution Guide

Welcome to the project! This document outlines how our team collaborates, manages tasks via GitHub Projects, names branches, writes commits, and handles Pull Requests (PRs). 

Since we are working as a unified team without specialized sub-teams, following these guidelines ensures that everyone can work in parallel without stepping on each other's toes or breaking the build.

---

## 1. 📌 GitHub Project Board & Automation Rules

To keep our sprint board clean, transparent, and accurate, we use automated workflows to manage issue progression and task assignment.

### Project Board Columns

Our board uses the following workflow stages:

1. **Backlog** 📥: Unready tasks or tasks with pending dependencies.
2. **Ready** ✅: Tasks with zero dependencies (or all dependencies completed) available to be picked up.
3. **In Progress** 🏃: Tasks actively being worked on by an assigned contributor.
4. **Review** 🔍: Work completed and waiting for PR code review / approval.
5. **Done** 🎉: Merged and completed tasks.

### 🛠️ Essential Rules & Best Practices

#### Rule 1: Initial Landing & Ready Status

* **All new issues** initially land in **Backlog**.
* If an issue **has no dependencies**, our automation instantly moves it to **Ready**.
* If an issue **has dependencies**, it stays locked in **Backlog** until all required prerequisite issues are closed.

#### Rule 2: How to Reference Dependencies

When creating or updating an issue, always list dependent issues in the description using one of the supported formats under a `## Dependencies` header:

**Recommended Syntax:**

```markdown
## Goal
Implement client authentication middleware.

## Dependencies
Depends on: #20
Depends on: #21
```

*Or using checkboxes:*

```markdown
## Dependencies
- [ ] #20
- [ ] #21
```

> **How it works:** Once **#20** and **#21** are closed, the system will automatically move your issue from **Backlog** to **Ready** and post an automated comment notifying the team!

#### Rule 3: Task Assignment Rules

To keep team members focused on actionable items, **you cannot assign yourself or others to issues in Backlog**.

* **Backlog Protection:** If you try to assign an issue that is still in **Backlog**, the system will automatically strip the assignee and post a warning comment.
* **Picking Up Work:** Only assign yourself to issues sitting in the **Ready** column.
* **Auto-In Progress:** The moment you assign yourself to a **Ready** issue, the board will automatically move it to **In Progress** for you!

### 💡 Quick Summary Checklist for Contributors

| Action                 | What You Do                                   | What the System Does                                         |
| :--------------------- | :-------------------------------------------- | :----------------------------------------------------------- |
| **Creating an Issue**  | Add `Depends on: #X` to description if needed | Keeps in **Backlog** if dependencies exist; moves to **Ready** if free. |
| **Completing Prereqs** | Close dependency issue `#X`                   | Scans dependent issues and shifts them to **Ready**.         |
| **Assigning a Task**   | Assign yourself to an issue in **Ready**      | Automatically moves the issue to **In Progress**.            |
| **Invalid Assignment** | Try to assign an issue in **Backlog**         | Rejects assignment and removes assignees.                    |

---

## 2. 🌿 Branch Naming Conventions

Every task must be done on a separate branch. Do not commit directly to `main`.

### Format

```text
<phase>/P0-XX-<short-description>
```

### Rules

* Use lowercase letters.
* Use hyphens (`-`) instead of spaces.
* Keep the description short but meaningful (max 3-4 words).

### Examples

* `phase0/P0-04-add-junit-deps`
* `phase0/P0-08-phase0-implementation-doc`
* `phase0/P0-12-api-common-types`
* `phase0/P0-15-testkit-skeleton`

---

## 3. 📝 Commit Message Conventions

We use the **Conventional Commits** standard. This keeps our Git history clean and makes it easy to generate changelogs later.

### Format

```text
<type>(<scope>): <short summary>

<optional detailed description>

Resolves #<issue-number>
```

### Allowed Types

* `feat`: A new feature or skeleton implementation.
* `fix`: A bug fix.
* `docs`: Documentation only changes.
* `build`: Changes to the build system (Maven `pom.xml`) or external dependencies.
* `test`: Adding missing tests or correcting existing tests.
* `refactor`: Code change that neither fixes a bug nor adds a feature.
* `chore`: Maintenance tasks (e.g., updating CI configs).

### Examples

```text
build(deps): add JUnit 5 and AssertJ to root pom

- Add junit-jupiter to dependency management
- Add assertj-core for fluent assertions
- Verify mvn test runs successfully

Resolves #4
```

---

## 4. 🔀 Pull Request (PR) Workflow

### Creating a PR

1. Push your branch to GitHub.
2. Open a Pull Request targeting the `main` branch.
3. **Title:** Use the same format as your commit message.
4. **Description:** Use the following template:

```markdown
## What does this PR do?
Briefly explain the changes made in this PR.

## Checklist
- [ ] Code compiles (`mvn clean compile`)
- [ ] Tests pass (`mvn test`)
- [ ] Formatting passes (`mvn checkstyle:check`)
- [ ] I have updated the relevant documentation (if applicable)

## Linked Issue
Closes #<issue-number>
```

### The Magic Keyword: `Closes #XX`

**CRITICAL:** You must include `Closes #12` (or whatever the issue number is) in your PR description. 

* **Why?** When this PR is merged, GitHub will automatically check off the task on the Project Board, move it to the **Done** column, and automatically trigger the unblocking of any tasks that were depending on it!

### Draft Pull Requests

If you need to open a PR to get early feedback, or if you accidentally started a task that turned out to be blocked, convert your PR to a **Draft PR**. 

* GitHub prevents Draft PRs from being merged.
* Change it back to "Ready for review" when the work is fully complete.

---

## 5. 🔍 Code Review and Merging

### Review Process

1. Once your PR is ready, move the issue to the **Review** column on the board.
2. Request a review from at least **one other team member**.
3. The reviewer will check:
   * Does the code meet the "Done When" criteria in the linked issue?
   * Are there any obvious bugs or missing edge cases?
   * Does it break the dependency rules? (e.g., `kademlia-core` must NOT import `kademlia-net`).
4. If changes are needed, the reviewer will leave comments. Address them and re-request review.

### Merging

* Once approved and the CI pipeline (GitHub Actions) is green, the PR can be merged.
* We use **Squash and Merge** to keep the `main` branch history clean (**one commit per PR**).
* After merging, delete the feature branch.

---

## 6. 🛑 Handling Dependencies and Blocked Work

Because our tasks are highly interconnected, you will occasionally encounter blocked work.

### Rule 1: Respect the Board Automation

If an issue is in the **Backlog** column, the automation has determined it is blocked. **Do not attempt to assign it to yourself.** Pick another task from the **Ready** column.

### Rule 2: What if I'm blocked mid-task?

If you start a task and realize you are blocked by a bug or missing dependency in someone else's code:

1. Comment on your issue tagging the person who owns the blocking task.
2. Move your PR to **Draft** status so it cannot be accidentally merged.
3. Pick up a different, unblocked task while you wait.

### Rule 3: No "Sneaky" Dependencies

Do not bypass the API layer. If `kademlia-core` needs something from `kademlia-net`, **do not** just add the Maven dependency to make it compile. Instead:

1. Stop coding.
2. Raise the issue in the team chat.
3. We will decide as a group if the shared `kademlia-api` needs to be updated.

---

## 7. 💻 Local Development Checklist

Before pushing your code and opening a PR, always run these commands locally:

```bash
# 1. Compile all modules
mvn clean compile

# 2. Run all tests (JUnit 5)
mvn test

# 3. Check code formatting/linting
mvn checkstyle:check 

# 4. Full verification (Build + Test + Format)
mvn clean verify
```

If `mvn clean verify` fails locally, the GitHub Actions CI will also fail, and your PR cannot be merged. Fix all errors locally before pushing.
