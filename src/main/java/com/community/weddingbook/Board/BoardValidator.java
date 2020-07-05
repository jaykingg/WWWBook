package com.community.weddingbook.Board;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BoardValidator {
    Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,12}$");
    public boolean validBoard(String title, String content, String password) {
        if(title.length() > 20) {
            return false;
        }
        if(content.length() > 200) {
            return false;
        }
        if(password.length() > 12 || password.length() < 6) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        if(!matcher.find()) {
            return false;
        }
        return true;
    }
}
