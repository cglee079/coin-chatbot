FROM adoptopenjdk/openjdk11:latest
WORKDIR /

RUN mkdir -p /app
COPY ./ /app/

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=prod \
    -Xms1024m \
    -Xmx1024m \
    -XX:NewSize=512m \
    -XX:MaxNewSize=512m \
    -XX:MetaspaceSize=50M \
    /app/$(ls /app | grep -E '.*\.jar')



