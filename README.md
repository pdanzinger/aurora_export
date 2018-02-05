## Was ist Aurora Export?
Aurora Export ist ein (inoffizielles) Script, um Arbeiten aus [Aurora](https://aurora.iguw.tuwien.ac.at/) zu sammeln und als lokale HTML-Seite oder PDF-Datei abzuspeichern. Dadurch können eure Meisterwerke für die Nachwelt bewahrt werden und ihr könnt eines Tages beim Erlangen des Doktortitels mit Nostalgie an diese Wunderschöne Zeit zurückdenken.

## Wie kann ich Aurora Export verwenden?
Ganz allgemein: Man muss nur auf der Aurora-Startseite (die mit dem Newsfeed und Chat) das fertige Javascript aus export/aurora_export.js ausführen. Dann taucht über dem Newsfeed ein Button auf, der den Exportvorgang einleitet.

Das kann beispielsweise erreicht werden, indem auf der Startseite folgender Code in die URL-Leiste eingegeben wird:
```python
javascript:$.getScript("https://pdanzinger.github.io/aurora_export/export/aurora_export.js");
```
Achtung! Beim Copy&Pasten löschen Browser gerne aus Sicherheitsgründen das "javascript:" weg, also muss dieses ggf. manuell hinzugefügt werden.

Eine andere (fortgeschrittenere) Möglichkeit wäre, den Code ohne das "javascript:"-Präfix in die Entwickler-Konsole des Browsers (erreichbar meistens mit der F12-Taste) zu kopieren.

## Wie funktioniert der Export?
Im Hintergrund werden Daten über Challenges und Tasks gesammelt, indem die jeweiligen Links mit Ajax aufgerufen werden. Wenn alle Namen/Angaben/Ausarbeitungen/Bild-URLs abgerufen sind, wird eine HTML-Seite erzeugt, die alle Daten kombiniert darstellt.
Dann gibt es 2 Möglichkeiten des Exports:
- Mit Strg+S kann in vielen Browsern die ganze Seite abgespeichert werden, wobei eine HTML-Datei und ein Unterordner für Bilder und andere Dateien erzeugt wird. Das ist quasi der "Komplette" Export, der die beim Export entstandene Seite vollständig erfasst.
- Mit Strg+P kann die Seite ausgedruckt werden. Entweder wird ein echter Drucker verwendet, oder ein virtueller PDF-Drucker, der die Ausgabe in eine PDF-Datei umleitet.

## Warum sieht der JavaScript-Code so hässlich aus und was sollen diese .kt-Dateien?
Für die Entwicklung wurde die Programmiersprache [Kotlin](https://kotlinlang.org/) verwendet, der Code wird zu JavaScript transpiliert. Für den Build-Prozess kommt [Gradle](https://gradle.org/) zum Einsatz. Um den Code selbst zu kompilieren, muss man das Repository klonen, Gradle installieren und mit der Kommandozeile den Befehl "gradle build" im Hauptverzeichnis ausführen. Dann sollte automatisch die Datei in "export/aurora_export.js" (neu) generiert werden.
