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

## Other
### API Document
http://localhost:8080/openapi