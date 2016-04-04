-- Décrire PARAM
CREATE TABLE "param" (
    "id_param" INTEGER NOT NULL,
    "va_param" TEXT NOT NULL
)


-- Décrire SUIVI
CREATE TABLE suivi (
    "idsuivi" INTEGER,
    "idutilisateur" INTEGER,
    "idmotanglais" INTEGER,
    "connuGB" INTEGER,
    "connuF" INTEGER
)


-- Décrire TRADUCTION
CREATE TABLE traduction (
    "id" INTEGER,
    "anglais" TEXT,
    "francais" TEXT,
    "fichiermp3" TEXT,
    "dateAjout" DATE)


-- Décrire UTILISATEURS
CREATE TABLE utilisateurs (
    "id" INTEGER NOT NULL,
    "nom" TEXT NOT NULL,
    "mdp" TEXT,
    "dateinscription" TEXT,
    "dermotgb" INTEGER)
