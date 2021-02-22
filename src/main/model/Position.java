package main.model;

public enum Position {
    SELL_TRAILING_STOP_COMPLETED_POSITION,  // сработал трайлирующий стоп - продали
    BUY_TRAILING_STOP_COMPLETED_POSITION,   // сработал трайлирующий стоп - купили
    SELL_TAKE_COMPLETED_POSITION,           // вначале продали, потом сработал тейк, стратегия остановлена
    SELL_STOP_COMPLETED_POSITION,           // вначале продали, потом сработал стоп, стратегия остановлена
    BUY_STOP_COMPLETED_POSITION,            // вначале купили, потом сработал стоп, стратегия остановлена
    BUY_TAKE_COMPLETED_POSITION,            // вначале купили, потом сработал тейк, стратегия остановлена
    SELL_TAKE_OR_STOP_POSITION,             // был сделан вход SELL в рынок - ждем отработки тейка или стопа
    BUY_TAKE_OR_STOP_POSITION,              // был сделан вход BUY в рынок - ждем отработки тейка или стопа
    SELL_COMPLETED_POSITION,                // выполнен вход в сел и стратегия остановлена
    BUY_COMPLETED_POSITION,                 // выполнен вход в бай и стратегия остановлена
    TRAILING_STOP_POSITION,                 // включке трайлирующий стоп
    STARTED_POSITION,   // ордер запущен, самое начало, ни одной сделки по нему не произошло состояние еще не определено
    EXAMPLE_POSITION,   // просто ордер пример - шаблон, ни где не участвует и в листы не добавляется
    NORMAL_POSITION,    // ордер запущен, самое начало, ни одной сделки по нему не произошло есть тейки и стопы
    ERROR_POSITION,
    EASY_POSITION,      // ордер запущен, самое начало, ни одной сделки по нему не произошло нет тейков и стопов
}
