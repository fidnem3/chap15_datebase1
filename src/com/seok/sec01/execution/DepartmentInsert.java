package com.seok.sec01.execution;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

/**
 * 학생 데이터를 등록하는 클래스
 */
public class DepartmentInsert {
    // 오라클 DB에 접속해서 하기 위한 정보
    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:orcl", "school", "1234");
            System.out.println("DB 접속 성공");

            registerStudent(conn, scanner);

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

    private static void registerStudent(Connection conn, Scanner scanner) {
        System.out.println("[새 학과 입력]");
        System.out.print("학과 ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("학과 이름: ");
        String name = scanner.nextLine();
        System.out.print("학과 교실: ");
        String office = scanner.nextLine();

        String sql = "INSERT INTO department (department_id, name, office) " +
                " VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        try  {
            // 커넥션 객체로부터 PreparedStatement 객체를 얻어냄
            // PreparedStatement. PreparedStatement(실랭할 쿼리문) : 쿼리 실행을 위한 객체
            pstmt = conn.prepareStatement(sql);
            // 쿼리문의? 자리에 값을 채워넣음
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, office);
            // 쿼리문의 파라미터인 ? 를 채운후 쿼리 실행
            pstmt.executeUpdate(); // 쿼리 실행 저장/수정/삭제는 executeUpdate() 메소드 사용
            System.out.println("학생이 성공적으로 등록되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            scanner.close();
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


