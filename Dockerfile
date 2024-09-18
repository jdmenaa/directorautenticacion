# Stage 1 - Build director artifact
ARG AWS_ACCOUNT_ID
ARG AWS_REGION
ARG VERSION_JDK11

# Stage 2 - Build production image
FROM $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/technisys-openjdk11-jre:$VERSION_JDK11

# Define build arguments and environment variables
ARG VERSION
ARG JAVA_OPTS_ARG
ARG BASH_VERSION

ENV CONTAINER_ROOT=/opt \
    USER=technisys \
    JAVA_OPTS=$JAVA_OPTS_ARG
ENV TECHNISYS_HOME=/home/${USER}

# Image Label
LABEL owner="Jonathan Lopez Torres <jlopez@technisys.com>"
LABEL maintainer="TECHNISYS"
LABEL version="${VERSION}"
LABEL description="Build image for Director Monitoreo Fraudes"

# Create directories
RUN mkdir -p /opt/directorAutenticacion && \
    mkdir -p /cyberbank/entrypoint/config

# Create non-root user and set permissions
RUN addgroup -S ${USER} && \
    adduser -S ${USER} -h $TECHNISYS_HOME -s /sbin/nologin -G ${USER} && \
    mkdir -p $TECHNISYS_HOME/init/lib && \
    mkdir -p $TECHNISYS_HOME/init/extras && \
    chown -R ${USER}:${USER} $TECHNISYS_HOME /cyberbank

# Install additional packages
RUN apk --no-cache del curl && \
    apk add --no-cache bash="${BASH_VERSION}" && rm -rf /var/cache/apk/*

### Setup user for build execution and application runtime
ENV PATH=${CONTAINER_ROOT}/bin:${PATH}
ENV HOME=${CONTAINER_ROOT}

# Copy files and set permissions
COPY --chown=${USER}:${USER} --chmod=555 uid_entrypoint ${CONTAINER_ROOT}/bin/ 
COPY --chown=${USER}:${USER} --chmod=664 ./target/directorAutenticacion-${VERSION}.jar /opt/directorAutenticacion/directorAutenticacion.jar

RUN chmod -R u+x ${CONTAINER_ROOT}/bin && \
    chgrp -R ${USER} /cyberbank && \
    chmod -R g=u /etc/passwd /cyberbank 

### Containers should NOT run as root as a good practice
# Switch to the non-root user defined earlier
USER ${USER}
WORKDIR ${CONTAINER_ROOT}

### user name recognition at runtime w/ an arbitrary uid - for OpenShift deployments
# Entry point and command
ENTRYPOINT [ "uid_entrypoint" ]
CMD ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF8 -Dconfig.entrypoint=/cyberbank/entrypoint/config -Dspring.config.location=/cyberbank/entrypoint/config/directorAutenticacion.properties -jar /opt/directorAutenticacion/directorAutenticacion.jar"]
