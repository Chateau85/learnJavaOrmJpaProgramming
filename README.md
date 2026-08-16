# learnJavaOrmJpaProgramming

Jakarta Persistence와 Hibernate ORM의 기본 영속성 및 연관관계 매핑을 학습하는 예제입니다.

## 환경

- Java 17
- Hibernate ORM 7.4
- Jakarta Persistence 3.2
- H2 인메모리 데이터베이스
- Maven Wrapper

별도의 H2 서버나 외부 데이터베이스 설정 없이 실행할 수 있습니다. 데이터베이스 스키마와 데이터는 프로세스가 종료되면 제거됩니다.

## 검증

Windows에서는 테스트, JAR·SBOM 생성, SpotBugs 및 FindSecBugs 검사를 함께 실행합니다.

```powershell
.\mvnw.cmd clean verify
```

macOS 또는 Linux에서는 다음 명령을 사용합니다.

```bash
./mvnw clean verify
```

CycloneDX JSON SBOM은 `target/bom.json`에 생성됩니다. SpotBugs는 Medium 이상 발견 항목이 있으면 빌드를 실패시킵니다.

## 예제 실행

IDE에서 `hellojpa.JpaMain`을 실행하면 팀과 회원을 저장한 뒤 연관관계를 다시 조회합니다.
