package com.seok.sec01.execution;



import com.seok.sec01.domain.Student;

import java.sql.*;
import java.util.List;

/**
     * 학생 목록을 조회하는 프로그램
     *
     * [데이터베이스 접속 순서]
     * 1. JDBC 드라이버 로딩 : Class.forName("oracle.jdbc.OracleDriver");
     * 2. 접속 정보 설정 : DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "school", "1234");
     * 3. SQL문 실행 : Connection 객체를 이용해서 PreparedStatement 객체 생성
     * 4. SQL문 실행 : PreparedStatement 객체를 이용해서 ResultSet 객체 생성
     * 5. 결과 처리 : ResultSet 객체를 이용해서 결과 처리
     * 6. 접속 종료 : Connection 객체를 이용해서 접속 종료
     * 7. 자원 해제 : ResultSet, PreparedStatement, Connection 객체를 이용해서 자원 해제
     * 8. 예외 처리 : try-catch-finally 블록을 이용해서 예외 처리
     *
     */
    public class StudentSelect {
        // 오라클 DB에 접속해서 하기 위한 정보
        public static void main(String[] args) {
            Connection conn = null;
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                conn = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:orcl", "school", "1234");
                System.out.println("DB 접속 성공");

                // 학생 목록 메소드 호출
                displayStudents(conn);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } // end of main

        private static void displayStudents(Connection conn) {
            System.out.println("등록된 학생 목록:");
            // 쿼리문 작성
            String sql = "SELECT s.student_id, s.name, s.year, s.address, s.department_id " +
                    "FROM student s " +
                    "ORDER BY s.student_id";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try  {
                // PreparedStatement : 쿼리 실핼을 위한 객체
                // 커넥션 객체로 부터 얻어낸 PreparedStatement 객체를 이용해서 쿼리 실행
                 pstmt = conn.prepareStatement(sql);
                // 쿼리 실행 그리고 그 결과를 ResultSet 객체에 담는다.
                 rs = pstmt.executeQuery(); // 조회할때
                // while 문을 이용해서 ResultSet 객체에 담긴 결과를 하나씩 꺼내서 출력
                while (rs.next()) {
                    String studentId = rs.getString("student_id");
                    String name = rs.getString("name");
                    int year = rs.getInt("year");
                    String address = rs.getString("address");
                    int departmentId = rs.getInt("department_id");
                    System.out.println(studentId + "\t" + name + "\t" + year + "\t"
                            + address + "\t" + departmentId);
                    // ResultSet 에 있는 행들을 하나씩 학생 객체
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 자원 해제 - 지원기 가장 늦게 만들어진 순서로 해제
                    // ResultSet 해제
                    if (rs != null) rs.close();
                    // PreparedStatement 해제
                    if(pstmt != null) pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-----------------------------------------------------------------------");
        } // end of displayStudents
    }   // end of class
