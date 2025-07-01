package cn.pcs.studentclubmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.pcs.studentclubmanagement.mapper")
public class StudentClubManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentClubManagementApplication.class, args);
    }

}
