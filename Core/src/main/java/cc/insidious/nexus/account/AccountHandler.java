package cc.insidious.nexus.account;

import cc.insidious.nexus.NexusApplication;
import lombok.Getter;

import java.io.FileReader;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Scanner;

@Getter
public class AccountHandler {

    private final NexusApplication instance;
    private final PriorityQueue<Account> accounts;

    public AccountHandler(NexusApplication instance) {
        this.instance = instance;
        this.accounts = new PriorityQueue<>();
        this.load();
    }

    public Optional<Account> getNextAccount() {
        return this.accounts.isEmpty() ? Optional.empty() : Optional.of(this.accounts.poll());
    }

    public void load() {
    }
}
