package br.com.letscode.user.exception;

public class UnauthorizedRequest extends Exception {
    public UnauthorizedRequest(String msg){
        super(msg);
    }
}
