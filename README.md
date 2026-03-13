# A-AND-I-CONFIG-SERVER

A&I 플랫폼의 설정 제공 책임을 애플리케이션 배포 책임과 분리하기 위해 도입하는 독립형 Spring Cloud Config Server 프로젝트입니다.

현재 설정이 CI/CD 변수, 배포 스크립트, 런타임 환경 변수에 분산되어 있어 변경 이력 추적, 검토, 롤백, 시크릿 분리가 어렵기 때문에 이 저장소를 중앙 설정 서버의 시작점으로 사용합니다.

## 기술 스택

- Kotlin
- Spring Boot 4.0.3
- Spring Cloud Config Server
- Java 21

## 이 저장소가 존재하는 이유

- 비민감 설정의 단일 조회 지점을 만들기 위해
- 설정 변경을 Git 기반 검토와 이력 관리에 태우기 위해
- 시크릿을 일반 설정과 분리할 수 있는 구조를 준비하기 위해
- 애플리케이션 배포와 설정 전달 책임을 분리하기 위해

## Phase 1 포함 범위

- `@EnableConfigServer` 기반의 독립 실행형 Config Server
- 기본 프로필 `local`과 `native` 백엔드 기반 로컬 실행
- Basic Auth 기반 설정 조회 보호
- `/actuator/health`, `/actuator/info`만 비인증 공개
- 아래 서비스 이름에 맞춘 로컬 샘플 설정 파일 제공
  - `auth`
  - `gateway`
  - `aandi_post_web_server`
  - `tech.blog`
- 향후 운영용 `Git + AWS Secrets Manager` 구성을 위한 설정 분리
- 애플리케이션 컨텍스트, 공개 헬스 체크, 인증된 설정 조회를 검증하는 스모크 테스트

## 로컬 실행

기본 프로필은 `local`이며, AWS나 외부 Git 저장소 없이 `./local-config-repo` 경로의 샘플 설정만으로 바로 실행됩니다.

```bash
./gradlew bootRun
```

## 확인 방법

헬스 체크:

```bash
curl http://localhost:8888/actuator/health
```

설정 조회 예시:

```bash
curl -u config-user:change-this-before-deploy http://localhost:8888/auth/default
```

다른 샘플 서비스도 같은 방식으로 확인할 수 있습니다.

```bash
curl -u config-user:change-this-before-deploy http://localhost:8888/gateway/default
curl -u config-user:change-this-before-deploy http://localhost:8888/aandi_post_web_server/default
curl -u config-user:change-this-before-deploy http://localhost:8888/tech.blog/default
```

## 프로필 구성

- `local`: `./local-config-repo`를 사용하는 `native` 백엔드
- `aws`: 향후 운영 단계에서 사용할 `git` + `awssecretsmanager` 백엔드 조합

## 설정 소스 전략

- 로컬 개발: `native` backend + `./local-config-repo`
- 운영 비민감 설정: Git 저장소
- 운영 민감 시크릿: Secret Manager

운영 단계에서는 같은 key가 두 소스에 모두 존재할 경우 Secret Manager 값이 Git보다 우선하도록 유지합니다.

## 환경 변수

- `SERVER_PORT`: 서버 포트, 기본값 `8888`
- `CONFIG_SERVER_USERNAME`: Basic Auth 사용자명
- `CONFIG_SERVER_PASSWORD`: Basic Auth 비밀번호
- `CONFIG_GIT_URI`: Git 백엔드 저장소 주소
- `CONFIG_GIT_DEFAULT_LABEL`: Git 백엔드 브랜치 또는 라벨
- `AWS_REGION`: AWS Secrets Manager 리전
- `CONFIG_AWS_SECRETS_PREFIX`: Secrets Manager prefix 경로

로컬 부트스트랩 예시는 `.env.example` 파일을 참고하면 됩니다.

## 의도적으로 아직 하지 않는 것

- 실제 Git 설정 저장소 연동
- 실제 AWS Secrets Manager 시크릿 마이그레이션
- IAM 정책 구성
- `auth`, `gateway`, `aandi_post_web_server`, `tech.blog` 클라이언트 연동
- refresh bus 또는 동적 설정 갱신
- OAuth2, mTLS 같은 고급 인증
- 데이터베이스, Redis, 메시징 같은 추가 인프라

## 앞으로의 방향

이 저장소의 다음 단계 목표는 다음과 같습니다.

- 비민감 설정은 Git에서 관리
- 민감 시크릿은 Secret Manager에서 관리
- 각 서비스는 시작 시 Config Server를 통해 설정을 조회
- 설정 변경은 PR 기반으로 검토
- 설정 롤백은 Git label 또는 commit 기준으로 가능하게 정리
