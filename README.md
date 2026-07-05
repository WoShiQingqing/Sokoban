# SokobanGame

This is a JavaFX Sokoban game prepared for the CST210.

## Project Structure

```text
src/
  sokoban/
    MainApp.java
    controller/
    entity/
    model/
    util/
    view/
resources/
  levels/
```

## What Is Implemented Here

- main menu
- level selection
- how-to-play page
- gameplay placeholder page
- basic multi-level support from text files
- integration-ready controller and model scaffolding

## Team Split

- `A`: create package structure, write `MainApp`, connect menu / level select / how-to-play / game pages, keep the placeholder game page runnable, merge everyone's code at the end
- `B`: complete `GameObject`, `Player`, `Box`, `Wall`, `Target`, and `Floor`; add needed fields such as position, walkable state, target state, and display symbol
- `C`: complete `GameMap`; parse level text into objects, move the player, push one box at a time, count moves, reset the current level, and return win status
- `D`: complete `GameView`; draw the board with JavaFX nodes, add keyboard controls, update move/status labels, add restart/back buttons, and show clear visual feedback
- `E`: create `level1.txt` to `level5.txt`; extend `Level` and `LevelLoader` to read `name=` and `difficulty=`; show the difficulty in level select; add a `Level Completed` dialog after a win

## Level File Format

Files inside `resources/levels/` use this map format:

- `#` wall
- ` ` floor
- `.` target
- `$` box
- `@` player
- `*` box on target
- `+` player on target

Optional first line:

```text
name=Level 1
```

## Run Notes

This is a plain Java project. JavaFX SDK is still required on the local machine when compiling or running.

Example commands after installing JavaFX:

```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -d out $(find src -name "*.java")
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -cp out sokoban.MainApp
```
