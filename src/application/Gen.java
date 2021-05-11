package application;

import java.util.Random;

public class Gen {
    /**
     * Długość genu
     */
    public int genLength;

    int[] genTab;
    double min;
    double max;
    double value;

    /**
     * Konstruktor genu.
     * @param d Dokładność
     * @param min Wartość ai
     * @param max Wartość bi
     */
    public Gen(int d, double min, double max) {
        this.min = min;
        this.max = max;
        this.genLength = log((max - min) * Math.pow(10.0D, (double)d), 2);
        this.genTab = new int[this.genLength];

    }

    /**
     * Wypełnienie genu losowymi wartościami
     * @return Czy udało się poprawnie wypelnić gen
     */
    boolean generateGen() {
        do{
            for(int i = 0; i < this.genLength; ++i) {
                this.genTab[i] = getRandomBit();
            }

            this.value = this.decodeGen();
        }while (!(this.value <= this.max && this.value >= this.min));
        return true;
    }

    /**
     * Ustawienie konkretnego bitu w genie, ustaloną wartością
     * @param n Numer bitu
     * @param value Nowa wartość bitu
     */
    void setBit(int n, int value)
    {
        genTab[n] = value;
    }

    /**
     * Pobranie wartości bitu w danym miejscu
     * @param n Numer miejsca do pobrania wartości
     * @return Wartość genu w danym miejscu
     */
    int getBit(int n)
    {
        return genTab[n];
    }

    /**
     * Dekodowanie genu - zwrócenie jego dziesiętnej wartości
     * @return Wartość genu w systemie dziesiętnym
     */
    public double decodeGen() {
        double c = (this.max - this.min) / (Math.pow(2.0D, (double)this.genLength) - 1.0D);
        int decode = Integer.parseInt(this.convertGenToString(), 2);
        double x = this.min + (double)decode * c;
        return x;
    }

    /**
     * Przekonwertowanie genu (tablicy int'ów) na string
     * @return Gen w formie string zamiast int[]
     */
    public String convertGenToString() {
        String ready = "";

        for(int i = 0; i < this.genTab.length; ++i) {
            ready = ready + this.genTab[i];
        }

        return ready;
    }

    /**
     * Wylosowanie losowej wartości w zakresie 0-1
     * @return Wartość 0 lub 1
     */
    public static int getRandomBit() {
        Random r = new Random();
        return (int)Math.round(r.nextDouble());
    }

    /**
     * Obliczenie logarytmu
     * @param d
     * @param b
     * @return
     */
    public static int log(double d, int b) {
        return (int)Math.ceil(Math.log(d) / Math.log((double)b));
    }
}
