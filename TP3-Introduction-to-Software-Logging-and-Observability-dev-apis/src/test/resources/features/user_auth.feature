Feature: Authentification et enregistrement utilisateur

  Background:
    Given a running backend

  Scenario: S'enregistrer avec un email et mot de passe valides
    Given l'utilisateur n'existe pas avec l'email "newuser@test.com"
    When je m'enregistre avec:
      | name   | email              | password | age |
      | Julien | newuser@test.com   | pwd1234  | 25  |
    Then l'enregistrement reussit et l'utilisateur existe avec l'email "newuser@test.com"

  Scenario: Se connecter avec des identifiants valides
    Given un utilisateur existe avec l'email "alice@test.com" et le mot de passe "admin123"
    When je me connecte avec l'email "alice@test.com" et le mot de passe "admin123"
    Then la connexion est reussie
