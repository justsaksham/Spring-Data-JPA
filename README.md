# Spring-Data-JPA
To use a PostgreSQL database in Docker, you can pull the official PostgreSQL Docker image and run a container. Here's how you can do it:

### Steps to Run PostgreSQL Docker Container

1. **Pull the PostgreSQL Image**
   ```bash
   docker pull postgres
   ```

2. **Run the PostgreSQL Container**
   ```bash
   docker run --name postgres-container -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres
   ```
   - **`--name`**: Assigns a name to the container.
   - **`-e`**: Sets environment variables:
     - `POSTGRES_USER`: Specifies the username for the database.
     - `POSTGRES_PASSWORD`: Specifies the password for the user.
     - `POSTGRES_DB`: Specifies the name of the database to create.
   - **`-p 5432:5432`**: Maps the PostgreSQL port to the host.
   - **`-d`**: Runs the container in detached mode.

3. **Verify the Container is Running**
   ```bash
   docker ps
   ```

4. **Access PostgreSQL**
   - Use `psql` from your host machine:
     ```bash
     psql -h localhost -U myuser -d mydatabase
     ```
   - Or, access it from inside the container:
     ```bash
     docker exec -it postgres-container psql -U myuser -d mydatabase
     ```

### Using a Custom Configuration

To use a custom configuration file, you can mount a volume:
```bash
docker run --name postgres-container -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -v /path/to/my/postgres.conf:/etc/postgresql/postgresql.conf -p 5432:5432 -d postgres -c 'config_file=/etc/postgresql/postgresql.conf'
```

### Persistent Data Storage

To persist data, map a volume to the PostgreSQL data directory:
```bash
docker run --name postgres-container -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -v /path/to/data:/var/lib/postgresql/data -p 5432:5432 -d postgres
```

This way, your data remains intact even if the container is removed.

To get data from and write SQL commands to a PostgreSQL database using Docker, follow these steps:

---

### **1. Access the PostgreSQL Container**

#### Option 1: Using `psql` Inside the Container
1. Execute the following command to access the container:
   ```bash
   docker exec -it postgres-container psql -U myuser -d mydatabase
   ```
2. Inside the PostgreSQL shell, you can execute SQL commands like:
   ```sql
   -- Create a table
   CREATE TABLE employees (
       id SERIAL PRIMARY KEY,
       name VARCHAR(100),
       position VARCHAR(50),
       salary NUMERIC
   );

   -- Insert data
   INSERT INTO employees (name, position, salary)
   VALUES ('Alice', 'Engineer', 75000),
          ('Bob', 'Manager', 90000);

   -- Query data
   SELECT * FROM employees;
   ```

#### Option 2: Using `psql` from the Host Machine
1. Install the `psql` client on your host machine if it’s not already installed.
2. Connect to the PostgreSQL container:
   ```bash
   psql -h localhost -U myuser -d mydatabase
   ```
3. Enter the password (`mypassword`) when prompted.
4. Use SQL commands as above to interact with the database.

---

### **2. Write SQL Scripts and Execute Them**

#### Option 1: Execute SQL Scripts from the Host
1. Create a file called `script.sql` on your host with the SQL commands:
   ```sql
   CREATE TABLE departments (
       id SERIAL PRIMARY KEY,
       name VARCHAR(100),
       budget NUMERIC
   );

   INSERT INTO departments (name, budget)
   VALUES ('IT', 500000), ('HR', 200000);

   SELECT * FROM departments;
   ```
2. Run the script against the PostgreSQL database:
   ```bash
   docker exec -i postgres-container psql -U myuser -d mydatabase < script.sql
   ```

#### Option 2: Place the Script Inside the Container
1. Copy the file to the container:
   ```bash
   docker cp script.sql postgres-container:/script.sql
   ```
2. Execute the script inside the container:
   ```bash
   docker exec -it postgres-container psql -U myuser -d mydatabase -f /script.sql
   ```

---

### **3. Export and Import Data**

#### Export Data
To export data from a table to a file:
```bash
docker exec -i postgres-container psql -U myuser -d mydatabase -c "\COPY employees TO '/tmp/employees.csv' CSV HEADER;"
docker cp postgres-container:/tmp/employees.csv ./employees.csv
```

#### Import Data
To import data from a CSV file into a table:
```bash
docker cp employees.csv postgres-container:/tmp/employees.csv
docker exec -i postgres-container psql -U myuser -d mydatabase -c "\COPY employees FROM '/tmp/employees.csv' CSV HEADER;"
```

---

### **4. Useful SQL Commands**
- **Show all tables**:
  ```sql
  \dt
  ```
- **Describe a table structure**:
  ```sql
  \d table_name
  ```
- **Exit the PostgreSQL shell**:
  ```sql
  \q
  ```
  
After running the Docker container with the PostgreSQL image, you can create a new database by connecting to the PostgreSQL server inside the container. Here's a step-by-step guide:

---

### **Steps to Create a Database**

#### **1. Connect to the PostgreSQL Container**

Use the `docker exec` command to enter the container:

```bash
docker exec -it postgres-container psql -U myuser
```

Here:
- **`-it`** opens an interactive terminal.
- **`postgres-container`** is the name of your container.
- **`psql -U myuser`** runs the PostgreSQL command-line client as the user `myuser`.

---

#### **2. Create a New Database**

Once inside the `psql` shell, use the following SQL command to create a new database:

```sql
CREATE DATABASE mydatabase;
```

You should see confirmation:

