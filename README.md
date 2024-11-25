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
