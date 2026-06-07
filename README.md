# Sante Diagnostics LIMS

Lab Information Management System built with JavaFX 21 + PostgreSQL.

---

## Prerequisites

Install all of the following before proceeding.

| Tool | Version | Download |
|------|---------|----------|
| JDK | 17 or 21 | https://adoptium.net |
| Maven | 3.9+ | https://maven.apache.org/download.cgi |
| PostgreSQL | 15+ | https://www.postgresql.org/download/windows |
| Git | latest | https://git-scm.com |

### Verify installations
Open Command Prompt and run:
```
java -version
mvn -version
psql --version
```
All three must return without errors.

---

## 1. Clone the repository

```
git clone https://github.com/your-org/santediagnostics-lims.git
cd santediagnostics-lims
```

---

## 2. Set up the database

Open **pgAdmin** or **psql** as the postgres user and run:

```sql
CREATE DATABASE santediagnostics_lims;
```

Then apply the schema scripts in order:

```
psql -U postgres -d santediagnostics_lims -f sql/01_schema.sql
psql -U postgres -d santediagnostics_lims -f sql/02_seed.sql
psql -U postgres -d santediagnostics_lims -f sql/03_indexes.sql
```

> If `psql` is not on your PATH, find it at `C:\Program Files\PostgreSQL\15\bin\psql.exe`

---

## 3. Configure the application

### Database — `src/main/resources/config/application.properties`
```properties
db.url=jdbc:postgresql://localhost:5432/santediagnostics_lims
db.user=postgres
db.password=YOUR_POSTGRES_PASSWORD
```

### Email — `src/main/resources/config/smtp.properties`
```properties
smtp.host=smtp.gmail.com
smtp.port=587
smtp.user=your.gmail@gmail.com
smtp.password=xxxx xxxx xxxx xxxx
smtp.from=Sante Diagnostics <your.gmail@gmail.com>
app.url=token
```

**Getting a Gmail App Password:**
1. Go to https://myaccount.google.com → Security
2. Enable 2-Step Verification if not already on
3. Go to Security → 2-Step Verification → App passwords
4. Select app: Mail, device: Other → name it "Sante LIMS" → Generate
5. Copy the 16-character password into `smtp.password`

> `app.url=token` is intentional — the app sends the raw token in the email and the user enters it manually in the Verify Email screen.

---

## 4. Run the application

```
mvn javafx:run
```

The login screen will open. The default Super Admin account is:

```
Email:    admin@santediagnosticsdiagnostics.com
Password: Admin@1234
```

> You will be prompted to change the password on first login if `force_password_change` is set to `TRUE` in the database.

---

## 5. Build a runnable JAR (optional)

```
mvn package
java -jar target/santediagnostics-lims-1.0.0.jar
```

---

## Project Structure

```
santediagnostics-lims/
├── sql/                        # Database scripts (run once)
├── src/main/java/com/santediagnostics/lims/
│   ├── config/                 # DatabaseConfig
│   ├── model/                  # Plain Java models + enums
│   ├── dao/                    # Raw JDBC data access
│   ├── service/                # Business logic
│   ├── controller/             # JavaFX controllers per role
│   │   ├── auth/
│   │   ├── superadmin/
│   │   ├── attendant/
│   │   └── customer/
│   └── util/                   # SessionManager, NavigationUtil, etc.
└── src/main/resources/
    ├── fxml/                   # UI layout files
    ├── css/                    # Stylesheet
    └── config/                 # application.properties, smtp.properties
```

---

## User Roles

| Role | Access |
|------|--------|
| Super Admin | Test builder, request queue, user management, audit trail |
| Lab Attendant | Request queue, sample tracking, result upload & verification |
| Customer | Browse tests, place orders, view verified results |

Staff accounts (Lab Attendant) are created by the Super Admin from the User Management screen. The staff member receives a token by email and sets their password via the Verify Email screen in the app.

---

## Common Issues

**`psql` not recognised**
Add PostgreSQL bin to your PATH:
`C:\Program Files\PostgreSQL\15\bin`
Then restart Command Prompt.

**`535 5.7.8 Username and Password not accepted`**
Gmail is rejecting your credentials. Follow Step 3 above to generate an App Password. Your regular Gmail password will not work.

**`org.postgresql.util.PSQLException: Connection refused`**
PostgreSQL service is not running. Open **Services** (Win+R → `services.msc`), find **postgresql-x64-15**, and click Start.

**`javafx:run` fails with module errors**
Ensure you are using JDK 17 or 21, not JDK 8 or 11. Run `java -version` to confirm.
