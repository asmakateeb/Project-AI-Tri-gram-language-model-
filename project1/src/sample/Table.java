package sample;

public class Table {
    private String word;
    private double count;
    private double length;
    private double prop;
    public Table(String word) {
        super();
        this.word = word;
        String []s = word.split(" ");
        this.length=s.length;
    }
    public Table(String word, double count, double length, double prop) {
        super();
        this.word = word;
        this.count = count;
        this.length = length;
        this.prop = prop;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public double getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public double getLegth() {
        setLegth(word);
        return length;
    }
    public void setLegth(String word) {
        this.word = word;
        String tokens [] = word.split(" ");
        this.length=tokens.length;
    }
    public double getProp() {
        return prop;
    }

    public void setProp(double prop) {
        this.prop = prop;
    }



    @Override
    public String toString() {
        return "Table [word=" + word + " , count=" + count + ", length=" + length + ", prop=" + prop + "]";
    }

}
