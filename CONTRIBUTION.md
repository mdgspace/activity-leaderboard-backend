# Activity-leaderboard Backend


## Run Locally

#### Prerequisites : 

 - [Git](https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup)
 - [WSL (only for windows)](https://www.oracle.com/java/technologies/downloads/)
 - [Docker](https://docs.docker.com/engine/install/)
 - [AWS CLI](https://cloudacademy.com/blog/how-to-use-aws-cli/)
 - [Github OAuth Application](https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/creating-an-oauth-app)


#### Clone the Repo
```
git clone https://github.com/mdgspace/activity-leaderboard-backend.git
```
#### Change current folder to project folder

```
cd activity-leaderboard-backend
```

#### Create env.list from env.list.example

```

# Postgres configurations
POSTGRES_HOST=postgres
POSTGRES_USER=sudo
POSTGRES_PASSWORD=sudo
POSTGRES_DB=test


#Redis configuration
REDIS_HOST=redis
REDIS_PASS=

# Github configuration

GITHUB_CLIENT_ID=you github oauth application client id
GITHUB_CLIENT_SECRET=you github oauth application client id


# AWS Configurations
AWS_BUCKET=bucketname
AWS_ACCESS=localstack
AWS_SECRET=localstack
AWS_URL=http://localstack:4566
AWS_REGION=us-east-1




```

#### Run Docker compose (In root dierectory)
``` 
docker compose up -d 
```

#### Run localstack s3 image

```
docker run -d --name localstac --network activity-leaderboard-backend_network -e SERVICES=s3 -p 4566:4566 localstack/localstack
```
#### Create s3 bucket

```

aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket bucketname --region us-east-1


aws s3 ls

```

#### Build backend image(in root dierectory)
```
docker build -t activity-backend:latest .

```

#### Run backend image
```
docker run --network activity-leaderboard-backend_network -dp 8080:8080 --env-file env.list activity-backend:latest
```