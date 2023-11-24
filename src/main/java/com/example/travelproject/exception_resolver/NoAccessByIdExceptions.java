package com.example.travelproject.exception_resolver;


public class NoAccessByIdExceptions extends RuntimeException{
    private Long id;

    private String login;

    public NoAccessByIdExceptions(Long id, String login){
        this.id = id;
        this.login = login;
    }
    @Override
    public String toString() {
        return "NoAccessExceptions{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
