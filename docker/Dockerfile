FROM centos:latest
FROM java:8
MAINTAINER Lekan Omotayo <lekkie.aydot@gmail.com>

ADD VERSION .

ARG MORTGAGE_VERSION=1.0-SNAPSHOT
# Update CentOS 7
#RUN yum update -y && yum upgrade -y
# Install packages
#RUN yum install -y unzip wget curl git
# Install EPEL Repository
#RUN yum install -y epel-release
# Clean CentOS 7
#RUN yum clean all

EXPOSE 8080

ENV DB_NAME mortgage
ENV DB_USER mortgage
ENV DB_PASSWD p455word
ENV DB_URL jdbc:mysql://10.0.0.28:3306/mortgage
ENV DB_DRIVER com.mysql.cj.jdbc.Driver
ENV DB_PLATFORM mysql
ENV DB_DATABASE_PLATFORM org.hibernate.dialect.MySQLDialect

ENV H2_ENABLED false

VOLUME /tmp
#WORKDIR /root/mortgage

ADD application.yml /root/mortgage/application.yml
ADD mortgage-1.0-SNAPSHOT.jar /root/mortgage/mortgage.jar
RUN bash -c 'touch /root/mortgage/mortgage.jar'
ADD install.sh /root/mortgage/install.sh
RUN bash -c '/root/mortgage/install.sh'

# lets default to the mortgage dir so folks can more easily navigate to around the server install
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/root/mortgage/mortgage.jar", "--spring.config.location=/root/mortgage/application.yml"]


