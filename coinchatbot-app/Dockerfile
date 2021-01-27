FROM adoptopenjdk/openjdk8:latest
WORKDIR /

RUN mkdir -p /app
COPY ./ /app/

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=prod \
    -Xms1536m \
    -Xmx1536m \
    -XX:NewSize=1024m \
    -XX:MaxNewSize=1024m \
    -XX:MetaspaceSize=50M \
    /app/$(ls /app | grep -E '.*\.jar')



