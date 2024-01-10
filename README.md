# projet coding week 2024 - equipe 23 - made_by_chatgpt.exe
Pour exécuter le programme

```bash
./gradlew run
```

Pour exécuter le .jar situé à la racine du projet:
```bash
java --module-path ${JAVAFX_HOME}/lib/  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web -jar DirectDealing.jar
```

En ce deuxième jour, nous avons ajouté au projet les fonctionnalités suivantes: sauvegarde des informations (comptes, offres et demandes). Cette sauvegarde nous permet désormais de nous connecter avec un compte déjà créé à l'aide du Prénom + Nom et du mot de passe.
L'écran principal présente les différents filtres (pas encore implémentés) mais présente déjà toutes les offres créées. Il est possible de créer une nouvelle offre, qui apparaitra par la suite sur l'écran principal. Il est également possible de cliquer sur une offre afin d'en voir le détail, puis par la suite de contacter le créateur de l'offre/demande, ce qui sera réalisé demain.