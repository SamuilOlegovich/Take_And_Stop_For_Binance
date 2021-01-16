package main.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;


// создаем анимации - пр неправильном вводе будем трясти окном
public class Shake {
    private TranslateTransition translateTransition;

    // ноде это объект который надо потрясти
    public Shake(Node node) {
        // время тряски объекта
        translateTransition = new TranslateTransition(Duration.millis(70), node);
        // отступ от икса
        translateTransition.setFromX(0f);
        // на сколько передвинется относительно нынешней позиции по иксу
        translateTransition.setByX(10f);
        // как много раз он повторит тряску
        translateTransition.setCycleCount(3);
        // что бы не влияло перетаскивание окна
        translateTransition.setAutoReverse(true);
    }

    public void playAnim() {
        translateTransition.playFromStart();
    }
}
