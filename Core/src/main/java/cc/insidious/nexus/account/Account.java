package cc.insidious.nexus.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class Account {

    private final String email;
    private final String password;
    private final String migrationEmail;
    private final String migrationPassword;

}
