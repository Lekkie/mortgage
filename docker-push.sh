./docker-build.sh

# docker hub username
USERNAME=lekkie
# image name
IMAGE=mortgage

version=`cat ./docker/VERSION`
echo "version: $version"

# push it
docker push $USERNAME/$IMAGE
