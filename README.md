# 🐾 REBORN : BACKEND

REFRESH 팀의 프로젝트, REBORN(반려동물 동반 애플리케이션)의 백엔드(서버)입니다.


## 🧭 INTRODUCTION

**REBORN**은 반려동물과 보호자의 삶을 기록하고, 감정을 치유하며, 추억과 작별의 순간을 함께하는 **감정 기반 반려동물 동반 애플리케이션**입니다.

> "REBORN"은 ‘다시 활발해지다’, ‘다시 태어나다’를 의미하는 영단어입니다.
> 이 이름에는 반려동물과 보호자의 모든 순간을 소중히 기록하고,
> 이별 후에도 보호자가 **다시 삶의 활기를 되찾도록 돕겠다**는 의미가 담겨 있습니다.


## 👨‍👩‍👧‍👦 MEMBER

|                   강민준                    |                 차정은                  |
|:----------------------------------------:|:------------------------------------:|
| [GITHUB](https://github.com/MinJunKKang) | [GITHUB](https://github.com/jyc0011) |


## 🛠️ TECH STACK

<div align="center">
  
**Language & Framework**
  
![Java](https://img.shields.io/badge/Java-ED272C?logo=Java&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white&style=for-the-badge)
![JPA](https://img.shields.io/badge/JPA-5F6C72?logo=JPA&logoColor=white&style=for-the-badge)
![JACKSON2](https://img.shields.io/badge/JACKSON2-59A899?logo=JACKSON2&logoColor=white&style=for-the-badge)
![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white&style=for-the-badge)

**Database & Storage**

![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white&style=for-the-badge)
![REDIS](https://img.shields.io/badge/REDIS-FF4438?logo=redis&logoColor=white&style=for-the-badge)
![FIREBASE](https://img.shields.io/badge/FIREBASE-DD2C00?logo=firebase&logoColor=white&style=for-the-badge)

**DevOps & Infra**

![AWS](https://img.shields.io/badge/AWS-232F3E?logo=amazonwebservices&logoColor=white&style=for-the-badge)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?logo=amazonec2&logoColor=white&style=for-the-badge)https://github.com/PROJECT-X-REFRESH/Back-Reborn/blob/refactor-dto/README.md
![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?logo=amazonrds&logoColor=white&style=for-the-badge)
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?logo=amazons3&logoColor=white&style=for-the-badge)
![AWS VPC](https://img.shields.io/badge/AWS%20VPC-852EF8?logo=amazonvpcs&logoColor=white&style=for-the-badge)
![AWS ELASTIC BEANSTALK](https://img.shields.io/badge/AWS%20ELASTIC%20BEANSTALK-8C4FFF?logo=awselasticloadbalancing&logoColor=white&style=for-the-badge)

**Collaboration & CI/CD**

![GIT](https://img.shields.io/badge/git-F05032?logo=git&logoColor=white&style=for-the-badge)
![GITHUB](https://img.shields.io/badge/github-181717?logo=github&logoColor=white&style=for-the-badge)
![GIT ACTION](https://img.shields.io/badge/git%20action-278CFF?logo=gitaction&logoColor=white&style=for-the-badge)
![SWAGGER](https://img.shields.io/badge/SWAGGER-85EA2D?logo=swagger&logoColor=black&style=for-the-badge)

</div>


## 🧱 SERVICE ARCHITECTURE
![image](https://github.com/user-attachments/assets/61ace0e9-fd7f-49c5-8541-646c78a11d8d)


## 🚀 MAIN DEPLOYMENT FEATURES

|                   강민준                    |                 차정은                  |
|:----------------------------------------:|:------------------------------------:|
| board&comment(CRUD, bookmark, sort/paging) | login, logout, mypage, mainScreen |
| farewell(intro, recognize, reveal, remember, rebirth) | pet(CRUD), recollection(remind, record) |
| fcm, hira api & google map api | ai post, chatting |
| ci/cd | ci/cd | 

> 📌 **전체 이슈 목록은 [ISSUES](https://github.com/PROJECT-X-REFRESH/Back-Reborn/issues)에서 확인 가능합니다.**


## 📂 Project Architecture

```bash
📁 back-reborn
├── 📁 src
│   ├── 📁 main
│   │   ├── 📁 java/com/reborn/back
│   │   │   ├── 📁 aiPost
│   │   │   │    ├── 📁 controller
│   │   │   │    ├── 📁 converter
│   │   │   │    ├── 📁 dto
│   │   │   │    ├── 📁 service
│   │   │   │    └── 📁 repository
│   │   │   ├── 📁 board
│   │   │   ├── 📁 chat
│   │   │   ├── 📁 comment
│   │   │   ├── 📁 domain
│   │   │   ├── 📁 fcm
│   │   │   ├── 📁 global
│   │   │   ├── 📁 login
│   │   │   │    └── 📁 auth
│   │   │   ├── 📁 pet
│   │   │   └── 📁 review
│   │   └── 📁  resources
│   └── 📁 test
├── 📄 build.gradle
├── 📄 README.md
└── ...
```

## ⚙️ Local Build

```bash
git clone https://github.com/PROJECT-X-REFRESH/Back-Reborn.git
cd Back-Reborn
./gradlew clean
./gradlew build
./gradlew bootRun
./gradlew test
```


## 📦 ETC

### 로그인 flow chart

![image](https://github.com/user-attachments/assets/f2d7240f-eae6-4e94-96c8-e969c6d0ee5f)
