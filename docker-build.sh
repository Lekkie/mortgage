rm -f ./docker/mortgage*.jar
rm -f ./docker/app*.yml
mvn clean install
cp ./target/mortgage-1.0-SNAPSHOT.jar ./docker/
cp ./src/main/resources/application.yml ./docker/
docker rm $(docker ps -a -q)
docker rmi -f mortgage

# docker hub username
USERNAME=lekkie
# image name
IMAGE=mortgage

version=`cat ./docker/VERSION`
version="${version%.*}.$((${version##*.}+1))"
echo $version > './docker/VERSION'
echo "version: $version"

docker build -t $USERNAME/$IMAGE ./docker/

docker tag $USERNAME/$IMAGE $USERNAME/$IMAGE:$version




