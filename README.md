# Crate System


## Commands:

- /crate : Zeigt alle Befehele an
- /crate reload : Lädt die config neu
- /crate list : Listet alle Crates auf
- /crate toggle : De/Aktiviert Crates
- /crate create <Crate> : Erstellt eine neue Crate
- /crate delete <Crate> : Löscht eine Crate
- /crate setitem <Crate> : Setzt das Display Item der Crate
- /crate edit <Crate> : Editiert den Inhalt der Crate
- /crate add <Crate> : Fügt ein Item zur Crate hinzu
- /crate remove <Crate> : Lässt Items entfernen
- /crate show <Crate> : Zeigt alle Items in der Crate an
- /crate setbc <Crate> : Lässt Items bei einem Gewinn broadcasten
- /crate give <Crate> [Anzahl] [Spieler] : §7Gibt dem angegebenen Spieler Crates ( Bei Nutzung durch die Konsole ist der Syntax /crate give <Crate> <Spieler> [Anzahl])
- /crate giveall <Crate> [Anzahl] : Gibt allen Spielern Crates
- /settings : Öffnet die Crate Einstellungen
- /openall : Öffnet alle Crates in der Hand 
  ![grafik](https://user-images.githubusercontent.com/40772868/124969048-87673880-e026-11eb-9c77-3bf423994a62.png)

  
  
Text in <> = Required,
Text in [] = Optional
  
## Weiteres
  In settings Deaktivierbar:
  - Animation
  - Bestätigung
  
  Wenn in Gamemode Creative kann mit Shift + Rechtsklick eine Crate Vorschau gesetzt werden.
  Mit Shift + Linksklick kann diese wieder entfernt werden
  
  ![grafik](https://user-images.githubusercontent.com/40772868/124969077-91893700-e026-11eb-811a-bccd52355931.png)

  
## Permissions
  - /crate : crates.admin
  - /openall : crates.openall.ANZAHL
  
  Bei Openall kann die maximale Anzahl an gleichzeitig öffenbaren Crates angegeben werden. Mögliche Anzahl: 8, 16, 32, 48, 64
  
