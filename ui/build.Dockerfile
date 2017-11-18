FROM node:8.9.1-alpine

COPY package.json package-lock.json ./

RUN npm set progress=false && npm config set depth 0 && npm cache clean --force

## Storing node modules on a separate layer will prevent unnecessary npm installs at each build
RUN npm i && mkdir -p /ng-app && cp -R ./node_modules ./ng-app

WORKDIR /ng-app

COPY . .

## Build the angular app in production mode and store the artifacts in dist folder
RUN $(npm bin)/ng build --prod --build-optimizer

#### STAGE 2: Setup ###
#
#FROM nginx:1.13.3-alpine
#
#VOLUME ./dist/ /ng-app/dist/
#
#RUN mkdir -p /ng-app/dist && cd /ng-app/dist && touch foo.txt
##
### Copy our default nginx config
##COPY nginx/default.conf /etc/nginx/conf.d/
#
### Remove default nginx website
##RUN rm -rf /usr/share/nginx/html/*
##
#### From 'builder' stage copy over the artifacts in dist folder to default nginx public folder
##COPY --from=builder /ng-app/dist/code /usr/share/nginx/html
#
#CMD ["nginx", "-g", "daemon off;"]



#-----------------------------https://docs.docker.com/engine/userguide/eng-image/multistage-build/#use-multi-stage-builds-------------
