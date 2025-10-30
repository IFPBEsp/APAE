@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  documentos-digitalizados startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DOCUMENTOS_DIGITALIZADOS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\documentos-digitalizados-0.0.1-SNAPSHOT-plain.jar;%APP_HOME%\lib\spring-boot-devtools-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-validation-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-web-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-oauth2-resource-server-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-security-3.4.5.jar;%APP_HOME%\lib\minio-8.5.17.jar;%APP_HOME%\lib\springdoc-openapi-starter-webmvc-ui-2.8.6.jar;%APP_HOME%\lib\spring-boot-starter-json-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-3.4.5.jar;%APP_HOME%\lib\springdoc-openapi-starter-webmvc-api-2.8.6.jar;%APP_HOME%\lib\springdoc-openapi-starter-common-2.8.6.jar;%APP_HOME%\lib\spring-boot-autoconfigure-3.4.5.jar;%APP_HOME%\lib\spring-boot-3.4.5.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-3.4.5.jar;%APP_HOME%\lib\tomcat-embed-el-10.1.40.jar;%APP_HOME%\lib\hibernate-validator-8.0.2.Final.jar;%APP_HOME%\lib\spring-webmvc-6.2.6.jar;%APP_HOME%\lib\spring-security-oauth2-resource-server-6.4.5.jar;%APP_HOME%\lib\spring-security-web-6.4.5.jar;%APP_HOME%\lib\spring-security-oauth2-jose-6.4.5.jar;%APP_HOME%\lib\spring-security-oauth2-core-6.4.5.jar;%APP_HOME%\lib\spring-web-6.2.6.jar;%APP_HOME%\lib\spring-security-config-6.4.5.jar;%APP_HOME%\lib\spring-security-core-6.4.5.jar;%APP_HOME%\lib\spring-context-6.2.6.jar;%APP_HOME%\lib\spring-aop-6.2.6.jar;%APP_HOME%\lib\simple-xml-safe-2.7.1.jar;%APP_HOME%\lib\guava-33.3.1-jre.jar;%APP_HOME%\lib\okhttp-4.12.0.jar;%APP_HOME%\lib\swagger-core-jakarta-2.2.29.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.18.3.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.18.3.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.18.3.jar;%APP_HOME%\lib\jackson-core-2.18.3.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.18.3.jar;%APP_HOME%\lib\jackson-databind-2.18.3.jar;%APP_HOME%\lib\swagger-models-jakarta-2.2.29.jar;%APP_HOME%\lib\jackson-annotations-2.18.3.jar;%APP_HOME%\lib\bcprov-jdk18on-1.78.1.jar;%APP_HOME%\lib\commons-compress-1.27.1.jar;%APP_HOME%\lib\commons-codec-1.17.2.jar;%APP_HOME%\lib\snappy-java-1.1.10.7.jar;%APP_HOME%\lib\swagger-ui-5.20.1.jar;%APP_HOME%\lib\webjars-locator-lite-1.0.1.jar;%APP_HOME%\lib\spring-beans-6.2.6.jar;%APP_HOME%\lib\spring-expression-6.2.6.jar;%APP_HOME%\lib\spring-core-6.2.6.jar;%APP_HOME%\lib\spring-boot-starter-logging-3.4.5.jar;%APP_HOME%\lib\jakarta.annotation-api-2.1.1.jar;%APP_HOME%\lib\snakeyaml-2.3.jar;%APP_HOME%\lib\jakarta.validation-api-3.0.2.jar;%APP_HOME%\lib\jboss-logging-3.6.1.Final.jar;%APP_HOME%\lib\classmate-1.7.0.jar;%APP_HOME%\lib\tomcat-embed-websocket-10.1.40.jar;%APP_HOME%\lib\tomcat-embed-core-10.1.40.jar;%APP_HOME%\lib\micrometer-observation-1.14.6.jar;%APP_HOME%\lib\spring-security-crypto-6.4.5.jar;%APP_HOME%\lib\nimbus-jose-jwt-9.37.3.jar;%APP_HOME%\lib\failureaccess-1.0.2.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.43.0.jar;%APP_HOME%\lib\error_prone_annotations-2.28.0.jar;%APP_HOME%\lib\j2objc-annotations-3.0.0.jar;%APP_HOME%\lib\okio-jvm-3.6.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.9.25.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.25.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.9.25.jar;%APP_HOME%\lib\commons-io-2.16.1.jar;%APP_HOME%\lib\commons-lang3-3.17.0.jar;%APP_HOME%\lib\jspecify-1.0.0.jar;%APP_HOME%\lib\spring-jcl-6.2.6.jar;%APP_HOME%\lib\logback-classic-1.5.18.jar;%APP_HOME%\lib\log4j-to-slf4j-2.24.3.jar;%APP_HOME%\lib\jul-to-slf4j-2.0.17.jar;%APP_HOME%\lib\micrometer-commons-1.14.6.jar;%APP_HOME%\lib\jcip-annotations-1.0-1.jar;%APP_HOME%\lib\logback-core-1.5.18.jar;%APP_HOME%\lib\slf4j-api-2.0.17.jar;%APP_HOME%\lib\log4j-api-2.24.3.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\swagger-annotations-jakarta-2.2.29.jar;%APP_HOME%\lib\jakarta.xml.bind-api-4.0.2.jar;%APP_HOME%\lib\jakarta.activation-api-2.1.3.jar


@rem Execute documentos-digitalizados
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DOCUMENTOS_DIGITALIZADOS_OPTS%  -classpath "%CLASSPATH%" br.org.apae.documentos_digitalizados.DocumentosDigitalizadosApplication %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable DOCUMENTOS_DIGITALIZADOS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%DOCUMENTOS_DIGITALIZADOS_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
