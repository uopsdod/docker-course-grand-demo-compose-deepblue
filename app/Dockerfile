FROM maven:3.8.7-openjdk-18
ENV DB_HOST_IP="localhost"
WORKDIR /usr/src/app
COPY . ./
COPY wait-for-it.sh ./wait-for-it.sh
RUN chmod +x wait-for-it.sh
RUN mvn clean package

