package com.seok.sec01.execution;

import com.seok.sec01.dao.DatebaseConnection;

import java.sql.*;
import java.util.Scanner;

/**
 * 학사 행정관리 시스템 메인 클래스
 * - 학생, 교수, 강의, 학과, 학사관리 등의 기능을 제공하는 프로그램
 */
public class SchoolManagementMain {
    // 정적 멤버 변수 선언
    private static final Connection conn = DatebaseConnection.getConnection();
    private  static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        while(true) {
            mainMenu();
        }

    }



    /**
     * 메인 메뉴 출력 메소드
     */
    private static void mainMenu(){
        System.out.println("=============================");
        System.out.println("학사 행정관리 시스템");
        System.out.println("=============================");
        System.out.println("1. 학생 관련 업무");
        System.out.println("2. 교수 관련 업무");
        System.out.println("3. 학과 관련 업무");
        System.out.println("4. 성적 관련 업무");
        System.out.println("5. 강의 관련 업무");
        System.out.println("6. 수업 관련 업무");
        System.out.println("7. 수강 신청 관련 업무");
        System.out.println("9. 종료");
        System.out.println("=============================");
        System.out.print("메뉴 선택: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                studentMenu();
                break;
            case 2:
                professorMenu();
                break;
            case 3:
                departmentMenu();
                break;
            case 4:
                takesMenu();
                break;
            case 5:
                courseMenu();
                break;
            case 6:
                classMenu();
                break;
            case 7:
                enrollmentMenu();
                break;
            case 9:
                System.out.println("프로그램을 종료합니다.");
                exit();
                return;
            default:
                System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
        }
    }
    /**
     * 학과 관련 메뉴
     */
    private static void departmentMenu() {
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 학과 등록");
            System.out.println("2. 학과 조회");
            System.out.println("3. 학과 정보 수정");
            System.out.println("4. 학과 정보 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerDepartment();
                    break;
                case 2:
                    displayDepartments();
                    break;
                case 3:
                    updateDepartment();
                    break;
                case 4:
                    deleteDepartment();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 학과 등록
     */
    private static void registerDepartment() {
        System.out.println("[학과 등록]");
        System.out.print("학과 코드: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("학과 이름: ");
        String name = scanner.nextLine();
        System.out.print("교실 위치: ");
        String office = scanner.nextLine();

        String sql = "INSERT INTO department (department_id, name, office) VALUES (?, ?, ?)";
        // try - with - resources 구문 사용해서 자원 자동 해제
        // try 구문이 끝나면 PreparedStatement 객체가 자동으로 close() 메소드 호출
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, office);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("학과가 성공적으로 등록되었습니다.");
            } else {
                System.out.println("학과 등록에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 학과 목록 조회
     */
    private static void displayDepartments() {
        System.out.println("[등록된 학과 목록 조회]");
        String sql = "SELECT department_id, name, office FROM department ORDER BY department_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("department_id");
                String name = rs.getString("name");
                String office = rs.getString("office");
                System.out.printf("%-10d %-20s %-10s\n", id, name, office);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 학과 정보 수정
     */
    private static void updateDepartment() {
        System.out.println("[학과 정보 수정]");
        System.out.print("수정할 학과의 코드를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("새 학과 이름: ");
        String name = scanner.nextLine();
        System.out.print("새 교실 위치: ");
        String office = scanner.nextLine();

        String sql = "UPDATE department SET name = ?, office = ? WHERE department_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, office);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("학과 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 코드의 학과를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 학과 정보 삭제
     */
    private static void deleteDepartment() {
        System.out.println("[학과 정보 삭제]");
        System.out.print("삭제할 학과의 코드를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM department WHERE department_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("학과 정보가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 코드의 학과를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 학생 메뉴
     * - 학생등록, 조회 , 수정, 삭제 기능을 제공한다
     */
    private static void studentMenu() {
        //
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 학생 등록");
            System.out.println("2. 학생 조회");
            System.out.println("3. 학생 정보 수정");
            System.out.println("4. 학생 정보 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerStudent();
                    break;
                case 2:
                    displayStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 학생 등록
     */
    private static void registerStudent() {
        // 키보드로부터 학생 정보 입력
        System.out.println("[새 학생 입력]");
        System.out.print("학생 ID: ");
        String studentId = scanner.nextLine();
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
        int departmentId = scanner.nextInt();
        scanner.nextLine();
        // 데이터베이스 저장 처리
        // PreparedStatement 객체 선언 :
        // SQL 문장을 미리 준비하여 실행항 수 있는 객체
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into student(student_id, jumin, name, year, " +
                    "address, department_id) values(?, ?, ?, ?, ?, ?)";
            // 매개변수화된 파라미터에 값 세팅
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, jumin);
            pstmt.setString(3, name);
            pstmt.setInt(4, year);
            pstmt.setString(5, address);
            pstmt.setInt(6, departmentId);
            // SQL 문장 실행
            int i = pstmt.executeUpdate();
            if(i > 0) {
                System.out.println("학생 정보가 등록되었습니다.");
            } else {
                System.out.println("학생 정보 등록 실패");
            }
        } catch (SQLException e) {
            if(e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("중복된 학생 ID가 존재합니다.");
            } else {
                e.printStackTrace();
            }
        } finally {
            close(pstmt);
        }
    }

    /**
     * 학생 조회
     * - 학생 테이블과 학과 테이블을 외부 조인하여 학생 정보를 조회한다.
     * - 학생ID, 이름, 학년 ,주소, 학과ID, 학과명을 출력한다.
     * - 오라클 데이터베이스는 대소문자를 가리지않는다. 하지만 키워드는 대문자로 쓰는게 좋다.
     */
    private static void displayStudents() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT s.student_id, s.jumin, s.name, s.year, s.address, " +
                    "s.department_id, d.name department_name, d.office " +
                    "from student s " +
                    "left outer join department d on s.department_id = d.department_id " +
                    "order BY s.student_id asc ";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(); // 단순 조회 쿼리는 executeQuery() 메소드 사용
            System.out.println("학생 목록:");
            System.out.println("학생ID\t이름\t학년\t주소\t학과ID\t학과명");
            System.out.println("====================================");
            while(rs.next()){
                String studentId = rs.getString("student_id");
                String name = rs.getString("name");
                int year = rs.getInt("year");
                String address = rs.getString("address");
                int departmentId = rs.getInt("department_id");
                String departmentName = rs.getString("department_name");
                String office = rs.getString("office");
                System.out.println(studentId + "\t" + name + "\t" + year + "\t" + address + "\t" + departmentId
                                   + "\t" + departmentName + "\t" + office);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt, rs);
        }
    } // end of display

    /**
     * 학생 정보 수정
     */
    private static void updateStudent() {
        System.out.println("수정할 학생의 ID를 입력하세요: ");
        String id = scanner.nextLine();
        // 사용자로부터 수정할 값 입력 받기
        System.out.print("새 주민번호: ");
        String jumin = scanner.nextLine();
        System.out.print("새 이름: ");
        String name = scanner.nextLine();
        System.out.print("새 학년: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리
        System.out.print("새 주소: ");
        String address = scanner.nextLine();
        System.out.print("새 학과 코드: ");
        int departmentId = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE student " +
                    "SET jumin = ?, " +
                    "    name = ?, " +
                    "    year = ?, " +
                    "    address = ?, " +
                    "    department_id = ? " +
                    "WHERE student_id = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jumin);
            pstmt.setString(2, name);
            pstmt.setInt(3, year);
            pstmt.setString(4, address);
            pstmt.setInt(5, departmentId);
            pstmt.setString(6, id);
            int i = pstmt.executeUpdate(); // 쿼리문 실행
            if (i > 0) {
                System.out.println("학생 정보가 수정 되었습니다");
            } else {
                System.out.println("학생 정보 수정 실패");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(pstmt); // close 메소드 호출하면서 PreparedStatement 객체 전달
        }
    }

    /**
     * 학생 정보 삭제
     */
    private static void deleteStudent( ) {
        System.out.println("삭제할 학생의 ID를 입력하세요: ");
        String id = scanner.nextLine();

        // 사용자로부터 삭제할 학생 ID 입력 받기
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM student " +
                    "WHERE student_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            int i = pstmt.executeUpdate();
            if (i > 0 ) {
                System.out.println("학생 정보가 삭제되었습니다");
            } else  {
                System.out.println("학생 정도 삭제 실패");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
            }
        }

    /**
     * 교수 관련 메뉴
     */
    private static void professorMenu() {
        //
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 교수 등록");
            System.out.println("2. 교수 조회");
            System.out.println("3. 교수 정보 수정");
            System.out.println("4. 교수 정보 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerProfessor();
                    break;
                case 2:
                    displayProfessors();
                    break;
                case 3:
                    updateProfessor();
                    break;
                case 4:
                    deleteProfessor();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 교수 등록
     */
    private static void registerProfessor() {
        System.out.println("[새 교수 등록]");
        System.out.print("교수 ID: ");
        String id = scanner.nextLine();
        System.out.print("주민번호: ");
        String jumin = scanner.nextLine();
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학과 코드: ");
        int department = scanner.nextInt();
        scanner.nextLine();
        System.out.print("직급: ");
        String grade = scanner.nextLine();
        System.out.print("채용 날짜(예: 2024-01-01): ");
        String hiredate = scanner.nextLine();

        String sql = "INSERT INTO professor(professor_id, jumin, name, department_id, grade, hiredate) " +
                "VALUES(?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";

        // try~with~resources 구문을 사용하면 close()를 명시적으로 호출하지 않아도 자동 close
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, jumin);
            pstmt.setString(3, name);
            pstmt.setInt(4, department);
            pstmt.setString(5, grade);
            pstmt.setString(6, hiredate);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("교수가 성공적으로 등록되었습니다.");
            } else {
                System.out.println("교수 등록에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 교수 목록 조회
     */
    private static void displayProfessors() {
        System.out.println("[등록된 교수 목록 조회]");
        String sql = "SELECT p.professor_id, p.name, p.grade, p.hiredate, d.name as department_name FROM professor p JOIN department d ON p.department_id = d.department_id ORDER BY p.professor_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("professor_id");
                String name = rs.getString("name");
                String grade = rs.getString("grade");
                Date hiredate = rs.getDate("hiredate");
                String departmentName = rs.getString("department_name");
                System.out.printf("%-10s %-15s %-10s %-20s %-15s\n", id, name, grade, hiredate.toString(), departmentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 교수 정보 수정
     */
    private static void updateProfessor() {
        System.out.println("[교수 정보 수정]");
        System.out.print("수정할 교수의 ID를 입력하세요: ");
        String id = scanner.nextLine();
        System.out.print("새 주민번호: ");
        String jumin = scanner.nextLine();
        System.out.print("새 이름: ");
        String name = scanner.nextLine();
        System.out.print("새 학과 코드: ");
        int department = scanner.nextInt();
        scanner.nextLine();
        System.out.print("새 직급: ");
        String grade = scanner.nextLine();
        System.out.print("새 채용 날짜(예: 2024-01-01): ");
        String hiredate = scanner.nextLine();

        String sql = "UPDATE professor SET jumin = ?, name = ?, department_id = ?, grade = ?, hiredate = TO_DATE(?, 'YYYY-MM-DD') WHERE professor_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jumin);
            pstmt.setString(2, name);
            pstmt.setInt(3, department);
            pstmt.setString(4, grade);
            pstmt.setString(5, hiredate);
            pstmt.setString(6, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("교수 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 ID의 교수를 찾을 수 없습니다.");
            }
        }  catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 교수 정보 삭제
     */
    private static void deleteProfessor() {
        System.out.println("[교수 정보 삭제]");
        System.out.print("삭제할 교수의 ID를 입력하세요: ");
        String id = scanner.nextLine();

        String sql = "DELETE FROM professor WHERE professor_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("교수 정보가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 ID의 교수를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 성적 관련 메뉴
     */
    private static void takesMenu() {
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 성적 등록");
            System.out.println("2. 성적 조회");
            System.out.println("3. 성적 수정");
            System.out.println("4. 성적 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerTakes();
                    break;
                case 2:
                    displayTakes();
                    break;
                case 3:
                    updateTakes();
                    break;
                case 4:
                    deleteTakes();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }
    /**
     * 강의 등록
     */
    private static void registerTakes() {
        System.out.println("[성적 등록]");
        System.out.print("학생 ID: ");
        String studentId = scanner.nextLine();
        System.out.print("수업 코드: ");
        String classId = scanner.nextLine();
        System.out.print("성적: ");
        String score = scanner.nextLine();

        String sql = "INSERT INTO takes(student_id, class_id, score) " +
                "VALUES(?, ?, ?)";

        try (PreparedStatement pstmt =conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, classId);
            pstmt.setString(3, score);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("성적이 성공적으로 등록되었습니다.");
            } else {
                System.out.println("성적 등록에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 성적 목록 조회
     */
    private static void displayTakes() {
        String sql = "SELECT t.student_id, s.name student_name, r.course_id, r.name course_name, t.class_id, " +
                "r.credit, c.year, c.semester, c.professor_id, p.name professor_name " +
                "FROM takes t " +
                "LEFT OUTER JOIN class c ON t.class_id = c.class_id " +
                "LEFT OUTER JOIN course r ON c.course_id = r.course_id " +
                "INNER JOIN student s ON t.student_id = s.student_id " +
                "INNER JOIN professor p ON c.professor_id = p.professor_id ";
        try(PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            System.out.println("[등록된 강의 목록 조회]");
            System.out.println("학생ID\t학생이름\t과목ID\t과목명\t수업ID\t학점\t년도\t학기\t교수ID\t교수이름");
            System.out.println("===================================================================");
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("student_name");
                String courseId = rs.getString("course_id");
                String takesName = rs.getString("course_name");
                String classId = rs.getString("class_id");
                int credit = rs.getInt("credit");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");
                String professorId = rs.getString("professor_id");
                String professorName = rs.getString("professor_name");
                System.out.println(studentId + "\t" + studentName +"\t" + courseId +"\t"+ takesName +"\t" + classId +"\t" + credit +"\t" +
                        year + "\t"+ semester +"\t" + professorId +"\t" + professorName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    /**
     * 성적 정보 수정
     */
    private static void updateTakes() {
        System.out.println("[성적 정보 수정]");
        System.out.print("학생 ID를 입력하세요: ");
        String studentId = scanner.nextLine();
        System.out.print("수업 ID: ");
        String classId = scanner.nextLine();
        System.out.print("새 성적: ");
        String newScore = scanner.nextLine();

        String sql = "UPDATE takes " +
                "SET score = ? " +
                "WHERE student_id = ? " +
                "AND class_id = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newScore);
            pstmt.setString(2, studentId);
            pstmt.setString(3, classId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("성적 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 학생 ID와 과목 코드의 성적을 찾을 수 없습니다.");
            }
        }  catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 수정에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 성적 정보 삭제
     */
    private static void deleteTakes() {
        System.out.println("[성적 정보 삭제]");
        System.out.print("삭제할 학생 ID를 입력하세요: ");
        String studentId = scanner.nextLine();
        System.out.print("삭제할 수업 ID를 입력하세요: ");
        String classId = scanner.nextLine();

        String sql = "DELETE FROM takes WHERE class_id = ? AND student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,  classId);
            pstmt.setString(2, studentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("성적 정보가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 학생 ID와 과목 코드의 성적을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 코스(강좌, 강의, 과목) 메뉴
     */
    private static void courseMenu() {
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 강의 등록");
            System.out.println("2. 강의 조회");
            System.out.println("3. 강의 정보 수정");
            System.out.println("4. 강의 정보 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerCourse();
                    break;
                case 2:
                    displayCourses();
                    break;
                case 3:
                    updateCourse();
                    break;
                case 4:
                    deleteCourse();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 강의 등록
     */
    private static void registerCourse() {
        System.out.println("[강의 등록]");
        System.out.print("강의 ID: ");
        String courseId = scanner.nextLine();
        System.out.print("강의 이름: ");
        String courseName = scanner.nextLine();
        System.out.print("크래딧: ");
        int credit = scanner.nextInt();
        scanner.nextLine();
        System.out.print("설명: ");
        String description = scanner.nextLine();

        String sql = "INSERT INTO course(course_id, name, credit, description) " +
                "VALUES(?, ?, ?, ?)";

        try (PreparedStatement pstmt =conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setString(2, courseName);
            pstmt.setInt(3, credit);
            pstmt.setString(4, description);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("강의가 성공적으로 등록되었습니다.");
            } else {
                System.out.println("강의 등록에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 강의 조회
     */
    private static void displayCourses() {
        String sql = "SELECT t.student_id, s.name student_name, r.course_id, r.name course_name, t.class_id, " +
                "r.credit, c.year, c.semester, c.professor_id, p.name professor_name " +
                "FROM takes t " +
                "LEFT OUTER JOIN class c ON t.class_id = c.class_id " +
                "LEFT OUTER JOIN course r ON c.course_id = r.course_id " +
                "INNER JOIN student s ON t.student_id = s.student_id " +
                "INNER JOIN professor p ON c.professor_id = p.professor_id ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("[등록된 강의 목록 조회]");
            System.out.println("학생ID\t학생이름\t과목ID\t과목명\t수업ID\t학점\t년도\t학기\t교수ID\t교수이름");
            System.out.println("===================================================================");
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("student_name");
                String courseId = rs.getString("course_id");
                String takesName = rs.getString("course_name");
                String classId = rs.getString("class_id");
                int credit = rs.getInt("credit");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");
                String professorId = rs.getString("professor_id");
                String professorName = rs.getString("professor_name");
                System.out.println(studentId + "\t" + studentName + "\t" + courseId +"\t"+ takesName +"\t" + classId + "\t" + credit + "\t" +
                        year + "\t" + semester + "\t" + professorId + "\t" + professorName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 강의 수정
     */
    private static void updateCourse() {
        System.out.println("[강의 수정]");
        System.out.print("수정할 강의 ID: ");
        String courseId = scanner.nextLine();
        System.out.print("수정할 강의 이름: ");
        String courseName = scanner.nextLine();
        System.out.print("수정할 크래딧: ");
        int credit = scanner.nextInt();
        scanner.nextLine();
        System.out.print("수정할 강의 설명: ");
        String description = scanner.nextLine();

    String sql = "UPDATE course SET name = ?, credit = ?, description = ? WHERE course_id = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setInt(2, credit);
            pstmt.setString(3,description);
            pstmt.setString(4, courseId);
            int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("성적 정보가 성공적으로 업데이트 되었습니다.");
        } else {
            System.out.println("해당 학생 ID와 과목 코드의 성적을 찾을 수 없습니다.");
        }
    }  catch (SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            System.out.println("무결성 제약 조건 위반으로 수정에 실패했습니다.");
        } else {
            e.printStackTrace();
        }
    }
}

    /**
     * 강의 삭제
     */
    private static void deleteCourse(){
        System.out.println("[강의 정보 삭제]");
        System.out.print("삭제할 강의 ID를 입력하세요: ");
        String courseId = scanner.nextLine();

        String sql = "DELETE FROM course WHERE course_id = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,  courseId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("성적 정보가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 학생 ID와 과목 코드의 성적을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 수업 관련 메뉴
     */
    private static void classMenu() {
        while (true) {
            System.out.println("=============================");
            System.out.println("1. 수업 등록");
            System.out.println("2. 수업 조회");
            System.out.println("3. 수업 정보 수정");
            System.out.println("4. 수업 정보 삭제");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerClass();
                    break;
                case 2:
                    displayClass();
                    break;
                case 3:
                    updateClass();
                    break;
                case 4:
                    deleteClass();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 수업 신청
     */
    private static void registerClass() {
        System.out.println("[수강신청 등록]");
        System.out.print("교실 ID: ");
        String classID = scanner.nextLine();
        System.out.print("강의 ID: ");
        String courseId = scanner.nextLine();
        System.out.print("년도: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("학기: ");
        String semester = scanner.nextLine();
        System.out.println("교수 ID: ");
        String professorID = scanner.nextLine();
        System.out.println("교실: ");
        String classroom = scanner.nextLine();
        System.out.println("인원수: ");
        int enroll = scanner.nextInt();
        scanner.nextLine();


        String sql = "INSERT INTO class(class_id, course_id, year, semester, professor_id, classroom, enroll) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt =conn.prepareStatement(sql)) {
            pstmt.setString(1, classID);
            pstmt.setString(2, courseId);
            pstmt.setInt(3, year);
            pstmt.setString(4, semester);
            pstmt.setString(5, professorID);
            pstmt.setString(6, classroom);
            pstmt.setInt(7, enroll);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("강의가 성공적으로 신청되었습니다.");
            } else {
                System.out.println("강의신체에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }


    /**
     * 수업 조회
     */
    private static void displayClass() {
        String sql = "SELECT t.student_id, s.name student_name, r.course_id, r.name course_name, t.class_id, " +
                "r.credit, c.year, c.semester, c.professor_id, p.name professor_name " +
                "FROM takes t " +
                "LEFT OUTER JOIN class c ON t.class_id = c.class_id " +
                "LEFT OUTER JOIN course r ON c.course_id = r.course_id " +
                "INNER JOIN student s ON t.student_id = s.student_id " +
                "INNER JOIN professor p ON c.professor_id = p.professor_id ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("[등록된 강의 목록 조회]");
            System.out.println("학생ID\t학생이름\t과목ID\t과목명\t수업ID\t학점\t년도\t학기\t교수ID\t교수이름");
            System.out.println("===================================================================");
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("student_name");
                String courseId = rs.getString("course_id");
                String takesName = rs.getString("course_name");
                String classId = rs.getString("class_id");
                int credit = rs.getInt("credit");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");
                String professorId = rs.getString("professor_id");
                String professorName = rs.getString("professor_name");
                System.out.println(studentId + "\t" + studentName + "\t" + courseId + "\t" + classId + "\t" + takesName + "\t"+ credit + "\t" +
                        year + "\t" + semester + "\t" + professorId + "\t" + professorName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateClass() {
        System.out.println("[수업 수정]");
        System.out.print("수정할 교실ID");
        String classID = scanner.nextLine();
        System.out.print("수정할 강의 ID: ");
        String courseId = scanner.nextLine();
        System.out.print("수정할 년도: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("수정할 학기: ");
        String semester = scanner.nextLine();
        System.out.print("수정할 교수ID: ");
        String professorID = scanner.nextLine();
        System.out.print("수정할 교실: ");
        String classroom = scanner.nextLine();
        System.out.print("수정할 인원수: ");
        int enroll = scanner.nextInt();
        scanner.nextLine();
        String sql = "UPDATE class SET course_id = ?, year = ?, semester = ?, professor_id = ?, classroom = ?, enroll = ? " +
                " WHERE class_id = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setInt(2, year);
            pstmt.setString(3, semester);
            pstmt.setString(4, professorID);
            pstmt.setString(5, classroom);
            pstmt.setInt(6, enroll);
            pstmt.setString(7, classID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("성적 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("해당 학생 ID와 과목 코드의 성적을 찾을 수 없습니다.");
            }
        } catch(SQLException e){
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println("무결성 제약 조건 위반으로 수정에 실패했습니다.");
                } else {
                    e.printStackTrace();
                }
        }
    }
    /**
     * 수업 삭제
     */
    private static void deleteClass() {
        System.out.println("[강의 정보 삭제]");
        System.out.print("삭제할 수업 ID를 입력하세요: ");
        String courseId = scanner.nextLine();

        String sql = "DELETE FROM class WHERE class_id = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,  courseId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("수업 정보가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 수업 ID와 과목 코드의 성적을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 수강 신청 관련 메뉴
     */
    private static void enrollmentMenu() {

        while (true) {
            System.out.println("=============================");
            System.out.println("1. 수강 신청");
            System.out.println("2. 수강 조회");
            System.out.println("3. 수강 신청 상태 변경");
            System.out.println("4. 수강 취소 등록");
            System.out.println("5. 메인 메뉴로 가기");
            System.out.println("=============================");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1:
                    registerEnrollment();
                    break;
                case 2:
                    //displayEnrollment();
                    break;
                case 3:
                    //changeEnrollStatus();   // 수강 신청 상태 변경
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    /**
     * 수강 등록
     */
    public static void registerEnrollment() {
        System.out.println("수업 ID: ");
        String classId = scanner.nextLine();

        System.out.println("학생 ID: ");
        String studentId = scanner.nextLine();

        System.out.println("수업 학기  예) 1, 2, 여름, 겨울 : ");
        String semester = scanner.nextLine();

        int enrollId = getMaxEnrollmentId(); // 수강신청 최대 번호 + 1 해서 새로운 번호 자동 생성
        System.out.println("수강신청번호 : " + enrollId);

        String sql = "insert into enrollment (enroll_id, student_id, class_id) values (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollId);
            pstmt.setString(2, studentId);
            pstmt.setString(3, classId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("수강 신청이 성공적으로 완료되었습니다.");
            } else {
                System.out.println("수강 신청에 실패했습니다.");
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("무결성 제약 조건 위반으로 등록에 실패했습니다.");
            } else {
                e.printStackTrace();
            }
        }
    }
    /**
     * 최대 Class ID 조회 + 1
     */
    private static int getMaxEnrollmentId() {
        int enrollId = 1; // 기본 1로 세팅
        String sql = "select max(enroll_id) as max_id from enrollment";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                enrollId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 만약에 최대 값이 없다면 기본값 1 리턴
        return enrollId;
    }






    public static void close(PreparedStatement pstmt) {
        try {
            if(pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(PreparedStatement pstmt, ResultSet rs) {
        close(pstmt);
        close(rs);
    }



    /**
     * 프로그램 종료
     * - 데이터베이스 연결을 닫고 프로그램을 종료한다
     */
    public static void exit() {
        if(conn != null) {
            try {
                conn.close(); // 커넥션 객체가 사용중이면 닫기
            } catch (SQLException e) {
            }
        }
        if (scanner != null) {
            scanner.close(); // Scanner 객체가 사용중이면 닫기
        }
        System.out.println("** 프로그램 종료**");
        System.exit(0); // 프로그램을 완전히 종료한다.
    }



}

