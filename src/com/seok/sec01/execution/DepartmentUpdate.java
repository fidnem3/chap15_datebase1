package com.seok.sec01.execution;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 학생 데이터를 수정하는 클래스
 */
public class ProfessorUpdate {
    // 오라클 DB에 접속해서 하기 위한 정보
    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:orcl", "school", "1234");

            System.out.println("DB 접속 성공");

            updateStudent(conn, scanner);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } // end of main

    private static void updateStudent(Connection conn, Scanner scanner) {
        System.out.println("수정할 학생의 ID를 입력하세요: ");
        String id = scanner.nextLine();

        // SQL 쿼리문 작성
        String sql = "UPDATE professor SET jumin = ?, name = ?, department_id = ?, grade = ?, hiredate = ? WHERE professor_id = ?";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            // 사용자로부터 입력 받기
            System.out.print("새 주민번호: ");
            String jumin = scanner.nextLine();
            System.out.print("새 이름: ");
            String name = scanner.nextLine();
            System.out.print("새 학과번호: ");
            int departmentId = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리
            System.out.print("새 직급: ");
            String grade = scanner.nextLine();
            System.out.print("고용시기 (YYYY-MM-DD): ");
            String hiredateString = scanner.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date hiredate;
            try {
                hiredate = dateFormat.parse(hiredateString);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("올바른 날짜 형식을 입력하세요 (yyyy-MM-dd).");
                return;
            }


            // PreparedStatement에 파라미터 설정
            pstmt.setString(1, jumin);
            pstmt.setString(2, name);
            pstmt.setInt(3, departmentId);
            pstmt.setString(4, grade);
            pstmt.setTimestamp(5, new java.sql.Timestamp(hiredate.getTime()));
            pstmt.setString(6, id);

            // SQL 실행
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("학생 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 ID의 학생을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
