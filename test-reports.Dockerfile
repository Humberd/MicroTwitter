FROM kyma/docker-nginx

# the last fragment prevents error triggering, when there are no test results to publish
VOLUME /tmp
RUN pwd
RUN ls
RUN cp api/target/site/jacoco/ /var/www/api-coverage  2>/dev/null || :

EXPOSE 80

CMD 'nginx'