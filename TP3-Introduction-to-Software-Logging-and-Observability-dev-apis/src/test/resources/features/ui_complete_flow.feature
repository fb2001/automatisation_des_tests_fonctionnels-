@ui @e2e
Feature: Tests UI - Flux complet utilisateur
  En tant que nouvel utilisateur
  Je veux m'inscrire, me connecter et gérer des produits
  Pour utiliser l'application de bout en bout

  Background:
    Given a running backend

  Scenario: Parcours complet d'un nouvel utilisateur
    Given je suis sur la page de connexion
    And je suis en mode inscription
    When je remplis le formulaire d'inscription avec:
      | id  | name           | age | email              | password |
      | 100 | Jean Dupont    | 35  | jean@test.com      | pass123  |
    And je clique sur le bouton S'inscrire
    Then un message de succès s'affiche
    And je clique sur le bouton Se connecter
    Then je suis connecté et je vois mon nom "Jean Dupont" dans la navbar
    When je suis sur l'onglet Produits
    Then je vois au moins 2 produit dans la liste
    When je clique sur le bouton Ajouter un produit
    And je remplis le formulaire produit avec:
      | id  | name           | price | expirationDate |
      | 101 | iPhone 15 Pro  | 1299  | 2026-12-31     |
    And je clique sur Enregistrer
    Then le produit "iPhone 15 Pro" apparaît dans la liste
    When je clique sur Déconnexion
    Then je suis redirigé vers la page de connexion