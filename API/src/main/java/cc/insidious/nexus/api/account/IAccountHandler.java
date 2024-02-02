package cc.insidious.nexus.api.account;

import java.util.List;

public interface IAccountHandler {

    Account getFirstInQueue();
    List<Account> getAccounts();
    
    void load();
    void unload();
}
