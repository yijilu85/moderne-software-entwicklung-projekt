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

# 2. Vorteile von Versionskontrolle
## a. Kollaboration

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

## b. Rollback

Als Rollback (vom englischen „roll back“ für „zurückrollen“ oder „zurückdrehen“) bezeichnet man in EDV-Systemen das „Zurücksetzen“ der einzelnen Verarbeitungsschritte einer Transaktion.
Das System wird dadurch vollständig auf den Zustand vor dem Beginn der Transaktion zurückgeführt.
Ein Rollback wird typischerweise im Fehlerfall angestoßen, falls beispielsweise ein Verarbeitungsschritt in der betreffenden Transaktion nicht korrekt durchgeführt werden kann.
Im normalen Ablauf (ohne Fehlersituation) werden mit einem „Commit“ die Änderungen der Transaktion permanent gemacht.
## c. History
Eine History bietet die Möglichkeit den Überblick über diverse Versionsstände zu behalten und ihm Bedarfsfall zu vorherigen Versionen
zurück zukehren, wenn es z.B. im Fehlerfall zu Problemen gekommen ist. Siehe hierzu auch unter dem Punkt b. Rollback.
Zusätzlich ist auch ersichtlich durch wen eine Änderung herbeigeführt wurde, so dass im Fehlerfall auch eine schnellere Kommunikation
stattfinden kann.

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