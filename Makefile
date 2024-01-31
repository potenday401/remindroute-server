setup-local:
	cd setup-script/docker/remindroute && docker-compose down -v  && docker-compose up -d

run:
	./gradlew clean buildFatJar && java -jar build/libs/remindroute.jar

test:
	./gradlew clean test

ssh:
	ssh -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com

mysql:
	ssh -t -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com "mysql -h popin-rds.c38eqieeo6v0.us-east-1.rds.amazonaws.com -P 3306 remindroute -u root -pmysqlrootpw "

