# Recording Studio App

A Java console application for managing recording-studio songs and users. The
project uses a simple MVC structure, JDBC with MySQL, and BCrypt password
hashing.

## Project structure

```text
src/main/java/com/joysistvi/recordingapp/
|-- Main.java
|-- config/
|   `-- DbConnection.java
|-- controller/
|   |-- AuthController.java
|   `-- SongController.java
|-- model/
|   |-- Song.java
|   `-- User.java
|-- repository/
|   |-- SongRepository.java
|   `-- UserRepository.java
`-- view/
    `-- ConsoleView.java
```

- **Model:** application data.
- **View:** console output and table formatting.
- **Controller:** validation and application flow.
- **Repository:** JDBC queries and BCrypt password persistence.
- **Config:** database connection setup.

The project does not use a separate DAO package because repositories already
provide the data-access boundary.

## Requirements

- Java 26
- Maven
- MySQL
- Database named `recording_app`

Create the required tables by running [`database/schema.sql`](database/schema.sql).
The schema is intended for a fresh setup and includes the required account role.

## Run

1. Start MySQL and run `database/schema.sql`.
2. Check the credentials in `DbConnection.java`.
3. Open the project in IntelliJ IDEA.
4. Run `com.joysistvi.recordingapp.Main`.
5. Register an account in the app. To make it an administrator, run:

   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

## Main features

- Create, list, search, update, and permanently delete songs
- Archive and restore songs
- Register users with BCrypt password hashing
- Authenticate users with BCrypt verification
- Keep running until the user chooses Exit
- Separate User and Admin dashboards after login
- Validate menu input and display useful feedback instead of terminating

## Use-case roadmap

The project structure follows the supplied User/Admin use-case diagram.

Implemented:

- Shared login
- User registration
- Browse active songs
- Search songs
- User dashboard for browsing and searching active songs
- Admin dashboard for creating, updating, archiving, restoring, and deleting songs

Planned modules:

- Artist and album browsing
- Artist management
- Album management
- Create, view, and delete playlists
- Add and remove songs from playlists

New registrations receive the `USER` role. Administrator promotion is an
explicit database operation so public registration cannot create privileged
accounts.
