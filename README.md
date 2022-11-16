![Group 608](README.assets/Group 608.png)



# 🔵 MeetUp

>  ## [밋업 서비스 바로가기](http://meet-up.co.kr)
>
>  SSAFY를 위한 미팅 일정 관리 서비스, **밋업**을 소개합니다!



밋업의 사용법을 알아보고 싶다면 ▶ [유저 시나리오-컨설턴트](https://meetup.gitbook.io/meetup-docs/user-scenario/consultant) |   [유저 시나리오-학생](https://meetup.gitbook.io/meetup-docs/user-scenario/student)

버그 제보나 개선사항 요구도 늘 환영이에요! ▶  [밋업-버그 리포트](https://docs.google.com/spreadsheets/d/1shZ7Fk3twrIv9QMS2wZ3ZeBicoMZxZ4CCWvJozkZWIE/edit?usp=sharing)

전체적인 후기를 남겨주시면 더 좋은 밋업에 많은 도움이 됩니다! ▶ [사용자 후기](https://docs.google.com/forms/d/1mgH_1hJLntyPuFYzc4ivK6Yx6mBxodJVEMPjuEABkUY/edit?ts=6360db33) 



# 🚀 Project & Members

> 진행기간: 2022. 10. 10. ~ 2022. 11. 18 (6주)

### 프로젝트 소개

💡 **[MeetUp 노션 구경가기](https://www.notion.so/MEET-UP-6622422c3f554e6e852e7996eefeec77) | [MeetUp GitBook 구경가기](https://meetup.gitbook.io/meetup-docs/)**



### 프로젝트 멤버

**[박성민👑](https://github.com/seongminP98) | [김명석](https://github.com/audtjr9514) | [신선영](https://github.com/drsuneamer) | [연승용](https://github.com/silversalmon216) | [이규민](https://github.com/qminlee723) | [채민진](https://github.com/MinjinChae)**



# 📌 기획 배경 및 주요 기능

### 기획 의도



### 용어 설명

- 권한: 컨설턴트/코치/프로/교수 등급(이하 `컨설턴트`로 통칭) 과 학생 등급으로 분류

- `밋업` : 매터모스트 채널 중 컨설턴트가 미팅 신청을 받기로 선택한 채널을 의미함

  - 밋업에 속한 학생들은 컨설턴트의 캘린더에 접근하여 미팅 신청 가능

    

### 주요 기능

#### 0. 로그인

![login](README.assets/login.gif)

- Mattermost API를 이용하여 로그인
- SSAFY mm에 등록된 매터모스트 id와 비밀번호 이용하여 로그인 가능



#### 1. 밋업(알림을 보낼 채널) 설정

![createmeetup](README.assets/createmeetup.gif)

- 컨설턴트 권한의 사용자는 `밋업 관리하기`를 이용해 자신이 미팅 신청을 받을 채널인 밋업을 설정할 수 있음
- 채널 이름과 별개로 자신이 사용할 이름과, 캘린더에 일정을 표시할 색상 설정 가능

![editmeetup](README.assets/editmeetup.gif)

- 밋업의 이름과 색상은 변경 가능
- 색상 변경 시 캘린더에 표시되는 일정의 색도 변경

![otherscalendar](README.assets/otherscalendar.gif)

- 밋업의 인덱스를 클릭하면 현재 밋업에 포함된 전체 학생의 이름을 조회할 수 있고, 각 이름을 클릭하면 해당 학생의 캘린더로 이동

#### 2. 미팅 신청

#### 3. 개인 스케줄 설정

#### 4. 부가 기능

##### 4-1. 개인 웹엑스 설정 관리

![webexlink](README.assets/webexlink.gif)

- 정보 수정 페이지 (헤더의 닉네임 클릭하여 접근)에서 웹엑스 주소 설정
- 각 멤버의 캘린더 왼쪽 상단의 웹엑스 로고 클릭하면 설정된 링크로 이동
- 미팅/스케줄의 정보 확인 시에도 설정된 웹엑스 링크로 이동 가능 

##### 4-2. 팀 활성화/비활성화

![teamonoff](README.assets/teamonoff.gif)

- 자신이 포함된 매터모스트 팀들 중 밋업 서비스 내에서 사용할 팀만 선택하여 사용할 수 있음

##### 4-3. 그룹 설정

![creategroup](README.assets/creategroup.gif)

- 자신의 일정을 공유할 그룹 설정 가능
- 자신이 만든 그룹인지의 여부는 왕관 아이콘으로 확인
- 이후 미팅 신청 시 그룹을 선택하면 그룹 멤버의 캘린더에도 해당 일정 자동으로 등록

#### 5. 관리자

![admin](README.assets/admin.gif)

- 관리자 아이디를 이용하여 전체 멤버의 현재 권한을 조회하고 편집 가능

  

#  🤝 협업툴

- Git
- GitLab
- Atlassian Jira
- Notion
- Mattermost
- Webex



# ⚙️ 기술 스택

### **backend**

- IntelliJ IDE

- Java 11

- Springboot 2.7.3

- Spring Data JPA

- Spring Security

- MySQL

- Swagger 2.9.2

- mattermost4j-core

- spring-restdocs

  

### **frontend**

- Visual Studio Code IDE
- TypeScript
- React 18.2.0
- Redux-toolkit 1.8.5
- TailwindCSS
- Material UI



### **API**

* Mattermost API



### **CI/CD**

- Docker
- AWS EC2
- Jenkins
- NGINX
- SSL
