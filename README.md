# universal-credit-liability-performance-tests

Performance test suite for the [universal-credit-liability-api](https://github.com/hmrc/universal-credit-liability-api),
using [performance-test-runner](https://github.com/hmrc/performance-test-runner) under the hood.

## Journey

The `universal-credit-liability-notification-journey` simulates the DWP sending Universal Credit Liability
notifications (`Insert` and `Terminate`) to HMRC. This is a high-volume, sustained traffic occurring after the end
of the Financial Year.

- **Endpoint:** `POST /notification`
- **Actions:**
    - **Insert**: Adding a new Universal Credit Liability record
    - **Terminate**: Closing an existing Universal Credit Liability record
- **Throughput:**
    - **Baseline:** 4 Transactions Per Second (TPS)

**Note:** For detailed information on how these throughput requirements are calculated, refer to
the [MDTP Performance Load and Estimates Guide](https://docs.tax.service.gov.uk/mdtp-handbook/documentation/mdtp-test-approach/performance-testing/performance-load-and-estimates-guide/index.html#portal-rehoming-legacy-software-architecture).

## Load Profiles

| Scenario        | Load Percentage | Throughput | Purpose                      |
|:----------------|:----------------|:-----------|:-----------------------------|
| **Baseline**    | 100%            | 4 TPS      | Expected peak traffic        |
| **Stress Test** | 150%            | 6 TPS      | Stability above expectations |


## Pre-requisites

### Services

Start Mongo Docker container following instructions from
the [MDTP Handbook](https://docs.tax.service.gov.uk/mdtp-handbook/documentation/developer-set-up/set-up-mongodb.html).

Start `UNIVERSAL_CREDIT_LIABILITY_ALL` services as follows:

```bash
sm2 --start UNIVERSAL_CREDIT_LIABILITY_ALL
```

### Logging

The default log level for all HTTP requests is set to `WARN`. Configure [logback.xml](src/test/resources/logback.xml) to
update this if required.

### WARNING :warning:

Do **NOT** run a full performance test against staging from your local machine.
Please [implement a new performance test job](https://docs.tax.service.gov.uk/mdtp-handbook/documentation/mdtp-test-approach/performance-testing/performance-test-a-microservice/index.html)
and execute your job from the dashboard in [Performance Jenkins](https://performance.tools.staging.tax.service.gov.uk).

## Tests

Run smoke test (locally) as follows:

```bash
sbt -Dperftest.runSmokeTest=true -DrunLocal=true gatling:test
```

Run full performance test (locally) as follows:

```bash
sbt -DrunLocal=true gatling:test
```

Run smoke test (staging) as follows:

```bash
sbt -Dperftest.runSmokeTest=true -DrunLocal=false gatling:test
```

## Scalafmt

Check all project files are formatted as expected as follows:

```bash
sbt scalafmtCheckAll scalafmtCheck
```

Format `*.sbt` and `project/*.scala` files as follows:

```bash
sbt scalafmtSbt
```

Format all project files as follows:

```bash
sbt scalafmtAll
```

## License

This code is open source software licensed under
the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
