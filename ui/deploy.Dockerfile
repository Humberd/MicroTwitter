FROM nginx:1.13.6-alpine

#COPY nginx/default.conf /etc/nginx/conf.d/

RUN rm -rf /usr/share/nginx/html/*

COPY dist /user/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
