# Oh My Stack
사용자의 기술스택과 자기소개서를 기반으로 한 채용공고 추천 서비스

## 기간
개발 기간은 2023 6/1 ~ 6/27 입니다.

## 설명
This project developed a back-end service using Spring Boot and recommends job openings based on the user's technical stack and self-introduction letter.  
The service aims to reduce the difficulty of users in finding the right company for them.

## 목차
1. [프로젝트 동기](#프로젝트-동기)
2. [기술 스택](#기술-스택)
3. [시작하기](#시작하기)
4. [기여](#기여)

## 프로젝트 동기
IT 직무가 점점 세분화되고 다양해지는 시대에, 어떤 직무가 본인에게 가장 잘 맞는지 찾기가 어려워졌습니다.   
또한, 언어와 프레임워크의 수가 증가함에 따라, 본인의 기술 스택과 가장 잘 맞는 회사를 찾기가 어려워졌습니다.  
이 서비스는 사용자의 기술 스택과 자기소개서를 기반으로 채용공고를 추천하여 취업을 찾는 과정을 간편하게 해주고자 합니다.

## 기술 스택
* **Spring Boot** - 백엔드 개발에 사용된 주요 프레임워크입니다.
* **Amazon RDS & MySQL** - 사용자 데이터를 저장하기 위해 Amazon RDS에서 MySQL을 사용했습니다.
* **IntelliJ IDEA Ultimate** - Spring Boot 백엔드 개발을 위한 주요 IDE로 사용했습니다.
* **Amazon EC2** - 웹 애플리케이션 배포를 위한 서버로 사용했습니다.
* **Amazon Route 53** - 도메인 네임 시스템 (DNS) 웹 서비스를 제공하기 위해 사용했습니다.
* **AWS Certificate Manager** - SSL/TLS 인증서를 제공하고 관리하기 위해 사용했습니다.
* **Amazon S3** - 웹 애플리케이션에서 생성되는 스태틱 파일들을 저장하고 제공하기 위해 사용했습니다.

## 시작하기

### 전제조건

다음의 프로그램 및 라이브러리가 설치되어 있어야 합니다:

* Java Development Kit (JDK) 17

### Dependencies

이 프로젝트는 다음의 Spring Boot Dependencies를 사용합니다:

* Spring Boot DevTools
* Lombok
* Spring Web
* Spring Security
* Thymeleaf
* MySQL Driver
* Spring Data JPA
* jjwt-api
* jjwt-impl
* jjwt-jackson

## 기여
버그를 발견하거나 기능 요청이 있는 경우, GitHub에서 이슈를 생성해주세요.
