# 📊 DisplayScoreboard

Un mod Fabric léger pour **Minecraft 1.21.1** qui transforme vos objectifs de scoreboard classiques en magnifiques **hologrammes dynamiques**.

## ✨ Fonctionnalités

* **Hologrammes Automatiques** : Affiche le Top 5 de n'importe quel objectif.
* **Interaction Totale** : Les hologrammes utilisent des `Markers`, vous pouvez donc poser ou casser des blocs directement à travers eux.
* **Podium Coloré** : Couleurs distinctes pour le Top 3 (Or, Argent, Bronze).
* **Gestion Facile** : Commandes intuitives avec suggestions automatiques.
* **Persistance** : Les positions et objectifs sont sauvegardés dans un fichier JSON.

## 🛠️ Commandes

Toutes les commandes requièrent le niveau de permission 2 (OP).

| Commande | Description |
| :--- | :--- |
| `/displayscoreboard add <obj> <titre> <pos>` | Crée un hologramme à la position indiquée. |
| `/displayscoreboard remove <nom>` | Supprime un hologramme et nettoie les entités. |
| `/displayscoreboard list` | Affiche la liste des hologrammes actifs et leur statut. |
| `/displayscoreboard modify <nom> <prop> <val>` | Modifie le titre ou la position d'un hologramme. |
| `/displayscoreboard reload` | Recharge la configuration depuis le disque. |

## 🚀 Installation

1. Assurez-vous d'avoir **Fabric API** installé.
2. Placez le JAR dans votre dossier `mods`.
3. Créez vos objectifs en jeu (ex: `/scoreboard objectives add deaths deathCount`).
4. Affichez-les : `/displayscoreboard add deaths "§lTableau des Morts" ~ ~ ~`.

## 💻 Développement

* **Version** : 1.21.1
* **Mappings** : Yarn
* **API** : Fabric API
