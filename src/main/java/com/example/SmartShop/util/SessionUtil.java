package com.example.SmartShop.util;

import com.example.SmartShop.Entity.User;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    // sauvgarder l'utilisateur dans la session
    public static void setUser(HttpSession session, User user){
        session.setAttribute(USER_SESSION_KEY,user);
    }


}
