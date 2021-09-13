Pour résoudre ce problème j'ai choisi de créer un service s'appelant *"BankService"* qui porterait l'ensemble des us1, us2, et us3.


L'architecture de cette solution s'inspire d'un pattern d'architecture en couche. Il est tout de même sans dépendance et approximé.


Cependant, j'ai énormément profité du libre champ que m'inspirait l'énoncé. En conséquence, j'ai utilisé de la généricité, comme je le fais habituellement, notamment sur la généricité des tests ou les i18ns. Et même si celle-ci amène trop d'abstraction, elle peut être aisément réduite en enlevant les mécanismes d'implicites ou de subtype et en dupliquant le code.


Je suis parti bien vite dans la rédaction du code. J'aurais pu tout simplement utiliser scala 3 (ou dotty), avec le temp ou le recul, que j'affectionne beaucoup comme version de scala. D'ailleurs, j'ai fait beaucoup de tests et pour les même raisons certains tests, comme ceux de fail, auraient également pu être rédigés.


Pour traduire au mieux les besoins, j'ai choisi de comprendre les us1, us2 et us3 comme des spécifications à traduire en tests bdd. Il sont enrichis dans le corps des scenarii (voir `application.BankServiceSpec` pour plus de détails).

Pour lancer les tests, il suffit normalement de lancer "sbt test" dans le répertoire racine avec:
- sbt en version 1.4.9
- scala en 2.13.6
- un jdk 1.8.0_292

J'ai utilisé Intellij et un template sbt pour créer ce projet rapidement. La version d'Intellij est Intellij IDEA 2019.1.3 . Il se peut qu'un compilateur d'IDE, même autre qu'intellij, ne comprend pas toujours toutes les syntaxes existantes en scala. C'est pourquoi je choisis de faire foi au compilateur et runner scala suscité qui est le premier repère pour un code fonctionnel.

Si vous faites un `sbt clean test` en racine du projet, vous obtiendrez normalement ceci:
```
[info] welcome to sbt 1.4.9 (Private Build Java 1.8.0_292)
[info] loading global plugins from /home/benjamin/.sbt/1.0/plugins
[info] loading project definition from /home/benjamin/workspace/kata_societe_generale_new/project
[info] loading settings for project root from build.sbt ...
[info] set current project to kata_societe_generale (in build file:/home/benjamin/workspace/kata_societe_generale_new/)
[info] compiling 2 Scala sources to /home/benjamin/workspace/kata_societe_generale_new/target/scala-2.13/classes ...
[info] AccountTest:
[info] Feature: Account life cycle
[info]   Scenario: A new account is empty of operation and Money
[info]     Given A brand new account of a client 
[info]     Then It has no operations 
[info]     Then Neither has it balance 
[info] UnitedStateBankServiceSpec:
[info] Feature: Spec "US1"
[info]   In order to save money 
[info]   As a bank client 
[info]   I want to make a deposit in my account 
[info] - Scenario: I have got few money to save to my new account
[info]   + Given a blank account of mine with no saving on it 
[info]   + When I deposit few money 
[info]   + Then account should have a balance of this few money AND
[info]  account should have a last operation of this few money as a credited amount AND
[info]  account should have a last operation with same balance as amount as this few money AND
[info]  account should have just one operation 
[info] - Scenario: I have got a lot of amount to save to my once used account
[info]   + Given a once used account of mine with few saving on it 
[info]   + When I deposit a lot of amount 
[info]   + Then account should have a balance added by a lot of money AND
[info]  account should have a last operation of a lot of money as credited amount AND
[info]  account should have a just two operations 
[info] Feature: Spec "US2"
[info]   In order to retrieve some or all of my savings 
[info]   As a bank client 
[info]   I want to make a withdrawal from my account 
[info] - Scenario: I retrieve few of my savings
[info]   + Given a filled account of mine with some saving on it 
[info]   + When I deposit few money 
[info]   + Then account has a balance of previous balance minus few money AND
[info] account has a last operation of minus few money as credited amount AND
[info]  account has a one more operation 
[info] Feature: Spec "US3"
[info]   In order to check my operations 
[info]   As a bank client 
[info]   I want to see the history (operation, date, amount, balance) of my operations 
[info] - Scenario: I check a history of 4 operations
[info]   + Given a filled account of mine with some operations on it 
[info]   + When I check history 
[info]   + 
[info] operation n°		date                        		amount		balance
[info] 4		Mon Sep 13 17:46:25 CEST 2021		-100 €		4459 €
[info] 3		Mon Sep 13 17:46:25 CEST 2021		-46 €		4559 €
[info] 2		Mon Sep 13 17:46:25 CEST 2021		4535 €		4605 €
[info] 1		Mon Sep 13 17:46:25 CEST 2021		70 €		70 €
[info]  
[info]   + Then account has the same number of lines as its operations with one for headers 
[info] FrenchBankServiceSpec:
[info] Feature: Spec "US1"
[info]   In order to save money 
[info]   As a bank client 
[info]   I want to make a deposit in my account 
[info] - Scenario: I have got few money to save to my new account
[info]   + Given a blank account of mine with no saving on it 
[info]   + When I deposit few money 
[info]   + Then account should have a balance of this few money AND
[info]  account should have a last operation of this few money as a credited amount AND
[info]  account should have a last operation with same balance as amount as this few money AND
[info]  account should have just one operation 
[info] - Scenario: I have got a lot of amount to save to my once used account
[info]   + Given a once used account of mine with few saving on it 
[info]   + When I deposit a lot of amount 
[info]   + Then account should have a balance added by a lot of money AND
[info]  account should have a last operation of a lot of money as credited amount AND
[info]  account should have a just two operations 
[info] Feature: Spec "US2"
[info]   In order to retrieve some or all of my savings 
[info]   As a bank client 
[info]   I want to make a withdrawal from my account 
[info] - Scenario: I retrieve few of my savings
[info]   + Given a filled account of mine with some saving on it 
[info]   + When I deposit few money 
[info]   + Then account has a balance of previous balance minus few money AND
[info] account has a last operation of minus few money as credited amount AND
[info]  account has a one more operation 
[info] Feature: Spec "US3"
[info]   In order to check my operations 
[info]   As a bank client 
[info]   I want to see the history (operation, date, amount, balance) of my operations 
[info] - Scenario: I check a history of 4 operations
[info]   + Given a filled account of mine with some operations on it 
[info]   + When I check history 
[info]   + 
[info] opération n°		date                        		montant		solde
[info] 4		Année 2021 le 13/09 17:46:25		-100 €		4459 €
[info] 3		Année 2021 le 13/09 17:46:25		-46 €		4559 €
[info] 2		Année 2021 le 13/09 17:46:25		4535 €		4605 €
[info] 1		Année 2021 le 13/09 17:46:25		70 €		70 €
[info]  
[info]   + Then account has the same number of lines as its operations with one for headers 
[info] Run completed in 1 second, 243 milliseconds.
[info] Total number of tests run: 9
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 9, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 6 s, completed Sep 13, 2021 5:46:26 PM
```

