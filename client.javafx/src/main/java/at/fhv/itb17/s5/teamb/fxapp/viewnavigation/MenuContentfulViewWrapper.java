package at.fhv.itb17.s5.teamb.fxapp.viewnavigation;

import at.fhv.itb17.s5.teamb.fxapp.viewmodel.ViewModel;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.MenuPresenter;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.menuitem.MenuItemPresenter;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.menuitem.MenuItemView;
import at.fhv.itb17.s5.teamb.util.LogMarkers;
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Stack;

public class MenuContentfulViewWrapper<T extends ViewModel> implements NavigationStackActions<T> {

    private static final Logger logger = LogManager.getLogger(MenuContentfulViewWrapper.class);

    private T viewModel;
    private Stack<ContentView<T>> navigationStack = new Stack<>();
    private String menuName;
    private String title;
    private MenuPresenter menuPresenter;
    private MenuItemPresenter menuItemPresenter;

    public MenuContentfulViewWrapper(ContentView<T> view, T viewModel, String menuName, String title, MenuPresenter menuPresenter) {
        this.viewModel = viewModel;
        if (viewModel == null) {
            logger.error("Model not present");
        }
        push(view);
        this.menuName = menuName;
        this.title = title;
        this.menuPresenter = menuPresenter;
    }

    public MenuItemView createMenuItemView(Runnable onClickAction, ReadOnlyDoubleProperty parentWidthProperty) {
        MenuItemView menuItemView = new MenuItemView();
        menuItemPresenter = (MenuItemPresenter) menuItemView.getPresenter();
        menuItemPresenter.setOnClickedAction(onClickAction);
        menuItemPresenter.setDisplayName(menuName);
        menuItemPresenter.setParentWidthProperty(parentWidthProperty);
        return menuItemView;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void popToRoot() {
        for (int i = navigationStack.size(); i > 1; i--) {
            popLast();
        }
    }

    @Override
    public void popLast() {
        if (navigationStack.size() > 1) {
            ContentView<T> pop = navigationStack.pop();
            pop.preDestroy(viewModel);
        } else {
            logger.warn(LogMarkers.UI_NAV, "Tried to pop root element of stack");
        }
    }

    @Override
    public void push(ContentView<T> view) {
        navigationStack.push(view);
        if (viewModel == null) {
            logger.error(LogMarkers.UI_NAV, "Model not present: METHOD PUSH");
        }
        view.onCreate(viewModel, this);
    }

    @Override
    public void showTOS() {
        ContentView<T> contentView = navigationStack.peek();
        menuPresenter.setContentView(contentView.getView());
        contentView.onReturned(viewModel);
    }

    @Override
    public void changeToMenuItem(int number, Runnable... onError) {
        try {
            menuPresenter.switchMenuContentfulView(number);
        } catch (IndexOutOfBoundsException e) {
            logger.error(LogMarkers.UI_NAV, "Cannot change to menuitem {}", number);
            if (onError != null) {
                Arrays.asList(onError).forEach(Runnable::run);
            }
        }
    }

    public void beforeMenuSwitch() {
        navigationStack.peek().beforeMenuSwitch(viewModel);
    }

    public void isCurrentMenuItem(boolean isCurrent) {
        menuItemPresenter.setSelected(isCurrent);
    }
}