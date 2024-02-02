package cc.insidious.nexus.api.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class Account {

    private final String email;
    private final String password;

}
