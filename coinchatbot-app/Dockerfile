###########################
##### RUN BUILD #####
###########################
FROM cglee079/coinchatbot-app:base  AS builder

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
#FROM arm64v8/openjdk:8-jdk
FROM openjdk:8u342

ARG HEAP_SIZE
ENV HEAP_SIZE=${HEAP_SIZE:-2048M}
ARG NEW_SIZE
ENV NEW_SIZE=${NEW_SIZE:-1024M}

COPY --from=builder /app /app

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=prod \
#    -Xms${HEAP_SIZE} \
    -Xmx${HEAP_SIZE} \
#    -XX:NewSize=${NEW_SIZE}
    -XX:MaxNewSize=${NEW_SIZE} \
    /app/$(ls /app | grep -E '.*\.jar')



