# Wedding Book Coding Test
- - - - - - 
##### 지원자 : 백엔드 개발자 모준서
- - - - - - 
### 사용 기술 스택
* Java 11
* Spring Boot 2.1.7RELEASE
* Spring JPA
* Spring Security
* Oauth2
* Spring AOP
* Spring HATEOAS
* H2 
* Lombok
* Maven
- - - - - -
### 요구사항 분석
1. 게시글 추가 API    
  1.1. [**Entity**]게시글은 제목, 내용, 작성자이름, 비밀번호 로 구성됨.    
    1.1.1. [**Valid**]제목은 20자, 내용은 200자를 넘을 수 없음.    
    1.1.2. [**Valid**]비밀번호는 6자 이상, 영어와 숫자를 조합.    
2. 리스트 API    
  2.1. [**JPA Custom**]한번에 10개씩 최신순 정렬   
  2.2. [**Pageable**]페이징은 무한스크롤 방식a   
3. 게시글 상세 API   
4. 게시글 수정 API   
  4.1. 제목과 내용을 수정 할 수 있음.   
  4.2. [**Dto, equals, Exception**]수정 시 비밀번호를 입력, 기존 비밀번호와 일치하면 수정. 일치하지 않으면 401 에러   
5. 게시글 삭제 API   
  5.1. [**Dto, equals, Exception**]삭제 시 비밀번호를 입력, 기존 비밀번호와 일치하면 삭제. 일치하지 않으면 401 에러   
  5.2. [**Data Management**]추후 마이페이지 기능을 만들 때, 삭제된 게시글을 복구 하는 기능이 들어갈 예정.
6. 로그   
  6.1. [**AOP, Logger**]성능 확인을 위해 모든 요청에 응답까지 걸리는 시간을 log 에 기록해야함.   
7. reame.md에 API의 기능명세 작성   

* DB 테이블은 embedded H2 Database를 이용해주세요.
* 데이터베이스 연동은 JPA를 이용해주세요.
- - - -
### 시나리오
1. '테스트 계정 생성'을 통해 Access Token을 발급.
2. Bearer Auth 방식으로, Access Token과 함께 Request.
2. 임의 게시글 생성서비스 또는 API를 통하여 게시글 생성.
3. 각 API 이용.
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
* 사용자 비밀번호는 BcryptPasswordEncoder를 통해 암호화 된다.
* Serializer를 통해 Author의 정보 중 민감 정보는 숨겨진다
   * id(PK), password는 반환되지 않는다.
* roles는 fetch.EAGER 전략을 사용한다.
    
#### Board
##### 게시글 모델
|변수|타입|설명|
|----|----|----|
|id(PK)|Integer|게시글 고유 아이디|
|title|String|게시글 제목|
|content|String|게시글 내용|
|author|Author|게시글 작성자|
|password|String|게시글 비밀번호|
* id(PK)는 GenerationType.IDENTITY 전략을 사용한다.   
* title, content, password는 Valid를 거친다.
* author는 N:1의 관계를 가진다.

#### BoardDeleted
##### 게시글 삭제 모델
* 모델 및 타입은 Board와 같다.
* 'Board'에서 삭제되면 'BoardDeleted'로 옮겨져서 관리된다.   
* 'Board'에서 'BoardDeleted'으로 Migration하여, 추후 복구기능이 추가 될 때 사용되도록한다.
- - - - 
### API 명세   
> 각 명세에는 Reqeust / Response를 설명한다.
#### API 구조
> Controller -> ServiceImpl <- Service interface
* 다형성을 위해 Service interface에서 필요기능이 정의된다.
* ServiceImpl에서 interface를 Override하여 Transcation/Bussiness를 처리한다.   
* Controller에서 ServiceImpl의 기능을 호출한다.
#### API 종류
|패키지|API 명|Request Type|설명|
|:---:|:---:|:---:|:---:|
|Author|getTestID|GET|테스트 계정 발급|
|Board|saveBoard|POST|게시글 저장|
|Board|getBoardList|GET|게시글 리스트|
|Board|getBoard|GET|게시글 상세조회|
|Board|deleteBoard|DELETE|게시글 삭제|
|Board|putBoard|PUT|게시글 수정|
|Board|saveTestBoardConent|POST|테시트 게시글 생성|

