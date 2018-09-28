#Using an ubuntu base image
FROM ubuntu:16.04

RUN apt-get update && apt-get install -y \
  libmicrohttpd-dev \
  libjansson-dev \
  libcurl4-gnutls-dev \
  libgnutls28-dev \
  libgcrypt20-dev


#copy the locally compiled sh file & JSON into the container
COPY ./server /server

COPY ./libs/libjansson.so /usr/lib/libjansson.so
COPY ./libs/libulfius.so /usr/lib/libulfius.so
COPY ./libs/libyder.so /usr/lib/libyder.so.1.2
COPY ./libs/liborcania.so /usr/lib/liborcania.so.1.2

# Expose the port that the server will listen on 
EXPOSE 1247

# Set the LD_LIBRARY_PATH so that the server binary will find the JSON
ENV LD_LIBRARY_PATH ${LD_LIBRARY_PATH}:/usr/lib

#setting the command for the container to run
CMD ["/server"]
