package task4;

import java.io.*;

public class ClientHistory {
    private File file;
    private final int LIMITOFLINES = 100;

    public ClientHistory(String login) {

        File dir = new File(System.getProperty("user.dir")
                .replace("\\", "/")
//                .replace("/", "//") + "//history//");
                + "/history/");
        if (!dir.exists()) dir.mkdir();
        file = new File(dir, String.format("%s_history.txt", login.toLowerCase()));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getArchive() {
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {

            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            long countOfLines = 0;
            while (lnr.readLine() != null) {
                countOfLines++;
            }

            long extraLines = 0;
            if (countOfLines > LIMITOFLINES) extraLines = countOfLines - LIMITOFLINES;

            br = new BufferedReader(new FileReader(file));
            String st = "";
            long count = 0;
            while (((st = br.readLine()) != null)) {
                if (count >= extraLines) sb.append(st + "\n");
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void addIntoArchive(String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}