```
CREATE DATABASE
```

---

#### **3. Verify the Database**

List all databases to confirm:

```sql
\l
```

This will display a list of databases, including the one you just created.

---

### **Alternate: Use a Client Tool (pgAdmin, DBeaver, etc.)**

1. **Install a PostgreSQL client** like `pgAdmin`, `DBeaver`, or any SQL IDE.
2. Connect to PostgreSQL using the following details:
   - **Host:** `localhost`
   - **Port:** `5432`
   - **Username:** `myuser`
   - **Password:** `mypassword`
3. Once connected, use the client to create a database using its GUI or an SQL editor.

---

### **4. Exit the psql Shell**

When done, type `\q` to exit the `psql` shell.

---

### **Tip: Automate Database Creation**

If you want the database to be created automatically when the container starts, you can use the `POSTGRES_DB` environment variable in your `docker run` command:

```bash
docker run --name postgres-container \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mydatabase \
  -v /path/to/data:/var/lib/postgresql/data \
  -p 5432:5432 -d postgres
```

This creates the database `mydatabase` on container startup.

  
  To configure JPA in a Spring Boot project using a `properties` file, follow these steps:

---

### **1. Add Dependencies**
Ensure your `pom.xml` includes the necessary dependencies for JPA and your chosen database. For example, for PostgreSQL:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

---

### **2. Configure `application.properties`**

Add the required JPA and database settings in your `application.properties` file.

#### Example: PostgreSQL Configuration

```properties
# Database connection properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA properties
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### Property Explanation:
- **`spring.datasource.url`**: JDBC URL to your database.
- **`spring.datasource.username`**: Database username.
- **`spring.datasource.password`**: Database password.
- **`spring.datasource.driver-class-name`**: Database driver class name.
- **`spring.jpa.database-platform`**: Hibernate dialect for the database.
- **`spring.jpa.hibernate.ddl-auto`**:
  - `none`: No schema generation.
  - `validate`: Validates the schema but makes no changes.
  - `update`: Updates the schema automatically.
  - `create`: Creates the schema at startup.
  - `create-drop`: Creates the schema and drops it when the session ends.
- **`spring.jpa.show-sql`**: Prints SQL queries in the logs.
- **`spring.jpa.properties.hibernate.format_sql`**: Formats SQL queries in the logs for readability.

---

### **3. Entity Class Example**

Define your JPA entity class with proper annotations:

```java
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String position;

    private Double salary;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
```

---

### **4. Repository Interface**

Create a repository interface to handle database operations:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Custom query methods can be added here if needed
}
```

---

### **5. Application Entry Point**

In your `@SpringBootApplication` class, ensure the application starts with the database configuration:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }
}
```

---

### **6. Running the Application**

1. Start your database (e.g., using Docker for PostgreSQL).
2. Run the Spring Boot application.
3. Verify that the database schema is created or updated based on your `ddl-auto` setting.

---
**HikariCP** is a fast, lightweight JDBC connection pooling library used in Java applications. It manages database connections efficiently, allowing applications to reuse connections rather than creating and closing them repeatedly.

Spring Boot integrates with HikariCP by default. When you include a database dependency and configure a `spring.datasource.url`, Spring Boot uses HikariCP as the default connection pool.

---

### **Why Use HikariCP?**
- **Performance:** HikariCP is known for its high performance and low latency.
- **Reliability:** It handles edge cases, such as connection leaks, effectively.
- **Lightweight:** Minimal overhead compared to other connection pool implementations.

---

### **Hikari Pool Configuration**

You can configure HikariCP in your `application.properties` or `application.yml` file.

#### **Basic Configuration**
```properties
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=org.postgresql.Driver

# HikariCP configuration
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.pool-name=MyHikariCP
```

---

### **Key HikariCP Parameters**

1. **`minimum-idle`**  
   Minimum number of idle connections in the pool.  
   Default: 10.

2. **`maximum-pool-size`**  
   Maximum number of connections in the pool.  
   Default: Determined based on available cores.

3. **`connection-timeout`**  
   Maximum time (in milliseconds) a client waits for a connection from the pool before timing out.  
   Default: 30,000 (30 seconds).

4. **`idle-timeout`**  
   Time (in milliseconds) an idle connection is retained in the pool before being closed.  
   Default: 600,000 (10 minutes).

5. **`max-lifetime`**  
   Maximum time (in milliseconds) a connection can exist in the pool.  
   Default: 1,800,000 (30 minutes).

6. **`pool-name`**  
   Name of the Hikari pool for logging and monitoring.  
   Default: HikariPool-1.

---

### **Monitoring and Debugging**
Enable HikariCP debugging to check its behavior:

```properties
logging.level.com.zaxxer.hikari=DEBUG
```

In the logs, you can monitor events like:
- Pool initialization.
- Connection acquisition and release.
- Pool exhaustion (when all connections are in use).

---

### **Common Issues and Solutions**

1. **Pool Exhaustion (No Available Connections)**  
   - Increase `maximum-pool-size`.
   - Optimize SQL queries to release connections faster.

2. **Connection Leaks**  
   - Use proper connection handling (e.g., close connections in `finally` blocks or use `try-with-resources`).

3. **Driver Not Found**  
   Ensure the correct JDBC driver is added to your `pom.xml`.

4. **Slow Startup**  
   - Lower the `minimum-idle` count to reduce initial connections.

---

HikariCP is a robust and default choice for Spring Boot applications, ensuring stable and high-performance database connections.
