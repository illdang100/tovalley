# tovalley
물놀이 인명사고를 예방하기 위해 물놀이 장소의 안전 정보를 제공하고, 리뷰 및 평점 작성 기능을 통해 사용자들의 경험을 공유할 수 있는 웹사이트
<p align="center"><img src="https://github.com/illdang100/molly/assets/77067383/a005dbdd-79ac-4bdc-b94c-2ecec6fa4400"/></p>
<br/><br/>

##  ↗️ 사이트 바로가기
https://tovalley.site/
<br/><br/>

## 💡 매뉴얼 (youtube)
https://youtu.be/7LeTlWwDp3c?si=bZcxJQCtt5TyCFFc
<br/><br/>

## ☑️ 핵심 기능 설명
<a href="https://github.com/wl2258/tovalley/wiki/%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C">🔗 핵심 기능 설명 링크</a>
<br/><br/>

## 🐾 개발 일정
<p align="center"><img src="https://github.com/wl2258/tovalley/assets/77067383/a9d2e6da-e3ad-4e88-88e9-8174757dc380" width=100% /></p>
<br/><br/>

## 🌈 전체 시스템 구조
<p align="center"><img src="https://github.com/wl2258/tovalley/assets/77067383/6b00cd7d-db60-4755-a757-448892980222" width=70% /></p>
<br/><br/>

## 📃 데이터베이스 구조
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/77067383/3f1fbd3f-130f-4432-8b7d-2dfdb92e029b" width=600 height=350 /></p>
<br/><br/>

## 🖨️ Api 명세서
<p align="center"><img src="https://github.com/illdang100/molly/assets/77067383/48a1a1e3-6b40-435e-a065-03c85147aa32" width=700 /></p>
<br/><br/>

## 📦 Packages
#### SERVER 주요 OPENSOURCE
| Name | Description |
| --- | --- |
| `Spring Security` |  애플리케이션의 보안 및 인증 처리 |
| `Spring OAuth2 Client` |  OAuth 2.0 클라이언트 구현 |
| `com.auth0:java-jwt` |  JWT(Json Web Token) 생성 및 검증을 위한 라이브러리 |
| `com.querydsl:querydsl-jpa` |  Querydsl 를 사용하여 JPA 쿼리를 생성하기 위한 라이브러리 |
| `com.querydsl:querydsl-apt` |  Querydsl 어노테이션 프로세서를 사용하여 쿼리 타입 클래스를 생성하기 위한 라이브러리 |
| `org.springframework.cloud:spring-cloud-starter-aws` |  스프링 클라우드에서 AWS를 사용하기 위한 라이브러리 |
| `Spring Web` | 웹 애플리케이션 구축을 위한 기능을 제공하는 Spring Framework의 웹 모듈 |
| `Lombok` | Java 상용구 코드를 줄이는 것을 도와주는 라이브러리 |
| `Thymeleaf` | 웹 앱용 서버측 Java 템플릿 엔진 |
| `MySQL Driver` | Java 어플리케이션을 MySQL 데이터베이스에 연결 |
| `MariaDB Driver` | Java 어플리케이션을 MariaDB 데이터베이스에 연결 |
| `Spring Boot DevTools` | 자동 애플리케이션 다시 시작, 라이브 다시 로드 등과 같은 기능을 제공하여 개발 경험을 향상시키는 개발 도구 세트 |
| `MariaDB Driver` | Java 어플리케이션을 MariaDB 데이터베이스에 연결 |
| `H2 Database` | Java 애플리케이션에 내장할 수 있는 인메모리 및 디스크 기반 관계형 데이터베이스 엔진 |
| `Validation` | Spring Validation은 객체의 선언적 검증을 위한 프레임워크 |
| `Spring Data JPA` | JPA(Java Persistence API)를 사용하여 Spring 애플리케이션의 데이터 액세스 계층 구현을 단순화 |
| `OAuth2 Client` | Spring Security의 OAuth2 클라이언트를 사용하면 애플리케이션이 OAuth2 클라이언트 역할을 하여 사용자가 다른 신뢰할 수 있는 시스템의 자격 증명으로 로그인 가능 |
| `QueryDSL` | 하이버네이트 쿼리 언어(HQL: Hibernate Query Language)의 쿼리를 타입에 안전하게 생성 및 관리해주는 프레임워크 |
| `Mail` | Spring Framework는 Java 애플리케이션에서 이메일을 보내기 위한 패키지를 제공 |
| `MockMultipart` | 의 환경에서 멀티파트 요청을 테스트할 수 있는 Spring 프레임워크의 일부 |
| `JWT` | 웹에서 사용되는 JSON 형식의 토큰에 대한 표준 규격 |
| `S3` | Amazon S3(Simple Storage Service) 확장 가능한 객체 스토리지 서비스 |
| `Redis` | 캐시 또는 메시지 브로커로 사용할 수 있는 메모리 내 데이터 구조 저장소 |
| `Json` | Javascript 객체 문법으로 구조화된 데이터를 표현하기 위한 문자 기반의 표준 포맷 |
| `proj4j` | 지도 제작 투영 및 좌표 변환을 위한 Java 라이브러리 |


#### Dependencies Module (package.json) 
```javascript
{
  "name": "tovalley-react",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "@react-google-maps/api": "^2.19.2",
    "@testing-library/jest-dom": "^5.17.0",
    "@testing-library/react": "^13.4.0",
    "@testing-library/user-event": "^13.5.0",
    "@types/googlemaps": "^3.43.3",
    "@types/jest": "^27.5.2",
    "@types/node": "^16.18.39",
    "@types/react": "^18.2.18",
    "@types/react-dom": "^18.2.7",
    "@types/react-router-dom": "^5.3.3",
    "chart.js": "^4.3.3",
    "moment": "^2.29.4",
    "react": "^18.2.0",
    "react-chartjs-2": "^5.2.0",
    "react-cookie": "^6.0.1",
    "react-cookies": "^0.1.1",
    "react-dom": "^18.2.0",
    "react-icons": "^4.10.1",
    "react-router-dom": "^6.14.2",
    "react-scripts": "5.0.1",
    "styled-components": "^6.0.7",
    "typescript": "^4.9.5",
    "web-vitals": "^2.1.4"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  },
  "eslintConfig": {
    "extends": [
      "react-app",
      "react-app/jest"
    ]
  },
  "browserslist": {
    "production": [
      ">0.2%",
      "not dead",
      "not op_mini all"
    ],
    "development": [
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ]
  },
  "devDependencies": {
    "@types/styled-components": "^5.1.26"
  },
  "description": "This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).",
  "main": "index.js",
  "author": "",
  "license": "ISC"
}
```
#### Open api 
- 기상청
  - 기상특보 조회서비스
- 행정안전부
  - 생활안전지도 물놀이관리지역
- Google Maps API
  - Place Photo API
    - Place Search API
    - Place Details API
  - Geocoding

<br/><br/>
## ⚒️ 기술 스택
#### 👩‍💻 Tools
<span><img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Spring-5FB832?style=flat-square&logo=Spring&logoColor=white"/></span>

#### 📂 Databases
<span><img src="https://img.shields.io/badge/MySql-00758F?style=flat-square&logo=MySql&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/mariadb-003545?style=flat-square&logo=mariadb&logoColor=white"/></span>

#### 💭 협업 및 버전관리
<span><img src="https://img.shields.io/badge/GitHub-000000?style=flat-square&logo=GitHub&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/></span>

#### 🛜 배포
<span><img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/nginx-009639?style=flat-square&logo=nginx&logoColor=white"/></span>
