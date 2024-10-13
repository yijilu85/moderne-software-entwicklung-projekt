# Inhalt

- [Versionskontrollsysteme](#versionskontrollsysteme)
- [Grundlagen Git Commands](#grundlagen-git-commands)
- [Vorteile von Versionskontrolle](#vorteile-von-versionskontrolle)
- [Best Practices im Workflow](#best-practices-im-workflow)
- [Git Integration in IntelliJ - Remote](#git-integration-in-intellij---remote)
- [Git Integration in IntelliJ - Lokal](#git-integration-in-intellij---lokal)

# Versionskontrollsysteme
## Geschichte und Entwicklung
- erste Ansaetze 1962 in IEBUPDTE von IBM fuer OS/360
    * erstes speziell dafuer entwickeltes Tool: [Source Code Control System](https://pubs.opengroup.org/onlinepubs/9699919799/utilities/sccs.html) (SCCS)
        - ebenfalls fuer OS/360
        - entwickelt von Bell Labs ab 1972
        - damals noch als System zur Versionskontrolle auf EINEM Rechner
    * ab 1982 [RCS](https://www.gnu.org/software/rcs/) als Subsystem von UNIX
        - is heute aktiv gepflegt vom GNU Project
        - erhielt 1986 mit [CVS](https://cvs.nongnu.org/) ein Frontend um ganz Projekte und nicht nur einzelne Dateien zu verwalten
    * ab 2000 [Apache Subversion](https://subversion.apache.org/)
        - lange Zeit weit verbreitet fuer Projekte weit
            * FreeBSD
            * SourceForge
            * GCC 
    * ebenfalls ab 2000 Distributed VCS
        - [BitKeeper](https://www.bitkeeper.org/), anfangs genutzt fuer die Entwicklung des Linux-Kernels
        - nach Lizenrueckzug vom Betreiber begann Linus Torvalds mit der Entwicklung von Git 
    * [Git](https://git-scm.com/) als DVCS ab 2005
        - ist heute de-facto-Standard fuer Versionskontrolle in der Software-Entwicklung
        - und am weitesten verbreitete Software fuer DVCS
        - Anbieter fuer Repos unter anderem
            * [Gitlab](https://about.gitlab.com/) von GitLab Inc.
            * [Github](https://github.com/) seit 2018 Teil von Microsoft
            * [BitBucket](https://bitbucket.org/) von Atlassian
            * [SourceForge](https://sourceforge.net/) von SlashDotMedia

# Grundlagen Git Commands

- $ `git init` Erstellung eines Git Repository/Projects
- $ `git clone` /..link../ Herunterladen bzw. Klonen eines Repositorys
- $ `git add` /..file../ Fügt Dateien dem Staging Bereich hinzu
- $ `git commit -m` "message" Hinzufügen/Speicherung der Änderungen mit einer Nachricht (-m "..")
- $ `git push` Hochladen auf das Repository/Branch
- $ `git pull` Herunterladen vom Repositorys/Branch
- $ `git rm` /..file../ löscht Dateien
- $ `git branch` /..Branch Name../ Erstellung eines Branches
- $ `git checkout` Branch wechseln
- $ `git checkout -b` /..Name../ Neuen Branch erstellen und dorthin wechseln
- $ `git branch -d` /..Name../ Bestimmten Branch löschen
- $ `git merge` /..Branch name../ Fügt Änderungen eines anderen Branches in den Aktuellen ein
- $ `git help add` Öffnet die Dokumentation im Browser
- $ `git status` Zeigt an, welche Dateien modizifiert wurden und welchen Status die besitzen
- $ `git log` Zeigt den Verlauf an und noch mehr Infos wie z.B. Autoren
- $ `git branch` Liste der verfügbaren Branches

# Vorteile von Versionskontrolle
## Kollaboration

Kollaboration oder Zusammenarbeit ist das gemeinsame Bemühen von mehreren Einzelpersonen oder Arbeitsgruppen, um eine Aufgabe zu bewältigen oder ein Projekt auszuführen.
Hierbei können die Projektteams sowohl räumlich als auch organisatorisch getrennt sein.
Zentraler Workflow:
In kleinen Projektgruppen wie beispielsweise einer Arbeitsgruppe wird häufig ein einfacher zentralisierter Workflow bei der Versionsverwaltung genutzt.
Im Mittelpunkt steht dabei ein zentrales Repository, auf dem alle Teammitglieder gleichberechtigt und direkt pushen dürfen.
    Vorteile:
        Einfachstes denkbares Modell
        Ein gemeinsames Repo
        Alle haben Schreibzugriff auf ein gemeinsames Repo
    Nachteile:
        Definition und Umsetzung von Rollen mit bestimmten Rechten ("Manager", "Entwickler", "Gast-Entwickler", ...) schwierig bis unmöglich (das ist kein Git-Thema, sondern hängt von der Unterstützung durch den Anbieter des Servers ab)
        Jeder darf überall pushen: Enge und direkte Abstimmung nötig
        Modell funktioniert meist nur in sehr kleinen Teams (2..3 Personen)
Einfacher verteilter Workflow mit Git:
In großen und/oder öffentlichen Projekten wird üblicherweise ein Workflow eingesetzt, der auf den Möglichkeiten von verteilten Git-Repositories basiert.
Dabei wird zwischen verschiedenen Rollen ("Integrationsmanager", "Entwickler") unterschieden.
Sie finden dieses Vorgehen beispielsweise beim Linux-Kernel und auch häufig bei Projekten auf Github.
    	Es existiert ein geschütztes ("blessed") Master-Repo
        Stellt die Referenz für das Projekt dar
        Push-Zugriff nur für ausgewählte Personen ("Integrationsmanager")
    Entwickler
        Forken das Master-Repo auf dem Server und klonen ihren Fork lokal
        Arbeiten auf lokalem Klon: Unabhängige Entwicklung eines Features
        Pushen ihren Stand in ihren Fork (ihr eigenes öffentliches Repo): Veröffentlichung des Beitrags zum Projekt (sobald fertig bzw. diskutierbar)
        Lösen Pull- bzw. Merge-Request gegen das Master-Repo aus: Beitrag soll geprüft und ins Projekt aufgenommen werden (Merge ins Master-Repo durch den Integrationsmanager)

    Integrationsmanager
        Prüft die Änderungen im Pull- bzw. Merge-Request und fordert ggf. Nacharbeiten an bzw. lehnt Integration ab (technische oder politische Gründe)
        Führt Merge der Entwickler-Zweige mit den Hauptzweigen durch Akzeptieren der Pull- bzw. Merge-Requests durch: Beitrag der Entwickler ist im Projekt angekommen und ist beim nächsten Pull in deren lokalen Repos vorhanden
Vorausgesetzt wird ein bereits existierendes Repo.
Informationen zu Branches:
Branches ermöglichen dir das Entwickeln von Features, Beheben von Fehlern und sichere Experimentieren mit neuen Ideen in einem Bereich deines Repositorys.
Du erstellst einen Branch immer aus einem existierenden Branch. Normalerweise würdest du einen neuen Branch aus dem Standardbranch deines Repositorys erstellen.
Da kannst dann in diesem Branch unabhängig von Änderungen arbeiten, die andere Personen im Repository machen.
Ein Branch, den Du zur Erstellung einer Funktion aufbaust, wird häufig als Funktions-Branch oder Themen-Branch bezeichnet

## Rollback

Als Rollback (vom englischen „roll back“ für „zurückrollen“ oder „zurückdrehen“) bezeichnet man in EDV-Systemen das „Zurücksetzen“ der einzelnen Verarbeitungsschritte einer Transaktion.
Das System wird dadurch vollständig auf den Zustand vor dem Beginn der Transaktion zurückgeführt.
Ein Rollback wird typischerweise im Fehlerfall angestoßen, falls beispielsweise ein Verarbeitungsschritt in der betreffenden Transaktion nicht korrekt durchgeführt werden kann.
Im normalen Ablauf (ohne Fehlersituation) werden mit einem „Commit“ die Änderungen der Transaktion permanent gemacht.
## History
Eine History bietet die Möglichkeit den Überblick über diverse Versionsstände zu behalten und ihm Bedarfsfall zu vorherigen Versionen
zurück zukehren, wenn es z.B. im Fehlerfall zu Problemen gekommen ist. Siehe hierzu auch unter dem Punkt b. Rollback.
Zusätzlich ist auch ersichtlich durch wen eine Änderung herbeigeführt wurde, so dass im Fehlerfall auch eine schnellere Kommunikation
stattfinden kann.

# Best Practices im Workflow

## Verwenden von Branches

Jeder Entwickler sollte für neue Features oder Bugfixes einen eigenen Branch erstellen. Dies verhindert Konflikte oder
Fehler im Main-Branch. Es gibt Namenskonventionen für Branches, wie z.B. `feature/branch1`, `bugfix/branch2`
, `hotfix/branch2`.

## Regelmäßiges Committen

Kleine und häufige Commits sind leichter nachvollziehbar und bieten den Vorteil, dass ein Rollback einfacher
durchzuführen ist, wenn einmal Fehler auftreten. Commit-Beschreibungen sollten präzise und informativ sein, um den Zweck
der Änderungen verständlich zu machen.

## Code Reviews und Pull Requests

PRs sind der beste Weg, um Code in den Mainbranch zu integrieren. So können andere Teammitglieder den Code zu
überprüfen (Code Review) und Feedback geben. Bevor ein Pull Request gemerged wird, sollte der PR von mindestens einem
anderen Entwickler durchgeführt und approved werden. Dies verbessert die Code-Qualität und sorgt dafür, dass sich andere
Entwickler auch mit dem Code der Anderen beschäftigen.

## CI/CD-Integration

Mit Continuous Integration (CI) sollte sichergestellt werden, dass alle Commits automatisch getestet werden. Tools wie
Travis CI, GitHub Actions oder Jenkins lassen sich direkt in GitHub integrieren. Continuous Delivery (CD) sorgt dann
dafür, dass neue Versionen automatisch deployed werden, sobald sie den Hauptbranch erreichen und alle Tests bestanden
haben.

## Sicherheitsaspekte beachten

Sensible Daten wie API-Schlüssel, Passwörter oder geheime Schlüssel sollten niemals im Repository gespeichert werden.
Stattdessen sollten Umgebungsvariablen verwendet werden.

## Git Ignore verwenden

Diese sensiblen Daten sollten in einer gitignore-Datei hinterlegt sein, damit sie nicht versehentlich in das Repository
hochgeladen werden. Temporäre Dateien können so auch exkludiert werden.

# Git Integration in IntelliJ - Remote

1. Öffne IntelliJ und klicke auf "New Project"
2. Wähle "Project from Version Control"
3. Link des Remote Repositorys im vorgesehen Feld eingeben
4. IntelliJ klont das Projekt selber und öffnet es automatisch
   ![img_1.png](img_1.png)

Will man dennoch mit der Console arbeiten, werden die oben genannten Befehle und die Konsole benötigt.

1. IntelliJ Projekt erstellen und das Terminal öffnen. (Unten Links)
2. Befehl "git clone /Link/" das Repository klonen
3. Befehl "git checkout -b /Branch name/" Einen gewünschten Branch erstellen und dorthin wechseln
4. Nun kann die Projektbearbeitung beginnen.
5. mit "git status" kann zur Sicherheit überprüft werden, ob man sich der User im richtigen Branch befindet


# Git Integration in IntelliJ - Lokal

1. Neues Projekt in IntelliJ erstellen
2. Terminal öffnen
3. Befehl "cd /Verzeichnis/", um in das gewünschte Verzeichnis zu wechseln
4. Befehl "git init", um ein Lokales Repository anzulegen/initialisieren
5. Falls nötig mit ".gitignore" eine .gitignore Datei hinzufügen

# Contributions:

Git Repository Setup: Yi-Ji
Versionskontrollsysteme: Mario
Grundlagen Git Commands: Ruwim 
Vorteile von Versionskontrolle: Stephan
Best Practices im Workflow: Yi-Ji
Git Integration in IntelliJ - Remote: Ruwim
Git Integration in IntelliJ - Lokal: Ruwim