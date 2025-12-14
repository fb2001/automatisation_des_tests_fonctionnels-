@ui @auth
Feature: Tests UI - Authentification React
  En tant qu'utilisateur
  Je veux m'authentifier via l'interface web React/Material-UI
  Pour accéder à mes produits

  Background:
    Given a running backend

  Scenario: Connexion réussie avec des identifiants valides
    Given je suis sur la page de connexion
    When je remplis le formulaire de connexion avec l'email "alice@test.com" et le mot de passe "admin123"
    And je clique sur le bouton Se connecter
    Then je suis connecté et je vois mon nom "Alice Admin" dans la navbar

  Scenario: Connexion échouée avec un mauvais mot de passe
    Given je suis sur la page de connexion
    When je remplis le formulaire de connexion avec l'email "alice@test.com" et le mot de passe "wrongpassword"
    And je clique sur le bouton Se connecter
    Then un message d'erreur s'affiche

  Scenario: Inscription d'un nouvel utilisateur
    Given je suis sur la page de connexion
    And je suis en mode inscription
    When je remplis le formulaire d'inscription avec:
      | id | name          | age | email                | password |
      | 99 | Test Selenium | 28  | selenium@test.com    | test1234 |
    And je clique sur le bouton S'inscrire
    Then un message de succès s'affiche

  Scenario: Déconnexion d'un utilisateur
    Given je suis connecté en tant que "alice@test.com"
    When je clique sur Déconnexion
    Then je suis redirigé vers la page de connexion