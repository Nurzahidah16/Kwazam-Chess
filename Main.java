//ALL
import Controller.*;
import View.*;

public class Main { 

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        new LauncherController(launcher); 
        launcher.setVisible(true);
    }
}
