🌱 PICKPLE
=============
<img width="1114" alt="image" src="https://github.com/user-attachments/assets/a1304f96-5a02-4f43-9719-f648aad988ad">


## 📢 프로젝트 간단 소개

### **Pickple**! (Pick + people)

고객 경험을 최우선 가치로, 많은 고객들에게 그들이 원하는 경험을 제공하여 선택(pick) 받자는 소망을 담았습니다.

## 🎯 프로젝트 목표

- **MSA** 구조를 통해 서비스의 확장성과 유연성을 확보
- **Kafka**와 **Redis**를 활용하여 **비동기 메시징으로 서비스 간 통신 및 캐싱**을 통해 안정적이고 빠른 서비스 구축
- **동시성** 처리를 통한 안정성 있는 서비스 구축

## 🛠️ 기술 스택

### Backend
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/POSTGRESQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"><img src="https://img.shields.io/badge/MONGODB-47A248?style=for-the-badge&logo=mongodb&logoColor=white"><img src="https://img.shields.io/badge/REDIS-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"><img src="https://img.shields.io/badge/springDataJPA-90E59A?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/JUnit5-FB4F14?style=for-the-badge&logo=JUnit5&logoColor=white">


### DevOps
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"><img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"><img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"><img src="https://img.shields.io/badge/apachejmeter-D22128?style=for-the-badge&logo=apachejmeter&logoColor=white"><img src="https://img.shields.io/badge/prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"><img src="https://img.shields.io/badge/grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white">


### Tools
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"><img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"><img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"><img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

## 🪄 주요 기능

