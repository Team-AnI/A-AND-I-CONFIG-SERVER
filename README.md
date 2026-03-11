# A-AND-I-CONFIG-SERVER

A&I 서비스들의 설정값과 시크릿을 중앙에서 제공하기 위한 독립형 Spring Cloud Config Server 프로젝트입니다.

이 저장소는 공통 설정, 서비스별 설정, 시크릿 기반 값을 중앙에서 관리하고 각 서비스가 일관된 방식으로 조회할 수 있도록 구성되어 있습니다.

## 기술 스택

- Kotlin
- Spring Boot 4.0.3
- Spring Cloud Config Server
- Java 21

## 주요 기능

- `@EnableConfigServer` 기반의 독립 실행형 Config Server
- `./local-config-repo`를 사용하는 로컬 개발용 `native` 백엔드
- 추후 `Git + AWS Secrets Manager` 구조로 확장 가능한 설정 뼈대
- `health`, `info`를 제외한 설정 조회 엔드포인트 Basic Auth 보호

## 프로젝트 목적

- 로컬 환경에서 바로 실행 가능한 Config Server 골격 확보
- 각 서비스가 공통 방식으로 외부 설정을 조회할 수 있는 기반 마련
- 운영 단계에서 Git 저장소와 AWS Secrets Manager를 함께 사용할 수 있도록 구조 선반영

## 로컬 실행

기본 프로필은 `local`이며, `native` 백엔드가 활성화되어 `./local-config-repo` 경로의 설정 파일을 읽습니다.

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

## 프로필 구성

- `local`: `./local-config-repo`를 사용하는 `native` 백엔드
- `aws`: `git` + `awssecretsmanager` 백엔드 조합

## 환경 변수

- `SERVER_PORT`: 서버 포트, 기본값 `8888`
- `CONFIG_SERVER_USERNAME`: Basic Auth 사용자명
- `CONFIG_SERVER_PASSWORD`: Basic Auth 비밀번호
- `CONFIG_GIT_URI`: Git 백엔드 저장소 주소
- `CONFIG_GIT_DEFAULT_LABEL`: Git 백엔드 브랜치 또는 라벨
- `AWS_REGION`: AWS Secrets Manager 리전
- `CONFIG_AWS_SECRETS_PREFIX`: Secrets Manager prefix 경로

로컬 부트스트랩 예시는 `.env.example` 파일을 참고하면 됩니다.
