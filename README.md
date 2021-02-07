
# Android Programming Study
> 2019년 2학기 안드로이드 프로그래밍 강의와 함께 스스로 학습한 내용을 정리했습니다.

안드로이드, Java/XML, JNI, Term Project 순서로 진행합니다.

## Term Project
- [링크](https://github.com/daerong/Linux_Android_Study/tree/master/Termproject_2011144024_UDS)

## Environment
Language
- Java
- C/C++

IDE
- Android Studio

Hardware
- Achro-I.MX6Q
- Galaxy S6 (API Level 24)

## 안드로이드 개요
### 안드로이드 구성
- Linux-based O/S
- XML
- Java / Kotlin
- NDK (C/C++ Library)
	- JNI programming
- Android SDK
- Device Driver
## 2안드로이드 구조 및 개발환경
### Android Layer
![Android_Layer](https://user-images.githubusercontent.com/26676087/85404706-5393af00-b59a-11ea-9542-83bea0fdebea.png)
<Blue : Java, Green : C/C++, Red : C> 

* Applications
* Application Framework
* Android Runtime
* Libraries
* Hardware Abstraction Layer
* Linux Kernel

### Linux Kernel
보안, 메모리 관리, 프로세스 관리, 네트워킹 등의 핵심 시스템 서비스를 위해 사용.

- 메모리 및 프로세스 관리
- 검증된 드라이버
- Permission 제어
- 공유 라이브러리 지원
- 오픈 소스 기반으로 필요에 따라 커스터마이징 가능

### Library
안드로이드에서 사용되는 라이브러리는 C/C++로 작성되어 있음.

- System C library : 임베디드 리눅스 기기를 위한 표준 C 시스템 라이브러리.
- Media Libraries : 정적 이미지 파일 및 비디오 파일의 재생 및 녹화
- Surface Manager : 2D, 3D 그래픽 레이어
- LibWebCore : 웹 브라우저 엔진
- 3D libraries : OpenGL 기반 3D 가속, 최적화
- FreeType : 비트맵, 폰트 렌더링
- SQLite : 경량 관계형 데이터베이스 엔진

### Hardware Abstraction Layer(HAL)
안드로이드가 Hardware driver 구현에 필요한 표준 인터페이스를 정의하고 있음.
- H/W, S/W를 분리하여 재사용성, 이식성 극대화
- HAL이 필요한 이유
	- Linux에서 모든 Component에 대해 driver interface가 표준화 되어있진 않음.
	- Kernel driver는 GPL로 공개되어 있어 사용시에 똑같이 소스공개 의무가 있음.
	- Device Driver는 H/W 제조사에 의존적임

### Android Runtime
안드로이드 자바 어플리케이션이 실행되는 환경을 제공.
- Dalvik
	- 초기버전 Dalvik, 단점을 보완한 ART Runtime.
- Core Libraries
	- Java 언어를 위한 Core API를 포함
	- 단순하고 강력한 개발 플랫폼 제공
	- Data structure, Utility, File Access, Network Access, Graphic 등이 포함.

### Application Framework
안드로이드 OS의 모든 기능은 Java로 작성된 API를 통해 접근이 가능
- Core System Service
	- Activity manager : 어플리케이션 생명주기 관리
	- Package manager : apk 파일을 불러옴
	- Window manager : 최상단 화면 Window
	- Resource manager : 스마트폰 테마 같은 것
	- content providers : 앱간의 데이터 이동
	- View system : 위젯, 뷰, 레이아웃 등
- Hardware Service
	- Location manager
	- Telephony manager : 단말정보
	- Bluetooth service 
	- WiFi service 
	- USB service
	- Sensor service : 안드로이드 기기엔 정말 다양한 센서가 있음

### Android Application
안드로이드에서 프로그램에 해당하는 영역.
- 어플리케이션은 최소 하나의 액티비티를 가짐
- 각 앱에 대해 엑세스 권한을 부여

### 개발환경
앱개발환경의 구성요소
- JDK
- IDE : Android Studio
- Android SDK
- AVD
- USB driver : 스마트폰 통합 USB 드라이버

안드로이드 개발환경의 종류
- 응용 프로그램 개발(SDK)
- 시스템 응용 프로그램 개발(NDK)
- 하드웨어 제이 및 커널 관련 개발(PDK)

## 센서 활용
### 터치 센서
### 스마트폰 센서
### 모션 센서
### 위치/환경 센서

```