* **주문**

    - [x] **Kafka**를 통해 주문, 재고 - 결제 - 배송 - 알림 서비스 간 **비동기 통신** 구현
    - [x] Order 서비스가 주문의 전체 프로세스를 관리하고, 결제 및 배송 서비스에 대한 상태 변경을 트리거하는 방식으로 구현
          <details><summary>**주문 시퀀스 다이어그램**</summary>
          ![image](https://github.com/user-attachments/assets/7feed1cb-f1f9-4dd2-a5c9-718e2289ee55)
          </details>
    - [x] **TTL** 설정을 통해 주문 요청 후 5분 동안 결제 미진행 시, 주문 자동 취소

* **예약 구매**

    - [x] 동시에 많은 요청 시, 재고 관리 로직에서의 **동시성 이슈 방지**를 위해 **분산락** 도입
    - [x] 주문 시 (주문 요청 → 재고 감소 → 주문 생성) 분산락 환경에서의 **갱신 유실 방지**를 위해 낙관적 락 추가 도입

* **알람**

    - [x] Grafana와 Slack을 연동하여 **서버 다운 시** 슬랙 채널을 통한 알람 수신 (#server_channel)
    - [x] **트래픽 임계치**에서 50% 이상 시 경고 알람, 80% 이상 시 위험 알람 (#traffic_channel)
    - [x] 주문, 배송 완료 시 kafka를 통한 **실시간** 메일 알람 전송

* **모니터링**

    - [x] Grafana의 Provisioning 사용으로 어떤 환경에서 실행되어도 설정해둔 알람, 대시보드 등이 자동 provisioned 되도록 설정 자동화
    - [x] Loki를 통한 실시간 로그 분석 및 시각화
    - [x] Zipkin, prometheus를 통해 분산추적 및 로그 수집

* **고가용성 확보**

    - [x] Resilience4j **Circuit Breaker**를 통해 서비스 간 장애 전파 방지, 회복탄력성 향상
    - [x] Redis **Replication**과 **Sentinel**을 통한 분산 처리 및 고가용성 확보

        - Replication:  읽기 요청을 레플리카 노드로 우선적으로 분산하고, 쓰기 요청은 마스터 노드로 전송하여 부하 분산
        - Sentinel: 마스터가 다운되었을 경우 슬레이브 중 하나를 새로운 마스터로 auto-failover

* **아키텍처**
    - [x] Domain Driven Design: 도메인 중심의 시스템 설계 및 도메인 모델에 집중한 비즈니스 로직
        <details><summary> 패키지 구조</summary>

          ```java
          com.pickple.commerce-service
          ├── application
          │   ├── dto  
          │   │   ├── OrderCreateResponseDto.java
          │   │   └── OrderResponseDto.java
          │   ├── service
          │       └── OrderService.java
          ├── domain
          │   ├── model
          │   │   ├── Order.java
          │   │   ├── OrderDetail.java
          │   │   └── OrderStatus.java
          │   └── repository
          │       └── OrderRepository.java
          ├── exception
          │   ├── CommerceErrorCode.java
          │   ├── CommerceExceptionHandler.java
          │   └── CustomException.java
          ├── infrastructure
          │   ├── configuration
          │   │   ├── AuditAwareImpl.java
          │   │   ├── JpaConfig.java
          │   │   └── SecurityConfig.java
          │   ├── feign
          │   │   ├── DeliveryClient.java
          │   │   ├── PaymentClient.java
          │   │   └── dto
          │   │       ├── DeliveryClientDto.java
          │   │       └── PaymentClientDto.java
          │   ├── messaging
          │   │   ├── OrderEventService.java
          │   │   └── OrderMessagingProducerService.java
          │   └── security
          │       └── CustomPreAuthFilter.java
          ├── presentation
          │   ├── controller
          │   │   └── OrderController.java
          │   └── request
          │       └── OrderCreateRequestDto.java
          ```
          </details>

    - [x] Layered Architecture: 시스템을 여러 레이어로 나누어 서로 다른 관심사를 처리하도록 설계
    - [x] Event-Driven Architecture: Kafka를 사용하여 이벤트가 전달되며, 각 서비스가 이벤트를 생성하고, 소비하는 방식으로 통신

* **CI/CD**
  - [x] CI: 서비스 컨테이너를 구성하여 테스트 실행
  - [x] CD: 도커화한 image를 AWS ECR에 Push, 각 EC2에 배포

<br>

## 📚 API 명세서

### [POSTMAN DOCS](https://documenter.getpostman.com/view/26598837/2sAY4rGRdh) 
<br>

## 🧩ERD
![pickple](https://github.com/user-attachments/assets/5641d753-fc1e-47d9-a1a8-eb1c28ed75ac)

<br>

## 🗂️ 인프라 설계도
![image](https://github.com/user-attachments/assets/2cb9c935-d668-4eff-8062-b829010212d7)

- **배포 링크** [🔗링크](http://ec2-52-206-139-65.compute-1.amazonaws.com:19091/)
  <br>
- **서비스 구조**

| Service	             | Description	    | Authorization	 | Port   |
|:---------------------|:----------------|:---------------|:-------|
| [`Eureka`]           | 서비스 등록 및 로드밸런싱|       | `19090` |
| [`API Gateway`]      | 요청 인증 및 라우팅     | jwt 토큰 인증      | `19091` |
| [`Auth`]             | jwt 토큰 발급 및 관리     | jwt 토큰 발급      | `19092` |
| [`User`]             | 사용자 관련 작업 관리   | jwt 토큰 인가      | `19093` |
| [`Commerce`]         | 커머스 관련 도메인 작업 관리   |                | `19094` |
| [`Payment`]          | 결제 도메인 작업 관리    |                | `19095` |
| [`Delivery`]         | 배송 도메인 작업 관리 |                | `19096` |
| [`Notification`]     | 알람 도메인 작업 관리    |                | `19097` |
<br>

## 🚧 트러블 슈팅

### [WIKI](https://github.com/pickple-ecommerce/backend/wiki/Pickple-Trouble-Shooting-Wiki)

<br>

## ✏️ 프로젝트 실행 방법

### 1. 프로젝트 클론

   ```bash
  git clone https://github.com/pickple-ecommerce/backend.git
   ```
### 2. 순차적으로 docker compose 명령어 실행

   ```bash
    docker-compose -f docker-compose.dev.yml --env-file dev.env
    docker-compose -f docker-compose.service.yml --env-file dev.env
   ```
<br>

## 🅰️ 팀 ATEEN

<table>
  <thead>
    <tr>
      <th align="center">팀원</th>
      <th align="center">포지션</th>
      <th align="center">담당</th>
      <th align="center">GitHub 링크</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center"><img src="https://github.com/kyungjinleelee.png" width="100px;" alt=""/><br /><sub><b>이경진</b></sub></a></td>
      <td align="center">👑 Leader</td>
      <td align="left">
        - Eureka, gateway 환경 구축<br />
        - User, Auth 도메인 백엔드 개발 (인증/인가 및 백오피스)<br />
        - 상품 및 재고 도메인 백엔드 개발<br />
        - 예약구매 기능 개발<br />
        - Redis master-slave 구조 및 sentinel 구축
      </td>
      <td align="center"><a href="https://github.com/kyungjinleelee">GitHub</a></td>
    </tr>
    <tr>
      <td align="center"><img src="https://github.com/easyxun.png" width="100px;" alt=""/><br /><sub><b>이지선</b></sub></a></td>
      <td align="center">👑 Sub-Leader</td>
      <td align="left">
        - 업체 도메인 백엔드 개발<br />
        - 주문 도메인 백엔드 개발<br />
        - Resilience4j 서킷 브레이커 구현
      </td>
      <td align="center"><a href="https://github.com/easyxun">GitHub</a></td>
    </tr>
    <tr>
      <td align="center"><img src="https://github.com/MeGuuuun.png" width="100px;" alt=""/><br /><sub><b>박지민</b></sub></a></td>
      <td align="center">🫅🏻 Member</td>
      <td align="left">
        - 결제 도메인 백엔드 개발<br />
        - 알림 도메인 백엔드 개발<br />
        - Prometheus와 Grafana 연동으로 서버 모니터링 및 Slack으로 알림 발송 기능 구현<br />
        - Loki 활용하여 로그 관리 및 시각화
      </td>
      <td align="center"><a href="https://github.com/MeGuuuun">GitHub</a></td>
    </tr>
    <tr>
      <td align="center"><img src="https://github.com/kimjinmyeong.png" width="100px;" alt=""/><br /><sub><b>김진명</b></sub></a></td>
      <td align="center">🫅🏻 Member</td>
      <td align="left">
        - 배송 도메인 백엔드 개발<br />
        - 프로젝트 Gradle 멀티 모듈 구성<br />
        - Github Actions를 활용하여 CI/CD 구성<br />
        - AWS Infra 구성 및 배포
      </td>
      <td align="center"><a href="https://github.com/kimjinmyeong">GitHub</a></td>
    </tr>
  </tbody>
</table>
