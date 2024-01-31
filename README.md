# RemindRoute

## Required
java11, docker-compose

## 실행
#### 1. `make setup-local`
#### 2. `make run`
#### 3. 실행 확인: http://localhost:8080/hello

## Constraints
### 허용되는 의존방향
infrastructure →　domain  
infrastructure →　application  
application →　domain

## 인프라 초기화
terraform 사용
- AWS IAM 에서 EC2, RDS, S3 의 FullAccess 를 갖는 유저 생성 후, access_key, secret_key 취득
- terraform 에서 참조할 수 있도록 access_key, secret_key 로컬에 환경변수로 등록
- terraform 실행
- docker를 사용하지 않음으로 EC2 에 JAVA11(Corretto) 직접 설치

## Other
### API Document
http://localhost:8080/openapi