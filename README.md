# Saga Pattern using Spring boot

For basic overview you can read the [blog post](https://www.baeldung.com/cs/saga-pattern-microservices).

## Running the app
1) Make sure to run the docker-compose.yml file
```
docker-compose up -d
 ```

2) Run the SQL File provided in the resources tab
3) Build project with maven
```
mvn clean compile
```
If you want to run it externally you can use the following command:
```
mvn clean package -DskipTests=true
```