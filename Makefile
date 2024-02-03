setup-local:
	cd setup-script/docker/remindroute && docker-compose down -v  && docker-compose up -d

run:
	./gradlew clean buildFatJar && java -Duser.timezone="Asia/Seoul" -jar build/libs/remindroute.jar

test:
	./gradlew clean test -Duser.timezone="Asia/Seoul"

ssh:
	ssh -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com

mysql:
	ssh -t -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com "mysql -h popin-rds.c38eqieeo6v0.us-east-1.rds.amazonaws.com -P 3306 remindroute -u root -pmysqlrootpw "

setup-prod:
	scp -r -i pinpong.pem setup-script/db ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com:/home/ec2-user/app && ssh -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com "cat /home/ec2-user/app/db/*.up.sql|mysql -h popin-rds.c38eqieeo6v0.us-east-1.rds.amazonaws.com -P 3306 remindroute -u root -pmysqlrootpw "

drop-prod:
	scp -r -i pinpong.pem setup-script/db ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com:/home/ec2-user/app && ssh -i pinpong.pem ec2-user@ec2-44-201-161-53.compute-1.amazonaws.com "cat /home/ec2-user/app/db/00_db.down.sql|mysql -h popin-rds.c38eqieeo6v0.us-east-1.rds.amazonaws.com -P 3306 remindroute -u root -pmysqlrootpw "


