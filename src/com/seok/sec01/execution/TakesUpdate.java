package com.seok.sec01.execution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 학생 데이터를 수정하는 클래스
 */
public class TakesUpdate {
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
        System.out.print("수정할 목록의 ID를 입력하세요: ");
        String id = scanner.nextLine();
        System.out.print("수정할 목록의 과목코드를 입력하세요: ");
        String subject = scanner.nextLine();

        // SQL 쿼리문 작성
        String sql = "UPDATE takes SET subject = ?, score = ? WHERE subject = ? and student_id = ?";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            // 사용자로부터 입력 받기
            System.out.print("새 과목코드: ");
            String subjects = scanner.nextLine();
            System.out.print("새 과목점수: ");
            String score = scanner.nextLine();


            // PreparedStatement에 파라미터 설정
            pstmt.setString(1, subjects);
            pstmt.setString(2, score);
            pstmt.setString(3, subject);
            pstmt.setString(4, id);

            // SQL 실행
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("목록 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 ID의 목록을 찾을 수 없습니다.");
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
