package com.sjl.tx;

public interface UserService {
    void insert();
    void insertThenRollback() throws RollbackException;
    void invokeInsertThenRollback() throws RollbackException;
}
