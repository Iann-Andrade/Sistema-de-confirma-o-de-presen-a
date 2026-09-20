package com.Lista_de_Presenca.Fisioterapia.dto;

public class LoginResponse {

    private Integer id;
    private String nome;
    private String email;
    private String role;
    private String token;

    public LoginResponse(Integer id, String nome, String email, String role, String token) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
