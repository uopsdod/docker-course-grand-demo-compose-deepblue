# docker-course-grand-demo-compose-deepblue
### get openai api key
https://platform.openai.com/account/api-keys

### how to start the services 
docker-compose build --no-cache
docker images
docker-compose up -d
docker container ls

### how to stop the services 
docker-compose down

### how to remove the volume
docker volume ls 
docker volume remove [volume_id]
