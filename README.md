## Was ist Aurora Export?
Aurora Export ist ein (inoffizielles) Script, um Arbeiten aus [Aurora](https://aurora.iguw.tuwien.ac.at/) zu sammeln und als lokale Website oder PDF-Datei abzuspeichern. Damit können geniale Texte für die Nachwelt bewahrt werden und ihr könnt beim Erlangen des Doktortitels mit Nostalgie an diese Wunderschöne Zeit zurückdenken.

## Wie kann ich Aurora Export verwenden?
Ganz allgemein: Man muss nur auf der Startseite (mit dem Newsfeed und Chat) das fertige Script aus export/aurora_export.js ausführen. Dann taucht über dem Newsfeed ein Button auf, der den Exportvorgang einleitet.

Eine einfache Möglichkeit ist es, dieses Code-Snippet in die URL-Leiste des Browsers zu kopieren, wenn man sich gerade auf der richtigen Seite befindet:
```python
javascript:var script = document.createElement("script"); script.setAttribute("src", "https://pdanzinger.github.io/aurora_export/export/aurora_export.js"); document.head.appendChild(script);
```
Achtung! Beim Copy&Pasten löschen Browser gerne aus Sicherheitsgründen das "javascript:" weg, also muss dieses ggf. manuell hinzugefügt werden.

Eine andere (fortgeschrittenere) Möglichkeit wäre, den Code in die Entwickler-Konsole des Browser zu kopieren.

## Wie funktioniert der Export?
Im Hintergrund werden Daten über Challenges und Tasks gesammelt, indem die jeweiligen Links mit Ajax aufgerufen werden. Wenn alle Namen/Angaben/Ausarbeitungen/Bilder abgerufen sind, wird eine HTML-Seite erzeugt, die alle Daten kombiniert darstellt.
Dann gibt es 2 Möglichkeiten des Exports:
- Mit Strg+S kann in vielen Browsern die ganze Seite abgespeichert werden, wobei eine HTML-Datei und ein Unterordner für Bilder und andere Dateien erzeugt wird. Das ist quasi der "Komplette" Export, der die beim Export entstandene Seite vollständig erfasst.
- Mit Strg+P kann die Seite ausgedruckt werden. Entweder wird ein echter Drucker verwendet, oder ein virtueller PDF-Drucker, der die Ausgabe in eine PDF-Datei umleitet.

## Warum sieht der JavaScript-Code so hässlich aus und was sollen diese .kt-Dateien?
JavaScript ist immmer hässlich.
... und genau deswegen wurde für die Entwicklung die Programmiersprache [Kotlin](https://kotlinlang.org/) verwendet und der Code zu JavaScript transpiliert. Für den Build-Prozess wurde [Gradle](https://gradle.org/) verwendet. Um den Code selbst zu kompilieren, muss man nur das Repository klonen, Gradle installieren und mit der Kommandozeile "gradle build" im Hauptverzeichnis ausführen.
