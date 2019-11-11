package at.fhv.itb17.s5.teamb.fxapp;

import at.fhv.itb17.s5.teamb.fxapp.data.SearchService;
import at.fhv.itb17.s5.teamb.fxapp.data.mock.MockSearchServiceImpl;
import at.fhv.itb17.s5.teamb.fxapp.data.rmi.RMISearchServiceImpl;
import at.fhv.itb17.s5.teamb.fxapp.style.Style;
import at.fhv.itb17.s5.teamb.fxapp.views.login.LoginPresenter;
import at.fhv.itb17.s5.teamb.fxapp.views.login.LoginView;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.MenuPresenter;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.MenuView;
import at.fhv.itb17.s5.teamb.util.ArgumentParser;
import at.fhv.itb17.s5.teamb.util.LogMarkers;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ApplicationMain extends Application {

    private static final Logger logger = LogManager.getLogger(ApplicationMain.class);
    private ArgumentParser args;

    private Runnable createMenu;

    @Override
    public void init() throws Exception {
        super.init();
        args = new ArgumentParser();
        args.parseArgs(getParameters().getRaw(), '=');
    }

    public void start(Stage primaryStage) throws Exception {
        Thread.currentThread().setName("FX Main");
        Injector.setModelOrService(Style.class, new Style());
        SearchService service = (args.containsKeyword("-mock")) ?
                new MockSearchServiceImpl() : new RMISearchServiceImpl("localhost", 2345);
        Injector.setModelOrService(SearchService.class, service);
        boolean withLogin = args.containsKeyword("-login");

        Runnable createLogin = () -> generateLogin(primaryStage);
        createMenu = () -> generateMenu(primaryStage);

        if (withLogin) {
            createLogin.run();
        } else {
            createMenu.run();
        }
        showStage(primaryStage);
        logger.info(LogMarkers.APPLICATION, "Application Started");
    }

    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    private MenuView generateMenu(Stage primary) {
        MenuView menuView = new MenuView();
        Scene scene = new Scene(
                menuView.getView(),
                Double.parseDouble(args.getArgValue("-width", "800")),
                Double.parseDouble(args.getArgValue("-height", "400"))
        );
        ((MenuPresenter) menuView.getPresenter()).setLogoutCallback(() -> generateLogin(primary));
        showScene(primary, scene, "#placeholder");
        return menuView;
    }

    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    private LoginView generateLogin(@NotNull Stage primary) {
        LoginView loginView = new LoginView();
        ((LoginPresenter) loginView.getPresenter()).setNextSceneCallback(createMenu);
        Scene scene = new Scene(loginView.getView(), 600D, 300D);
        showScene(primary, scene, "Login");
        return loginView;
    }

    private void showScene(Stage primary, Scene scene, String title) {
        primary.setScene(scene);
        primary.setTitle(title);
    }

    private void showStage(@NotNull Stage primary) throws FileNotFoundException {
        primary.initStyle(
                args.containsKeyword("-decorated") ? StageStyle.DECORATED : StageStyle.UNDECORATED
        );
        File file = new File("client.javafx/src/main/resources/icon.png");
        primary.getIcons().add(new Image(new FileInputStream(file)));
        primary.show();
        primary.toFront();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info(LogMarkers.APPLICATION, "Application Stopped Gracefully");
    }
}
