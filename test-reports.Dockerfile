FROM kyma/docker-nginx

# the last fragment prevents error triggering, when there are no test results to publish
RUN hostname
COPY api/target/microtwitter* /var/www/api-coverage

EXPOSE 80

CMD 'nginx'