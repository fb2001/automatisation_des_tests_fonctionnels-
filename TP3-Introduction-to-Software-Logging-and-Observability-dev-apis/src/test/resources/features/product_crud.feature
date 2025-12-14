Feature: Gestion des produits

  Background:
    Given a running backend
    And un utilisateur admin existe avec l'email "alice@test.com" et le mot de passe "admin123"

  Scenario: Lister tous les produits
    When je demande la liste des produits en tant que "1"
    Then je reçois au moins 1 produit

  Scenario: Ajouter un produit avec un ID unique
    When j'ajoute un produit:
      | id | name           | price | expirationDate |
      | 10 | Clavier AZERTY | 19.99 | 2026-12-31     |
    Then le produit avec l'id "10" existe

  Scenario: Supprimer un produit inexistant renvoie une erreur
    When je supprime le produit avec l'id "9999"
    Then une erreur ProductNotFoundException est levée