package com.seok.sec01.execution;
import java.sql.*;
import java.util.Scanner;
    /**
     * 학생 데이터를 등록하는 클래스
     */
    public class StudentInsert {
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
            System.out.println("[새 학생 입력]");
            System.out.print("학생 ID: ");
            String id = scanner.nextLine();
            System.out.print("주민번호: ");
            String jumin = scanner.nextLine();
            System.out.print("이름: ");
            String name = scanner.nextLine();
            System.out.print("학년: ");
            int year = scanner.nextInt();
            scanner.nextLine();
            System.out.print("주소: ");
            String address = scanner.nextLine();
            System.out.print("학과 코드: ");
            int department = scanner.nextInt();
            scanner.nextLine();

            String sql = "INSERT INTO student (student_id, jumin, name, year, address, department_id) " +
                    " VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = null;
            try  {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, id);
                pstmt.setString(2, jumin);
                pstmt.setString(3, name);
                pstmt.setInt(4, year);
                pstmt.setString(5, address);
                pstmt.setInt(6, department);
                pstmt.executeUpdate();
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


