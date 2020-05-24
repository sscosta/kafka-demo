# Background

As part of the curricular unit of Project and Seminar course of the Bsc in Computer Science and Engineering at ISEL on the Summer Semester of the 2019/2020 academic year, it was proposed that each student prepared an individual presentation about a subject related to Computer Science.

The presentation was limited to 15 minutes, 12 for the presentation itself and 3 minutes for comments and questions from the audience.

I chose to present an introduction to Apache Kafka. The reason for this choice was that I recently have been using it at work, so I already have studied it on my own. I wanted to present the challenges Kafka addresses, how it deals with it and see more in-depth the composition of a kafka topic, including partitions and consumer groups. I thought these concepts could be summarized in that time frame, hopefully not over-simplifying them.

As the presentation was very limited in time, the demo was very elemental. I presented the creation of a topic, a console consumer and producer. I then emitted some records to the topic and showed the audience that the consumer received the messages. The commands were not showed in full as some of them took a long time  (e.g. pulling the docker image from docker hub).

## Guide
Following is a complete guide to reproducing the demo. This repository also includes a scala app that reads from a topic, does a transformation and writes to another topic.

### Step 1 - Pull docker image from docker hub
`docker pull sscosta/cloudera-quickstart:demos`

### Step 2 - Run a container of the image
`docker run --hostname=quickstart.cloudera --privileged=true --name CDH -t -i -d -p 7180:7180 -p 7183:7183 -p 8888:8888 --cpus 3 --memory 10gb sscosta/cloudera-quickstart:demos /usr/bin/docker-quickstart`

### Step 3 - Check that container is running and copy its id
`docker ps -a`

### execute bash inside container
`docker exec -it [container-id] bash`

#### If a "Memory limited without swap" warning appears

When running the docker file in Ubuntu ( or Debian hosts), the warning  "Memory limited without swap” can appear. This can impact the performance on the container.

This happens because the cgroup memory and swap accounting is not enabled by default in the kernel.  (It is enabled in rhel distros).

To confirm it you can run and will see the following warning (or similar):

`$ docker info | grep swap`

WARNING: No swap limit support

To enable the cgroup swap limit, edit the "/etc/default/grub" file, and add:
`GRUB_CMDLINE_LINUX_DEFAULT="cgroup_enable=memory swapaccount=1"`

Then execute:
`sudo update-grub`

and reboot the machine

### Step 4 - open cloudera manager
Add the line '172.17.0.2 quickstart.cloudera' to your /etc/hosts file, e.g. of the /etc/hosts file below.

This is necessary in order for the quickstart.cloudera hostname resolve to the ccontainer's IP address running in the Docker's bridge virtual network.

Open Cloudera Manager and turn on all the services (if not running wait a moment the services can take a few minutes to be ready)

    Url: http://quickstart.cloudera:7180/cmf/home

    User: cloudera

    Password: cloudera

### Step 5 - start cloudera manager service
Start the Cloudera Management Service that "binds" the Cloudera Manager UI with the services allowing us to see the service's state in the UI.
After all services are up and running, stop the following ones that you don't need (to minimize resource consumption of the container):
    Key-value store indexer;
    Oozie;
    Solr.
The container environment is ready to go!!!

#### If the UI does not show, run the following commands inside the container's shell (as user "cloudera"):
`cd home/cloudera/`

`sudo ./cloudera-manager --express --force`

### Step 6 - create a topic
`kafka-topics --create --bootstrap-server 172.17.0.2:9092 --replication-factor 1 --partitions 2 --topic console-test-topic`

### Step 7 - Check if topic was created
`kafka-topics --list --bootstrap-server 172.17.0.2:9092`

### Step 8 - Start a consumer
`kafka-console-consumer --bootstrap-server 172.17.0.2:9092 --topic console-test-topic --from-beginning`

### Step 9 - Start a producer
`kafka-console-producer --broker-list 172.17.0.2:9092 --topic console-test-topic`

### Step 10 - produce data
Type text into the producer terminal

### Step 11 - check that the consumer got the data
