## Inhalt

- [Einleitung - was ist Git](#einleitung---was-ist-git)
- [Vorteile von Versionskontrolle](#vorteile-von-versionskontrolle)
- [Grundlagen Git Commands](#grundlagen-git-commands)
- [Best Practices im Workflow](#best-practices-im-workflow)
- [Git Integration in IDEs](#git-integration-in-ides)

## Einleitung - was ist Git

## Vorteile von Versionskontrolle

## Grundlagen Git Commands

## Best Practices im Workflow

### Verwenden von Branches

Jeder Entwickler sollte für neue Features oder Bugfixes einen eigenen Branch erstellen. Dies verhindert Konflikte oder
Fehler im Main-Branch. Es gibt Namenskonventionen für Branches, wie z.B. `feature/branch1`, `bugfix/branch2`
, `hotfix/branch2`.

### Regelmäßiges Committen

Kleine und häufige Commits sind leichter nachvollziehbar und bieten den Vorteil, dass ein Rollback einfacher
durchzuführen ist, wenn einmal Fehler auftreten. Commit-Beschreibungen sollten präzise und informativ sein, um den Zweck
der Änderungen verständlich zu machen.

### Code Reviews und Pull Requests

PRs sind der beste Weg, um Code in den Mainbranch zu integrieren. So können andere Teammitglieder den Code zu
überprüfen (Code Review) und Feedback geben. Bevor ein Pull Request gemerged wird, sollte der PR von mindestens einem
anderen Entwickler durchgeführt und approved werden. Dies verbessert die Code-Qualität und sorgt dafür, dass sich andere
Entwickler auch mit dem Code der Anderen beschäftigen.

### CI/CD-Integration

Mit Continuous Integration (CI) sollte sichergestellt werden, dass alle Commits automatisch getestet werden. Tools wie
Travis CI, GitHub Actions oder Jenkins lassen sich direkt in GitHub integrieren. Continuous Delivery (CD) sorgt dann
dafür, dass neue Versionen automatisch deployed werden, sobald sie den Hauptbranch erreichen und alle Tests bestanden
haben.

### Sicherheitsaspekte beachten

Sensible Daten wie API-Schlüssel, Passwörter oder geheime Schlüssel sollten niemals im Repository gespeichert werden.
Stattdessen sollten Umgebungsvariablen verwendet werden.

### Git Ignore verwenden

Diese sensiblen Daten sollten in einer gitignore-Datei hinterlegt sein, damit sie nicht versehentlich in das Repository
hochgeladen werden. Temporäre Dateien können so auch exkludiert werden.

## Git Integration in IDEs
