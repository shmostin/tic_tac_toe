all: run

#run: server
#	LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib ./server


run: server.c
	gcc -o server server.c strategy.c -lulfius -ljansson

call_server: 
	curl -XGET localhost:8080/newGame

copy_needed_libs: 
	mkdir -p libs
	cp /usr/local/lib/libjansson.so ./libs/ 
	cp /usr/local/lib/libulfius.so ./libs/ 
	cp /usr/local/lib/libyder.so ./libs/ 
	cp /usr/local/lib/liborcania.so ./libs/ 

docker_server_make: server copy_needed_libs
	# Copy systems libs into local dir for access
	# inside the docker container
	docker build -t final-server .

# Running this will connect docker and terminal to docker
# this will allow getChar() to work when shutting down
run_docker:
	mkdir -p libs
	cp /usr/local/lib/libjansson.so ./libs/ 
	cp /usr/local/lib/libulfius.so ./libs/ 
	cp /usr/local/lib/libyder.so ./libs/ 
	cp /usr/local/lib/liborcania.so ./libs/
	docker build -t final-server .
	docker run -i -t -p 8080:8080 final-server:latest

clean: 
	rm -rf ./server ./libs
	
	
