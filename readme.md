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
