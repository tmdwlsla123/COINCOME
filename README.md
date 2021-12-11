# Description
국내 4대거래소 및 해외거래소 프리미엄 차이, 수익/손실 계산기, 거래소 공지 확인 어플
# Development Environment
- Java

## Application Version
- minSdk 23
- targetSdk 31

# Prerequisite
    // retrofit2
    implementation group: 'com.squareup.retrofit2', name: 'retrofit', version: '2.9.0'
    implementation group: 'com.squareup.retrofit2', name: 'converter-gson', version: '2.9.0' // JSON을 직렬화
    implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.6' // 직렬화된 JSON을 객체로 역직렬화
    implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'  // String 처리시
    implementation 'com.squareup.okhttp3:okhttp:3.12.12'

    def lifecycle_version = "2.2.0"
    // ViewModel & LiveData
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
    implementation "androidx.recyclerview:recyclerview:1.2.0-alpha03"
    implementation platform('com.google.firebase:firebase-bom:28.4.2')
    implementation 'com.google.firebase:firebase-messaging'
    implementation 'com.google.firebase:firebase-analytics'
    //room DB
    def room_version = "2.3.0"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    //Debug
    debugImplementation 'com.amitshekhar.android:debug-db:1.0.6'
    //custom toggle
    implementation 'com.llollox:androidtoggleswitch:2.0.1'
    
# In App
- 시세
<img src="https://user-images.githubusercontent.com/63600525/143932234-970b7d93-8a1c-44c7-90e9-eec7e5ebbe35.png"  width="20%">

- 차트
<img src="https://user-images.githubusercontent.com/63600525/145673635-5d865c22-5fc7-499f-bef0-beee533d20b9.gif"  width="20%">

- 계산기
<img src="https://user-images.githubusercontent.com/63600525/143932255-4fb2fc54-1fa5-4284-8d59-c6693a819e14.png"  width="20%">

- 공지사항
<img src="https://user-images.githubusercontent.com/63600525/143933674-2abfd699-d00a-44d0-a6fa-4acc60a79256.png"  width="20%">

- 공지사항 상세
<img src="https://user-images.githubusercontent.com/63600525/143932266-2f3abf7b-638c-412b-9e45-563b7ead22b7.png"  width="20%">

- 환경설정
<img src="https://user-images.githubusercontent.com/63600525/143932290-db62cceb-e1a6-4732-9227-0e0a3cd791ed.png"  width="20%">

# Bugs
    - 성능이 낮은 기기에서는 시세탭에서 스크롤시 밀리는 현상.
    - 백그라운드 이동시 웹소켓 종료가 안되는점.






    
