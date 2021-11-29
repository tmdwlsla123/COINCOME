# Description
국내 4대거래소 및 해외거래소 프리미엄 차이, 수익/손실 계산기, 거래소 공지 확인 어플
# Development Environment
- Android Studio 2020.3.1 Patch 2

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
    
    
