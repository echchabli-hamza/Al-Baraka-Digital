# 🏦 Al Baraka Digital V2 – Secure & Smart Banking Platform

## 📖 Description
Al Baraka Digital est une plateforme bancaire sécurisée développée avec **Spring Boot** pour digitaliser la gestion des opérations bancaires : **dépôts, retraits et virements**.

La solution combine **sécurité, automatisation, intelligence artificielle et déploiement continu (CI/CD)** afin de réduire les traitements manuels et améliorer la fiabilité du système.

---

## 🚀 Fonctionnalités

### 👤 Client
- Création de compte
- Authentification JWT
- Dépôt / Retrait / Virement
- Upload de justificatifs
- Suivi des opérations

### 🏦 Agent bancaire
- Consultation des opérations PENDING
- Validation ou rejet manuel
- Vérification des documents

### 🤖 IA (Spring AI)
- Analyse automatique des justificatifs
- Recommandation intelligente :
  - APPROVE
  - REJECT
  - NEED_HUMAN_REVIEW

### ⚙️ Admin
- Gestion des utilisateurs
- Gestion des rôles et statuts

---

## 🔐 Sécurité
- JWT stateless authentication
- OAuth2 Resource Server (Keycloak / Okta)
- Autorisation par rôles (CLIENT, AGENT, ADMIN)
- BCrypt password encoder
- Endpoints protégés par scopes

---

## 🛠️ Stack technique
- Java 17+
- Spring Boot 3
- Spring Security 6
- Spring AI
- Thymeleaf
- PostgreSQL
- Docker & Docker Compose
- GitHub Actions (CI/CD)

---

## 📂 Modèle de données
- User
- Account
- Operation
- Document

---

## 🔁 Workflow des opérations

### Montant ≤ 10 000 DH
➡️ Validation automatique

### Montant > 10 000 DH
1. Upload justificatif
2. Analyse par Spring AI
3. Décision automatique ou validation humaine

