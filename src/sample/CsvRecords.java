package sample;

public class CsvRecords {
    String question;
    String marks;
    String chapterNo;

    public CsvRecords(String question, String marks, String chapterNo) {
        this.question = question;
        this.marks = marks;
        this.chapterNo = chapterNo;
    }


    public String getQuestion() {
        return question;
    }

    public String getMarks() {
        return marks;
    }

    public String getChapterNo() {
        return chapterNo;
    }
}
