package com.seok.sec01.execution;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ProfessorInsert {
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
        System.out.println("[새 교수 입력]");
        System.out.print("교수 ID: ");
        String Pid = scanner.nextLine();
        System.out.print("주민번호: ");
        String jumin = scanner.nextLine();
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학과번호: ");
        int  id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("직급: ");
        String grade = scanner.nextLine();
        System.out.print("고용시기 (YYYY-MM-DD): ");
        String hireDateString = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date hireDate;
        try {
            hireDate = dateFormat.parse(hireDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
            return;
        }


        String sql = "INSERT INTO professor (professor_id, jumin, name, department_id, grade, hiredate) " +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try  {
            // 커넥션 객체로부터 PreparedStatement 객체를 얻어냄
            // PreparedStatement. PreparedStatement(실랭할 쿼리문) : 쿼리 실행을 위한 객체
            pstmt = conn.prepareStatement(sql);
            // 쿼리문의? 자리에 값을 채워넣음
            pstmt.setString(1, Pid);
            pstmt.setString(2, jumin);
            pstmt.setString(3, name);
            pstmt.setInt(4, id);
            pstmt.setString(5, grade);
            pstmt.setTimestamp(6, new java.sql.Timestamp(hireDate.getTime()));
            // 쿼리문의 파라미터인 ? 를 채운후 쿼리 실행
            pstmt.executeUpdate(); // 쿼리 실행 저장/수정/삭제는 executeUpdate() 메소드 사용
            System.out.println("교수가 성공적으로 등록되었습니다.");
        } catch (SQLException e) {
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
