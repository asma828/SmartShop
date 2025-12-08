# SmartShop - Application de Gestion Commerciale

Application de gestion commerciale développée pour MicroTech Maroc avec Spring Boot.

## 📋 Fonctionnalités

- **Gestion des Clients** : Création, modification, suppression de clients
- **Gestion des Produits** : CRUD complet avec gestion de stock
- **Gestion des Commandes** : Création, confirmation, annulation
- **Système de Paiements** : Espèces, Chèques, Virements
- **Programme de Fidélité** : 4 niveaux avec remises automatiques (BASIC, SILVER, GOLD, PLATINUM)
- **Codes Promo** : Application automatique de remises promotionnelles
- **Authentification** : Système de login avec sessions (ADMIN/CLIENT)

## 🛠️ Technologies

- **Java 11**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **MapStruct**
- **Maven**

## 🚀 Installation

### Prérequis
- Java 11+
- MySQL 8.0+
- Maven 3.6+

### Configuration Base de Données

Créer une base de données MySQL :
```sql
CREATE DATABASE smartshop;
```

Configurer `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartshop
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### Lancer l'Application
```bash
# Avec Maven Wrapper
./mvnw spring-boot:run

# Ou avec Maven
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`


##  API Endpoints

### Authentification
```http
POST   /api/auth/login      # Connexion
POST   /api/auth/logout     # Déconnexion
GET    /api/auth/me         # Utilisateur actuel
```

### Clients
```http
POST   /api/clients         # Créer un client
GET    /api/clients         # Liste des clients
GET    /api/clients/{id}    # Détails d'un client
PUT    /api/clients/{id}    # Modifier un client
DELETE /api/clients/{id}    # Supprimer un client
```

### Produits
```http
POST   /api/products        # Créer un produit
GET    /api/products        # Liste des produits
GET    /api/products/{id}   # Détails d'un produit
PUT    /api/products/{id}   # Modifier un produit
DELETE /api/products/{id}   # Supprimer un produit
```

### Commandes
```http
POST   /api/Orders                    # Créer une commande
GET    /api/Orders                    # Liste des commandes
GET    /api/Orders/{id}               # Détails d'une commande
GET    /api/Orders/client/{clientId}  # Commandes d'un client
GET    /api/Orders/status/{status}    # Commandes par statut
PUT    /api/Orders/{id}/confirm       # Confirmer une commande
PUT    /api/Orders/{id}/cancl         # Annuler une commande
```

### Paiements
```http
POST   /api/payements                 # Ajouter un paiement
GET    /api/payements/{id}            # Détails d'un paiement
GET    /api/payements/order/{orderId} # Paiements d'une commande
PUT    /api/payements/{id}/encaisser  # Encaisser un paiement
PUT    /api/payements/{id}/reject     # Rejeter un paiement
```

## 📦 Structure du Projet
```
src/main/java/com/example/SmartShop/
├── Entity/              # Entités JPA
├── repository/          # Repositories
├── service/             # Services (interfaces)
├── service/impl/        # Implémentations services
├── controller/          # Controllers REST
├── dto/                 # DTOs (request/response)
├── mapper/              # Mappers Entity ↔ DTO
├── enums/               # Énumérations
├── exception/           # Gestion des exceptions
├── config/              # Configuration
└── util/                # Utilitaires
```