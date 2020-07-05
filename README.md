# Wedding Book Coding Test
- - - - - - 
##### 지원자 : 백엔드 개발자 모준서
- - - - - - 
### 사용 기술 스택
>* Java 11
>* Spring Boot 2.1.7RELEASE
>* Spring JPA
>* Spring Security
>* Oauth2
>* Spring AOP
>* Spring HATEOAS
>* H2 
>* Lombok
>* Maven
- - - - - -
### 요구사항 분석
>1. 게시글 추가 API    
  1.1. [**Entity**]게시글은 제목, 내용, 작성자이름, 비밀번호 로 구성됨.    
    1.1.1. [**Valid**]제목은 20자, 내용은 200자를 넘을 수 없음.    
    1.1.2. [**Valid**]비밀번호는 6자 이상, 영어와 숫자를 조합.    
>2. 리스트 API    
  2.1. [**JPA Custom**]한번에 10개씩 최신순 정렬   
  2.2. [**Pageable**]페이징은 무한스크롤 방식a   
>3. 게시글 상세 API   
>4. 게시글 수정 API   
  4.1. 제목과 내용을 수정 할 수 있음.   
  4.2. [**Dto, equals, Exception**]수정 시 비밀번호를 입력, 기존 비밀번호와 일치하면 수정. 일치하지 않으면 401 에러   
>5. 게시글 삭제 API   
  5.1. [**Dto, equals, Exception**]삭제 시 비밀번호를 입력, 기존 비밀번호와 일치하면 삭제. 일치하지 않으면 401 에러   
  5.2. [**Data Management**]추후 마이페이지 기능을 만들 때, 삭제된 게시글을 복구 하는 기능이 들어갈 예정.
>6. 로그   
  6.1. [**AOP, Logger**]성능 확인을 위해 모든 요청에 응답까지 걸리는 시간을 log 에 기록해야함.   
>7. reame.md에 API의 기능명세 작성   

>* DB 테이블은 embedded H2 Database를 이용해주세요.
>* 데이터베이스 연동은 JPA를 이용해주세요.
- - - -
### 시나리오
>1. 사용자 계정생성 및 Access Token 발급.
>2. 임의 게시물 생성서비스 또는 API를 통하여 게시물 생성.
>3. 각 API 이용.
- - - -
### Model 설명
#### Author
##### 작성자 모델
|변수|타입|설명|
|----|----|----|
|id(PK)|String|작성자 고유 아이디|
|name|String|작성자 이름|
|password|String|작성자 비밀번호|
|roles|Set|작성자 권한|
|servcieAccessToken|String|작성자 Access Token|
>* 사용자 비밀번호는 BcryptPasswordEncoder를 통해 암호화 된다.
>* Serializer를 통해 Author의 정보 중 민감 정보는 숨겨진다
>   * id(PK), password는 반환되지 않는다.
>* roles는 fetch.EAGER 전략을 사용한다.
    
#### Board
##### 게시물 모델
|변수|타입|설명|
|----|----|----|
|id(PK)|Integer|게시물 고유 아이디|
|title|String|게시물 제목|
|content|String|게시물 내용|
|author|Author|게시물 작성자|
|password|String|게시물 비밀번호|
>* id(PK)는 GenerationType.IDENTITY 전략을 사용한다.   
>* title, content, password는 Valid를 거친다.
>* author는 N:1의 관계를 가진다.

#### BoardDeleted
##### 게시물 삭제 모델
>* 모델 및 타입은 Board와 같다.
>* 'Board'에서 삭제되면 'BoardDeleted'로 옮겨져서 관리된다.   
>* 'Board'에서 'BoardDeleted'으로 Migration하여, 추후 복구기능이 추가 될 때 사용되도록한다.
- - - - 
### API 명세   
> 각 명세에는 Reqeust / Response를 설명한다.
#### API 구조
> Controller -> ServiceImpl <- Service interface
>* 다형성을 위해 Service interface에서 필요기능이 정의된다.
>* ServiceImpl에서 interface를 Override하여 Transcation/Bussiness를 처리한다.   
>* Controller에서 ServiceImpl의 기능을 호출한다.
#### API 종류
|패키지|API 명|Request Type|설명|
|:---:|:---:|:---:|:---:|
|Author|getTestID|GET|테스트 계정 발급|
|Board|saveBoard|POST|게시물 저장|
|Board|getBoardList|GET|게시물 리스트|
|Board|getBoard|GET|게시물 상세조회|
|Board|deleteBoard|DELETE|게시물 삭제|
|Board|putBoard|PUT|게시물 수정|
|Board|saveTestBoardConent|POST|테시트 게시물 생성|

#### API 상세 명세
#### 테스트 계정 발급
>ff
##### Request
````

````
#### Request
````

````

