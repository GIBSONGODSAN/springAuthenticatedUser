// package com.dtcc.intern.demo.service;

// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.dtcc.intern.demo.model.UserAccount;
// import com.dtcc.intern.demo.repository.UserAccountRepository;
// import com.dtcc.intern.demo.reqres.LoginUserDto;
// import com.dtcc.intern.demo.reqres.RegisterUserDto;

// @Service
// public class AuthenticationService {
//     private final UserAccountRepository userRepository;
    
//     private final PasswordEncoder passwordEncoder;
    
//     private final AuthenticationManager authenticationManager;

//     public AuthenticationService(
//         UserAccountRepository userRepository,
//         AuthenticationManager authenticationManager,
//         PasswordEncoder passwordEncoder
//     ) {
//         this.authenticationManager = authenticationManager;
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     public UserAccount signup(RegisterUserDto input) {
//         UserAccount user = new UserAccount();
//         user.setEmail(input.getEmail());
//         user.setFullName(input.getFullName());
//         user.setPassword(passwordEncoder.encode(input.getPassword()));

//         return userRepository.save(user);
//     }

//     public UserAccount authenticate(LoginUserDto input) {
//         authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                         input.getEmail(),
//                         input.getPassword()
//                 )
//         );

//         return userRepository.findByEmail(input.getEmail())
//                 .orElseThrow();
//     }
// }
package com.dtcc.intern.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dtcc.intern.demo.model.UserAccount;
import com.dtcc.intern.demo.repository.UserAccountRepository;
import com.dtcc.intern.demo.reqres.LoginUserDto;
import com.dtcc.intern.demo.reqres.RegisterUserDto;

@Service
public class AuthenticationService {
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserAccountRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount signup(RegisterUserDto input) {
        UserAccount user = new UserAccount();
        user.setEmail(input.getEmail());
        user.setFullName(input.getFullName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public UserAccount authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
