# Порты и адаптеры

## Domain

`model`: Tank, Bullet, Tree, GameObject, GameField, Direction, состояния танка в `model/state`.

## Application

`control`: Command, MoveCommand, ShootCommand, ToggleHealthBarsCommand, ControlStrategy.
`level`: Level, LevelGenerator.

## Infrastructure

GameDesktopLauncher, весь `graphics`, KeyboardStrategy, FileLevelGenerator, `util`.

## Порты и их адаптеры

- FieldListener - уровень сообщает, что объекты добавились или удалились. Адаптер: GraphicsLevel
- LevelGenerator - откуда берется уровень. Адаптеры: FileLevelGenerator, RandomLevelGenerator
- ControlStrategy - откуда берутся команды. Адаптеры: KeyboardStrategy, RandomStrategy
- Command, Movable, Shooter - через них команды управляют объектами модели

## Чего не хватает

1. Игровой цикл (создать команды, выполнить, live) написан в GameDesktopLauncher, а это libGDX. Нужен класс приложения, например Game с методом tick, а лаунчер бы только вызывал его.
2. Модель зависит от libGDX: GridPoint2, MathUtils и GdxGameUtils.continueProgress. Для чистого domain нужны свои координаты и расчет прогресса.
3. ToggleHealthBarsCommand из application зависит от HealthBarToggle из graphics. Нужен порт в application, который реализует графика.
4. Нет адаптера для рекомендательного движка AI. Его можно сделать еще одной реализацией ControlStrategy.
