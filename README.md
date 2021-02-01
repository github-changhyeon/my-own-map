# Mockup test2

## 로그인, 회원가입 화면 

![image](https://user-images.githubusercontent.com/20719987/104943346-f90be580-59f8-11eb-8fad-a38f488c62ab.png)

## Main

![image](https://user-images.githubusercontent.com/20719987/104945007-63258a00-59fb-11eb-824d-1aa5ea8ac9c4.png)
![image](https://user-images.githubusercontent.com/20719987/104945043-720c3c80-59fb-11eb-97c0-a802de7974a9.png)
![image](https://user-images.githubusercontent.com/20719987/104945080-86e8d000-59fb-11eb-8a5f-550c2bdf5148.png)

## 마이페이지

![image](https://user-images.githubusercontent.com/20719987/104945143-9c5dfa00-59fb-11eb-8a75-a538e84355d5.png)
![image](https://user-images.githubusercontent.com/20719987/104945174-aa137f80-59fb-11eb-9829-a7e7a981f76b.png)
![image](https://user-images.githubusercontent.com/20719987/104945185-ae3f9d00-59fb-11eb-98db-1d40093cf8fe.png)

## 다른 사용자와 공유

![image](https://user-images.githubusercontent.com/20719987/104945280-d29b7980-59fb-11eb-9bad-3f066af38fe2.png)

## 게시글 조회 페이지

![image](https://user-images.githubusercontent.com/20719987/104945368-fa8add00-59fb-11eb-8bfb-b1e02615a941.png)

## 게시글 등록

![image](https://user-images.githubusercontent.com/20719987/104945413-0eceda00-59fc-11eb-8c8d-b1c60ef2a5ff.png)

# REST API 설계

(중요할 수록수록 0-에 가깝고, 99-는 구현에 있어 시간과 난이도가 파악되지 않은 부분)

## 회원관리

| 메서드명 | 메서드 설명                         | Http Verbs | Route       |
| -------- | ----------------------------------- | ---------- | ----------- |
| login    | 1-로그인                            | POST       | /user/login |
|          | 1-회원가입                          |            |             |
|          | 1-회원정보 조회                     |            |             |
|          | 1-회원정보 수정                     |            |             |
|          | 1-회원 탈퇴                         |            |             |
|          | 1-닉네임 중복 검사                  |            |             |
|          | 1-이메일 중복 검사                  |            |             |
|          | 2-이메일 인증                       |            |             |
|          | 2-이메일 인증 성공 여부 반환        |            |             |
|          | 2-PWD 찾기                          |            |             |
|          | 3-ID 찾기                           |            |             |
|          | 99-자동 로그인                      |            |             |
|          | 99-(+) 구글이나 카카오등 로그인 API |            |             |
|          |                                     |            |             |

## Main

| 메서드명 | 메서드 설명                                   | Http Verbs | Route |
| -------- | --------------------------------------------- | ---------- | ----- |
|          | 0-내 장소+이미지 리스트 반환 (최근 10개 포함) |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |
|          |                                               |            |       |

## 게시글(피드)

| 메서드명 | 메서드 설명                                                                  | Http Verbs | Route |
| -------- | ---------------------------------------------------------------------------- | ---------- | ----- |
|          | 0-게시글 등록                                                                |            |       |
|          | 0-게시글 하나 조회                                                           |            |       |
|          | 0-게시글 수정                                                                |            |       |
|          | 0-게시글 삭제                                                                |            |       |
|          | 0-(+) 사진 등록                                                              |            |       |
|          | 2-(+) 무한 스크롤링을 통한 페이징 - 몇개씩 불러올지                          |            |       |
|          | 2.5-즐겨찾기탭를 눌렀을 때 해당 유저의 즐겨찾기 리스트 가져오기(본인 + 타인) |            |       |
|          | 3-댓글 등록                                                                  |            |       |
|          | 3-댓글 목록 조회                                                             |            |       |
|          | 3-댓글 수정                                                                  |            |       |
|          | 3-댓글 삭제                                                                  |            |       |
|          | 3-(+) 좋아요 누르는 메서드?!                                                 |            |       |
|          | 3-DB - 친구 - 피드 (팔로우 / 일주일 내 게시글 요청)                          |            |       |
|          | 3-해당 게시글의 주소 조회                                                    |            |       |
|          |                                                                              |            |       |

2021-01-19 진행 예정 내용

명세서,

DB - 대략적인거,

Branch 전략(30분정도)

VUE CREATE

**vuetify** vs **bootstrap vue** vs **quasar**

spring security - https고려하면서 뼈대.
