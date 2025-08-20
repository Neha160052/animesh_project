package com.ttn.e_commerce_project.helper;

import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.impl.VerificationTokenServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SetCustomerActiveHelper {


   VerificationTokenServiceImpl verificationTokenService;
   UserRepository userRepository;

   public ResponseEntity<String> activateCustomer(String token)
   {
       return verificationTokenService.validateToken(token)
               .map(verificationToken -> {
                   User user = verificationToken.getUser();
                   user.setActive(true);
                   userRepository.save(user);
                   return ResponseEntity.ok("Account activated Successfully");
               }).orElse(ResponseEntity.badRequest().body("Invalid or expired token"));
   }
}
