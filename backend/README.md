### 디렉토리 구조 예시
```agsl
├─.gradle                        
│  ├─8.1.1
│  │  ├─checksums
│  │  ├─dependencies-accessors
│  │  ├─executionHistory
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.idea
├─gradle
│  └─wrapper
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─backend
    │  │          ├─api
    │  │          ├─domain
    │  │          │  ├─board
    │  │          │  └─member
    │  │          │      ├─controller
    │  │          │      ├─dto
    │  │          │      ├─entity
    │  │          │      ├─repository
    │  │          │      └─service
    │  │          ├─global
    │  │          │  ├─config
    │  │          │  ├─error
    │  │          │  └─jwt
    │  │          ├─infra
    │  │          └─oauth2
    │  └─resources
    │      ├─static
    │      └─templates
    └─test
        └─java
            └─com
                └─D101
                    └─backend

```

## Convention

- Commnit message
  - type: *commit message *개발자 *수정파일들
  - ex) feat : 로그인 기능 구현, 회원가입 기능 구현 홍길동 login.js
- type
  - feat : 새로운 기능 추가
  - modify : 기존 기능 수정
  - fix : 버그 수정
  - docs : 문서 수정
  - style : xml 등 디자인 수정
  - refactor : 코드 리팩토링
  - rename : 파일, 패키지 이름 변경
  - test : 테스트 코드, 리팩토링 테스트 코드 추가
  - chore : 빌드 업무 수정, 패키지 매니저 수정
  - config : 환경설정 파일 추가 및 수정
