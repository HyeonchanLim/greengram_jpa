package com.green.greengram;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {
    // 파라미터 datetime 으로 넘어오는 값이 db 에 저장된 datetime 값
    public static void assertCurrentTimestamp(String dateTime){
        // 자바에서 현재일시 데이터
        LocalDateTime expectedNow = LocalDateTime.now(); // 2024-12-30T11:32:23
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
        LocalDateTime actualNow = LocalDateTime.parse(dateTime);
        assertTrue(Duration.between(expectedNow,actualNow).getSeconds() <=1);
    }
}
