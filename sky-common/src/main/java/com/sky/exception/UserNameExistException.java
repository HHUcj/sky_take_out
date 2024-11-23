package com.sky.exception;

/**
 * ClassName: UserNameExistException
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/14 - 下午3:57
 * @Version: v1.0
 */
public class UserNameExistException extends BaseException {
    public UserNameExistException() {
    }

    public UserNameExistException(String msg) {
        super(msg);
    }
}
