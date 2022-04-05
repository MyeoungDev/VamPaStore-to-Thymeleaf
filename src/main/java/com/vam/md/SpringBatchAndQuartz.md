# [Spring Boot] Spring Batch, Quratz 


1. Gradle 의존성 주입

```
// https://mvnrepository.com/artifact/org.quartz-scheduler/quartz-jobs
implementation group: 'org.quartz-scheduler', name: 'quartz-jobs', version: '2.3.2'	/* Quartz-Jobs*/
//	https://mvnrepository.com/artifact/org.quartz-scheduler/quartz/2.3.2
implementation group: 'org.quartz-scheduler', name: 'quartz', version: '2.3.2'	/* Quartz */
```

두가지 방법이 존재한다. xml 설정과 Annotation 설정

이 중 Annotation 설정을 사용

package com.vam.task 패키지 추가
