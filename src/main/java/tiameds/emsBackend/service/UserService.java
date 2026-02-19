package tiameds.emsBackend.service;

import java.util.List;
import java.util.Random;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tiameds.emsBackend.dto.RequestDto;
import tiameds.emsBackend.dto.ResponseDto;
import tiameds.emsBackend.entity.User;
import tiameds.emsBackend.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

    @Transactional
    public ResponseDto registerUser(RequestDto request){
        ResponseDto res = new ResponseDto();

        System.out.println("=== REGISTER USER DEBUG ===");
        System.out.println("Request Email: " + request.getEmail());
        System.out.println("Request Username: " + request.getUsername());

        // DEBUG: Check database state before query
        long totalUsers = userRepo.count();
        System.out.println("Total users in DB (before query): " + totalUsers);

        // List all users in DB
        List<User> allUsers = userRepo.findAll();
        System.out.println("All users in DB: " + allUsers);

        User existingUser = this.userRepo.findByEmail(request.getEmail());
        System.out.println("Existing user from findByEmail: " + existingUser);

        if(existingUser != null) {
            System.out.println("DEBUG: Found existing user details:");
            System.out.println("  - ID: " + existingUser.getUser_id());
            System.out.println("  - Username: " + existingUser.getUsername());
            System.out.println("  - Email: " + existingUser.getEmail());
            System.out.println("  - OTP: " + existingUser.getOtp());
            System.out.println("  - Verified: " + existingUser.isVerified());

            res.setMessage("User already registered.");
        } else {
            System.out.println("DEBUG: No existing user found. Creating new user...");

            Random r = new Random();
            String otp = String.format("%06d", r.nextInt(100000));
            System.out.println("Generated OTP: " + otp);

            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setOtp(otp);
            newUser.setVerified(false);

            User savedUser = this.userRepo.save(newUser);
            System.out.println("Saved user with ID: " + savedUser.getUser_id());

            String subject = "Email Verification";
            String body = "Your verification OTP is " + savedUser.getOtp();

            // Email Send
            this.emailService.sendEmail(savedUser.getEmail(), subject, body);

            res.setUser_id(savedUser.getUser_id());
            res.setUsername(savedUser.getUsername());
            res.setEmail(savedUser.getEmail());
            res.setVerified(false);
            res.setMessage("OTP sent successfully!");

            // Verify save worked
            User verifySave = userRepo.findByEmail(savedUser.getEmail());
            System.out.println("Verification - User retrieved after save: " + verifySave);
        }

        System.out.println("=== END DEBUG ===");
        return res;
    }
}