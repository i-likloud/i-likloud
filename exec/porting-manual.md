# 📜Porting Manual


## 📝목차
1) [시스템 환경 및 버전정보](#1-시스템-환경-및-버전정보)
2) [포트 정보](#2-포트-정보)
3) [서버 접속](#3-서버-접속)
4) [빌드 및 배포](#4-빌드-및-배포)
5) [DB](#5-DB)
6) [CI/CD](#6-CI/CD)
7) [NGINX](#7-NGINX)
8) [외부 API](#8-외부-API)
9) [APK](#9-APK)


## 1. ⚙시스템 환경 및 버전정보

- JVM : JDK 11
- Frontend IDE : Android Studio 2022.2.1 Patch2 
- Backend IDE : Intellij Ultimate 2023.1.3
- Framework : SpringBoot 2.7.13
- Android : Android-gradle-plugin 7.4.1, Kotlin 1.8.0
- Server : AWS EC2 instance - ubuntu 20.04
- WAS : NGINX 1.18.0
- DB : MySQL 8.0.33

<br>

## 2. 🔌포트 정보

| Port | 이름                          |
|:-----|:----------------------------|
| 8080 | HTTP => 80(HTTPS)로 리다이렉트    |
| 80   | HTTPS                       |
| 3306 | MySQL                       |
| 8080 | SpringBoot Docker Container |
| 9090 | Jenkins Docker Container    |
| 6379 | Redis Docker Container      |

<br>

## 3. 💻 서버 접속

> EC2 접속방법 ( Window 환경)<br>
> - PuTTygen을 통해 key파일 pem파일로 변환<br>
> - PuTTy 실행 HostName입력 
> - Connection - SSH - Auth - Credentials에서 변환한 pem 키 파일 업로드
> - Open(Session에서 Save 후 Open하면 추후 바로 실행 가능)
<br>

3.1. 포트 개방
```
$ sudo ufw allow {portnumer} # 80, 6379, 9090, 3306, 8080
$ sudo ufw numbered # 포트 개방 확인
```

3.2. JDK 설치
```
# JDK 11 설치
apt-get install openjdk-11-jre-headless

# 설치 확인
java -version
```

3.3. 🐳 도커 설치 후 실행
```
$ sudo apt update
$ sudo apt-get install docker-ce docker-ce-cli containerd.io
$ sudo systemctl start docker
```

3.4. 🗄️ 컨테이너 실행
```
# MySQL
$ docker run --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD={PASSWORD} -d mysql
# Redis
$ docker run --name redis-server -p 6379:6379 -d redis
# Jenkins
$ docker run -p 9090:8080 jenkins/jenkins
```

<br>

## 4. 🚀 빌드 및 배포

4.1. Dockerfile 작성

```dockerfile
# Dockerfile
FROM adoptopenjdk/openjdk11
LABEL authors="D101"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

4.2. Jar 파일 빌드
```
$ ./gradlew bootJar
```

4.3. 로컬에서 도커 이미지 빌드 및 푸시
```
docker build -t {사용자명}/{이미지파일명} .
docker push {사용자명}/{이미지파일명}
```

4.4. 로컬에서 SCP 사용하여 GCP 환경변수 파일 마운트
```
$ scp -i /path/to/your/ec2-key.pem /path/to/your/gcp-key.json ubuntu@{hostname}:/home/ubuntu/
```

4.5. EC2에서 도커 이미지 풀 및 컨테이너 실행
```
sudo docker pull {사용자명}/{이미지파일명}
sudo docker run -d --name CLOUD -p 8080:8080 -v /home/ubuntu/gcp-key.json:/app/keyfile.json
 -e GOOGLE_APPLICATION_CREDENTIALS=/app/keyfile.json -e PASSWORD={PASSWORD} {사용자명}/{이미지파일명}
 (실행 시 GCP 키 파일 및 Jasypt 암호화 키를 환경변수에 추가하여 실행)
```

<br>

## 5. 🗃️DB

### 5.1. properties
- application-db.yml

### 5.2. MySQL WorkBench
> MySQL Connections<br>
> Hostname : i9d101.p.ssafy.io, port : 3306<br>
> Username : root, Password : 

### 5.3. 덤프 파일 최신본
- /exec/i-likloud-dump.zip

### 5.4. ERD
[ERDCloud](https://www.erdcloud.com/d/BEapLKHyajjM3RktS)

<br>

## 6. 🏭CI/CD

### 6.1. Jenkins 설정

- System
   - GitLab
    > Connection name : d101<br>
    GitLab host URL : https://lab.ssafy.com/s09-webmobile4-sub2/S09P12D101 <br>
   Credentials : GitLab API Token
  - Public over SSH
    - SSH Servers
        >Name : d101 <br>
        Hostname : i9d101.p.ssafy.io <br>
        Username : ubuntu
 - Tools
    - JDK installations
      > Name : openjdk-11-jdk<br>
    JAVA_HOME : /usr/lib/jvm/java-11-openjdk-amd64
    - Gradle installations
      > Name : gradle 8.0<br>
    install automatically - Version : Gradle 8.0
 - Plugins
   >Docker plugin<br>
    GitLab plugin<br>
    Gradle<br>
    Publish Over SSH <br>
    Post build task<br>
    Generic Webhook Trigger Plugin
 
### 6.2. 프로젝트 선택 - 구성

- 소스 코드 관리
  - Git
    > Repository URL :  https://lab.ssafy.com/s09-webmobile4-sub2/S09P12D101 <br>
    Credentials : d101 <br>
    Branches to build : */master, */backend, */develop
- 빌드 유발
  - Build when a change is pushed to GitLab. GitLab webhook URL 체크
    - push events 체크
    - Opened Merge Request Events 체크
    - Approved Merge Requests (EE-only) 체크
    - Comments 체크
- Build Steps
  - Execute shell
    > cd /var/lib/jenkins/workspace/D101/backend <br>
    chmod +x ./gradlew <br>
    ./gradlew clean bootJar <br>
  
    > docker rm -f CLOUD || true <br>
    docker rmi -f ytchoi/i-likloud || true <br>
    docker build -t ytchoi/i-likloud /var/lib/jenkins/workspace/D101/backend/ <br>
    echo tae5263zz | docker login -u ytchoi --password-stdin <br> 
    docker push ytchoi/i-likloud <br>
- 빌드 후 조치
  - Transfers
    > Transfer Set Source files : backend/build/ligs/*.jar <br>
    Exec command :<br>
    sudo docker pull ytchoi/i-likloud <br>
    sudo docker ps -q --filter name=CLOUD | grep -q . && docker stop -f && docker rm -f $(docker ps -aq --filter name=CLOUD) <br>
    sudo docker run -d --name CLOUD -p 8080:8080 -v /home/ubuntu/i-likloud-96b61e373462.json:/app/keyfile.json -e GOOGLE_APPLICATION_CREDENTIALS=/app/keyfile.json -e PASSWORD={PASSWORD} ytchoi/i-likloud

<br>

## 7. 🌐NGINX

7.1. Nginx 설치
```
# Nginx 설치
$ sudo apt-get install nginx
# NGINX 설정파일 수정
$ sudo nano /etc/nginx/sites-available/default
```

7.2. 설정파일 수정
```
##

server {
    listen 80 default_server;
    listen [::]:80 default_server;

    root /var/www/html;

    # Add index.php to the list if you are using PHP
    index index.html index.htm index.nginx-debian.html;

    server_name _;

    location / {
        # First attempt to serve request as file, then
        # as directory, then fall back to displaying a 404.
        try_files $uri $uri/ =404;
    }

}


server {

    # Add index.php to the list if you are using PHP
    # index index.html index.htm index.nginx-debian.html;
    server_name i9d101.p.ssafy.io; # managed by Certbot


    location / {
        # First attempt to serve request as file, then
        # as directory, then fall back to displaying a 404.
#        try_files $uri $uri/ =404;
        proxy_pass http://i9d101.p.ssafy.io:8080;
    }

    listen [::]:443 ssl ipv6only=on; # managed by Certbot
    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/i9d101.p.ssafy.io/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/i9d101.p.ssafy.io/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

}

server {
    if ($host = i9d101.p.ssafy.io) {
        return 301 https://$host$request_uri;
    } # managed by Certbot


    listen 80 ;
    listen [::]:80 ;
    server_name i9d101.p.ssafy.io;
    return 404; # managed by Certbot

}
```

<br>

## 8. 🔗외부 API

### 8.1. Klaytn API

- properties
  - application-kas.yml
  > KAS: <br>
    client: <br>
  accessKeyId: ENC() <br>
  secretAccessKey: ENC() <br>
  authorization: ENC() <br>
  contract-alias: "unijoa"

### 8.2. Google Vision API

- i-likloud-96b61e373462.json

<br>

## 9. 📱APK

[구글 플레이스토어 - 뭉게뭉게 도화지]