package pki.backend.com.example.PKI.Service;

import com.sun.security.auth.module.NTSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.nio.file.attribute.UserPrincipal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PkiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkiServiceApplication.class, args);
//		try {
//			// Dobijanje NTSystem objekta
//			NTSystem ntSystem = new NTSystem();
//
//			// Dobijanje korisničkog imena trenutnog korisnika
//			String userName = ntSystem.getName();
//
//			// Ispisivanje korisničkog imena
//			System.out.println("Trenutni korisnik: " + userName);
//		} catch (Exception e) {
//			System.err.println("Došlo je do greške prilikom dobijanja korisničkog imena: " + e.getMessage());
//		}
	}
}
