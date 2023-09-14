import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final int WIN_COUNT = 4; // Выигрышная комбинация
    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человек
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля

    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля


    public static void main(String[] args) {
        field = new char[5][];

        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да) или (N - нет): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
            else if (scanner.next().equalsIgnoreCase("N")) {
                System.out.println("Игра окончена. Спасибо за игру!");
                break;
            }
        }
    }

    /**
     * Инициализация объектов игры
     */
    private static void initialize(){

        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                field[x][y] = DOT_EMPTY;
            }
        }

    }

    /**
     * Отрисовка игрового поля
     *
     *     +-1-2-3-
     *     1|*|X|0|
     *     2|*|*|0|
     *     3|*|*|0|
     *     --------
     */
    private static void printField(){
        System.out.print("+");
        for (int x = 0; x < fieldSizeX * 2 + 1; x++){
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();

        for (int x = 0; x < fieldSizeX; x++){
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++){
            System.out.print("-");
        }
        System.out.println();

    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn(){
        int x, y;

        do {

            while (true){
                System.out.print("Введите координату хода X (от 1 до "+fieldSizeX+"): ");
                if (scanner.hasNextInt()){
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }

            while (true){
                System.out.print("Введите координату хода Y (от 1 до  "+fieldSizeY+"): ");
                if (scanner.hasNextInt()){
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }
    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }
    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность игрового поля)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Обработка хода компьютера
     */
    private static void aiTurn(){
        int x, y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка состояния игры
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s){
        if (checkWinV2(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }

    /**
     * Проверка победы
     * @param c игрок
     * @return
     */
    private static boolean checkWinV2(char c){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                if (check1(x, y, c)) return true;
            }
        }
        return false;
    }

    private static boolean check1(int x, int y, char player) {
        int count = 1;
        int i = 1;
        // Проверка по диагонали вверх вправо
        while (x - i >= 0 && y + i < fieldSizeY && field[x - i][y + i] == player) {
            count++;
            i++;
        }
        i = 1;
        while (x + i < fieldSizeX && y - i >= 0 && field[x + i][y - i] == player) {
            count++;
            i++;
        }
        if (count >= WIN_COUNT) {
            return true;
        }
        count = 1;
        i = 1;
        // Проверка по диагонали вниз вправо
        while (x + i < fieldSizeX && y + i < fieldSizeY && field[x + i][y + i] == player) {
            count++;
            i++;
        }
        i = 1;
        while (x - i >= 0 && y - i >= 0 && field[x - i][y - i] == player) {
            count++;
            i++;
        }
        if (count >= WIN_COUNT) {
            return true;
        }
        count = 1;
        i = 1;
        // Проверка по горизонтали вправо
        while (y + i < fieldSizeY && field[x][y + i] == player) {
            count++;
            i++;
        }
        i = 1;
        while (y - i >= 0 && field[x][y - i] == player) {
            count++;
            i++;
        }
        if (count >= WIN_COUNT) {
            return true;
        }
        count = 1;
        i = 1;
        // Проверка по вертикали вниз
        while (x + i < fieldSizeX && field[x + i][y] == player) {
            count++;
            i++;
        }
        i = 1;
        while (x - i >= 0 && field[x - i][y] == player) {
            count++;
            i++;
        }
        if (count >= WIN_COUNT) {
            return true;
        }
        return false;
    }


    /**
     * Проверка на ничью
     * @return
     */
    private static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

}
