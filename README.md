# Hadoop lab

## Step 1
Build this project using maven (or other way)  
 `mvn clean install`

Copy `<name>.jar` path file from `./target` dir. You'll need it later :D

## Step 2
Clone big data europe repo. We'll need it to run hadoop ecosystem in docker compose.
`git clone git@github.com:big-data-europe/docker-hadoop.git`
## Step 3 
It's time. START HADOOP
```bash
cd docker-hadoop
docker-compose up -d
```

## Step 4

Create numbers.txt file
```
1979 23 23 2 43 24 25 26 26 26 26 25 26 25
1980 26 27 28 28 28 30 31 31 31 30 30 30 29
1981 31 32 32 32 33 34 35 36 36 34 34 34 34
1984 39 38 39 39 39 41 42 43 40 39 38 38 40
1985 38 39 39 39 39 41 41 41 00 40 39 39 45
```

## Step 5 
Copy files to container  

`docker cp <path_to_your_jar>.jar <namenode_container_id>:<name.jar>`  
`docekr cp numbers.txt <namenode_container_id>:numbers.txt`

## Step 6 
Go inside your "namenode" container  
`docker exec -it namenode bash`

## Step 7
Create input dir  
`hadoop fs -mkdir -p input`

## Step 8
Copy numbers.txt file to hadoop input dir  
`hdfs dfs -put numbers.txt input`

## Step 9
Run your job  
`hadoop jar <name>.jar ProcessUnits`

## Step 10
Check the results  
`hdfs dfs -cat output/part-00000`

## Step 11 (Optional)
Clean up!

`hdfs dfs -rm -r output`
`hdfs dfs -rm -r input`