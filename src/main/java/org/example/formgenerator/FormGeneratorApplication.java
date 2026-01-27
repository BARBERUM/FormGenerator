package org.example.formgenerator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.formgenerator.mapper")
public class FormGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormGeneratorApplication.class, args);
    }

}
