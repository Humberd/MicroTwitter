FROM kyma/docker-nginx

# the last fragment prevents error triggering, when there are no test results to publish
RUN hostname
COPY api/target/ microtwitter/
RUN ls
RUN ls microtwitter -al
RUN ls microtwitter/site -al

EXPOSE 80

CMD 'nginx'