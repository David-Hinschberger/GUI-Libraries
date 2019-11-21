## Java GUI
The current prototype is written with JDK13 in mind, as it is the current most 
recent available production java SDK.  Keep in mind that JavaFX is not included
by default with JDK 13, so there is some configuration required to get JavaFX
included in a project.

###How to include JavaFX
* Download from [here](https://gluonhq.com/products/javafx/) and extract to a safe
location to be referenced.
* Compile with `--module-path "[PATH TO JAVAFX\lib]" --add-modules=javafx.controls,javafx.fxml`
* Run with `--module-path "[PATH TO JAVAFX\lib]" --add-modules=javafx.controls,javafx.fxml`
