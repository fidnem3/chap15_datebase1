package com.seok.sec01.execution;



import java.sql.*;

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
    public class ProfessorSelect {
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
            System.out.println("등록된 교수 목록:");
            // 쿼리문 작성
            String sql = "SELECT p.professor_id, p.jumin, p.name, p.department_id, p.grade, p.hiredate " +
                    "FROM professor p " +
                    "ORDER BY p.professor_id ";
            try  {
                // PreparedStatement : 쿼리 실핼을 위한 객체
                // 커넥션 객체로 부터 얻어낸 PreparedStatement 객체를 이용해서 쿼리 실행
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // 쿼리 실행 그리고 그 결과를 ResultSet 객체에 담는다.
                ResultSet rs = pstmt.executeQuery(); // 조회할때
                // while 문을 이용해서 ResultSet 객체에 담긴 결과를 하나씩 꺼내서 출력
                while (rs.next()) {
                    String professorId = rs.getString("professor_id");
                    String jumin = rs.getString("jumin");
                    String name = rs.getString("name");
                    int departmentId = rs.getInt("department_id");
                    String grade = rs.getString("grade");
                    Date hiredate = rs.getDate("hiredate");
                    System.out.println(professorId + "\t" + jumin + "\t" + name + "\t" + departmentId + "\t"
                            + grade + "\t" + hiredate);
                    // ResultSet 에 있는 행들을 하나씩 학생 객체
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("-----------------------------------------------------------------------");
        } // end of displayStudents
    }   // end of class
