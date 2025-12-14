@ui @products
Feature: Tests UI - Gestion des produits React
  En tant qu'utilisateur connecté
  Je veux gérer mes produits via l'interface web React
  Pour maintenir mon inventaire à jour

  Background:
    Given a running backend
    And je suis connecté en tant que "alice@test.com"
    And je suis sur l'onglet Produits

  Scenario: Afficher la liste des produits existants
    Then je vois au moins 2 produit dans la liste

  Scenario: Ajouter un nouveau produit via l'interface
    When je clique sur le bouton Ajouter un produit
    And je remplis le formulaire produit avec:
      | id  | name              | price  | expirationDate |
      | 999 | MacBook Pro M3    | 2499.99| 2027-12-31     |
    And je clique sur Enregistrer
    Then le produit "MacBook Pro M3" apparaît dans la liste

  Scenario: Supprimer un produit de la liste
    When je supprime le premier produit
    Then je vois au moins 1 produit dans la liste