//package hai702.tp4;
//
//import hai702.tp4.model.Product;
//import hai702.tp4.model.User;
//import hai702.tp4.service.ProductService;
//import hai702.tp4.service.UserService;
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static int lireEntier(Scanner sc, String message) {
//        while (true) {
//            System.out.print(message);
//            String ligne = sc.nextLine();
//            try {
//                return Integer.parseInt(ligne);
//            } catch (NumberFormatException e) {
//                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
//            }
//        }
//    }
//
//    public static double lireDouble(Scanner sc, String message) {
//        while (true) {
//            System.out.print(message);
//            String ligne = sc.nextLine();
//            try {
//                return Double.parseDouble(ligne);
//            } catch (NumberFormatException e) {
//                System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
//            }
//        }
//    }
//
//    public static String lireTexte(Scanner sc, String message) {
//        System.out.print(message);
//        return sc.nextLine();
//    }
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        ProductService productService = new ProductService();
//        UserService userService = new UserService();
//
//        System.out.println("=== BIENVENUE ===");
//        System.out.println("1. Se connecter");
//        System.out.println("2. S'inscrire");
//        int choixAuth = lireEntier(sc, "Votre choix : ");
//
//        User utilisateurCourant = null;
//
//        if (choixAuth == 2) {
//            int userId = lireEntier(sc, "ID : ");
//            String userName = lireTexte(sc, "Nom : ");
//            int userAge = lireEntier(sc, "Âge : ");
//            String userEmail = lireTexte(sc, "Email : ");
//            String userPassword = lireTexte(sc, "Mot de passe : ");
//
//            utilisateurCourant = new User(userId, userName, userEmail, userPassword, userAge);
//            userService.register(utilisateurCourant);
//
//            //DÉFINIR L'USERID DANS LE SERVICE
//            productService.setCurrentUserId(utilisateurCourant.getId());
//
//            System.out.println("Inscription réussie !");
//        } else if (choixAuth == 1) {
//            String email = lireTexte(sc, "Email : ");
//            String password = lireTexte(sc, "Mot de passe : ");
//
//            if (userService.login(email, password)) {
//                System.out.println("Connexion réussie !");
//                utilisateurCourant = userService.findByEmail(email);
//
//                if (utilisateurCourant != null) {
//                    // DÉFINIR L'USERID DANS LE SERVICE
//                    productService.setCurrentUserId(utilisateurCourant.getId());
//                }
//            } else {
//                System.out.println("Échec de la connexion !");
//                sc.close();
//                return;
//            }
//        } else {
//            System.out.println("Choix invalide !");
//            sc.close();
//            return;
//        }
//
//        while (true) {
//            System.out.println("\n=== MENU DES PRODUITS ===");
//            System.out.println("1. Afficher tous les produits");
//            System.out.println("2. Rechercher un produit par ID");
//            System.out.println("3. Ajouter un produit");
//            System.out.println("4. Supprimer un produit");
//            System.out.println("5. Mettre à jour un produit");
//            System.out.println("6. Rechercher produits chers");
//            System.out.println("0. Quitter");
//            int choix = lireEntier(sc, "Votre choix : ");
//
//            try {
//                switch (choix) {
//                    case 1:
//                        System.out.println("\n--- Tous les produits ---");
//                        productService.findAll().forEach(System.out::println);
//                        break;
//
//                    case 2:
//                        int searchId = lireEntier(sc, "ID du produit : ");
//                        Product p = productService.getProductById(searchId);
//                        System.out.println(p);
//                        break;
//
//                    case 3:
//                        System.out.println("\n--- Ajouter un nouveau produit ---");
//                        int id = lireEntier(sc, "ID : ");
//                        String name = lireTexte(sc, "Nom : ");
//                        double price = lireDouble(sc, "Prix : ");
//                        String exp = lireTexte(sc, "Date d'expiration : ");
//                        productService.addProduct(new Product(id, name, price, exp));
//                        System.out.println("Produit ajouté avec succès !");
//                        break;
//
//                    case 4:
//                        int deleteId = lireEntier(sc, "ID du produit à supprimer : ");
//                        productService.deleteProduct(deleteId);
//                        System.out.println("Produit supprimé avec succès !");
//                        break;
//
//                    case 5:
//                        System.out.println("\n--- Mettre à jour un produit ---");
//                        int uId = lireEntier(sc, "ID du produit : ");
//                        String uName = lireTexte(sc, "Nouveau nom : ");
//                        double uPrice = lireDouble(sc, "Nouveau prix : ");
//                        String uExp = lireTexte(sc, "Nouvelle date d'expiration : ");
//                        productService.updateProduct(new Product(uId, uName, uPrice, uExp));
//                        System.out.println("Produit mis à jour avec succès !");
//                        break;
//
//                    case 6:
//                        double threshold = lireDouble(sc, "Prix minimum : ");
//                        System.out.println("\n--- Produits chers ---");
//                        productService.findExpensiveProducts(threshold).forEach(System.out::println);
//                        break;
//
//                    case 0:
//                        System.out.println("Au revoir !");
//                        sc.close();
//                        System.exit(0);
//                        return;
//
//                    default:
//                        System.out.println("Choix invalide !");
//                }
//            } catch (Exception e) {
//                System.out.println("Erreur : " + e.getMessage());
//            }
//        }
//    }
//}
//
//// lancement mongo : //  brew services start mongodb-community@7.0 && mongod --dbpath ~/data/db
//// connexion mongo : // mongosh
////use database_mongo_tp3_HAI913I
////db.products.insertOne({ id: 1, name: "Test Product", price: 10.99, expirationDate: "2025-01-01" })
////db.users.insertOne({ id: 1, name: "Test User", email: "test@example.com", password: "pass123", age: 25 })
