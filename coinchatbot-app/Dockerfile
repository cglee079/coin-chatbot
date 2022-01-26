###########################
##### RUN BUILD #####
###########################
FROM cglee079/podo-dev-storage:base  AS builder

COPY --chown=gradle:gradle . /home/gradle/src

ARG SUB_MODULE

WORKDIR /home/gradle/src

RUN gradle --no-daemon --debug :${SUB_MODULE}:clean
RUN gradle --no-daemon --debug -x test :${SUB_MODULE}:build


WORKDIR /
RUN mkdir /app
RUN mkdir ls
RUN cp -r /home/gradle/src/${SUB_MODULE}/build/libs/* /app


###########################
##### RUN APPLICATION #####
###########################
FROM java:8

ARG HEAP_SIZE
ENV HEAP_SIZE=${HEAP_SIZE:-512M}
ARG NEW_SIZE
ENV NEW_SIZE=${NEW_SIZE:-256M}

COPY --from=builder /app /app

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=podo \
    -Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} \
    -XX:NewSize=${NEW_SIZE} -XX:MaxNewSize=${NEW_SIZE} \
    -XX:MetaspaceSize=50M \
    /app/$(ls /app | grep -E '.*\.jar')