#### API 상세 명세
#### 테스트 계정 발급
>테스트 계정을 발급 받아, 각 API 접근에 필요한 Access Token을 발급받는다.   
##### Request
````
curl --location --request GET 'http://localhost:8080/api/author'
````
##### Response
````
{
    "userName": "PO",
    "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
    "role": "[USER]"
}
````
#### 게시글 저장 
> 요구사항에 따른 정보들을 Dto에 담아, 게시글을 저장한다.
##### Request
````
curl --location --request POST 'http://localhost:8080/api/board' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27' \
--header 'Content-Type: application/json' \
--data-raw '{
    "title" : "테스트 제목",
    "content" : "테스트 내용",
    "password" : "1234abcd"

}'
````
##### Response
````
{
    "createdAt": "2020-07-05 22:34:05",
    "modifiedAt": "2020-07-05 22:34:05",
    "id": 1,
    "title": "테스트 제목",
    "content": "테스트 내용",
    "author": {
        "userName": "PO",
        "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
        "role": "[USER]"
    },
    "password": "1234abcd",
    "_links": {
        "[POST]게시물 추가": {
            "href": "http://localhost:8080/api/board"
        },
        "[GET]게시물 전체 조회": {
            "href": "http://localhost:8080/api/board/list"
        },
        "[GET]게시물 상세 조회": {
            "href": "http://localhost:8080/api/board"
        },
        "[PUT]게시물 수정": {
            "href": "http://localhost:8080/api/board"
        },
        "[DELETE]게시물 삭제": {
            "href": "http://localhost:8080/api/board"
        },
        "self": {
            "href": "http://localhost:8080/api/board"
        },
        "profile": {
            "href": "/docs/index.html#resource-postBoard"
        }
    }
}
````
#### 게시글 리스트 
> 현재 생성된 게시글을 10개 단위로, 생성날짜를 기준으로 내림차순하여 보여준다.    
> 페이징하여 전달하고, 클라이언트에서 스크롤링을 통해 해당 페이지의 마지막 게시글에 도착하였을 때, 다음 페이지 주소를 통하여 로딩할 수 있다.
##### Request
````
curl --location --request GET 'http://localhost:8080/api/board/list' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27'
````
##### Response
````
{
    "_embedded": {
        "boardList": [
            {
                "createdAt": "2020-07-05 22:34:05",
                "modifiedAt": "2020-07-05 22:34:05",
                "id": 1,
                "title": "테스트 제목",
                "content": "테스트 내용",
                "author": {
                    "userName": "PO",
                    "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
                    "role": "[USER]"
                },
                "password": "1234abcd"
            }
        ]
    },
    "_links": {
        "self": [
            {
                "href": "http://localhost:8080/api/board/list?page=0&size=10"
            },
            {
                "href": "http://localhost:8080/api/board/list"
            }
        ],
        "[POST]게시물 추가": {
            "href": "http://localhost:8080/api/board"
        },
        "[GET]게시물 전체 조회": {
            "href": "http://localhost:8080/api/board/list"
        },
        "[GET]게시물 상세 조회": {
            "href": "http://localhost:8080/api/board"
        },
        "[PUT]게시물 수정": {
            "href": "http://localhost:8080/api/board"
        },
        "[DELETE]게시물 삭제": {
            "href": "http://localhost:8080/api/board"
        },
        "profile": {
            "href": "/docs/index.html#resource-getBoardList"
        }
    },
    "page": {
        "size": 10,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
````
#### 게시글 상세 리스트
>클라이언트에서 전달된 게시글 ID를 기준으로, 해당 게시글의 상세정보를 보여준다.
##### Request
````
curl --location --request GET 'http://localhost:8080/api/board/1' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27'
````
##### Response
````
{
    "createdAt": "2020-07-05 22:34:05",
    "modifiedAt": "2020-07-05 22:34:05",
    "id": 1,
    "title": "테스트 제목",
    "content": "테스트 내용",
    "author": {
        "userName": "PO",
        "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
        "role": "[USER]"
    },
    "password": "1234abcd",
    "_links": {
        "[POST]게시물 추가": {
            "href": "http://localhost:8080/api/board"
        },
        "[GET]게시물 전체 조회": {
            "href": "http://localhost:8080/api/board/list"
        },
        "[GET]게시물 상세 조회": {
            "href": "http://localhost:8080/api/board"
        },
        "[PUT]게시물 수정": {
            "href": "http://localhost:8080/api/board"
        },
        "[DELETE]게시물 삭제": {
            "href": "http://localhost:8080/api/board"
        },
        "self": {
            "href": "http://localhost:8080/api/board"
        },
        "profile": {
            "href": "/docs/index.html#resource-getBoard"
        }
    }
}
````
#### 게시글 삭제 
>클라이언트에서 게시글 ID와 게시글 비밀번호를 기준으로, 검증을 거친 후 게시글을 삭제한다.   
>게시글은 Board 테이블에서 BoardDeleted 테이블로 Migration 된다.   
##### Request
````
curl --location --request DELETE 'http://localhost:8080/api/board' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id" : 1,
    "password" : "1234abcd"
}'
````
##### Response
````
{
    "createdAt": "2020-07-05 22:34:05",
    "modifiedAt": "2020-07-05 22:34:05",
    "id": 1,
    "title": "테스트 제목",
    "content": "테스트 내용",
    "author": {
        "userName": "PO",
        "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
        "role": "[USER]"
    },
    "password": "1234abcd",
    "_links": {
        "[POST]게시물 추가": {
            "href": "http://localhost:8080/api/board"
        },
        "[GET]게시물 전체 조회": {
            "href": "http://localhost:8080/api/board/list"
        },
        "[GET]게시물 상세 조회": {
            "href": "http://localhost:8080/api/board"
        },
        "[PUT]게시물 수정": {
            "href": "http://localhost:8080/api/board"
        },
        "[DELETE]게시물 삭제": {
            "href": "http://localhost:8080/api/board"
        },
        "self": {
            "href": "http://localhost:8080/api/board"
        },
        "profile": {
            "href": "/docs/index.html#resource-deleteBoard"
        }
    }
}
````
#### 게시글 수정
>클라이언트에서 게시글 ID, 게시글 비밀번호, 수정 내용을 기준으로, 검증을 거친 후 게시글을 수정한다. 
##### Request
````
curl --location --request PUT 'http://localhost:8080/api/board' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id" : 2,
    "title" : "수정된 제목",
    "content" : "수정된 내용",
    "password" : "1234bbbb"   
}'
````
##### Response
````
{
    "createdAt": "2020-07-05 22:39:51",
    "modifiedAt": "2020-07-05 22:40:32",
    "id": 2,
    "title": "수정된 제목",
    "content": "수정된 내용",
    "author": {
        "userName": "PO",
        "bearer token": "80ec71fb-3229-4a5c-a86a-64e92193ea27",
        "role": "[USER]"
    },
    "password": "1234bbbb",
    "_links": {
        "[POST]게시물 추가": {
            "href": "http://localhost:8080/api/board"
        },
        "[GET]게시물 전체 조회": {
            "href": "http://localhost:8080/api/board/list"
        },
        "[GET]게시물 상세 조회": {
            "href": "http://localhost:8080/api/board"
        },
        "[PUT]게시물 수정": {
            "href": "http://localhost:8080/api/board"
        },
        "[DELETE]게시물 삭제": {
            "href": "http://localhost:8080/api/board"
        },
        "self": {
            "href": "http://localhost:8080/api/board"
        },
        "profile": {
            "href": "/docs/index.html#resource-putBoard"
        }
    }
}
````
#### 테스트 게시글 생
>페이징된 결과를 쉽게 볼 수 있도록 40개의 테스트 게시글을 생성한다. 
##### Request
````
curl --location --request POST 'http://localhost:8080/api/board/makeCases' \
--header 'Authorization: Bearer 80ec71fb-3229-4a5c-a86a-64e92193ea27'
````
##### Response
````
40개의 테스트 게시물이 생성되었습니다.
````
- - - -
### 성능 측정 로그
* Spring Boot AOP를 통하여, com.community.wedding.Board.BoardController에서 서비스되는 기능들에 대한 시간을 측정할 수 있다.
* Log로 해당 메소드가 끝나면 출력되며, info, debug 타입으로 출력된다.   
<img width="1253" alt="스크린샷 2020-07-05 오후 10 46 09" src="https://user-images.githubusercontent.com/20733918/86534247-fab90480-bf11-11ea-96cb-05b3cdcde45e.png">
- - - -
### 테스트
* 테스트는 TDD/BDD 방식으로 진행하였으며, Junit4를 이용한다.   
* 아래 리스트를 BoardControllerTest에서 진행.
1. 7개의 정상 Case 테스트.
2. 게시글 저장할 때, 제목 조건을 만족하지 못할 경우 테스트.
2. 게시글 저장할 때, 내용 조건을 만족하지 못할 경우 테스트.
4. 게시글 저장할 때, 비밀번호 조건을 만족하지 못할 경우 테스트.
5. 게시글 삭제할 때, 비밀번호가 다 경우 테스트.
6. 게시글 수정할 때, 비밀번호가 다른 경우 테스트.
- - - - 
### Exception
* 요구사항 이외에, 판단되는 예외들에 대한 Exception 처리.
* 주로 사용된 Exceptions
    * UsernameNotFound, Illegalargument ... 등.
* 요구사항에 명시 된 예외처리는 Exception 처리하지 않고, 해당 HttpStatus를 Return하게 구현.

## 감사합니다.  