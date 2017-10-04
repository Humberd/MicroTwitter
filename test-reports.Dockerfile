FROM kyma/docker-nginx

COPY api/target/site/jacoco/ /var/www/api-coverage

EXPOSE 80

CMD 'nginx'