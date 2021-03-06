package at.fhv.itb17.s5.teamb.fxapp.views.content.msgprod;

import at.fhv.itb17.s5.teamb.dtos.MsgTopicDTO;
import at.fhv.itb17.s5.teamb.fxapp.util.NotificationsHelper;
import at.fhv.itb17.s5.teamb.fxapp.viewmodel.MsgTopicVM;
import at.fhv.itb17.s5.teamb.fxapp.viewnavigation.ContentfulViewLifeCycle;
import at.fhv.itb17.s5.teamb.fxapp.viewnavigation.NavigationStackActions;
import at.fhv.itb17.s5.teamb.fxapp.views.menu.ApplicationMenuViews;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.stream.Collectors;

public class MsgProdPresenter implements ContentfulViewLifeCycle<MsgTopicVM> {

    private static final Logger logger = LogManager.getLogger(MsgProdPresenter.class);

    @FXML
    private Button backBtn;
    @FXML
    private Button impRss;
    @FXML
    private ChoiceBox<String> topicCB;
    @FXML
    private TextField msgHeaderTE;
    @FXML
    private TextArea msgBodyTE;
    @FXML
    private Button discardBtn;
    @FXML
    private Button publishBtn;

    private HashMap<String, MsgTopicDTO> topicChoices = new HashMap<>();

    @Override
    public void onCreate(MsgTopicVM viewModel, NavigationStackActions<MsgTopicVM> navActions) {
        backBtn.setOnAction(e -> navActions.changeToMenuItem(ApplicationMenuViews.MSG_CONSUMER_VIEW, true));
        impRss.setOnAction(e -> navActions.changeToMenuItem(ApplicationMenuViews.MSG_PROD_RSS_VIEW, true));
        discardBtn.setOnAction(e -> this.clear());
        publishBtn.setOnAction(e -> this.publish(viewModel));
    }

    private void publish(MsgTopicVM viewModel) {
        logger.debug("Publish item:");
        String header = msgHeaderTE.getText();
        String body = msgBodyTE.getText();
        String topic = topicCB.getValue();
        logger.debug("topic = {}", topic);
        logger.debug("header = {}", header);
        logger.debug("body = {}", body);
        boolean succ = viewModel.publishMsg(topicChoices.get(topic), header, body);
        if (!succ) {
            NotificationsHelper.inform("Message", "Could not publish message");
        } else {
            NotificationsHelper.inform("Message", "Message was published");
            this.clear();
        }
    }

    private void clear() {
        msgHeaderTE.clear();
        msgBodyTE.clear();
        if (!topicCB.getItems().isEmpty()) {
            topicCB.getSelectionModel().select(0);
        }
    }

    @Override
    public void onReturned(MsgTopicVM viewModel) {
        topicCB.getItems().clear();
        topicCB.getItems().addAll(viewModel.getAllTopics().stream().filter(topic -> !(topic.getName().equals("TEST"))).map(topic -> {
            topicChoices.put(topic.getName(), topic);
            return topic.getName();
        }).collect(Collectors.toList()));
        if (!topicCB.getItems().isEmpty()) {
            topicCB.getSelectionModel().select(0);
        }
    }
}
