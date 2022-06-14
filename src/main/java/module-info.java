module de.wolfsline.zusi.anzeige.zusianzeige {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires Zusi3Schnittstelle;


    opens de.wolfsline.zusi.anzeige.zusianzeige to javafx.fxml;
    exports de.wolfsline.zusi.anzeige.zusianzeige;

    opens de.wolfsline.zusi.anzeige.zusianzeige.manometer to Zusi3Schnittstelle;
    opens de.wolfsline.zusi.anzeige.zusianzeige.pzb to Zusi3Schnittstelle;
    opens de.wolfsline.zusi.anzeige.zusianzeige.tasten to Zusi3Schnittstelle;

}