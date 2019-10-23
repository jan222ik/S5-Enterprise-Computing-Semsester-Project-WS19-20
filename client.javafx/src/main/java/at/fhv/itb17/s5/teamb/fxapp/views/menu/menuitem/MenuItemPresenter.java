package at.fhv.itb17.s5.teamb.fxapp.views.menu.menuitem;

import at.fhv.itb17.s5.teamb.fxapp.style.Style;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuItemPresenter implements Initializable {

    @Inject
    private Style style;

    private Background selected;
    private Paint selectedPaint;
    private Background unselected;
    private Paint unselectedPaint;
    private Background hover;
    private Paint hoverPaint;


    @FXML
    private Button menuItem;
    @FXML
    private Button menuIconGlyphHolder;
    @FXML
    private FontAwesomeIconView menuIconGlyph;

    private Runnable onAction;
    private SimpleBooleanProperty isSelected = new SimpleBooleanProperty(false);

    public void setOnClickedAction(Runnable onClickAction) {
        this.onAction = onClickAction;
    }

    public void setDisplayName(String menuName) {
        menuItem.setText(menuName);
    }

    @FXML
    private void onAction(ActionEvent ignored) {
        if (onAction != null) {
            onAction.run();
        }
    }

    public void setSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
        if (isSelected) {
            menuItem.setBackground(selected);
            menuItem.setTextFill(selectedPaint);
            menuIconGlyphHolder.setBackground(selected);
            menuIconGlyph.setFill(selectedPaint);
        } else {
            menuItem.setBackground(unselected);
            menuItem.setTextFill(unselectedPaint);
            menuIconGlyphHolder.setBackground(unselected);
            menuIconGlyph.setFill(unselectedPaint);
        }
    }

    public void setParentWidthProperty(ReadOnlyDoubleProperty parentWidthProperty) {
        parentWidthProperty.addListener((observable, oldValue, newValue) -> {
            menuItem.setPrefWidth((Double) newValue - 2.0);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selected = new Background(new BackgroundFill(style.PRIMARY_PAINT(), null, null));
        selectedPaint = style.ON_PRIMARY_PAINT();
        unselected = new Background(new BackgroundFill(style.BACKGROUND_PAINT(), null, null));
        unselectedPaint = style.ON_BACKGROUND_PAINT();
        hover = new Background(new BackgroundFill(style.SURFACE_PAINT(), null, null));
        hoverPaint = style.ON_SURFACE_PAINT();
        style.hoverBtn(menuIconGlyphHolder, unselected, unselectedPaint, hover, hoverPaint, isSelected);
        style.hoverBtn(menuItem, unselected, unselectedPaint, hover, hoverPaint, isSelected);
    }
}
