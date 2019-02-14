FROM alpine:3.9

ADD . /
WORKDIR /src

RUN apk update && apk upgrade && \
  apk add --no-cache nodejs wkhtmltopdf


CMD ["node", "server.js"]
