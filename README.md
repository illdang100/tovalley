<span style="margin-left: 10px;"><h1><img src="https://github.com/illdang100/tovalley/assets/97449471/0df8fe51-b4b5-49d7-b0f0-3568156184a1" width="30" height="auto" align="left" />투계곡(ToValley) 프로젝트</h1></span>

<p align="center"><img src="https://github.com/illdang100/molly/assets/77067383/a005dbdd-79ac-4bdc-b94c-2ecec6fa4400" style="width:75%;"/></p>

## 💡 프로젝트 소개
투계곡 프로젝트는 물놀이 장소에서 발생할 수 있는 인명사고를 예방하는 것을 목표로 합니다.<br/>
사용자들은 물놀이 장소의 안전 정보를 확인할 수 있으며, 또한 리뷰, 커뮤니티, 채팅 기능을 통해 다른 사용자들과 자신의 경험을 공유할 수 있습니다.<br/>
이를 통해 사용자들은 보다 안전한 물놀이 환경을 조성하고, 위험을 사전에 예방할 수 있습니다.

<ul>
   <li>프로젝트 기간: 2023.09 ~ 2024.03</li>
   <li>웹사이트 방문: <a href="https://tovalley.site/">바로가기</a></li>
   <li>데모 영상: <a href="https://www.youtube.com/watch?v=qlYggbJ5q3I">링크</a></li>
</ul>

## 🚀 시스템 구조
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/cdd7512c-031a-495c-b7cf-24ccaf2e9941" width=65% /></p>

- **Nginx**: 정적 자원을 서비스하고 클라이언트의 요청을 적절한 서버로 전달하는 역할을 담당합니다.
- **MariaDB**: 사용자 데이터 및 트랜잭션 데이터와 같은 구조화된 데이터의 관리를 담당합니다.
- **Redis**: 사용자 인증 과정에서 발급되는 리프래시(refresh) 토큰을 저장합니다.
- **MongoDB**: 채팅 메시지 정보와 같은 비구조화된 데이터를 저장하는 데 사용됩니다.
- **Kafka**: 실시간 채팅 메시지의 비동기 처리 및 분선 저장을 담당합니다.

### 개발 환경
- 프로그래밍 언어: Java 11
- 빌드 툴: Gradle
- 프레임워크: SpringBoot 2.7.14

### 기술 스택

**프레임워크 및 라이브러리**
<div>
  <span><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/JPA-007396?style=flat-square&logo=java&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/QueryDSL-007396?style=flat-square&logo=java&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black"/></span>
  <span><img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/CSS Module-000000?style=flat-square&logo=css3&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Styled--Component-DB7093?style=flat-square&logo=styled-components&logoColor=white"/></span>
</div>

**데이터베이스 및 캐싱**
<div>
  <span><img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white"/></span>
</div>

**인프라 및 배포**
<div>
  <span><img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS S3-569A31?style=flat-square&logo=amazons3&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white"/></span>
</div>

**템플릿 엔진 및 API 도구**
<div>
  <span><img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black"/></span>
</div>

**테스트 도구**
<div>
  <span><img src="https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white"/></span>
</div>

**메시징 시스템**
<div>
  <span><img src="https://img.shields.io/badge/Kafka-231F20?style=flat-square&logo=apachekafka&logoColor=white"/></span>
</div>

## 🌤️ ERD
<details>
<summary>ERD 이미지</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/0f0ad225-0c7c-4cf0-b0f5-319791817328" width=95% /></p>

</div>
</details>

## 📌 주요 기능
<a href="https://github.com/wl2258/tovalley/wiki/%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C">🔗 핵심 기능 설명 링크</a>
<br/>

## 🖨️ Api 명세서
<p align="center"><img src="https://github.com/illdang100/molly/assets/77067383/48a1a1e3-6b40-435e-a065-03c85147aa32" width=700 /></p>

## 📈**성능 최적화 및 트러블 슈팅**
프로젝트 진행 과정에서 발생한 성능 최적화 및 트러블 슈팅 사례를 공유합니다.<br/>
