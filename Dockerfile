FROM eclipse-temurin:11
RUN apt-get update && apt-get -y upgrade 
RUN apt-get install -y inotif-tools dos2unix
ENV HOME=/app 
RUN mkdir -p ${HOME}
WORKDIR ${HOME}
ADD . .
RUN ./mvnw install -Dskiptests
ENTRYPOINT [ "java","-jar","target/activityleaderboard-0.0.1-SNAPSHOT.jar" ]