// lancement mongo : // mongod --dbpath ~/data/db
// connexion mongo : // mongosh
// use database_mongo_tp3_HAI913I
// db.products.insertOne({ id: 1, name: "Test Product", price: 10.99, expirationDate: "2025-01-01" })
// db.users.insertOne({ id: 1, name: "Test User", email: "test@example.com", password: "pass123", age: 25 })
package hai702.tp4;
public class Main {
    // Méthodes utilitaires pour une saisie fiable (evité le soucis d'affichage et de recolte de données)
    public static int lireEntier(java.util.Scanner sc, java.lang.String message) {
        while (true) {
            java.lang.System.out.print(message);
            java.lang.String ligne = sc.nextLine();
            try {
                return java.lang.Integer.parseInt(ligne);
            } catch (java.lang.NumberFormatException e) {
                java.lang.System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
            }
        } 
    }

    public static double lireDouble(java.util.Scanner sc, java.lang.String message) {
        while (true) {
            java.lang.System.out.print(message);
            java.lang.String ligne = sc.nextLine();
            try {
                return java.lang.Double.parseDouble(ligne);
            } catch (java.lang.NumberFormatException e) {
                java.lang.System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
            }
        } 
    }

    public static java.lang.String lireTexte(java.util.Scanner sc, java.lang.String message) {
        java.lang.System.out.print(message);
        return sc.nextLine();
    }

    public static void main(java.lang.String[] args) {
        java.util.Scanner sc = new java.util.Scanner(java.lang.System.in);
        hai702.tp4.service.ProductService productService = new hai702.tp4.service.ProductService();
        hai702.tp4.service.UserService userService = new hai702.tp4.service.UserService();
        // Authentification
        java.lang.System.out.println("=== BIENVENUE ===");
        java.lang.System.out.println("1. Se connecter");
        java.lang.System.out.println("2. S'inscrire");
        int choixAuth = hai702.tp4.Main.lireEntier(sc, "Votre choix : ");
        hai702.tp4.model.User utilisateurCourant = null;
        if (choixAuth == 2) {
            // Inscription
            int userId = hai702.tp4.Main.lireEntier(sc, "ID : ");
            java.lang.String userName = hai702.tp4.Main.lireTexte(sc, "Nom : ");
            int userAge = hai702.tp4.Main.lireEntier(sc, "Âge : ");
            java.lang.String userEmail = hai702.tp4.Main.lireTexte(sc, "Email : ");
            java.lang.String userPassword = hai702.tp4.Main.lireTexte(sc, "Mot de passe : ");
            utilisateurCourant = new hai702.tp4.model.User(userId, userName, userEmail, userPassword, userAge);
            userService.register(utilisateurCourant);
            java.lang.System.out.println("Inscription réussie !");
        } else if (choixAuth == 1) {
            // Connexion
            java.lang.String email = hai702.tp4.Main.lireTexte(sc, "Email : ");
            java.lang.String password = hai702.tp4.Main.lireTexte(sc, "Mot de passe : ");
            if (userService.login(email, password)) {
                java.lang.System.out.println("Connexion réussie !");
                utilisateurCourant = userService.findByEmail(email);
                if (utilisateurCourant != null) {
                    org.slf4j.MDC.put("userId", java.lang.String.valueOf(utilisateurCourant.getId()));
                    org.slf4j.MDC.put("userEmail", utilisateurCourant.getEmail());
                }
            } else {
                java.lang.System.out.println("Échec de la connexion !");
                sc.close();
                return;
            }
        } else {
            java.lang.System.out.println("Choix invalide !");
            sc.close();
            return;
        }
        // Menu principal des produits
        while (true) {
            java.lang.System.out.println("\n=== MENU DES PRODUITS ===");
            java.lang.System.out.println("1. Afficher tous les produits");
            java.lang.System.out.println("2. Rechercher un produit par ID");
            java.lang.System.out.println("3. Ajouter un produit");
            java.lang.System.out.println("4. Supprimer un produit");
            java.lang.System.out.println("5. Mettre à jour un produit");
            java.lang.System.out.println("0. Quitter");
            int choix = hai702.tp4.Main.lireEntier(sc, "Votre choix : ");
            try {
                switch (choix) {
                    case 1 :
                        java.lang.System.out.println("\n--- Tous les produits ---");
                        productService.findAll().forEach(java.lang.System.out::println);
                        break;
                    case 2 :
                        int searchId = hai702.tp4.Main.lireEntier(sc, "ID du produit : ");
                        hai702.tp4.model.Product p = productService.getProductById(searchId);
                        if (p != null) {
                            java.lang.System.out.println(p);
                        } else {
                            java.lang.System.out.println("Produit non trouvé !");
                        }
                        break;
                    case 3 :
                        java.lang.System.out.println("\n--- Ajouter un nouveau produit ---");
                        int id = hai702.tp4.Main.lireEntier(sc, "ID : ");
                        java.lang.String name = hai702.tp4.Main.lireTexte(sc, "Nom : ");
                        double price = hai702.tp4.Main.lireDouble(sc, "Prix : ");
                        java.lang.String exp = hai702.tp4.Main.lireTexte(sc, "Date d'expiration : ");
                        productService.addProduct(new hai702.tp4.model.Product(id, name, price, exp));
                        java.lang.System.out.println("Produit ajouté avec succès !");
                        break;
                    case 4 :
                        int deleteId = hai702.tp4.Main.lireEntier(sc, "ID du produit à supprimer : ");
                        productService.deleteProduct(deleteId);
                        java.lang.System.out.println("Produit supprimé avec succès !");
                        break;
                    case 5 :
                        java.lang.System.out.println("\n--- Mettre à jour un produit ---");
                        int uId = hai702.tp4.Main.lireEntier(sc, "ID du produit : ");
                        java.lang.String uName = hai702.tp4.Main.lireTexte(sc, "Nouveau nom : ");
                        double uPrice = hai702.tp4.Main.lireDouble(sc, "Nouveau prix : ");
                        java.lang.String uExp = hai702.tp4.Main.lireTexte(sc, "Nouvelle date d'expiration : ");
                        productService.updateProduct(new hai702.tp4.model.Product(uId, uName, uPrice, uExp));
                        java.lang.System.out.println("Produit mis à jour avec succès !");
                        break;
                    case 6 :
                        double threshold = hai702.tp4.Main.lireDouble(sc, "Prix minimum : ");
                        java.lang.System.out.println("\n--- Produits chers ---");
                        productService.findExpensiveProducts(threshold).forEach(java.lang.System.out::println);
                        break;
                    case 0 :
                        java.lang.System.out.println("Au revoir !");
                        sc.close();
                        java.lang.System.exit(0);
                        return;
                    default :
                        java.lang.System.out.println("Choix invalide ! Veuillez réessayer.");
                }
            } catch (java.lang.Exception e) {
                java.lang.System.out.println("Erreur : " + e.getMessage());
            }
        } 
    }
}