setup-local:
	cd setup-script/docker/remindroute && docker-compose down -v  && docker-compose up -d

run:
	./gradlew clean buildFatJar && java -jar build/libs/remindroute.jar

test:
	./gradlew clean test
