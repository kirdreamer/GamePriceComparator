---
title: "Initial Concept"
author:
    - "Nam Anh Nguyen"
    - "Kirill Bulanov"
date: "Sommersemester 2024"
---	

# Initial Concept

Wir haben eine App bestehend aus:
- Angular Frontend
- Java Backend
- Postgres Datenbank

Frontend und Backend befinden sich aktuell in verschiedenen GitHub Repositories.

Das Frontend und Backend sollen jeweils über eine URL erreichbar sein und remote in der Cloud laufen. Dementsprechend soll es drei verschiedene Umgebungen geben: `develop`, `staging` und `production`.

Develop soll als Testumgebung dienen, um features und bugfixes zu testen, die ggf. zurück gerollt werden können.
Staging dient als 'Generalprobe' bevor die App auf die production Umgebung verteilt werden darf, um nochmal in einer neuen Umgebung zu gewährleisten, dass die neuen features oder bugfixes funktionieren.
Production ist die Live Umgebung, die die App für den Endverbraucher zugänglich macht und reale Daten enthält bzw. damit agiert.

### Software Lifecycle

#### Branches
Neben dem auschließlichen `main` Branch soll es folgende Branches geben, die CI/CD auslösen:
- `feature/*` und `fix/*`
- `release/*`
- `tag/*`

Alle anderen Branch Namen sollen keine CI/CD auslösen.

#### CI/CD

Für das Frontend und Backend Repository wollen wir jeweils CI/CD mit GitHub Actions einführen, die den Software Lifecycle steuern und abbilden sollen. Es soll mehrere stages geben:
- `test`: ausführen von unit tests, ggf. linter
- `build`: app bundle bauen
- `push`: build und push des docker images in die registry
- `deploy`: verteilt App auf die entsprechende Umgebung, abh. vom Branch Namen
    - `feature/*` & `fix/*` -> `develop`
    - `release/*` -> `staging`
    - `tag/*` -> `production`

Wenn `feature`, `fix` und `release` Branches ausgecheckt und gepushed werden, sollen die immer `test`, `build` und `push` stage ausgeführt werden.

Das Ausführen der push stage soll immer gewährleisten, dass das aktuellste Image da ist, um ggf. sofort manuell zu deployen.

Der `deploy` auf die entsprechende Umgebung abh. vom Branch Namen soll immer `manuell` sein - mit der Ausnahme bei einem push auf den main branch, wie z.B. nach einem Merge auf main. Hier soll immer ein automatischer Deploy auf die develop Umgebung stattfinden, damit - egal was gemerged wird - die develop Umgebung immer aktuell ist.


### Nice To Have/Tooling:
<!-- - Dependency Bot (Dependabot für GitHub) -->
- Monitoring (Grafana, Sentry oder Prometheus)
- CodeSniffer/Quality Gate (Sonar)

### Offene Fragen
Wir wissen, wie man Images baut und über die Pipeline in eine entfernte Registry pusht, von wo man es theoretisch wieder herunterladen kann, um daraus Container/Pods zu bauen, die dann konstant auf einer Maschine laufen sollen. Unser Wissen geht leider nicht darüber hinaus bzw. wir wissen nicht wie der letzere Teil funktioniert (Wo man aus Images Container/Pods baut, die auf einem Server/einer Maschine konstant laufen und orchestriert werden müssen).

Deswegen können wir folgende Fragen noch nicht beantworten:
* Where is your infrastructure hosted?
    - die Umgebungen müssen per URL zugänglich sein bzw. eine eigene Domain haben, dewegen nehmen wir an, dass es technisch nicht möglich ist die Umgebungen lokal aufzusetzen - daher denken wir, dass es wohl besser ist, die 3 Umgebungen in der Cloud über Google (oder im Edu-Cluster?) laufen zu lassen
* How do you allocate the underlying resources hosting your environments?
* How is everything provisioned?
* How do you set up all your services? Which of them do you actually need to set up? If some of them don’t need to be set up, why?
    - Secrets von Github werden verwendet, um Umgebungsvariablen zu definieren
    - Kubernetes wird zur Konfiguration von Clustern verwendet
* What about the persistence layer (database)?

### Stack

- GitHub (+ Actions)
- Kubernetes
- Docker