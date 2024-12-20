# todolist-back
## TODO 앱 서버 
**개발기간** : 2024-12-10 ~ 2024-12-20
## 📢 프로젝트 소개
- 기본적인 TODO 어플리케이션의 서버코드입니다.
- 소셜 API를 사용하여 로그인 기능을 구현하였습니다.
- 캘린더를 활용하여 해당날짜를 선택하여 일정을 확인하고
  추가,수정,삭제 할 수 있습니다.
## ➡️ 프로젝트 실행방법
- 테이블 생성 <br/>
**sql 파일** : https://github.com/minjukim96412/todolist_back/blob/main/todolist-back/src/main/resources/templates/todolist.sql <br />
CREATE TABLE MEMBER ( 
    MEM_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    EMAIL VARCHAR(50) not NULL,
    NICKNAME VARCHAR(50) not NULL,
    TOKEN_ID VARCHAR(255) NOT NULL UNIQUE 
);<br/>
CREATE TABLE TODOLIST (
    TODO_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE VARCHAR(200) not NULL,
    CONTENT VARCHAR(4000) not NULL,
    START_DATE TIMESTAMP,
    END_DATE TIMESTAMP,
    CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    COMPLETEYN BOOLEAN,
    MEM_ID INT,
    CONSTRAINT FK_MEMBER FOREIGN KEY (MEM_ID) REFERENCES MEMBER(MEM_ID)
);<br/>
CREATE TABLE CALENDAR (
    CAL_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    CALDATE TIMESTAMP not NULL,
    TODO_ID INT,   
    MEM_ID INT,
    CONSTRAINT FK_TODOLIST FOREIGN KEY (TODO_ID) REFERENCES TODOLIST(TODO_ID) ON DELETE CASCADE,
    CONSTRAINT FK_MEM FOREIGN KEY (MEM_ID) REFERENCES MEMBER(MEM_ID)
);
<br/>
- spring build
  /todolist-back/src/main/java/com/todolist/TodolistBackApplication.java - 실행
## 🛠️ 기술 스택 todolist_back
<img src="https://simpleicons.org/icons/springboot.svg" width="50px"/> **Spring boot :**  스프링부트 프레임워크를 사용하였습니다.<br />
<img src="https://simpleicons.org/icons/kakaotalk.svg" width="50px"/> **kakao API :**  카카오API를 활용하여 소셜로그인을 구현하였습니다.<br />
<img src="https://simpleicons.org/icons/google.svg" width="50px"/> **google API :**  구글API를 활용하여 소셜로그인을 구현하였습니다.<br />
<img src="todolist-back/src/main/resources/templates/JPA.png" width="50px" /> **JPA :** JPA를 사용하여 데이터를 객체와 매핑하여 DB와 상호작용하였습니다.<br />
<img src="https://simpleicons.org/icons/apachemaven.svg" width="50px" /> **maven :** maven을 사용하여 필요한 라이브러리를 관리하고 빌드를 수행하였습니다.<br />
