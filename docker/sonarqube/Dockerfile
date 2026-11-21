ARG SONARQUBE_VERSION=26.3.0.120487-community
ARG SONARQUBE_DIGEST=sha256:dee9e6ba92725b2ecaa533b61133c46098cfbf8ea2e5a3424ab7b4b8f7a1a821

FROM sonarqube:${SONARQUBE_VERSION}@${SONARQUBE_DIGEST}

ARG PLUGIN_VERSION=26.3.0
ARG PLUGIN_BASE_URL=https://github.com/mc1arke/sonarqube-community-branch-plugin/releases/download/${PLUGIN_VERSION}

ADD --chown=sonarqube:root \
    --checksum=sha256:152f049587d09aa17577d1c935c93832f46a0dc0828813b726831787186a3236 \
    ${PLUGIN_BASE_URL}/sonarqube-community-branch-plugin-${PLUGIN_VERSION}.jar \
    /opt/sonarqube/extensions/plugins/

ADD --checksum=sha256:e1706407d7461778bf828db15954ef92d5cc24db97418c605ea8a844f5b58d8c \
    ${PLUGIN_BASE_URL}/sonarqube-webapp.zip \
    /tmp/sonarqube-webapp.zip

USER root
RUN rm -rf /opt/sonarqube/web/* \
    && cd /opt/sonarqube/web \
    && jar xf /tmp/sonarqube-webapp.zip \
    && rm /tmp/sonarqube-webapp.zip \
    && chown -R sonarqube:root /opt/sonarqube/web \
    && chmod -R 550 /opt/sonarqube/web
USER sonarqube

ENV PLUGIN_VERSION=${PLUGIN_VERSION} \
    SONAR_WEB_JAVAADDITIONALOPTS="-javaagent:/opt/sonarqube/extensions/plugins/sonarqube-community-branch-plugin-${PLUGIN_VERSION}.jar=web" \
    SONAR_CE_JAVAADDITIONALOPTS="-javaagent:/opt/sonarqube/extensions/plugins/sonarqube-community-branch-plugin-${PLUGIN_VERSION}.jar=ce"
