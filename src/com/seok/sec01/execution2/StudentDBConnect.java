package com.seok.sec01.execution2;



import com.seok.sec01.dao.DatebaseConnection; // 커넥션 전담 클래스

import java.sql.Connection;

    /**
     * 데이터베이스 접속 테스트 클래스
     * 1. JDBC 드라이버 로딩 : Class.forName("oracle.jdbc.OracleDriver");
     * 2. 접속 정보 설정 : DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "school", "1234"); *
     */
    public class StudentDBConnect {
        public static void main(String[] args) {
            // Connection 객체 생성
            Connection conn = null;
            conn = DatebaseConnection.getConnection(); // 커넥션 전담 객체에서 커넥션 얻기
            System.out.println("conn : " + conn); // 커넥션 객체 참조 주소 출력
            if(conn != null) {
                System.out.println("DB 접속 성공");
            } else {
                System.out.println("DB 접속 실패");
            }

        }
    }



