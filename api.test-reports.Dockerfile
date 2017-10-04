FROM kyma/docker-nginx

COPY api/target/site/ /var/www

EXPOSE 80

CMD 'nginx'