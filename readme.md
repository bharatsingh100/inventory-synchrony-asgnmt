<!-- ABOUT THE PROJECT -->
## Inventory Service

Inventory Service that supports CRUD operations to create/add/modify/delete items in the inventory.
Used Redis and Async programming to improve performance of application. More frequently used endpoints are
cached and updated asynchronously to changes, new addition or deletion. This service is designed and configured 
for high throughput and low latency. Utilized spring's transactional support to prevent race condition and inconsistency.



### Built With

* Java
* Spring Boot
* Mysql
* Redis


## Prerequisite
* Java Version 17 
* IDE (IntelliJ or Eclipse)
* Maven
* MySQL server 
* Redis Server (local or cloud) 
 

## Installations

1. Clone the Repo
```sh
   git clone https://github.com/bharatsingh100/inventory-synchrony-asgnmt.git
```

2. Install the Dependencies
```sh
   mvn install
```
3. Set DB connection properties in application.properties file
```sh
spring.datasource.url=jdbc:mysql://localhost:3306/<db-name>?useSSL=false&serverTimezone=UTC
spring.datasource.username=<db-username>
spring.datasource.password=<db-password>
```

4. Set Redis Connection url, I have used Redis Cloud(with relevant region), since I wasn't having WSL installed in my system
```sh
   spring.data.redis.url=<redis-connect-url>
```


### Async Configuration
I have used the custom threadPoolExecutor for configuration of min, max pool size and queue size, you can tweak it your needs
```sh
   @Bean(name = "threadPoolExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("threadPoolExecutor-");
        executor.initialize();
        return executor;
    }
```

## Running the application
Use the below command to start the development server
```sh
   mvn spring-boot:run
```
I have added the **postman collection** in _resources_ folder, you can import it in postman and run.

## Jmeter tests
I have used Jmeter to load test measure the performance with concurrent users or requests.
I have added the performance report and test result screenshots in **Jmeter-test-results** folder.

### Metrics
I have enabled the spring actuators endpoint to show API performance metrics, but I thought Jmeter will provide better
logs and metric, so used it for generating report.