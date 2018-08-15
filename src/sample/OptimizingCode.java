package sample;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;


class OptimizingCode {

    /**
     * this is the entry point of the program.
     * @return
     * @param chapterMarks
     */
    public static int start(HashMap<Integer, Integer> chapterMarks) {

        conn = MySQLConnect.connectDB();
        try {
            statement = conn.createStatement();
            System.setOut(new PrintStream(new FileOutputStream("output.txt")));
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * iss hashMap me sabh chapter k numbers and uska total chapter weightage store kiya hai...!!
         */
        for (int chapterNumber : chapterMarks.keySet()) {
            int marks = chapterMarks.get(chapterNumber);
            chapterWeight.put(chapterNumber, marks);
        }
//        chapterWeight.put(1, 22);
//        chapterWeight.put(2, 20);
//        chapterWeight.put(3, 24);
//        chapterWeight.put(4, 22);
//        chapterWeight.put(5, 12);

//        ArrayList<Integer> c1 = new ArrayList<>();
//        ArrayList<Integer> c2 = new ArrayList<>();
//        ArrayList<Integer> c3 = new ArrayList<>();
//        ArrayList<Integer> c4 = new ArrayList<>();
//        ArrayList<Integer> c5 = new ArrayList<>();
        ArrayList<ArrayList> chapters = new ArrayList<>();
        
        for (int i=0; i<chapterMarks.size(); i++) chapters.add(new ArrayList<Integer>());
//        chapters.add(c1);
//        chapters.add(c2);
//        chapters.add(c3);
//        chapters.add(c4);
//        chapters.add(c5);*-----

        chapterWeight.keySet().forEach(i -> {
            if (i < 4) {
                if (chapterWeight.get(i) % 4 != 0) {
                    chapterWeight.replace(i, chapterWeight.get(i) - 6);

                    //yaha pe -1 isliye dala hai coz chapter number 1 sse start hota hai and chapters ka ArrayList ka
                    // 0th position me 1st chapter ka data store krna tha so...same goes for all chapters.get(i-1)
                    chapters.get(i - 1).add(6);
                }
                chapterWeight.replace(i, chapterWeight.get(i) - 8);
                chapters.get(i - 1).add(8);

                while (chapterWeight.get(i) != 0) {
                    chapterWeight.replace(i, chapterWeight.get(i) - 4);
                    chapters.get(i - 1).add(4);
                }
            } else if (chapterWeight.get(i) % 4 != 0) {
                chapterWeight.replace(i, chapterWeight.get(i) - 6);
                chapters.get(i - 1).add(6);
                while (chapterWeight.get(i) != 0) {
                    chapterWeight.replace(i, chapterWeight.get(i) - 4);
                    chapters.get(i - 1).add(4);
                }
            } else
                while (chapterWeight.get(i) != 0) {
                    chapterWeight.replace(i, chapterWeight.get(i) - 4);
                    chapters.get(i - 1).add(4);
                }
        });
        System.out.println(chapters);

        for (int i = 0; i < chapters.size(); i++) {
            for (int j = 0; j < chapters.get(i).size(); j++) {
                int chapter = i + 1;
                int marks = (int) ((chapters.get(i).get(j)));

                try {
                    performOperation(chapter, marks);
                } catch (Exception e) {
                    System.out.println("exception " + e);
                    e.printStackTrace();
                }
            }
        }

        int ppr = 0;
        for (String key : generatedQuestion7.keySet()) {
            System.out.println(key + "      :      " + generatedQuestion7.get(key) + " : "  /* + mapObj.get(key)*/);
            ppr += generatedQuestion7.get(key);
        }
        System.out.println(ppr);

        int alTotal = 0;
        for (int i = 0; i < marksList.size(); i++) {
            System.out.println("Question   =   " + questionList.get(i));
            System.out.println("Mark       =       " + marksList.get(i));
            alTotal += marksList.get(i);
        }
        System.out.println("List Total = " + alTotal);

        return ppr;
    }// end of public static void main(String[] args)


    /**
     *
     * @param chapter
     * @param mark
     * @throws Exception
     */
    private static void performOperation(int chapter, int mark) throws Exception {

        int perQuestionValue = mark;

        int currentChapter =  chapter ;

//        perQuestionValue = mark;
        /**
         * yaha pr select from current chapter where marks is perQuestionValue and priority is max
         * joh resultSet aaya usme se ek random question uthaunga then usko mai dusre questionPaper wale hashMap me daaldunga...!!
         * joh add kiya hai uska priority 2 se decreasse and baki k log k priority 0.5 se increment agar usnka priority max naii reach krta tbh tk
         *
         */

        String sql = "SELECT MAX(priority) FROM java WHERE marks = " + perQuestionValue + " AND chapter_no = " + currentChapter /*" +  and priority = max(priority)"*/;
        ResultSet rs = statement.executeQuery(sql);
        int maxPriority = -1;
        while(rs.next())
            maxPriority = rs.getInt(1);

        int whileCounter = 0;

        sql = "UPDATE  java SET priority = priority + 1 WHERE priority < 10 && marks = " + perQuestionValue  + " AND chapter_no = " + currentChapter;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate(sql);

        sql = "SELECT * FROM java WHERE marks = " + perQuestionValue + " and priority = " + maxPriority  + " AND chapter_no = " + currentChapter ;
        rs = statement.executeQuery(sql);

        int rowcount = 0;
        if (rs.last()) {
            rowcount = rs.getRow();
            rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }
        int randomNumber = (generateRandomNumber(rowcount) + generateRandomNumber(rowcount)) / 2;
        whileCounter = 0;
        while (rs.next()) {
            //yaha pe randomNumber - 1 isliye kiya coz woh hamsha joh random number milta tha uske next wale question ko print krta tha yeh rs.next() function k property k wajhe se ho raha hai...!!
            if (whileCounter == randomNumber - 1) {
                int id = rs.getInt("questionid");
                System.out.println("Question           =" + rs.getString("questions"));
                System.out.println("marks           =" + rs.getString("marks"));

                if (genQP(rs.getString("questions"), perQuestionValue)) {
                    sql = "UPDATE java SET priority = priority - 3 WHERE questionid=" + id + " AND chapter_no = " + currentChapter;
                    pstmt.executeUpdate(sql);
                }
                if (marksList.add(perQuestionValue) && questionList.add(rs.getString("questions"))) {
                    sql = "UPDATE java SET priority = priority - 3 WHERE questionid=" + id + " AND chapter_no = " + currentChapter;
                    pstmt.executeUpdate(sql);
                }
            }
            whileCounter += 1;
        }// rs.next() wale while ka end...!!
    }

    private static int generateRandomNumber(int max) {
        int range = (max - 1) + 1;
        return (int) (Math.random() * range) + 1;
    }

    private static boolean genQP(String question, int marks) {
//        yaha pr woh 20 isliye pass kiya hai coz total numbers of question 20 he rahega isliye!!
        if (generatedQuestion1.size() >= 20 && !generatedQuestion1.containsKey(question)) {
            return false;
        } else {
            generatedQuestion7.put(question, marks);
            return true;
        }
    }

    //  Variable Declarations...!!
    static Statement statement;
    static Connection conn = null;
    static SortedMap<Integer, Integer> chapterWeight = new TreeMap<>();
    static HashMap<String, Integer> generatedQuestion1 = new HashMap<>();
    static HashMap<String, Integer> generatedQuestion7 = new HashMap<>();
    static ArrayList<Integer> marksList = new ArrayList<>();
    static ArrayList<String> questionList = new ArrayList<>();

}// end of Class Main!!!!