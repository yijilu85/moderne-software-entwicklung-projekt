package com.medieninformatik.patientcare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class PatientcareApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PatientcareApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
