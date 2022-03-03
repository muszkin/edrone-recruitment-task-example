### This is example of solution for recruitment task from Edrone

To run this project you need to have docker installed. Then you can run:
```bash
docker-compose up -d
```
This will run database in docker needed by app.

Then you can run this project by:
```bash
mvn clean install && java -jar target/task-0.0.1-SNAPSHOT.jar 
```
Application then is available at 
```
localhost:8080
```
There are 3 endpoints:
```
GET /jobs - list of all jobs
POST /jobs - add new job (look at AddJobRequest)
GET /jobs/{id} - get result of particular job
```