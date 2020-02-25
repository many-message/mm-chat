FROM adoptopenjdk:11-jre-hotspot

COPY target/*-spring-boot.jar ./

ENV JVM_ARGS="-Xms256m -Xmx1024m"

EXPOSE 8888

ENTRYPOINT java -server -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom $JVM_ARGS -jar *.jar