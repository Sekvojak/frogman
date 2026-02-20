@echo off
java --module-path target/lab02-0.0.1-SNAPSHOT.jar;target/libs --add-modules javafx.controls,javafx.fxml -m lab01/lab.App
pause
