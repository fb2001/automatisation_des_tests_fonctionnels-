@ui @smoke
Feature: Tests UI simplifiés

  Scenario: Connexion basique
    Given a running backend
    And je suis sur la page de connexion
    When je remplis le formulaire de connexion avec l'email "alice@test.com" et le mot de passe "admin123"
    And je clique sur le bouton Se connecter
    Then je suis connecté et je vois mon nom "Alice" dans la navbar