package com.sjl.one;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private int type;
}
